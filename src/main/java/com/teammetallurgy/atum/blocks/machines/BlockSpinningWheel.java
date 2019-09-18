package com.teammetallurgy.atum.blocks.machines;

import com.teammetallurgy.atum.api.recipe.RecipeHandlers;
import com.teammetallurgy.atum.api.recipe.spinningwheel.ISpinningWheelRecipe;
import com.teammetallurgy.atum.blocks.machines.tileentity.TileEntitySpinningWheel;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.utils.StackHelper;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockSpinningWheel extends BlockContainer {
    private static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyInteger SPOOL = PropertyInteger.create("spool", 0, 3);
    private static final PropertyBool WHEEL = PropertyBool.create("wheel");

    public BlockSpinningWheel() {
        super(Material.WOOD);
        this.setHardness(1.2F);
        this.setHarvestLevel("axe", 0);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(SPOOL, 0).withProperty(WHEEL, false));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull World world, int meta) {
        return new TileEntitySpinningWheel();
    }

    @Override
    public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof TileEntitySpinningWheel) {
            TileEntitySpinningWheel spinningWheel = (TileEntitySpinningWheel) tileEntity;
            if (player.isSneaking()) {
                StackHelper.giveItem(player, EnumHand.MAIN_HAND, spinningWheel.getStackInSlot(0).copy());
                StackHelper.giveItem(player, EnumHand.MAIN_HAND, spinningWheel.getStackInSlot(1).copy());
                spinningWheel.decrStackSize(0, spinningWheel.getInventoryStackLimit());
                spinningWheel.decrStackSize(1, spinningWheel.getInventoryStackLimit());
                spinningWheel.input = new NBTTagCompound();
                spinningWheel.rotations = 0;
                spinningWheel.wheel = false;
                world.setBlockState(pos, world.getBlockState(pos).withProperty(SPOOL, 0), 2);
                spinningWheel.markDirty();
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tileEntity = world.getTileEntity(pos);
        ItemStack heldStack = player.getHeldItem(hand);

        if (tileEntity instanceof TileEntitySpinningWheel) {
            TileEntitySpinningWheel spinningWheel = (TileEntitySpinningWheel) tileEntity;

            if (facing == state.getValue(FACING)) {
                this.output(world, pos, player, spinningWheel);
            } else {
                if (facing == EnumFacing.UP) {
                    if (spinningWheel.isEmpty() && !heldStack.isEmpty() && spinningWheel.isItemValidForSlot(0, heldStack) && state.getValue(SPOOL) < 3) {
                        ItemStack copyStack = new ItemStack(heldStack.getItem(), 1, heldStack.getMetadata());
                        boolean canInsert = false;

                        if (spinningWheel.input.isEmpty()) {
                            spinningWheel.input = copyStack.writeToNBT(new NBTTagCompound());
                        }
                        ItemStack inputStack = new ItemStack(spinningWheel.input);
                        if (StackHelper.areStacksEqualIgnoreSize(inputStack, heldStack)) {
                            canInsert = true;
                        } else if (!inputStack.isEmpty()) {
                            if (world.isRemote) {
                                player.sendStatusMessage(new TextComponentTranslation("chat.atum.spinningWheel.recipeInProgress", inputStack.getDisplayName()).setStyle(new Style().setColor(TextFormatting.RED)), true);
                                world.playSound(player, new BlockPos(player), SoundEvents.ENTITY_HORSE_SADDLE, SoundCategory.BLOCKS, 0.8F, 1.0F);
                            }
                        }

                        if (canInsert) {
                            spinningWheel.setInventorySlotContents(0, copyStack);
                            if (!player.isCreative()) {
                                heldStack.shrink(1);
                            }
                            spinningWheel.markDirty();
                        }
                    } else if (!spinningWheel.input.isEmpty()) {
                        ItemStack input = new ItemStack(spinningWheel.input);
                        for (ISpinningWheelRecipe spinningWheelRecipe : RecipeHandlers.spinningWheelRecipes) {
                            if (spinningWheelRecipe.isValidInput(input)) {
                                if (!spinningWheel.isEmpty()) {
                                    spinningWheel.wheel = !spinningWheel.wheel;
                                }
                                if (spinningWheelRecipe.getRotations() == spinningWheel.rotations && state.getValue(SPOOL) < 3 && !spinningWheel.isEmpty()) {
                                    world.setBlockState(pos, state.cycleProperty(SPOOL), 2);
                                    spinningWheel.decrStackSize(0, 1);
                                    spinningWheel.rotations = 0;
                                    spinningWheel.wheel = false;
                                } else if (!spinningWheel.wheel && state.getValue(SPOOL) < 3 && !spinningWheel.isEmpty()) {
                                    spinningWheel.rotations += 1;
                                    if (world.isRemote) {
                                        world.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_LADDER_FALL, SoundCategory.BLOCKS, 0.55F, 0.4F, true);
                                    }
                                }
                                if (state.getValue(SPOOL) == 3) {
                                    ItemStack copyOutput = spinningWheelRecipe.getOutput();
                                    ItemStack output = new ItemStack(copyOutput.getItem(), copyOutput.getCount(), copyOutput.getMetadata());
                                    spinningWheel.setInventorySlotContents(1, output);
                                    spinningWheel.input = new NBTTagCompound();
                                }
                            }
                        }
                        spinningWheel.markDirty();
                    }
                }
            }
        }
        return true;
    }

    public void output(World world, BlockPos pos, @Nullable EntityPlayer player, TileEntitySpinningWheel spinningWheel) {
        IBlockState state = world.getBlockState(pos);
        if (state.getValue(SPOOL) == 3) {
            if (!world.isRemote && player != null) {
                StackHelper.giveItem(player, EnumHand.MAIN_HAND, spinningWheel.getStackInSlot(1));
                spinningWheel.decrStackSize(1, spinningWheel.getInventoryStackLimit());
            }
            spinningWheel.input = new NBTTagCompound();
            spinningWheel.wheel = false;
            world.setBlockState(pos, state.cycleProperty(SPOOL), 2);
            spinningWheel.markDirty();
        }
    }

    @Override
    @Nonnull
    public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
        return new ItemStack(AtumBlocks.SPINNING_WHEEL);
    }

    @Override
    public void breakBlock(World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntitySpinningWheel) {
            InventoryHelper.dropInventoryItems(world, pos, (IInventory) tileEntity);
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    @Nonnull
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @Nonnull
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntitySpinningWheel) {
            return state.withProperty(WHEEL, ((TileEntitySpinningWheel) tileEntity).wheel);
        }
        return state;
    }

    @Override
    @Nonnull
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer) {
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta & 3)).withProperty(SPOOL, (meta & 15) >> 2);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;
        meta = meta | state.getValue(FACING).getHorizontalIndex();
        meta = meta | state.getValue(SPOOL) << 2;
        return meta;
    }

    @Override
    @Nonnull
    public IBlockState withRotation(@Nonnull IBlockState state, Rotation rotation) {
        return state.withProperty(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    @Nonnull
    public IBlockState withMirror(@Nonnull IBlockState state, Mirror mirror) {
        return state.withRotation(mirror.toRotation(state.getValue(FACING)));
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, SPOOL, WHEEL);
    }
}
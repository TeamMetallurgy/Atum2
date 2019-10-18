package com.teammetallurgy.atum.blocks.machines;

import com.teammetallurgy.atum.api.recipe.RecipeHandlers;
import com.teammetallurgy.atum.api.recipe.spinningwheel.ISpinningWheelRecipe;
import com.teammetallurgy.atum.blocks.machines.tileentity.SpinningWheelTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.utils.StackHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.BooleanProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.DirectionProperty;
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
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockSpinningWheel extends ContainerBlock {
    private static final DirectionProperty FACING = HorizontalBlock.FACING;
    public static final PropertyInteger SPOOL = PropertyInteger.create("spool", 0, 3);
    private static final BooleanProperty WHEEL = BooleanProperty.create("wheel");

    public BlockSpinningWheel() {
        super(Material.WOOD);
        this.setHardness(1.2F);
        this.setHarvestLevel("axe", 0);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(SPOOL, 0).with(WHEEL, false));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull World world, int meta) {
        return new SpinningWheelTileEntity();
    }

    @Override
    public void onBlockClicked(World world, BlockPos pos, PlayerEntity player) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof SpinningWheelTileEntity) {
            SpinningWheelTileEntity spinningWheel = (SpinningWheelTileEntity) tileEntity;
            if (player.isSneaking()) {
                StackHelper.giveItem(player, Hand.MAIN_HAND, spinningWheel.getStackInSlot(0).copy());
                StackHelper.giveItem(player, Hand.MAIN_HAND, spinningWheel.getStackInSlot(1).copy());
                spinningWheel.decrStackSize(0, spinningWheel.getInventoryStackLimit());
                spinningWheel.decrStackSize(1, spinningWheel.getInventoryStackLimit());
                spinningWheel.input = new CompoundNBT();
                spinningWheel.rotations = 0;
                spinningWheel.wheel = false;
                world.setBlockState(pos, world.getBlockState(pos).with(SPOOL, 0), 2);
                spinningWheel.markDirty();
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, BlockState state, PlayerEntity player, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
        TileEntity tileEntity = world.getTileEntity(pos);
        ItemStack heldStack = player.getHeldItem(hand);

        if (tileEntity instanceof SpinningWheelTileEntity) {
            SpinningWheelTileEntity spinningWheel = (SpinningWheelTileEntity) tileEntity;

            if (facing == state.get(FACING)) {
                this.output(world, pos, player, spinningWheel);
            } else {
                if (facing == Direction.UP) {
                    if (spinningWheel.isEmpty() && !heldStack.isEmpty() && spinningWheel.isItemValidForSlot(0, heldStack) && state.get(SPOOL) < 3) {
                        ItemStack copyStack = new ItemStack(heldStack.getItem(), 1, heldStack.getMetadata());
                        boolean canInsert = false;

                        if (spinningWheel.input.isEmpty()) {
                            spinningWheel.input = copyStack.writeToNBT(new CompoundNBT());
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
                                if (spinningWheelRecipe.getRotations() == spinningWheel.rotations && state.get(SPOOL) < 3 && !spinningWheel.isEmpty()) {
                                    world.setBlockState(pos, state.cycleProperty(SPOOL), 2);
                                    spinningWheel.decrStackSize(0, 1);
                                    spinningWheel.rotations = 0;
                                    spinningWheel.wheel = false;
                                } else if (!spinningWheel.wheel && state.get(SPOOL) < 3 && !spinningWheel.isEmpty()) {
                                    spinningWheel.rotations += 1;
                                    if (world.isRemote) {
                                        world.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_LADDER_FALL, SoundCategory.BLOCKS, 0.55F, 0.4F, true);
                                    }
                                }
                                if (state.get(SPOOL) == 3) {
                                    ItemStack copyOutput = spinningWheelRecipe.getOutput();
                                    ItemStack output = new ItemStack(copyOutput.getItem(), copyOutput.getCount(), copyOutput.getMetadata());
                                    spinningWheel.setInventorySlotContents(1, output);
                                    spinningWheel.input = new CompoundNBT();
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

    public void output(World world, BlockPos pos, @Nullable PlayerEntity player, SpinningWheelTileEntity spinningWheel) {
        BlockState state = world.getBlockState(pos);
        if (state.get(SPOOL) == 3) {
            if (!world.isRemote && player != null) {
                StackHelper.giveItem(player, Hand.MAIN_HAND, spinningWheel.getStackInSlot(1));
                spinningWheel.decrStackSize(1, spinningWheel.getInventoryStackLimit());
            }
            spinningWheel.input = new CompoundNBT();
            spinningWheel.wheel = false;
            world.setBlockState(pos, state.cycleProperty(SPOOL), 2);
            spinningWheel.markDirty();
        }
    }

    @Override
    @Nonnull
    public ItemStack getPickBlock(@Nonnull BlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, PlayerEntity player) {
        return new ItemStack(AtumBlocks.SPINNING_WHEEL);
    }

    @Override
    public void breakBlock(World world, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof SpinningWheelTileEntity) {
            InventoryHelper.dropInventoryItems(world, pos, (IInventory) tileEntity);
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    @Nonnull
    public EnumBlockRenderType getRenderType(BlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @Nonnull
    public BlockState getActualState(@Nonnull BlockState state, IBlockReader world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof SpinningWheelTileEntity) {
            return state.with(WHEEL, ((SpinningWheelTileEntity) tileEntity).wheel);
        }
        return state;
    }

    @Override
    @Nonnull
    public BlockState getStateForPlacement(World world, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer) {
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer).with(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    @Nonnull
    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState().with(FACING, Direction.byHorizontalIndex(meta & 3)).with(SPOOL, (meta & 15) >> 2);
    }

    @Override
    public int getMetaFromState(BlockState state) {
        int meta = 0;
        meta = meta | state.get(FACING).getHorizontalIndex();
        meta = meta | state.get(SPOOL) << 2;
        return meta;
    }

    @Override
    @Nonnull
    public BlockState withRotation(@Nonnull BlockState state, Rotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    @Nonnull
    public BlockState withMirror(@Nonnull BlockState state, Mirror mirror) {
        return state.withRotation(mirror.toRotation(state.get(FACING)));
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, SPOOL, WHEEL);
    }
}
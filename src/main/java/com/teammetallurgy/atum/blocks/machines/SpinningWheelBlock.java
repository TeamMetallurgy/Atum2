package com.teammetallurgy.atum.blocks.machines;

import com.teammetallurgy.atum.api.recipe.IAtumRecipeType;
import com.teammetallurgy.atum.api.recipe.recipes.SpinningWheelRecipe;
import com.teammetallurgy.atum.blocks.machines.tileentity.SpinningWheelTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.misc.StackHelper;
import com.teammetallurgy.atum.misc.recipe.RecipeHelper;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

public class SpinningWheelBlock extends ContainerBlock {
    private static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final IntegerProperty SPOOL = IntegerProperty.create("spool", 0, 3);
    private static final BooleanProperty WHEEL = BooleanProperty.create("wheel");

    public SpinningWheelBlock() {
        super(Properties.create(Material.WOOD).hardnessAndResistance(1.2F).harvestTool(ToolType.AXE).harvestLevel(0));
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(SPOOL, 0).with(WHEEL, false));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull IBlockReader reader) {
        return new SpinningWheelTileEntity();
    }

    @Override
    public void onBlockClicked(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof SpinningWheelTileEntity) {
            SpinningWheelTileEntity spinningWheel = (SpinningWheelTileEntity) tileEntity;
            if (player.isCrouching()) {
                StackHelper.giveItem(player, Hand.MAIN_HAND, spinningWheel.getStackInSlot(0).copy());
                StackHelper.giveItem(player, Hand.MAIN_HAND, spinningWheel.getStackInSlot(1).copy());
                spinningWheel.decrStackSize(0, spinningWheel.getInventoryStackLimit());
                spinningWheel.decrStackSize(1, spinningWheel.getInventoryStackLimit());
                spinningWheel.input = new CompoundNBT();
                spinningWheel.rotations = 0;
                world.setBlockState(pos, world.getBlockState(pos).with(SPOOL, 0).with(WHEEL, false), 2);
                spinningWheel.markDirty();
            }
        }
    }

    @Override
    @Nonnull
    public ActionResultType onBlockActivated(@Nonnull BlockState state, World world, @Nonnull BlockPos pos, PlayerEntity player, @Nonnull Hand hand, @Nonnull BlockRayTraceResult rayTraceResult) {
        TileEntity tileEntity = world.getTileEntity(pos);
        ItemStack heldStack = player.getHeldItem(hand);

        if (tileEntity instanceof SpinningWheelTileEntity && hand == Hand.MAIN_HAND) {
            SpinningWheelTileEntity spinningWheel = (SpinningWheelTileEntity) tileEntity;

            Direction facing = rayTraceResult.getFace();
            if (facing == state.get(FACING)) {
                this.output(world, pos, player, spinningWheel);
            } else {
                if (facing == Direction.UP) {
                    if (spinningWheel.isEmpty() && !heldStack.isEmpty() && spinningWheel.isItemValidForSlot(0, heldStack) && state.get(SPOOL) < 3) {
                        ItemStack copyStack = new ItemStack(heldStack.getItem(), 1);
                        boolean canInsert = false;

                        if (spinningWheel.input.isEmpty()) {
                            spinningWheel.input = copyStack.write(new CompoundNBT());
                        }
                        ItemStack inputStack = ItemStack.read(spinningWheel.input);
                        if (StackHelper.areStacksEqualIgnoreSize(inputStack, heldStack)) {
                            canInsert = true;
                        } else if (!inputStack.isEmpty()) {
                            if (world.isRemote) {
                                player.sendStatusMessage(new TranslationTextComponent("chat.atum.spinning_wheel_recipe_in_progress", inputStack.getDisplayName()).mergeStyle(TextFormatting.RED), true);
                                world.playSound(player, player.getPosition(), SoundEvents.ENTITY_HORSE_SADDLE, SoundCategory.BLOCKS, 0.8F, 1.0F);
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
                        ItemStack input = ItemStack.read(spinningWheel.input);
                        Collection<SpinningWheelRecipe> recipes = RecipeHelper.getRecipes(world.getRecipeManager(), IAtumRecipeType.SPINNING_WHEEL);
                        for (SpinningWheelRecipe spinningWheelRecipe : recipes) {
                            for (Ingredient ingredient : spinningWheelRecipe.getIngredients()) {
                                for (ItemStack ingredientStack : ingredient.getMatchingStacks()) {
                                    if (StackHelper.areStacksEqualIgnoreSize(ingredientStack, input)) {
                                        boolean isSpoolFull = false;
                                        if (!spinningWheel.isEmpty()) {
                                            //Spin wheel
                                            world.setBlockState(pos, state.func_235896_a_(WHEEL), 2);

                                            if (state.get(SPOOL) < 3) {
                                                if (spinningWheelRecipe.getRotations() == spinningWheel.rotations) {
                                                    spinningWheel.decrStackSize(0, 1);
                                                    spinningWheel.rotations = 0;
                                                    int count = ingredientStack.getCount();
                                                    float precentage = (float) 3 / count;
                                                    int spoolSize = state.get(SPOOL) + Math.round(precentage);
                                                    world.setBlockState(pos, state.with(SPOOL, Math.min(3, spoolSize)).with(WHEEL, false), 2);
                                                    if (spoolSize >= 3) {
                                                        isSpoolFull = true;
                                                    }
                                                } else if (!state.get(WHEEL)) {
                                                    spinningWheel.rotations += 1;
                                                    if (world.isRemote) {
                                                        world.playSound((double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_LADDER_FALL, SoundCategory.BLOCKS, 0.55F, 0.4F, true);
                                                    }
                                                }
                                            }
                                        }
                                        if (isSpoolFull) {
                                            ItemStack copyOutput = spinningWheelRecipe.getCraftingResult(spinningWheel);
                                            ItemStack output = new ItemStack(copyOutput.getItem(), copyOutput.getCount());
                                            spinningWheel.setInventorySlotContents(1, output);
                                            spinningWheel.input = new CompoundNBT();
                                        }
                                    }
                                }
                            }
                        }
                        spinningWheel.markDirty();
                    }
                }
            }
        }
        return ActionResultType.SUCCESS;
    }

    public void output(World world, BlockPos pos, @Nullable PlayerEntity player, SpinningWheelTileEntity spinningWheel) {
        BlockState state = world.getBlockState(pos);
        if (state.get(SPOOL) == 3) {
            if (!world.isRemote && player != null) {
                StackHelper.giveItem(player, Hand.MAIN_HAND, spinningWheel.getStackInSlot(1));
                spinningWheel.decrStackSize(1, spinningWheel.getInventoryStackLimit());
            }
            spinningWheel.input = new CompoundNBT();
            world.setBlockState(pos, state.func_235896_a_(SPOOL).with(WHEEL, false), 2);
            spinningWheel.markDirty();
        }
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return new ItemStack(AtumBlocks.SPINNING_WHEEL);
    }

    @Override
    public void onReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        if (newState.getBlock() != state.getBlock()) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof SpinningWheelTileEntity) {
                InventoryHelper.dropInventoryItems(world, pos, (IInventory) tileEntity);
            }
            world.removeTileEntity(pos);
        }
    }

    @Override
    @Nonnull
    public BlockRenderType getRenderType(@Nonnull BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    @Nonnull
    public BlockState mirror(@Nonnull BlockState state, Mirror mirror) {
        return state.rotate(mirror.toRotation(state.get(FACING)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
        container.add(FACING, SPOOL, WHEEL);
    }
}
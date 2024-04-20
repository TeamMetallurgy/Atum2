package com.teammetallurgy.atum.blocks.machines;

import com.mojang.serialization.MapCodec;
import com.teammetallurgy.atum.api.recipe.AtumRecipeTypes;
import com.teammetallurgy.atum.api.recipe.recipes.SpinningWheelRecipe;
import com.teammetallurgy.atum.blocks.machines.tileentity.SpinningWheelTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumTileEntities;
import com.teammetallurgy.atum.misc.StackHelper;
import com.teammetallurgy.atum.misc.recipe.RecipeHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class SpinningWheelBlock extends BaseEntityBlock {
    public static final MapCodec<SpinningWheelBlock> CODEC = simpleCodec(SpinningWheelBlock::new);
    private static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final IntegerProperty SPOOL = IntegerProperty.create("spool", 0, 3);
    private static final BooleanProperty WHEEL = BooleanProperty.create("wheel");

    public SpinningWheelBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(SPOOL, 0).setValue(WHEEL, false));
    }

    @Override
    @Nonnull
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return AtumTileEntities.SPINNING_WHEEL.get().create(pos, state);
    }

    @Override
    public void attack(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player) {
        BlockEntity tileEntity = level.getBlockEntity(pos);

        if (tileEntity instanceof SpinningWheelTileEntity spinningWheel) {
            if (player.isCrouching()) {
                StackHelper.giveItem(player, InteractionHand.MAIN_HAND, spinningWheel.getItem(0).copy());
                StackHelper.giveItem(player, InteractionHand.MAIN_HAND, spinningWheel.getItem(1).copy());
                spinningWheel.removeItem(0, spinningWheel.getMaxStackSize());
                spinningWheel.removeItem(1, spinningWheel.getMaxStackSize());
                spinningWheel.input = new CompoundTag();
                spinningWheel.rotations = 0;
                level.setBlock(pos, level.getBlockState(pos).setValue(SPOOL, 0).setValue(WHEEL, false), 2);
                spinningWheel.setChanged();
            }
        }
    }

    @Override
    @Nonnull
    public InteractionResult use(@Nonnull BlockState state, Level level, @Nonnull BlockPos pos, Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult rayTraceResult) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        ItemStack heldStack = player.getItemInHand(hand);

        if (tileEntity instanceof SpinningWheelTileEntity spinningWheel && hand == InteractionHand.MAIN_HAND) {
            Direction facing = rayTraceResult.getDirection();
            if (facing == state.getValue(FACING)) {
                this.output(level, pos, player, spinningWheel);
            } else {
                if (facing == Direction.UP) {
                    if (spinningWheel.isEmpty() && !heldStack.isEmpty() && spinningWheel.canPlaceItem(0, heldStack) && state.getValue(SPOOL) < 3) {
                        ItemStack copyStack = new ItemStack(heldStack.getItem(), 1);
                        boolean canInsert = false;

                        if (spinningWheel.input.isEmpty()) {
                            spinningWheel.input = copyStack.save(new CompoundTag());
                        }
                        ItemStack inputStack = ItemStack.of(spinningWheel.input);
                        if (StackHelper.areStacksEqualIgnoreSize(inputStack, heldStack)) {
                            canInsert = true;
                        } else if (!inputStack.isEmpty()) {
                            if (level.isClientSide) {
                                player.displayClientMessage(Component.translatable("chat.atum.spinning_wheel_recipe_in_progress", inputStack.getHoverName()).withStyle(ChatFormatting.RED), true);
                                level.playSound(player, player.blockPosition(), SoundEvents.HORSE_SADDLE, SoundSource.BLOCKS, 0.8F, 1.0F);
                            }
                        }

                        if (canInsert) {
                            spinningWheel.setItem(0, copyStack);
                            if (!player.isCreative()) {
                                heldStack.shrink(1);
                            }
                            spinningWheel.setChanged();
                        }
                    } else if (!spinningWheel.input.isEmpty()) {
                        ItemStack input = ItemStack.of(spinningWheel.input);
                        List<RecipeHolder<SpinningWheelRecipe>> recipes = RecipeHelper.getRecipes(level.getRecipeManager(), AtumRecipeTypes.SPINNING_WHEEL.get());
                        for (RecipeHolder<SpinningWheelRecipe> spinningWheelRecipe : recipes) {
                            for (Ingredient ingredient : spinningWheelRecipe.value().getIngredients()) {
                                for (ItemStack ingredientStack : ingredient.getItems()) {
                                    if (StackHelper.areStacksEqualIgnoreSize(ingredientStack, input)) {
                                        boolean isSpoolFull = false;
                                        if (!spinningWheel.isEmpty()) {
                                            //Spin wheel
                                            level.setBlock(pos, state.cycle(WHEEL), 2);

                                            if (state.getValue(SPOOL) < 3) {
                                                if (spinningWheelRecipe.value().getRotations() == spinningWheel.rotations) {
                                                    spinningWheel.removeItem(0, 1);
                                                    spinningWheel.rotations = 0;
                                                    int count = ingredientStack.getCount();
                                                    float precentage = (float) 3 / count;
                                                    int spoolSize = state.getValue(SPOOL) + Math.round(precentage);
                                                    level.setBlock(pos, state.setValue(SPOOL, Math.min(3, spoolSize)).setValue(WHEEL, false), 2);
                                                    if (spoolSize >= 3) {
                                                        isSpoolFull = true;
                                                    }
                                                } else if (!state.getValue(WHEEL)) {
                                                    spinningWheel.rotations += 1;
                                                    if (level.isClientSide) {
                                                        level.playLocalSound((double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.LADDER_FALL, SoundSource.BLOCKS, 0.55F, 0.4F, true);
                                                    }
                                                }
                                            }
                                        }
                                        if (isSpoolFull) {
                                            ItemStack copyOutput = spinningWheelRecipe.value().assemble(spinningWheel, level.registryAccess());
                                            ItemStack output = new ItemStack(copyOutput.getItem(), copyOutput.getCount());
                                            spinningWheel.setItem(1, output);
                                            spinningWheel.input = new CompoundTag();
                                        }
                                    }
                                }
                            }
                        }
                        spinningWheel.setChanged();
                    }
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    public void output(Level level, BlockPos pos, @Nullable Player player, SpinningWheelTileEntity spinningWheel) {
        BlockState state = level.getBlockState(pos);
        if (state.getValue(SPOOL) == 3) {
            if (!level.isClientSide && player != null) {
                StackHelper.giveItem(player, InteractionHand.MAIN_HAND, spinningWheel.getItem(1));
                spinningWheel.removeItem(1, spinningWheel.getMaxStackSize());
            }
            spinningWheel.input = new CompoundTag();
            level.setBlock(pos, state.cycle(SPOOL).setValue(WHEEL, false), 2);
            spinningWheel.setChanged();
        }
    }

    @Override
    @Nonnull
    public ItemStack getCloneItemStack(@Nonnull BlockState state, @Nonnull HitResult target, @Nonnull LevelReader level, @Nonnull BlockPos pos, @Nonnull Player player) {
        return new ItemStack(AtumBlocks.SPINNING_WHEEL.get());
    }

    @Override
    public void onRemove(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        if (newState.getBlock() != state.getBlock()) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof SpinningWheelTileEntity) {
                Containers.dropContents(level, pos, (Container) tileEntity);
            }
            level.removeBlockEntity(pos);
        }
    }

    @Override
    @Nonnull
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    @Nonnull
    public BlockState mirror(@Nonnull BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
        container.add(FACING, SPOOL, WHEEL);
    }
}
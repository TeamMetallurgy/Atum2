package com.teammetallurgy.atum.blocks.vegetation;

import com.mojang.serialization.MapCodec;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;

import javax.annotation.Nonnull;

public class DateBlock extends BushBlock implements BonemealableBlock {
    public static final MapCodec<DateBlock> CODEC = simpleCodec(DateBlock::new);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    private static final VoxelShape STEM = Block.box(7.0D, 10.0D, 7.0D, 9.0D, 16.0D, 9.0D);
    private static final VoxelShape BOUNDING_BOX = Block.box(5.0D, 2.0D, 5.0D, 11.0D, 16.0D, 11.0D);

    public DateBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    @Nonnull
    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    @Override
    @Nonnull
    public VoxelShape getShape(BlockState state, @Nonnull BlockGetter reader, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        if (state.getValue(AGE) == 0) {
            return STEM;
        }
        return BOUNDING_BOX;
    }

    @Override
    public void tick(@Nonnull BlockState state, ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
        if (!level.isClientSide) {
            super.tick(state, level, pos, rand);
            if (!level.isAreaLoaded(pos, 1)) return;
            if (state.getValue(AGE) != 7) {
                if (CommonHooks.onCropsGrowPre(level, pos, state, level.random.nextDouble() <= 0.12F)) {
                    level.setBlock(pos, state.cycle(AGE), 2);
                    CommonHooks.onCropsGrowPost(level, pos, state);
                }
            }
        }
    }

    @Override
    public boolean canSurvive(BlockState state, @Nonnull LevelReader level, @Nonnull BlockPos pos) {
        if (state.getBlock() == this) {
            BlockState stateUp = level.getBlockState(pos.above());
            return stateUp.is(BlockTags.LEAVES);
        }
        return super.mayPlaceOn(level.getBlockState(pos.below()), level, pos);
    }

    @Override
    @Nonnull
    public InteractionResult use(BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult rayTraceResult) {
        if (state.getValue(AGE) == 7) {
            dropResources(state, level, pos);
            return level.setBlockAndUpdate(pos, this.defaultBlockState()) ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
        return InteractionResult.PASS;
    }

    @Override
    @Nonnull
    public ItemStack getCloneItemStack(@Nonnull BlockState state, @Nonnull HitResult target, @Nonnull LevelReader level, @Nonnull BlockPos pos, @Nonnull Player player) {
        return new ItemStack(AtumItems.DATE.get());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
        container.add(AGE);
    }

    @Override
    public boolean isValidBonemealTarget(@Nonnull LevelReader level, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        return state.getValue(AGE) != 7;
    }

    @Override
    public boolean isBonemealSuccess(@Nonnull Level level, @Nonnull RandomSource rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(@Nonnull ServerLevel level, @Nonnull RandomSource rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        int growth = state.getValue(AGE) + Mth.nextInt(rand, 1, 2);
        int maxAge = 7;

        if (growth > maxAge) {
            growth = maxAge;
        }

        if (state.getValue(AGE) != 7) {
            level.setBlock(pos, this.defaultBlockState().setValue(AGE, growth), 2);
        }
    }
}
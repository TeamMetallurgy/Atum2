package com.teammetallurgy.atum.blocks.vegetation;

import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
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
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nonnull;
import java.util.Random;

public class DateBlock extends BushBlock implements BonemealableBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    private static final VoxelShape STEM = Block.box(7.0D, 10.0D, 7.0D, 9.0D, 16.0D, 9.0D);
    private static final VoxelShape BOUNDING_BOX = Block.box(5.0D, 2.0D, 5.0D, 11.0D, 16.0D, 11.0D);

    public DateBlock() {
        super(Properties.of(Material.PLANT).sound(SoundType.GRASS).strength(0.35F).noOcclusion().randomTicks());
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
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
    public void tick(@Nonnull BlockState state, ServerLevel world, @Nonnull BlockPos pos, @Nonnull Random rand) {
        if (!world.isClientSide) {
            super.tick(state, world, pos, rand);
            if (!world.isAreaLoaded(pos, 1)) return;
            if (state.getValue(AGE) != 7) {
                if (ForgeHooks.onCropsGrowPre(world, pos, state, world.random.nextDouble() <= 0.12F)) {
                    world.setBlock(pos, state.cycle(AGE), 2);
                    ForgeHooks.onCropsGrowPost(world, pos, state);
                }
            }
        }
    }

    @Override
    public boolean canSurvive(BlockState state, @Nonnull LevelReader world, @Nonnull BlockPos pos) {
        if (state.getBlock() == this) {
            BlockState stateUp = world.getBlockState(pos.above());
            return stateUp.is(BlockTags.LEAVES);
        }
        return super.mayPlaceOn(world.getBlockState(pos.below()), world, pos);
    }

    @Override
    @Nonnull
    public InteractionResult use(BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult rayTraceResult) {
        if (state.getValue(AGE) == 7) {
            dropResources(state, world, pos);
            return world.setBlockAndUpdate(pos, this.defaultBlockState()) ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
        return InteractionResult.PASS;
    }

    @Override
    @Nonnull
    public ItemStack getCloneItemStack(@Nonnull BlockGetter getter, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new ItemStack(AtumItems.DATE);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
        container.add(AGE);
    }

    @Override
    public boolean isValidBonemealTarget(@Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull BlockState state, boolean isClient) {
        return state.getValue(AGE) != 7;
    }

    @Override
    public boolean isBonemealSuccess(@Nonnull Level world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(@Nonnull ServerLevel world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        int growth = state.getValue(AGE) + Mth.nextInt(rand, 1, 2);
        int maxAge = 7;

        if (growth > maxAge) {
            growth = maxAge;
        }

        if (state.getValue(AGE) != 7) {
            world.setBlock(pos, this.defaultBlockState().setValue(AGE, growth), 2);
        }
    }
}
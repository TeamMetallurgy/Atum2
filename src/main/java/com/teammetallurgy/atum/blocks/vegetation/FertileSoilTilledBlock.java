package com.teammetallurgy.atum.blocks.vegetation;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

import javax.annotation.Nonnull;
import java.util.Random;

public class FertileSoilTilledBlock extends FarmBlock {
    public static final BooleanProperty BLESSED = BooleanProperty.create("blessed");

    public FertileSoilTilledBlock() {
        super(Block.Properties.of(Material.DIRT).randomTicks().strength(0.5F).sound(SoundType.GRAVEL).isViewBlocking(AtumBlocks::always).isSuffocating(AtumBlocks::always));
        this.registerDefaultState(this.stateDefinition.any().setValue(MOISTURE, 0).setValue(BLESSED, false));
    }

    @Override
    @Nonnull
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return FarmBlock.SHAPE;
    }

    @Override
    public void tick(BlockState state, @Nonnull ServerLevel serverLevel, @Nonnull BlockPos pos, @Nonnull Random rand) {
        if (!state.canSurvive(serverLevel, pos)) {
            turnToSoil(serverLevel, pos, AtumBlocks.FERTILE_SOIL.get());
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, @Nonnull Random rand) {
        int moisture = state.getValue(MOISTURE);

        Block blockUp = world.getBlockState(pos.above()).getBlock();
        if (state.getValue(BLESSED) && blockUp instanceof BonemealableBlock) {
            world.scheduleTick(pos.above(), blockUp, 5);
        }

        if (!this.hasWater(world, pos) && !world.isRainingAt(pos.above())) {
            if (moisture > 0) {
                world.setBlock(pos, state.setValue(MOISTURE, moisture - 1), 2);
            } else if (!this.hasCrops(world, pos)) {
                turnToSoil(world, pos, AtumBlocks.FERTILE_SOIL.get());
            }
        } else if (moisture < 7) {
            world.setBlock(pos, state.setValue(MOISTURE, 7), 2);
        }
    }

    @Override
    public boolean isFertile(BlockState state, BlockGetter world, BlockPos pos) {
        return state.getValue(MOISTURE) > 0 || state.getValue(BLESSED);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Random rand) {
        if (state.getValue(BLESSED) && !world.getBlockState(pos.above()).isRedstoneConductor(world, pos.above())) {
            if (rand.nextDouble() <= 0.15D) {
                double d0 = rand.nextGaussian() * 0.01D;
                double d1 = rand.nextGaussian() * 0.005D;
                double d2 = rand.nextGaussian() * 0.01D;
                world.addParticle(AtumParticles.TEFNUT.get(), (float) pos.getX() + rand.nextFloat(), (double) pos.getY() + 1.05D, (float) pos.getZ() + rand.nextFloat(), d0, d1, d2);
            }
        }
    }

    @Override
    public void fallOn(@Nonnull Level world, BlockState state, @Nonnull BlockPos pos, @Nonnull Entity entity, float fallDistance) {
        if (!world.isClientSide && entity.canTrample(this.defaultBlockState(), pos, fallDistance)) {
            turnToSoil(world, pos, AtumBlocks.FERTILE_SOIL.get());
        }
        entity.causeFallDamage(fallDistance, 1.0F, DamageSource.FALL);
    }

    private static void turnToSoil(Level world, BlockPos pos, Block block) {
        world.setBlockAndUpdate(pos, pushEntitiesUp(world.getBlockState(pos), block.defaultBlockState(), world, pos));
    }

    private boolean hasCrops(Level world, BlockPos pos) {
        Block block = world.getBlockState(pos.above()).getBlock();
        return block instanceof IPlantable && canSustainPlant(world.getBlockState(pos), world, pos, Direction.UP, (IPlantable) block);
    }

    private boolean hasWater(Level world, BlockPos pos) {
        for (BlockPos mutablePos : BlockPos.betweenClosed(pos.offset(-6, 0, -6), pos.offset(6, 1, 6))) {
            if (world.getBlockState(mutablePos).getFluidState().is(FluidTags.WATER)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void neighborChanged(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Block block, @Nonnull BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, world, pos, block, fromPos, isMoving);

        if (world.getBlockState(pos.above()).getMaterial().isSolid()) {
            turnToSoil(world, pos, AtumBlocks.SAND.get());
        }
    }

    /*@Override
    public void onPlantGrow(BlockState state, @Nonnull LevelAccessor world, @Nonnull BlockPos pos, BlockPos source) { //TODO Is this even needed anymore?
        if (this == AtumBlocks.FERTILE_SOIL_TILLED) {
            world.setBlock(pos, AtumBlocks.FERTILE_SOIL.defaultBlockState(), 2);
        }
        super.onPlantGrow(state, world, pos, source);
    }*/

    @Override
    public void onPlace(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState oldState, boolean isMoving) {
        super.onPlace(state, world, pos, oldState, isMoving);

        if (world.getBlockState(pos.above()).getMaterial().isSolid()) {
            turnToSoil(world, pos, AtumBlocks.SAND.get());
        }
    }

    @Override
    public boolean canSustainPlant(@Nonnull BlockState state, @Nonnull BlockGetter world, BlockPos pos, @Nonnull Direction direction, IPlantable plantable) {
        PlantType plantType = plantable.getPlantType(world, pos.above());

        if (plantType.equals(PlantType.CROP) || plantType.equals(PlantType.PLAINS)) {
            return true;
        }
        return super.canSustainPlant(state, world, pos, direction, plantable);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
        container.add(MOISTURE, BLESSED);
    }
}
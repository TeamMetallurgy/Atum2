package com.teammetallurgy.atum.blocks.vegetation;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumParticles;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import java.util.Random;

public class FertileSoilTilledBlock extends FarmlandBlock {
    public static final BooleanProperty BLESSED = BooleanProperty.create("blessed");

    public FertileSoilTilledBlock() {
        super(Block.Properties.create(Material.EARTH).tickRandomly().hardnessAndResistance(0.5F).sound(SoundType.GROUND).harvestTool(ToolType.SHOVEL).harvestLevel(0));
        this.setDefaultState(this.stateContainer.getBaseState().with(MOISTURE, 0).with(BLESSED, false));
    }

    @Override
    @Nonnull
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return FarmlandBlock.SHAPE;
    }

    @Override
    public void tick(BlockState state, World world, BlockPos pos, Random rand) {
        int moisture = state.get(MOISTURE);

        Block blockUp = world.getBlockState(pos.up()).getBlock();
        if (state.get(BLESSED) && blockUp instanceof IGrowable) {
            world.getPendingBlockTicks().scheduleTick(pos.up(), blockUp, this.tickRate(world));
        }

        if (!this.hasWater(world, pos) && !world.isRainingAt(pos.up())) {
            if (moisture > 0) {
                world.setBlockState(pos, state.with(MOISTURE, moisture - 1), 2);
            } else if (!this.hasCrops(world, pos)) {
                turnToSoil(world, pos);
            }
        } else if (moisture < 7) {
            world.setBlockState(pos, state.with(MOISTURE, 7), 2);
        }
    }

    @Override
    public int tickRate(IWorldReader world) {
        return 5;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
        if (state.get(BLESSED) && !world.getBlockState(pos.up()).isNormalCube(world, pos.up())) {
            if (rand.nextDouble() <= 0.15D) {
                for (int amount = 0; amount < 3; ++amount) {
                    double d0 = rand.nextGaussian() * 0.01D;
                    double d1 = rand.nextGaussian() * 0.005D;
                    double d2 = rand.nextGaussian() * 0.01D;
                    world.addParticle(AtumParticles.TEFNUT, (float) pos.getX() + rand.nextFloat(), (double) pos.getY() + 1.05D, (float) pos.getZ() + rand.nextFloat(), d0, d1, d2);
                }
            }
        }
    }

    @Override
    public void onFallenUpon(@Nonnull World world, @Nonnull BlockPos pos, Entity entity, float fallDistance) {
        if (!world.isRemote && entity.canTrample(this.getDefaultState(), pos, fallDistance)) {
            turnToSoil(world, pos);
        }
        entity.fall(fallDistance, 1.0F);
    }

    private static void turnToSoil(World world, BlockPos pos) {
        world.setBlockState(pos, nudgeEntitiesWithNewState(world.getBlockState(pos), AtumBlocks.FERTILE_SOIL.getDefaultState(), world, pos));
    }

    private boolean hasCrops(World world, BlockPos pos) {
        Block block = world.getBlockState(pos.up()).getBlock();
        return block instanceof IPlantable && canSustainPlant(world.getBlockState(pos), world, pos, Direction.UP, (IPlantable) block);
    }

    private boolean hasWater(World world, BlockPos pos) {
        for (BlockPos mutableBlockPos : BlockPos.getAllInBoxMutable(pos.add(-6, 0, -6), pos.add(6, 1, 6))) {
            if (world.getBlockState(mutableBlockPos).getMaterial() == Material.WATER) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, world, pos, block, fromPos, isMoving);

        if (world.getBlockState(pos.up()).getMaterial().isSolid()) {
            turnToSoil(world, pos);
        }
    }

    @Override
    public void onPlantGrow(BlockState state, @Nonnull IWorld world, @Nonnull BlockPos pos, BlockPos source) {
        if (this == AtumBlocks.FERTILE_SOIL_TILLED) {
            world.setBlockState(pos, AtumBlocks.FERTILE_SOIL.getDefaultState(), 2);
        }
        super.onPlantGrow(state, world, pos, source);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onBlockAdded(state, world, pos, oldState, isMoving);

        if (world.getBlockState(pos.up()).getMaterial().isSolid()) {
            turnToSoil(world, pos);
        }
    }

    @Override
    public boolean canSustainPlant(@Nonnull BlockState state, @Nonnull IBlockReader world, BlockPos pos, @Nonnull Direction direction, IPlantable plantable) {
        PlantType plantType = plantable.getPlantType(world, pos.up());
        switch (plantType) {
            case Crop:
            case Plains:
                return true;
            default:
                return super.canSustainPlant(state, world, pos, direction, plantable);
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
        container.add(MOISTURE, BLESSED);
    }

    /*@Override
    public Property[] getNonRenderingProperties() { //TODO
        return new Property[]{BLESSED};
    }*/
}
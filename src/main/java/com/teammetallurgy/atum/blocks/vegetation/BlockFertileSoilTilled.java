package com.teammetallurgy.atum.blocks.vegetation;

import com.teammetallurgy.atum.blocks.base.IRenderMapper;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BlockFertileSoilTilled extends FarmlandBlock implements IRenderMapper {
    public static final PropertyBool BLESSED = PropertyBool.create("blessed");

    public BlockFertileSoilTilled() {
        super(Block.Properties.create(Material.EARTH).tickRandomly().hardnessAndResistance(0.5F).sound(SoundType.GROUND).harvestTool(ToolType.SHOVEL).harvestLevel(0));
        this.setDefaultState(this.blockState.getBaseState().with(MOISTURE, 0).with(BLESSED, false));
    }

    @Override
    public void updateTick(@Nonnull World world, BlockPos pos, BlockState state, Random rand) {
        int moisture = state.getValue(MOISTURE);

        Block blockUp = world.getBlockState(pos.up()).getBlock();
        if (state.getValue(BLESSED) && blockUp instanceof IGrowable) {
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
    public int tickRate(World world) {
        return 5;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random rand) {
        if (state.get(BLESSED) && !world.getBlockState(pos.up()).isNormalCube()) {
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
        if (!world.isRemote && entity.canTrample(world, this, pos, fallDistance)) {
            turnToSoil(world, pos);
        }
        entity.fall(fallDistance, 1.0F);
    }

    private static void turnToSoil(World world, BlockPos pos) {
        BlockState state = AtumBlocks.FERTILE_SOIL.getDefaultState();
        world.setBlockState(pos, state);
        AxisAlignedBB axisAlignedBB = field_194405_c.offset(pos);

        for (Entity entity : world.getEntitiesWithinAABBExcludingEntity(null, axisAlignedBB)) {
            double axisY = Math.min(axisAlignedBB.maxY - axisAlignedBB.minY, axisAlignedBB.maxY - entity.getBoundingBox().minY);
            entity.setPositionAndUpdate(entity.posX, entity.posY + axisY + 0.001D, entity.posZ);
        }
    }

    private boolean hasCrops(World world, BlockPos pos) {
        Block block = world.getBlockState(pos.up()).getBlock();
        return block instanceof IPlantable && canSustainPlant(world.getBlockState(pos), world, pos, Direction.UP, (IPlantable) block);
    }

    private boolean hasWater(World world, BlockPos pos) {
        for (BlockPos.MutableBlockPos mutableBlockPos : BlockPos.getAllInBoxMutable(pos.add(-6, 0, -6), pos.add(6, 1, 6))) {
            if (world.getBlockState(mutableBlockPos).getMaterial() == Material.WATER) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        super.neighborChanged(state, world, pos, block, fromPos);

        if (world.getBlockState(pos.up()).getMaterial().isSolid()) {
            turnToSoil(world, pos);
        }
    }

    @Override
    public void onPlantGrow(BlockState state, @Nonnull World world, @Nonnull BlockPos pos, BlockPos source) {
        if (this == AtumBlocks.FERTILE_SOIL_TILLED) {
            world.setBlockState(pos, AtumBlocks.FERTILE_SOIL.getDefaultState(), 2);
        }
        super.onPlantGrow(state, world, pos, source);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, BlockState state) {
        super.onBlockAdded(world, pos, state);

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
    @OnlyIn(Dist.CLIENT)
    public boolean shouldSideBeRendered(@Nonnull BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos, Direction side) {
        switch (side) {
            case UP:
                return true;
            case NORTH:
            case SOUTH:
            case WEST:
            case EAST:
                BlockState stateSide = world.getBlockState(pos.offset(side));
                Block block = stateSide.getBlock();
                return !stateSide.isOpaqueCube() && block != AtumBlocks.FERTILE_SOIL && block != Blocks.GRASS_PATH;
            default:
                return super.shouldSideBeRendered(state, world, pos, side);
        }
    }

    @Override
    @Nonnull
    public Item getItemDropped(BlockState state, @Nullable Random rand, int fortune) {
        return AtumItems.FERTILE_SOIL_PILE;
    }

    @Override
    public int quantityDropped(Random random) {
        return MathHelper.getInt(random, 3, 5);
    }

    @Override
    @Nonnull
    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState().with(MOISTURE, meta & 7).with(BLESSED, meta > 7);
    }

    @Override
    public int getMetaFromState(BlockState state) {
        return state.getValue(MOISTURE) + (state.getValue(BLESSED) ? 8 : 0);
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, MOISTURE, BLESSED);
    }

    @Override
    public IProperty[] getNonRenderingProperties() {
        return new IProperty[]{BLESSED};
    }
}
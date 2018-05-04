package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockFertileSoilTilled extends BlockFarmland {

    public BlockFertileSoilTilled() {
        super();
        this.setHardness(0.5F);
    }

    @Override
    public void updateTick(@Nonnull World world, BlockPos pos, IBlockState state, Random rand) {
        int i = state.getValue(MOISTURE);

        if (!this.hasWater(world, pos) && !world.isRainingAt(pos.up())) {
            if (i > 0) {
                world.setBlockState(pos, state.withProperty(MOISTURE, i - 1), 2);
            } else if (!this.hasCrops(world, pos)) {
                turnToSoil(world, pos);
            }
        } else if (i < 7) {
            world.setBlockState(pos, state.withProperty(MOISTURE, 7), 2);
        }
    }

    @Override
    public void onFallenUpon(World world, @Nonnull BlockPos pos, Entity entity, float fallDistance) {
        if (!world.isRemote && entity.canTrample(world, this, pos, fallDistance)) {
            turnToSoil(world, pos);
        }
        super.onFallenUpon(world, pos, entity, fallDistance);
    }

    private static void turnToSoil(World world, BlockPos pos) {
        world.setBlockState(pos, AtumBlocks.FERTILE_SOIL.getDefaultState());
        AxisAlignedBB axisAlignedBB = field_194405_c.offset(pos);

        for (Entity entity : world.getEntitiesWithinAABBExcludingEntity(null, axisAlignedBB)) {
            double axisY = Math.min(axisAlignedBB.maxY - axisAlignedBB.minY, axisAlignedBB.maxY - entity.getEntityBoundingBox().minY);
            entity.setPositionAndUpdate(entity.posX, entity.posY + axisY + 0.001D, entity.posZ);
        }
    }

    private boolean hasCrops(World world, BlockPos pos) {
        Block block = world.getBlockState(pos.up()).getBlock();
        return block instanceof IPlantable && canSustainPlant(world.getBlockState(pos), world, pos, EnumFacing.UP, (IPlantable) block);
    }

    private boolean hasWater(World world, BlockPos pos) {
        for (BlockPos.MutableBlockPos mutableBlockPos : BlockPos.getAllInBoxMutable(pos.add(-4, 0, -4), pos.add(4, 1, 4))) {
            if (world.getBlockState(mutableBlockPos).getMaterial() == Material.WATER) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        super.neighborChanged(state, world, pos, block, fromPos);

        if (world.getBlockState(pos.up()).getMaterial().isSolid()) {
            turnToSoil(world, pos);
        }
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);

        if (world.getBlockState(pos.up()).getMaterial().isSolid()) {
            turnToSoil(world, pos);
        }
    }

    @Override
    public boolean canSustainPlant(@Nonnull IBlockState state, @Nonnull IBlockAccess world, BlockPos pos, @Nonnull EnumFacing direction, IPlantable plantable) {
        EnumPlantType plantType = plantable.getPlantType(world, pos.up());

        switch (plantType) {
            case Crop:
                return true;
            case Plains:
                return true;
            default:
                return super.canSustainPlant(state, world, pos, direction, plantable);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {
        switch (side) {
            case UP:
                return true;
            case NORTH:
            case SOUTH:
            case WEST:
            case EAST:
                IBlockState stateSide = world.getBlockState(pos.offset(side));
                Block block = stateSide.getBlock();
                return !stateSide.isOpaqueCube() && block != AtumBlocks.FERTILE_SOIL && block != Blocks.GRASS_PATH;
            default:
                return super.shouldSideBeRendered(state, world, pos, side);
        }
    }

    @Override
    @Nonnull
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return AtumItems.FERTILE_SOIL_PILE;
    }

    @Override
    public int quantityDropped(Random random) {
        return MathHelper.getInt(random, 3, 5);
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, MOISTURE);
    }
}
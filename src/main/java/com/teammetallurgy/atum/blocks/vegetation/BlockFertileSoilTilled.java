package com.teammetallurgy.atum.blocks.vegetation;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.base.IRenderMapper;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
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
import javax.annotation.Nullable;
import java.util.Random;

public class BlockFertileSoilTilled extends BlockFarmland implements IRenderMapper {
    public static final PropertyBool BLESSED = PropertyBool.create("blessed");

    public BlockFertileSoilTilled() {
        super();
        this.setHardness(0.5F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(MOISTURE, 0).withProperty(BLESSED, false));
    }

    @Override
    public void updateTick(@Nonnull World world, BlockPos pos, IBlockState state, Random rand) {
        int moisture = state.getValue(MOISTURE);

        Block blockUp = world.getBlockState(pos.up()).getBlock();
        if (state.getValue(BLESSED) && blockUp instanceof IGrowable) {
            world.scheduleUpdate(pos.up(), blockUp, this.tickRate(world));
        }

        if (!this.hasWater(world, pos) && !world.isRainingAt(pos.up())) {
            if (moisture > 0) {
                world.setBlockState(pos, state.withProperty(MOISTURE, moisture - 1), 2);
            } else if (!this.hasCrops(world, pos)) {
                turnToSoil(world, pos);
            }
        } else if (moisture < 7) {
            world.setBlockState(pos, state.withProperty(MOISTURE, 7), 2);
        }
    }

    @Override
    public int tickRate(World world) {
        return 5;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (state.getValue(BLESSED) && !world.getBlockState(pos.up()).isNormalCube()) {
            if (rand.nextDouble() <= 0.15D) {
                for (int amount = 0; amount < 5; ++amount) {
                    double d0 = rand.nextGaussian() * 0.01D;
                    double d1 = rand.nextGaussian() * 0.005D;
                    double d2 = rand.nextGaussian() * 0.01D;
                    Atum.proxy.spawnParticle(AtumParticles.Types.TEFNUT, Minecraft.getMinecraft().player, (double) ((float) pos.getX() + rand.nextFloat()), (double) pos.getY() + 1.05D, (double) ((float) pos.getZ() + rand.nextFloat()), d0, d1, d2);
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
        IBlockState state = AtumBlocks.FERTILE_SOIL.getDefaultState();
        world.setBlockState(pos, state);
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
        for (BlockPos.MutableBlockPos mutableBlockPos : BlockPos.getAllInBoxMutable(pos.add(-6, 0, -6), pos.add(6, 1, 6))) {
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
    public void onPlantGrow(IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, BlockPos source) {
        if (this == AtumBlocks.FERTILE_SOIL_TILLED) {
            world.setBlockState(pos, AtumBlocks.FERTILE_SOIL.getDefaultState(), 2);
        }
        super.onPlantGrow(state, world, pos, source);
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
    public Item getItemDropped(IBlockState state, @Nullable Random rand, int fortune) {
        return AtumItems.FERTILE_SOIL_PILE;
    }

    @Override
    public int quantityDropped(Random random) {
        return MathHelper.getInt(random, 3, 5);
    }

    @Override
    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(MOISTURE, meta & 7).withProperty(BLESSED, meta > 7);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
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
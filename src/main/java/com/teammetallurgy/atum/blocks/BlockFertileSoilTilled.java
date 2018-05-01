package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockFertileSoilTilled extends Block {
    public static final PropertyInteger MOISTURE = PropertyInteger.create("moisture", 0, 7);
    protected static final AxisAlignedBB TILLED_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.9375D, 1.0D);

    public BlockFertileSoilTilled() {
        super(Material.GROUND);
        this.setHardness(0.5F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(MOISTURE, Integer.valueOf(0)));
        this.setTickRandomly(true);
        this.setLightOpacity(255);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return TILLED_AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    /*@Override
    @SideOnly(Side.CLIENT) //TODO Is this needed?
    public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand) {
        int enchanted = (world.getBlockMetadata(par2, par3, par4) & 4 & 4) >> 2;
        if (enchanted == 1 && rand.nextDouble() > 0.6D) {
            double d0 = rand.nextGaussian() * 0.02D;
            double d1 = rand.nextGaussian() * 0.02D;
            double d2 = rand.nextGaussian() * 0.02D;
            world.spawnParticle("happyVillager", (double) ((float) par2 + par5Random.nextFloat()), (double) par3 + (double) par5Random.nextFloat() * this.getBlockBoundsMaxY() * 0.4D + 1.0D, (double) ((float) par4 + par5Random.nextFloat()), d0, d1, d2);
        }
    }*/

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        int i = state.getValue(MOISTURE);

        if (!this.hasWater(world, pos) && !world.isRainingAt(pos.up())) {
            if ((i & 3) > 0 && rand.nextDouble() > 0.5D) {
                world.setBlockState(pos, state.withProperty(MOISTURE, i - 1), 2);
            } else if (!this.hasCrops(world, pos)) {
                world.setBlockState(pos, AtumBlocks.FERTILE_SOIL.getDefaultState());
            }
        } else if (i < 7) {
            world.setBlockState(pos, state.withProperty(MOISTURE, 7), 2);
        }

        Block blockUp = world.getBlockState(pos.up()).getBlock();
        if (blockUp != null) {
            for (int j = 0; j < 2; ++i) {
                blockUp.updateTick(world, pos.up(), state, rand);
            }
        }
    }

    @Override
    public void onFallenUpon(World world, BlockPos pos, Entity entity, float fallDistance) {
        if (!world.isRemote && world.rand.nextFloat() < fallDistance - 0.5F && entity instanceof EntityLivingBase && (entity instanceof EntityPlayer || world.getGameRules().getBoolean("mobGriefing")) && entity.width * entity.width * entity.height > 0.512F) {
            world.setBlockState(pos, AtumBlocks.FERTILE_SOIL.getDefaultState());
        }

        super.onFallenUpon(world, pos, entity, fallDistance);
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
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos) {
        super.neighborChanged(state, world, pos, neighborBlock, neighborPos);

        if (world.getBlockState(pos.up()).getMaterial().isSolid()) {
            world.setBlockState(pos, AtumBlocks.FERTILE_SOIL.getDefaultState());
        }
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
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
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
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
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return AtumBlocks.SAND.getItemDropped(AtumBlocks.SAND.getDefaultState(), rand, fortune);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(AtumBlocks.SAND);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(MOISTURE, meta & 7);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(MOISTURE);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, MOISTURE);
    }
}
package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockReed;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockPapyrus extends BlockReed implements IRenderMapper {
    private static final PropertyBool TOP = PropertyBool.create("top");

    public BlockPapyrus() {
        super();
        this.setHardness(0.0F);
        this.setSoundType(SoundType.PLANT);
        this.setTickRandomly(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0).withProperty(TOP, false));
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (world.getBlockState(pos.down()).getBlock() == AtumBlocks.PAPYRUS || this.checkForDrop(world, pos, state)) {
            if (world.isAirBlock(pos.up())) {
                int i;
                for (i = 1; world.getBlockState(pos.down(i)).getBlock() == this; ++i) {
                    //Do nothing
                }
                if (i < 3) {
                    int j = state.getValue(AGE);

                    if (ForgeHooks.onCropsGrowPre(world, pos, state, true)) {
                        if (j == 15) {
                            world.setBlockState(pos.up(), this.getDefaultState());
                            world.setBlockState(pos, state.withProperty(AGE, 0), 4);
                        } else {
                            world.setBlockState(pos, state.withProperty(AGE, j + 1), 4);
                        }
                        ForgeHooks.onCropsGrowPost(world, pos, state, world.getBlockState(pos));
                    }
                }
            }
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, @Nonnull BlockPos pos) {
        IBlockState state = world.getBlockState(pos.down());
        Block block = state.getBlock();
        if (block.canSustainPlant(state, world, pos.down(), EnumFacing.UP, this)) return true;

        if (block == this) {
            return true;
        } else if (block != AtumBlocks.FERTILE_SOIL && block != AtumBlocks.FERTILE_SOIL_TILLED && block != AtumBlocks.SAND) {
            return false;
        } else {
            BlockPos blockpos = pos.down();

            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                IBlockState iblockstate = world.getBlockState(blockpos.offset(enumfacing));

                if (iblockstate.getMaterial() == Material.WATER) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public boolean canSustainPlant(@Nonnull IBlockState state, @Nonnull IBlockAccess world, BlockPos pos, @Nonnull EnumFacing direction, IPlantable plantable) {
        IBlockState plant = plantable.getPlant(world, pos.offset(direction));
        if (plant.getBlock() == AtumBlocks.PAPYRUS && this == AtumBlocks.PAPYRUS) {
            return true;
        }
        return super.canSustainPlant(state, world, pos, direction, plantable);
    }


    @Override
    @Nonnull
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return AtumItems.PAPYRUS_PLANT;
    }

    @Override
    @Nonnull
    public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
        return new ItemStack(AtumItems.PAPYRUS_PLANT);
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AGE, TOP);
    }

    @Override
    @Nonnull
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        BlockPos upperPos = pos.add(0, 1, 0);
        return state.withProperty(TOP, world.isAirBlock(upperPos));
    }

    @Override
    public IProperty[] getNonRenderingProperties() {
        return new IProperty[]{AGE};
    }
}
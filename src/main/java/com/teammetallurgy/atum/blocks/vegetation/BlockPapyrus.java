package com.teammetallurgy.atum.blocks.vegetation;

import com.teammetallurgy.atum.blocks.base.IRenderMapper;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockPapyrus extends SugarCaneBlock implements IRenderMapper {
    private static final PropertyBool TOP = PropertyBool.create("top");

    public BlockPapyrus() {
        super();
        this.setHardness(0.0F);
        this.setSoundType(SoundType.PLANT);
        this.setTickRandomly(true);
        this.setDefaultState(this.blockState.getBaseState().with(AGE, 0).with(TOP, false));
    }

    @Override
    public void updateTick(World world, BlockPos pos, @Nonnull BlockState state, Random rand) {
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
                            world.setBlockState(pos, state.with(AGE, 0), 4);
                        } else {
                            world.setBlockState(pos, state.with(AGE, j + 1), 4);
                        }
                        ForgeHooks.onCropsGrowPost(world, pos, state, world.getBlockState(pos));
                    }
                }
            }
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, @Nonnull BlockPos pos) {
        BlockState state = world.getBlockState(pos.down());
        Block block = state.getBlock();
        if (block.canSustainPlant(state, world, pos.down(), Direction.UP, this)) return true;

        if (block == this) {
            return true;
        } else if (block != AtumBlocks.FERTILE_SOIL && block != AtumBlocks.FERTILE_SOIL_TILLED && block != AtumBlocks.SAND) {
            return false;
        } else {
            BlockPos blockpos = pos.down();

            for (Direction Direction : Direction.Plane.HORIZONTAL) {
                BlockState BlockState = world.getBlockState(blockpos.offset(Direction));

                if (BlockState.getMaterial() == Material.WATER) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public boolean canSustainPlant(@Nonnull BlockState state, @Nonnull IBlockReader world, BlockPos pos, @Nonnull Direction direction, IPlantable plantable) {
        BlockState plant = plantable.getPlant(world, pos.offset(direction));
        if (plant.getBlock() == AtumBlocks.PAPYRUS && this == AtumBlocks.PAPYRUS) {
            return true;
        }
        return super.canSustainPlant(state, world, pos, direction, plantable);
    }

    @Override
    @Nonnull
    public Item getItemDropped(BlockState state, Random rand, int fortune) {
        return AtumItems.PAPYRUS_PLANT;
    }

    @Override
    @Nonnull
    public ItemStack getPickBlock(@Nonnull BlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, PlayerEntity player) {
        return new ItemStack(AtumItems.PAPYRUS_PLANT);
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AGE, TOP);
    }

    @Override
    @Nonnull
    public BlockState getActualState(@Nonnull BlockState state, IBlockReader world, BlockPos pos) {
        BlockPos upperPos = pos.add(0, 1, 0);
        return state.with(TOP, world.isAirBlock(upperPos));
    }

    @Override
    public IProperty[] getNonRenderingProperties() {
        return new IProperty[]{AGE};
    }
}
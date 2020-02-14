package com.teammetallurgy.atum.blocks.stone.limestone;

import com.teammetallurgy.atum.blocks.machines.KilnBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LimestoneBrickBlock extends Block {
    public static final BooleanProperty UNBREAKABLE = BooleanProperty.create("unbreakable");

    public LimestoneBrickBlock() {
        super(Block.Properties.create(Material.ROCK, MaterialColor.SAND).hardnessAndResistance(1.5F, 8.0F));
        this.setDefaultState(this.stateContainer.getBaseState().with(UNBREAKABLE, false));
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, @Nonnull ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);

        if (state.getBlock() == AtumBlocks.LIMESTONE_BRICK_SMALL) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        BlockPos checkPos = pos.add(dx, dy, dz);
                        BlockState kilnState = world.getBlockState(checkPos);
                        if (kilnState.getBlock() == AtumBlocks.KILN) {
                            KilnBlock kiln = (KilnBlock) kilnState.getBlock();
                            kiln.tryMakeMultiblock(world, checkPos, kilnState);
                        }
                    }
                }
            }
        }
    }

    @Override
    public float getBlockHardness(BlockState state, IBlockReader world, BlockPos pos) {
        return state.get(UNBREAKABLE) ? -1.0F : super.getBlockHardness(state, world, pos);
    }

    @Override
    public float getExplosionResistance(BlockState state, IWorldReader world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
        return world.getBlockState(pos).get(UNBREAKABLE) ? 6000000.0F : super.getExplosionResistance(state, world, pos, exploder, explosion);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
        container.add(UNBREAKABLE);
    }

    /*@Override
    public Property[] getNonRenderingProperties() { //TODO
        return new Property[]{UNBREAKABLE};
    }*/
}
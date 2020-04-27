package com.teammetallurgy.atum.world.gen.carver;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.Dynamic;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.carver.ICarverConfig;
import net.minecraft.world.gen.carver.WorldCarver;

import javax.annotation.Nonnull;
import java.util.BitSet;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public abstract class AtumCarver<C extends ICarverConfig> extends WorldCarver<C> {

    public AtumCarver(Function<Dynamic<?>, ? extends C> config, int maxHeight) {
        super(config, maxHeight);
        this.carvableBlocks = ImmutableSet.of(AtumBlocks.LIMESTONE, AtumBlocks.SAND, AtumBlocks.FERTILE_SOIL, AtumBlocks.LIMESTONE_GRAVEL);
    }

    @Override
    protected boolean func_225556_a_(@Nonnull IChunk chunk, @Nonnull Function<BlockPos, Biome> biomePos, BitSet bitSet, @Nonnull Random rand, @Nonnull BlockPos.Mutable pos, @Nonnull BlockPos.Mutable pos1, @Nonnull BlockPos.Mutable pos2, int i, int i1, int i2, int x, int z, int i5, int y, int i7, @Nonnull AtomicBoolean atomicBoolean) {
        int bit = i5 | i7 << 4 | y << 8;
        if (bitSet.get(bit)) {
            return false;
        } else {
            bitSet.set(bit);
            pos.setPos(x, y, z);
            BlockState state = chunk.getBlockState(pos);
            BlockState stateUp = chunk.getBlockState(pos1.setPos(pos).move(Direction.UP));
            if (state.getBlock() == AtumBlocks.SAND) {
                atomicBoolean.set(true);
            }

            if (!this.canCarveBlock(state, stateUp)) {
                return false;
            } else {
                if (y < 11) {
                    chunk.setBlockState(pos, LAVA.getBlockState(), false);
                } else {
                    chunk.setBlockState(pos, CAVE_AIR, false);
                    if (atomicBoolean.get()) {
                        pos2.setPos(pos).move(Direction.DOWN);
                        if (chunk.getBlockState(pos2).getBlock() == AtumBlocks.SAND) {
                            chunk.setBlockState(pos2, biomePos.apply(pos).getSurfaceBuilderConfig().getTop(), false);
                        }
                    }
                }
                return true;
            }
        }
    }

    @Override
    protected boolean canCarveBlock(BlockState state, @Nonnull BlockState aboveState) {
        Block block = state.getBlock();
        return this.isCarvable(state) || (block == AtumBlocks.FERTILE_SOIL || block == AtumBlocks.LIMESTONE_GRAVEL) && !aboveState.getFluidState().isTagged(FluidTags.WATER);
    }
}
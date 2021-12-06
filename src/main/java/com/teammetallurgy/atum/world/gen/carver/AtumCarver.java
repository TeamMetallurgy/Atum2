package com.teammetallurgy.atum.world.gen.carver;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import org.apache.commons.lang3.mutable.MutableBoolean;

import javax.annotation.Nonnull;
import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;

public abstract class AtumCarver<C extends CarverConfiguration> extends WorldCarver<C> {

    public AtumCarver(Codec<C> config, int maxHeight) {
        super(config, maxHeight);
        this.replaceableBlocks = ImmutableSet.of(AtumBlocks.LIMESTONE, AtumBlocks.SAND, AtumBlocks.FERTILE_SOIL, AtumBlocks.LIMESTONE_GRAVEL);
    }

    @Override
    protected boolean carveBlock(@Nonnull ChunkAccess chunk, @Nonnull Function<BlockPos, Biome> biomePos, BitSet bitSet, @Nonnull Random rand, @Nonnull BlockPos.MutableBlockPos pos, @Nonnull BlockPos.MutableBlockPos pos1, @Nonnull BlockPos.MutableBlockPos pos2, int i, int i1, int i2, int x, int z, int i5, int y, int i7, @Nonnull MutableBoolean mutableBoolean) {
        int bit = i5 | i7 << 4 | y << 8;
        if (bitSet.get(bit)) {
            return false;
        } else {
            bitSet.set(bit);
            pos.set(x, y, z);
            BlockState state = chunk.getBlockState(pos);
            BlockState stateUp = chunk.getBlockState(pos1.set(pos).move(Direction.UP));
            if (state.getBlock() == AtumBlocks.SAND) {
                mutableBoolean.setTrue();
            }

            if (!this.canReplaceBlock(state, stateUp)) {
                return false;
            } else {
                if (y < 11) {
                    chunk.setBlockState(pos, LAVA.createLegacyBlock(), false);
                } else {
                    chunk.setBlockState(pos, CAVE_AIR, false);
                    if (mutableBoolean.isTrue()) {
                        pos2.set(pos).move(Direction.DOWN);
                        if (chunk.getBlockState(pos2).getBlock() == AtumBlocks.SAND) {
                            chunk.setBlockState(pos2, biomePos.apply(pos).getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial(), false);
                        }
                    }
                }
                return true;
            }
        }
    }

    @Override
    protected boolean canReplaceBlock(BlockState state, @Nonnull BlockState aboveState) {
        Block block = state.getBlock();
        return this.canReplaceBlock(state) || (block == AtumBlocks.FERTILE_SOIL || block == AtumBlocks.LIMESTONE_GRAVEL) && !aboveState.getFluidState().is(FluidTags.WATER);
    }
}
package com.teammetallurgy.atum.world.gen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;

public class DoubleBlockStateFeatureConfig extends BlockStateFeatureConfig {
    public static final Codec<DoubleBlockStateFeatureConfig> DOUBLE_STATE_CODEC = RecordCodecBuilder.create((p_236683_0_) -> {
        return p_236683_0_.group(BlockState.CODEC.fieldOf("state").forGetter((p_236693_0_) -> {
            return p_236693_0_.state;
        }), BlockState.CODEC.fieldOf("state2").forGetter((p_236692_0_) -> {
            return p_236692_0_.state2;
        })).apply(p_236683_0_, DoubleBlockStateFeatureConfig::new);
    });
    public final BlockState state2;

    public DoubleBlockStateFeatureConfig(BlockState state1, BlockState state2) {
        super(state1);
        this.state2 = state2;
    }
}
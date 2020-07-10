package com.teammetallurgy.atum.world.gen.feature.config;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;

import javax.annotation.Nonnull;

public class DoubleBlockStateFeatureConfig extends BlockStateFeatureConfig {
    public final BlockState state2;

    public DoubleBlockStateFeatureConfig(BlockState state1, BlockState state2) {
        super(state1);
        this.state2 = state2;
    }

    @Override
    @Nonnull
    public <T> Dynamic<T> serialize(@Nonnull DynamicOps<T> d) {
        return new Dynamic<>(d, d.createMap(ImmutableMap.of(d.createString("state"), BlockState.serialize(d, this.state).getValue(), d.createString("state2"), BlockState.serialize(d, this.state2).getValue())));
    }

    public static <T> DoubleBlockStateFeatureConfig deserializeDouble(Dynamic<T> d) {
        BlockState state = d.get("state").map(BlockState::deserialize).orElse(Blocks.AIR.getDefaultState());
        BlockState state2 = d.get("state2").map(BlockState::deserialize).orElse(Blocks.AIR.getDefaultState());
        return new DoubleBlockStateFeatureConfig(state, state2);
    }
}
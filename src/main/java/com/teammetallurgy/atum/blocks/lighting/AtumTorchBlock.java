package com.teammetallurgy.atum.blocks.lighting;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.init.AtumParticles;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AtumTorchBlock extends TorchBlock implements INebuTorch {
    public static final List<Block> ALL_TORCHES = new ArrayList<>();
    public static final Map<Block, Block> UNLIT = Maps.newHashMap();
    public static final Map<Block, Block> LIT = Maps.newHashMap();

    //Flame particles
    public static final HashMap<God, ParticleOptions> GOD_FLAMES = new HashMap<>();
    public static final HashMap<ParticleOptions, God> GODS = new HashMap<>();

    public AtumTorchBlock(int lightValue, ParticleOptions particleType) {
        super(Block.Properties.of(Material.DECORATION).noCollission().strength(0.0F).lightLevel(s -> lightValue).sound(SoundType.WOOD), particleType);
    }

    public AtumTorchBlock(int lightValue) {
        this(lightValue, ParticleTypes.FLAME);
    }

    public AtumTorchBlock(@Nullable God god) {
        this(14, god == null ? AtumParticles.NEBU_FLAME : GOD_FLAMES.get(god));
    }

    public ParticleOptions getParticleType() {
        return this.flameParticle;
    }

    @Override
    public boolean isNebuTorch() {
        return this.getParticleType() != ParticleTypes.FLAME;
    }

    @Override
    public God getGod() {
        return GODS.get(this.getParticleType());
    }
}
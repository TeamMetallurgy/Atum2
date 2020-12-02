package com.teammetallurgy.atum.blocks.base;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.client.particle.NebuFlameParticle;
import com.teammetallurgy.atum.init.AtumParticles;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.material.Material;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AtumTorchBlock extends TorchBlock {
    public static final List<Block> ALL_TORCHES = new ArrayList<>();
    public static final Map<Block, Block> UNLIT = Maps.newHashMap();
    public static final Map<Block, Block> LIT = Maps.newHashMap();
    private BasicParticleType particleType = ParticleTypes.FLAME;

    public AtumTorchBlock(int lightValue, BasicParticleType particleType) {
        super(Block.Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.0F).setLightLevel(s -> lightValue).sound(SoundType.WOOD), particleType);
    }

    public AtumTorchBlock(int lightValue) {
        this(lightValue, ParticleTypes.FLAME);
    }

    public AtumTorchBlock(@Nullable God god) {
        this(14, god == null ? AtumParticles.NEBU_FLAME : NebuFlameParticle.GOD_FLAMES.get(god));
        this.particleType = god == null ? AtumParticles.NEBU_FLAME : NebuFlameParticle.GOD_FLAMES.get(god);
    }

    public BasicParticleType getParticleType() {
        return particleType;
    }
}
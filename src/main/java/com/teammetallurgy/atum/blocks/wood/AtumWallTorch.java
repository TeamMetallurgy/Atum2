package com.teammetallurgy.atum.blocks.wood;

import net.minecraft.block.WallTorchBlock;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;

public class AtumWallTorch extends WallTorchBlock {

    public AtumWallTorch(Properties properties) {
        super(properties, ParticleTypes.FLAME);
    }

    public AtumWallTorch(Properties properties, BasicParticleType particleType) {
        super(properties, particleType);
    }
}
package com.teammetallurgy.atum.blocks.lighting;

import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.client.particle.NebuFlameParticle;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;

public class AtumWallTorch extends WallTorchBlock implements INebuTorch {

    public AtumWallTorch(Properties properties) {
        super(properties, ParticleTypes.FLAME);
    }

    public AtumWallTorch(Properties properties, IParticleData particleType) {
        super(properties, particleType);
    }

    @Override
    public boolean isNebuTorch() {
        return this.particleData != ParticleTypes.FLAME;
    }

    @Override
    public God getGod() {
        return NebuFlameParticle.GODS.get(this.particleData);
    }
}
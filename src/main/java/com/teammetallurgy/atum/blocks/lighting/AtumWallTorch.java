package com.teammetallurgy.atum.blocks.lighting;

import com.teammetallurgy.atum.api.God;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.WallTorchBlock;

public class AtumWallTorch extends WallTorchBlock implements INebuTorch {

    public AtumWallTorch(Properties properties) {
        super(properties, ParticleTypes.FLAME);
    }

    public AtumWallTorch(Properties properties, ParticleOptions particleType) {
        super(properties, particleType);
    }

    @Override
    public boolean isNebuTorch() {
        return this.flameParticle != ParticleTypes.FLAME;
    }

    @Override
    public God getGod() {
        return AtumTorchBlock.GODS.get(this.flameParticle);
    }
}
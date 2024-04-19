package com.teammetallurgy.atum.blocks.lighting;

import com.teammetallurgy.atum.api.God;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class AtumWallTorch extends WallTorchBlock implements INebuTorch {
    private final Supplier<SimpleParticleType> flameParticle;

    public AtumWallTorch(Properties properties) {
        super(ParticleTypes.FLAME, properties);
        this.flameParticle = () -> ParticleTypes.FLAME;
    }

    public AtumWallTorch(Properties properties, Supplier<SimpleParticleType> particleType) {
        super(null, properties);
        this.flameParticle = particleType;
    }

    @Override
    public boolean isNebuTorch() {
        return this.flameParticle.get() != ParticleTypes.FLAME;
    }

    @Override
    public God getGod() {
        return AtumTorchBlock.GODS.get(this.flameParticle);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, @Nonnull RandomSource random) {
        Direction direction = state.getValue(FACING);
        double d0 = (double)pos.getX() + 0.5D;
        double d1 = (double)pos.getY() + 0.7D;
        double d2 = (double)pos.getZ() + 0.5D;
        Direction direction1 = direction.getOpposite();
        level.addParticle(ParticleTypes.SMOKE, d0 + 0.27D * (double)direction1.getStepX(), d1 + 0.22D, d2 + 0.27D * (double)direction1.getStepZ(), 0.0D, 0.0D, 0.0D);
        level.addParticle(this.flameParticle.get(), d0 + 0.27D * (double)direction1.getStepX(), d1 + 0.22D, d2 + 0.27D * (double)direction1.getStepZ(), 0.0D, 0.0D, 0.0D);
    }
}
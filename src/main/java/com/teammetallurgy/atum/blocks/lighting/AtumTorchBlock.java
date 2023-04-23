package com.teammetallurgy.atum.blocks.lighting;

import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.init.AtumParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class AtumTorchBlock extends TorchBlock implements INebuTorch {
    //Flame particles
    public static final HashMap<God, Supplier<SimpleParticleType>> GOD_FLAMES = new HashMap<>();
    public static final HashMap<Supplier<SimpleParticleType>, God> GODS = new HashMap<>();
    protected final Supplier<SimpleParticleType> flameParticle;

    public AtumTorchBlock(int lightValue, Supplier<SimpleParticleType> particleType) {
        super(Block.Properties.of(Material.DECORATION).noCollission().strength(0.0F).lightLevel(s -> lightValue).sound(SoundType.WOOD), null);
        this.flameParticle = particleType;
    }

    public AtumTorchBlock(int lightValue) {
        this(lightValue, () -> ParticleTypes.FLAME);
    }

    public AtumTorchBlock(@Nullable God god) {
        this(14, god == null ? AtumParticles.NEBU_FLAME : GOD_FLAMES.get(god));
    }

    public Supplier<SimpleParticleType> getParticleType() {
        return this.flameParticle;
    }

    @Override
    public boolean isNebuTorch() {
        return this.getParticleType().get() != ParticleTypes.FLAME;
    }

    @Override
    public God getGod() {
        return GODS.get(this.getParticleType());
    }

    @Override
    public void animateTick(@Nonnull BlockState state, Level level, BlockPos pos, @Nonnull RandomSource random) {
        double x = (double)pos.getX() + 0.5D;
        double y = (double)pos.getY() + 0.7D;
        double z = (double)pos.getZ() + 0.5D;
        level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
        level.addParticle(this.flameParticle.get(), x, y, z, 0.0D, 0.0D, 0.0D);
    }
}
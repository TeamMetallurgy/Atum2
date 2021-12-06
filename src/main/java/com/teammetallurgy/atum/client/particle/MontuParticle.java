package com.teammetallurgy.atum.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class MontuParticle extends TextureSheetParticle {
    float scale;

    public MontuParticle(ClientLevel world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        this(world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, 1.0F, spriteSet);
    }

    public MontuParticle(ClientLevel world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, float scale, SpriteSet spriteSet) {
        super(world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed);
        this.xd *= 0.10000000149011612D;
        this.yd *= 0.10000000149011612D;
        this.zd *= 0.10000000149011612D;
        this.xd += xSpeed;
        this.yd += ySpeed;
        this.zd += zSpeed;
        float f = (float) (Math.random() * 0.30000001192092896D);
        this.rCol = f;
        this.gCol = f;
        this.bCol = f;
        this.quadSize *= 0.75F;
        this.quadSize *= scale;
        this.scale = this.quadSize;
        this.lifetime = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
        this.lifetime = (int) ((float) this.lifetime * scale);
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    @Nonnull
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void render(VertexConsumer builder, Camera renderInfo, float partialTicks) {
        float f = ((float) this.age + partialTicks) / (float) this.lifetime * 32.0F;
        f = Mth.clamp(f, 0.0F, 1.0F);
        this.quadSize = this.scale * f;
        super.render(builder, renderInfo, partialTicks);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
        }
        this.yd += 0.004D;
        this.move(this.xd, this.yd, this.zd);

        if (this.y == this.yo) {
            this.xd *= 1.1D;
            this.zd *= 1.1D;
        }
        this.xd *= 0.9599999785423279D;
        this.yd *= 0.9599999785423279D;
        this.zd *= 0.9599999785423279D;

        if (this.onGround) {
            this.xd *= 0.699999988079071D;
            this.zd *= 0.699999988079071D;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(@Nonnull SimpleParticleType particleType, @Nonnull ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new MontuParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}
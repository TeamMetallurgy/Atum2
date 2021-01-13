package com.teammetallurgy.atum.entity.projectile;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.entity.projectile.arrow.CustomArrow;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SChangeGameStatePacket;
import net.minecraft.util.*;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.FMLPlayMessages;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PharaohOrbEntity extends CustomArrow implements IEntityAdditionalSpawnData {
    private final God god;

    public PharaohOrbEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        super(AtumEntities.PHARAOH_ORB, world);
        this.pickupStatus = PickupStatus.DISALLOWED;
        this.setDamage(this.getOrbDamage());
        this.god = God.getGodByName(spawnEntity.getAdditionalData().readString());
    }

    public PharaohOrbEntity(EntityType<? extends PharaohOrbEntity> entityType, World world) {
        super(entityType, world);
        this.pickupStatus = PickupStatus.DISALLOWED;
        this.setDamage(this.getOrbDamage());
        this.god = God.ATEM;
    }

    public PharaohOrbEntity(World world, LivingEntity shooter, God god) {
        super(AtumEntities.PHARAOH_ORB, world, shooter);
        this.pickupStatus = PickupStatus.DISALLOWED;
        this.setDamage(this.getOrbDamage());
        this.god = god;
    }

    public God getGod() {
        return this.god;
    }

    public int getOrbDamage() {
        return 6;
    }

    @Override
    @Nonnull
    protected ItemStack getArrowStack() {
        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    protected SoundEvent getHitEntitySound() {
        return SoundEvents.BLOCK_ANCIENT_DEBRIS_HIT;
    }

    @Override
    protected void func_225516_i_() {
        this.remove();
    }

    public static DamageSource causeOrbDamage(PharaohOrbEntity pharaohOrbEntity, @Nullable Entity indirectEntity) {
        return (new IndirectEntityDamageSource("atum:pharaoh_orb", pharaohOrbEntity, indirectEntity)).setDifficultyScaled().setProjectile();
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult rayTrace) {
        Entity entity = rayTrace.getEntity();
        float f = (float) this.getMotion().length();
        int i = MathHelper.ceil(MathHelper.clamp((double) f * this.getDamage(), 0.0D, 2.147483647E9D));

        if (this.getIsCritical()) {
            long j = this.rand.nextInt(i / 2 + 2);
            i = (int) Math.min(j + (long) i, 2147483647L);
        }

        Entity entity1 = this.func_234616_v_();
        DamageSource damagesource = causeOrbDamage(this, entity1);
        if (entity1 instanceof LivingEntity) {
            ((LivingEntity) entity1).setLastAttackedEntity(entity);
        }

        int fireTimer = entity.getFireTimer();

        if (entity.attackEntityFrom(damagesource, (float) i)) {
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;

                if (this.knockbackStrength > 0) {
                    Vector3d vector3d = this.getMotion().mul(1.0D, 0.0D, 1.0D).normalize().scale((double) this.knockbackStrength * 0.6D);
                    if (vector3d.lengthSquared() > 0.0D) {
                        livingEntity.addVelocity(vector3d.x, 0.1D, vector3d.z);
                    }
                }

                if (!this.world.isRemote && entity1 instanceof LivingEntity) {
                    EnchantmentHelper.applyThornEnchantments(livingEntity, entity1);
                    EnchantmentHelper.applyArthropodEnchantments((LivingEntity) entity1, livingEntity);
                }

                this.arrowHit(livingEntity);
                if (entity1 != null && livingEntity != entity1 && livingEntity instanceof PlayerEntity && entity1 instanceof ServerPlayerEntity && !this.isSilent()) {
                    ((ServerPlayerEntity) entity1).connection.sendPacket(new SChangeGameStatePacket(SChangeGameStatePacket.field_241770_g_, 0.0F));
                }
            }

            this.playSound(this.getHitEntitySound(), 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
            if (this.getPierceLevel() <= 0) {
                this.remove();
            }
        } else {
            entity.forceFireTicks(fireTimer);
            this.setMotion(this.getMotion().scale(-0.1D));
            this.rotationYaw += 180.0F;
            this.prevRotationYaw += 180.0F;
            if (!this.world.isRemote && this.getMotion().lengthSquared() < 1.0E-7D) {
                this.remove();
            }
        }
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Atum.MOD_ID, "textures/entity/orb/orb_" + (this.getGod() != null ? this.getGod().getName() : God.ATEM.getName()) + ".png");
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeString(this.god.getName());
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {

    }
}
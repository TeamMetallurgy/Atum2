package com.teammetallurgy.atum.entity.projectile;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.blocks.lighting.AtumTorchBlock;
import com.teammetallurgy.atum.entity.projectile.arrow.CustomArrow;
import com.teammetallurgy.atum.entity.undead.PharaohEntity;
import com.teammetallurgy.atum.init.AtumDamageTypes;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.items.artifacts.horus.HorusAscensionItem;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PlayMessages;

import javax.annotation.Nonnull;

public class PharaohOrbEntity extends CustomArrow implements IEntityAdditionalSpawnData {
    private final God god;
    //Montu Berserk
    private static final Object2FloatMap<Entity> BERSERK_TIMER = new Object2FloatOpenHashMap<>();
    private static final Object2FloatMap<Entity> BERSERK_DAMAGE = new Object2FloatOpenHashMap<>();

    public PharaohOrbEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        super(AtumEntities.PHARAOH_ORB.get(), level);
        this.pickup = Pickup.DISALLOWED;
        this.setBaseDamage(this.getOrbDamage());
        this.god = God.getGodByName(spawnEntity.getAdditionalData().readUtf());
        MinecraftForge.EVENT_BUS.register(this);
    }

    public PharaohOrbEntity(EntityType<? extends PharaohOrbEntity> entityType, Level level) {
        super(entityType, level);
        this.pickup = Pickup.DISALLOWED;
        this.setBaseDamage(this.getOrbDamage());
        this.god = God.ATEM;
    }

    public PharaohOrbEntity(Level level, PharaohEntity shooter, God god) {
        super(AtumEntities.PHARAOH_ORB.get(), level, shooter.getX(), shooter.getEyeY() - (double) 0.3F, shooter.getZ());
        this.setOwner(shooter);
        this.pickup = Pickup.DISALLOWED;
        this.setBaseDamage(this.getOrbDamage());
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
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.GENERIC_EXTINGUISH_FIRE;
    }

    @Override
    protected void tickDespawn() {
        this.discard();
    }

    @Override
    public void tick() {
        super.tick();
        this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.01D, 0.0D)); //Decrease arc

        //Montu Berserk
        if (this.getGod() == God.MONTU) {
            Entity pharaoh = this.getOwner();
            float berserkTimer = BERSERK_TIMER.getFloat(this.getOwner());
            if (berserkTimer > 1) {
                berserkTimer--;
                BERSERK_TIMER.put(pharaoh, berserkTimer);
            }
            if (berserkTimer == 1) {
                BERSERK_DAMAGE.removeFloat(pharaoh);
                BERSERK_TIMER.removeFloat(pharaoh);
            }
        }

        //Particle
        if (this.level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel) level;
            serverLevel.sendParticles(AtumTorchBlock.GOD_FLAMES.get(this.getGod()).get(), this.getX() + (level.random.nextDouble() - 0.5D) * (double) this.getBbWidth(), this.getY() + level.random.nextDouble() * (double) this.getBbHeight(), this.getZ() + (level.random.nextDouble() - 0.5D) * (double) this.getBbWidth(), 2, 0.0D, 0.0D, 0.0D, 0.01D);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult rayTrace) {
        Entity entity = rayTrace.getEntity();
        float f = (float) this.getDeltaMovement().length();
        int i = Mth.ceil(Mth.clamp((double) f * this.getBaseDamage(), 0.0D, 2.147483647E9D));

        Entity entity1 = this.getOwner();
        if (entity1 instanceof LivingEntity) {
            ((LivingEntity) entity1).setLastHurtMob(entity);
        }

        int fireTimer = entity.getRemainingFireTicks();

        if (!(entity instanceof PharaohEntity) && entity.hurt(this.level.damageSources().source(AtumDamageTypes.PHARAOH_ORB), (float) i)) {
            if (entity instanceof LivingEntity livingEntity) {

                if (this.getKnockback() > 0) {
                    Vec3 vector3d = this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale((double) this.getKnockback() * 0.6D);
                    if (vector3d.lengthSqr() > 0.0D) {
                        livingEntity.push(vector3d.x, 0.1D, vector3d.z);
                    }
                }

                if (!this.level.isClientSide && entity1 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingEntity, entity1);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) entity1, livingEntity);
                }

                this.doPostHurtEffects(livingEntity);
                Entity shooter = this.getOwner();
                if (!this.level.isClientSide && shooter instanceof LivingEntity) {
                    this.doGodSpecificEffect(this.getGod(), (LivingEntity) shooter, livingEntity);
                }
                if (entity1 != null && livingEntity != entity1 && livingEntity instanceof Player && entity1 instanceof ServerPlayer && !this.isSilent()) {
                    ((ServerPlayer) entity1).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
                }
            }

            this.playSound(this.getDefaultHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            if (this.getPierceLevel() <= 0) {
                this.discard();
            }
        } else {
            entity.setRemainingFireTicks(fireTimer);
            this.setDeltaMovement(this.getDeltaMovement().scale(-0.1D));
            this.setYRot(this.getYRot() + 180.0F);
            this.yRotO += 180.0F;
            if (!this.level.isClientSide && this.getDeltaMovement().lengthSqr() < 1.0E-7D) {
                this.discard();
            }
        }
        this.discard();
    }

    public void doGodSpecificEffect(God god, LivingEntity shooter, LivingEntity target) {
        switch (god) {
            case ANPUT:
                target.addEffect(new MobEffectInstance(MobEffects.HUNGER, 80, 1));
                break;
            case ANUBIS:
                target.addEffect(new MobEffectInstance(MobEffects.WITHER, 60, 1));
                break;
            case ATEM:
                target.addEffect(new MobEffectInstance(MobEffects.HARM, 1, 0));
                break;
            case GEB:
                target.knockback(1.5F, Mth.sin(this.getYRot() * ((float) Math.PI / 180F)), -Mth.cos(this.getYRot() * ((float) Math.PI / 180F)));
                break;
            case HORUS:
                target.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 60, 1));
                break;
            case ISIS:
                shooter.heal(10);
                break;
            case NUIT:
                target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0));
                break;
            case NEPTHYS:
                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 120));
                break;
            case PTAH:
                this.applyReverseKnockback(target, 2.0F, Mth.sin(this.getYRot() * ((float) Math.PI / 180F)), -Mth.cos(this.getYRot() * ((float) Math.PI / 180F)));
                break;
            case RA:
                target.setSecondsOnFire(3);
                break;
            case SETH:
                target.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0));
                break;
            case SHU:
                HorusAscensionItem.knockUp(target, shooter, this.random);
                break;
            case TEFNUT:
                target.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100));
                break;
            default:
                break;
        }
    }

    public void applyReverseKnockback(LivingEntity target, float reverseStrength, double ratioX, double ratioZ) {
        reverseStrength = (float) ((double) reverseStrength * (1.0D - target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)));
        if (!(reverseStrength <= 0.0F)) {
            target.hasImpulse = true;
            Vec3 vector3d = target.getDeltaMovement();
            Vec3 vector3d1 = (new Vec3(ratioX, 0.0D, ratioZ)).normalize().scale(reverseStrength);
            target.setDeltaMovement(vector3d.x / 2.0D - vector3d1.x, target.isOnGround() ? -Math.min(0.4D, vector3d.y / 2.0D + (double) reverseStrength) : vector3d.y, -(vector3d.z / 2.0D - vector3d1.z));
        }
    }

    @SubscribeEvent
    public void onBerserk(LivingHurtEvent event) { //Default Orb damage i 6.0
        if (event.getEntity().getLevel() instanceof ServerLevel) {
            if (event.getSource().is(AtumDamageTypes.PHARAOH_ORB)) {
                if (this.getGod() == God.MONTU) {
                    Entity pharaoh = this.getOwner();
                    float berserkDamage = BERSERK_DAMAGE.getFloat(pharaoh);
                    event.setAmount(berserkDamage);
                }
            }
            if (event.getSource().is(DamageTypes.PLAYER_ATTACK)) {
                if (event.getEntity() instanceof PharaohEntity pharaoh) {
                    if (God.getGod(pharaoh.getVariant()) == God.MONTU) {
                        BERSERK_TIMER.put(this.getOwner(), 60);
                        float berserkDamage =  BERSERK_DAMAGE.getFloat(this.getOwner()) + (6.0F * 0.10F); //Add 10% damage for each time the player hits the Montu Pharaoh, while timer is active
                        BERSERK_DAMAGE.put(this.getOwner(), berserkDamage);
                    }
                }
            }
        }
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Atum.MOD_ID, "textures/entity/orb/orb_" + (this.getGod() != null ? this.getGod().getName() : God.ATEM.getName()) + ".png");
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.god.getName());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
    }
}
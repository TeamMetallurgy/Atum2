package com.teammetallurgy.atum.entity.projectile;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.blocks.lighting.AtumTorchBlock;
import com.teammetallurgy.atum.entity.projectile.arrow.CustomArrow;
import com.teammetallurgy.atum.entity.undead.PharaohEntity;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.items.artifacts.horus.HorusAscensionItem;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SChangeGameStatePacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.FMLPlayMessages;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class PharaohOrbEntity extends CustomArrow implements IEntityAdditionalSpawnData {
    private final God god;
    //Montu Berserk
    private static int berserkTimer;
    private static float berserkDamage;

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

    public PharaohOrbEntity(World world, PharaohEntity shooter, God god) {
        super(AtumEntities.PHARAOH_ORB, world, shooter.getPosX(), shooter.getPosYEye() - (double) 0.3F, shooter.getPosZ());
        this.setShooter(shooter);
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
        return SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE;
    }

    @Override
    protected void func_225516_i_() {
        this.remove();
    }

    @Override
    public void tick() {
        super.tick();
        this.setMotion(this.getMotion().add(0.0D, 0.01D, 0.0D)); //Decrease arc

        //Montu Berserk
        if (this.getGod() == God.MONTU) {
            if (berserkTimer > 1) {
                berserkTimer--;
            }
            if (berserkTimer == 1) {
                berserkDamage = 0;
                berserkTimer = 0;
            }
        }

        //Particle
        if (this.world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) world;
            serverWorld.spawnParticle(AtumTorchBlock.GOD_FLAMES.get(this.getGod()), this.getPosX() + (world.rand.nextDouble() - 0.5D) * (double) this.getWidth(), this.getPosY() + world.rand.nextDouble() * (double) this.getHeight(), this.getPosZ() + (world.rand.nextDouble() - 0.5D) * (double) this.getWidth(), 2, 0.0D, 0.0D, 0.0D, 0.01D);
        }
    }

    public static DamageSource causeOrbDamage(PharaohOrbEntity pharaohOrbEntity, @Nullable Entity indirectEntity) {
        return (new IndirectEntityDamageSource("atum_pharaoh_orb", pharaohOrbEntity, indirectEntity)).setDifficultyScaled().setProjectile();
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult rayTrace) {
        Entity entity = rayTrace.getEntity();
        float f = (float) this.getMotion().length();
        int i = MathHelper.ceil(MathHelper.clamp((double) f * this.getDamage(), 0.0D, 2.147483647E9D));

        Entity entity1 = this.func_234616_v_();
        DamageSource damagesource = causeOrbDamage(this, entity1);
        if (entity1 instanceof LivingEntity) {
            ((LivingEntity) entity1).setLastAttackedEntity(entity);
        }

        int fireTimer = entity.getFireTimer();

        if (!(entity instanceof PharaohEntity) && entity.attackEntityFrom(damagesource, (float) i)) {
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
                Entity shooter = this.func_234616_v_();
                if (!this.world.isRemote && shooter instanceof LivingEntity) {
                    this.doGodSpecificEffect(this.getGod(), (LivingEntity) shooter, livingEntity);
                }
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
        this.remove();
    }

    public void doGodSpecificEffect(God god, LivingEntity shooter, LivingEntity target) {
        switch (god) {
            case ANPUT:
                target.addPotionEffect(new EffectInstance(Effects.HUNGER, 80, 1));
                break;
            case ANUBIS:
                target.addPotionEffect(new EffectInstance(Effects.WITHER, 60, 1));
                break;
            case ATEM:
                target.addPotionEffect(new EffectInstance(Effects.INSTANT_DAMAGE, 1, 0));
                break;
            case GEB:
                target.applyKnockback(1.5F, MathHelper.sin(this.rotationYaw * ((float) Math.PI / 180F)), -MathHelper.cos(this.rotationYaw * ((float) Math.PI / 180F)));
                break;
            case HORUS:
                target.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 60, 1));
                break;
            case ISIS:
                shooter.heal(10);
                break;
            case NUIT:
                target.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 60, 0));
                break;
            case NEPTHYS:
                target.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 120));
                break;
            case PTAH:
                this.applyReverseKnockback(target, 2.0F, MathHelper.sin(this.rotationYaw * ((float) Math.PI / 180F)), -MathHelper.cos(this.rotationYaw * ((float) Math.PI / 180F)));
                break;
            case RA:
                target.setFire(3);
                break;
            case SETH:
                target.addPotionEffect(new EffectInstance(Effects.POISON, 100, 0));
                break;
            case SHU:
                HorusAscensionItem.knockUp(target, shooter, this.rand);
                break;
            case TEFNUT:
                target.addPotionEffect(new EffectInstance(Effects.NAUSEA, 100));
                break;
            default:
                break;
        }
    }

    public void applyReverseKnockback(LivingEntity target, float reverseStrength, double ratioX, double ratioZ) {
        reverseStrength = (float) ((double) reverseStrength * (1.0D - target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)));
        if (!(reverseStrength <= 0.0F)) {
            target.isAirBorne = true;
            Vector3d vector3d = target.getMotion();
            Vector3d vector3d1 = (new Vector3d(ratioX, 0.0D, ratioZ)).normalize().scale(reverseStrength);
            target.setMotion(vector3d.x / 2.0D - vector3d1.x, target.isOnGround() ? -Math.min(0.4D, vector3d.y / 2.0D + (double) reverseStrength) : vector3d.y, -(vector3d.z / 2.0D - vector3d1.z));
        }
    }

    @SubscribeEvent
    public static void onBerserk(LivingHurtEvent event) {
        Entity immediateSource = event.getSource().getImmediateSource();
        if (immediateSource instanceof PharaohOrbEntity) {
            if (((PharaohOrbEntity) immediateSource).getGod() == God.MONTU) {
                if (berserkTimer == 0) {
                    event.setAmount(event.getAmount());
                    berserkDamage = (event.getAmount() / 10) + event.getAmount();
                } else {
                    berserkDamage = berserkDamage + (event.getAmount() / 10);
                    event.setAmount(berserkDamage);
                }
                berserkTimer = 80;
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
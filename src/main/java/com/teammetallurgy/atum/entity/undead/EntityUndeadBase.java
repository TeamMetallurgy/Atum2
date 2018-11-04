package com.teammetallurgy.atum.entity.undead;

import com.teammetallurgy.atum.entity.EntityTarantula;
import com.teammetallurgy.atum.entity.bandit.EntityBanditBase;
import com.teammetallurgy.atum.entity.stone.EntityStoneBase;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityUndeadBase extends EntityMob {
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityUndeadBase.class, DataSerializers.VARINT);
    private String texturePath;

    public EntityUndeadBase(World world) {
        super(world);
    }

    protected boolean hasSkinVariants() {
        return false;
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(6, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        applyEntityAI();
    }

    protected void applyEntityAI() {
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityBanditBase.class, true));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityStoneBase.class, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityAnimal.class, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityTarantula.class, true));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        if (this.hasSkinVariants()) {
            this.dataManager.register(VARIANT, 0);
        }
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);

        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * difficulty.getClampedAdditionalDifficulty());

        if (this.hasSkinVariants()) {
            final int variant = MathHelper.getInt(world.rand, 0, this.getVariantAmount());
            this.setVariant(variant);
        }
        return livingdata;
    }

    protected int getVariantAmount() {
        return 6;
    }

    @Override
    public boolean isPotionApplicable(@Nonnull PotionEffect potionEffect) {
        return potionEffect.getPotion() != MobEffects.POISON && super.isPotionApplicable(potionEffect);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_ZOMBIE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ZOMBIE_DEATH;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.world.isRemote && this.dataManager.isDirty()) {
            this.dataManager.setClean();
            this.texturePath = null;
        }
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.world.isDaytime() && this.world.canSeeSky(this.getPosition()) && !this.world.isRemote && !this.isImmuneToFire && this.shouldBurnInDay()) {
            this.setFire(8);
        }
    }

    @Override
    public void onEntityUpdate() {
        if (this.fire > 0) {
            if (!this.isImmuneToFire) {
                if (this.fire % 20 == 0) {
                    this.attackEntityFrom(DamageSource.ON_FIRE, getBurnDamage());
                }
                --this.fire;
            }
        }
        super.onEntityUpdate();
    }

    protected float getBurnDamage() {
        return 1.0F;
    }

    protected boolean shouldBurnInDay() {
        return true;
    }

    @Override
    @Nonnull
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    @Override
    public boolean getCanSpawnHere() {
        int i = MathHelper.floor(this.getEntityBoundingBox().minY);
        if (i <= 62) {
            return false;
        } else {
            return super.getCanSpawnHere();
        }
    }

    protected void setVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
        this.texturePath = null;
    }

    public int getVariant() {
        return this.dataManager.get(VARIANT);
    }

    @SideOnly(Side.CLIENT)
    public String getTexture() {
        if (this.hasSkinVariants()) {
            if (this.texturePath == null) {
                this.texturePath = String.valueOf(new ResourceLocation(Constants.MOD_ID, "textures/entities/" + this.getName() + "_" + this.getVariant()) + ".png");
            }
        } else {
            this.texturePath = String.valueOf(new ResourceLocation(Constants.MOD_ID, "textures/entities/" + this.getName() + ".png"));
        }
        return this.texturePath;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        if (this.hasSkinVariants()) {
            compound.setInteger("Variant", this.getVariant());
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (this.hasSkinVariants()) {
            this.setVariant(compound.getInteger("Variant"));
        }
    }
}
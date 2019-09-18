package com.teammetallurgy.atum.entity.efreet;

import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

public abstract class EntityEfreetBase extends EntityAgeable {
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityEfreetBase.class, DataSerializers.VARINT);
    private String texturePath;
    private int angerLevel;
    private UUID angerTargetUUID;

    public EntityEfreetBase(World world) {
        super(world);
        this.setSize(0.6F, 1.8F);
        ((PathNavigateGround) this.getNavigator()).setBreakDoors(true);
        this.isImmuneToFire = true;
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(3, new EntityAIMoveIndoors(this));
        this.tasks.addTask(4, new EntityAIRestrictOpenDoor(this));
        this.tasks.addTask(5, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(6, new EntityAIMoveTowardsRestriction(this, 0.6D));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.applyEntityAI();
    }

    protected void applyEntityAI() {
        this.targetTasks.addTask(1, new EntityEfreetBase.AIHurtByAggressor(this));
        this.targetTasks.addTask(2, new EntityEfreetBase.AITargetAggressor(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        if (this.hasSkinVariants()) {
            this.dataManager.register(VARIANT, 0);
        }
    }

    int getVariantAmount() {
        return 3;
    }

    boolean hasSkinVariants() {
        return this.getVariantAmount() > 0;
    }

    void setVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
        this.texturePath = null;
    }

    private int getVariant() {
        return this.dataManager.get(VARIANT);
    }

    @SideOnly(Side.CLIENT)
    public String getTexture() {
        if (this.texturePath == null) {
            String entityName = Objects.requireNonNull(Objects.requireNonNull(EntityRegistry.getEntry(this.getClass())).getRegistryName()).getPath();
            if (this.hasSkinVariants()) {
                this.texturePath = new ResourceLocation(Constants.MOD_ID, "textures/entity/" + entityName + "_" + this.getVariant()) + ".png";
            } else {
                this.texturePath = new ResourceLocation(Constants.MOD_ID, "textures/entity/" + entityName) + ".png";
            }
        }
        return this.texturePath;
    }

    @Override
    @Nonnull
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEFINED;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ILLUSION_ILLAGER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ENTITY_ILLUSION_ILLAGER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ILLAGER_DEATH;
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);

        this.setEquipmentBasedOnDifficulty(difficulty);
        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * difficulty.getClampedAdditionalDifficulty());

        if (this.hasSkinVariants()) {
            final int variant = MathHelper.getInt(world.rand, 0, getVariantAmount());
            this.setVariant(variant);
        }
        return livingdata;
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        //Don't use for now, might do something with it later
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
    @SideOnly(Side.CLIENT)
    public boolean canRenderOnFire() {
        return this.isAngry() && fire > 0;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    protected void updateAITasks() {
        if (this.isAngry() && this.angerTargetUUID != null && this.getRevengeTarget() == null) {
            EntityPlayer player = this.world.getPlayerEntityByUUID(this.angerTargetUUID);
            if (player != null) {
                this.setRevengeTarget(player);
                this.attackingPlayer = player;
                this.recentlyHit = this.getRevengeTimer();
            }
        }
        super.updateAITasks();
    }

    @Override
    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        } else {
            Entity entity = source.getTrueSource();
            if (entity instanceof EntityPlayer) {
                this.becomeAngryAt(entity);
            }
            this.fire = 1000;
            return super.attackEntityFrom(source, amount);
        }
    }

    @Override
    public void setRevengeTarget(@Nullable LivingEntity livingBase) {
        super.setRevengeTarget(livingBase);
        if (livingBase != null) {
            this.angerTargetUUID = livingBase.getUniqueID();
        }
    }

    private void becomeAngryAt(Entity entity) {
        this.angerLevel = 200 + this.rand.nextInt(400);
        if (entity instanceof LivingEntity) {
            this.setRevengeTarget((LivingEntity) entity);
        }
    }

    private boolean isAngry() {
        return this.angerLevel > 0;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        if (this.hasSkinVariants()) {
            compound.setInteger("Variant", this.getVariant());
        }
        compound.setShort("Anger", (short) this.angerLevel);
        if (this.angerTargetUUID != null) {
            compound.setString("HurtBy", this.angerTargetUUID.toString());
        } else {
            compound.setString("HurtBy", "");
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (this.hasSkinVariants()) {
            this.setVariant(compound.getInteger("Variant"));
        }
        this.angerLevel = compound.getShort("Anger");
        String hurtBy = compound.getString("HurtBy");

        if (!hurtBy.isEmpty()) {
            this.angerTargetUUID = UUID.fromString(hurtBy);
            EntityPlayer player = this.world.getPlayerEntityByUUID(this.angerTargetUUID);
            if (player != null) {
                this.setRevengeTarget(player);
                this.attackingPlayer = player;
                this.recentlyHit = this.getRevengeTimer();
            }
        }
    }

    @Override
    public boolean attackEntityAsMob(@Nonnull Entity entity) { //Copied from EntityMob, to allow Efreet to attack
        float attackDamage = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        int knocback = 0;

        if (entity instanceof LivingEntity) {
            attackDamage += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((LivingEntity) entity).getCreatureAttribute());
            knocback += EnchantmentHelper.getKnockbackModifier(this);
        }

        boolean causeDamage = entity.attackEntityFrom(DamageSource.causeMobDamage(this), attackDamage);

        if (causeDamage) {
            if (knocback > 0 && entity instanceof LivingEntity) {
                ((LivingEntity) entity).knockBack(this, (float) knocback * 0.5F, (double) MathHelper.sin(this.rotationYaw * 0.017453292F), (double) (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int fireAspect = EnchantmentHelper.getFireAspectModifier(this);

            if (fireAspect > 0) {
                entity.setFire(fireAspect * 4);
            }

            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                ItemStack heldStack = this.getHeldItemMainhand();
                ItemStack stackActive = player.isHandActive() ? player.getActiveItemStack() : ItemStack.EMPTY;

                if (!heldStack.isEmpty() && !stackActive.isEmpty() && heldStack.getItem().canDisableShield(heldStack, stackActive, player, this) && stackActive.getItem().isShield(stackActive, player)) {
                    float efficiency = 0.25F + (float) EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

                    if (this.rand.nextFloat() < efficiency) {
                        player.getCooldownTracker().setCooldown(stackActive.getItem(), 100);
                        this.world.setEntityState(player, (byte) 30);
                    }
                }
            }
            this.applyEnchantments(this, entity);
        }
        return causeDamage;
    }

    static class AIHurtByAggressor extends EntityAIHurtByTarget {
        AIHurtByAggressor(EntityEfreetBase efreet) {
            super(efreet, true);
        }

        @Override
        protected void setEntityAttackTarget(EntityCreature creature, @Nonnull LivingEntity livingBase) {
            if (creature instanceof EntityEfreetBase) {
                ((EntityEfreetBase) creature).becomeAngryAt(livingBase);
            }
        }
    }

    static class AITargetAggressor extends EntityAINearestAttackableTarget<EntityPlayer> {
        AITargetAggressor(EntityEfreetBase efreet) {
            super(efreet, EntityPlayer.class, true);
        }

        @Override
        public boolean shouldExecute() {
            return ((EntityEfreetBase) this.taskOwner).isAngry();
        }
    }
}
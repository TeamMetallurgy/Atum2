package com.teammetallurgy.atum.entity.animal;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.ai.AICamelCaravan;
import com.teammetallurgy.atum.entity.projectile.EntityCamelSpit;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class EntityCamel extends AbstractChestHorse implements IRangedAttackMob {
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityCamel.class, DataSerializers.VARINT);
    private String texturePath;
    private boolean didSpit;
    private EntityCamel caravanHead;
    private EntityCamel caravanTail;

    public EntityCamel(World worldIn) {
        super(worldIn);
        this.setSize(0.9F, 1.4F);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        if (this.hasSkinVariants()) {
            this.dataManager.register(VARIANT, 0);
        }
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(@Nonnull DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);

        if (hasSkinVariants()) {
            final int variant = MathHelper.getInt(world.rand, 0, getVariantAmount());
            this.setVariant(variant);
        }
        return livingdata;
    }

    public ContainerHorseChest getHorseChest() {
        return horseChest;
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIRunAroundLikeCrazy(this, 1.2D));
        this.tasks.addTask(2, new AICamelCaravan(this, 2.0999999046325684D));
        this.tasks.addTask(3, new EntityAIAttackRanged(this, 1.25D, 40, 20.0F));
        this.tasks.addTask(3, new EntityAIPanic(this, 1.2D));
        this.tasks.addTask(4, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(5, new EntityAIFollowParent(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.7D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityCamel.AIHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityCamel.AIDefendTarget(this));
    }

    @Override
    public void openGUI(@Nonnull EntityPlayer player) {
        if (!this.world.isRemote && (!this.isBeingRidden() || this.isPassenger(player)) && this.isTame()) {
            this.horseChest.setCustomName(this.getName());
            player.openGui(Atum.instance, 3, world, this.getEntityId(), 0, 0);
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variant", this.getVariant());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setVariant(compound.getInteger("Variant"));
    }

    protected int getInventorySize() {
        return this.hasChest() ? 17 * 2 : super.getInventorySize();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_LLAMA_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ENTITY_LLAMA_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_LLAMA_DEATH;
    }

    @Override
    protected void playStepSound(@Nonnull BlockPos pos, Block block) {
        this.playSound(SoundEvents.ENTITY_HORSE_STEP, 0.15F, 1.0F);
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_COW;
    }

    @Override
    public boolean processInteract(EntityPlayer player, @Nonnull EnumHand hand) {
        return super.processInteract(player, hand);
    }

    @Override
    public EntityCamel createChild(@Nonnull EntityAgeable ageable) {
        EntityCamel camel = new EntityCamel(this.world);
        camel.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(ageable)), null);
        return new EntityCamel(this.world);
    }

    @Override
    public float getEyeHeight() {
        return this.isChild() ? this.height : 1.3F;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.world.isRemote && this.dataManager.isDirty()) {
            this.dataManager.setClean();
            this.texturePath = null;
        }
    }

    private int getVariantAmount() {
        return 5;
    }

    private boolean hasSkinVariants() {
        return true;
    }

    private void setVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
        this.texturePath = null;
    }

    private int getVariant() {
        return this.dataManager.get(VARIANT);
    }

    @SideOnly(Side.CLIENT)
    public String getTexture() {
        String entityName = Objects.requireNonNull(Objects.requireNonNull(EntityRegistry.getEntry(this.getClass())).getRegistryName()).getPath();
        if (this.hasSkinVariants()) {
            if (this.texturePath == null) {
                this.texturePath = new ResourceLocation(Constants.MOD_ID, "textures/entities/" + entityName + "_" + this.getVariant()) + ".png";
            }
        } else {
            this.texturePath = String.valueOf(new ResourceLocation(Constants.MOD_ID, "textures/entities/" + entityName + ".png"));
        }
        return this.texturePath;
    }

    @Override
    public void attackEntityWithRangedAttack(@Nonnull EntityLivingBase target, float distanceFactor) {
        this.spit(target);
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {
    }

    private void spit(EntityLivingBase target) {
        EntityCamelSpit camelSpit = new EntityCamelSpit(this.world, this);
        double d0 = target.posX - this.posX;
        double d1 = target.getEntityBoundingBox().minY + (double) (target.height / 3.0F) - camelSpit.posY;
        double d2 = target.posZ - this.posZ;
        float f = MathHelper.sqrt(d0 * d0 + d2 * d2) * 0.2F;
        camelSpit.shoot(d0, d1 + (double) f, d2, 1.5F, 10.0F);
        System.out.println(d0 + " " + d1 + " " + d2);
        this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LLAMA_SPIT, this.getSoundCategory(), 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
        this.world.spawnEntity(camelSpit);
        this.didSpit = true;
    }

    private void setDidSpit(boolean didSpit) {
        this.didSpit = didSpit;
    }

    @Override
    public double getMountedYOffset() {
        return (double) this.height * 0.9D;
    }

    public void leaveCaravan() {
        if (this.caravanHead != null) {
            this.caravanHead.caravanTail = null;
        }
        this.caravanHead = null;
    }

    public void joinCaravan(EntityCamel camel) {
        this.caravanHead = camel;
        this.caravanHead.caravanTail = this;
    }

    public boolean hasCaravanTrail() {
        return this.caravanTail != null;
    }

    public boolean inCaravan() {
        return this.caravanHead != null;
    }

    @Nullable
    public EntityCamel getCaravanHead() {
        return this.caravanHead;
    }

    @Override
    protected double followLeashSpeed() {
        return 2.0D;
    }

    @Override
    protected void followMother() {
        if (!this.inCaravan() && this.isChild()) {
            super.followMother();
        }
    }

    @Override
    public boolean canEatGrass() {
        return false;
    }

    static class AIDefendTarget extends EntityAINearestAttackableTarget<EntityDesertWolf> {
        AIDefendTarget(EntityCamel camel) {
            super(camel, EntityDesertWolf.class, 16, false, true, null);
        }

        @Override
        public boolean shouldExecute() {
            if (super.shouldExecute() && this.targetEntity != null && !this.targetEntity.isTamed()) {
                return true;
            } else {
                this.taskOwner.setAttackTarget(null);
                return false;
            }
        }

        @Override
        protected double getTargetDistance() {
            return super.getTargetDistance() * 0.25D;
        }
    }

    static class AIHurtByTarget extends EntityAIHurtByTarget {
        AIHurtByTarget(EntityCamel camel) {
            super(camel, false);
        }

        @Override
        public boolean shouldContinueExecuting() {
            if (this.taskOwner instanceof EntityCamel) {
                EntityCamel camel = (EntityCamel) this.taskOwner;
                if (camel.didSpit) {
                    camel.setDidSpit(false);
                    return false;
                }
            }
            return super.shouldContinueExecuting();
        }
    }
}
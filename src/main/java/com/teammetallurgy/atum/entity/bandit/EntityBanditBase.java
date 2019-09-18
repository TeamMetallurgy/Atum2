package com.teammetallurgy.atum.entity.bandit;

import com.teammetallurgy.atum.entity.animal.EntityDesertWolf;
import com.teammetallurgy.atum.entity.stone.EntityStoneBase;
import com.teammetallurgy.atum.entity.undead.EntityUndeadBase;
import com.teammetallurgy.atum.integration.champion.ChampionsHelper;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class EntityBanditBase extends MonsterEntity {
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityBanditBase.class, DataSerializers.VARINT);
    private String texturePath;

    EntityBanditBase(World world) {
        super(world);
        this.setSize(0.6F, 1.8F);
        new PathNavigateGround(this, world).setEnterDoors(true);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(7, new EntityAIAvoidEntity<>(this, EntityDesertWolf.class, 6.0F, 1.0D, 1.2D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, PlayerEntity.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.applyEntityAI();
    }

    protected void applyEntityAI() {
        this.targetTasks.addTask(0, new EntityAIHurtByTarget(this, false, EntityBanditBase.class));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, PlayerEntity.class, true));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityUndeadBase.class, true));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityStoneBase.class, true));
    }

    @Override
    public boolean canAttackClass(Class<? extends LivingEntity> cls) {
        return !EntityBanditBase.class.isAssignableFrom(cls) && super.canAttackClass(cls);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        if (this.hasSkinVariants()) {
            this.dataManager.register(VARIANT, 0);
        }
    }

    @Override
    @Nonnull
    protected PathNavigate createNavigator(@Nonnull World world) {
        return new PathNavigateGround(this, world);
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);

        this.setEnchantmentBasedOnDifficulty(difficulty);
        this.setEquipmentBasedOnDifficulty(difficulty);
        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * difficulty.getClampedAdditionalDifficulty());

        if (hasSkinVariants()) {
            final int variant = MathHelper.getInt(world.rand, 0, getVariantAmount());
            this.setVariant(variant);
        }
        return livingdata;
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        //Don't use for now, might do something with it later
    }

    int getVariantAmount() {
        return 6;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.world.isRemote && this.dataManager.isDirty()) {
            this.dataManager.setClean();
            this.texturePath = null;
        }
    }

    boolean hasSkinVariants() {
        return true;
    }

    private void setVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
        this.texturePath = null;
    }

    private int getVariant() {
        return this.dataManager.get(VARIANT);
    }

    @OnlyIn(Dist.CLIENT)
    public String getTexture() {
        if (this.texturePath == null) {
            String entityName = Objects.requireNonNull(Objects.requireNonNull(EntityRegistry.getEntry(this.getClass())).getRegistryName()).getPath();

            if (ChampionsHelper.isChampion(this)) {
                ResourceLocation texture = ChampionsHelper.getTexture(this, entityName);
                if (texture != null) {
                    this.texturePath = texture.toString();
                    return this.texturePath;
                }
            }

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
    protected void playStepSound(BlockPos pos, Block blockIn) {
        super.playStepSound(pos, blockIn);
    }

    @Override
    public boolean getCanSpawnHere() {
        BlockPos pos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
        return pos.getY() > 62 && canSpawnNoHeightCheck(false);
    }

    @Override
    protected boolean isValidLightLevel() {
        BlockPos pos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
        return this.world.getLightFor(EnumSkyBlock.BLOCK, pos) <= 8;
    }

    public boolean canSpawnNoHeightCheck(boolean isFromSpawner) {
        BlockPos pos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
        return this.isValidLightLevel() && this.world.canBlockSeeSky(pos) && this.world.getDifficulty() != EnumDifficulty.PEACEFUL && world.isDaytime()
                && this.world.checkNoEntityCollision(this.getEntityBoundingBox()) && this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty()
                && (isFromSpawner || !this.world.containsAnyLiquid(this.getEntityBoundingBox()));
    }

    @Override
    public void writeEntityToNBT(CompoundNBT compound) {
        super.writeEntityToNBT(compound);
        if (this.hasSkinVariants()) {
            compound.setInteger("Variant", this.getVariant());
        }
    }

    @Override
    public void readEntityFromNBT(CompoundNBT compound) {
        super.readEntityFromNBT(compound);
        if (this.hasSkinVariants()) {
            this.setVariant(compound.getInteger("Variant"));
        }
    }
}
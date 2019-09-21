package com.teammetallurgy.atum.entity.bandit;

import com.teammetallurgy.atum.entity.animal.EntityDesertWolf;
import com.teammetallurgy.atum.entity.stone.EntityStoneBase;
import com.teammetallurgy.atum.entity.undead.EntityUndeadBase;
import com.teammetallurgy.atum.integration.champion.ChampionsHelper;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.Block;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.ILivingEntityData;
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
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.goalSelector.addGoal(6, new EntityAIWander(this, 1.0D));
        this.goalSelector.addGoal(7, new EntityAIAvoidEntity<>(this, EntityDesertWolf.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(8, new EntityAIWatchClosest(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new EntityAILookIdle(this));
        this.applyEntityAI();
    }

    protected void applyEntityAI() {
        this.targetSelector.addGoal(0, new EntityAIHurtByTarget(this, false, EntityBanditBase.class));
        this.targetSelector.addGoal(1, new EntityAINearestAttackableTarget<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(1, new EntityAINearestAttackableTarget<>(this, EntityUndeadBase.class, true));
        this.targetSelector.addGoal(1, new EntityAINearestAttackableTarget<>(this, EntityStoneBase.class, true));
    }

    @Override
    public boolean canAttackClass(Class<? extends LivingEntity> cls) {
        return !EntityBanditBase.class.isAssignableFrom(cls) && super.canAttackClass(cls);
    }

    @Override
    protected void registerData() {
        super.registerData();
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
    public ILivingEntityData onInitialSpawn(DifficultyInstance difficulty, @Nullable ILivingEntityData livingdata) {
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
    public void tick() {
        super.tick();

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
    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.UNDEFINED;
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
        BlockPos pos = new BlockPos(this.posX, this.getBoundingBox().minY, this.posZ);
        return pos.getY() > 62 && canSpawnNoHeightCheck(false);
    }

    @Override
    protected boolean isValidLightLevel() {
        BlockPos pos = new BlockPos(this.posX, this.getBoundingBox().minY, this.posZ);
        return this.world.getLightFor(EnumSkyBlock.BLOCK, pos) <= 8;
    }

    public boolean canSpawnNoHeightCheck(boolean isFromSpawner) {
        BlockPos pos = new BlockPos(this.posX, this.getBoundingBox().minY, this.posZ);
        return this.isValidLightLevel() && this.world.canBlockSeeSky(pos) && this.world.getDifficulty() != EnumDifficulty.PEACEFUL && world.isDaytime()
                && this.world.checkNoEntityCollision(this.getBoundingBox()) && this.world.getCollisionBoxes(this, this.getBoundingBox()).isEmpty()
                && (isFromSpawner || !this.world.containsAnyLiquid(this.getBoundingBox()));
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        if (this.hasSkinVariants()) {
            compound.putInt("Variant", this.getVariant());
        }
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (this.hasSkinVariants()) {
            this.setVariant(compound.getInt("Variant"));
        }
    }
}
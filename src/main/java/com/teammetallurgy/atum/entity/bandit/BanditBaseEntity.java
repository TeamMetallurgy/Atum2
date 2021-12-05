package com.teammetallurgy.atum.entity.bandit;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.ITexture;
import com.teammetallurgy.atum.entity.animal.DesertWolfEntity;
import com.teammetallurgy.atum.entity.stone.StoneBaseEntity;
import com.teammetallurgy.atum.entity.undead.UndeadBaseEntity;
import net.minecraft.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.*;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;

public class BanditBaseEntity extends PatrollingMonster implements ITexture {
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(BanditBaseEntity.class, EntityDataSerializers.INT);
    private String texturePath;
    private boolean canPatrol;
    private UUID leadingEntity;

    BanditBaseEntity(EntityType<? extends BanditBaseEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(5, new BanditPatrolGoal<>(this, 0.7D, 0.595D)); //Only applies if spawned in a patrol
        this.goalSelector.addGoal(6, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new AvoidEntityGoal<>(this, DesertWolfEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.applyEntityAI();
    }

    protected void applyEntityAI() {
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this, BanditBaseEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, UndeadBaseEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, StoneBaseEntity.class, true));
    }

    public static AttributeSupplier.Builder getBaseAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.FOLLOW_RANGE, 30.0D).add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.ATTACK_KNOCKBACK);
    }

    @Override
    public boolean canAttackType(@Nonnull EntityType<?> type) {
        return type != this.getType() && super.canAttackType(type);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        if (this.hasSkinVariants()) {
            this.entityData.define(VARIANT, 0);
        }
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@Nonnull ServerLevelAccessor world, @Nonnull DifficultyInstance difficulty, @Nonnull MobSpawnType spawnReason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag nbt) {
        spawnData = mobInitialSpawn(spawnData);
        this.populateDefaultEquipmentEnchantments(difficulty);
        this.populateDefaultEquipmentSlots(difficulty);
        this.setCanPickUpLoot(this.random.nextFloat() < 0.55F * difficulty.getSpecialMultiplier());

        if (this.isPatrolLeader()) {
            this.setItemSlot(EquipmentSlot.HEAD, createBanditBanner());
            this.setDropChance(EquipmentSlot.HEAD, 0.1F);
        }

        if (spawnReason == MobSpawnType.PATROL) {
            this.setPatrolling(true);
        }

        if (this.hasSkinVariants()) {
            final int variant = Mth.nextInt(this.random, 0, getVariantAmount());
            this.setVariant(variant);
        }
        return spawnData;
    }

    public SpawnGroupData mobInitialSpawn(@Nullable SpawnGroupData spawnData) {
        this.getAttribute(Attributes.FOLLOW_RANGE).addPermanentModifier(new AttributeModifier("Random spawn bonus", this.random.nextGaussian() * 0.05D, AttributeModifier.Operation.MULTIPLY_BASE));
        this.setLeftHanded(this.random.nextFloat() < 0.5F);
        return spawnData;
    }

    public static ItemStack createBanditBanner() {
        ItemStack banner = new ItemStack(Items.WHITE_BANNER);
        CompoundTag nbt = banner.getOrCreateTagElement("BlockEntityTag");
        ListTag nbtList = new BannerPattern.Builder().addPattern(BannerPattern.BASE, DyeColor.WHITE).addPattern(BannerPattern.STRIPE_DOWNLEFT, DyeColor.GRAY)
                .addPattern(BannerPattern.STRIPE_DOWNRIGHT, DyeColor.GRAY).addPattern(BannerPattern.CROSS, DyeColor.RED)
                .addPattern(BannerPattern.FLOWER, DyeColor.BLACK).addPattern(BannerPattern.FLOWER, DyeColor.ORANGE)
                .addPattern(BannerPattern.CIRCLE_MIDDLE, DyeColor.BLACK).addPattern(BannerPattern.CIRCLE_MIDDLE, DyeColor.YELLOW)
                .addPattern(BannerPattern.SKULL, DyeColor.BLACK).addPattern(BannerPattern.SKULL, DyeColor.WHITE).toListTag();
        nbt.put("Patterns", nbtList);
        banner.setHoverName(new TranslatableComponent("block.atum.bandit_banner").withStyle(ChatFormatting.GOLD));
        return banner;
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    @Override
    protected void populateDefaultEquipmentSlots(@Nonnull DifficultyInstance difficulty) {
        //Don't use for now, might do something with it later
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level.isClientSide && this.entityData.isDirty()) {
            this.entityData.clearDirty();
            this.texturePath = null;
        }
    }

    boolean hasSkinVariants() {
        return true;
    }

    protected int getVariantAmount() {
        return 7;
    }

    private void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
        this.texturePath = null;
    }

    private int getVariant() {
        return this.entityData.get(VARIANT);
    }

    protected boolean canPatrol() {
        return this.canPatrol;
    }

    public void setCanPatrol(boolean canPatrol) {
        this.canPatrol = canPatrol;
    }

    public void setLeadingEntity(BanditBaseEntity leadingEntity) {
        this.leadingEntity = leadingEntity.getUUID();
    }

    @Nullable
    public UUID getLeadingEntity() {
        return this.leadingEntity;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public String getTexture() {
        if (this.texturePath == null) {
            String entityName = Objects.requireNonNull(this.getType().getRegistryName()).getPath();

            if (this.hasSkinVariants()) {
                this.texturePath = new ResourceLocation(Atum.MOD_ID, "textures/entity/" + entityName + "_" + this.getVariant()) + ".png";
            } else {
                this.texturePath = new ResourceLocation(Atum.MOD_ID, "textures/entity/" + entityName) + ".png";
            }
        }
        return this.texturePath;
    }

    @Override
    @Nonnull
    public MobType getMobType() {
        return MobType.UNDEFINED;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PILLAGER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
        return SoundEvents.PILLAGER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PILLAGER_DEATH;
    }

    @Override
    public boolean checkSpawnRules(@Nonnull LevelAccessor world, @Nonnull MobSpawnType spawnReason) {
        return super.checkSpawnRules(world, spawnReason);
    }

    public static boolean canSpawn(EntityType<? extends BanditBaseEntity> banditBase, LevelAccessor world, MobSpawnType spawnReason, BlockPos pos, Random random) {
        return (spawnReason == MobSpawnType.SPAWNER || pos.getY() > 62 && world.canSeeSkyFromBelowWater(pos)) && world.getBrightness(LightLayer.BLOCK, pos) <= 8 && checkAnyLightMonsterSpawnRules(banditBase, world, spawnReason, pos, random);
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.hasSkinVariants()) {
            compound.putInt("Variant", this.getVariant());
        }
        compound.putBoolean("CanPatrol", this.canPatrol);
        if (this.leadingEntity != null) {
            compound.putUUID("LeadingEntity", this.leadingEntity);
        }
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (this.hasSkinVariants()) {
            this.setVariant(compound.getInt("Variant"));
        }
        this.canPatrol = compound.getBoolean("CanPatrol");
        if (compound.hasUUID("LeadingEntity")) {
            this.leadingEntity = compound.getUUID("LeadingEntity");
        }
    }

    public static class BanditPatrolGoal<T extends BanditBaseEntity> extends Goal {
        private final T owner;
        private final double patrollerSpeed;
        private final double leaderSpeed;
        private long time;

        public BanditPatrolGoal(T bandit, double patrollerSpeed, double leaderSpeed) {
            this.owner = bandit;
            this.patrollerSpeed = patrollerSpeed;
            this.leaderSpeed = leaderSpeed;
            this.time = -1L;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            boolean flag = this.owner.level.getGameTime() < this.time;
            return this.owner.canPatrol() && this.owner.isPatrolling() && this.owner.getTarget() == null && !this.owner.isVehicle() && this.owner.hasPatrolTarget() && !flag;
        }

        @Override
        public void tick() {
            boolean isLeader = this.owner.isPatrolLeader();
            PathNavigation navigator = this.owner.getNavigation();
            if (navigator.isDone()) {
                List<BanditBaseEntity> patrollers = this.getPatrollers();
                if (this.owner.isPatrolling() && patrollers.isEmpty()) {
                    this.owner.setPatrolling(false);
                } else if (isLeader && this.owner.getPatrolTarget().closerThan(this.owner.position(), 10.0D)) {
                    this.owner.findPatrolTarget();
                } else {
                    BlockPos patrolTargetPos = this.owner.getPatrolTarget();
                    Vec3 vec3d = new Vec3(patrolTargetPos.getX(), patrolTargetPos.getY(), patrolTargetPos.getZ());
                    Vec3 vec3d1 = this.owner.position();
                    Vec3 vec3d2 = vec3d1.subtract(vec3d);
                    vec3d = vec3d2.yRot(90.0F).scale(0.4D).add(vec3d);
                    Vec3 vec3d3 = vec3d.subtract(vec3d1).normalize().scale(10.0D).add(vec3d1);
                    BlockPos pos = new BlockPos(vec3d3);
                    pos = this.owner.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos);
                    if (!navigator.moveTo(pos.getX(), pos.getY(), pos.getZ(), isLeader ? this.leaderSpeed : this.patrollerSpeed)) {
                        this.tryMoveTo();
                        this.time = this.owner.level.getGameTime() + 200L;
                    } else if (isLeader) {
                        BanditBaseEntity leader = this.owner;
                        for (BanditBaseEntity patroller : this.getPatrollers(leader)) {
                            patroller.setPatrolTarget(pos);
                        }
                    }
                }
            }
        }

        private List<BanditBaseEntity> getPatrollers() {
            return this.owner.level.getEntitiesOfClass(BanditBaseEntity.class, this.owner.getBoundingBox().inflate(24.0D), (e) -> e.canJoinPatrol() && !e.is(this.owner));
        }

        private List<BanditBaseEntity> getPatrollers(BanditBaseEntity leader) {
            return this.owner.level.getEntitiesOfClass(BanditBaseEntity.class, this.owner.getBoundingBox().inflate(24.0D), (e) -> e.canJoinPatrol() && !e.is(this.owner) && e.leadingEntity == leader.getUUID());
        }

        private boolean tryMoveTo() {
            Random random = this.owner.getRandom();
            BlockPos pos = this.owner.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (this.owner.blockPosition()).offset(-8 + random.nextInt(16), 0, -8 + random.nextInt(16)));
            return this.owner.getNavigation().moveTo(pos.getX(), pos.getY(), pos.getZ(), this.patrollerSpeed);
        }
    }
}
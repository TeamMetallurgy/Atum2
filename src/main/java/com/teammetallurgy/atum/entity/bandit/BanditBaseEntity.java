package com.teammetallurgy.atum.entity.bandit;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.ITexture;
import com.teammetallurgy.atum.entity.animal.DesertWolfEntity;
import com.teammetallurgy.atum.entity.stone.StoneBaseEntity;
import com.teammetallurgy.atum.entity.undead.UndeadBaseEntity;
import com.teammetallurgy.atum.integration.champion.ChampionsHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.PatrollerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class BanditBaseEntity extends PatrollerEntity implements ITexture {
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(BanditBaseEntity.class, DataSerializers.VARINT);
    private String texturePath;
    private boolean canPatrol;
    private UUID leadingEntity;

    BanditBaseEntity(EntityType<? extends BanditBaseEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(4, new BanditPatrolGoal<>(this, 0.7D, 0.595D)); //Only applies if spawned in a patrol
        this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new AvoidEntityGoal<>(this, DesertWolfEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.applyEntityAI();
    }

    protected void applyEntityAI() {
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this, BanditBaseEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, UndeadBaseEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, StoneBaseEntity.class, true));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30.0D);
    }

    @Override
    public boolean canAttack(@Nonnull EntityType<?> type) {
        return type != this.getType() && super.canAttack(type);
    }

    @Override
    protected void registerData() {
        super.registerData();
        if (this.hasSkinVariants()) {
            this.dataManager.register(VARIANT, 0);
        }
    }

    @Override
    @Nullable
    public ILivingEntityData onInitialSpawn(@Nonnull IWorld world, @Nonnull DifficultyInstance difficulty, @Nonnull SpawnReason spawnReason, @Nullable ILivingEntityData spawnData, @Nullable CompoundNBT nbt) {
        spawnData = mobInitialSpawn(spawnData);
        this.setEnchantmentBasedOnDifficulty(difficulty);
        this.setEquipmentBasedOnDifficulty(difficulty);
        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * difficulty.getClampedAdditionalDifficulty());

        if (this.isLeader()) {
            this.setItemStackToSlot(EquipmentSlotType.HEAD, createBanditBanner());
            this.setDropChance(EquipmentSlotType.HEAD, 1.5F);
        }

        if (spawnReason == SpawnReason.PATROL) {
            this.setPatrolling(true);
        }

        if (this.hasSkinVariants()) {
            final int variant = MathHelper.nextInt(this.rand, 0, getVariantAmount());
            this.setVariant(variant);
        }
        return spawnData;
    }

    public ILivingEntityData mobInitialSpawn(@Nullable ILivingEntityData spawnData) {
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05D, AttributeModifier.Operation.MULTIPLY_BASE));
        this.setLeftHanded(this.rand.nextFloat() < 0.5F);
        return spawnData;
    }

    public static ItemStack createBanditBanner() {
        ItemStack banner = new ItemStack(Items.WHITE_BANNER);
        CompoundNBT nbt = banner.getOrCreateChildTag("BlockEntityTag");
        ListNBT nbtList = new BannerPattern.Builder().setPatternWithColor(BannerPattern.BASE, DyeColor.WHITE).setPatternWithColor(BannerPattern.STRIPE_DOWNLEFT, DyeColor.GRAY)
                .setPatternWithColor(BannerPattern.STRIPE_DOWNRIGHT, DyeColor.GRAY).setPatternWithColor(BannerPattern.CROSS, DyeColor.RED)
                .setPatternWithColor(BannerPattern.FLOWER, DyeColor.BLACK).setPatternWithColor(BannerPattern.FLOWER, DyeColor.ORANGE)
                .setPatternWithColor(BannerPattern.CIRCLE_MIDDLE, DyeColor.BLACK).setPatternWithColor(BannerPattern.CIRCLE_MIDDLE, DyeColor.YELLOW)
                .setPatternWithColor(BannerPattern.SKULL, DyeColor.BLACK).setPatternWithColor(BannerPattern.SKULL, DyeColor.WHITE).func_222476_a();
        nbt.put("Patterns", nbtList);
        banner.setDisplayName(new TranslationTextComponent("block.atum.bandit_banner").applyTextStyle(TextFormatting.GOLD));
        return banner;
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(@Nonnull DifficultyInstance difficulty) {
        //Don't use for now, might do something with it later
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

    protected int getVariantAmount() {
        return 7;
    }

    private void setVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
        this.texturePath = null;
    }

    private int getVariant() {
        return this.dataManager.get(VARIANT);
    }

    protected boolean canPatrol() {
        return this.canPatrol;
    }

    public void setCanPatrol(boolean canPatrol) {
        this.canPatrol = canPatrol;
    }

    public void setLeadingEntity(BanditBaseEntity leadingEntity) {
        this.leadingEntity = leadingEntity.getUniqueID();
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

            if (ChampionsHelper.isChampion(this)) {
                ResourceLocation texture = ChampionsHelper.getTexture(this, entityName);
                if (texture != null) {
                    this.texturePath = texture.toString();
                    return this.texturePath;
                }
            }

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
    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.UNDEFINED;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_PILLAGER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
        return SoundEvents.ENTITY_PILLAGER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PILLAGER_DEATH;
    }

    @Override
    public boolean canSpawn(@Nonnull IWorld world, @Nonnull SpawnReason spawnReason) {
        return super.canSpawn(world, spawnReason);
    }

    public static boolean canSpawn(EntityType<? extends BanditBaseEntity> banditBase, IWorld world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return (spawnReason == SpawnReason.SPAWNER || pos.getY() > 62 && world.canBlockSeeSky(pos)) && world.getLightFor(LightType.BLOCK, pos) <= 8 && canMonsterSpawn(banditBase, world, spawnReason, pos, random);
    }

    @Override
    public void writeAdditional(@Nonnull CompoundNBT compound) {
        super.writeAdditional(compound);
        if (this.hasSkinVariants()) {
            compound.putInt("Variant", this.getVariant());
        }
        compound.putBoolean("CanPatrol", this.canPatrol);
        if (this.leadingEntity != null) {
            compound.putUniqueId("LeadingEntity", this.leadingEntity);
        }
    }

    @Override
    public void readAdditional(@Nonnull CompoundNBT compound) {
        super.readAdditional(compound);
        if (this.hasSkinVariants()) {
            this.setVariant(compound.getInt("Variant"));
        }
        this.canPatrol = compound.getBoolean("CanPatrol");
        this.leadingEntity = compound.getUniqueId("LeadingEntity");
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
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean shouldExecute() {
            boolean flag = this.owner.world.getGameTime() < this.time;
            return this.owner.canPatrol() && this.owner.isPatrolling() && this.owner.getAttackTarget() == null && !this.owner.isBeingRidden() && this.owner.hasPatrolTarget() && !flag;
        }

        @Override
        public void tick() {
            boolean isLeader = this.owner.isLeader();
            PathNavigator navigator = this.owner.getNavigator();
            if (navigator.noPath()) {
                List<BanditBaseEntity> patrollers = this.getPatrollers();
                if (this.owner.isPatrolling() && patrollers.isEmpty()) {
                    this.owner.setPatrolling(false);
                } else if (isLeader && this.owner.getPatrolTarget().withinDistance(this.owner.getPositionVec(), 10.0D)) {
                    this.owner.resetPatrolTarget();
                } else {
                    Vec3d vec3d = new Vec3d(this.owner.getPatrolTarget());
                    Vec3d vec3d1 = this.owner.getPositionVec();
                    Vec3d vec3d2 = vec3d1.subtract(vec3d);
                    vec3d = vec3d2.rotateYaw(90.0F).scale(0.4D).add(vec3d);
                    Vec3d vec3d3 = vec3d.subtract(vec3d1).normalize().scale(10.0D).add(vec3d1);
                    BlockPos pos = new BlockPos(vec3d3);
                    pos = this.owner.world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos);
                    if (!navigator.tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), isLeader ? this.leaderSpeed : this.patrollerSpeed)) {
                        this.tryMoveTo();
                        this.time = this.owner.world.getGameTime() + 200L;
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
            return this.owner.world.getEntitiesWithinAABB(BanditBaseEntity.class, this.owner.getBoundingBox().grow(24.0D), (e) -> e.func_213634_ed() && !e.isEntityEqual(this.owner));
        }

        private List<BanditBaseEntity> getPatrollers(BanditBaseEntity leader) {
            return this.owner.world.getEntitiesWithinAABB(BanditBaseEntity.class, this.owner.getBoundingBox().grow(24.0D), (e) -> e.func_213634_ed() && !e.isEntityEqual(this.owner) && e.leadingEntity == leader.getUniqueID());
        }

        private boolean tryMoveTo() {
            Random random = this.owner.getRNG();
            BlockPos pos = this.owner.world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (new BlockPos(this.owner)).add(-8 + random.nextInt(16), 0, -8 + random.nextInt(16)));
            return this.owner.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), this.patrollerSpeed);
        }
    }
}
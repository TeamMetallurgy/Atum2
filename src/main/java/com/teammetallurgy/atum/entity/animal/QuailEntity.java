package com.teammetallurgy.atum.entity.animal;

import com.teammetallurgy.atum.entity.ai.goal.FollowFlockLeaderGoal;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

public class QuailEntity extends ChickenEntity { //TODO Look at custom egg and maybe meat
    private static final Ingredient TEMPTATION_ITEMS = Ingredient.fromItems(AtumItems.EMMER_SEEDS, Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS);
    private QuailEntity flockLeader;
    private int groupSize = 1;

    public QuailEntity(EntityType<? extends ChickenEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.6D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, 1.4D, 1.6D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, WolfEntity.class, 10.0F, 1.3D, 1.6D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, DesertWolfEntity.class, 10.0F, 1.3D, 1.6D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, MonsterEntity.class, 4.0F, 1.0D, 1.4D));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.0D, false, TEMPTATION_ITEMS));
        this.goalSelector.addGoal(5, new FollowFlockLeaderGoal(this));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
    }

    public static AttributeModifierMap.MutableAttribute getAttributes() {
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 4.0D).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.32D);
    }

    @Override
    protected float getStandingEyeHeight(@Nonnull Pose pose, @Nonnull EntitySize size) {
        return this.isChild() ? size.height * 0.5F : size.height;
    }

    @Override
    @Nonnull
    protected ResourceLocation getLootTable() {
        return EntityType.CHICKEN.getLootTable();
    }

    @Override
    public ChickenEntity func_241840_a(@Nonnull ServerWorld world, @Nonnull AgeableEntity ageableEntity) {
        return AtumEntities.QUAIL.create(world);
    }

    @Override
    public boolean isBreedingItem(@Nonnull ItemStack stack) {
        return TEMPTATION_ITEMS.test(stack);
    }

    public int getMaxFlockSize() {
        return 6;
    }

    public boolean hasFlockLeader() {
        return this.flockLeader != null && this.flockLeader.isAlive();
    }

    public QuailEntity addToFlock(QuailEntity quail) { //func_212803_a
        this.flockLeader = quail;
        quail.increaseGroupSize();
        return quail;
    }

    public void leaveGroup() {
        this.flockLeader.decreaseGroupSize();
        this.flockLeader = null;
    }

    private void increaseGroupSize() {
        ++this.groupSize;
    }

    private void decreaseGroupSize() {
        --this.groupSize;
    }

    public boolean canGroupGrow() {
        return this.isFlockLeader() && this.groupSize < this.getMaxFlockSize();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isFlockLeader() && this.world.rand.nextInt(200) == 1) {
            List<QuailEntity> list = this.world.getEntitiesWithinAABB(this.getClass(), this.getBoundingBox().grow(8.0D, 8.0D, 8.0D));
            if (list.size() <= 1) {
                this.groupSize = 1;
            }
        }
    }

    public boolean isFlockLeader() {
        return this.groupSize > 1;
    }

    public boolean inRangeOfFlockLeader() {
        return this.getDistanceSq(this.flockLeader) <= 121.0D;
    }

    public void moveToFlockLeader() {
        if (this.hasFlockLeader()) {
            this.getNavigator().tryMoveToEntityLiving(this.flockLeader, 1.0D);
        }
    }

    public void func_212810_a(Stream<QuailEntity> quails) {
        quails.limit(this.getMaxFlockSize() - this.groupSize).filter((q) -> {
            return q != this;
        }).forEach((quail) -> {
            quail.addToFlock(this);
        });
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(@Nonnull IServerWorld world, @Nonnull DifficultyInstance difficulty, @Nonnull SpawnReason reason, @Nullable ILivingEntityData spawnData, @Nullable CompoundNBT dataTag) {
        if (spawnData == null) {
            spawnData = new GroupData(this);
        } else {
            this.addToFlock(((GroupData) spawnData).flockLeader);
        }
        return super.onInitialSpawn(world, difficulty, reason, spawnData, dataTag);
    }

    public static class GroupData extends AgeableData {
        public final QuailEntity flockLeader;

        public GroupData(QuailEntity flockLeader) {
            super(true);
            this.flockLeader = flockLeader;
        }
    }
}
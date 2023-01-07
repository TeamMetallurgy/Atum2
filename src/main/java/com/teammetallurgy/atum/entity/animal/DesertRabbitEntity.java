package com.teammetallurgy.atum.entity.animal;

import com.teammetallurgy.atum.api.AtumAPI;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class DesertRabbitEntity extends Rabbit { //TODO Test. Have partially implemented old way of defining rabbit type. Rabbits are now using a custom variant, which we probably shouldn´t extend (Although test to make sure)
    private static final EntityDataAccessor<Integer> DATA_TYPE_ID = SynchedEntityData.defineId(DesertRabbitEntity.class, EntityDataSerializers.INT);

    public DesertRabbitEntity(EntityType<? extends DesertRabbitEntity> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 5.0D).add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, Ingredient.of(AtumAPI.Tags.CROPS_FLAX), false));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, DesertWolfEntity.class, 16.0F, 2.2D, 2.6D));
    }

    @Override
    @Nonnull
    protected ResourceLocation getDefaultLootTable() {
        return EntityType.RABBIT.getDefaultLootTable();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_TYPE_ID, 0);
    }
    
    public int getAtumRabbitType() {
        return this.entityData.get(DATA_TYPE_ID);
    }

    public void setAtumRabbitType(int id) {
        this.entityData.set(DATA_TYPE_ID, id);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(@Nonnull ServerLevelAccessor serverLevelAccessor, @Nonnull DifficultyInstance difficultyInstance, @Nonnull MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag nbt) {
        int i = this.getRandomAtumRabbitType(serverLevelAccessor);
        if (spawnGroupData instanceof AtumRabbitGroupData) {
            i = ((AtumRabbitGroupData) spawnGroupData).rabbitType;
        } else {
            spawnGroupData = new AtumRabbitGroupData(i);
        }

        this.setAtumRabbitType(i);
        return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, spawnType, spawnGroupData, nbt);
    }
    
    public int getRandomAtumRabbitType(LevelAccessor world) {
        Biome biome = world.getBiome(this.blockPosition()).value();
        int i = this.random.nextInt(100);

        Optional<ResourceKey<Biome>> optional = world.registryAccess().registryOrThrow(Registries.BIOME).getResourceKey(biome);

        if (optional.isPresent()) {
            ResourceKey<Biome> biomeKey = optional.get();
            /*if (biomeKey.equals(AtumBiomes.SAND_PLAINS)) { //TODO Readd when biomes is fixed
                return i <= 80 ? 0 : 1;
            } else if (biomeKey.equals(AtumBiomes.SAND_DUNES)) {
                return i <= 60 ? 1 : 2;
            } else if (biomeKey.equals(AtumBiomes.SAND_HILLS)) {
                return i <= 30 ? 1 : 2;
            } else if (biomeKey.equals(AtumBiomes.LIMESTONE_MOUNTAINS)) {
                return i <= 30 ? 2 : 3;
            } else if (biomeKey.equals(AtumBiomes.LIMESTONE_CRAGS)) {
                return i <= 30 ? 3 : 4;
            } else if (biomeKey.equals(AtumBiomes.SPARSE_WOODS) || biomeKey.equals(AtumBiomes.DENSE_WOODS)) {
                return i <= 50 ? 2 : 3;
            } else if (biomeKey.equals(AtumBiomes.OASIS)) {
                return i <= 50 ? 2 : 3;
            } else if (biomeKey.equals(AtumBiomes.DEAD_OASIS)) {
                return i <= 33 ? 2 : (i <= 66 ? 3 : 4);
            } else if (biomeKey.equals(AtumBiomes.DRIED_RIVER)) {
                return i <= 50 ? 1 : 2;
            } else {*/
                return 0;
            //}
        } else {
            return 0;
        }
    }

    @Override
    public boolean isFood(@Nonnull ItemStack stack) {
        return stack.is(AtumAPI.Tags.CROPS_FLAX) || super.isFood(stack);
    }

    @Override
    public DesertRabbitEntity getBreedOffspring(@Nonnull ServerLevel world, @Nonnull AgeableMob ageable) {
        DesertRabbitEntity rabbit = AtumEntities.DESERT_RABBIT.get().create(this.level);
        int type = this.getRandomAtumRabbitType(this.level);

        if (rabbit != null) {
            if (this.random.nextInt(20) != 0) {
                if (ageable instanceof DesertRabbitEntity && this.random.nextBoolean()) {
                    type = ((DesertRabbitEntity) ageable).getAtumRabbitType();
                } else {
                    type = this.getAtumRabbitType();
                }
            }
            rabbit.setAtumRabbitType(type);
        }
        return rabbit;
    }

    public static class AtumRabbitGroupData extends AgeableMob.AgeableMobGroupData {
        public final int rabbitType;

        public AtumRabbitGroupData(int rabbitType) {
            super(1.0F);
            this.rabbitType = rabbitType;
        }
    }
}
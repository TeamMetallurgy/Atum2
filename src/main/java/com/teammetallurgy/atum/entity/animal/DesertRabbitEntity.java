package com.teammetallurgy.atum.entity.animal;

import com.teammetallurgy.atum.api.AtumAPI;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;

import javax.annotation.Nonnull;
import java.util.Optional;

public class DesertRabbitEntity extends Rabbit {

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
    protected int getRandomRabbitType(LevelAccessor world) {
        Biome biome = world.getBiome(this.blockPosition()).value();
        int i = this.random.nextInt(100);

        Optional<ResourceKey<Biome>> optional = world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getResourceKey(biome);

        if (optional.isPresent()) {
            ResourceKey<Biome> biomeKey = optional.get();
            if (biomeKey.equals(AtumBiomes.SAND_PLAINS)) {
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
            } else {
                return 0;
            }
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
        int type = this.getRandomRabbitType(this.level);

        if (rabbit != null) {
            if (this.random.nextInt(20) != 0) {
                if (ageable instanceof DesertRabbitEntity && this.random.nextBoolean()) {
                    type = ((DesertRabbitEntity) ageable).getRabbitType();
                } else {
                    type = this.getRabbitType();
                }
            }
            rabbit.setRabbitType(type);
        }
        return rabbit;
    }
}
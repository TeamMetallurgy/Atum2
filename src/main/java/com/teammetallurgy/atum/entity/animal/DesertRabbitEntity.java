package com.teammetallurgy.atum.entity.animal;

import com.teammetallurgy.atum.api.AtumAPI;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.world.biome.*;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;

public class DesertRabbitEntity extends RabbitEntity {

    public DesertRabbitEntity(EntityType<? extends DesertRabbitEntity> entityType, World world) {
        super(entityType, world);
    }

    public static AttributeModifierMap.MutableAttribute getAttributes() {
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 5.0D).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, Ingredient.fromTag(AtumAPI.Tags.CROPS_FLAX), false));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, DesertWolfEntity.class, 16.0F, 2.2D, 2.6D));
    }

    @Override
    @Nonnull
    protected ResourceLocation getLootTable() {
        return EntityType.RABBIT.getLootTable();
    }

    @Override
    protected int getRandomRabbitType(IWorld world) { //TODO
        Biome biome = world.getBiome(this.getPosition());
        int i = this.rand.nextInt(100);

        if (biome instanceof SandPlainsBiome) {
            return i <= 80 ? 0 : 1;
        } else if (biome instanceof SandDunesBiome) {
            return i <= 60 ? 1 : 2;
        } else if (biome instanceof SandHillsBiome) {
            return i <= 30 ? 1 : 2;
        } else if (biome instanceof LimestoneMountainsBiome) {
            return i <= 30 ? 2 : 3;
        } else if (biome instanceof LimestoneCragsBiome) {
            return i <= 30 ? 3 : 4;
        } else if (biome instanceof DeadwoodForestBiome) {
            return i <= 50 ? 2 : 3;
        } else if (biome instanceof OasisBiome) {
            return i <= 50 ? 2 : 3;
        } else if (biome instanceof DeadOasisBiome) {
            return i <= 33 ? 2 : (i <= 66 ? 3 : 4);
        } else if (biome instanceof DriedRiverBiome) {
            return i <= 50 ? 1 : 2;
        } else {
            return 0;
        }
    }

    @Override
    public boolean isBreedingItem(@Nonnull ItemStack stack) {
        Item item = stack.getItem();
        return item.isIn(AtumAPI.Tags.CROPS_FLAX) || super.isBreedingItem(stack);
    }

    @Override
    public DesertRabbitEntity func_241840_a(@Nonnull ServerWorld world, @Nonnull AgeableEntity ageable) {
        DesertRabbitEntity rabbit = AtumEntities.DESERT_RABBIT.create(this.world);
        int type = this.getRandomRabbitType(this.world);

        if (rabbit != null) {
            if (this.rand.nextInt(20) != 0) {
                if (ageable instanceof DesertRabbitEntity && this.rand.nextBoolean()) {
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
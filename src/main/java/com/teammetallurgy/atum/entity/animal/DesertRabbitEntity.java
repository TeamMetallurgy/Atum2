package com.teammetallurgy.atum.entity.animal;

import com.teammetallurgy.atum.api.AtumAPI;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nonnull;

public class DesertRabbitEntity extends RabbitEntity {

    public DesertRabbitEntity(EntityType<? extends DesertRabbitEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.32D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, Ingredient.fromTag(AtumAPI.Tags.CROPS_FLAX), false));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, DesertWolfEntity.class, 16.0F, 2.2D, 2.6D));
    }

    @Override
    protected int getRandomRabbitType(IWorld world) {
        Biome biome = world.getBiome(new BlockPos(this));
        int i = this.rand.nextInt(100);

        /*if (biome instanceof BiomeSandPlains) { //TODO
            return i <= 80 ? 0 : 1;
        } else if (biome instanceof BiomeSandDunes) {
            return i <= 60 ? 1 : 2;
        } else if (biome instanceof BiomeSandHills) {
            return i <= 30 ? 1 : 2;
        } else if (biome instanceof BiomeLimestoneMountains) {
            return i <= 30 ? 2 : 3;
        } else if (biome instanceof BiomeLimestoneCrags) {
            return i <= 30 ? 3 : 4;
        } else if (biome instanceof BiomeDeadwoodForest) {
            return i <= 50 ? 2 : 3;
        } else if (biome instanceof BiomeOasis) {
            return i <= 50 ? 2 : 3;
        } else if (biome instanceof BiomeDeadOasis) {
            return i <= 33 ? 2 : (i <= 66 ? 3 : 4);
        } else if (biome instanceof BiomeDriedRiver) {
            return i <= 50 ? 1 : 2;
        } else {*/
            return 0;
        //}
    }

    @Override
    public boolean isBreedingItem(@Nonnull ItemStack stack) {
        Item item = stack.getItem();
        return item.isIn(AtumAPI.Tags.CROPS_FLAX) || super.isBreedingItem(stack);
    }

    @Override
    public DesertRabbitEntity createChild(AgeableEntity ageable) {
        DesertRabbitEntity rabbit = AtumEntities.RABBIT.create(this.world);
        int type = this.getRandomRabbitType(this.world);

        if (this.rand.nextInt(20) != 0) {
            if (ageable instanceof DesertRabbitEntity && this.rand.nextBoolean()) {
                type = ((DesertRabbitEntity) ageable).getRabbitType();
            } else {
                type = this.getRabbitType();
            }
        }
        rabbit.setRabbitType(type);
        return rabbit;
    }
}
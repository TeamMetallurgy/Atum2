package com.teammetallurgy.atum.entity.villager;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.merchant.villager.VillagerData;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.villager.VillagerType;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class AtumVillagerEntity extends VillagerEntity {

    public AtumVillagerEntity(EntityType<? extends VillagerEntity> type, World world) {
        super(type, world, VillagerType.DESERT); //Type not used, by Atum villagers
    }

    @Override
    @Nonnull
    public VillagerData getVillagerData() {
        return super.getVillagerData();
    }
}
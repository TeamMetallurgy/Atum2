package com.teammetallurgy.atum.entity.villager;

import net.minecraft.entity.merchant.villager.VillagerData;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.villager.VillagerType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AtumVillagerData extends VillagerData { //Same as vanilla VillagerData, but makes sure VillagerType is not used

    public AtumVillagerData(VillagerProfession profession, int level) {
        super(null, profession, level);
    }

    @Override
    @Nonnull
    public VillagerData withProfession(@Nonnull VillagerProfession profession) {
        return new AtumVillagerData(profession, this.getLevel());
    }

    @Override
    @Nonnull
    public VillagerData withType(@Nullable VillagerType type) {
        return new AtumVillagerData(this.getProfession(), this.getLevel());
    }

    @Override
    @Nonnull
    public VillagerData withLevel(int level) {
        return new AtumVillagerData(this.getProfession(), level);
    }
}
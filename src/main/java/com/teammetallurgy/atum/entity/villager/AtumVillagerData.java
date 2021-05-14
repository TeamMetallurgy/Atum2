package com.teammetallurgy.atum.entity.villager;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teammetallurgy.atum.misc.AtumRegistry;
import com.teammetallurgy.atum.misc.ForgeRegistryEntryCodec;
import net.minecraft.entity.merchant.villager.VillagerData;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.villager.VillagerType;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;

public class AtumVillagerData extends VillagerData { //Same as vanilla VillagerData, but makes sure VillagerType is not used
    public static final Codec<AtumVillagerProfession> VILLAGER_PROFESSION_CODEC = ForgeRegistryEntryCodec.getOrCreate(AtumRegistry.VILLAGER_PROFESSION.get());
    public static final Codec<AtumVillagerData> CODEC = RecordCodecBuilder.create((dataInstance) -> {
        return dataInstance.group(VILLAGER_PROFESSION_CODEC.fieldOf("profession").orElseGet(AtumVillagerProfession.NONE).forGetter((data) -> {
            return data.profession;
        }), Codec.INT.fieldOf("level").orElse(1).forGetter((data) -> {
            return data.level;
        }), IStringSerializable.createEnumCodec(Race::values, Race::getTypeFromName).fieldOf("race").orElse(Race.HUMAN).forGetter((data) -> {
            return data.race;
        })).apply(dataInstance, AtumVillagerData::new);
    });
    private final AtumVillagerProfession profession;
    private final int level;
    private final Race race;

    public AtumVillagerData(AtumVillagerProfession profession, int level, Race race) {
        super(null, null, level);
        this.profession = profession;
        this.level = level;
        this.race = race;
    }

    public AtumVillagerProfession getAtumProfession() {
        return this.profession;
    }

    public Race getRace() {
        return this.race;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Nonnull
    public AtumVillagerData withProfession(@Nonnull AtumVillagerProfession profession) {
        return new AtumVillagerData(profession, this.getLevel(), this.getRace());
    }

    @Override
    @Nonnull
    public AtumVillagerData withLevel(int level) {
        return new AtumVillagerData(this.getAtumProfession(), level, this.getRace());
    }

    @Nonnull
    public AtumVillagerData withRace(Race race) {
        return new AtumVillagerData(this.getAtumProfession(), this.getLevel(), race);
    }

    //Deprecated usage of vanilla mehtods
    @Override
    @Nonnull
    @Deprecated
    public VillagerType getType() {
        return super.getType();
    }

    @Override
    @Nonnull
    @Deprecated
    public VillagerProfession getProfession() {
        return super.getProfession();
    }

    @Override
    @Nonnull
    @Deprecated
    public VillagerData withType(VillagerType type) {
        return super.withType(type);
    }

    @Override
    @Nonnull
    @Deprecated
    public VillagerData withProfession(VillagerProfession profession) {
        return super.withProfession(profession);
    }
}
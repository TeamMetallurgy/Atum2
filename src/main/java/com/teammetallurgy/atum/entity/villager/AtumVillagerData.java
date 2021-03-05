package com.teammetallurgy.atum.entity.villager;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teammetallurgy.atum.misc.AtumRegistry;
import com.teammetallurgy.atum.misc.ForgeRegistryEntryCodec;
import net.minecraft.entity.villager.VillagerType;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AtumVillagerData { //Same as vanilla VillagerData, but makes sure VillagerType is not used
    public static final Codec<AtumVillagerProfession> VILLAGER_PROFESSION_CODEC = ForgeRegistryEntryCodec.getOrCreate(AtumRegistry.VILLAGER_PROFESSION.get());
    public static final Codec<AtumVillagerData> CODEC = RecordCodecBuilder.create((dataInstance) -> {
        return dataInstance.group(VILLAGER_PROFESSION_CODEC.fieldOf("profession").orElseGet(AtumVillagerProfession.NONE).forGetter((data) -> {
            return data.profession;
        }), Codec.INT.fieldOf("level").orElse(1).forGetter((data) -> {
            return data.level;
        }), IStringSerializable.createEnumCodec(Race::values, Race::getTypeFromName).fieldOf("race").orElse(Race.HUMAN).forGetter((data) -> {
            return data.race;
        }), Codec.BOOL.fieldOf("is_female").orElse(false).forGetter((data) -> {
            return data.isFemale;
        })).apply(dataInstance, AtumVillagerData::new);
    });
    private final AtumVillagerProfession profession;
    private final int level;
    private final Race race;
    private final boolean isFemale;

    public AtumVillagerData(AtumVillagerProfession profession, int level, Race race, boolean isFemale) {
        this.profession = profession;
        this.level = level;
        this.race = race;
        this.isFemale = isFemale;
    }

    public AtumVillagerProfession getProfession() {
        return this.profession;
    }

    public int getLevel() {
        return this.level;
    }

    public Race getRace() {
        return this.race;
    }

    public boolean isFemale() {
        return this.isFemale;
    }

    @Nonnull
    public AtumVillagerData withProfession(@Nonnull AtumVillagerProfession profession) {
        return new AtumVillagerData(profession, this.getLevel(), this.getRace(), this.isFemale());
    }

    @Nonnull
    public AtumVillagerData withType(@Nullable VillagerType type) {
        return new AtumVillagerData(this.getProfession(), this.getLevel(), this.getRace(), this.isFemale());
    }

    @Nonnull
    public AtumVillagerData withLevel(int level) {
        return new AtumVillagerData(this.getProfession(), level, this.getRace(), this.isFemale());
    }

    @Nonnull
    public AtumVillagerData withRace(Race race) {
        return new AtumVillagerData(this.getProfession(), this.getLevel(), race, this.isFemale());
    }

    @Nonnull
    public AtumVillagerData withGender(boolean isFemale) {
        return new AtumVillagerData(this.getProfession(), this.getLevel(), this.getRace(), isFemale);
    }
}
package com.teammetallurgy.atum.entity.villager;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.merchant.villager.VillagerData;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.villager.VillagerType;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AtumVillagerData extends VillagerData { //Same as vanilla VillagerData, but makes sure VillagerType is not used
    public static final Codec<AtumVillagerData> CODEC = RecordCodecBuilder.create((dataInstance) -> {
        return dataInstance.group(Registry.VILLAGER_PROFESSION.fieldOf("profession").orElseGet(() -> {
            return VillagerProfession.NONE;
        }).forGetter((data) -> {
            return data.profession;
        }), Codec.INT.fieldOf("level").orElse(1).forGetter((data) -> {
            return data.level;
        }), IStringSerializable.createEnumCodec(Race::values, Race::getTypeFromName).fieldOf("race").orElse(Race.HUMAN).forGetter((data) -> {
            return data.race;
        }), Codec.BOOL.fieldOf("is_female").orElse(false).forGetter((data) -> {
            return data.isFemale;
        })).apply(dataInstance, AtumVillagerData::new);
    });
    private final VillagerProfession profession;
    private final int level;
    private final Race race;
    private final boolean isFemale;

    public AtumVillagerData(VillagerProfession profession, int level, Race race, boolean isFemale) {
        super(null, profession, level);
        this.profession = profession;
        this.level = level;
        this.race = race;
        this.isFemale = isFemale;
    }

    public Race getRace() {
        return this.race;
    }

    public boolean isFemale() {
        return this.isFemale;
    }

    @Override
    @Nonnull
    public VillagerData withProfession(@Nonnull VillagerProfession profession) {
        return new AtumVillagerData(profession, this.getLevel(), this.getRace(), this.isFemale());
    }

    @Override
    @Nonnull
    public VillagerData withType(@Nullable VillagerType type) {
        return new AtumVillagerData(this.getProfession(), this.getLevel(), this.getRace(), this.isFemale());
    }

    @Override
    @Nonnull
    public VillagerData withLevel(int level) {
        return new AtumVillagerData(this.getProfession(), level, this.getRace(), this.isFemale());
    }

    @Nonnull
    public VillagerData withRace(Race race) {
        return new AtumVillagerData(this.getProfession(), this.getLevel(), race, this.isFemale());
    }

    @Nonnull
    public VillagerData withGender(boolean isFemale) {
        return new AtumVillagerData(this.getProfession(), this.getLevel(), this.getRace(), isFemale);
    }
}
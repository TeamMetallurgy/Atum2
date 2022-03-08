package com.teammetallurgy.atum.entity.villager;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teammetallurgy.atum.init.AtumVillagerProfession;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;

import javax.annotation.Nonnull;

public class AtumVillagerData extends VillagerData { //Same as vanilla VillagerData, but makes sure VillagerType is not used
    public static final Codec<AtumVillagerProfession> VILLAGER_PROFESSION_CODEC = AtumVillagerProfession.villagerProfession.get().getCodec();
    public static final Codec<AtumVillagerData> CODEC = RecordCodecBuilder.create((dataInstance) -> {
        return dataInstance.group(VILLAGER_PROFESSION_CODEC.fieldOf("profession").orElseGet(AtumVillagerProfession.NONE).forGetter((data) -> {
            return data.profession;
        }), Codec.INT.fieldOf("level").orElse(1).forGetter((data) -> {
            return data.level;
        }), StringRepresentable.fromEnum(Race::values, Race::getTypeFromName).fieldOf("race").orElse(Race.HUMAN).forGetter((data) -> {
            return data.race;
        })).apply(dataInstance, AtumVillagerData::new);
    });
    private final AtumVillagerProfession profession;
    private final int level;
    private final Race race;

    public AtumVillagerData(AtumVillagerProfession profession, int level, Race race) {
        super(null, VillagerProfession.NITWIT, level);
        this.profession = profession;
        this.level = Math.max(1, level);
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
    public AtumVillagerData setLevel(int level) {
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
    public VillagerData setType(VillagerType type) {
        return super.setType(type);
    }

    @Override
    @Nonnull
    @Deprecated
    public VillagerData setProfession(VillagerProfession profession) {
        return super.setProfession(profession);
    }
}
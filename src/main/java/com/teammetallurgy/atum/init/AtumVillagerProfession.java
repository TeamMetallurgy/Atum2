package com.teammetallurgy.atum.init;

import com.google.common.collect.ImmutableSet;
import com.teammetallurgy.atum.Atum;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AtumVillagerProfession extends ForgeRegistryEntry<AtumVillagerProfession> {
    public static final RegistryObject<AtumVillagerProfession> NONE = register("none", AtumPointsOfInterest.UNEMPLOYED, null);
    public static final RegistryObject<AtumVillagerProfession> ALCHEMIST = register("alchemist", () -> PoiType.CLERIC, SoundEvents.VILLAGER_WORK_CLERIC);
    public static final RegistryObject<AtumVillagerProfession> ARMORER = register("armorer", () -> PoiType.ARMORER, SoundEvents.VILLAGER_WORK_ARMORER);
    public static final RegistryObject<AtumVillagerProfession> BUTCHER = register("butcher", () -> PoiType.BUTCHER, SoundEvents.VILLAGER_WORK_BUTCHER);
    public static final RegistryObject<AtumVillagerProfession> CARTOGRAPHER = register("cartographer", () -> PoiType.CARTOGRAPHER, SoundEvents.VILLAGER_WORK_CARTOGRAPHER);
    public static final RegistryObject<AtumVillagerProfession> CURATOR = register("curator", AtumPointsOfInterest.CURATOR, SoundEvents.VILLAGER_WORK_CARTOGRAPHER);
    public static final RegistryObject<AtumVillagerProfession> FARMER = register("farmer", () -> PoiType.FARMER, ImmutableSet.of(AtumItems.EMMER_EAR, AtumItems.EMMER_SEEDS, AtumItems.FLAX_SEEDS, () -> Items.WHEAT, () -> Items.WHEAT_SEEDS, () -> Items.BEETROOT_SEEDS, () -> Items.BONE_MEAL), ImmutableSet.of(AtumBlocks.FERTILE_SOIL_TILLED, () -> Blocks.FARMLAND), SoundEvents.VILLAGER_WORK_FARMER);
    public static final RegistryObject<AtumVillagerProfession> FLETCHER = register("fletcher", () -> PoiType.FLETCHER, SoundEvents.VILLAGER_WORK_FLETCHER);
    public static final RegistryObject<AtumVillagerProfession> GLASSBLOWER = register("glassblower", AtumPointsOfInterest.GLASSBLOWER, SoundEvents.VILLAGER_WORK_CLERIC);
    public static final RegistryObject<AtumVillagerProfession> HUNTER = register("hunter", () -> PoiType.LEATHERWORKER, SoundEvents.VILLAGER_WORK_LEATHERWORKER);
    public static final RegistryObject<AtumVillagerProfession> LIBRARIAN = register("librarian", () -> PoiType.LIBRARIAN, SoundEvents.VILLAGER_WORK_LIBRARIAN);
    public static final RegistryObject<AtumVillagerProfession> MASON = register("mason", () -> PoiType.MASON, SoundEvents.VILLAGER_WORK_MASON);
    public static final RegistryObject<AtumVillagerProfession> NITWIT = register("nitwit", () -> PoiType.NITWIT, null);
    public static final RegistryObject<AtumVillagerProfession> TAILOR = register("tailor", () -> PoiType.SHEPHERD, SoundEvents.VILLAGER_WORK_SHEPHERD);
    public static final RegistryObject<AtumVillagerProfession> TOOLSMITH = register("toolsmith", () -> PoiType.TOOLSMITH, SoundEvents.VILLAGER_WORK_TOOLSMITH);
    public static final RegistryObject<AtumVillagerProfession> WEAPONSMITH = register("weaponsmith", () -> PoiType.WEAPONSMITH, SoundEvents.VILLAGER_WORK_WEAPONSMITH);
    private final String name;
    private final PoiType pointOfInterest;
    private final ImmutableSet<Supplier<Item>> specificItems;
    private final ImmutableSet<Supplier<Block>> relatedWorldBlocks;
    private final SoundEvent sound;

    public AtumVillagerProfession(String name, PoiType pointOfInterest, ImmutableSet<Supplier<Item>> specificItems, ImmutableSet<Supplier<Block>> relatedWorldBlocks, @Nullable SoundEvent sound) {
        this.name = name;
        this.pointOfInterest = pointOfInterest;
        this.specificItems = specificItems;
        this.relatedWorldBlocks = relatedWorldBlocks;
        this.sound = sound;
    }

    public PoiType getPointOfInterest() {
        return this.pointOfInterest;
    }

    public ImmutableSet<Item> getSpecificItems() {
        return this.specificItems.stream().map(Supplier::get).collect(Collectors.collectingAndThen(Collectors.toSet(), ImmutableSet::copyOf));
    }

    public ImmutableSet<Block> getRelatedWorldBlocks() {
        return this.relatedWorldBlocks.stream().map(Supplier::get).collect(Collectors.collectingAndThen(Collectors.toSet(), ImmutableSet::copyOf));
    }

    @Nullable
    public SoundEvent getSound() {
        return this.sound;
    }

    public String toString() {
        return this.name;
    }

    public static RegistryObject<AtumVillagerProfession> register(String name, Supplier<PoiType> pointOfInterest, @Nullable SoundEvent sound) {
        return register(name, pointOfInterest, ImmutableSet.of(), ImmutableSet.of(), sound);
    }

    public static RegistryObject<AtumVillagerProfession> register(String name, Supplier<PoiType> pointOfInterest, ImmutableSet<Supplier<Item>> specificItems, ImmutableSet<Supplier<Block>> relatedWorldBlocks, @Nullable SoundEvent sound) {
        return Atum.ATUM_PROFESSION_DEFERRED.register(name, () -> new AtumVillagerProfession(name, pointOfInterest.get(), specificItems, relatedWorldBlocks, sound));
    }
}
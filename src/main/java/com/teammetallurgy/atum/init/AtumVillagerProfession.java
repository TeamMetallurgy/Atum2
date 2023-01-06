package com.teammetallurgy.atum.init;

import com.google.common.collect.ImmutableSet;
import com.teammetallurgy.atum.Atum;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public record AtumVillagerProfession(String name, Predicate<Holder<PoiType>> heldJobSite, Predicate<Holder<PoiType>> acquirableJobSite, ImmutableSet<Supplier<Item>> specificItems, ImmutableSet<Supplier<Block>> relatedWorldBlocks, @Nullable SoundEvent sound)  {
    public static final Predicate<Holder<PoiType>> ALL_ACQUIRABLE_JOBS = (poiTypeHolder) -> {
        return poiTypeHolder.is(PoiTypeTags.ACQUIRABLE_JOB_SITE); //TODO Add Atum version tag
    };
    public static final RegistryObject<AtumVillagerProfession> NONE = register("none", PoiType.NONE, ALL_ACQUIRABLE_JOBS, null);
    public static final RegistryObject<AtumVillagerProfession> ALCHEMIST = register("alchemist", PoiTypes.CLERIC, SoundEvents.VILLAGER_WORK_CLERIC);
    public static final RegistryObject<AtumVillagerProfession> ARMORER = register("armorer", PoiTypes.ARMORER, SoundEvents.VILLAGER_WORK_ARMORER);
    public static final RegistryObject<AtumVillagerProfession> BUTCHER = register("butcher", PoiTypes.BUTCHER, SoundEvents.VILLAGER_WORK_BUTCHER);
    public static final RegistryObject<AtumVillagerProfession> CARTOGRAPHER = register("cartographer", PoiTypes.CARTOGRAPHER, SoundEvents.VILLAGER_WORK_CARTOGRAPHER);
    public static final RegistryObject<AtumVillagerProfession> CURATOR = register("curator", AtumPoiTypes.CURATOR, SoundEvents.VILLAGER_WORK_CARTOGRAPHER);
    public static final RegistryObject<AtumVillagerProfession> FARMER = register("farmer", PoiTypes.FARMER, ImmutableSet.of(AtumItems.EMMER_EAR, AtumItems.EMMER_SEEDS, AtumItems.FLAX_SEEDS, () -> Items.WHEAT, () -> Items.WHEAT_SEEDS, () -> Items.BEETROOT_SEEDS, () -> Items.BONE_MEAL), ImmutableSet.of(AtumBlocks.FERTILE_SOIL_TILLED, () -> Blocks.FARMLAND), SoundEvents.VILLAGER_WORK_FARMER);
    public static final RegistryObject<AtumVillagerProfession> FLETCHER = register("fletcher", PoiTypes.FLETCHER, SoundEvents.VILLAGER_WORK_FLETCHER);
    public static final RegistryObject<AtumVillagerProfession> GLASSBLOWER = register("glassblower", AtumPoiTypes.GLASSBLOWER, SoundEvents.VILLAGER_WORK_CLERIC);
    public static final RegistryObject<AtumVillagerProfession> HUNTER = register("hunter", PoiTypes.LEATHERWORKER, SoundEvents.VILLAGER_WORK_LEATHERWORKER);
    public static final RegistryObject<AtumVillagerProfession> LIBRARIAN = register("librarian", PoiTypes.LIBRARIAN, SoundEvents.VILLAGER_WORK_LIBRARIAN);
    public static final RegistryObject<AtumVillagerProfession> MASON = register("mason", PoiTypes.MASON, SoundEvents.VILLAGER_WORK_MASON);
    public static final RegistryObject<AtumVillagerProfession> NITWIT = register("nitwit", PoiType.NONE, PoiType.NONE, null);
    public static final RegistryObject<AtumVillagerProfession> TAILOR = register("tailor", PoiTypes.SHEPHERD, SoundEvents.VILLAGER_WORK_SHEPHERD);
    public static final RegistryObject<AtumVillagerProfession> TOOLSMITH = register("toolsmith", PoiTypes.TOOLSMITH, SoundEvents.VILLAGER_WORK_TOOLSMITH);
    public static final RegistryObject<AtumVillagerProfession> WEAPONSMITH = register("weaponsmith", PoiTypes.WEAPONSMITH, SoundEvents.VILLAGER_WORK_WEAPONSMITH);

    public ImmutableSet<Item> getSpecificItems() {
        return this.specificItems.stream().map(Supplier::get).collect(Collectors.collectingAndThen(Collectors.toSet(), ImmutableSet::copyOf));
    }

    public ImmutableSet<Block> getRelatedWorldBlocks() {
        return this.relatedWorldBlocks.stream().map(Supplier::get).collect(Collectors.collectingAndThen(Collectors.toSet(), ImmutableSet::copyOf));
    }

    public String toString() {
        return this.name;
    }

    private static RegistryObject<AtumVillagerProfession> register(String name, ResourceKey<PoiType> heldJobSite, @Nullable SoundEvent sound) {
        return register(name, (poiTypeHolder) -> poiTypeHolder.is(heldJobSite), (poiTypeHolder) -> poiTypeHolder.is(heldJobSite), sound);
    }
    
    public static RegistryObject<AtumVillagerProfession> register(String name, Predicate<Holder<PoiType>> heldJobSite, Predicate<Holder<PoiType>> acquirableJobSite, @Nullable SoundEvent sound) {
        return register(name, heldJobSite, acquirableJobSite, ImmutableSet.of(), ImmutableSet.of(), sound);
    }

    public static RegistryObject<AtumVillagerProfession> register(String name, ResourceKey<PoiType> key, ImmutableSet<Supplier<Item>> specificItems, ImmutableSet<Supplier<Block>> relatedWorldBlocks, @Nullable SoundEvent sound) {
        return register(name, (poiTypeHolder) -> poiTypeHolder.is(key), (poiTypeHolder) -> poiTypeHolder.is(key), specificItems, relatedWorldBlocks, sound);
    }

    public static RegistryObject<AtumVillagerProfession> register(String name, Predicate<Holder<PoiType>> heldJobSite, Predicate<Holder<PoiType>> acquirableJobSite, ImmutableSet<Supplier<Item>> specificItems, ImmutableSet<Supplier<Block>> relatedWorldBlocks, @Nullable SoundEvent sound) {
        return Atum.ATUM_PROFESSION_DEFERRED.register(name, () -> new AtumVillagerProfession(name, heldJobSite, acquirableJobSite, specificItems, relatedWorldBlocks, sound));
    }
}
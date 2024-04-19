package com.teammetallurgy.atum.init;

import com.google.common.collect.ImmutableSet;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.AtumAPI;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nullable;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public record AtumVillagerProfession(String name, Predicate<Holder<PoiType>> heldJobSite, Predicate<Holder<PoiType>> acquirableJobSite, ImmutableSet<Supplier<Item>> specificItems, ImmutableSet<Supplier<Block>> relatedWorldBlocks, @Nullable SoundEvent sound)  {
    public static final Predicate<Holder<PoiType>> ALL_ACQUIRABLE_JOBS = (poiTypeHolder) -> poiTypeHolder.is(AtumAPI.Tags.ACQUIRABLE_JOB_SITE); //TODO Fix json, after figuring out if all Atum villager professions are correct?
    public static final DeferredHolder<AtumVillagerProfession, AtumVillagerProfession> NONE = register("none", PoiType.NONE, ALL_ACQUIRABLE_JOBS, null);
    public static final DeferredHolder<AtumVillagerProfession, AtumVillagerProfession> ALCHEMIST = register("alchemist", PoiTypes.CLERIC, SoundEvents.VILLAGER_WORK_CLERIC);
    public static final DeferredHolder<AtumVillagerProfession, AtumVillagerProfession> ARMORER = register("armorer", PoiTypes.ARMORER, SoundEvents.VILLAGER_WORK_ARMORER);
    public static final DeferredHolder<AtumVillagerProfession, AtumVillagerProfession> BUTCHER = register("butcher", PoiTypes.BUTCHER, SoundEvents.VILLAGER_WORK_BUTCHER);
    public static final DeferredHolder<AtumVillagerProfession, AtumVillagerProfession> CARTOGRAPHER = register("cartographer", PoiTypes.CARTOGRAPHER, SoundEvents.VILLAGER_WORK_CARTOGRAPHER);
    public static final DeferredHolder<AtumVillagerProfession, AtumVillagerProfession> CURATOR = register("curator", AtumPoiTypes.CURATOR.getKey(), SoundEvents.VILLAGER_WORK_CARTOGRAPHER);
    public static final DeferredHolder<AtumVillagerProfession, AtumVillagerProfession> FARMER = register("farmer", PoiTypes.FARMER, ImmutableSet.of(AtumItems.EMMER_EAR, AtumItems.EMMER_SEEDS, AtumItems.FLAX_SEEDS, () -> Items.WHEAT, () -> Items.WHEAT_SEEDS, () -> Items.BEETROOT_SEEDS, () -> Items.BONE_MEAL), ImmutableSet.of(AtumBlocks.FERTILE_SOIL_TILLED, () -> Blocks.FARMLAND), SoundEvents.VILLAGER_WORK_FARMER);
    public static final DeferredHolder<AtumVillagerProfession, AtumVillagerProfession> FLETCHER = register("fletcher", PoiTypes.FLETCHER, SoundEvents.VILLAGER_WORK_FLETCHER);
    public static final DeferredHolder<AtumVillagerProfession, AtumVillagerProfession> GLASSBLOWER = register("glassblower", AtumPoiTypes.GLASSBLOWER.getKey(), SoundEvents.VILLAGER_WORK_CLERIC);
    public static final DeferredHolder<AtumVillagerProfession, AtumVillagerProfession> HUNTER = register("hunter", PoiTypes.LEATHERWORKER, SoundEvents.VILLAGER_WORK_LEATHERWORKER);
    public static final DeferredHolder<AtumVillagerProfession, AtumVillagerProfession> LIBRARIAN = register("librarian", PoiTypes.LIBRARIAN, SoundEvents.VILLAGER_WORK_LIBRARIAN);
    public static final DeferredHolder<AtumVillagerProfession, AtumVillagerProfession> MASON = register("mason", PoiTypes.MASON, SoundEvents.VILLAGER_WORK_MASON);
    public static final DeferredHolder<AtumVillagerProfession, AtumVillagerProfession> NITWIT = register("nitwit", PoiType.NONE, PoiType.NONE, null);
    public static final DeferredHolder<AtumVillagerProfession, AtumVillagerProfession> TAILOR = register("tailor", PoiTypes.SHEPHERD, SoundEvents.VILLAGER_WORK_SHEPHERD);
    public static final DeferredHolder<AtumVillagerProfession, AtumVillagerProfession> TOOLSMITH = register("toolsmith", PoiTypes.TOOLSMITH, SoundEvents.VILLAGER_WORK_TOOLSMITH);
    public static final DeferredHolder<AtumVillagerProfession, AtumVillagerProfession> WEAPONSMITH = register("weaponsmith", PoiTypes.WEAPONSMITH, SoundEvents.VILLAGER_WORK_WEAPONSMITH);

    public ImmutableSet<Item> getSpecificItems() {
        return this.specificItems.stream().map(Supplier::get).collect(Collectors.collectingAndThen(Collectors.toSet(), ImmutableSet::copyOf));
    }

    public ImmutableSet<Block> getRelatedWorldBlocks() {
        return this.relatedWorldBlocks.stream().map(Supplier::get).collect(Collectors.collectingAndThen(Collectors.toSet(), ImmutableSet::copyOf));
    }

    public String toString() {
        return this.name;
    }

    private static DeferredHolder<AtumVillagerProfession, AtumVillagerProfession> register(String name, ResourceKey<PoiType> heldJobSite, @Nullable SoundEvent sound) {
        return register(name, (poiTypeHolder) -> poiTypeHolder.is(heldJobSite), (poiTypeHolder) -> poiTypeHolder.is(heldJobSite), sound);
    }
    
    public static DeferredHolder<AtumVillagerProfession, AtumVillagerProfession> register(String name, Predicate<Holder<PoiType>> heldJobSite, Predicate<Holder<PoiType>> acquirableJobSite, @Nullable SoundEvent sound) {
        return register(name, heldJobSite, acquirableJobSite, ImmutableSet.of(), ImmutableSet.of(), sound);
    }

    public static DeferredHolder<AtumVillagerProfession, AtumVillagerProfession> register(String name, ResourceKey<PoiType> key, ImmutableSet<Supplier<Item>> specificItems, ImmutableSet<Supplier<Block>> relatedWorldBlocks, @Nullable SoundEvent sound) {
        return register(name, (poiTypeHolder) -> poiTypeHolder.is(key), (poiTypeHolder) -> poiTypeHolder.is(key), specificItems, relatedWorldBlocks, sound);
    }

    public static DeferredHolder<AtumVillagerProfession, AtumVillagerProfession> register(String name, Predicate<Holder<PoiType>> heldJobSite, Predicate<Holder<PoiType>> acquirableJobSite, ImmutableSet<Supplier<Item>> specificItems, ImmutableSet<Supplier<Block>> relatedWorldBlocks, @Nullable SoundEvent sound) {
        return Atum.ATUM_PROFESSION_DEFERRED.register(name, () -> new AtumVillagerProfession(name, heldJobSite, acquirableJobSite, specificItems, relatedWorldBlocks, sound));
    }
}
package com.teammetallurgy.atum.entity.villager;

import com.google.common.collect.ImmutableSet;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class AtumVillagerProfession extends ForgeRegistryEntry<AtumVillagerProfession> {
    public static final DeferredRegister<AtumVillagerProfession> ATUM_PROFESSION_DEFERRED = DeferredRegister.create(AtumVillagerProfession.class, Atum.MOD_ID);
    public static final RegistryObject<AtumVillagerProfession> NONE = register("none", PointOfInterestType.UNEMPLOYED, null);
    public static final RegistryObject<AtumVillagerProfession> ARMORER = register("armorer", PointOfInterestType.ARMORER, SoundEvents.ENTITY_VILLAGER_WORK_ARMORER);
    public static final RegistryObject<AtumVillagerProfession> BUTCHER = register("butcher", PointOfInterestType.BUTCHER, SoundEvents.ENTITY_VILLAGER_WORK_BUTCHER);
    public static final RegistryObject<AtumVillagerProfession> CARTOGRAPHER = register("cartographer", PointOfInterestType.CARTOGRAPHER, SoundEvents.ENTITY_VILLAGER_WORK_CARTOGRAPHER);
    public static final RegistryObject<AtumVillagerProfession> CLERIC = register("cleric", PointOfInterestType.CLERIC, SoundEvents.ENTITY_VILLAGER_WORK_CLERIC);
    public static final RegistryObject<AtumVillagerProfession> FARMER = register("farmer", PointOfInterestType.FARMER, ImmutableSet.of(AtumItems.EMMER, AtumItems.EMMER_SEEDS, AtumItems.FLAX_SEEDS, Items.WHEAT, Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS, Items.BONE_MEAL), ImmutableSet.of(AtumBlocks.FERTILE_SOIL_TILLED, Blocks.FARMLAND), SoundEvents.ENTITY_VILLAGER_WORK_FARMER);
    public static final RegistryObject<AtumVillagerProfession> FLETCHER = register("fletcher", PointOfInterestType.FLETCHER, SoundEvents.ENTITY_VILLAGER_WORK_FLETCHER);
    public static final RegistryObject<AtumVillagerProfession> LIBRARIAN = register("librarian", PointOfInterestType.LIBRARIAN, SoundEvents.ENTITY_VILLAGER_WORK_LIBRARIAN);
    public static final RegistryObject<AtumVillagerProfession> MASON = register("mason", PointOfInterestType.MASON, SoundEvents.ENTITY_VILLAGER_WORK_MASON);
    public static final RegistryObject<AtumVillagerProfession> NITWIT = register("nitwit", PointOfInterestType.NITWIT, null);
    public static final RegistryObject<AtumVillagerProfession> TAILOR = register("tailor", PointOfInterestType.SHEPHERD, SoundEvents.ENTITY_VILLAGER_WORK_SHEPHERD);
    public static final RegistryObject<AtumVillagerProfession> TOOLSMITH = register("toolsmith", PointOfInterestType.TOOLSMITH, SoundEvents.ENTITY_VILLAGER_WORK_TOOLSMITH);
    public static final RegistryObject<AtumVillagerProfession> WEAPONSMITH = register("weaponsmith", PointOfInterestType.WEAPONSMITH, SoundEvents.ENTITY_VILLAGER_WORK_WEAPONSMITH);
    private final String name;
    private final PointOfInterestType pointOfInterest;
    private final ImmutableSet<Item> specificItems;
    private final ImmutableSet<Block> relatedWorldBlocks;
    private final SoundEvent sound;

    public AtumVillagerProfession(String name, PointOfInterestType pointOfInterest, ImmutableSet<Item> specificItems, ImmutableSet<Block> relatedWorldBlocks, @Nullable SoundEvent sound) {
        this.name = name;
        this.pointOfInterest = pointOfInterest;
        this.specificItems = specificItems;
        this.relatedWorldBlocks = relatedWorldBlocks;
        this.sound = sound;
    }

    public PointOfInterestType getPointOfInterest() {
        return this.pointOfInterest;
    }

    /**
     * @return A shared static immutable set of the specific items this profession can handle.
     */
    public ImmutableSet<Item> getSpecificItems() {
        return this.specificItems;
    }

    /**
     * @return A shared static immutable set of the world blocks this profession interracts with beside job site block.
     */
    public ImmutableSet<Block> getRelatedWorldBlocks() {
        return this.relatedWorldBlocks;
    }

    @Nullable
    public SoundEvent getSound() {
        return this.sound;
    }

    public String toString() {
        return this.name;
    }

    public static RegistryObject<AtumVillagerProfession> register(String name, PointOfInterestType pointOfInterest, @Nullable SoundEvent sound) {
        return register(name, pointOfInterest, ImmutableSet.of(), ImmutableSet.of(), sound);
    }

    public static RegistryObject<AtumVillagerProfession> register(String name, PointOfInterestType pointOfInterest, ImmutableSet<Item> specificItems, ImmutableSet<Block> relatedWorldBlocks, @Nullable SoundEvent sound) {
        return ATUM_PROFESSION_DEFERRED.register(name, () -> new AtumVillagerProfession(name, pointOfInterest, specificItems, relatedWorldBlocks, sound));
    }
}
package com.teammetallurgy.atum.init;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.villager.AtumVillagerProfession;
import com.teammetallurgy.atum.misc.AtumRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumPointsOfInterest {
    private static final List<PointOfInterestType> POINTS_OF_INTEREST = new ArrayList<>();
    private static final Supplier<Set<PointOfInterestType>> WORKSTATIONS = Suppliers.memoize(() -> {
        return AtumRegistry.VILLAGER_PROFESSION.get().getValues().stream().map(AtumVillagerProfession::getPointOfInterest).collect(Collectors.toSet());
    });
    public static final Predicate<PointOfInterestType> ANY_VILLAGER_WORKSTATION = (type) -> {
        return WORKSTATIONS.get().contains(type);
    };
    private static final Set<BlockState> CURIO_DISPLAYS = ImmutableList.of(AtumBlocks.PALM_CURIO_DISPLAY, AtumBlocks.DEADWOOD_CURIO_DISPLAY, AtumBlocks.ACACIA_CURIO_DISPLAY, AtumBlocks.LIMESTONE_CURIO_DISPLAY, AtumBlocks.ALABASTER_CURIO_DISPLAY, AtumBlocks.PORPHYRY_CURIO_DISPLAY, AtumBlocks.NEBU_CURIO_DISPLAY).stream().flatMap((block) -> {
        return block.getStateContainer().getValidStates().stream();
    }).collect(ImmutableSet.toImmutableSet());
    public static final PointOfInterestType PORTAL = register("portal", getAllStates(AtumBlocks.PORTAL), 0, 1);
    public static final PointOfInterestType UNEMPLOYED = register("unemployed", ImmutableSet.of(), 1, ANY_VILLAGER_WORKSTATION, 1);
    public static final PointOfInterestType CURATOR = register("curator", CURIO_DISPLAYS, 1, 1);
    public static final PointOfInterestType GLASSBLOWER = register("glassblower", getAllStates(AtumBlocks.GLASSBLOWER_FURNACE), 1, 1);

    public static PointOfInterestType register(String name, Set<BlockState> states, int maxFreeTickets, int validRange) {
        ResourceLocation id = new ResourceLocation(Atum.MOD_ID, name);
        PointOfInterestType pointOfInterestType = PointOfInterestType.registerBlockStates(new PointOfInterestType(id.toString(), states, maxFreeTickets, validRange));
        return registerPoi(pointOfInterestType, id);
    }

    public static PointOfInterestType register(String name, Set<BlockState> states, int maxFreeTickets, Predicate<PointOfInterestType> predicate, int validRange) {
        ResourceLocation id = new ResourceLocation(Atum.MOD_ID, name);
        PointOfInterestType pointOfInterestType = new PointOfInterestType(id.toString(), states, maxFreeTickets, predicate, validRange);
        return registerPoi(pointOfInterestType, id);
    }

    public static PointOfInterestType registerPoi(PointOfInterestType pointOfInterestType, ResourceLocation id) {
        pointOfInterestType.setRegistryName(id);
        POINTS_OF_INTEREST.add(pointOfInterestType);
        return pointOfInterestType;
    }

    private static Set<BlockState> getAllStates(Block block) {
        return ImmutableSet.copyOf(block.getStateContainer().getValidStates());
    }

    @SubscribeEvent
    public static void registerRecipeSerializers(RegistryEvent.Register<PointOfInterestType> event) {
        for (PointOfInterestType pointOfInterest : POINTS_OF_INTEREST) {
            event.getRegistry().register(pointOfInterest);
        }
    }
}
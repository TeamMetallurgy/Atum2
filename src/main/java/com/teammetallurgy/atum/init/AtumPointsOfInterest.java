package com.teammetallurgy.atum.init;

import com.google.common.collect.ImmutableSet;
import com.teammetallurgy.atum.Atum;
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

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumPointsOfInterest {
    private static final List<PointOfInterestType> POINTS_OF_INTEREST = new ArrayList<>();
    public static final PointOfInterestType PORTAL = register("portal", getAllStates(AtumBlocks.PORTAL), 0, 1);

    public static PointOfInterestType register(String name, Set<BlockState> states, int maxFreeTickets, int i) {
        ResourceLocation id = new ResourceLocation(Atum.MOD_ID, name);
        PointOfInterestType pointOfInterestType = PointOfInterestType.registerBlockStates(new PointOfInterestType(id.toString(), states, maxFreeTickets, i));
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
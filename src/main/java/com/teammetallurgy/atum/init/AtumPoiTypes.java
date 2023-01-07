package com.teammetallurgy.atum.init;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.teammetallurgy.atum.Atum;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;
import java.util.function.Supplier;

public class AtumPoiTypes {
    public static final DeferredRegister<PoiType> POI_DEFERRED = DeferredRegister.create(ForgeRegistries.POI_TYPES, Atum.MOD_ID);
    public static final RegistryObject<PoiType> PORTAL = register("portal", () -> getAllStates(AtumBlocks.PORTAL), 0, 1);
    public static final RegistryObject<PoiType> CURATOR = register("curator", () -> ImmutableList.of(AtumBlocks.PALM_CURIO_DISPLAY, AtumBlocks.DEADWOOD_CURIO_DISPLAY, AtumBlocks.ACACIA_CURIO_DISPLAY, AtumBlocks.LIMESTONE_CURIO_DISPLAY, AtumBlocks.ALABASTER_CURIO_DISPLAY, AtumBlocks.PORPHYRY_CURIO_DISPLAY, AtumBlocks.NEBU_CURIO_DISPLAY).stream().flatMap((block) -> {
        return block.get().getStateDefinition().getPossibleStates().stream();
    }).collect(ImmutableSet.toImmutableSet()), 1, 1);
    public static final RegistryObject<PoiType> GLASSBLOWER = register("glassblower", () -> getAllStates(AtumBlocks.GLASSBLOWER_FURNACE), 1, 1);

    public static RegistryObject<PoiType> register(String name, Supplier<Set<BlockState>> states, int maxFreeTickets, int validRange) {
        PoiType poiType = new PoiType(states.get(), maxFreeTickets, validRange);
        return POI_DEFERRED.register(name, () -> poiType);
    }

    private static Set<BlockState> getAllStates(Supplier<Block> block) {
        return ImmutableSet.copyOf(block.get().getStateDefinition().getPossibleStates());
    }
}
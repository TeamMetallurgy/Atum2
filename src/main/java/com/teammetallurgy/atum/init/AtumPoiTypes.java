package com.teammetallurgy.atum.init;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.teammetallurgy.atum.Atum;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;
import java.util.function.Supplier;

public class AtumPoiTypes {
    public static final DeferredRegister<PoiType> POI_DEFERRED = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, Atum.MOD_ID);
    public static final DeferredHolder<PoiType, PoiType> PORTAL = POI_DEFERRED.register("portal", () -> new PoiType(getAllStates(AtumBlocks.PORTAL), 0, 1));
    public static final DeferredHolder<PoiType, PoiType> CURATOR = POI_DEFERRED.register("curator", () -> new PoiType(ImmutableList.of(AtumBlocks.PALM_CURIO_DISPLAY, AtumBlocks.DEADWOOD_CURIO_DISPLAY, AtumBlocks.ACACIA_CURIO_DISPLAY, AtumBlocks.LIMESTONE_CURIO_DISPLAY, AtumBlocks.ALABASTER_CURIO_DISPLAY, AtumBlocks.PORPHYRY_CURIO_DISPLAY, AtumBlocks.NEBU_CURIO_DISPLAY).stream().flatMap((block) -> {
        return block.get().getStateDefinition().getPossibleStates().stream();
    }).collect(ImmutableSet.toImmutableSet()), 1, 1));
    public static final DeferredHolder<PoiType, PoiType> GLASSBLOWER = POI_DEFERRED.register("glassblower", () -> new PoiType(getAllStates(AtumBlocks.GLASSBLOWER_FURNACE), 1, 1));

    private static Set<BlockState> getAllStates(Supplier<Block> block) {
        return ImmutableSet.copyOf(block.get().getStateDefinition().getPossibleStates());
    }
}
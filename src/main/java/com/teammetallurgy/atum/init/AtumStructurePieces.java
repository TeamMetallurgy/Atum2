package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.world.gen.structure.girafitomb.GirafiTombPieces;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumStructurePieces {
    public static final IStructurePieceType GIRAFI_TOMB_PIECE = register(GirafiTombPieces.GirafiTombTemplate::new, "girafi_tomb_template");

    private static IStructurePieceType register(IStructurePieceType type, String name) {
        return Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(Atum.MOD_ID, name), type);
    }
}
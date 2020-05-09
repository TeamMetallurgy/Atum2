package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.world.gen.structure.girafitomb.GirafiTombPieces;
import com.teammetallurgy.atum.world.gen.structure.lighthouse.LighthousePieces;
import com.teammetallurgy.atum.world.gen.structure.ruins.RuinPieces;
import com.teammetallurgy.atum.world.gen.structure.tomb.TombPieces;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumStructurePieces {
    public static final IStructurePieceType GIRAFI_TOMB = register(GirafiTombPieces.GirafiTombTemplate::new, "girafi_tomb_template");
    public static final IStructurePieceType LIGHTHOUSE = register(LighthousePieces.LighthouseTemplate::new, "lighthouse_template");
    public static final IStructurePieceType TOMB = register(TombPieces.TombTemplate::new, "tomb_template");
    public static final IStructurePieceType RUIN = register(RuinPieces.RuinTemplate::new, "ruin_template");

    private static IStructurePieceType register(IStructurePieceType type, String name) {
        return Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(Atum.MOD_ID, name), type);
    }
}
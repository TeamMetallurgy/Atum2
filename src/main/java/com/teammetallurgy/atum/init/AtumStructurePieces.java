package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.world.gen.structure.girafitomb.GirafiTombPieces;
import com.teammetallurgy.atum.world.gen.structure.mineshaft.AtumMineshaftPieces;
import com.teammetallurgy.atum.world.gen.structure.pyramid.PyramidPieces;
import com.teammetallurgy.atum.world.gen.structure.ruins.RuinPieces;
import com.teammetallurgy.atum.world.gen.structure.tomb.TombPieces;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumStructurePieces {
    public static final StructurePieceType GIRAFI_TOMB = register(GirafiTombPieces.GirafiTombTemplate::new, "girafi_tomb_template");
    public static final StructurePieceType TOMB = register(TombPieces.TombTemplate::new, "tomb_template");
    public static final StructurePieceType RUIN = register(RuinPieces.RuinTemplate::new, "ruin_template");
    public static final StructurePieceType PYRAMID = register(PyramidPieces.PyramidTemplate::new, "pyramid_template");
    public static final StructurePieceType PYRAMID_MAZE = register(PyramidPieces.Maze::new, "maze");
    public static final StructurePieceType MINESHAFT_CORRIDOR = register(AtumMineshaftPieces.Corridor::new, "mineshaft_corridor");
    public static final StructurePieceType MINESHAFT_CROSSING = register(AtumMineshaftPieces.Cross::new, "mineshaft_crossing");
    public static final StructurePieceType MINESHAFT_ROOM = register(AtumMineshaftPieces.Room::new, "mineshaft_room");
    public static final StructurePieceType MINESHAFT_STAIRS = register(AtumMineshaftPieces.Stairs::new, "mineshaft_stairs");

    private static StructurePieceType register(StructurePieceType type, String name) {
        return Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(Atum.MOD_ID, name), type);
    }
}
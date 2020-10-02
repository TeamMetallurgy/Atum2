package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.world.biome.*;
import net.minecraftforge.registries.ObjectHolder;

import static com.teammetallurgy.atum.misc.AtumRegistry.registerBiome;

@ObjectHolder(value = Atum.MOD_ID)
public class AtumBiomes {
    public static final AtumBiome DEAD_OASIS = registerBiome(new DeadOasisBiome(), "dead_oasis");
    public static final AtumBiome DEADWOOD_FOREST = registerBiome(new DeadwoodForestBiome(), "deadwood_forest");
    public static final AtumBiome DRIED_RIVER = registerBiome(new DriedRiverBiome(), "dried_river");
    public static final AtumBiome LIMESTONE_CRAGS = registerBiome(new LimestoneCragsBiome(), "limestone_crags");
    public static final AtumBiome LIMESTONE_MOUNTAINS = registerBiome(new LimestoneMountainsBiome(), "limestone_mountains");
    public static final AtumBiome OASIS = registerBiome(new OasisBiome(), "oasis");
    public static final AtumBiome SAND_DUNES = registerBiome(new SandDunesBiome(), "sand_dunes");
    public static final AtumBiome SAND_HILLS = registerBiome(new SandHillsBiome(), "sand_hills");
    public static final AtumBiome SAND_PLAINS = registerBiome(new SandPlainsBiome(), "sand_plains");
}
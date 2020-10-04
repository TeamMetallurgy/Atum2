package com.teammetallurgy.atum.world.biome;

import com.teammetallurgy.atum.world.gen.AtumGenSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.provider.IBiomeProviderSettings;
import net.minecraft.world.storage.WorldInfo;

public class AtumBiomeProviderSettings implements IBiomeProviderSettings {
    private final long seed;
    private final WorldType worldType;
    private AtumGenSettings generatorSettings = new AtumGenSettings();

    public AtumBiomeProviderSettings(WorldInfo info) {
        this.seed = info.getSeed();
        this.worldType = info.getGenerator();
    }

    public AtumBiomeProviderSettings setGeneratorSettings(AtumGenSettings generatorSettings) {
        this.generatorSettings = generatorSettings;
        return this;
    }

    public long getSeed() {
        return this.seed;
    }

    public WorldType getWorldType() {
        return this.worldType;
    }

    public AtumGenSettings getGeneratorSettings() {
        return this.generatorSettings;
    }
}
package com.teammetallurgy.atum.world.biome;

import com.mojang.datafixers.util.Pair;
import com.teammetallurgy.atum.init.AtumBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;

import java.util.List;
import java.util.function.Consumer;

public class AtumBiomeBuilder { //Based of OverworldBiomeBuilder //TODO implement fully
    //For reference in JSON
    private static final float VALLEY_SIZE = 0.05F;
    private static final float LOW_START = 0.26666668F;
    public static final float HIGH_START = 0.4F;
    private static final float HIGH_END = 0.93333334F;
    private static final float PEAK_SIZE = 0.1F;
    public static final float PEAK_START = 0.56666666F;
    private static final float PEAK_END = 0.7666667F;
    public static final float NEAR_INLAND_START = -0.11F;
    public static final float MID_INLAND_START = 0.03F;
    public static final float FAR_INLAND_START = 0.3F;
    public static final float EROSION_INDEX_1_START = -0.78F;
    public static final float EROSION_INDEX_2_START = -0.375F;

    private final Climate.Parameter FULL_RANGE = Climate.Parameter.span(-1.0F, 1.0F);
    private final Climate.Parameter[] humidities = new Climate.Parameter[]{Climate.Parameter.span(-1.0F, -0.35F), Climate.Parameter.span(-0.35F, -0.1F), Climate.Parameter.span(-0.1F, 0.1F), Climate.Parameter.span(0.1F, 0.3F), Climate.Parameter.span(0.3F, 1.0F)};
    private final Climate.Parameter[] erosions = new Climate.Parameter[]{Climate.Parameter.span(-1.0F, -0.78F), Climate.Parameter.span(-0.78F, -0.375F), Climate.Parameter.span(-0.375F, -0.2225F), Climate.Parameter.span(-0.2225F, 0.05F), Climate.Parameter.span(0.05F, 0.45F), Climate.Parameter.span(0.45F, 0.55F), Climate.Parameter.span(0.55F, 1.0F)};
    private final Climate.Parameter inlandContinentalness = Climate.Parameter.span(-0.11F, 0.55F);
    private final Climate.Parameter nearInlandContinentalness = Climate.Parameter.span(-0.11F, 0.03F);
    private final Climate.Parameter midInlandContinentalness = Climate.Parameter.span(0.03F, 0.3F);
    private final Climate.Parameter farInlandContinentalness = Climate.Parameter.span(0.3F, 1.0F);
    private final ResourceKey<Biome>[] MIDDLE_BIOMES = new ResourceKey[]{AtumBiomes.SAND_PLAINS, AtumBiomes.SPARSE_WOODS, AtumBiomes.DENSE_WOODS, AtumBiomes.SAND_HILLS, AtumBiomes.SAND_PLAINS};
    private final ResourceKey<Biome>[] MIDDLE_BIOMES_VARIANT = new ResourceKey[]{AtumBiomes.SAND_PLAINS, null, AtumBiomes.OASIS, null, AtumBiomes.DEAD_OASIS};
    private final ResourceKey<Biome>[] PLATEAU_BIOMES = new ResourceKey[]{AtumBiomes.SAND_DUNES, AtumBiomes.SAND_DUNES, AtumBiomes.SAND_DUNES, AtumBiomes.SAND_DUNES, AtumBiomes.SAND_DUNES};
    private final ResourceKey<Biome>[] PLATEAU_BIOMES_VARIANT = new ResourceKey[]{AtumBiomes.LIMESTONE_CRAGS, null, null, null, null};
    private final ResourceKey<Biome>[] EXTREME_HILLS = new ResourceKey[]{AtumBiomes.LIMESTONE_MOUNTAINS, AtumBiomes.LIMESTONE_MOUNTAINS, AtumBiomes.LIMESTONE_MOUNTAINS, AtumBiomes.LIMESTONE_MOUNTAINS, AtumBiomes.LIMESTONE_MOUNTAINS};

    public List<Climate.ParameterPoint> spawnTarget() {
        Climate.Parameter climate$parameter = Climate.Parameter.point(0.0F);
        float f = 0.16F;
        return List.of(new Climate.ParameterPoint(this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.inlandContinentalness, this.FULL_RANGE), this.FULL_RANGE, climate$parameter, Climate.Parameter.span(-1.0F, -0.16F), 0L), new Climate.ParameterPoint(this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.inlandContinentalness, this.FULL_RANGE), this.FULL_RANGE, climate$parameter, Climate.Parameter.span(0.16F, 1.0F), 0L));
    }

    protected void addBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> p_187176_) {
        this.addInlandBiomes(p_187176_);
        this.addUndergroundBiomes(p_187176_);
    }

    private void addInlandBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> p_187216_) {
        this.addMidSlice(p_187216_, Climate.Parameter.span(-1.0F, -0.93333334F));
        this.addHighSlice(p_187216_, Climate.Parameter.span(-0.93333334F, -0.7666667F));
        this.addPeaks(p_187216_, Climate.Parameter.span(-0.7666667F, -0.56666666F));
        this.addHighSlice(p_187216_, Climate.Parameter.span(-0.56666666F, -0.4F));
        this.addMidSlice(p_187216_, Climate.Parameter.span(-0.4F, -0.26666668F));
        this.addLowSlice(p_187216_, Climate.Parameter.span(-0.26666668F, -0.05F));
        this.addValleys(p_187216_, Climate.Parameter.span(-0.05F, 0.05F));
        this.addLowSlice(p_187216_, Climate.Parameter.span(0.05F, 0.26666668F));
        this.addMidSlice(p_187216_, Climate.Parameter.span(0.26666668F, 0.4F));
        this.addHighSlice(p_187216_, Climate.Parameter.span(0.4F, 0.56666666F));
        this.addPeaks(p_187216_, Climate.Parameter.span(0.56666666F, 0.7666667F));
        this.addHighSlice(p_187216_, Climate.Parameter.span(0.7666667F, 0.93333334F));
        this.addMidSlice(p_187216_, Climate.Parameter.span(0.93333334F, 1.0F));
    }

    private void addPeaks(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> p_187178_, Climate.Parameter p_187179_) {
        for (int j = 0; j < this.humidities.length; ++j) {
            Climate.Parameter climate$parameter1 = this.humidities[j];
            ResourceKey<Biome> resourcekey3 = this.pickPlateauBiome(j, p_187179_);
            ResourceKey<Biome> resourcekey4 = this.pickExtremeHillsBiome(j, p_187179_);
            ResourceKey<Biome> resourcekey6 = this.pickPeakBiome(j, p_187179_);
            this.addSurfaceBiome(p_187178_, climate$parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[1], p_187179_, 0.0F, resourcekey6);
            this.addSurfaceBiome(p_187178_, climate$parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[2], p_187179_, 0.0F, resourcekey3);
            this.addSurfaceBiome(p_187178_, climate$parameter1, this.farInlandContinentalness, this.erosions[3], p_187179_, 0.0F, resourcekey3);
            this.addSurfaceBiome(p_187178_, climate$parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[5], p_187179_, 0.0F, resourcekey4);
        }
    }

    private void addHighSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> p_187198_, Climate.Parameter p_187199_) {
        for (int j = 0; j < this.humidities.length; ++j) {
            Climate.Parameter climate$parameter1 = this.humidities[j];
            ResourceKey<Biome> resourcekey3 = this.pickPlateauBiome(j, p_187199_);
            ResourceKey<Biome> resourcekey4 = this.pickExtremeHillsBiome(j, p_187199_);
            ResourceKey<Biome> resourcekey6 = this.pickSlopeBiome(j, p_187199_);
            ResourceKey<Biome> resourcekey7 = this.pickPeakBiome(j, p_187199_);
            this.addSurfaceBiome(p_187198_, climate$parameter1, this.nearInlandContinentalness, this.erosions[0], p_187199_, 0.0F, resourcekey6);
            this.addSurfaceBiome(p_187198_, climate$parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[0], p_187199_, 0.0F, resourcekey7);
            this.addSurfaceBiome(p_187198_, climate$parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[1], p_187199_, 0.0F, resourcekey6);
            this.addSurfaceBiome(p_187198_, climate$parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[2], p_187199_, 0.0F, resourcekey3);
            this.addSurfaceBiome(p_187198_, climate$parameter1, this.farInlandContinentalness, this.erosions[3], p_187199_, 0.0F, resourcekey3);
            this.addSurfaceBiome(p_187198_, climate$parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[5], p_187199_, 0.0F, resourcekey4);
        }
    }

    private void addMidSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> p_187218_, Climate.Parameter p_187219_) {
            for (int j = 0; j < this.humidities.length; ++j) {
                Climate.Parameter climate$parameter1 = this.humidities[j];
                ResourceKey<Biome> resourcekey = this.pickMiddleBiome(j, p_187219_);
                ResourceKey<Biome> resourcekey3 = this.pickExtremeHillsBiome(j, p_187219_);
                ResourceKey<Biome> resourcekey4 = this.pickPlateauBiome(j, p_187219_);
                ResourceKey<Biome> resourcekey8 = this.pickSlopeBiome(j, p_187219_);
                this.addSurfaceBiome(p_187218_, climate$parameter1, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[0], p_187219_, 0.0F, resourcekey8);
                this.addSurfaceBiome(p_187218_, climate$parameter1, this.farInlandContinentalness, this.erosions[1], p_187219_, 0.0F, resourcekey4);
                this.addSurfaceBiome(p_187218_, climate$parameter1, this.nearInlandContinentalness, this.erosions[2], p_187219_, 0.0F, resourcekey);
                this.addSurfaceBiome(p_187218_, climate$parameter1, this.farInlandContinentalness, this.erosions[2], p_187219_, 0.0F, resourcekey4);
                if (p_187219_.max() < 0L) {
                    this.addSurfaceBiome(p_187218_, climate$parameter1, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[4], p_187219_, 0.0F, resourcekey);
                }
                this.addSurfaceBiome(p_187218_, climate$parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[5], p_187219_, 0.0F, resourcekey3);
            }
    }

    private void addLowSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> p_187229_, Climate.Parameter p_187230_) {
            for (int j = 0; j < this.humidities.length; ++j) {
                Climate.Parameter climate$parameter1 = this.humidities[j];
                ResourceKey<Biome> resourcekey = this.pickMiddleBiome(j, p_187230_);
                this.addSurfaceBiome(p_187229_, climate$parameter1, this.nearInlandContinentalness, Climate.Parameter.span(this.erosions[2], this.erosions[3]), p_187230_, 0.0F, resourcekey);
                this.addSurfaceBiome(p_187229_, climate$parameter1, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[4], p_187230_, 0.0F, resourcekey);
                this.addSurfaceBiome(p_187229_, climate$parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[5], p_187230_, 0.0F, resourcekey);
            }
    }

    private void addValleys(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> p_187238_, Climate.Parameter p_187239_) {
        this.addSurfaceBiome(p_187238_, this.FULL_RANGE, this.nearInlandContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), p_187239_, 0.0F, AtumBiomes.DRIED_RIVER);
    }

    private void addUndergroundBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> p_187227_) {
        /*this.addUndergroundBiome(p_187227_, this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.span(0.8F, 1.0F), this.FULL_RANGE, this.FULL_RANGE, 0.0F, Biomes.DRIPSTONE_CAVES);
        this.addUndergroundBiome(p_187227_, this.FULL_RANGE, Climate.Parameter.span(0.7F, 1.0F), this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, 0.0F, Biomes.LUSH_CAVES);*/
    }

    private ResourceKey<Biome> pickMiddleBiome(int p_187165_, Climate.Parameter p_187166_) {
        if (p_187166_.max() < 0L) {
            return this.MIDDLE_BIOMES[p_187165_];
        } else {
            ResourceKey<Biome> resourcekey = this.MIDDLE_BIOMES_VARIANT[p_187165_];
            return resourcekey == null ? this.MIDDLE_BIOMES[p_187165_] : resourcekey;
        }
    }

    private ResourceKey<Biome> pickPlateauBiome(int p_187235_, Climate.Parameter p_187236_) {
        if (p_187236_.max() < 0L) {
            return this.PLATEAU_BIOMES[p_187235_];
        } else {
            ResourceKey<Biome> resourcekey = this.PLATEAU_BIOMES_VARIANT[p_187235_];
            return resourcekey == null ? this.PLATEAU_BIOMES[p_187235_] : resourcekey;
        }
    }

    private ResourceKey<Biome> pickPeakBiome(int p_187242_, Climate.Parameter p_187243_) {
        return AtumBiomes.LIMESTONE_MOUNTAINS;
    }

    private ResourceKey<Biome> pickSlopeBiome(int p_187246_, Climate.Parameter p_187247_) {
        return p_187246_ <= 1 ? AtumBiomes.SAND_DUNES : AtumBiomes.SAND_HILLS;
    }

    private ResourceKey<Biome> pickExtremeHillsBiome(int p_187250_, Climate.Parameter p_187251_) {
        ResourceKey<Biome> resourcekey = this.EXTREME_HILLS[p_187250_];
        return resourcekey == null ? this.pickMiddleBiome(p_187250_, p_187251_) : resourcekey;
    }

    private void addSurfaceBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> p_187181_, Climate.Parameter p_187183_, Climate.Parameter p_187184_, Climate.Parameter p_187185_, Climate.Parameter p_187186_, float p_187187_, ResourceKey<Biome> p_187188_) {
        p_187181_.accept(Pair.of(Climate.parameters(new Climate.Parameter(1L, 1L), p_187183_, p_187184_, p_187185_, Climate.Parameter.point(0.0F), p_187186_, p_187187_), p_187188_));
        p_187181_.accept(Pair.of(Climate.parameters(new Climate.Parameter(1L, 1L), p_187183_, p_187184_, p_187185_, Climate.Parameter.point(1.0F), p_187186_, p_187187_), p_187188_));
    }

    private void addUndergroundBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> p_187201_, Climate.Parameter p_187203_, Climate.Parameter p_187204_, Climate.Parameter p_187205_, Climate.Parameter p_187206_, float p_187207_, ResourceKey<Biome> p_187208_) {
        p_187201_.accept(Pair.of(Climate.parameters(new Climate.Parameter(1L, 1L), p_187203_, p_187204_, p_187205_, Climate.Parameter.span(0.2F, 0.9F), p_187206_, p_187207_), p_187208_));
    }
}
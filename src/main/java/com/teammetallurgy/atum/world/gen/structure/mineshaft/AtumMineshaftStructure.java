package com.teammetallurgy.atum.world.gen.structure.mineshaft;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.Atum;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class AtumMineshaftStructure extends Structure<AtumMineshaftConfig> {

    public AtumMineshaftStructure(Codec<AtumMineshaftConfig> config) {
        super(config);
    }

    @Override
    protected boolean func_230363_a_(@Nonnull ChunkGenerator generator, @Nonnull BiomeProvider provider, long seed, SharedSeedRandom seedRandom, int chunkX, int chunkZ, @Nonnull Biome biome, @Nonnull ChunkPos chunkPos, AtumMineshaftConfig config) {
        seedRandom.setLargeFeatureSeed(seed, chunkX, chunkZ);
        double probability = config.probability;
        return seedRandom.nextDouble() < probability;
    }

    /*@Override
    public boolean place(@Nonnull IWorld world, @Nonnull ChunkGenerator<? extends GenerationSettings> generator, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull AtumMineshaftConfig config) { //TODO. Maybe DimensionGeneratorSettings
        ChunkPos chunkPos = new ChunkPos(pos);
        if (StructureHelper.doesChunkHaveStructure(world, chunkPos.x, chunkPos.z, AtumFeatures.PYRAMID)) {
            return false;
        } else {
            return super.place(world, generator, rand, pos, config);
        }
    }*/

    @Override
    @Nonnull
    public Structure.IStartFactory<AtumMineshaftConfig> getStartFactory() {
        return Start::new;
    }

    public static class Start extends StructureStart<AtumMineshaftConfig> {

        public Start(Structure<AtumMineshaftConfig> structure, int chunkPosX, int chunkPosZ, MutableBoundingBox box, int references, long seed) {
            super(structure, chunkPosX, chunkPosZ, box, references, seed);
        }

        @Override
        public void func_230364_a_(@Nonnull DynamicRegistries registries, @Nonnull ChunkGenerator generator, @Nonnull TemplateManager manager, int chunkX, int chunkZ, @Nonnull Biome biome, @Nonnull AtumMineshaftConfig config) {
            AtumMineshaftPieces.Room room = new AtumMineshaftPieces.Room(0, this.rand, (chunkX << 4) + 2, (chunkZ << 4) + 2, config.type);
            this.components.add(room);
            room.buildComponent(room, this.components, this.rand);
            this.recalculateStructureSize();
            if (config.type.isSurface()) {
                int y = generator.getGroundHeight() - this.bounds.maxY + this.bounds.getYSize() / 2 - -5;
                this.bounds.offset(0, y, 0);

                for (StructurePiece structurepiece : this.components) {
                    structurepiece.offset(0, y, 0);
                }
            } else {
                this.func_214628_a(generator.getGroundHeight(), this.rand, 10);
            }
        }
    }

    public enum Type implements IStringSerializable {
        DEADWOOD("deadwood", false),
        LIMESTONE("limestone", false),
        DEADWOOD_SURFACE("deadwood_surface", true),
        LIMESTONE_SURFACE("limestone_surface", true);

        public static final Codec<Type> CODEC = IStringSerializable.createEnumCodec(Type::values, Type::byName);
        private static final Map<String, Type> BY_NAME = Arrays.stream(values()).collect(Collectors.toMap(Type::getName, (p_214716_0_) -> p_214716_0_));
        private final String name;
        private final boolean isSurface;

        Type(String name, boolean isSurface) {
            this.name = name;
            this.isSurface = isSurface;
        }

        public String getName() {
            return this.name;
        }

        public boolean isSurface() {
            return isSurface;
        }

        public static Type byName(String name) {
            return BY_NAME.get(name);
        }

        public static Type byId(int id) {
            return id >= 0 && id < values().length ? values()[id] : DEADWOOD;
        }

        @Override
        @Nonnull
        public String getString() {
            return this.name;
        }
    }
}
package com.teammetallurgy.atum.world.gen.structure.mineshaft;

import com.mojang.serialization.Codec;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class AtumMineshaftStructure extends StructureFeature<AtumMineshaftConfig> {

    public AtumMineshaftStructure(Codec<AtumMineshaftConfig> config) {
        super(config);
    }

    @Override
    protected boolean isFeatureChunk(@Nonnull ChunkGenerator generator, @Nonnull BiomeSource provider, long seed, WorldgenRandom seedRandom, int chunkX, int chunkZ, @Nonnull Biome biome, @Nonnull ChunkPos chunkPos, AtumMineshaftConfig config) {
        seedRandom.setLargeFeatureSeed(seed, chunkX, chunkZ);
        double probability = config.probability;
        return seedRandom.nextDouble() < probability;
    }

    @Override
    @Nonnull
    public StructureFeature.StructureStartFactory<AtumMineshaftConfig> getStartFactory() {
        return Start::new;
    }

    public static class Start extends StructureStart<AtumMineshaftConfig> {

        public Start(StructureFeature<AtumMineshaftConfig> structure, int chunkPosX, int chunkPosZ, BoundingBox box, int references, long seed) {
            super(structure, chunkPosX, chunkPosZ, box, references, seed);
        }

        @Override
        public void generatePieces(@Nonnull RegistryAccess registries, @Nonnull ChunkGenerator generator, @Nonnull StructureManager manager, int chunkX, int chunkZ, @Nonnull Biome biome, @Nonnull AtumMineshaftConfig config) {
            AtumMineshaftPieces.Room room = new AtumMineshaftPieces.Room(0, this.random, (chunkX << 4) + 2, (chunkZ << 4) + 2, config.type);
            this.pieces.add(room);
            room.addChildren(room, this.pieces, this.random);
            this.calculateBoundingBox();
            if (config.type.isSurface()) {
                int y = generator.getSpawnHeight() - this.boundingBox.y1 + this.boundingBox.getYSpan() / 2 - -5;
                this.boundingBox.move(0, y, 0);

                for (StructurePiece structurepiece : this.pieces) {
                    structurepiece.move(0, y, 0);
                }
            } else {
                this.moveBelowSeaLevel(generator.getSpawnHeight(), this.random, 10);
            }
        }
    }

    public enum Type implements StringRepresentable {
        DEADWOOD("deadwood", false),
        LIMESTONE("limestone", false),
        DEADWOOD_SURFACE("deadwood_surface", true),
        LIMESTONE_SURFACE("limestone_surface", true);

        public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values, Type::byName);
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
        public String getSerializedName() {
            return this.name;
        }
    }
}
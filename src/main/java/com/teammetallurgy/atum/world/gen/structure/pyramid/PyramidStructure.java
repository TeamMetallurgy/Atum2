/*
package com.teammetallurgy.atum.world.gen.structure.pyramid;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.api.event.PharaohBeatenEvent;
import com.teammetallurgy.atum.blocks.base.IUnbreakable;
import com.teammetallurgy.atum.blocks.stone.limestone.LimestoneBrickBlock;
import com.teammetallurgy.atum.blocks.trap.TrapBlock;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumStructures;
import com.teammetallurgy.atum.network.NetworkHandler;
import com.teammetallurgy.atum.network.packet.SyncHandStackSizePacket;
import com.teammetallurgy.atum.world.DimensionHelper;
import com.teammetallurgy.atum.world.gen.structure.StructureHelper;
import net.minecraft.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature.StructureStartFactory;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class PyramidStructure extends StructureFeature<NoneFeatureConfiguration> { //TODO

    public PyramidStructure(Codec<NoneFeatureConfiguration> config) {
        super(config);
    }

    @Override
    protected boolean linearSeparation() {
        return false;
    }

    @Override
    protected boolean isFeatureChunk(@Nonnull ChunkGenerator generator, @Nonnull BiomeSource provider, long seed, @Nonnull WorldgenRandom seedRandom, int chunkX, int chunkZ, @Nonnull Biome biome, @Nonnull ChunkPos chunkPos, @Nonnull NoneFeatureConfiguration config) {
        for (Biome b : provider.getBiomesWithin(chunkX * 16 + 9, DimensionHelper.GROUND_LEVEL, chunkZ * 16 + 9, 32)) {
            if (!b.getGenerationSettings().isValidStart(this)) {
                return false;
            } else {
                return StructureHelper.getYPosForStructure(chunkX, chunkZ, generator, null) > 55;
            }
        }
        return false;
    }

    @Override
    @Nonnull
    public StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
        return Start::new;
    }

    @SubscribeEvent
    public void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof ServerPlayer) {
            LevelAccessor level = event.getEntity().level;
            if (level instanceof ServerLevel) {
                ServerLevel serverLevel = (ServerLevel) level;
                StructureStart<?> structureStart = serverLevel.structureFeatureManager().getStructureAt(event.getPos(), true, AtumStructures.PYRAMID_STRUCTURE);
                if (structureStart instanceof Start && structureStart.isValid()) {
                    Start start = (Start) structureStart;
                    if (!DimensionHelper.isBeatenPyramid(serverLevel, start.getBoundingBox())) {
                        ServerPlayer player = (ServerPlayer) event.getEntity();
                        Block placedBlock = event.getPlacedBlock().getBlock();
                        if (!player.isCreative() && !(placedBlock instanceof TorchBlock)) {
                            event.setCanceled(true);
                            ItemStack placedStack = new ItemStack(placedBlock);
                            InteractionHand hand = player.getMainHandItem().getItem() == placedStack.getItem() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
                            NetworkHandler.sendTo(player, new SyncHandStackSizePacket(placedStack, hand == InteractionHand.MAIN_HAND ? 1 : 0));
                        }
                    }
                }
            }
        }
    }

    public static class Start extends StructureStart<NoneFeatureConfiguration> {

        public Start(StructureFeature<NoneFeatureConfiguration> structure, int chunkPosX, int chunkPosZ, BoundingBox box, int references, long seed) {
            super(structure, chunkPosX, chunkPosZ, box, references, seed);
            MinecraftForge.EVENT_BUS.register(this);
        }

        @Override
        public void generatePieces(@Nonnull RegistryAccess registries, @Nonnull ChunkGenerator generator, @Nonnull StructureManager manager, int chunkX, int chunkZ, @Nonnull Biome biome, @Nonnull NoneFeatureConfiguration config) {
            Rotation rotation = Rotation.getRandom(this.random);
            int y = StructureHelper.getYPosForStructure(chunkX, chunkZ, generator, rotation);

            if (y > 55) {
                int yChance = Mth.nextInt(this.random, 7, 14);
                BlockPos pos = new BlockPos(chunkX * 16 + 8, y - yChance, chunkZ * 16 + 8);
                List<StructurePiece> components = PyramidPieces.getComponents(manager, pos, rotation);
                this.pieces.addAll(components);
                this.calculateBoundingBox();
            }
        }


        @SubscribeEvent
        public void onPharaohBeaten(PharaohBeatenEvent event) {
            Level level = event.getPharaoh().level;
            BlockPos sarcophagusPos = event.getPharaoh().getSarcophagusPos();
            if (sarcophagusPos != null && level instanceof ServerLevel && !level.isClientSide) {
                if (this.getBoundingBox().isInside(sarcophagusPos)) {
                    ServerLevel serverLevel = (ServerLevel) level;
                    DimensionHelper.getData(serverLevel).addBeatenPyramid(this.getBoundingBox());
                    this.changePyramidBlocks(serverLevel);
                }
            }
        }

        public void changePyramidBlocks(ServerLevel level) {
            BoundingBox box = this.getBoundingBox();

            for (int z = box.z0; z <= box.z1; ++z) {
                for (int y = box.y0; y <= box.y1; ++y) {
                    for (int x = box.x0; x <= box.x1; ++x) {
                        BlockPos pos = new BlockPos(x, y, z);
                        if (!level.isEmptyBlock(pos)) {
                            BlockState state = level.getBlockState(pos);
                            if (state.getBlock() instanceof IUnbreakable && state.getValue(IUnbreakable.UNBREAKABLE)) {
                                if (state.getBlock() == AtumBlocks.LIMESTONE_BRICK_LARGE && level.random.nextDouble() <= 0.08D) {
                                    if (level.isEmptyBlock(pos.below())) {
                                        level.setBlock(pos, AtumBlocks.LIMESTONE_BRICK_CRACKED_BRICK.defaultBlockState().setValue(LimestoneBrickBlock.CAN_FALL, true), 2);
                                    } else {
                                        level.setBlock(pos, AtumBlocks.LIMESTONE_BRICK_CRACKED_BRICK.defaultBlockState(), 2);
                                    }
                                } else {
                                    level.setBlock(pos, state.setValue(IUnbreakable.UNBREAKABLE, false), 2);
                                }
                            } else if (state.getBlock() instanceof TrapBlock) {
                                level.setBlock(pos, AtumBlocks.LIMESTONE_BRICK_CARVED.defaultBlockState(), 2);
                            } else if (state.getBlock() instanceof SpawnerBlock) {
                                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                            }
                        }
                    }
                }
            }
        }


        @Override
        public void placeInChunk(@Nonnull WorldGenLevel seedReader, @Nonnull StructureFeatureManager manager, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BoundingBox box, @Nonnull ChunkPos chunkPos) {
            Optional<ResourceKey<Biome>> optional = seedReader.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getResourceKey(seedReader.getBiome(this.getLocatePos()));
            if (optional.isPresent() && optional.get() != AtumBiomes.DRIED_RIVER) {
                super.placeInChunk(seedReader, manager, generator, rand, box, chunkPos);
                int y = this.boundingBox.y0;

                for (int x = box.x0; x <= box.x1; ++x) {
                    for (int z = box.z0; z <= box.z1; ++z) {
                        BlockPos pos = new BlockPos(x, y, z);
                        if (!StructureHelper.doesChunkHaveStructure(seedReader, pos, StructureFeature.VILLAGE)) {
                            if (!seedReader.isEmptyBlock(pos) && this.boundingBox.isInside(pos)) {
                                boolean isVecInside = false;

                                for (StructurePiece piece : this.pieces) {
                                    if (piece.getBoundingBox().isInside(pos)) {
                                        isVecInside = true;
                                        break;
                                    }
                                }

                                if (isVecInside) {
                                    for (int pyramidY = y - 1; pyramidY > 1; --pyramidY) {
                                        BlockPos pyramidPos = new BlockPos(x, pyramidY, z);

                                        if (!seedReader.isEmptyBlock(pyramidPos) && !seedReader.getBlockState(pyramidPos).getMaterial().isLiquid()) {
                                            break;
                                        }
                                        seedReader.setBlock(pyramidPos, AtumBlocks.LIMESTONE_BRICK_LARGE.defaultBlockState(), 2);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}*/

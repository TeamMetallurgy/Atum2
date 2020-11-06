package com.teammetallurgy.atum.world.gen.structure.pyramid;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.api.event.PharaohBeatenEvent;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.SarcophagusTileEntity;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumStructures;
import com.teammetallurgy.atum.network.NetworkHandler;
import com.teammetallurgy.atum.network.packet.SyncHandStackSizePacket;
import com.teammetallurgy.atum.world.DimensionHelper;
import com.teammetallurgy.atum.world.gen.structure.StructureHelper;
import net.minecraft.block.Block;
import net.minecraft.block.TorchBlock;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class PyramidStructure extends Structure<NoFeatureConfig> {

    public PyramidStructure(Codec<NoFeatureConfig> config) {
        super(config);
    }

    @Override
    protected boolean func_230365_b_() {
        return false;
    }

    @Override
    protected boolean func_230363_a_(@Nonnull ChunkGenerator generator, @Nonnull BiomeProvider provider, long seed, @Nonnull SharedSeedRandom seedRandom, int chunkX, int chunkZ, @Nonnull Biome biome, @Nonnull ChunkPos chunkPos, @Nonnull NoFeatureConfig config) {
        for (Biome b : provider.getBiomes(chunkX * 16 + 9, DimensionHelper.GROUND_LEVEL, chunkZ * 16 + 9, 32)) {
            if (!b.getGenerationSettings().hasStructure(this)) {
                return false;
            } else {
                return StructureHelper.getYPosForStructure(chunkX, chunkZ, generator, null) > 55;
            }
        }
        return false;
    }

    @Override
    @Nonnull
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return Start::new;
    }

    @SubscribeEvent
    public void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof ServerPlayerEntity) {
            IWorld world = event.getEntity().world;
            if (world instanceof ServerWorld) {
                StructureStart<?> structureStart = ((ServerWorld) world).func_241112_a_().getStructureStart(event.getPos(), true, AtumStructures.PYRAMID_STRUCTURE);
                if (structureStart instanceof Start && structureStart.isValid()) {
                    Start start = (Start) structureStart;
                    System.out.println(start.isPyramidCompleted());
                    if (!start.isPyramidCompleted()) {
                        ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
                        Block placedBlock = event.getPlacedBlock().getBlock();
                        if (!player.isCreative() && !(placedBlock instanceof TorchBlock)) {
                            event.setCanceled(true);
                            ItemStack placedStack = new ItemStack(placedBlock);
                            Hand hand = player.getHeldItemMainhand().getItem() == placedStack.getItem() ? Hand.MAIN_HAND : Hand.OFF_HAND;
                            NetworkHandler.sendTo(player, new SyncHandStackSizePacket(placedStack, hand == Hand.MAIN_HAND ? 1 : 0));
                        }
                    }
                }
            }
        }
    }

    public static class Start extends StructureStart<NoFeatureConfig> {
        private boolean isPyramidCompleted;

        public Start(Structure<NoFeatureConfig> structure, int chunkPosX, int chunkPosZ, MutableBoundingBox box, int references, long seed) {
            super(structure, chunkPosX, chunkPosZ, box, references, seed);
        }

        @Override
        public void func_230364_a_(@Nonnull DynamicRegistries registries, @Nonnull ChunkGenerator generator, @Nonnull TemplateManager manager, int chunkX, int chunkZ, @Nonnull Biome biome, @Nonnull NoFeatureConfig config) {
            Rotation rotation = Rotation.randomRotation(this.rand);
            int y = StructureHelper.getYPosForStructure(chunkX, chunkZ, generator, rotation);

            if (y > 55) {
                int yChance = MathHelper.nextInt(this.rand, 7, 14);
                BlockPos pos = new BlockPos(chunkX * 16 + 8, y - yChance, chunkZ * 16 + 8);
                List<StructurePiece> components = PyramidPieces.getComponents(manager, pos, rotation);
                this.components.addAll(components);
                this.recalculateStructureSize();
            }
        }

        public boolean isPyramidCompleted() {
            return isPyramidCompleted;
        }

        @SubscribeEvent
        public void onPharaohBeaten(PharaohBeatenEvent event) { //TODO Move
            World world = event.getPharaoh().world;
            BlockPos sarcophagusPos = event.getPharaoh().getSarcophagusPos();
            if (sarcophagusPos != null && world != null && !world.isRemote) {
                TileEntity tileEntity = world.getTileEntity(sarcophagusPos);
                if (tileEntity instanceof SarcophagusTileEntity) {
                    SarcophagusTileEntity sarcophagus = (SarcophagusTileEntity) tileEntity;
                    if (this.getBoundingBox().isVecInside(sarcophagusPos)) {
                        System.out.println("PYRAMID IS COMPLETED");
                        this.isPyramidCompleted = true;
                    }
                }
            }
        }

        @Override
        public void func_230366_a_(@Nonnull ISeedReader seedReader, @Nonnull StructureManager manager, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull MutableBoundingBox box, @Nonnull ChunkPos chunkPos) {
            Optional<RegistryKey<Biome>> optional = seedReader.func_241828_r().getRegistry(Registry.BIOME_KEY).getOptionalKey(seedReader.getBiome(this.getPos()));
            if (optional.isPresent() && optional.get() != AtumBiomes.DRIED_RIVER) {
                super.func_230366_a_(seedReader, manager, generator, rand, box, chunkPos);
                int y = this.bounds.minY;

                for (int x = box.minX; x <= box.maxX; ++x) {
                    for (int z = box.minZ; z <= box.maxZ; ++z) {
                        BlockPos pos = new BlockPos(x, y, z);

                        if (!seedReader.isAirBlock(pos) && this.bounds.isVecInside(pos)) {
                            boolean isVecInside = false;

                            for (StructurePiece piece : this.components) {
                                if (piece.getBoundingBox().isVecInside(pos)) {
                                    isVecInside = true;
                                    break;
                                }
                            }

                            if (isVecInside) {
                                for (int pyramidY = y - 1; pyramidY > 1; --pyramidY) {
                                    BlockPos pyramidPos = new BlockPos(x, pyramidY, z);

                                    if (!seedReader.isAirBlock(pyramidPos) && !seedReader.getBlockState(pyramidPos).getMaterial().isLiquid()) {
                                        break;
                                    }
                                    seedReader.setBlockState(pyramidPos, AtumBlocks.LIMESTONE_BRICK_LARGE.getDefaultState(), 2);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
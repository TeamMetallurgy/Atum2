package com.teammetallurgy.atum.world.gen.structure.pyramid;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.event.PharaohBeatenEvent;
import com.teammetallurgy.atum.blocks.base.IUnbreakable;
import com.teammetallurgy.atum.blocks.stone.limestone.LimestoneBrickBlock;
import com.teammetallurgy.atum.blocks.trap.TrapBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumStructures;
import com.teammetallurgy.atum.network.NetworkHandler;
import com.teammetallurgy.atum.network.packet.SyncHandStackSizePacket;
import com.teammetallurgy.atum.world.DimensionHelper;
import com.teammetallurgy.atum.world.gen.structure.AtumStructure;
import com.teammetallurgy.atum.world.gen.structure.StructureHelper;
import com.teammetallurgy.atum.world.gen.structure.girafitomb.GirafiTombPieces;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.Structures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class PyramidStructure extends AtumStructure {
    public static final Codec<PyramidStructure> CODEC = simpleCodec(PyramidStructure::new);
    public static final ResourceLocation PYRAMID = new ResourceLocation(Atum.MOD_ID, "pyramid");

    public PyramidStructure(Structure.StructureSettings structureSettings) {
        super(structureSettings);
    }

    @Override
    @Nonnull
    public StructureType<?> type() {
        return AtumStructures.PYRAMID.get();
    }

    @Override
    @Nonnull
    protected Optional<GenerationStub> findGenerationPoint(@Nonnull GenerationContext generationContext) {
        StructureTemplateManager templateManager = generationContext.structureTemplateManager();
        Rotation rotation = Rotation.getRandom(generationContext.random());
        BlockPos pos = this.getLowestYIn5by5BoxOffset7Blocks(generationContext, rotation);
        int yChance = Mth.nextInt(generationContext.random(), 7, 14);
        BlockPos pyramidPos = new BlockPos(pos.getX(), pos.getY() - yChance, pos.getZ());
        return pyramidPos.getY() > 55 ? Optional.empty() : onTopOfChunkCenter(generationContext, Heightmap.Types.WORLD_SURFACE_WG, (piecesBuilder) -> {
             PyramidPieces.addComponents(piecesBuilder, templateManager, PYRAMID, pyramidPos, rotation);
        });
    }

    @Override
    public void afterPlace(WorldGenLevel genLevel, @Nonnull StructureManager manager, @Nonnull ChunkGenerator chunkGenerator, @Nonnull RandomSource random, BoundingBox boundingBox, @Nonnull ChunkPos chunkPos, PiecesContainer piecesContainer) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        int minBuild = genLevel.getMinBuildHeight();
        BoundingBox calculateBoundingBox = piecesContainer.calculateBoundingBox();
        int calculateBoundingBoxminY = calculateBoundingBox.minY();

        for (int x = boundingBox.minX(); x <= boundingBox.maxX(); ++x) {
            for (int z = boundingBox.minZ(); z <= boundingBox.maxZ(); ++z) {
                mutablePos.set(x, calculateBoundingBoxminY, z);
                if (!StructureHelper.doesChunkHaveStructure(genLevel.getLevel(), mutablePos, BuiltinStructures.VILLAGE_DESERT)) { //TODO Replace with our village, when implemented

                    if (!genLevel.isEmptyBlock(mutablePos) && calculateBoundingBox.isInside(mutablePos) && piecesContainer.isInsidePiece(mutablePos)) {
                        for (int i1 = calculateBoundingBoxminY - 1; i1 > minBuild; --i1) {
                            mutablePos.setY(i1);
                            if (!genLevel.isEmptyBlock(mutablePos) && !genLevel.getBlockState(mutablePos).getMaterial().isLiquid()) {
                                break;
                            }
                            genLevel.setBlock(mutablePos, AtumBlocks.LIMESTONE_BRICK_LARGE.get().defaultBlockState(), 2);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof ServerPlayer) {
            LevelAccessor level = event.getEntity().level;
            if (level instanceof ServerLevel serverLevel) {
                StructureManager structureManager = serverLevel.structureManager();
                Structure structure = structureManager.registryAccess().registryOrThrow(Registries.STRUCTURE).get(AtumStructures.PYRAMID_KEY);
                if (structure != null) {
                    StructureStart structureStart = structureManager.getStructureAt(event.getPos(), structure);
                    if (structure instanceof PyramidStructure && structureStart.isValid()) {
                        if (!DimensionHelper.isBeatenPyramid(serverLevel, structureStart.getBoundingBox())) { //TODO isBeatenPyramid does not work, works perfectly fine besides  that
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
    }

    @SubscribeEvent
    public static void onPharaohBeaten(PharaohBeatenEvent event) {
        Level level = event.getPharaoh().level;
        BlockPos sarcophagusPos = event.getPharaoh().getSarcophagusPos();
        if (sarcophagusPos != null && level instanceof ServerLevel serverLevel && !level.isClientSide) {
            StructureManager structureManager = serverLevel.structureManager();
            Structure structure = structureManager.registryAccess().registryOrThrow(Registries.STRUCTURE).get(AtumStructures.PYRAMID_KEY);
            if (structure != null) {
                BoundingBox box = structureManager.getStructureAt(sarcophagusPos, structure).getBoundingBox();
                if (box.isInside(sarcophagusPos)) {
                    DimensionHelper.getData(serverLevel).addBeatenPyramid(box);
                    changePyramidBlocks(serverLevel, box);
                }
            }
        }
    }

    public static void changePyramidBlocks(ServerLevel level, BoundingBox box) {
        for (int z = box.minZ(); z <= box.maxZ(); ++z) {
            for (int y = box.minY(); y <= box.maxY(); ++y) {
                for (int x = box.minX(); x <= box.maxX(); ++x) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (!level.isEmptyBlock(pos)) {
                        BlockState state = level.getBlockState(pos);
                        if (state.getBlock() instanceof IUnbreakable && state.getValue(IUnbreakable.UNBREAKABLE)) {
                            if (state.getBlock() == AtumBlocks.LIMESTONE_BRICK_LARGE.get() && level.random.nextDouble() <= 0.08D) {
                                if (level.isEmptyBlock(pos.below())) {
                                    level.setBlock(pos, AtumBlocks.LIMESTONE_BRICK_CRACKED_BRICK.get().defaultBlockState().setValue(LimestoneBrickBlock.CAN_FALL, true), 2);
                                } else {
                                    level.setBlock(pos, AtumBlocks.LIMESTONE_BRICK_CRACKED_BRICK.get().defaultBlockState(), 2);
                                }
                            } else {
                                level.setBlock(pos, state.setValue(IUnbreakable.UNBREAKABLE, false), 2);
                            }
                        } else if (state.getBlock() instanceof TrapBlock) {
                            level.setBlock(pos, AtumBlocks.LIMESTONE_BRICK_CARVED.get().defaultBlockState(), 2);
                        } else if (state.getBlock() instanceof SpawnerBlock) {
                            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                        }
                    }
                }
            }
        }
    }
}
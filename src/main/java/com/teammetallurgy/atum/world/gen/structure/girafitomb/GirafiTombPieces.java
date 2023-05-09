package com.teammetallurgy.atum.world.gen.structure.girafitomb;

import com.teammetallurgy.atum.blocks.base.ChestBaseBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.init.AtumStructurePieces;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import javax.annotation.Nonnull;

public class GirafiTombPieces {

    public static class GirafiTombTemplate extends TemplateStructurePiece {

        public GirafiTombTemplate(StructureTemplateManager manager, ResourceLocation location, BlockPos pos, Rotation rotation) {
            super(AtumStructurePieces.GIRAFI_TOMB.get(), 0, manager, location, location.toString(), makeSettings(rotation), pos);
        }

        public GirafiTombTemplate(StructureTemplateManager manager, CompoundTag nbt) {
            super(AtumStructurePieces.GIRAFI_TOMB.get(), nbt, manager, (t) -> makeSettings(Rotation.valueOf(nbt.getString("Rot"))));
        }

        private static StructurePlaceSettings makeSettings(Rotation rotation) {
            return (new StructurePlaceSettings()).setIgnoreEntities(true).setRotation(rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        }

        @Override
        protected void handleDataMarker(@Nonnull String function, @Nonnull BlockPos pos, @Nonnull ServerLevelAccessor level, @Nonnull RandomSource rand, @Nonnull BoundingBox box) {
            switch (function) {
                case "Crate":
                    if (box.isInside(pos)) {
                        if (rand.nextDouble() <= 0.15D) {
                            level.setBlock(pos, ChestBaseBlock.correctFacing(level, pos, AtumBlocks.DEADWOOD_CRATE.get().defaultBlockState(), AtumBlocks.DEADWOOD_CRATE.get()), 2);

                            RandomizableContainerBlockEntity.setLootTable(level, rand, pos, AtumLootTables.CRATE);
                        } else {
                            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                        }
                    }
                    break;
                case "GirafiSarcophagus": {
                    BlockPos posDown = pos.below();
                    if (box.isInside(posDown)) {
                        RandomizableContainerBlockEntity.setLootTable(level, rand, posDown, AtumLootTables.GIRAFI_TOMB);
                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    }
                    break;
                }
                case "Sarcophagus": {
                    BlockPos posDown = pos.below();
                    if (box.isInside(posDown)) {
                        RandomizableContainerBlockEntity.setLootTable(level, rand, posDown, AtumLootTables.PHARAOH);
                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    }
                    break;
                }
            }
        }

        @Override
        protected void addAdditionalSaveData(@Nonnull StructurePieceSerializationContext context, @Nonnull CompoundTag compound) { //Is actually write, just horrible name
            super.addAdditionalSaveData(context, compound);
            compound.putString("Rot", this.placeSettings.getRotation().name());
        }
    }
}
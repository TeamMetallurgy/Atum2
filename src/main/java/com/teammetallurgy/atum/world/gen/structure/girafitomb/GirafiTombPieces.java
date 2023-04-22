/*
package com.teammetallurgy.atum.world.gen.structure.girafitomb;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.base.ChestBaseBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.init.AtumStructurePieces;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nonnull;
import java.util.Random;

public class GirafiTombPieces { //TODO
    public static final ResourceLocation GIRAFI_TOMB = new ResourceLocation(Atum.MOD_ID, "girafi_tomb");

    public static class GirafiTombTemplate extends TemplateStructurePiece {
        private final Rotation rotation;

        public GirafiTombTemplate(StructureManager manager, BlockPos pos, Rotation rotation) {
            super(AtumStructurePieces.GIRAFI_TOMB, 0);
            this.templatePosition = pos;
            this.rotation = rotation;
            this.loadTemplate(manager);
        }

        public GirafiTombTemplate(StructureManager manager, CompoundTag nbt) {
            super(AtumStructurePieces.GIRAFI_TOMB, nbt);
            this.rotation = Rotation.valueOf(nbt.getString("Rot"));
            this.loadTemplate(manager);
        }

        private void loadTemplate(StructureManager manager) {
            StructureTemplate template = manager.get(GIRAFI_TOMB);
            StructurePlaceSettings placementsettings = (new StructurePlaceSettings()).setIgnoreEntities(true).setRotation(this.rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
            if (template != null) {
                this.setup(template, this.templatePosition, placementsettings);
            }
        }

        @Override
        protected void handleDataMarker(@Nonnull String function, @Nonnull BlockPos pos, @Nonnull ServerLevelAccessor level, @Nonnull Random rand, @Nonnull BoundingBox box) {
            if (function.equals("Crate")) {
                if (box.isInside(pos)) {
                    if (rand.nextDouble() <= 0.15D) {
                        level.setBlock(pos, ChestBaseBlock.correctFacing(level, pos, AtumBlocks.DEADWOOD_CRATE.defaultBlockState(), AtumBlocks.DEADWOOD_CRATE), 2);

                        RandomizableContainerBlockEntity.setLootTable(level, rand, pos, AtumLootTables.CRATE);
                    } else {
                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    }
                }
            } else if (function.equals("GirafiSarcophagus")) {
                BlockPos posDown = pos.below();
                if (box.isInside(posDown)) {
                    RandomizableContainerBlockEntity.setLootTable(level, rand, posDown, AtumLootTables.GIRAFI_TOMB);
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                }
            } else if (function.equals("Sarcophagus")) {
                BlockPos posDown = pos.below();
                if (box.isInside(posDown)) {
                    RandomizableContainerBlockEntity.setLootTable(level, rand, posDown, AtumLootTables.PHARAOH);
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                }
            }
        }

        @Override
        protected void addAdditionalSaveData(@Nonnull CompoundTag compound) { //Is actually write, just horrible name
            super.addAdditionalSaveData(compound);
            compound.putString("Rot", this.placeSettings.getRotation().name());
        }
    }
}*/

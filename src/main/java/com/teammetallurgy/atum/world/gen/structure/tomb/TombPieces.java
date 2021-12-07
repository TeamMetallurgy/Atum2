/*
package com.teammetallurgy.atum.world.gen.structure.tomb;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.base.ChestBaseBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.init.AtumStructurePieces;
import com.teammetallurgy.atum.world.gen.structure.ruins.RuinPieces;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nonnull;
import java.util.Random;

public class TombPieces { //TODO
    public static final ResourceLocation TOMB = new ResourceLocation(Atum.MOD_ID, "tomb");

    public static class TombTemplate extends TemplateStructurePiece {
        private final Rotation rotation;

        public TombTemplate(StructureManager manager, BlockPos pos, Rotation rotation) {
            super(AtumStructurePieces.TOMB, 0);
            this.templatePosition = pos;
            this.rotation = rotation;
            this.loadTemplate(manager);
        }

        public TombTemplate(StructureManager manager, CompoundTag nbt) {
            super(AtumStructurePieces.TOMB, nbt);
            this.rotation = Rotation.valueOf(nbt.getString("Rot"));
            this.loadTemplate(manager);
        }

        private void loadTemplate(StructureManager manager) {
            StructureTemplate template = manager.get(TOMB);
            StructurePlaceSettings placementsettings = (new StructurePlaceSettings()).setIgnoreEntities(true).setRotation(this.rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
            if (template != null) {
                this.setup(template, this.templatePosition, placementsettings);
            }
        }

        @Override
        protected void handleDataMarker(@Nonnull String function, @Nonnull BlockPos pos, @Nonnull ServerLevelAccessor world, @Nonnull Random rand, @Nonnull BoundingBox box) {
            if (function.equals("SpawnerUndead")) {
                if (box.isInside(pos)) {
                    world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2); //Set structure block to air, to remove its TE
                    world.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), 2);

                    BlockEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity instanceof SpawnerBlockEntity) {
                        ((SpawnerBlockEntity) tileEntity).getSpawner().setEntityId(RuinPieces.RuinTemplate.UNDEAD.get(rand.nextInt(RuinPieces.RuinTemplate.UNDEAD.size())));
                    }
                }
            } else if (function.equals("Crate")) {
                if (box.isInside(pos)) {
                    if (rand.nextDouble() < 0.2D) {
                        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2); //Set structure block to air, to remove its TE
                        world.setBlock(pos, ChestBaseBlock.correctFacing(world, pos, AtumBlocks.DEADWOOD_CRATE.defaultBlockState(), AtumBlocks.DEADWOOD_CRATE), 2);
                        RandomizableContainerBlockEntity.setLootTable(world, rand, pos, AtumLootTables.CRATE);
                    } else {
                        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    }
                }
            } else if (function.equals("Chest")) {
                BlockPos posDown = pos.below();
                if (box.isInside(posDown)) {
                    RandomizableContainerBlockEntity.setLootTable(world, rand, posDown, AtumLootTables.TOMB_CHEST);
                }
                world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
            }
        }

        @Override
        protected void addAdditionalSaveData(@Nonnull CompoundTag compound) { //Is actually write, just horrible name
            super.addAdditionalSaveData(compound);
            compound.putString("Rot", this.placeSettings.getRotation().name());
        }
    }
}*/

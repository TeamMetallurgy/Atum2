package com.teammetallurgy.atum.world.gen.structure.tomb;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.base.ChestBaseBlock;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.LimestoneChestTileEntity;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.CrateTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.init.AtumStructurePieces;
import com.teammetallurgy.atum.world.gen.structure.ruins.RuinPieces;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.Nonnull;
import java.util.Random;

public class TombPieces {
    public static final ResourceLocation TOMB = new ResourceLocation(Atum.MOD_ID, "tomb");

    public static class TombTemplate extends TemplateStructurePiece {
        private final Rotation rotation;

        public TombTemplate(TemplateManager manager, BlockPos pos, Rotation rotation) {
            super(AtumStructurePieces.TOMB, 0);
            this.templatePosition = pos;
            this.rotation = rotation;
            this.loadTemplate(manager);
        }

        public TombTemplate(TemplateManager manager, CompoundNBT nbt) {
            super(AtumStructurePieces.TOMB, nbt);
            this.rotation = Rotation.valueOf(nbt.getString("Rot"));
            this.loadTemplate(manager);
        }

        private void loadTemplate(TemplateManager manager) {
            Template template = manager.getTemplate(TOMB);
            PlacementSettings placementsettings = (new PlacementSettings()).setIgnoreEntities(true).setRotation(this.rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
            if (template != null) {
                this.setup(template, this.templatePosition, placementsettings);
            }
        }

        @Override
        protected void handleDataMarker(@Nonnull String function, @Nonnull BlockPos pos, @Nonnull IServerWorld world, @Nonnull Random rand, @Nonnull MutableBoundingBox box) {
            if (function.equals("SpawnerUndead")) {
                if (box.isVecInside(pos)) {
                    world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2); //Set structure block to air, to remove its TE
                    world.setBlockState(pos, Blocks.SPAWNER.getDefaultState(), 2);

                    TileEntity tileEntity = world.getTileEntity(pos);
                    if (tileEntity instanceof MobSpawnerTileEntity) {
                        ((MobSpawnerTileEntity) tileEntity).getSpawnerBaseLogic().setEntityType(RuinPieces.RuinTemplate.UNDEAD.get(rand.nextInt(RuinPieces.RuinTemplate.UNDEAD.size())));
                    }
                }
            } else if (function.equals("Crate")) {
                if (box.isVecInside(pos)) {
                    if (rand.nextDouble() < 0.2D) {
                        world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2); //Set structure block to air, to remove its TE
                        world.setBlockState(pos, ChestBaseBlock.correctFacing(world, pos, AtumBlocks.DEADWOOD_CRATE.getDefaultState(), AtumBlocks.DEADWOOD_CRATE), 2);
                        LockableLootTileEntity.setLootTable(world, rand, pos, AtumLootTables.CRATE);
                    } else {
                        world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
                    }
                }
            } else if (function.equals("Chest")) {
                BlockPos posDown = pos.down();
                if (box.isVecInside(posDown)) {
                    LockableLootTileEntity.setLootTable(world, rand, posDown, AtumLootTables.TOMB_CHEST);
                }
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
            }
        }

        @Override
        protected void readAdditional(@Nonnull CompoundNBT compound) { //Is actually write, just horrible name
            super.readAdditional(compound);
            compound.putString("Rot", this.placeSettings.getRotation().name());
        }
    }
}
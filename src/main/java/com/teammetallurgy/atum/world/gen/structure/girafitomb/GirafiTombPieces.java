package com.teammetallurgy.atum.world.gen.structure.girafitomb;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.base.ChestBaseBlock;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.SarcophagusTileEntity;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.CrateTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.init.AtumStructurePieces;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
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

public class GirafiTombPieces {
    public static final ResourceLocation GIRAFI_TOMB = new ResourceLocation(Atum.MOD_ID, "girafi_tomb");

    public static class GirafiTombTemplate extends TemplateStructurePiece {
        private final Rotation rotation;

        public GirafiTombTemplate(TemplateManager manager, BlockPos pos, Rotation rotation) {
            super(AtumStructurePieces.GIRAFI_TOMB, 0);
            this.templatePosition = pos;
            this.rotation = rotation;
            this.loadTemplate(manager);
        }

        public GirafiTombTemplate(TemplateManager manager, CompoundNBT nbt) {
            super(AtumStructurePieces.GIRAFI_TOMB, nbt);
            this.rotation = Rotation.valueOf(nbt.getString("Rot"));
            this.loadTemplate(manager);
        }

        private void loadTemplate(TemplateManager manager) {
            Template template = manager.getTemplate(GIRAFI_TOMB);
            PlacementSettings placementsettings = (new PlacementSettings()).setIgnoreEntities(true).setRotation(this.rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
            if (template != null) {
                this.setup(template, this.templatePosition, placementsettings);
            }
        }

        @Override
        protected void handleDataMarker(@Nonnull String function, @Nonnull BlockPos pos, @Nonnull IServerWorld world, @Nonnull Random rand, @Nonnull MutableBoundingBox box) {
            if (function.equals("Crate")) {
                if (box.isVecInside(pos)) {
                    if (rand.nextDouble() <= 0.15D) {
                        world.setBlockState(pos, ChestBaseBlock.correctFacing(world, pos, AtumBlocks.DEADWOOD_CRATE.getDefaultState(), AtumBlocks.DEADWOOD_CRATE), 2);

                        TileEntity tileEntity = world.getTileEntity(pos);
                        if (tileEntity instanceof CrateTileEntity) {
                            ((CrateTileEntity) tileEntity).setLootTable(AtumLootTables.CRATE, rand.nextLong());
                        }
                    } else {
                        world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
                    }
                }
            } else if (function.equals("GirafiSarcophagus")) {
                BlockPos posDown = pos.down();
                if (box.isVecInside(posDown)) {
                    TileEntity tileentity = world.getTileEntity(posDown);
                    if (tileentity instanceof SarcophagusTileEntity) {
                        ((SarcophagusTileEntity) tileentity).setLootTable(AtumLootTables.GIRAFI_TOMB, rand.nextLong());
                    }
                    world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
                }
            } else if (function.equals("Sarcophagus")) {
                BlockPos posDown = pos.down();
                if (box.isVecInside(posDown)) {
                    TileEntity tileentity = world.getTileEntity(posDown);
                    if (tileentity instanceof SarcophagusTileEntity) {
                        ((SarcophagusTileEntity) tileentity).setLootTable(AtumLootTables.PHARAOH, rand.nextLong());
                    }
                    world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
                }
            }
        }

        @Override
        protected void readAdditional(@Nonnull CompoundNBT compound) { //Is actually write, just horrible name
            super.readAdditional(compound);
            compound.putString("Rot", this.placeSettings.getRotation().name());
        }
    }
}
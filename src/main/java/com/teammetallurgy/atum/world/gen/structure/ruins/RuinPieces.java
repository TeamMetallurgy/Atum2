package com.teammetallurgy.atum.world.gen.structure.ruins;

import com.teammetallurgy.atum.blocks.base.ChestBaseBlock;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.CrateTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.init.AtumStructurePieces;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class RuinPieces {
    private static List<EntityType<?>> getBandits() {
        return Arrays.asList(AtumEntities.BARBARIAN.get(), AtumEntities.BRIGAND.get(), AtumEntities.NOMAD.get());
    }

    public static List<EntityType<?>> getUndead() {
        return Arrays.asList(AtumEntities.BONESTORM.get(), AtumEntities.FORSAKEN.get(), AtumEntities.MUMMY.get(), AtumEntities.WRAITH.get());
    }

    public static class RuinTemplate extends TemplateStructurePiece {

        public RuinTemplate(StructureTemplateManager manager, ResourceLocation location, BlockPos pos, Rotation rotation) {
            super(AtumStructurePieces.RUIN.get(), 0, manager, location, location.toString(), makeSettings(rotation), pos);
        }

        public RuinTemplate(StructureTemplateManager manager, CompoundTag nbt) {
            super(AtumStructurePieces.RUIN.get(), nbt, manager, (t) -> makeSettings(Rotation.valueOf(nbt.getString("Rot"))));
        }

        private static StructurePlaceSettings makeSettings(Rotation rotation) {
            return (new StructurePlaceSettings()).setIgnoreEntities(true).setRotation(rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        }

        @Override
        protected void handleDataMarker(@Nonnull String function, @Nonnull BlockPos pos, @Nonnull ServerLevelAccessor level, @Nonnull RandomSource rand, @Nonnull BoundingBox box) {
            if (function.equals("Spawner")) {
                if (box.isInside(pos)) {
                    level.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), 2);

                    BlockEntity tileEntity = level.getBlockEntity(pos);
                    if (tileEntity instanceof SpawnerBlockEntity) {
                        EntityType<?> type;
                        if (rand.nextDouble() < 0.5D) {
                            type = RuinPieces.getBandits().get(rand.nextInt(RuinPieces.getBandits().size()));
                        } else {
                            type = RuinPieces.getUndead().get(rand.nextInt(RuinPieces.getUndead().size()));
                        }
                        ((SpawnerBlockEntity) tileEntity).setEntityId(type, rand);
                    }
                }
            } else if (function.equals("Crate")) {
                if (box.isInside(pos)) {
                    if (rand.nextDouble() <= 0.15D) {
                        level.setBlock(pos, ChestBaseBlock.correctFacing(level, pos, AtumBlocks.DEADWOOD_CRATE.get().defaultBlockState(), AtumBlocks.DEADWOOD_CRATE.get()), 2);
                        if (level.getBlockEntity(pos) instanceof CrateTileEntity crate) {
                            crate.setLootTable(AtumLootTables.CRATE, rand.nextLong());
                        }
                    } else {
                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    }
                }
            }
        }

        @Override
        protected void addAdditionalSaveData(@Nonnull StructurePieceSerializationContext context, @Nonnull CompoundTag compound) { //Is actually write, just horrible name
            super.addAdditionalSaveData(context, compound);
            compound.putString("Rot", this.getRotation().name());
        }
    }
}
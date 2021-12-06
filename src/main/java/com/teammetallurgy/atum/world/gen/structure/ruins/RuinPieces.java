package com.teammetallurgy.atum.world.gen.structure.ruins;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.base.ChestBaseBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.init.AtumStructurePieces;
import com.teammetallurgy.atum.misc.AtumConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
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
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RuinPieces {

    public static class RuinTemplate extends TemplateStructurePiece {
        private static final List<EntityType<?>> BANDITS = Arrays.asList(AtumEntities.BARBARIAN, AtumEntities.BRIGAND, AtumEntities.NOMAD);
        public static final List<EntityType<?>> UNDEAD = Arrays.asList(AtumEntities.BONESTORM, AtumEntities.FORSAKEN, AtumEntities.MUMMY, AtumEntities.WRAITH);
        private final int ruinType;
        private final Rotation rotation;

        public RuinTemplate(StructureManager manager, BlockPos pos, Random random, Rotation rotation) {
            super(AtumStructurePieces.RUIN, 0);
            this.templatePosition = pos;
            this.rotation = rotation;
            this.ruinType = Mth.nextInt(random, 1, AtumConfig.WORLD_GEN.ruinsAmount.get());
            this.loadTemplate(manager);
        }

        public RuinTemplate(StructureManager manager, CompoundTag nbt) {
            super(AtumStructurePieces.RUIN, nbt);
            this.rotation = Rotation.valueOf(nbt.getString("Rot"));
            this.ruinType = nbt.getInt("Type");
            this.loadTemplate(manager);
        }

        private void loadTemplate(StructureManager manager) {
            StructureTemplate template = manager.get(new ResourceLocation(Atum.MOD_ID, "ruins/ruin" + this.ruinType));
            StructurePlaceSettings placementsettings = (new StructurePlaceSettings()).setIgnoreEntities(true).setRotation(this.rotation).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
            if (template != null) {
                this.setup(template, this.templatePosition, placementsettings);
            }
        }

        @Override
        protected void handleDataMarker(@Nonnull String function, @Nonnull BlockPos pos, @Nonnull ServerLevelAccessor world, @Nonnull Random rand, @Nonnull BoundingBox box) {
            if (function.equals("Spawner")) {
                if (box.isInside(pos)) {
                    world.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), 2);

                    BlockEntity tileEntity = world.getBlockEntity(pos);
                    if (tileEntity instanceof SpawnerBlockEntity) {
                        EntityType<?> type;
                        if (rand.nextDouble() < 0.5D) {
                            type = BANDITS.get(rand.nextInt(BANDITS.size()));
                        } else {
                            type = UNDEAD.get(rand.nextInt(UNDEAD.size()));
                        }
                        ((SpawnerBlockEntity) tileEntity).getSpawner().setEntityId(type);
                    }
                }
            } else if (function.equals("Crate")) {
                if (box.isInside(pos)) {
                    if (rand.nextDouble() <= 0.15D) {
                        world.setBlock(pos, ChestBaseBlock.correctFacing(world, pos, AtumBlocks.DEADWOOD_CRATE.defaultBlockState(), AtumBlocks.DEADWOOD_CRATE), 2);
                        RandomizableContainerBlockEntity.setLootTable(world, rand, pos, AtumLootTables.CRATE);
                    } else {
                        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    }
                }
            }
        }

        @Override
        protected void addAdditionalSaveData(@Nonnull CompoundTag compound) { //Is actually write, just horrible name
            super.addAdditionalSaveData(compound);
            compound.putString("Rot", this.getRotation().name());
            compound.putInt("Type", this.ruinType);
        }
    }
}
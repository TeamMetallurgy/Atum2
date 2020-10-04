package com.teammetallurgy.atum.world.gen.structure.ruins;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.base.ChestBaseBlock;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.CrateTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.init.AtumStructurePieces;
import com.teammetallurgy.atum.misc.AtumConfig;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

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

        public RuinTemplate(TemplateManager manager, BlockPos pos, Random random, Rotation rotation) {
            super(AtumStructurePieces.RUIN, 0);
            this.templatePosition = pos;
            this.rotation = rotation;
            this.ruinType = MathHelper.nextInt(random, 1, AtumConfig.WORLD_GEN.ruinsAmount.get());
            this.loadTemplate(manager);
        }

        public RuinTemplate(TemplateManager manager, CompoundNBT nbt) {
            super(AtumStructurePieces.RUIN, nbt);
            this.rotation = Rotation.valueOf(nbt.getString("Rot"));
            this.ruinType = nbt.getInt("Type");
            this.loadTemplate(manager);
        }

        private void loadTemplate(TemplateManager manager) {
            Template template = manager.getTemplate(new ResourceLocation(Atum.MOD_ID, "ruins/ruin" + this.ruinType));
            PlacementSettings placementsettings = (new PlacementSettings()).setIgnoreEntities(true).setRotation(this.rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
            this.setup(template, this.templatePosition, placementsettings);
        }

        @Override
        protected void handleDataMarker(@Nonnull String function, @Nonnull BlockPos pos, @Nonnull IServerWorld world, @Nonnull Random rand, @Nonnull MutableBoundingBox box) {
            if (function.equals("Spawner")) {
                if (box.isVecInside(pos)) {
                    world.setBlockState(pos, Blocks.SPAWNER.getDefaultState(), 2);

                    TileEntity tileEntity = world.getTileEntity(pos);
                    if (tileEntity instanceof MobSpawnerTileEntity) {
                        EntityType<?> type;
                        if (rand.nextDouble() < 0.5D) {
                            type = BANDITS.get(rand.nextInt(BANDITS.size()));
                        } else {
                            type = UNDEAD.get(rand.nextInt(UNDEAD.size()));
                        }
                        ((MobSpawnerTileEntity) tileEntity).getSpawnerBaseLogic().setEntityType(type);
                    }
                }
            } else if (function.equals("Crate")) {
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
            }
        }

        @Override
        protected void readAdditional(@Nonnull CompoundNBT compound) { //Is actually write, just horrible name
            super.readAdditional(compound);
            compound.putString("Rot", this.placeSettings.getRotation().name());
            compound.putInt("Type", this.ruinType);
        }
    }
}
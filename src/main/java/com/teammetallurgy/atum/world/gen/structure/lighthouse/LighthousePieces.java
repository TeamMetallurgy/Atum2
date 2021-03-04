package com.teammetallurgy.atum.world.gen.structure.lighthouse;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.base.ChestBaseBlock;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.CrateTileEntity;
import com.teammetallurgy.atum.entity.efreet.SunspeakerEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.init.AtumStructurePieces;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnReason;
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
import java.util.*;

public class LighthousePieces {
    public static final ResourceLocation LIGHTHOUSE = new ResourceLocation(Atum.MOD_ID, "lighthouse");

    public static class LighthouseTemplate extends TemplateStructurePiece {
        private final Rotation rotation;
        private int sunspeakerSpawned;

        public LighthouseTemplate(TemplateManager manager, BlockPos pos, Rotation rotation) {
            super(AtumStructurePieces.LIGHTHOUSE, 0);
            this.templatePosition = pos;
            this.rotation = rotation;
            this.loadTemplate(manager);
        }

        public LighthouseTemplate(TemplateManager manager, CompoundNBT nbt) {
            super(AtumStructurePieces.LIGHTHOUSE, nbt);
            this.rotation = Rotation.valueOf(nbt.getString("Rot"));
            this.sunspeakerSpawned = nbt.getInt("SunspeakerCount");
            this.loadTemplate(manager);
        }

        private void loadTemplate(TemplateManager manager) {
            Template template = manager.getTemplate(LIGHTHOUSE);
            PlacementSettings placementsettings = (new PlacementSettings()).setRotation(this.rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
            this.setup(template, this.templatePosition, placementsettings);
        }

        private void spawnSunspeakers(IServerWorld world, MutableBoundingBox box, int x, int y, int z, int min, int max) {
            if (this.sunspeakerSpawned > 0) {
                return;
            }
            world.setBlockState(new BlockPos(x, y, z), Blocks.AIR.getDefaultState(), 2);
            Random rand = new Random(world.getWorld().getSeed() ^ x ^ (z << 16));

            int numToSpawn = rand.nextInt(1 + max - min) + min;

            // Since each level is has a different number of valid spawn space, this will ensure they spawn evenly on each level
            int[] levels = {0, 5, 10, 22};
            List<Integer> ylevels = new ArrayList<>();
            for (int i = 0; i < numToSpawn; i++) {
                ylevels.add(y + levels[rand.nextInt(levels.length)] + 1);
            }

            // Used to avoid sunspeakers spawning on top of each other
            Set<BlockPos> usedPosition = new HashSet<>();

            int tries = 0;
            while (this.sunspeakerSpawned < numToSpawn) {
                int sw = 2;
                int j = x + rand.nextInt(2 * sw + 1) - sw;
                int k = ylevels.get(sunspeakerSpawned);
                int l = z + rand.nextInt(2 * sw + 1) - sw;

                BlockPos pos = new BlockPos(j, k, l);
                if (usedPosition.contains(pos)) {
                    continue;
                }
                usedPosition.add(pos);

                if (box.isVecInside(pos) && world.isAirBlock(pos)) {
                    ++this.sunspeakerSpawned;

                    /*SunspeakerEntity sunspeaker = AtumEntities.SUNSPEAKER.create(world.getWorld());
                    if (sunspeaker != null) {
                        sunspeaker.setLocationAndAngles((double) j + 0.5D, k, (double) l + 0.5D, 0.0F, 0.0F);
                        sunspeaker.onInitialSpawn(world, world.getDifficultyForLocation(sunspeaker.getPosition()), SpawnReason.STRUCTURE, null, null);
                        world.addEntity(sunspeaker);
                    }*/
                }

                // Failsafe
                if (++tries > 100) {
                    break;
                }
            }
        }

        @Override
        protected void handleDataMarker(@Nonnull String function, @Nonnull BlockPos pos, @Nonnull IServerWorld world, @Nonnull Random rand, @Nonnull MutableBoundingBox box) {
            if (function.equals("PalmCrate")) {
                if (box.isVecInside(pos)) {
                    if (rand.nextDouble() <= 0.20D) {
                        world.setBlockState(pos, ChestBaseBlock.correctFacing(world, pos, AtumBlocks.PALM_CRATE.getDefaultState(), AtumBlocks.PALM_CRATE), 2);

                        TileEntity tileEntity = world.getTileEntity(pos);
                        if (tileEntity instanceof CrateTileEntity) {
                            ((CrateTileEntity) tileEntity).setLootTable(AtumLootTables.LIGHTHOUSE, rand.nextLong());
                        }
                    } else {
                        world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
                    }
                }
            } else if (function.equals("Sunspeaker")) {
                spawnSunspeakers(world, box, pos.getX(), pos.getY(), pos.getZ(), 2, 5);
            }
        }

        @Override
        protected void readAdditional(@Nonnull CompoundNBT compound) {
            super.readAdditional(compound);
            compound.putString("Rot", this.placeSettings.getRotation().name());
            compound.putInt("SunspeakerCount", this.sunspeakerSpawned);
        }
    }
}
package com.teammetallurgy.atum.world.gen.feature.tree;

import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldWriter;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.OptionalInt;
import java.util.Random;
import java.util.Set;

public class AtumTreeFeature extends TreeFeature { //TODO

    public AtumTreeFeature(Codec<BaseTreeFeatureConfig> config) {
        super(config);
    }
    
    /*@Override
    public boolean generate(@Nonnull ISeedReader seedReader, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BaseTreeFeatureConfig config) {
        MutableBoundingBox box = MutableBoundingBox.getNewBoundingBox();
        Set<BlockPos> logs = Sets.newHashSet();
        Set<BlockPos> leaves = Sets.newHashSet();
        int baseHeight = 2 + rand.nextInt(3) + rand.nextInt(2);
        //int baseHeight = config.field_236679_h_ + rand.nextInt(config.heightRandA + 1) + rand.nextInt(config.heightRandB + 1);
        boolean place = this.place(seedReader, rand, pos, logs, leaves, box, config);
        if (!place) {
            return false;
        } else { ;
            this.setFertileSoilAt(seedReader, pos.down(), pos);
            int height = baseHeight - rand.nextInt(4) - 1;
            int i1 = 3 - rand.nextInt(3);
            BlockPos.Mutable mutablePos = new BlockPos.Mutable();
            int x = pos.getX();
            int z = pos.getZ();
            int y = 0;

            for (int h = 0; h < baseHeight; ++h) {
                int treeHeight = pos.getY() + h;
                if (h >= height && i1 > 0) {
                    --i1;
                }
                if (setLog(seedReader, rand, mutablePos.setPos(x, treeHeight, z), logs, box, config)) {
                    y = treeHeight;
                }
            }

            if (config.ophidianTongueChance > 0.0D && rand.nextDouble() <= config.ophidianTongueChance) { //TODO
                Set<BlockPos> set = Sets.newHashSet();
                BlockPos genPos;
                if (rand.nextInt(4) == 0) {
                    genPos = pos.west();
                    if (isAirAt(seedReader, genPos)) {
                        this.addOphidianTongueToTree(seedReader, genPos, VineBlock.EAST, set, mutableBox);
                    }
                }
                if (rand.nextInt(4) == 0) {
                    genPos = pos.east();
                    if (isAirAt(seedReader, genPos)) {
                        this.addOphidianTongueToTree(seedReader, genPos, VineBlock.WEST, set, mutableBox);
                    }
                }
                if (rand.nextInt(4) == 0) {
                    genPos = pos.north();
                    if (isAirAt(seedReader, genPos)) {
                        this.addOphidianTongueToTree(seedReader, genPos, VineBlock.SOUTH, set, mutableBox);
                    }
                }
                if (rand.nextInt(4) == 0) {
                    genPos = pos.south();
                    if (isAirAt(seedReader, genPos)) {
                        this.addOphidianTongueToTree(seedReader, genPos, VineBlock.NORTH, set, mutableBox);
                    }
                }
            }
            return true;
        }
    }*/

    @Override
    protected boolean place(@Nonnull IWorldGenerationReader genReader, Random rand, BlockPos pos, Set<BlockPos> logs, Set<BlockPos> leaves, MutableBoundingBox box, BaseTreeFeatureConfig config) { //Coped from TreeFeature, to add more soil support
        int trunk = config.trunkPlacer.func_236917_a_(rand);
        int foliage = config.foliagePlacer.func_230374_a_(rand, trunk, config);
        int k = trunk - foliage;
        int l = config.foliagePlacer.func_230376_a_(rand, k);
        BlockPos blockpos;
        if (!config.forcePlacement) {
            int i = genReader.getHeight(Heightmap.Type.OCEAN_FLOOR, pos).getY();
            int j = genReader.getHeight(Heightmap.Type.WORLD_SURFACE, pos).getY();
            if (j - i > config.maxWaterDepth) {
                return false;
            }

            int k1;
            if (config.field_236682_l_ == Heightmap.Type.OCEAN_FLOOR) {
                k1 = i;
            } else if (config.field_236682_l_ == Heightmap.Type.WORLD_SURFACE) {
                k1 = j;
            } else {
                k1 = genReader.getHeight(config.field_236682_l_, pos).getY();
            }

            blockpos = new BlockPos(pos.getX(), k1, pos.getZ());
        } else {
            blockpos = pos;
        }

        if (blockpos.getY() >= 1 && blockpos.getY() + trunk + 1 <= 256) {
            if (isSoilOrFarm(genReader, blockpos.down())) {
                return false;
            } else {
                OptionalInt optionalInt = config.minimumSize.func_236710_c_();
                int l1 = this.func_241521_a_(genReader, trunk, blockpos, config);
                if (l1 >= trunk || optionalInt.isPresent() && l1 >= optionalInt.getAsInt()) {
                    List<FoliagePlacer.Foliage> list = config.trunkPlacer.func_230382_a_(genReader, rand, l1, blockpos, logs, box, config);
                    list.forEach((p_236407_8_) -> {
                        config.foliagePlacer.func_236752_a_(genReader, rand, config, l1, p_236407_8_, foliage, l, leaves, box);
                    });
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    protected static boolean isSoilOrFarm(IWorldGenerationBaseReader reader, @Nonnull BlockPos pos) {
        return isDirtOrFarmlandAt(reader, pos) || reader.hasBlockState(pos, (state -> state.getBlock() == AtumBlocks.LIMESTONE_GRAVEL));
    }

    private static boolean isDirtOrFarmlandAt(IWorldGenerationBaseReader reader, BlockPos pos) {
        return reader.hasBlockState(pos, (state) -> {
            Block block = state.getBlock();
            return isDirt(block) || block instanceof FarmlandBlock;
        });
    }

    //TODO Move all  these into Trunk or Foliage providers, if needed
    /*protected void setFertileSoilAt(IWorldGenerationReader reader, BlockPos pos, BlockPos origin) {
        if (!(reader instanceof IWorld)) {
            this.setFertileSoil(reader, pos);
            return;
        }
        //((IWorld) reader).getBlockState(pos).onPlantGrow((IWorld) reader, pos, origin); //TODO
    }

    protected void setFertileSoil(IWorldGenerationReader seedReader, BlockPos pos) {
        if (!(isFertileSoil(seedReader, pos))) {
            this.setBlockState(seedReader, pos, AtumBlocks.FERTILE_SOIL.getDefaultState());
        }
    }

    protected static boolean isFertileSoil(IWorldGenerationBaseReader seedReader, BlockPos pos) {
        return seedReader.hasBlockState(pos, (p) -> p.getBlock() != AtumBlocks.FERTILE_SOIL);
    }

    public static boolean isAirAt(IWorldGenerationBaseReader reader, BlockPos pos) {
        return reader.hasBlockState(pos, (ab) -> ab.isAir());
    }

    private void addOphidianTongueToTree(IWorldGenerationReader seedReader, BlockPos pos, BooleanProperty booleanProperty, Set<BlockPos> positions, MutableBoundingBox mutableBox) {
        this.generateOphidianTongue(seedReader, pos, booleanProperty, positions, mutableBox);
        int i = 4;
        for (pos = pos.down(); isAirAt(seedReader, pos) && i > 0; --i) {
            this.generateOphidianTongue(seedReader, pos, booleanProperty, positions, mutableBox);
            pos = pos.down();
        }
    }

    protected void generateOphidianTongue(IWorldWriter world, BlockPos pos, BooleanProperty booleanProperty, Set<BlockPos> positions, MutableBoundingBox mutableBox) {
        this.setOphidianTongue(world, pos, AtumBlocks.OPHIDIAN_TONGUE.getDefaultState().with(booleanProperty, true), positions, mutableBox);
    }

    protected void setOphidianTongue(IWorldWriter worldWriter, BlockPos pos, BlockState state, Set<BlockPos> positions, MutableBoundingBox mutableBox) {
        worldWriter.setBlockState(pos, state, 19);
        positions.add(pos);
        mutableBox.expandTo(new MutableBoundingBox(pos, pos));
    }*/
}
package com.teammetallurgy.atum.world.gen.feature;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.teammetallurgy.atum.blocks.vegetation.DateBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.gen.feature.config.PalmConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.VineBlock;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldWriter;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.OptionalInt;
import java.util.Random;
import java.util.Set;

public class PalmFeature extends Feature<PalmConfig> {

    public PalmFeature(Codec<PalmConfig> config) {
        super(config);
    }

    @Override
    public boolean func_241855_a(@Nonnull ISeedReader seedReader, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull PalmConfig config) {
        int baseHeight = config.baseHeight + rand.nextInt(config.heightRandA + 1) + rand.nextInt(config.heightRandB + 1);
        Set<BlockPos> logs = Sets.newHashSet();
        MutableBoundingBox mutableBox = MutableBoundingBox.getNewBoundingBox();
        boolean canPlace = this.place(seedReader, rand, pos, logs, Sets.newHashSet(), mutableBox, config);
        if (mutableBox.minX <= mutableBox.maxX && canPlace && !logs.isEmpty()) {
            BlockPos posOptional = optional.get();
            this.setFertileSoilAt(seedReader, posOptional.down(), posOptional);
            int height = baseHeight - rand.nextInt(4) - 1;
            int i1 = 3 - rand.nextInt(3);
            BlockPos.Mutable mutablePos = new BlockPos.Mutable();
            int x = posOptional.getX();
            int z = posOptional.getZ();
            int y = 0;

            for (int h = 0; h < baseHeight; ++h) {
                int treeHeight = posOptional.getY() + h;
                if (h >= height && i1 > 0) {
                    --i1;
                }
                if (this.setLog(seedReader, rand, mutablePos.setPos(x, treeHeight, z), logs, mutableBox, config)) {
                    y = treeHeight;
                }
            }

            BlockPos leafPos = new BlockPos(x, y, z);
            this.generateLeaf(seedReader, leafPos.up(), rand, config);
            for (BlockPos baseLeafPos : BlockPos.Mutable.getAllInBoxMutable(leafPos.add(-1, 0, -1), leafPos.add(1, 0, 1))) {
                this.generateLeaf(seedReader, baseLeafPos, rand, config);
            }
            this.generateLeaf(seedReader, leafPos.add(2, 0, 0), rand, config);
            this.generateLeaf(seedReader, leafPos.add(-2, 0, 0), rand, config);
            this.generateLeaf(seedReader, leafPos.add(0, 0, 2), rand, config);
            this.generateLeaf(seedReader, leafPos.add(0, 0, -2), rand, config);
            this.generateLeaf(seedReader, leafPos.add(0, -1, -2), rand, config);
            this.generateLeaf(seedReader, leafPos.add(0, -1, 2), rand, config);
            this.generateLeaf(seedReader, leafPos.add(2, -1, 0), rand, config);
            this.generateLeaf(seedReader, leafPos.add(-2, -1, 0), rand, config);
            this.generateLeaf(seedReader, leafPos.add(0, -1, -3), rand, config);
            this.generateLeaf(seedReader, leafPos.add(0, -1, 3), rand, config);
            this.generateLeaf(seedReader, leafPos.add(3, -1, 0), rand, config);
            this.generateLeaf(seedReader, leafPos.add(-3, -1, 0), rand, config);

            if (config.dateChance > 0.0D) {
                BlockPos datePos = leafPos.down().offset(Direction.Plane.HORIZONTAL.random(rand));
                if (rand.nextDouble() <= config.dateChance) {
                    seedReader.setBlockState(datePos, AtumBlocks.DATE_BLOCK.getDefaultState().with(DateBlock.AGE, MathHelper.nextInt(rand, 0, 7)), 2);
                    if (rand.nextDouble() <= 0.25F) { //Chance for 2nd date
                        datePos = leafPos.down().offset(Direction.Plane.HORIZONTAL.random(rand));
                        seedReader.setBlockState(datePos, AtumBlocks.DATE_BLOCK.getDefaultState().with(DateBlock.AGE, MathHelper.nextInt(rand, 0, 7)), 2);
                    }
                }
            }

            if (config.ophidianTongueChance > 0.0D && rand.nextDouble() <= config.ophidianTongueChance) {
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
    }

    private boolean place(IWorldGenerationReader generationReader, Random rand, BlockPos positionIn, Set<BlockPos> p_225557_4_, Set<BlockPos> p_225557_5_, MutableBoundingBox box, PalmConfig config) { //Coped from TreeFeature, to add Limestone Gravel support
        int i = config.field_236678_g_.func_236917_a_(rand);
        int j = config.field_236677_f_.func_230374_a_(rand, i, config);
        int k = i - j;
        int l = config.field_236677_f_.func_230376_a_(rand, k);
        BlockPos blockpos;
        if (!config.forcePlacement) {
            int i1 = generationReader.getHeight(Heightmap.Type.OCEAN_FLOOR, positionIn).getY();
            int j1 = generationReader.getHeight(Heightmap.Type.WORLD_SURFACE, positionIn).getY();
            if (j1 - i1 > config.field_236680_i_) {
                return false;
            }

            int k1;
            if (config.field_236682_l_ == Heightmap.Type.OCEAN_FLOOR) {
                k1 = i1;
            } else if (config.field_236682_l_ == Heightmap.Type.WORLD_SURFACE) {
                k1 = j1;
            } else {
                k1 = generationReader.getHeight(config.field_236682_l_, positionIn).getY();
            }

            blockpos = new BlockPos(positionIn.getX(), k1, positionIn.getZ());
        } else {
            blockpos = positionIn;
        }

        if (blockpos.getY() >= 1 && blockpos.getY() + i + 1 <= 256) {
            if (!isSoilOrFarm(generationReader, blockpos.down())) {
                return false;
            } else {
                OptionalInt optionalint = config.field_236679_h_.func_236710_c_();
                int l1 = this.func_241521_a_(generationReader, i, blockpos, config);
                if (l1 >= i || optionalint.isPresent() && l1 >= optionalint.getAsInt()) {
                    List<FoliagePlacer.Foliage> list = config.field_236678_g_.func_230382_a_(generationReader, rand, l1, blockpos, p_225557_4_, box, config);
                    list.forEach((p_236407_8_) -> {
                        config.field_236677_f_.func_236752_a_(generationReader, rand, config, l1, p_236407_8_, j, l, p_225557_5_, box);
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

    protected void setFertileSoilAt(IWorldGenerationReader reader, BlockPos pos, BlockPos origin) {
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

    private void generateLeaf(IWorldGenerationReader seedReader, BlockPos pos, Random rand, PalmConfig config) {
        if (TreeFeature.isAirOrLeavesAt(seedReader, pos)) {
            seedReader.setBlockState(pos, config.leavesProvider.getBlockState(rand, pos), 19);
        }
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
    }
}
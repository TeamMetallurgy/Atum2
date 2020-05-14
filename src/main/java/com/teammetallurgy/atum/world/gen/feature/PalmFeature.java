package com.teammetallurgy.atum.world.gen.feature;

import com.google.common.collect.Sets;
import com.mojang.datafixers.Dynamic;
import com.teammetallurgy.atum.blocks.vegetation.DateBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.gen.feature.config.PalmConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.VineBlock;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldWriter;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractSmallTreeFeature;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class PalmFeature extends AbstractSmallTreeFeature<PalmConfig> { //Based on Acacia tree

    public PalmFeature(Function<Dynamic<?>, ? extends PalmConfig> dynamicFunction) {
        super(dynamicFunction);
    }

    @Override
    protected boolean place(@Nonnull IWorldGenerationReader genReader, Random rand, @Nonnull BlockPos pos, @Nonnull Set<BlockPos> logs, @Nonnull Set<BlockPos> leaves, @Nonnull MutableBoundingBox mutableBox, PalmConfig config) {
        int baseHeight = config.baseHeight + rand.nextInt(config.heightRandA + 1) + rand.nextInt(config.heightRandB + 1);
        int trunkHeight = config.trunkHeight >= 0 ? config.trunkHeight + rand.nextInt(config.trunkHeightRandom + 1) : baseHeight - (config.foliageHeight + rand.nextInt(config.foliageHeightRandom + 1));
        int foliagePlacer = config.foliagePlacer.func_225573_a_(rand, trunkHeight, baseHeight, config);
        Optional<BlockPos> optional = this.func_227212_a_(genReader, baseHeight, trunkHeight, foliagePlacer, pos, config);
        if (!optional.isPresent()) {
            return false;
        } else {
            BlockPos posOptional = optional.get();
            this.setFertileSoilAt(genReader, posOptional.down(), posOptional);
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
                if (this.func_227216_a_(genReader, rand, mutablePos.setPos(x, treeHeight, z), logs, mutableBox, config)) {
                    y = treeHeight;
                }
            }

            BlockPos leafPos = new BlockPos(x, y, z);
            //config.foliagePlacer.func_225571_a_(genReader, rand, config, baseHeight, trunkHeight, foliagePlacer + 1, leafPos, leaves);
            this.generateLeaf(genReader, leafPos.up(), rand, config);
            for (BlockPos baseLeafPos : BlockPos.Mutable.getAllInBoxMutable(leafPos.add(-1, 0, -1), leafPos.add(1, 0, 1))) {
                this.generateLeaf(genReader, baseLeafPos, rand, config);
            }
            this.generateLeaf(genReader, leafPos.add(2, 0, 0), rand, config);
            this.generateLeaf(genReader, leafPos.add(-2, 0, 0), rand, config);
            this.generateLeaf(genReader, leafPos.add(0, 0, 2), rand, config);
            this.generateLeaf(genReader, leafPos.add(0, 0, -2), rand, config);
            this.generateLeaf(genReader, leafPos.add(0, -1, -2), rand, config);
            this.generateLeaf(genReader, leafPos.add(0, -1, 2), rand, config);
            this.generateLeaf(genReader, leafPos.add(2, -1, 0), rand, config);
            this.generateLeaf(genReader, leafPos.add(-2, -1, 0), rand, config);
            this.generateLeaf(genReader, leafPos.add(0, -1, -3), rand, config);
            this.generateLeaf(genReader, leafPos.add(0, -1, 3), rand, config);
            this.generateLeaf(genReader, leafPos.add(3, -1, 0), rand, config);
            this.generateLeaf(genReader, leafPos.add(-3, -1, 0), rand, config);

            if (config.dateChance > 0.0D) {
                BlockPos datePos = leafPos.down().offset(Direction.Plane.HORIZONTAL.random(rand));
                if (rand.nextDouble() <= config.dateChance) {
                    genReader.setBlockState(datePos, AtumBlocks.DATE_BLOCK.getDefaultState().with(DateBlock.AGE, MathHelper.nextInt(rand, 0, 7)), 2);
                    if (rand.nextDouble() <= 0.25F) { //Chance for 2nd date
                        datePos = leafPos.down().offset(Direction.Plane.HORIZONTAL.random(rand));
                        genReader.setBlockState(datePos, AtumBlocks.DATE_BLOCK.getDefaultState().with(DateBlock.AGE, MathHelper.nextInt(rand, 0, 7)), 2);
                    }
                }
            }

            if (config.ophidianTongueChance > 0.0D && rand.nextDouble() <= config.ophidianTongueChance) {
                Set<BlockPos> set = Sets.newHashSet();
                BlockPos genPos;
                if (rand.nextInt(4) == 0) {
                    genPos = pos.west();
                    if (AbstractTreeFeature.isAir(genReader, genPos)) {
                        this.addOphidianTongueToTree(genReader, genPos, VineBlock.EAST, set, mutableBox);
                    }
                }
                if (rand.nextInt(4) == 0) {
                    genPos = pos.east();
                    if (AbstractTreeFeature.isAir(genReader, genPos)) {
                        this.addOphidianTongueToTree(genReader, genPos, VineBlock.WEST, set, mutableBox);
                    }
                }
                if (rand.nextInt(4) == 0) {
                    genPos = pos.north();
                    if (AbstractTreeFeature.isAir(genReader, genPos)) {
                        this.addOphidianTongueToTree(genReader, genPos, VineBlock.SOUTH, set, mutableBox);
                    }
                }
                if (rand.nextInt(4) == 0) {
                    genPos = pos.south();
                    if (AbstractTreeFeature.isAir(genReader, genPos)) {
                        this.addOphidianTongueToTree(genReader, genPos, VineBlock.NORTH, set, mutableBox);
                    }
                }
            }
            return true;
        }
    }

    @Override
    @Nonnull
    public Optional<BlockPos> func_227212_a_(@Nonnull IWorldGenerationReader genReader, int baseHeight, int trunkHeight, int foliagePlacer, BlockPos pos, TreeFeatureConfig config) { //Coped from AbstractTreeFeature, to add Limestone Gravel support
        BlockPos blockpos;
        if (!config.forcePlacement) {
            int i = genReader.getHeight(Heightmap.Type.OCEAN_FLOOR, pos).getY();
            int j = genReader.getHeight(Heightmap.Type.WORLD_SURFACE, pos).getY();
            blockpos = new BlockPos(pos.getX(), i, pos.getZ());
            if (j - i > config.maxWaterDepth) {
                return Optional.empty();
            }
        } else {
            blockpos = pos;
        }

        if (blockpos.getY() >= 1 && blockpos.getY() + baseHeight + 1 <= genReader.getMaxHeight()) {
            for(int i1 = 0; i1 <= baseHeight + 1; ++i1) {
                int j1 = config.foliagePlacer.func_225570_a_(trunkHeight, baseHeight, foliagePlacer, i1);
                BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

                for(int k = -j1; k <= j1; ++k) {
                    int l = -j1;

                    while(l <= j1) {
                        if (i1 + blockpos.getY() >= 0 && i1 + blockpos.getY() < genReader.getMaxHeight()) {
                            blockpos$mutable.setPos(k + blockpos.getX(), i1 + blockpos.getY(), l + blockpos.getZ());
                            if (canBeReplacedByLogs(genReader, blockpos$mutable) && (config.ignoreVines || !isVine(genReader, blockpos$mutable))) {
                                ++l;
                                continue;
                            }

                            return Optional.empty();
                        }

                        return Optional.empty();
                    }
                }
            }
            return isSoilOrFarm(genReader, blockpos.down(), config.getSapling()) && blockpos.getY() < genReader.getMaxHeight() - baseHeight - 1 ? Optional.of(blockpos) : Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    protected static boolean isSoilOrFarm(IWorldGenerationBaseReader reader, @Nonnull BlockPos pos, IPlantable sapling) {
        if (!(reader instanceof IBlockReader) || sapling == null) {
            return isDirtOrGrassBlockOrFarmland(reader, pos) || reader.hasBlockState(pos, (state -> state.getBlock() == AtumBlocks.LIMESTONE_GRAVEL));
        }
        return reader.hasBlockState(pos, state -> state.canSustainPlant((IBlockReader) reader, pos, Direction.UP, sapling));
    }

    protected void setFertileSoilAt(IWorldGenerationReader reader, BlockPos pos, BlockPos origin) {
        if (!(reader instanceof IWorld)) {
            this.setFertileSoil(reader, pos);
            return;
        }
        ((IWorld) reader).getBlockState(pos).onPlantGrow((IWorld) reader, pos, origin);
    }

    protected void setFertileSoil(IWorldGenerationReader genReader, BlockPos pos) {
        if (!(isFertileSoil(genReader, pos))) {
            this.setBlockState(genReader, pos, AtumBlocks.FERTILE_SOIL.getDefaultState());
        }
    }

    protected static boolean isFertileSoil(IWorldGenerationBaseReader genReader, BlockPos pos) {
        return genReader.hasBlockState(pos, (p) -> p.getBlock() != AtumBlocks.FERTILE_SOIL);
    }

    private void generateLeaf(IWorldGenerationReader genReader, BlockPos pos, Random rand, PalmConfig config) {
        if (AbstractTreeFeature.isAirOrLeaves(genReader, pos)) {
            genReader.setBlockState(pos, config.leavesProvider.getBlockState(rand, pos), 19);
        }
    }

    private void addOphidianTongueToTree(IWorldGenerationReader genReader, BlockPos pos, BooleanProperty booleanProperty, Set<BlockPos> positions, MutableBoundingBox mutableBox) {
        this.generateOphidianTongue(genReader, pos, booleanProperty, positions, mutableBox);
        int i = 4;
        for (pos = pos.down(); AbstractTreeFeature.isAir(genReader, pos) && i > 0; --i) {
            this.generateOphidianTongue(genReader, pos, booleanProperty, positions, mutableBox);
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
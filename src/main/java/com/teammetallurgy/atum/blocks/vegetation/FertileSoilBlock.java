package com.teammetallurgy.atum.blocks.vegetation;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class FertileSoilBlock extends Block implements BonemealableBlock {

    public FertileSoilBlock() {
        super(Block.Properties.of(Material.GRASS, MaterialColor.GRASS).randomTicks().strength(0.5F).sound(SoundType.GRAVEL));
    }

    @Override
    public int getLightBlock(@Nonnull BlockState state, @Nonnull BlockGetter reader, @Nonnull BlockPos pos) {
        return 255;
    }

    @Override
    public void randomTick(@Nonnull BlockState state, ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
        if (!level.isClientSide) {
            if (!level.isAreaLoaded(pos, 3)) return;

            if (!hasWater(level, pos)) {
                Optional<ResourceKey<Biome>> biomeKey = level.registryAccess().registryOrThrow(Registries.BIOME).getResourceKey(level.getBiome(pos).value());
                /*if (biomeKey.isPresent() && biomeKey.get() != AtumBiomes.OASIS) { //TODO Readd when biomes is fixed
                    level.setBlock(pos, AtumBlocks.SAND.get().defaultBlockState(), 2);
                }*/
            } else if (Block.isFaceFull(level.getBlockState(pos.above()).getCollisionShape(level, pos), Direction.DOWN)) {
                if (level.random.nextDouble() >= 0.5D) {
                    level.setBlock(pos, AtumBlocks.SAND.get().defaultBlockState(), 2);
                }
            } else {
                if (level.getMaxLocalRawBrightness(pos.above()) >= 9) {
                    for (int i = 0; i < 4; ++i) {
                        BlockPos posGrow = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);

                        if (posGrow.getY() >= 0 && posGrow.getY() < 256 && !level.hasChunkAt(posGrow) || !hasWater(level, posGrow)) {
                            return;
                        }
                        BlockState stateUp = level.getBlockState(posGrow.above());
                        BlockState stateGrow = level.getBlockState(posGrow);

                        if (stateGrow.getBlock() == AtumBlocks.SAND.get() && level.getMaxLocalRawBrightness(posGrow.above()) >= 4 && stateUp.getLightBlock(level, pos.above()) <= 2) {
                            level.setBlockAndUpdate(posGrow, AtumBlocks.FERTILE_SOIL.get().defaultBlockState());
                        }
                    }
                }
            }
        }
    }

    private boolean hasWater(Level level, BlockPos pos) {
        for (BlockPos mutablePos : BlockPos.betweenClosed(pos.offset(-6, -1, -6), pos.offset(6, 4, 6))) {
            if (level.getBlockState(mutablePos).getFluidState().is(FluidTags.WATER)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canSustainPlant(@Nonnull BlockState state, @Nonnull BlockGetter level, BlockPos pos, @Nonnull Direction direction, IPlantable plantable) {
        BlockState plant = plantable.getPlant(level, pos.relative(direction));
        PlantType plantType = plantable.getPlantType(level, pos.above());

        boolean hasWater = level.getBlockState(pos.east()).getFluidState().is(FluidTags.WATER) ||
                level.getBlockState(pos.west()).getFluidState().is(FluidTags.WATER) ||
                level.getBlockState(pos.north()).getFluidState().is(FluidTags.WATER) ||
                level.getBlockState(pos.south()).getFluidState().is(FluidTags.WATER);

        if (plantType.equals(PlantType.PLAINS) || plantType.equals(PlantType.DESERT)) {
            return true;
        } else if (plantType.equals(PlantType.BEACH)) {
            return hasWater;
        } else if (plantType.equals(PlantType.CROP)) {
            return plant.getBlock() instanceof StemBlock;
        } else {
            return super.canSustainPlant(state, level, pos, direction, plantable);
        }
    }

    @Override
    public boolean isValidBonemealTarget(@Nonnull LevelReader level, @Nonnull BlockPos pos, @Nonnull BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(@Nonnull Level level, @Nonnull RandomSource rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(@Nonnull ServerLevel level, @Nonnull RandomSource rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        BlockPos posUp = pos.above();

        for (int amount = 0; amount < 36; ++amount) {
            BlockPos up = posUp;
            int amountCheck = 0;

            while (true) {
                if (amountCheck >= amount / 16) {
                    if (level.isEmptyBlock(up)) {
                        if (rand.nextDouble() <= 75) {
                            BlockState grassState = AtumBlocks.OASIS_GRASS.get().defaultBlockState();
                            if (AtumBlocks.OASIS_GRASS.get().canSurvive(grassState, level, up)) {
                                level.setBlock(up, grassState, 3);
                            }
                        }
                    }
                    break;
                }
                up = up.offset(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);

                if (level.getBlockState(up.below()).getBlock() != AtumBlocks.FERTILE_SOIL.get() || level.getBlockState(up).isRedstoneConductor(level, up)) {
                    break;
                }
                ++amountCheck;
            }
        }
    }

    @Override
    public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
        if (toolAction == ToolActions.HOE_TILL) {
            if (context.getItemInHand().getItem() == AtumItems.OSIRIS_BLESSING.get()) {
                return AtumBlocks.FERTILE_SOIL_TILLED.get().defaultBlockState().setValue(FertileSoilTilledBlock.MOISTURE, 7).setValue(FertileSoilTilledBlock.BLESSED, true);
            } else {
                return AtumBlocks.FERTILE_SOIL_TILLED.get().defaultBlockState();
            }
        } else if (toolAction == ToolActions.SHOVEL_FLATTEN) {
            return AtumBlocks.FERTILE_SOIL_PATH.get().defaultBlockState();
        } else {
            return super.getToolModifiedState(state, context, toolAction, simulate);
        }
    }
}
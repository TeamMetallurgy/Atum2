package com.teammetallurgy.atum.blocks.vegetation;

import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
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
import java.util.Optional;
import java.util.Random;

public class FertileSoilBlock extends Block implements BonemealableBlock {

    public FertileSoilBlock() {
        super(Block.Properties.of(Material.GRASS, MaterialColor.GRASS).randomTicks().strength(0.5F).sound(SoundType.GRAVEL));
    }

    @Override
    public int getLightBlock(@Nonnull BlockState state, @Nonnull BlockGetter reader, @Nonnull BlockPos pos) {
        return 255;
    }

    @Override
    public void randomTick(@Nonnull BlockState state, ServerLevel world, @Nonnull BlockPos pos, @Nonnull Random random) {
        if (!world.isClientSide) {
            if (!world.isAreaLoaded(pos, 3)) return;

            if (!hasWater(world, pos)) {
                Optional<ResourceKey<Biome>> biomeKey = world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getResourceKey(world.getBiome(pos).value());
                if (biomeKey.isPresent() && biomeKey.get() != AtumBiomes.OASIS) {
                    world.setBlock(pos, AtumBlocks.SAND.defaultBlockState(), 2);
                }
            } else if (Block.isFaceFull(world.getBlockState(pos.above()).getCollisionShape(world, pos), Direction.DOWN)) {
                if (world.random.nextDouble() >= 0.5D) {
                    world.setBlock(pos, AtumBlocks.SAND.defaultBlockState(), 2);
                }
            } else {
                if (world.getMaxLocalRawBrightness(pos.above()) >= 9) {
                    for (int i = 0; i < 4; ++i) {
                        BlockPos posGrow = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);

                        if (posGrow.getY() >= 0 && posGrow.getY() < 256 && !world.hasChunkAt(posGrow) || !hasWater(world, posGrow)) {
                            return;
                        }
                        BlockState stateUp = world.getBlockState(posGrow.above());
                        BlockState stateGrow = world.getBlockState(posGrow);

                        if (stateGrow.getBlock() == AtumBlocks.SAND && world.getMaxLocalRawBrightness(posGrow.above()) >= 4 && stateUp.getLightBlock(world, pos.above()) <= 2) {
                            world.setBlockAndUpdate(posGrow, AtumBlocks.FERTILE_SOIL.defaultBlockState());
                        }
                    }
                }
            }
        }
    }

    private boolean hasWater(Level world, BlockPos pos) {
        for (BlockPos mutablePos : BlockPos.betweenClosed(pos.offset(-6, -1, -6), pos.offset(6, 4, 6))) {
            if (world.getBlockState(mutablePos).getFluidState().is(FluidTags.WATER)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canSustainPlant(@Nonnull BlockState state, @Nonnull BlockGetter world, BlockPos pos, @Nonnull Direction direction, IPlantable plantable) {
        BlockState plant = plantable.getPlant(world, pos.relative(direction));
        PlantType plantType = plantable.getPlantType(world, pos.above());

        boolean hasWater = world.getBlockState(pos.east()).getFluidState().is(FluidTags.WATER) ||
                world.getBlockState(pos.west()).getFluidState().is(FluidTags.WATER) ||
                world.getBlockState(pos.north()).getFluidState().is(FluidTags.WATER) ||
                world.getBlockState(pos.south()).getFluidState().is(FluidTags.WATER);

        if (plantType.equals(PlantType.PLAINS) || plantType.equals(PlantType.DESERT)) {
            return true;
        } else if (plantType.equals(PlantType.BEACH)) {
            return hasWater;
        } else if (plantType.equals(PlantType.CROP)) {
            return plant.getBlock() instanceof StemBlock;
        } else {
            return super.canSustainPlant(state, world, pos, direction, plantable);
        }
    }

    @Override
    public boolean isValidBonemealTarget(@Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(@Nonnull Level world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(@Nonnull ServerLevel world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        BlockPos posUp = pos.above();

        for (int amount = 0; amount < 36; ++amount) {
            BlockPos up = posUp;
            int amountCheck = 0;

            while (true) {
                if (amountCheck >= amount / 16) {
                    if (world.isEmptyBlock(up)) {
                        if (rand.nextDouble() <= 75) {
                            BlockState grassState = AtumBlocks.OASIS_GRASS.defaultBlockState();
                            if (AtumBlocks.OASIS_GRASS.canSurvive(grassState, world, up)) {
                                world.setBlock(up, grassState, 3);
                            }
                        }
                    }
                    break;
                }
                up = up.offset(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);

                if (world.getBlockState(up.below()).getBlock() != AtumBlocks.FERTILE_SOIL || world.getBlockState(up).isRedstoneConductor(world, up)) {
                    break;
                }
                ++amountCheck;
            }
        }
    }

    @Override
    public BlockState getToolModifiedState(BlockState state, Level world, BlockPos pos, Player player, @Nonnull ItemStack stack, ToolAction toolAction) {
        /*if (toolAction == ToolActions.HOE_TILL) { //TODO Requires Forge to fix HOE_TILL
            if (stack.getItem() == AtumItems.OSIRIS_BLESSING) {
                return AtumBlocks.FERTILE_SOIL_TILLED.defaultBlockState().setValue(FertileSoilTilledBlock.MOISTURE, 7).setValue(FertileSoilTilledBlock.BLESSED, true);
            } else {
                return AtumBlocks.FERTILE_SOIL_TILLED.defaultBlockState();
            }
        } else */if (toolAction == ToolActions.SHOVEL_FLATTEN) {
            return AtumBlocks.FERTILE_SOIL_PATH.defaultBlockState();
        } else {
            return super.getToolModifiedState(state, world, pos, player, stack, toolAction);
        }
    }
}
package com.teammetallurgy.atum.blocks.vegetation;

import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.Random;

public class FertileSoilBlock extends Block implements IGrowable {

    public FertileSoilBlock() {
        super(Block.Properties.create(Material.ORGANIC, MaterialColor.GRASS).tickRandomly().hardnessAndResistance(0.5F).sound(SoundType.GROUND).harvestTool(ToolType.SHOVEL).harvestLevel(0));
    }

    @Override
    public int getOpacity(@Nonnull BlockState state, @Nonnull IBlockReader reader, @Nonnull BlockPos pos) {
        return 255;
    }

    @Override
    public void randomTick(@Nonnull BlockState state, ServerWorld world, @Nonnull BlockPos pos, @Nonnull Random random) {
        if (!world.isRemote) {
            if (!world.isAreaLoaded(pos, 3)) return;

            if (!hasWater(world, pos)) {
                Optional<RegistryKey<Biome>> biomeKey = world.func_241828_r().getRegistry(Registry.BIOME_KEY).getOptionalKey(world.getBiome(pos));
                if (biomeKey.isPresent() && biomeKey.get() != AtumBiomes.OASIS) {
                    world.setBlockState(pos, AtumBlocks.SAND.getDefaultState(), 2);
                }
            } else if (Block.doesSideFillSquare(world.getBlockState(pos.up()).getCollisionShape(world, pos), Direction.DOWN)) {
                if (world.rand.nextDouble() >= 0.5D) {
                    world.setBlockState(pos, AtumBlocks.SAND.getDefaultState(), 2);
                }
            } else {
                if (world.getLight(pos.up()) >= 9) {
                    for (int i = 0; i < 4; ++i) {
                        BlockPos posGrow = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);

                        if (posGrow.getY() >= 0 && posGrow.getY() < 256 && !world.isBlockLoaded(posGrow) || !hasWater(world, posGrow)) {
                            return;
                        }
                        BlockState stateUp = world.getBlockState(posGrow.up());
                        BlockState stateGrow = world.getBlockState(posGrow);

                        if (stateGrow.getBlock() == AtumBlocks.SAND && world.getLight(posGrow.up()) >= 4 && stateUp.getOpacity(world, pos.up()) <= 2) {
                            world.setBlockState(posGrow, AtumBlocks.FERTILE_SOIL.getDefaultState());
                        }
                    }
                }
            }
        }
    }

    private boolean hasWater(World world, BlockPos pos) {
        for (BlockPos mutablePos : BlockPos.getAllInBoxMutable(pos.add(-6, -1, -6), pos.add(6, 4, 6))) {
            if (world.getBlockState(mutablePos).getFluidState().isTagged(FluidTags.WATER)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canSustainPlant(@Nonnull BlockState state, @Nonnull IBlockReader world, BlockPos pos, @Nonnull Direction direction, IPlantable plantable) {
        BlockState plant = plantable.getPlant(world, pos.offset(direction));
        PlantType plantType = plantable.getPlantType(world, pos.up());

        boolean hasWater = world.getBlockState(pos.east()).getFluidState().isTagged(FluidTags.WATER) ||
                world.getBlockState(pos.west()).getFluidState().isTagged(FluidTags.WATER) ||
                world.getBlockState(pos.north()).getFluidState().isTagged(FluidTags.WATER) ||
                world.getBlockState(pos.south()).getFluidState().isTagged(FluidTags.WATER);

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
    public boolean canGrow(@Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean canUseBonemeal(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        return true;
    }

    @Override
    public void grow(@Nonnull ServerWorld world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        BlockPos posUp = pos.up();

        for (int amount = 0; amount < 36; ++amount) {
            BlockPos up = posUp;
            int amountCheck = 0;

            while (true) {
                if (amountCheck >= amount / 16) {
                    if (world.isAirBlock(up)) {
                        if (rand.nextDouble() <= 75) {
                            BlockState grassState = AtumBlocks.OASIS_GRASS.getDefaultState();
                            if (AtumBlocks.OASIS_GRASS.isValidPosition(grassState, world, up)) {
                                world.setBlockState(up, grassState, 3);
                            }
                        }
                    }
                    break;
                }
                up = up.add(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);

                if (world.getBlockState(up.down()).getBlock() != AtumBlocks.FERTILE_SOIL || world.getBlockState(up).isNormalCube(world, up)) {
                    break;
                }
                ++amountCheck;
            }
        }
    }

    @Override
    public BlockState getToolModifiedState(BlockState state, World world, BlockPos pos, PlayerEntity player, @Nonnull ItemStack stack, ToolType toolType) {
        if (toolType == ToolType.HOE) {
            if (stack.getItem() == AtumItems.TEFNUTS_BLESSING) {
                return AtumBlocks.FERTILE_SOIL_TILLED.getDefaultState().with(FertileSoilTilledBlock.MOISTURE, 7).with(FertileSoilTilledBlock.BLESSED, true);
            } else {
                return AtumBlocks.FERTILE_SOIL_TILLED.getDefaultState();
            }
        } else if (toolType == ToolType.SHOVEL) {
            return AtumBlocks.FERTILE_SOIL_PATH.getDefaultState();
        } else {
            return super.getToolModifiedState(state, world, pos, player, stack, toolType);
        }
    }
}
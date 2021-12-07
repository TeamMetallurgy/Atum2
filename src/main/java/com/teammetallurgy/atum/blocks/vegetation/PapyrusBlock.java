package com.teammetallurgy.atum.blocks.vegetation;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nonnull;

public class PapyrusBlock extends SugarCaneBlock {
    private static final BooleanProperty TOP = BooleanProperty.create("top");

    public PapyrusBlock() {
        super(Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0.0F).sound(SoundType.GRASS));
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(TOP, false));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockState soil = world.getBlockState(pos.below());
        Block block = soil.getBlock();

        if (block == this) {
            return true;
        } else {
            if (block == AtumBlocks.FERTILE_SOIL || block == AtumBlocks.FERTILE_SOIL_TILLED || block == AtumBlocks.SAND) {
                BlockPos powDown = pos.below();

                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    FluidState fluidState = world.getFluidState(powDown.relative(direction));
                    if (fluidState.is(FluidTags.WATER)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    @Override
    public boolean canSustainPlant(@Nonnull BlockState state, @Nonnull BlockGetter world, BlockPos pos, @Nonnull Direction direction, IPlantable plantable) {
        BlockState plant = plantable.getPlant(world, pos.relative(direction));
        if (plant.getBlock() == AtumBlocks.PAPYRUS && this == AtumBlocks.PAPYRUS) {
            return true;
        }
        return super.canSustainPlant(state, world, pos, direction, plantable);
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        world.setBlockAndUpdate(pos, state.setValue(TOP, world.isEmptyBlock(pos.above())));
        if (world.getBlockState(pos.below()).equals(state.setValue(TOP, true))) { //Correct non-top papyrus, when manually placed
            world.setBlockAndUpdate(pos.below(), state.setValue(TOP, false));
        }
    }

    @Override
    public void onRemove(BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        if (world.getBlockState(pos.below()).getBlock() == this) {
            world.setBlockAndUpdate(pos.below(), state.setValue(TOP, world.isEmptyBlock(pos.above()))); //Fix new top, when top gets removed
        }
    }

    @Override
    @Nonnull
    public ItemStack getCloneItemStack(@Nonnull BlockGetter getter, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new ItemStack(AtumItems.PAPYRUS_PLANT);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
        container.add(AGE, TOP);
    }
}
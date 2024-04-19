package com.teammetallurgy.atum.blocks.vegetation;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.common.IPlantable;

import javax.annotation.Nonnull;

public class PapyrusBlock extends SugarCaneBlock {
    private static final BooleanProperty TOP = BooleanProperty.create("top");

    public PapyrusBlock() {
        super(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).instabreak().noCollission().randomTicks().sound(SoundType.GRASS));
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(TOP, false));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState soil = level.getBlockState(pos.below());
        Block block = soil.getBlock();

        if (block == this) {
            return true;
        } else {
            if (block == AtumBlocks.FERTILE_SOIL.get() || block == AtumBlocks.FERTILE_SOIL_TILLED.get() || block == AtumBlocks.STRANGE_SAND.get()) {
                BlockPos powDown = pos.below();

                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    FluidState fluidState = level.getFluidState(powDown.relative(direction));
                    if (fluidState.is(FluidTags.WATER)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    @Override
    public boolean canSustainPlant(@Nonnull BlockState state, @Nonnull BlockGetter level, BlockPos pos, @Nonnull Direction direction, IPlantable plantable) {
        BlockState plant = plantable.getPlant(level, pos.relative(direction));
        if (plant.getBlock() == AtumBlocks.PAPYRUS.get() && this == AtumBlocks.PAPYRUS.get()) {
            return true;
        }
        return super.canSustainPlant(state, level, pos, direction, plantable);
    }

    @Override
    public void onPlace(BlockState state, Level level, @Nonnull BlockPos pos, @Nonnull BlockState oldState, boolean isMoving) {
        level.setBlockAndUpdate(pos, state.setValue(TOP, level.isEmptyBlock(pos.above())));
        if (level.getBlockState(pos.below()).equals(state.setValue(TOP, true))) { //Correct non-top papyrus, when manually placed
            level.setBlockAndUpdate(pos.below(), state.setValue(TOP, false));
        }
    }

    @Override
    public void onRemove(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        if (level.getBlockState(pos.below()).getBlock() == this) {
            level.setBlockAndUpdate(pos.below(), state.setValue(TOP, level.isEmptyBlock(pos.above()))); //Fix new top, when top gets removed
        }
    }

    @Override
    @Nonnull
    public ItemStack getCloneItemStack(@Nonnull BlockState state, @Nonnull HitResult target, @Nonnull LevelReader level, @Nonnull BlockPos pos, @Nonnull Player player) {
        return new ItemStack(AtumItems.PAPYRUS_PLANT.get());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
        container.add(AGE, TOP);
    }
}
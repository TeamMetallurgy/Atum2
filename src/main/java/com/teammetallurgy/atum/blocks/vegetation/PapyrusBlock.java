package com.teammetallurgy.atum.blocks.vegetation;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nonnull;

public class PapyrusBlock extends SugarCaneBlock {
    private static final BooleanProperty TOP = BooleanProperty.create("top");

    public PapyrusBlock() {
        super(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0.0F).sound(SoundType.PLANT));
        this.setDefaultState(this.stateContainer.getBaseState().with(AGE, 0).with(TOP, false));
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
        BlockState soil = world.getBlockState(pos.down());
        Block block = soil.getBlock();
        if (block.canSustainPlant(soil, world, pos.down(), Direction.UP, this)) return true;

        if (block == this) {
            return true;
        } else {
            if (block != AtumBlocks.FERTILE_SOIL && block != AtumBlocks.FERTILE_SOIL_TILLED && block != AtumBlocks.SAND) {
                BlockPos powDown = pos.down();

                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    IFluidState fluidState = world.getFluidState(powDown.offset(direction));
                    if (fluidState.isTagged(FluidTags.WATER)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    @Override
    public boolean canSustainPlant(@Nonnull BlockState state, @Nonnull IBlockReader world, BlockPos pos, @Nonnull Direction direction, IPlantable plantable) {
        BlockState plant = plantable.getPlant(world, pos.offset(direction));
        if (plant.getBlock() == AtumBlocks.PAPYRUS && this == AtumBlocks.PAPYRUS) {
            return true;
        }
        return super.canSustainPlant(state, world, pos, direction, plantable);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        world.setBlockState(pos, state.with(TOP, world.isAirBlock(pos.up())));
    }

    /*@Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return new ItemStack(AtumItems.PAPYRUS_PLANT); //TODO
    }*/

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
        container.add(AGE, TOP);
    }
}
package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.blocks.base.IUnbreakable;
import com.teammetallurgy.atum.blocks.lighting.INebuTorch;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.SarcophagusTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;

public class QuandaryBlock extends Block implements IUnbreakable {
    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    private static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

    public QuandaryBlock() {
        super(Block.Properties.create(Material.ROCK, MaterialColor.SAND).hardnessAndResistance(1.5F, 8.0F).setRequiresTool().harvestTool(ToolType.PICKAXE).harvestLevel(1));
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(ACTIVATED, false).with(UNBREAKABLE, false));
    }

    @Override
    public float getExplosionResistance(BlockState state, IBlockReader world, BlockPos pos, Explosion explosion) {
        return world.getBlockState(pos).get(UNBREAKABLE) ? 6000000.0F : super.getExplosionResistance(state, world, pos, explosion);
    }

    @Override
    public void neighborChanged(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Block block, @Nonnull BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, world, pos, block, fromPos, isMoving);
        Block facingBlock = world.getBlockState(pos.offset(state.get(FACING))).getBlock();
        world.setBlockState(pos, state.with(ACTIVATED, facingBlock instanceof INebuTorch && ((INebuTorch) facingBlock).isNebuTorch()), 2);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    public boolean canProvidePower(@Nonnull BlockState state) {
        return true;
    }

    @Override
    public int getStrongPower(BlockState blockState, @Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nonnull Direction direction) {
        return blockState.getWeakPower(world, pos, direction);
    }

    @Override
    public int getWeakPower(BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nonnull Direction direction) {
        return !state.get(ACTIVATED) ? 0 : 15;
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    @Nonnull
    public BlockState mirror(@Nonnull BlockState state, Mirror mirror) {
        return state.rotate(mirror.toRotation(state.get(FACING)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
        container.add(FACING, ACTIVATED, UNBREAKABLE);
    }

    public static class Helper {

        public static boolean canSpawnPharaoh(World world, BlockPos pos, Direction facing, PlayerEntity player, SarcophagusTileEntity sarcophagus) {
            Block topLeftCorner = world.getBlockState(pos.offset(facing.rotateY(), 2).offset(facing.getOpposite(), 1)).getBlock();
            Block bottomLeftCorner = world.getBlockState(pos.offset(facing.rotateY(), 2).offset(facing, 2)).getBlock();
            Block topRightCorner = world.getBlockState(pos.offset(facing.rotateYCCW(), 3).offset(facing.getOpposite(), 1)).getBlock();
            Block bottomRightCorner = world.getBlockState(pos.offset(facing.rotateYCCW(), 3).offset(facing, 2)).getBlock();

            if (topLeftCorner instanceof INebuTorch && bottomLeftCorner instanceof INebuTorch && topRightCorner instanceof INebuTorch && bottomRightCorner instanceof INebuTorch) {
                INebuTorch torchTopLeftCorner = (INebuTorch) topLeftCorner;
                INebuTorch torchBottomLeftCorner = (INebuTorch) bottomLeftCorner;
                INebuTorch torchTopRightCorner = (INebuTorch) topRightCorner;
                INebuTorch torchBottomRightCorner = (INebuTorch) bottomRightCorner;

                if (torchTopLeftCorner.isNebuTorch() && torchBottomLeftCorner.isNebuTorch() && torchTopRightCorner.isNebuTorch() && torchBottomRightCorner.isNebuTorch()) {
                    playRewardDing(world, pos);

                    God god = torchTopLeftCorner.getGod();
                    if (god == torchBottomLeftCorner.getGod() && god == torchTopRightCorner.getGod() && god == torchBottomRightCorner.getGod()) {
                        sarcophagus.spawn(player, world.getDifficultyForLocation(pos), god);
                    } else {
                        sarcophagus.spawn(player, world.getDifficultyForLocation(pos), null);
                    }
                    return true;
                }
                return false;
            }
            return false;
        }

        public static void playRewardDing(World world, BlockPos pos) {
            world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_NOTE_BLOCK_CHIME, SoundCategory.BLOCKS, 1.3F, 1.0F);
        }
    }
}
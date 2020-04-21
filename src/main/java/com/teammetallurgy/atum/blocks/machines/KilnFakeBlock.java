package com.teammetallurgy.atum.blocks.machines;

import com.teammetallurgy.atum.blocks.machines.tileentity.KilnBaseTileEntity;
import com.teammetallurgy.atum.blocks.machines.tileentity.KilnTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

public class KilnFakeBlock extends ContainerBlock {
    public static final BooleanProperty UP = BooleanProperty.create("up");

    public KilnFakeBlock() {
        super(Properties.create(Material.ROCK, MaterialColor.SAND).hardnessAndResistance(1.5F, 10.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(0));
        this.setDefaultState(this.stateContainer.getBaseState().with(UP, false));
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull IBlockReader reader) {
        return new KilnTileEntity();
    }

    @Override
    @Nonnull
    public ActionResultType onBlockActivated(@Nonnull BlockState state, World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull BlockRayTraceResult rayTraceResult) {
        if (world.isRemote) {
            return ActionResultType.PASS;
        }
        BlockPos tepos = getPrimaryKilnBlock(world, pos);
        if (tepos != null) {
            INamedContainerProvider container = this.getContainer(world.getBlockState(tepos), world, tepos);
            if (container != null && player instanceof ServerPlayerEntity) {
                NetworkHooks.openGui((ServerPlayerEntity) player, container, tepos);
                return ActionResultType.SUCCESS;
            }
        }
        return super.onBlockActivated(state, world, pos, player, hand, rayTraceResult);
    }

    @Override
    public void onReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) { //TODO Test if work the same as breakBlock
        BlockPos primaryPos = this.getPrimaryKilnBlock(world, pos);
        if (primaryPos != null) {
            BlockState primaryState = world.getBlockState(primaryPos);
            if (primaryState.getBlock() == AtumBlocks.KILN && primaryState.get(KilnBlock.MULTIBLOCK_PRIMARY)) {
                ((KilnBlock) AtumBlocks.KILN).destroyMultiblock(world, primaryPos, primaryState.get(KilnBlock.FACING));
            }
        }
        super.onReplaced(state, world, pos, newState, isMoving);
    }

    private BlockPos getPrimaryKilnBlock(World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof KilnBaseTileEntity) {
            KilnBaseTileEntity tekb = (KilnBaseTileEntity) te;
            return tekb.getPrimaryPos();
        }
        return null;
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return new ItemStack(AtumBlocks.KILN);
    }

    @Override
    @Nonnull
    public BlockRenderType getRenderType(@Nonnull BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
        container.add(UP);
    }
}
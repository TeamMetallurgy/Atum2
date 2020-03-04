package com.teammetallurgy.atum.blocks.machines;

import com.teammetallurgy.atum.blocks.machines.tileentity.KilnBaseTileEntity;
import com.teammetallurgy.atum.blocks.machines.tileentity.KilnTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
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
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult rayTraceResult) {
        if (world.isRemote) {
            return ActionResultType.PASS;
        }
        BlockPos tepos = getPrimaryKilnBlock(world, pos);
        if (tepos != null) {
            TileEntity tileEntity = world.getTileEntity(tepos);
            if (tileEntity instanceof KilnTileEntity) {
                //player.openGui(Atum.instance, 5, world, tepos.getX(), tepos.getY(), tepos.getZ()); //TODO
                return ActionResultType.SUCCESS;
            }
        }
        return super.onBlockActivated(state, world, pos, player, handIn, rayTraceResult);
    }

    @Override
    public void onReplaced(BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) { //TODO Test if work the same as breakBlock
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
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
        container.add(UP);
    }

    /*@Override
    public Property[] getNonRenderingProperties() { //TODO
        return new Property[]{UP};
    }*/
}
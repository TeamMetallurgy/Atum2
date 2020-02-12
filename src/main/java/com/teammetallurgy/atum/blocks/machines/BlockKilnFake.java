package com.teammetallurgy.atum.blocks.machines;

import com.teammetallurgy.atum.blocks.machines.tileentity.KilnBaseTileEntity;
import com.teammetallurgy.atum.blocks.machines.tileentity.KilnTileEntity;
import com.teammetallurgy.atum.blocks.stone.limestone.LimestoneBrickBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockKilnFake extends ContainerBlock {
    public static final BooleanProperty UP = BooleanProperty.create("up");

    public BlockKilnFake() {
        super(Material.ROCK, MaterialColor.SAND);
        this.setHardness(1.5F);
        this.setResistance(10.0F);
        this.setHarvestLevel("pickaxe", 0);
        this.setSoundType(SoundType.STONE);
        this.setDefaultState(this.stateContainer.getBaseState().with(UP, false));
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader reader) {
        return new KilnTileEntity();
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult rayTraceResult) {
        if (world.isRemote) {
            return true;
        }
        BlockPos tepos = getPrimaryKilnBlock(world, pos, state);
        if (tepos != null) {
            TileEntity tileEntity = world.getTileEntity(tepos);
            if (tileEntity instanceof KilnTileEntity) {
                //player.openGui(Atum.instance, 5, world, tepos.getX(), tepos.getY(), tepos.getZ()); //TODO
                return true;
            }
        }
        return super.onBlockActivated(state, world, pos, player, handIn, rayTraceResult);
    }

    @Override
    public void breakBlock(World world, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        BlockPos primaryPos = this.getPrimaryKilnBlock(world, pos, state);
        if (primaryPos != null) {
            BlockState primaryState = world.getBlockState(primaryPos);
            if (primaryState.getBlock() == AtumBlocks.KILN && primaryState.get(BlockKiln.MULTIBLOCK_PRIMARY))
                ((BlockKiln) AtumBlocks.KILN).destroyMultiblock(world, primaryPos, primaryState.get(BlockKiln.FACING));
        }
    }

    private BlockPos getPrimaryKilnBlock(World world, BlockPos pos, BlockState state) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof KilnBaseTileEntity) {
            KilnBaseTileEntity tekb = (KilnBaseTileEntity) te;
            return tekb.getPrimaryPos();
        }
        return null;
    }

    @Override
    @Nonnull
    public Item getItemDropped(BlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(LimestoneBrickBlock.getBrick(LimestoneBrickBlock.BrickType.SMALL));
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

    @Override
    public Property[] getNonRenderingProperties() {
        return new Property[]{UP};
    }
}
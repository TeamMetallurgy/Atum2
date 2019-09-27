package com.teammetallurgy.atum.blocks.machines;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.base.IRenderMapper;
import com.teammetallurgy.atum.blocks.machines.tileentity.KilnTileEntity;
import com.teammetallurgy.atum.blocks.machines.tileentity.KilnBaseTileEntity;
import com.teammetallurgy.atum.blocks.stone.limestone.LimestoneBrickBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.state.Property;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockKilnFake extends ContainerBlock implements IRenderMapper {
    public static final PropertyBool UP = PropertyBool.create("up");

    public BlockKilnFake() {
        super(Material.ROCK, MaterialColor.SAND);
        this.setHardness(1.5F);
        this.setResistance(10.0F);
        this.setHarvestLevel("pickaxe", 0);
        this.setSoundType(SoundType.STONE);
        this.setDefaultState(this.stateContainer.getBaseState().with(UP, false));
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World world, int meta) {
        return new KilnTileEntity();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, BlockState state, PlayerEntity player, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        }
        BlockPos tepos = getPrimaryKilnBlock(world, pos, state);
        if (tepos != null) {
            TileEntity tileEntity = world.getTileEntity(tepos);
            if (tileEntity instanceof KilnTileEntity) {
                player.openGui(Atum.instance, 5, world, tepos.getX(), tepos.getY(), tepos.getZ());
                return true;
            }
        }
        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void breakBlock(World world, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        BlockPos primaryPos = this.getPrimaryKilnBlock(world, pos, state);
        if (primaryPos != null) {
            BlockState primaryState = world.getBlockState(primaryPos);
            if (primaryState.getBlock() == AtumBlocks.KILN && primaryState.getValue(BlockKiln.MULTIBLOCK_PRIMARY))
                ((BlockKiln) AtumBlocks.KILN).destroyMultiblock(world, primaryPos, primaryState.getValue(BlockKiln.FACING));
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
    @Nonnull
    public ItemStack getPickBlock(@Nonnull BlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, PlayerEntity player) {
        return new ItemStack(AtumBlocks.KILN);
    }

    @Override
    @Nonnull
    protected ItemStack getSilkTouchDrop(@Nonnull BlockState state) {
        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState().with(UP, (meta & 0b001) == 1);
    }

    @Override
    public int getMetaFromState(BlockState state) {
        int meta = 0;
        if (state.get(UP)) {
            meta |= 0b001;
        }
        return meta;
    }

    @Override
    @Nonnull
    public EnumBlockRenderType getRenderType(BlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, UP);
    }

    @Override
    public Property[] getNonRenderingProperties() {
        return new Property[]{UP};
    }
}
package com.teammetallurgy.atum.blocks.machines;

import com.teammetallurgy.atum.blocks.base.IRenderMapper;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockKilnFake extends Block implements IRenderMapper {
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool NORTH = PropertyBool.create("north");

    public BlockKilnFake() {
        super(Material.ROCK, MapColor.SAND);
        this.setHardness(1.5F);
        this.setResistance(10.0F);
        this.setSoundType(SoundType.STONE);
        this.setDefaultState(this.blockState.getBaseState().withProperty(UP, false).withProperty(EAST, false).withProperty(NORTH, false));
    }

    @Override
    @Nonnull
    public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
        return new ItemStack(AtumBlocks.KILN);
    }

    @Override
    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        IBlockState state = this.getDefaultState();
        EnumFacing facing = EnumFacing.byHorizontalIndex(meta & 0x11b);
        return state.withProperty(UP, (meta & 0b001) == 1).withProperty(EAST, (meta & 0b010) == 1).withProperty(NORTH, (meta & 0b100) == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;
        if (state.getValue(UP)) {
            meta |= 0b001;
        }
        if (state.getValue(EAST)) {
            meta |= 0b010;
        }
        if (state.getValue(NORTH)) {
            meta |= 0b100;
        }
        return meta;
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, UP, NORTH, EAST);
    }

    @Override
    public IProperty[] getNonRenderingProperties() {
        return new IProperty[]{UP, NORTH, EAST};
    }
}
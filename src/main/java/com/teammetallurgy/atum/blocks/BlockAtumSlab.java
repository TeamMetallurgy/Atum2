package com.teammetallurgy.atum.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockAtumSlab extends Block { //TODO Remove and replace with BlockSlab in 1.13
    public static final PropertyEnum<BlockAtumSlab.Type> TYPE = PropertyEnum.create("type", BlockAtumSlab.Type.class);
    private static final AxisAlignedBB AABB_SLAB_BOTTOM = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    private static final AxisAlignedBB AABB_SLAB_TOP = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);

    public BlockAtumSlab(Material material) {
        this(material, material.getMaterialMapColor());
        this.setHardness(2.0F);
    }

    BlockAtumSlab(Material material, MapColor color) {
        super(material, color);
        this.setDefaultState(this.getDefaultState().withProperty(TYPE, Type.BOTTOM));
        this.useNeighborBrightness = true;
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPE);
    }

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }

    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(TYPE)) {
            case DOUBLE:
                return FULL_BLOCK_AABB;
            case TOP:
                return AABB_SLAB_TOP;
            default:
                return AABB_SLAB_BOTTOM;
        }
    }

    @Override
    public boolean isTopSolid(IBlockState state) {
        return state.getValue(TYPE) == Type.DOUBLE || state.getValue(TYPE) == Type.TOP;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return state.getValue(TYPE) == Type.DOUBLE;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return state.getValue(TYPE) == Type.DOUBLE;
    }

    @Override
    @Nonnull
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        Type type = state.getValue(TYPE);

        if (type == Type.DOUBLE) {
            return BlockFaceShape.SOLID;
        } else if (face == EnumFacing.UP && type == Type.TOP) {
            return BlockFaceShape.SOLID;
        } else {
            return face == EnumFacing.DOWN && type == Type.BOTTOM ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
        }
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        if (ForgeModContainer.disableStairSlabCulling) {
            return super.doesSideBlockRendering(state, world, pos, face);
        }

        if (state.isOpaqueCube()) {
            return true;
        }

        Type type = state.getValue(TYPE);
        return (type == Type.TOP && face == EnumFacing.UP) || (type == Type.BOTTOM && face == EnumFacing.DOWN);
    }

    @Override
    @Nonnull
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState state = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(TYPE, Type.BOTTOM);

        if (state.getValue(TYPE) == Type.DOUBLE) {
            return state.withProperty(TYPE, Type.DOUBLE);
        } else {
            return facing != EnumFacing.DOWN && (facing == EnumFacing.UP || (double) hitY <= 0.5D) ? state : state.withProperty(TYPE, Type.TOP);
        }
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, @Nonnull Random random) {
        return state.getValue(TYPE) == Type.DOUBLE ? 2 : 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, @Nonnull IBlockAccess blockAccess, @Nonnull BlockPos pos, EnumFacing side) {
        if (side != EnumFacing.UP && side != EnumFacing.DOWN && !super.shouldSideBeRendered(state, blockAccess, pos, side)) {
            return false;
        }
        return super.shouldSideBeRendered(state, blockAccess, pos, side);
    }

    @Override
    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        IBlockState state = this.getDefaultState().withProperty(TYPE, Type.byOrdinal(meta & 7));

        if (state.getValue(TYPE) != Type.DOUBLE) {
            state = state.withProperty(TYPE, (meta & 8) == 0 ? Type.BOTTOM : Type.TOP);
        }

        return state;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i = i | state.getValue(TYPE).ordinal();

        if (state.getValue(TYPE) != Type.DOUBLE && state.getValue(TYPE) == Type.TOP) {
            i |= 8;
        }
        return i;
    }

    public enum Type implements IStringSerializable {
        TOP("top"),
        BOTTOM("bottom"),
        DOUBLE("double");

        private static final Type[] LENGTH_LOOKUP = new Type[Type.values().length];
        private final String name;

        Type(String name) {
            this.name = name;
        }

        public static Type byOrdinal(int length) {
            if (length < 0 || length >= LENGTH_LOOKUP.length) {
                length = 0;
            }
            return LENGTH_LOOKUP[length];
        }

        @Override
        public String toString() {
            return this.name;
        }

        @Override
        @Nonnull
        public String getName() {
            return this.name;
        }

        static {
            for (Type type : values()) {
                LENGTH_LOOKUP[type.ordinal()] = type;
            }
        }
    }
}
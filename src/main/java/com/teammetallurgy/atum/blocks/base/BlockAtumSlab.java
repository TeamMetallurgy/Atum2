package com.teammetallurgy.atum.blocks.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeModContainer;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockAtumSlab extends Block { //TODO Remove and replace with BlockSlab in 1.13
    public static final PropertyEnum<BlockAtumSlab.Type> TYPE = PropertyEnum.create("type", BlockAtumSlab.Type.class);
    private static final AxisAlignedBB AABB_SLAB_BOTTOM = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    private static final AxisAlignedBB AABB_SLAB_TOP = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);

    public BlockAtumSlab(Material material) {
        this(material, material.getMaterialMapColor());
    }

    public BlockAtumSlab(Material material, MapColor color) {
        super(material, color);
        this.setHardness(2.0F);
        this.setDefaultState(this.getDefaultState().with(TYPE, Type.BOTTOM));
        this.setLightOpacity(255);
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
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
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
    public boolean isTopSolid(BlockState state) {
        return state.getValue(TYPE) == Type.DOUBLE || state.getValue(TYPE) == Type.TOP;
    }

    @Override
    public boolean isOpaqueCube(BlockState state) {
        return state.getValue(TYPE) == Type.DOUBLE;
    }

    @Override
    public boolean isFullCube(BlockState state) {
        return state.getValue(TYPE) == Type.DOUBLE;
    }

    @Override
    @Nonnull
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face) {
        Type type = state.getValue(TYPE);

        if (type == Type.DOUBLE) {
            return BlockFaceShape.SOLID;
        } else if (face == Direction.UP && type == Type.TOP) {
            return BlockFaceShape.SOLID;
        } else {
            return face == Direction.DOWN && type == Type.BOTTOM ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getPackedLightmapCoords(BlockState state, IBlockAccess source, @Nonnull BlockPos pos) {
        int i = source.getCombinedLight(pos, state.getLightValue(source, pos));

        if (i == 0 && state.getBlock() instanceof BlockAtumSlab) {
            pos = pos.down();
            state = source.getBlockState(pos);
            return source.getCombinedLight(pos, state.getLightValue(source, pos));
        }
        return super.getPackedLightmapCoords(state, source, pos);
    }

    @Override
    public boolean doesSideBlockRendering(BlockState state, IBlockAccess world, BlockPos pos, Direction face) {
        if (ForgeModContainer.disableStairSlabCulling) {
            return super.doesSideBlockRendering(state, world, pos, face);
        }

        if (state.isOpaqueCube()) {
            return true;
        }

        Type type = state.getValue(TYPE);
        return (type == Type.TOP && face == Direction.UP) || (type == Type.BOTTOM && face == Direction.DOWN);
    }

    @Override
    @Nonnull
    public BlockState getStateForPlacement(World world, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer) {
        BlockState state = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer).with(TYPE, Type.BOTTOM);

        if (state.getValue(TYPE) == Type.DOUBLE) {
            return state.with(TYPE, Type.DOUBLE);
        } else {
            return facing != Direction.DOWN && (facing == Direction.UP || (double) hitY <= 0.5D) ? state : state.with(TYPE, Type.TOP);
        }
    }

    @Override
    public int quantityDropped(BlockState state, int fortune, @Nonnull Random random) {
        return state.getValue(TYPE) == Type.DOUBLE ? 2 : 1;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean shouldSideBeRendered(BlockState state, @Nonnull IBlockAccess blockAccess, @Nonnull BlockPos pos, Direction side) {
        if (side != Direction.UP && side != Direction.DOWN && !super.shouldSideBeRendered(state, blockAccess, pos, side)) {
            return false;
        }
        return super.shouldSideBeRendered(state, blockAccess, pos, side);
    }

    @Override
    @Nonnull
    public BlockState getStateFromMeta(int meta) {
        BlockState state = this.getDefaultState().with(TYPE, Type.byOrdinal(meta & 7));

        if (state.getValue(TYPE) != Type.DOUBLE) {
            state = state.with(TYPE, (meta & 8) == 0 ? Type.BOTTOM : Type.TOP);
        }

        return state;
    }

    @Override
    public int getMetaFromState(BlockState state) {
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
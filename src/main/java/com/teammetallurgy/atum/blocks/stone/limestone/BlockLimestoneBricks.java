package com.teammetallurgy.atum.blocks.stone.limestone;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.blocks.base.BlockAtumDoor;
import com.teammetallurgy.atum.blocks.base.IRenderMapper;
import com.teammetallurgy.atum.utils.AtumRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemDoor;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class BlockLimestoneBricks extends Block implements IRenderMapper {
    public static final PropertyBool UNBREAKABLE = PropertyBool.create("unbreakable");
    private static final Map<BrickType, BlockLimestoneBricks> BRICKS = Maps.newEnumMap(BrickType.class);
    private static final Map<BrickType, BlockAtumDoor> DOORS = Maps.newEnumMap(BrickType.class);

    public BlockLimestoneBricks() {
        super(Material.ROCK);
        this.setHardness(1.5F);
        this.setResistance(10.0F);
        this.setSoundType(SoundType.STONE);
        this.setDefaultState(this.blockState.getBaseState().withProperty(UNBREAKABLE, false));
    }

    @Override
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        return state.getValue(UNBREAKABLE) ? -1.0F : super.getBlockHardness(state, world, pos);
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
        return world.getBlockState(pos).getValue(UNBREAKABLE) ? 6000000.0F: super.getExplosionResistance(world, pos, exploder, explosion);
    }

    @Override
    @Nonnull
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return MapColor.SAND;
    }

    public static void registerBricks() {
        for (BrickType type : BrickType.values()) {
            BlockLimestoneBricks brick = new BlockLimestoneBricks();
            BRICKS.put(type, brick);
            AtumRegistry.registerBlock(brick, "limestone_brick_" + type.getName());
        }
    }

    public static BlockLimestoneBricks getBrick(BrickType type) {
        return BRICKS.get(type);
    }

    public static void registerDoors() {
        for (BrickType type : BrickType.values()) {
            BlockAtumDoor door = new BlockAtumDoor(Material.ROCK);
            DOORS.put(type, door);
            AtumRegistry.registerBlock(door, new ItemDoor(door), "limestone_brick_" + type.getName() + "_door");
        }
    }

    public static BlockAtumDoor getDoor(BrickType type) {
        return DOORS.get(type);
    }

    @Override
    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(UNBREAKABLE, meta > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(UNBREAKABLE) ? 1 : 0;
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, UNBREAKABLE);
    }

    @Override
    public IProperty[] getNonRenderingProperties() {
        return new IProperty[]{UNBREAKABLE};
    }

    public enum BrickType implements IStringSerializable {
        SMALL("small"),
        LARGE("large"),
        CRACKED("cracked_brick"),
        CHISELED("chiseled"),
        CARVED("carved");

        private final String name;

        BrickType(String name) {
            this.name = name;
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
    }
}
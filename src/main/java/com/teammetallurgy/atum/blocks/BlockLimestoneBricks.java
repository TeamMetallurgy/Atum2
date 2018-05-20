package com.teammetallurgy.atum.blocks;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.utils.AtumRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemDoor;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;
import java.util.Map;

public class BlockLimestoneBricks extends Block {
    private static final Map<BrickType, BlockLimestoneBricks> BRICKS = Maps.newEnumMap(BrickType.class);
    private static final Map<BrickType, BlockAtumDoor> DOORS = Maps.newEnumMap(BrickType.class);

    public BlockLimestoneBricks() {
        super(Material.ROCK);
        this.setHardness(1.5F);
        this.setResistance(10.0F);
        this.setSoundType(SoundType.STONE);
    }

    public BlockLimestoneBricks setUnbreakable() {
        super.setBlockUnbreakable();
        this.setResistance(20000.0F);
        return this;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    public static void registerBricks() {
        for (BrickType type : BrickType.values()) {
            BlockLimestoneBricks brick = new BlockLimestoneBricks().setUnbreakable();
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

    public enum BrickType implements IStringSerializable {
        SMALL("small"),
        LARGE("large"),
        CRACKED("cracked_brick"),
        CHISELED("chiseled");

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
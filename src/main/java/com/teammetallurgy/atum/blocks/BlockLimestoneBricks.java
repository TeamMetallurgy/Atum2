package com.teammetallurgy.atum.blocks;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.utils.AtumRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;
import java.util.Map;

public class BlockLimestoneBricks extends Block {
    private static final Map<BrickType, Block> BRICKS = Maps.newEnumMap(BrickType.class);

    public BlockLimestoneBricks() {
        super(Material.ROCK);
    }

    public Block setUnbreakable() {
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
            Block brick = new BlockLimestoneBricks();
            BRICKS.put(type, brick);
            AtumRegistry.registerBlock(new BlockLimestoneBricks(), "limestone_brick_" + type.getName());
        }
    }

    public static Block getBrick(BrickType type) {
        return BRICKS.get(type);
    }

    public enum BrickType implements IStringSerializable {
        SMALL("small"),
        LARGE("large"),
        CRACKED("cracked_brick"),
        CHISELED("chiseled");

        private static final BrickType[] LENGTH_LOOKUP = new BrickType[BrickType.values().length];
        private final String name;

        BrickType(String name) {
            this.name = name;
        }

        public static BrickType byOrdinal(int length) {
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
            for (BrickType type : values()) {
                LENGTH_LOOKUP[type.ordinal()] = type;
            }
        }
    }
}
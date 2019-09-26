package com.teammetallurgy.atum.blocks.wood;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.utils.AtumRegistry;
import com.teammetallurgy.atum.utils.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;

public class BlockAtumPlank extends Block {
    private static final Map<WoodType, Block> PLANKS = Maps.newEnumMap(WoodType.class);
    private static final Map<WoodType, Item> STICKS = Maps.newEnumMap(WoodType.class);

    private BlockAtumPlank() {
        super(Material.WOOD);
        this.setHardness(2.0F);
        this.setResistance(5.0F);
        this.setSoundType(SoundType.WOOD);
    }

    public static void registerPlanks() {
        for (WoodType type : WoodType.values()) {
            Block plank = new BlockAtumPlank();
            PLANKS.put(type, plank);
            AtumRegistry.registerBlock(plank, type.getName() + "_planks");
        }
    }

    public static Block getPlank(WoodType type) {
        return PLANKS.get(type);
    }

    @Override
    @Nonnull
    public MaterialColor getMapColor(BlockState state, IBlockReader blockAccess, BlockPos blockPos) {
        return WoodType.byIndex(WoodType.values().length).getMapColor();
    }

    @Override
    public int getMetaFromState(BlockState state) {
        return 0;
    }

    @Override
    public void getOreDictEntries() {
        OreDictHelper.add(this, "plank", Objects.requireNonNull(this.getRegistryName()).getPath().replace("_planks", ""));
        OreDictHelper.add(this, "plankWood");
    }

    public enum WoodType implements IStringSerializable {
        PALM("palm", MaterialColor.WOOD),
        DEADWOOD("deadwood", MaterialColor.WOOD);

        private static final BlockAtumPlank.WoodType[] ORDINAL_LOOKUP = new BlockAtumPlank.WoodType[values().length];
        private final String unlocalizedName;
        private final MaterialColor mapColor;

        WoodType(String unlocalizedName, MaterialColor mapColor) {
            this.unlocalizedName = unlocalizedName;
            this.mapColor = mapColor;
        }

        public MaterialColor getMapColor() {
            return this.mapColor;
        }

        @Override
        public String toString() {
            return this.unlocalizedName;
        }

        @Override
        @Nonnull
        public String getName() {
            return this.unlocalizedName;
        }

        public static BlockAtumPlank.WoodType byIndex(int length) {
            if (length < 0 || length >= ORDINAL_LOOKUP.length) {
                length = 0;
            }
            return ORDINAL_LOOKUP[length];
        }

        static {
            for (BlockAtumPlank.WoodType type : values()) {
                ORDINAL_LOOKUP[type.ordinal()] = type;
            }
        }
    }
}
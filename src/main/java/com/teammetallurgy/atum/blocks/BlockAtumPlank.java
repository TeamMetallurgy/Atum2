package com.teammetallurgy.atum.blocks;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.utils.AtumRegistry;
import com.teammetallurgy.atum.utils.IOreDictEntry;
import com.teammetallurgy.atum.utils.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;

public class BlockAtumPlank extends Block implements IOreDictEntry {
    private static final Map<WoodType, Block> PLANKS = Maps.newEnumMap(WoodType.class);
    private static final Map<WoodType, Item> STICKS = Maps.newEnumMap(WoodType.class);

    public BlockAtumPlank() {
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

    public static void registerSticks() {
        for (BlockAtumPlank.WoodType type : BlockAtumPlank.WoodType.values()) {
            Item stick = new Item();
            STICKS.put(type, stick);
            AtumRegistry.registerItem(stick, type.getName() + "Stick");
            OreDictHelper.add(stick, "stickWood");
        }
    }

    public static Item getStick(WoodType type) {
        return STICKS.get(type);
    }

    @Override
    @Nonnull
    public MapColor getMapColor(IBlockState state, IBlockAccess blockAccess, BlockPos blockPos) {
        return WoodType.byIndex(WoodType.values().length).getMapColor();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public void getOreDictEntries() {
        OreDictHelper.add(this, "plank", Objects.requireNonNull(this.getRegistryName()).getResourcePath().replace("_planks", ""));
        OreDictHelper.add(this, "plankWood");
    }

    public enum WoodType implements IStringSerializable {
        PALM("palm", MapColor.WOOD),
        DEADWOOD("deadwood", MapColor.OBSIDIAN);

        private static final BlockAtumPlank.WoodType[] ORDINAL_LOOKUP = new BlockAtumPlank.WoodType[values().length];
        private final String unlocalizedName;
        private final MapColor mapColor;

        WoodType(String unlocalizedName, MapColor mapColor) {
            this.unlocalizedName = unlocalizedName;
            this.mapColor = mapColor;
        }

        public MapColor getMapColor() {
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
package com.teammetallurgy.atum.utils;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.text.WordUtils;

public class OreDictHelper {
    public static NonNullList<IOreDictEntry> entries = NonNullList.create();

    public static void add(Item item, String prefix, String name) {
        add(prefix + WordUtils.capitalizeFully(name, '_'), new ItemStack(item));
    }

    public static void add(Block block, String prefix, String name) {
        add(prefix + WordUtils.capitalizeFully(name, '_'), new ItemStack(block));
    }

    public static void add(Item item, String name) {
        add(name, new ItemStack(item));
    }

    public static void add(Block block, String name) {
        add(name, new ItemStack(block));
    }

    public static void add(String name, ItemStack... stacks) {
        for (ItemStack stack : stacks) {
            OreDictionary.registerOre(AtumUtils.toCamelCase(name), stack);
        }
    }

    public static void add(ItemStack stack, String... names) {
        for (String name : names) {
            OreDictionary.registerOre(AtumUtils.toCamelCase(name), stack);
        }
    }

    public static void register() {
        for (IOreDictEntry entry : entries) {
            entry.getOreDictEntries();
        }
    }
}
package com.teammetallurgy.atum.items;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;
import java.util.*;

public class AtumWeightedLootSet {
    public Map<Integer, ItemStack> loot;
    public Map<Integer, Integer> lootMin;
    public Map<Integer, Integer> lootMax;
    public int totalWeight;

    public AtumWeightedLootSet() {
        loot = new HashMap<>();
        lootMin = new HashMap<>();
        lootMax = new HashMap<>();
        totalWeight = 0;
    }

    public void addLoot(@Nonnull ItemStack stack, int weight, int min, int max) {
        if (weight <= 0 || stack.isEmpty())
            return;

        loot.put(totalWeight + weight, stack);
        lootMin.put(totalWeight + weight, min);
        lootMax.put(totalWeight + weight, max);
        totalWeight += weight;
    }

    @Nonnull
    public ItemStack getRandomLoot() {
        Random rand = new Random();
        int weight = rand.nextInt(totalWeight);

        ItemStack stack = ItemStack.EMPTY;

        Set<Integer> keySet = loot.keySet();
        Integer[] keys = keySet.toArray(new Integer[keySet.size()]);
        Arrays.sort(keys);

        for (Integer key : keys) {
            if (key >= weight) {
                stack = loot.get(key).copy();
                int min = lootMin.get(key);
                int max = lootMax.get(key);
                int amount = rand.nextInt(max - min + 1) + min;
                stack.setCount(amount);
                if (stack.getItem() == Items.ENCHANTED_BOOK) {
                    Enchantment enchantment = Enchantment.REGISTRY.getRandomObject(rand);
                    int l = MathHelper.getInt(rand, enchantment.getMinLevel(), enchantment.getMaxLevel());
                    ItemEnchantedBook.addEnchantment(stack, new EnchantmentData(enchantment, l));
                }
                break;
            }
        }
        return stack;
    }
}
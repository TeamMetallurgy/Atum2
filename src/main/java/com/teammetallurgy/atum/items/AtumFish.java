package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class AtumFish {
    private static AtumWeightedLootSet fishLoot;

    static {
        fishLoot = new AtumWeightedLootSet();

        AtumFish.addFish(new ItemStack(Items.FISH, 1, 0).getItem(), 100); //Cod
        AtumFish.addFish(AtumItems.FORSAKEN_FISH, 30);
        AtumFish.addFish(AtumItems.MUMMIFIED_FISH, 5);
        AtumFish.addFish(AtumItems.JEWELED_FISH, 50);
        AtumFish.addFish(AtumItems.SKELETAL, 50);
    }

    public static void addFish(@Nonnull Item fish, int weight) {
        fishLoot.addLoot(new ItemStack(fish), weight, 1, 1);
    }

    @Nonnull
    public static ItemStack getRandomFish() {
        return fishLoot.getRandomLoot();
    }
}
package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import java.util.Random;

public class AtumLoot {
    public static NonNullList<ItemStack> artifacts = NonNullList.create();
    public static AtumWeightedLootSet goodLoot;
    public static AtumWeightedLootSet junkLoot;

    public static void addArtifact(@Nonnull ItemStack stack) {
        artifacts.add(stack);
    }

    @Nonnull
    public static ItemStack getRandomLoot() {
        return artifacts.get(new Random().ints(0, artifacts.size()).findAny().getAsInt());
    }

    @Nonnull
    public static ItemStack getRandomArtifact() {

        int i = (new Random()).nextInt(artifacts.size());
        return artifacts.get(i).copy();
    }

    public static void fillChest(IInventory inventory, int multiplier, float quality) {
        if (inventory == null) {
            return;
        }

        Random rand = new Random();
        for (int i = 0; i < multiplier; i++) {
            int slot = rand.nextInt(inventory.getSizeInventory());
            float roll = rand.nextFloat();
            ItemStack stack;
            if (rand.nextFloat() < quality) {
                if (roll > 0.20) {
                    stack = goodLoot.getRandomLoot();
                } else if (roll > 0.005) {
                    stack = ItemLoot.getRandomLoot(rand, true);
                } else {
                    int randomArtifactID = rand.nextInt(artifacts.size());
                    stack = artifacts.get(randomArtifactID).copy();
                }
            } else {
                stack = junkLoot.getRandomLoot();
            }
            inventory.setInventorySlotContents(slot, stack);
        }
    }

    public static void register() {
        goodLoot = new AtumWeightedLootSet();
        junkLoot = new AtumWeightedLootSet();

        ItemStack stack = new ItemStack(AtumItems.PTAHS_DECADENCE);
        artifacts.add(stack);

        artifacts.add(new ItemStack(AtumItems.SOBEKS_RAGE));
        artifacts.add(new ItemStack(AtumItems.OSIRIS_WILL));
        artifacts.add(new ItemStack(AtumItems.AKERS_TOIL));
        artifacts.add(new ItemStack(AtumItems.GEBS_BLESSING));
        artifacts.add(new ItemStack(AtumItems.ATENS_FURY));
        artifacts.add(new ItemStack(AtumItems.RAS_GLORY));
        artifacts.add(new ItemStack(AtumItems.SEKHMETS_WRATH));
        artifacts.add(new ItemStack(AtumItems.NUTS_AGILITY));
        artifacts.add(new ItemStack(AtumItems.HORUS_FLIGHT));
        artifacts.add(new ItemStack(AtumItems.MONTHUS_STRIKE));
        artifacts.add(new ItemStack(AtumItems.NEITHS_AUDACITY));
        artifacts.add(new ItemStack(AtumItems.HEDETETS_STING));
        artifacts.add(new ItemStack(AtumItems.NUS_FLUX));
        artifacts.add(new ItemStack(AtumItems.ANHURS_MIGHT));
        artifacts.add(new ItemStack(AtumItems.HORUS_SOARING));
        artifacts.add(new ItemStack(AtumItems.SHUS_BREATH));
        artifacts.add(new ItemStack(AtumItems.HEDETETS_VENOM));
        artifacts.add(new ItemStack(AtumItems.MONTHUS_BLAST));
        artifacts.add(new ItemStack(AtumItems.MNEVIS_HORNS));
        artifacts.add(new ItemStack(AtumItems.ISIS_EMBRACE));
        artifacts.add(new ItemStack(AtumItems.MAATS_BALANCE));
        artifacts.add(new ItemStack(AtumItems.NUTS_CALL));
        artifacts.add(new ItemStack(AtumItems.PTAHS_DESTRUCTION));
        artifacts.add(new ItemStack(AtumItems.ANUKETS_BOUNTY));
        artifacts.add(new ItemStack(AtumItems.ANUBIS_MERCY));
        artifacts.add(new ItemStack(AtumItems.AMUNETS_HOMECOMING));
        artifacts.add(new ItemStack(AtumItems.ISIS_HEALING));
        artifacts.add(new ItemStack(AtumItems.MAFDETS_QUICKNESS));

        // Junk Loot Stuff
        junkLoot.addLoot(new ItemStack(AtumItems.FLAX_SEED), 5, 1, 2);
        junkLoot.addLoot(new ItemStack(Items.STICK), 5, 1, 5);
        junkLoot.addLoot(new ItemStack(AtumItems.DATE), 5, 1, 2);
        junkLoot.addLoot(new ItemStack(Items.BONE), 10, 1, 3);
        junkLoot.addLoot(new ItemStack(AtumBlocks.SAND), 12, 1, 64);
        junkLoot.addLoot(new ItemStack(AtumItems.SCIMITAR), 5, 1, 1);
        junkLoot.addLoot(new ItemStack(Items.LEATHER), 5, 1, 5);

        // Good Loot
        goodLoot.addLoot(new ItemStack(Items.IRON_INGOT), 5, 1, 3);
        goodLoot.addLoot(new ItemStack(Items.GOLD_INGOT), 4, 1, 3);
        goodLoot.addLoot(new ItemStack(Items.DIAMOND), 1, 1, 2);
        goodLoot.addLoot(new ItemStack(Items.ENCHANTED_BOOK, 1, 1), 5, 1, 1);
        ItemStack stick = new ItemStack(Items.STICK);
        stick.setStackDisplayName("Amazing Stick");
        goodLoot.addLoot(stick, 1, 1, 1);
    }
}
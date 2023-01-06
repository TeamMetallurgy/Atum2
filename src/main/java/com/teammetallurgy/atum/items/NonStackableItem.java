package com.teammetallurgy.atum.items;

import net.minecraft.world.item.Item;

public class NonStackableItem extends Item {

    public NonStackableItem() {
        super(new Item.Properties().stacksTo(1));
    }
}
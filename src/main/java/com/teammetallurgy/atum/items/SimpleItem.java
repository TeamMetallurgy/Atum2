package com.teammetallurgy.atum.items;

import net.minecraft.world.item.Item;

public class SimpleItem extends Item {

    public SimpleItem(Item.Properties properties) {
        super(properties);
    }

    public SimpleItem() {
        this(new Item.Properties());
    }
}
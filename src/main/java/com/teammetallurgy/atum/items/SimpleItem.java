package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.Atum;
import net.minecraft.world.item.Item;

public class SimpleItem extends Item {

    public SimpleItem() {
        super(new Item.Properties().tab(Atum.GROUP));
    }
}
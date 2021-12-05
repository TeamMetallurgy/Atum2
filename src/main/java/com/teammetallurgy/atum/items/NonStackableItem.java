package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.Atum;
import net.minecraft.world.item.Item;

public class NonStackableItem extends Item {

    public NonStackableItem() {
        super(new Item.Properties().stacksTo(1).tab(Atum.GROUP));
    }
}
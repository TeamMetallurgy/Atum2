package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.Atum;
import net.minecraft.world.item.Item;

public class ScarabItem extends Item {

    public ScarabItem() {
        super(new Item.Properties().stacksTo(1).tab(Atum.GROUP));
    }
}
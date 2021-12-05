package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.Atum;
import net.minecraft.item.Item;

public class ScarabItem extends Item {

    public ScarabItem() {
        super(new Item.Properties().maxStackSize(1).group(Atum.GROUP));
    }
}
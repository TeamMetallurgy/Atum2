package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.Atum;
import net.minecraft.item.Item;

public class NonStackableItem extends Item {

    public NonStackableItem() {
        super(new Item.Properties().maxStackSize(1).group(Atum.GROUP));
    }
}
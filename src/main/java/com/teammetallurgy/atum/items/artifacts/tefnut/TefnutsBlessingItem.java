package com.teammetallurgy.atum.items.artifacts.tefnut;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.AtumMats;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;

public class TefnutsBlessingItem extends HoeItem {

    public TefnutsBlessingItem() {
        super(AtumMats.NEBU, -3, 0.0F, new Item.Properties().rarity(Rarity.RARE).group(Atum.GROUP));
    }
}
package com.teammetallurgy.atum.items.artifacts.tefnut;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.AtumMats;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;

public class TefnutsBlessingItem extends HoeItem implements IArtifact {

    public TefnutsBlessingItem() {
        super(AtumMats.NEBU, -3, 0.0F, new Item.Properties().rarity(Rarity.RARE).group(Atum.GROUP));
    }

    @Override
    public God getGod() {
        return God.TEFNUT;
    }
}
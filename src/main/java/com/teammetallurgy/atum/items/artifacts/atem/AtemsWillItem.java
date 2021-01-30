package com.teammetallurgy.atum.items.artifacts.atem;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.AtumMats;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.item.SwordItem;

public class AtemsWillItem extends SwordItem implements IArtifact {

    public AtemsWillItem() {
        super(AtumMats.NEBU, 3, -2.4F, new Item.Properties().rarity(Rarity.RARE).group(Atum.GROUP));
    }

    @Override
    public God getGod() {
        return God.ATEM;
    }
}
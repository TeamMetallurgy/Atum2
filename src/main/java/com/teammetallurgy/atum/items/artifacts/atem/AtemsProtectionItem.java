package com.teammetallurgy.atum.items.artifacts.atem;

import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.tools.AtumShieldItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;

public class AtemsProtectionItem extends AtumShieldItem implements IArtifact {

    public AtemsProtectionItem() {
        super(500, new Item.Properties().rarity(Rarity.RARE));
        this.setRepairItem(AtumItems.NEBU_INGOT);
    }

    @Override
    public God getGod() {
        return God.ATEM;
    }
}
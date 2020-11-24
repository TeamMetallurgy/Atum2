package com.teammetallurgy.atum.items.artifacts.tefnut;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.AtumMats;
import net.minecraft.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class TefnutsBlessingItem extends HoeItem {

    public TefnutsBlessingItem() {
        super(AtumMats.NEBU, -3, 0.0F, new Item.Properties().rarity(Rarity.RARE).group(Atum.GROUP));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }
}
package com.teammetallurgy.atum.items.artifacts;

import com.teammetallurgy.atum.Atum;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nonnull;

public class RingItem extends Item implements ICurioItem {

    public RingItem(Properties properties) {
        super(properties.rarity(Rarity.RARE).tab(Atum.GROUP));
    }

    public RingItem() {
        this(new Item.Properties());
    }

    @Override
    public boolean isEnchantable(@Nonnull ItemStack stack) {
        return false;
    }

    @Override
    public boolean canRightClickEquip(@Nonnull ItemStack stack) {
        return true;
    }
}
package com.teammetallurgy.atum.items.artifacts;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nonnull;

public class RingItem extends Item implements ICurioItem {

    public RingItem(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.RARE));
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
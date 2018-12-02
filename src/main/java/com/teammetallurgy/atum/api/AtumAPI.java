package com.teammetallurgy.atum.api;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

public class AtumAPI {
    public static final ItemArmor.ArmorMaterial MUMMY_ARMOR_MATERIAL = EnumHelper.addArmorMaterial("MUMMY", "mummy", 5, new int[]{1, 2, 2, 1}, 12, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
    public static final ItemArmor.ArmorMaterial WANDERER_ARMOR_MATERIAL = EnumHelper.addArmorMaterial("WANDERER", "wanderer", 10, new int[]{1, 2, 3, 1}, 14, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);

}
package com.teammetallurgy.atum.api;

import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

import java.util.Objects;

public class AtumAPI {
    public static final ItemArmor.ArmorMaterial MUMMY_ARMOR_MATERIAL = EnumHelper.addArmorMaterial("MUMMY", "mummy", 5, new int[]{1, 2, 2, 1}, 12, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
    public static final ItemArmor.ArmorMaterial WANDERER_ARMOR_MATERIAL = EnumHelper.addArmorMaterial("WANDERER", "wanderer", 10, new int[]{1, 2, 3, 1}, 14, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
    public static final Item.ToolMaterial KHNUMITE = Objects.requireNonNull(EnumHelper.addToolMaterial("khnumite", 1, 160, 3.6F, 1.1F, 10)).setRepairItem(new ItemStack(AtumItems.KHNUMITE));
}
package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.utils.IOreDictEntry;
import com.teammetallurgy.atum.utils.OreDictHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import javax.annotation.Nonnull;

public class DyeItem extends Item implements IOreDictEntry {

    @Override
    public boolean itemInteractionForEntity(@Nonnull ItemStack stack, EntityPlayer player, LivingEntity target, EnumHand hand) {
        if (target instanceof EntitySheep) {
            EntitySheep sheep = (EntitySheep) target;
            EnumDyeColor color = EnumDyeColor.BLACK;
            if (this == AtumItems.DYE_BROWN) {
                color = EnumDyeColor.BROWN;
            }
            if (!sheep.getSheared() && sheep.getFleeceColor() != color) {
                sheep.setFleeceColor(color);
                stack.shrink(1);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void getOreDictEntries() {
        OreDictHelper.add(this, "dye");
    }
}
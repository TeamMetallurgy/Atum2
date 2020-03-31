package com.teammetallurgy.atum.items.artifacts.atum;

import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.TexturedArmorItem;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class FeetOfAtumItem extends TexturedArmorItem {

    public FeetOfAtumItem() {
        super(ArmorMaterial.DIAMOND, "atum_armor", EquipmentSlotType.FEET, new Item.Properties().rarity(Rarity.RARE));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @SubscribeEvent
    public static void onKnockback(LivingKnockBackEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (entity.getItemStackFromSlot(EquipmentSlotType.FEET).getItem() == AtumItems.FEET_OF_ATUM && entity.onGround) {
            event.setStrength(0);
        }
    }
}
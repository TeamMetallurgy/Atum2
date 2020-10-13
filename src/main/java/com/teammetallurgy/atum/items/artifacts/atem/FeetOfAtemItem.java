package com.teammetallurgy.atum.items.artifacts.atem;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.TexturedArmorItem;
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

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class FeetOfAtemItem extends TexturedArmorItem {

    public FeetOfAtemItem() {
        super(ArmorMaterial.DIAMOND, "atem_armor", EquipmentSlotType.FEET, new Item.Properties().rarity(Rarity.RARE));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @SubscribeEvent
    public static void onKnockback(LivingKnockBackEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (entity.getItemStackFromSlot(EquipmentSlotType.FEET).getItem() == AtumItems.FEET_OF_ATEM && entity.isOnGround()) {
            event.setStrength(0);
        }
    }
}
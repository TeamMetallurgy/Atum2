package com.teammetallurgy.atum.items.artifacts.atem;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class FeetOfAtemItem extends AtemArmor {

    public FeetOfAtemItem() {
        super("atem_armor", EquipmentSlotType.FEET);
    }

    @SubscribeEvent
    public static void onKnockback(LivingKnockBackEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (entity.getItemStackFromSlot(EquipmentSlotType.FEET).getItem() == AtumItems.FEET_OF_ATEM && entity.isOnGround()) {
            event.setStrength(0);
        }
    }
}
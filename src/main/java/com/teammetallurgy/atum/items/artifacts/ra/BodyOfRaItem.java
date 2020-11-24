package com.teammetallurgy.atum.items.artifacts.ra;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class BodyOfRaItem extends RaArmor {

    public BodyOfRaItem() {
        super("ra_armor", EquipmentSlotType.CHEST);
    }

    @SubscribeEvent
    public static void onDamage(LivingDamageEvent event) {
        if (event.getEntityLiving().getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() == AtumItems.BODY_OF_RA && event.getSource().isFireDamage()) {
            event.setAmount(0.0F);
        }
    }
}
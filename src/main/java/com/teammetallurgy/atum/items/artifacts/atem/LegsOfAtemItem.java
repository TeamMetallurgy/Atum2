package com.teammetallurgy.atum.items.artifacts.atem;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class LegsOfAtemItem extends AtemArmor {
    private static final AttributeModifier SPEED_BOOST = new AttributeModifier(UUID.fromString("2aa9e06c-cc77-4c0a-b832-58d8aaef1500"), "Legs of Atem speed boost", 0.02D, AttributeModifier.Operation.ADDITION);

    public LegsOfAtemItem() {
        super("atem_armor", EquipmentSlotType.LEGS);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        ModifiableAttributeInstance attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (player.isAlive() && player.getItemStackFromSlot(EquipmentSlotType.LEGS).getItem() == AtumItems.LEGS_OF_ATEM) {
            if (!attribute.hasModifier(SPEED_BOOST)) {
                attribute.applyNonPersistentModifier(SPEED_BOOST);
            }
        } else if (attribute.hasModifier(SPEED_BOOST)) {
            attribute.removeModifier(SPEED_BOOST);
        }
    }
}
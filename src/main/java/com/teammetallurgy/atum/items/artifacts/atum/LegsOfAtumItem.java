package com.teammetallurgy.atum.items.artifacts.atum;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.TexturedArmorItem;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class LegsOfAtumItem extends TexturedArmorItem {
    private static final AttributeModifier SPEED_BOOST = new AttributeModifier(UUID.fromString("2aa9e06c-cc77-4c0a-b832-58d8aaef1500"), "Legs of Atum speed boost", 0.02D, AttributeModifier.Operation.ADDITION);

    public LegsOfAtumItem() {
        super(ArmorMaterial.DIAMOND, "atum_armor", EquipmentSlotType.LEGS, new Item.Properties().rarity(Rarity.RARE));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        ModifiableAttributeInstance attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (player.isAlive() && player.getItemStackFromSlot(EquipmentSlotType.LEGS).getItem() == AtumItems.LEGS_OF_ATUM) {
            if (!attribute.hasModifier(SPEED_BOOST)) {
                attribute.applyNonPersistentModifier(SPEED_BOOST);
            }
        } else if (attribute.hasModifier(SPEED_BOOST)) {
            attribute.removeModifier(SPEED_BOOST);
        }
    }
}
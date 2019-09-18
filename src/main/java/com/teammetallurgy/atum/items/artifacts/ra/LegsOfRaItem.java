package com.teammetallurgy.atum.items.artifacts.ra;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.TexturedArmorItem;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.annotation.Nonnull;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class LegsOfRaItem extends TexturedArmorItem {
    private static final AttributeModifier SPEED_BOOST = new AttributeModifier(UUID.fromString("2140f663-2112-497b-a5d7-36c40abb7a76"), "Legs of Ra speed boost", 0.02D, 0);

    public LegsOfRaItem() {
        super(ArmorMaterial.DIAMOND, "ra_armor_2", EquipmentSlotType.LEGS);
        this.setRepairItem(Items.DIAMOND);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    @Nonnull
    public EnumRarity getRarity(@Nonnull ItemStack stack) {
        return EnumRarity.RARE;
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        World world = player.world;
        ModifiableAttributeInstance attribute = (ModifiableAttributeInstance) player.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        if (player.getItemStackFromSlot(EquipmentSlotType.LEGS).getItem() == AtumItems.LEGS_OF_RA) {
            if (player.isSprinting()) {
                Atum.proxy.spawnParticle(AtumParticles.Types.RA_FIRE, player, player.posX + (world.rand.nextDouble() - 0.5D) * (double) player.getWidth(), player.posY + 0.15D, player.posZ + (world.rand.nextDouble() - 0.5D) * (double) player.getWidth(), 0.0D, 0.0D, 0.0D);
                if (!attribute.hasModifier(SPEED_BOOST)) {
                    attribute.applyModifier(SPEED_BOOST);
                } else {
                    attribute.removeModifier(SPEED_BOOST);
                }
            }
        } else if (attribute.hasModifier(SPEED_BOOST)) {
            attribute.removeModifier(SPEED_BOOST); //Fail safe, in case speed boost does somehow not get removed properly
        }
    }
}
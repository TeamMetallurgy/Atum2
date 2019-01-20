package com.teammetallurgy.atum.items.artifacts.ra;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.ItemTexturedArmor;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class ItemLegsOfRa extends ItemTexturedArmor {
    private static final AttributeModifier SPEED_BOOST = new AttributeModifier(UUID.fromString("2140f663-2112-497b-a5d7-36c40abb7a76"), "Legs of Ra speed boost", 0.02D, 0);

    public ItemLegsOfRa() {
        super(ArmorMaterial.DIAMOND, "ra_armor_2", EntityEquipmentSlot.LEGS);
        this.setRepairItem(Items.DIAMOND);
    }

    @Override
    @SideOnly(Side.CLIENT)
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
        EntityPlayer player = event.player;
        World world = player.world;
        ModifiableAttributeInstance attribute = (ModifiableAttributeInstance) player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        if (player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() == AtumItems.LEGS_OF_RA) {
            if (player.isSprinting()) {
                Atum.proxy.spawnParticle(AtumParticles.Types.RA_FIRE, player, player.posX + (world.rand.nextDouble() - 0.5D) * (double) player.width, player.posY + 0.15D, player.posZ + (world.rand.nextDouble() - 0.5D) * (double) player.width, 0.0D, 0.0D, 0.0D);
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

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag tooltipType) {
        if (Keyboard.isKeyDown(42)) {
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getTranslationKey() + ".line1"));
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getTranslationKey() + ".line2"));
        } else {
            tooltip.add(I18n.format(this.getTranslationKey() + ".line3") + " " + TextFormatting.DARK_GRAY + "[SHIFT]");
        }
    }
}
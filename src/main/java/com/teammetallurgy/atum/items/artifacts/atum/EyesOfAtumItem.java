package com.teammetallurgy.atum.items.artifacts.atum;

import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.TexturedArmorItem;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class EyesOfAtumItem extends TexturedArmorItem {
    private static EffectInstance savedNightVision;

    public EyesOfAtumItem() {
        super(ArmorMaterial.DIAMOND, "atum_armor", EquipmentSlotType.HEAD, new Item.Properties().rarity(Rarity.RARE));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void render(DrawScreenEvent.Pre event) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player != null && player.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == AtumItems.EYES_OF_ATUM) {
            if (savedNightVision != null && savedNightVision.getDuration() == 0) {
                savedNightVision = null;
            }
            player.removePotionEffect(Effects.NIGHT_VISION);
            if (savedNightVision != null) {
                player.addPotionEffect(savedNightVision);
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void nightVision(TickEvent.RenderTickEvent event) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player != null && player.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == AtumItems.EYES_OF_ATUM) {
            if (event.phase == TickEvent.Phase.START) {
                EffectInstance temp = player.getActivePotionEffect(Effects.NIGHT_VISION);
                if (temp != null && !temp.isCurativeItem(new ItemStack(AtumItems.EYES_OF_ATUM))) {
                    savedNightVision = temp;
                }
                player.removePotionEffect(Effects.NIGHT_VISION);
                EffectInstance eyes = new EffectInstance(Effects.NIGHT_VISION, 1200, 0, false, false);
                eyes.addCurativeItem(new ItemStack(AtumItems.EYES_OF_ATUM));
                player.addPotionEffect(eyes);
            }
        }
    }
}
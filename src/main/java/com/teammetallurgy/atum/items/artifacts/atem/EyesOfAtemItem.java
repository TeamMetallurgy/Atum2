package com.teammetallurgy.atum.items.artifacts.atem;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, value = Dist.CLIENT)
public class EyesOfAtemItem extends AtemArmor {
    private static EffectInstance savedNightVision;

    public EyesOfAtemItem() {
        super("atem_armor", EquipmentSlotType.HEAD);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void render(DrawScreenEvent.Pre event) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player != null && player.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == AtumItems.EYES_OF_ATEM) {
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
        if (player != null && player.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == AtumItems.EYES_OF_ATEM) {
            if (event.phase == TickEvent.Phase.START) {
                EffectInstance temp = player.getActivePotionEffect(Effects.NIGHT_VISION);
                if (temp != null && !temp.isCurativeItem(new ItemStack(AtumItems.EYES_OF_ATEM))) {
                    savedNightVision = temp;
                }
                player.removePotionEffect(Effects.NIGHT_VISION);
                EffectInstance eyes = new EffectInstance(Effects.NIGHT_VISION, 1200, 0, false, false);
                eyes.addCurativeItem(new ItemStack(AtumItems.EYES_OF_ATEM));
                player.addPotionEffect(eyes);
            }
        }
    }
}
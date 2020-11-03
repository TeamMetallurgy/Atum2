package com.teammetallurgy.atum.misc.event;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.AtumAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.Item;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, value = Dist.CLIENT)
public class TooltipEvent {

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        if (!event.getItemStack().isEmpty()) {
            Item item = event.getItemStack().getItem();
            if (!item.getTags().isEmpty() && item.isIn(AtumAPI.Tags.TOOLTIP)) {
                if (item.getRegistryName() != null) {
                    String itemIdentifier = item.getRegistryName().getPath() + ".tooltip";
                    if (InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
                        event.getToolTip().add(new TranslationTextComponent(Atum.MOD_ID + "." + itemIdentifier + ".line1").mergeStyle(TextFormatting.DARK_PURPLE));
                        event.getToolTip().add(new TranslationTextComponent(Atum.MOD_ID + "." + itemIdentifier + ".line2").mergeStyle(TextFormatting.DARK_PURPLE));
                    } else {
                        event.getToolTip().add(new TranslationTextComponent(Atum.MOD_ID + "." + itemIdentifier + ".title")
                                .appendString(" ").append(new TranslationTextComponent(Atum.MOD_ID + ".tooltip.shift").mergeStyle(TextFormatting.DARK_GRAY)));
                    }
                }
            }
        }
    }
}
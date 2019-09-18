package com.teammetallurgy.atum.utils.event;

import com.teammetallurgy.atum.api.AtumAPI;
import com.teammetallurgy.atum.utils.Constants;
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

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class TooltipEvent {

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        if (!event.getItemStack().isEmpty()) {
            Item item = event.getItemStack().getItem();
            if (item.isIn(AtumAPI.Tags.TOOLTIP)) {
                if (item.getRegistryName() != null) {
                    String itemIdentifier = item.getRegistryName().getPath() + ".tooltip";
                    if (InputMappings.isKeyDown(Minecraft.getInstance().mainWindow.getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
                        event.getToolTip().add(new TranslationTextComponent(Constants.MOD_ID + "." + itemIdentifier + ".line1").applyTextStyle(TextFormatting.DARK_PURPLE));
                        event.getToolTip().add(new TranslationTextComponent(Constants.MOD_ID + "." + itemIdentifier + ".line2").applyTextStyle(TextFormatting.DARK_PURPLE));
                    } else {
                        event.getToolTip().add(new TranslationTextComponent(Constants.MOD_ID + "." + itemIdentifier + ".title")
                                .appendText(" ").appendSibling(new TranslationTextComponent(Constants.MOD_ID + ".tooltip.shift").applyTextStyle(TextFormatting.DARK_GRAY)));
                    }
                }
            }
        }
    }
}
package com.teammetallurgy.atum.misc.event;

import com.mojang.blaze3d.platform.InputConstants;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.AtumAPI;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, value = Dist.CLIENT)
public class TooltipEvent {

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!stack.isEmpty()) {
            Item item = event.getItemStack().getItem();
            if (stack.is(AtumAPI.Tags.TOOLTIP)) {
                if (item.getRegistryName() != null) {
                    String itemIdentifier = item.getRegistryName().getPath() + ".tooltip";
                    if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
                        event.getToolTip().add(new TranslatableComponent(Atum.MOD_ID + "." + itemIdentifier + ".title").append(": ").withStyle(ChatFormatting.GRAY)
                                .append(new TranslatableComponent(Atum.MOD_ID + "." + itemIdentifier + ".line1").withStyle(ChatFormatting.DARK_GRAY)));
                        event.getToolTip().add(new TranslatableComponent(Atum.MOD_ID + "." + itemIdentifier + ".line2").withStyle(ChatFormatting.DARK_GRAY));
                    } else {
                        event.getToolTip().add(new TranslatableComponent(Atum.MOD_ID + "." + itemIdentifier + ".title").withStyle(ChatFormatting.GRAY)
                                .append(" ").append(new TranslatableComponent(Atum.MOD_ID + ".tooltip.shift").withStyle(ChatFormatting.DARK_GRAY)));
                    }
                }
            }
        }
    }
}
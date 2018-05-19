package com.teammetallurgy.atum.handler.event;

import com.teammetallurgy.atum.handler.AtumConfig;
import com.teammetallurgy.atum.items.artifacts.atum.ItemEyesOfAtum;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT)
public class AtumFogEventListener {

    @SubscribeEvent
    public static void renderFog(EntityViewRenderEvent.RenderFogEvent event) {
        if (event.getEntity().dimension == AtumConfig.DIMENSION_ID && AtumConfig.FOG_ENABLED) {
            GlStateManager.setFog(GlStateManager.FogMode.EXP);
            float fogDensity = 0.08F;

            if (event.getEntity() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.getEntity();
                if (player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof ItemEyesOfAtum) {
                    fogDensity = 0.04F;
                }
                GlStateManager.setFogDensity(fogDensity);
            }
        }
    }
}
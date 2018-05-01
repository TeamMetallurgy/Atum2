package com.teammetallurgy.atum.handler.event;

import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onRender(TickEvent.RenderTickEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().player;

        if (player != null && player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == AtumItems.MUMMY_HELMET) {
            ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
            int width = scaledResolution.getScaledWidth();
            int height = scaledResolution.getScaledHeight();

            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("atum", "textures/hud/mummyblur.png"));
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
            bufferBuilder.pos(0.0D, height, -100).tex(0.0D, 1.0D).endVertex();
            bufferBuilder.pos(width, height, -100).tex(1.0D, 1.0D).endVertex();
            bufferBuilder.pos(width, 0.0D, -100).tex(1.0D, 0.0D).endVertex();
            bufferBuilder.pos(0.0D, 0.0D, -100).tex(0.0D, 0.0D).endVertex();
            tessellator.draw();
        }
    }

    @SubscribeEvent
    public static void onUpdate(TickEvent.PlayerTickEvent event) {
        /*EntityPlayer player = event.player;

        if (player.dimension == AtumConfig.DIMENSION_ID) { //TODO Fix
            if (player.world.isRaining()) {
                //raining = true;

                Random random = new Random();
                int particlesPerTick = (3 - Minecraft.getMinecraft().gameSettings.particleSetting) * 6;
                for (int i = 0; i < particlesPerTick; i++) {
                    float x = random.nextInt(4) - 2;
                    float z = random.nextInt(4) - 2;
                    float y = (random.nextFloat() - 0.7F) * 2F;

                    float vx = 0.1F + random.nextFloat() * 0.1F;
                    float vz = 0.1F + random.nextFloat() * 0.1F;

                    player.world.spawnParticle(EnumParticleTypes.BLOCK_DUST, player.posX + x, player.posY + y, player.posZ + z, vx + player.motionX, 0.0D, vz + player.motionZ);
                }
            }
        }*/
    }
}
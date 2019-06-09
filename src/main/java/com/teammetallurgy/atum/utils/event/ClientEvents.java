package com.teammetallurgy.atum.utils.event;

import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.artifacts.atum.ItemEyesOfAtum;
import com.teammetallurgy.atum.items.artifacts.nuit.ItemNuitsVanishing;
import com.teammetallurgy.atum.proxy.ClientProxy;
import com.teammetallurgy.atum.utils.AtumConfig;
import com.teammetallurgy.atum.utils.Constants;
import com.teammetallurgy.atum.world.WorldProviderAtum;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Side.CLIENT)
public class ClientEvents {
    private static final ResourceLocation MUMMY_BLUR_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/hud/mummyblur.png");
    private static final ResourceLocation SAND_BLUR_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/hud/sandstormwip.png");
    private static float intensity = 1;

    @SubscribeEvent
    public static void renderlast(RenderWorldLastEvent event) {
        if (Minecraft.getMinecraft().player.dimension == AtumConfig.DIMENSION_ID) {
            if (Minecraft.getMinecraft().gameSettings.hideGUI) {
                renderSand(event.getPartialTicks(), 1, 2, 3, 4, 5, 6);
            } else {
                renderSand(event.getPartialTicks(), 1, 2, 3, 4, 5, 6);
            }
        }
    }

    /*@SubscribeEvent
    public static void renderSand(RenderGameOverlayEvent.Pre event) {
        if (event.getType() != ElementType.ALL) return;

        if (Minecraft.getMinecraft().player.dimension == AtumConfig.DIMENSION_ID) {
            //renderSand(event.getPartialTicks(), 1); //TODO Keithy. Minor for later
        }
    }*/

    private static void renderSand(float partialTicks, int... layers) {
        float baseDarkness = AtumConfig.SAND_DARKNESS;
        float baseAlpha = AtumConfig.SAND_ALPHA;
        float eyesOfAtumAlpha = AtumConfig.SAND_EYES_ALPHA;

        if (Minecraft.getMinecraft().player.dimension == AtumConfig.DIMENSION_ID) {
            Minecraft mc = Minecraft.getMinecraft();
            WorldProviderAtum provider = (WorldProviderAtum) mc.player.world.provider;
            float stormStrength = provider.stormStrength;

            if (stormStrength < 0.0001F) {
                return;
            }

            float light = mc.player.world.getSunBrightness(partialTicks);

            GlStateManager.pushMatrix();
            GlStateManager.pushAttrib();
            ScaledResolution scaledRes = new ScaledResolution(mc);

            //mc.entityRenderer.setupOverlayRendering();

            //GlStateManager.clear(256);
            GlStateManager.matrixMode(5889);
            GlStateManager.pushMatrix();
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0D, scaledRes.getScaledWidth_double(), scaledRes.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
            GlStateManager.matrixMode(5888);
            GlStateManager.pushMatrix();
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0F, 0.0F, -2000.0F);

            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.disableAlpha();
            mc.getTextureManager().bindTexture(SAND_BLUR_TEXTURE);

            EntityPlayerSP player = mc.player;
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);

            BlockPos playerPos = new BlockPos(player.posX, player.posY, player.posZ);
            boolean sky = player.world.canBlockSeeSky(playerPos);
            if (!sky || player.world.getBiome(playerPos) == AtumBiomes.OASIS) {
                intensity -= 0.001f * partialTicks;
                intensity = Math.max(0, intensity);
            } else {
                intensity += 0.01f * partialTicks;
                intensity = Math.min(stormStrength, intensity);
            }

            for (int i : layers) {
                float scale = 0.2f / (float) i;
                float alpha = (float) Math.pow(intensity - baseAlpha, i) * intensity;

                // Make it easier to see
                ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                if (helmet.getItem() instanceof ItemEyesOfAtum) {
                    alpha *= eyesOfAtumAlpha;
                }

                GlStateManager.color(baseDarkness * light, baseDarkness * light, baseDarkness * light, alpha);
                float scaleX = 0.01f * scaledRes.getScaledHeight() * scale * scaledRes.getScaleFactor();
                float scaleY = 0.01f * scaledRes.getScaledWidth() * scale * scaledRes.getScaleFactor();
                float speed = 500f - i * 15;
                float movement = -(System.currentTimeMillis() % (int) speed) / speed;
                float yaw = 0.25f * (mc.player.rotationYaw % 360 / 360f) / scale;
                float pitch = 0.5f * (mc.player.rotationPitch % 360 / 360f) / scale;

                bufferbuilder.pos(0.0D, (double) scaledRes.getScaledHeight(), 90.0D).tex(movement + yaw, 1.0D / scaleY + pitch).endVertex();
                bufferbuilder.pos((double) scaledRes.getScaledWidth(), (double) scaledRes.getScaledHeight(), 90.0D).tex(1.0D / scaleX + movement + yaw, 1.0D / scaleY + pitch).endVertex();
                bufferbuilder.pos((double) scaledRes.getScaledWidth(), 0.0D, 90.0D).tex(1.0D / scaleX + movement + yaw, 0.0D + pitch).endVertex();
                bufferbuilder.pos(0.0D, 0.0D, 90.0D).tex(movement + yaw, 0.0D + pitch).endVertex();
            }
            tessellator.draw();

            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableAlpha();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            GlStateManager.matrixMode(5889);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
            GlStateManager.popMatrix();

            GlStateManager.popAttrib();
            GlStateManager.popMatrix();
        }
    }

    @SubscribeEvent
    public static void renderFog(EntityViewRenderEvent.RenderFogEvent event) {
        float sandstormFog = AtumConfig.SANDSTORM_FOG;

        if (event.getEntity().dimension == AtumConfig.DIMENSION_ID && AtumConfig.FOG_ENABLED) {
            GlStateManager.setFog(GlStateManager.FogMode.EXP);
            float fogDensity = 0.08F;

            if (event.getEntity() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.getEntity();
                ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                if (player.getPosition().getY() <= 60) {
                    fogDensity += (float) (62 - player.getPosition().getY()) * 0.005F;
                }
                if (helmet.getItem() instanceof ItemEyesOfAtum) {
                    fogDensity = fogDensity / 3;
                }
                if (helmet.getItem() == AtumItems.WANDERER_HELMET || helmet.getItem() == AtumItems.DESERT_HELMET_IRON || helmet.getItem() == AtumItems.DESERT_HELMET_GOLD || helmet.getItem() == AtumItems.DESERT_HELMET_DIAMOND) {
                    fogDensity = fogDensity / 1.5F;
                }
                if (player.posY >= player.world.getSeaLevel() - 8) {
                    WorldProviderAtum provider = (WorldProviderAtum) Minecraft.getMinecraft().player.world.provider;
                    fogDensity *= 1 + sandstormFog - (sandstormFog - sandstormFog * provider.stormStrength);
                }
                GlStateManager.setFogDensity(fogDensity);
            }
        }
    }

    @SubscribeEvent
    public static void onRender(RenderPlayerEvent.Pre event) {
        EntityPlayer player = event.getEntityPlayer();
        EnumHand hand = player.getHeldItem(EnumHand.OFF_HAND).getItem() == AtumItems.NUITS_VANISHING ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
        ItemStack heldStack = player.getHeldItem(hand);
        if (ItemNuitsVanishing.IS_BAUBLES_INSTALLED && ItemNuitsVanishing.getAmulet(player).getItem() == AtumItems.NUITS_VANISHING) {
            heldStack = ItemNuitsVanishing.getAmulet(player);
        }
        if (heldStack.getItem() == AtumItems.NUITS_VANISHING) {
            if (!ItemNuitsVanishing.isPlayerMoving(player)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void renderMummyHelmet(RenderGameOverlayEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        Minecraft mc = Minecraft.getMinecraft();

        if (player != null && mc.gameSettings.thirdPersonView == 0 && event.getType() == RenderGameOverlayEvent.ElementType.HELMET && player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == AtumItems.MUMMY_HELMET) {
            ScaledResolution scaledResolution = new ScaledResolution(mc);
            int width = scaledResolution.getScaledWidth();
            int height = scaledResolution.getScaledHeight();

            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableAlpha();
            mc.getTextureManager().bindTexture(MUMMY_BLUR_TEXTURE);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(0.0D, (double) height, -90.0D).tex(0.0D, 1.0D).endVertex();
            bufferbuilder.pos((double) width, (double) height, -90.0D).tex(1.0D, 1.0D).endVertex();
            bufferbuilder.pos((double) width, 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
            bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
            tessellator.draw();
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableAlpha();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (Minecraft.getMinecraft().world != null && !Minecraft.getMinecraft().isGamePaused()) {
                ClientProxy.atumParticles.updateEffects();
            }
        }
    }

    @SubscribeEvent
    public static void onRenderLastWorld(RenderWorldLastEvent event) {
        EntityRenderer entityRenderer = Minecraft.getMinecraft().entityRenderer;
        AtumParticles particles = ClientProxy.atumParticles;
        Minecraft mc = Minecraft.getMinecraft();
        Entity entity = mc.getRenderViewEntity();

        entityRenderer.enableLightmap();
        mc.profiler.endStartSection("litParticles");
        particles.renderLitParticles(entity, event.getPartialTicks());
        RenderHelper.disableStandardItemLighting();
        mc.profiler.endStartSection("particles");
        particles.renderParticles(entity, event.getPartialTicks());
        entityRenderer.disableLightmap();
    }

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        if (event.getWorld().isRemote) {
            ClientProxy.atumParticles.clearEffects(event.getWorld());
        }
    }
}
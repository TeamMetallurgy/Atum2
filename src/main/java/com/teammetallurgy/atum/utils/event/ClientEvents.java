package com.teammetallurgy.atum.utils.event;

import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.artifacts.atum.ItemEyesOfAtum;
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
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Side.CLIENT)
public class ClientEvents {
    protected static final ResourceLocation SAND_BLUR_TEX_PATH = new ResourceLocation("atum", "textures/hud/sandstormwip.png");
    private static float intensity = 1;    
    
    @SubscribeEvent
    public static void renderlast(RenderWorldLastEvent event) {
        if (Minecraft.getMinecraft().player.dimension == AtumConfig.DIMENSION_ID) {
			
        	WorldProviderAtum provider = (WorldProviderAtum) Minecraft.getMinecraft().player.world.provider;
			float rain = provider.stormStrength;
			if(rain < 0.0001f)
				return;
	
			ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
	        Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
	        GlStateManager.enableBlend();
	
	        
	        GlStateManager.disableDepth();
	        GlStateManager.depthMask(false);
	        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
	        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	        GlStateManager.disableAlpha();
	        Minecraft.getMinecraft().getTextureManager().bindTexture(SAND_BLUR_TEX_PATH);
	
	        EntityPlayerSP player = Minecraft.getMinecraft().player;
	        boolean sky = player.world.canBlockSeeSky(new BlockPos(player.posX, player.posY, player.posZ));
	        
	        Tessellator tessellator = Tessellator.getInstance();
	        BufferBuilder bufferbuilder = tessellator.getBuffer();
	        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
	        
	        int min = 2;
	        if(Minecraft.getMinecraft().gameSettings.hideGUI)
	        	min = 1;
	        
	        for(int i = 2; i < 6; i++)
	        {
		        float scale = 0.2f / (float)i;
	            if(!sky) { 
	            	intensity -= 0.0005f;
	            	intensity = intensity < 0 ? 0 : intensity;
	            } else {
	            	intensity += 0.01f;
	            	intensity = intensity > 1 ? 1 : intensity;
	            }
	            GlStateManager.color(0.5f, 0.5f, 0.5f, (float)Math.pow(intensity - 0.1f, i) *  rain);
		        float scaleX = 0.01f * scaledRes.getScaledHeight() * scale * scaledRes.getScaleFactor();
		        float scaleY = 0.01f * scaledRes.getScaledWidth() * scale * scaledRes.getScaleFactor();
		        float speed = 500f - i * 15;
		        float movement = -(System.currentTimeMillis() % (int)speed)/speed;
	        	float yaw = 0.25f * (Minecraft.getMinecraft().player.rotationYaw % 360 / 360f) / scale;
	        	float pitch = 0.5f * (Minecraft.getMinecraft().player.rotationPitch % 360 / 360f) / scale;
	
		        bufferbuilder.pos(0.0D, (double)scaledRes.getScaledHeight(), 90.0D)                              .tex(movement + yaw, 1.0D / scaleY + pitch).endVertex();
		        bufferbuilder.pos((double)scaledRes.getScaledWidth(), (double)scaledRes.getScaledHeight(), 90.0D).tex(1.0D / scaleX + movement + yaw, 1.0D / scaleY + pitch).endVertex();
		        bufferbuilder.pos((double)scaledRes.getScaledWidth(), 0.0D, 90.0D)                               .tex(1.0D / scaleX + movement + yaw, 0.0D + pitch).endVertex();
		        bufferbuilder.pos(0.0D, 0.0D, 90.0D)                                                             .tex(movement + yaw, 0.0D + pitch).endVertex();
	        }
	        tessellator.draw();
	
	        GlStateManager.depthMask(true);
	        GlStateManager.enableDepth();
	        GlStateManager.enableAlpha();
	        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
    
	@SubscribeEvent
	public static void renderSand(RenderGameOverlayEvent.Pre event) {
        if (Minecraft.getMinecraft().player.dimension == AtumConfig.DIMENSION_ID) {

			if(event.getType() != ElementType.ALL)
				return;

        	WorldProviderAtum provider = (WorldProviderAtum) Minecraft.getMinecraft().player.world.provider;
			float rain = provider.stormStrength;
			if(rain < 0.0001f)
				return;
	
			ScaledResolution scaledRes = event.getResolution();
	        Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
	        GlStateManager.enableBlend();
	
	        
	        GlStateManager.disableDepth();
	        GlStateManager.depthMask(false);
	        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
	        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	        GlStateManager.disableAlpha();
	        Minecraft.getMinecraft().getTextureManager().bindTexture(SAND_BLUR_TEX_PATH);
	
	        EntityPlayerSP player = Minecraft.getMinecraft().player;
	        boolean sky = player.world.canBlockSeeSky(new BlockPos(player.posX, player.posY, player.posZ));
	        
	        Tessellator tessellator = Tessellator.getInstance();
	        BufferBuilder bufferbuilder = tessellator.getBuffer();
	        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
	        for(int i = 1; i < 2; i++)
	        {
		        float scale = 0.2f / (float)i;
	            if(!sky) { 
	            	intensity -= 0.0005f;
	            	intensity = intensity < 0 ? 0 : intensity;
	            } else {
	            	intensity += 0.01f;
	            	intensity = intensity > 1 ? 1 : intensity;
	            }
	            GlStateManager.color(0.5f, 0.5f, 0.5f, (float)Math.pow(intensity - 0.1f, i) *  rain);
		        float scaleX = 0.01f * scaledRes.getScaledHeight() * scale * scaledRes.getScaleFactor();
		        float scaleY = 0.01f * scaledRes.getScaledWidth() * scale * scaledRes.getScaleFactor();
		        float speed = 500f - i * 15;
		        float movement = -(System.currentTimeMillis() % (int)speed)/speed;
	        	float yaw = 0.25f * (Minecraft.getMinecraft().player.rotationYaw % 360 / 360f) / scale;
	        	float pitch = 0.5f * (Minecraft.getMinecraft().player.rotationPitch % 360 / 360f) / scale;
	        	//System.out.println(Minecraft.getMinecraft().player.rotationYaw);
	
		        bufferbuilder.pos(0.0D, (double)scaledRes.getScaledHeight(), 90.0D)                              .tex(movement + yaw, 1.0D / scaleY + pitch).endVertex();
		        bufferbuilder.pos((double)scaledRes.getScaledWidth(), (double)scaledRes.getScaledHeight(), 90.0D).tex(1.0D / scaleX + movement + yaw, 1.0D / scaleY + pitch).endVertex();
		        bufferbuilder.pos((double)scaledRes.getScaledWidth(), 0.0D, 90.0D)                               .tex(1.0D / scaleX + movement + yaw, 0.0D + pitch).endVertex();
		        bufferbuilder.pos(0.0D, 0.0D, 90.0D)                                                             .tex(movement + yaw, 0.0D + pitch).endVertex();
	        }
	        tessellator.draw();
	
	        GlStateManager.depthMask(true);
	        GlStateManager.enableDepth();
	        GlStateManager.enableAlpha();
	        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
	}

    @SubscribeEvent
    public static void renderFog(EntityViewRenderEvent.RenderFogEvent event) {
        if (event.getEntity().dimension == AtumConfig.DIMENSION_ID && AtumConfig.FOG_ENABLED) {
            GlStateManager.setFog(GlStateManager.FogMode.EXP);
            float fogDensity = 0.08F;

            if (event.getEntity() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.getEntity();
                ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                if (player.getPosition().getY() <= 60) {
                    fogDensity += (float) (62 - player.getPosition().getY()) * 0.00666F;
                }
                if (helmet.getItem() instanceof ItemEyesOfAtum) {
                    fogDensity = fogDensity / 3;
                }
                if (helmet.getItem() == AtumItems.WANDERER_HELMET || helmet.getItem() == AtumItems.DESERT_HELMET_IRON || helmet.getItem() == AtumItems.DESERT_HELMET_DIAMOND) {
                    fogDensity = fogDensity / 1.5F;
                }
                fogDensity *= 6 - (5 - 5 * event.getEntity().world.rainingStrength);
                GlStateManager.setFogDensity(fogDensity);
            }
        }
    }

    @SubscribeEvent
    public static void onRender(RenderPlayerEvent.Pre event) {
        EntityPlayer player = event.getEntityPlayer();
        EnumHand hand = player.getHeldItem(EnumHand.OFF_HAND).getItem() == AtumItems.NUITS_VANISHING ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
        ItemStack heldStack = player.getHeldItem(hand);
        if (heldStack.getItem() == AtumItems.NUITS_VANISHING) {
            if (player.onGround && !player.isSneaking() && player.distanceWalkedModified == player.prevDistanceWalkedModified) {
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
            mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/hud/mummyblur.png"));
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
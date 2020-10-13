package com.teammetallurgy.atum.misc.event;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.artifacts.atem.EyesOfAtemItem;
import com.teammetallurgy.atum.items.artifacts.nuit.NuitsVanishingItem;
import com.teammetallurgy.atum.misc.AtumConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {
    private static final ResourceLocation MUMMY_BLUR_TEXTURE = new ResourceLocation(Atum.MOD_ID, "textures/hud/mummyblur.png");
    private static final ResourceLocation SAND_BLUR_TEXTURE = new ResourceLocation(Atum.MOD_ID, "textures/hud/sandstormwip.png");
    private static float intensity = 1;

    @SubscribeEvent
    public static void renderlast(RenderWorldLastEvent event) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player != null && player.world.getDimensionKey() == Atum.ATUM) {
            if (Minecraft.getInstance().gameSettings.hideGUI) {
                renderSand(event.getPartialTicks(), 1, 2, 3, 4, 5, 6);
            } else {
                renderSand(event.getPartialTicks(), 1, 2, 3, 4, 5, 6);
            }
        }
    }

    /*@SubscribeEvent
    public static void renderSand(RenderGameOverlayEvent.Pre event) { //Keithy. Minor for later
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL || player == null) return;

        if (player.dimension == AtumDimensionType.ATUM) {
            //renderSand(event.getPartialTicks(), 1);
        }
    }*/

    private static void renderSand(float partialTicks, int... layers) {
        float baseDarkness = AtumConfig.SANDSTORM.sandDarkness.get() / 100.0F;
        float baseAlpha = AtumConfig.SANDSTORM.sandAlpha.get() / 100.0F;
        float eyesOfAtumAlpha = AtumConfig.SANDSTORM.sandEyesAlpha.get() / 100.0F;
        Minecraft mc = Minecraft.getInstance();
        ClientPlayerEntity player = mc.player;
        if (player == null) return;
        //Dimension dimension = player.world.getDimension();

        if (/*dimension instanceof AtumDimension &&*/ mc.world != null && mc.world.getDimensionKey() == Atum.ATUM) {
            //AtumDimension atum = (AtumDimension) dimension;
            World world = mc.world;
            float stormStrength = 0.0F /*atum.stormStrength*/; //TODO

            if (stormStrength < 0.0001F) {
                return;
            }

            float light = getSunBrightness(world, partialTicks);

            RenderSystem.pushMatrix();
            RenderSystem.pushTextureAttributes();

            //mc.entityRenderer.setupOverlayRendering();

            //RenderSystem.clear(256);
            RenderSystem.matrixMode(5889);
            RenderSystem.pushMatrix();
            RenderSystem.loadIdentity();
            RenderSystem.ortho(0.0D, mc.getMainWindow().getScaledWidth(), mc.getMainWindow().getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
            RenderSystem.matrixMode(5888);
            RenderSystem.pushMatrix();
            RenderSystem.loadIdentity();
            RenderSystem.translatef(0.0F, 0.0F, -2000.0F);

            RenderSystem.enableBlend();
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderSystem.disableAlphaTest();
            mc.getTextureManager().bindTexture(SAND_BLUR_TEXTURE);

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);

            BlockPos playerPos = new BlockPos(player.getPosX(), player.getPosY(), player.getPosZ());
            boolean sky = player.world.canBlockSeeSky(playerPos);
            Optional<RegistryKey<Biome>> biomeKey = world.func_241828_r().getRegistry(Registry.BIOME_KEY).getOptionalKey(player.world.getBiome(playerPos));
            if (!sky || biomeKey.isPresent() && biomeKey.get() == AtumBiomes.OASIS) { //TODO Test
                intensity -= 0.001F * partialTicks;
                intensity = Math.max(0, intensity);
            } else {
                intensity += 0.01F * partialTicks;
                intensity = Math.min(stormStrength, intensity);
            }

            for (int i : layers) {
                float scale = 0.2F / (float) i;
                float alpha = (float) Math.pow(intensity - baseAlpha, i) * intensity;

                // Make it easier to see
                ItemStack helmet = player.getItemStackFromSlot(EquipmentSlotType.HEAD);
                if (helmet.getItem() instanceof EyesOfAtemItem) {
                    alpha *= eyesOfAtumAlpha;
                }

                RenderSystem.color4f(baseDarkness * light, baseDarkness * light, baseDarkness * light, alpha);
                double scaleX = 0.01F * mc.getMainWindow().getScaledHeight() * scale * mc.getMainWindow().getGuiScaleFactor();
                double scaleY = 0.01F * mc.getMainWindow().getScaledWidth() * scale * mc.getMainWindow().getGuiScaleFactor();
                float speed = 500f - i * 15;
                float movement = -(System.currentTimeMillis() % (int) speed) / speed;
                float yaw = 0.25F * (player.rotationYaw % 360 / 360F) / scale;
                float pitch = 0.5F * (player.rotationPitch % 360 / 360F) / scale;

                bufferbuilder.pos(0.0D, mc.getMainWindow().getScaledHeight(), 90.0D).tex(movement + yaw, (float) (1.0D / scaleY + pitch)).endVertex();
                bufferbuilder.pos(mc.getMainWindow().getScaledWidth(), mc.getMainWindow().getScaledHeight(), 90.0D).tex((float) (1.0D / scaleX + movement + yaw), (float) (1.0D / scaleY + pitch)).endVertex();
                bufferbuilder.pos(mc.getMainWindow().getScaledWidth(), 0.0D, 90.0D).tex((float) (1.0D / scaleX + movement + yaw), 0.0F + pitch).endVertex();
                bufferbuilder.pos(0.0D, 0.0D, 90.0D).tex(movement + yaw, 0.0F + pitch).endVertex();
            }
            tessellator.draw();

            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.enableAlphaTest();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

            RenderSystem.matrixMode(5889);
            RenderSystem.popMatrix();
            RenderSystem.matrixMode(5888);
            RenderSystem.popMatrix();

            RenderSystem.popAttributes();
            RenderSystem.popMatrix();
        }
    }

    public static float getSunBrightness(World world, float partialTicks) {
        float celestialAngle = world.getCelestialAngleRadians(partialTicks);
        float f1 = 1.0F - (celestialAngle) * 2.0F + 0.2F;
        f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
        f1 = 1.0F - f1;
        f1 = (float) ((double) f1 * (1.0D - (double) (world.getRainStrength(partialTicks) * 5.0F) / 16.0D));
        f1 = (float) ((double) f1 * (1.0D - (double) (world.getThunderStrength(partialTicks) * 5.0F) / 16.0D));
        return f1 * 0.8F + 0.2F;
    }

    @SubscribeEvent
    public static void renderFog(EntityViewRenderEvent.RenderFogEvent event) {
        float sandstormFog = AtumConfig.SANDSTORM.sandstormFog.get();
        ClientPlayerEntity clientPlayer = Minecraft.getInstance().player;
        if (clientPlayer == null) return;
        //Dimension dimension = clientPlayer.world.dimension;
        Entity entity = event.getInfo().getRenderViewEntity();

        if (/*dimension instanceof AtumDimension &&*/ entity.world.getDimensionKey() == Atum.ATUM && AtumConfig.GENERAL.fogEnabled.get()) {
            RenderSystem.fogMode(GlStateManager.FogMode.EXP);
            float fogDensity = 0.08F;

            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;
                ItemStack helmet = player.getItemStackFromSlot(EquipmentSlotType.HEAD);
                if (player.getPosition().getY() <= 60) {
                    fogDensity += (float) (62 - player.getPosition().getY()) * 0.005F;
                }
                if (helmet.getItem() instanceof EyesOfAtemItem) {
                    fogDensity = fogDensity / 3;
                }
                if (helmet.getItem() == AtumItems.WANDERER_HELMET || helmet.getItem() == AtumItems.DESERT_HELMET_IRON || helmet.getItem() == AtumItems.DESERT_HELMET_GOLD || helmet.getItem() == AtumItems.DESERT_HELMET_DIAMOND) {
                    fogDensity = fogDensity / 1.5F;
                }
                if (player.getPosY() >= player.world.getSeaLevel() - 8) {
                    /*AtumDimension providerAtum = (AtumDimension) dimension;
                    fogDensity *= 1 + sandstormFog - (sandstormFog - sandstormFog * providerAtum.stormStrength);*/ //TODO
                }
                RenderSystem.fogDensity(fogDensity);
                event.setResult(Event.Result.ALLOW);
            }
        }
    }

    @SubscribeEvent
    public static void onRender(RenderPlayerEvent.Pre event) {
        PlayerEntity player = event.getPlayer();
        Hand hand = player.getHeldItem(Hand.OFF_HAND).getItem() == AtumItems.NUITS_VANISHING ? Hand.OFF_HAND : Hand.MAIN_HAND;
        ItemStack heldStack = player.getHeldItem(hand);
        /*if (NuitsVanishingItem.IS_BAUBLES_INSTALLED && NuitsVanishingItem.getAmulet(player).getItem() == AtumItems.NUITS_VANISHING) {
            heldStack = NuitsVanishingItem.getAmulet(player);
        }*/
        if (heldStack.getItem() == AtumItems.NUITS_VANISHING) {
            if (!NuitsVanishingItem.isPlayerMoving(player)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void renderMummyHelmet(RenderGameOverlayEvent event) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        Minecraft mc = Minecraft.getInstance();

        if (player != null && mc.gameSettings.getPointOfView().func_243192_a() && event.getType() == RenderGameOverlayEvent.ElementType.HELMET && player.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == AtumItems.MUMMY_HELMET) {
            int width = mc.getMainWindow().getScaledWidth();
            int height = mc.getMainWindow().getScaledHeight();

            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableAlphaTest();
            mc.getTextureManager().bindTexture(MUMMY_BLUR_TEXTURE);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(0.0D, height, -90.0D).tex(0.0F, 1.0F).endVertex();
            bufferbuilder.pos(width, height, -90.0D).tex(1.0F, 1.0F).endVertex();
            bufferbuilder.pos(width, 0.0D, -90.0D).tex(1.0F, 0.0F).endVertex();
            bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0F, 0.0F).endVertex();
            tessellator.draw();
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.enableAlphaTest();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
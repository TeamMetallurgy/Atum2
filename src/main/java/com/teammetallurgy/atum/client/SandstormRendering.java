package com.teammetallurgy.atum.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.items.artifacts.ArtifactArmor;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.world.SandstormHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, value = Dist.CLIENT)
public class SandstormRendering {
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

        if (player.world.getDimensionKey() == Atum.ATUM) {
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

        if (mc.world != null && mc.world.getDimensionKey() == Atum.ATUM) {
            World world = mc.world;
            float stormStrength = SandstormHandler.INSTANCE.stormStrength;

            if (stormStrength < 0.0001F) {
                return;
            }

            float light = getSunBrightness(world, partialTicks);

            RenderSystem.pushMatrix();

            //mc.entityRenderer.setupOverlayRendering();

            RenderSystem.clear(256, Minecraft.IS_RUNNING_ON_MAC);
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
            if (!sky || playerPos.getY() < 50 ||  biomeKey.isPresent() && biomeKey.get() == AtumBiomes.OASIS) {
                intensity -= 0.006F * partialTicks;
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
                if (helmet.getItem() instanceof ArtifactArmor) {
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
}
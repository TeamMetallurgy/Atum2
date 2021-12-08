package com.teammetallurgy.atum.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.items.artifacts.ArtifactArmor;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.world.SandstormHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, value = Dist.CLIENT)
public class SandstormRendering {
    private static final ResourceLocation SAND_BLUR_TEXTURE = new ResourceLocation(Atum.MOD_ID, "textures/hud/sandstormwip.png");
    private static float intensity = 1;

    @SubscribeEvent
    public static void renderlast(RenderLevelLastEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && player.level.dimension() == Atum.ATUM) {
            if (Minecraft.getInstance().options.hideGui) {
                renderSand(event.getPartialTick(), 1, 2, 3, 4, 5, 6);
            } else {
                renderSand(event.getPartialTick(), 1, 2, 3, 4, 5, 6);
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
        LocalPlayer player = mc.player;
        if (player == null) return;

        if (mc.level != null && mc.level.dimension() == Atum.ATUM) {
            Level world = mc.level;
            float stormStrength = SandstormHandler.INSTANCE.stormStrength;

            if (stormStrength < 0.0001F) {
                return;
            }

            float light = getSunBrightness(world, partialTicks);

            RenderSystem.pushMatrix();

            //mc.entityRenderer.setupOverlayRendering();

            RenderSystem.clear(256, Minecraft.ON_OSX);
            RenderSystem.matrixMode(5889);
            RenderSystem.pushMatrix();
            RenderSystem.loadIdentity();
            RenderSystem.ortho(0.0D, mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight(), 0.0D, 1000.0D, 3000.0D);
            RenderSystem.matrixMode(5888);
            RenderSystem.pushMatrix();
            RenderSystem.loadIdentity();
            RenderSystem.translatef(0.0F, 0.0F, -2000.0F);

            RenderSystem.enableBlend();
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderSystem.disableAlphaTest();
            mc.getTextureManager().bind(SAND_BLUR_TEXTURE);

            Tesselator tessellator = Tesselator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuilder();
            bufferbuilder.begin(7, DefaultVertexFormat.POSITION_TEX);

            BlockPos playerPos = new BlockPos(player.getX(), player.getY(), player.getZ());
            boolean sky = player.level.canSeeSkyFromBelowWater(playerPos);
            Optional<ResourceKey<Biome>> biomeKey = world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getResourceKey(player.level.getBiome(playerPos));
            if (!sky || playerPos.getY() < 50 /*||  biomeKey.isPresent() && biomeKey.get() == AtumBiomes.OASIS*/) { //TODO Uncomment when biomes are re-added
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
                ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
                if (helmet.getItem() instanceof ArtifactArmor) {
                    alpha *= eyesOfAtumAlpha;
                }

                RenderSystem.color4f(baseDarkness * light, baseDarkness * light, baseDarkness * light, alpha);
                double scaleX = 0.01F * mc.getWindow().getGuiScaledHeight() * scale * mc.getWindow().getGuiScale();
                double scaleY = 0.01F * mc.getWindow().getGuiScaledWidth() * scale * mc.getWindow().getGuiScale();
                float speed = 500f - i * 15;
                float movement = -(System.currentTimeMillis() % (int) speed) / speed;
                float yaw = 0.25F * (player.getYRot() % 360 / 360F) / scale;
                float pitch = 0.5F * (player.getXRot() % 360 / 360F) / scale;

                bufferbuilder.vertex(0.0D, mc.getWindow().getGuiScaledHeight(), 90.0D).uv(movement + yaw, (float) (1.0D / scaleY + pitch)).endVertex();
                bufferbuilder.vertex(mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight(), 90.0D).uv((float) (1.0D / scaleX + movement + yaw), (float) (1.0D / scaleY + pitch)).endVertex();
                bufferbuilder.vertex(mc.getWindow().getGuiScaledWidth(), 0.0D, 90.0D).uv((float) (1.0D / scaleX + movement + yaw), 0.0F + pitch).endVertex();
                bufferbuilder.vertex(0.0D, 0.0D, 90.0D).uv(movement + yaw, 0.0F + pitch).endVertex();
            }
            tessellator.end();

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

    public static float getSunBrightness(Level world, float partialTicks) {
        float celestialAngle = world.getSunAngle(partialTicks);
        float f1 = 1.0F - (celestialAngle) * 2.0F + 0.2F;
        f1 = Mth.clamp(f1, 0.0F, 1.0F);
        f1 = 1.0F - f1;
        f1 = (float) ((double) f1 * (1.0D - (double) (world.getRainLevel(partialTicks) * 5.0F) / 16.0D));
        f1 = (float) ((double) f1 * (1.0D - (double) (world.getThunderLevel(partialTicks) * 5.0F) / 16.0D));
        return f1 * 0.8F + 0.2F;
    }
}
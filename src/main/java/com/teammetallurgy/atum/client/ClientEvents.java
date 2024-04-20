package com.teammetallurgy.atum.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.artifacts.nuit.NuitsVanishingItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderGuiOverlayEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {
    private static final ResourceLocation MUMMY_BLUR_TEXTURE = new ResourceLocation(Atum.MOD_ID, "textures/hud/mummyblur.png");

    /*@SubscribeEvent
    public static void renderFog(RenderFogEvent event) { //TODO Is there a replacement for this?
        float sandstormFog = AtumConfig.SANDSTORM.sandstormFog.get();
        LocalPlayer clientPlayer = Minecraft.getInstance().player;
        if (clientPlayer == null) return;
        Entity entity = event.getCamera().getEntity();
        Level level = entity.level;

        if (level.dimension() == Atum.ATUM && AtumConfig.GENERAL.fogEnabled.get()) {
            if (event.getMode() == FogRenderer.FogMode.FOG_SKY) {
                event.setCanceled(true); //Needs to get canceled, before it can work
                event.setFarPlaneDistance(100.0F);
                event.setNearPlaneDistance(25.0F);
            } else if (event.getMode() == FogRenderer.FogMode.FOG_TERRAIN) {
                float fogDensity = 220.0F;

                if (entity instanceof Player player) {
                    if (player.blockPosition().getY() <= 60) {
                        fogDensity += (float) (62 - player.blockPosition().getY()) * 0.333F;
                    }
                    Holder<Biome> biome = level.getBiome(player.blockPosition());
                    if (biome.is(AtumBiomes.OASIS.location())) {
                        fogDensity *= 2.0F;
                    }

                    for (ItemStack armor : player.getArmorSlots()) {
                        if (armor.getItem() instanceof IFogReductionItem fogReductionItem) {
                            EquipmentSlot slotType = Mob.getEquipmentSlotForItem(armor);
                            if (fogReductionItem.getSlotTypes().contains(slotType)) {
                                fogDensity = fogReductionItem.getFogReduction(fogDensity, armor);
                            }
                        }
                    }
                    if (player.getY() >= DimensionHelper.GROUND_LEVEL - 8) {
                        fogDensity *= 1 + sandstormFog - (sandstormFog - sandstormFog * SandstormHandler.INSTANCE.stormStrength);
                    }
                    Minecraft mc = Minecraft.getInstance();
                    if (mc.level != null) {
                        Camera camera = event.getCamera();
                        boolean flag2 = mc.level.effects().isFoggyAt(Mth.floor(camera.getPosition().x()), Mth.floor(camera.getPosition().y())) || mc.gui.getBossOverlay().shouldCreateWorldFog();
                        setupFog(fogDensity, camera, FogRenderer.FogMode.FOG_TERRAIN, Math.max(mc.gameRenderer.getRenderDistance(), 32.0F), flag2);
                    }
                }
            }
        }
    }*/


    @SubscribeEvent
    public static void onRender(RenderPlayerEvent.Pre event) {
        Optional<ICuriosItemHandler> optional = CuriosApi.getCuriosInventory(event.getEntity());
        if (optional.isPresent()) {
            Optional<SlotResult> slotResult = optional.get().findFirstCurio(AtumItems.NUITS_VANISHING.get());
            if (slotResult.isPresent()) {
                if (!NuitsVanishingItem.TIMER.containsKey(event.getEntity()) && !NuitsVanishingItem.isLivingEntityMoving(event.getEntity())) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void renderMummyHelmet(RenderGuiOverlayEvent.Post event) {
        LocalPlayer player = Minecraft.getInstance().player;
        Minecraft mc = Minecraft.getInstance();

        if (player != null && mc.options.getCameraType().isFirstPerson() && player.getItemBySlot(EquipmentSlot.HEAD).getItem() == AtumItems.MUMMY_HELMET.get()) {
            int width = mc.getWindow().getGuiScaledWidth();
            int height = mc.getWindow().getGuiScaledHeight();

            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, MUMMY_BLUR_TEXTURE);
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder bufferbuilder = tesselator.getBuilder();
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferbuilder.vertex(0.0D, height, -90.0D).uv(0.0F, 1.0F).endVertex();
            bufferbuilder.vertex(width, height, -90.0D).uv(1.0F, 1.0F).endVertex();
            bufferbuilder.vertex(width, 0.0D, -90.0D).uv(1.0F, 0.0F).endVertex();
            bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0F, 0.0F).endVertex();
            tesselator.end();
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
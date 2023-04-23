package com.teammetallurgy.atum.client;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.artifacts.nuit.NuitsVanishingItem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.FogType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;

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

    public static void setupFog(float baseFog, Camera p_109025_, FogRenderer.FogMode p_109026_, float p_109027_, boolean p_109028_) {
        FogType fogtype = p_109025_.getFluidInCamera();
        Entity entity = p_109025_.getEntity();
        FogShape fogshape = FogShape.SPHERE;
        float f;
        float f1;
        if (baseFog >= 0) {
            f = -8.0f;
            f1 = baseFog * 0.5F;
        } else
        if (entity instanceof LivingEntity && ((LivingEntity)entity).hasEffect(MobEffects.BLINDNESS)) {
            int i = ((LivingEntity)entity).getEffect(MobEffects.BLINDNESS).getDuration();
            float f3 = Mth.lerp(Math.min(1.0F, (float)i / 20.0F), p_109027_, 5.0F);
            if (p_109026_ == FogRenderer.FogMode.FOG_SKY) {
                f = 0.0F;
                f1 = f3 * 0.8F;
            } else {
                f = fogtype == FogType.WATER ? -4.0F : f3 * 0.25F;
                f1 = f3;
            }
        } else if (fogtype == FogType.WATER) {
            f = -8.0F;
            f1 = 96.0F;
            if (entity instanceof LocalPlayer localplayer) {
                f1 *= Math.max(0.25F, localplayer.getWaterVision());
            }
            if (f1 > p_109027_) {
                f1 = p_109027_;
                fogshape = FogShape.CYLINDER;
            }
        } else if (p_109028_) {
            f = p_109027_ * 0.05F;
            f1 = Math.min(p_109027_, 192.0F) * 0.5F;
        } else if (p_109026_ == FogRenderer.FogMode.FOG_SKY) {
            f = 0.0F;
            f1 = p_109027_;
            fogshape = FogShape.CYLINDER;
        } else {
            float f2 = Mth.clamp(p_109027_ / 10.0F, 4.0F, 64.0F);
            f = p_109027_ - f2;
            f1 = p_109027_;
            fogshape = FogShape.CYLINDER;
        }

        RenderSystem.setShaderFogStart(f);
        RenderSystem.setShaderFogEnd(f1);
        RenderSystem.setShaderFogShape(fogshape);
    }

    @SubscribeEvent
    public static void onRender(RenderPlayerEvent.Pre event) {
        Optional<ImmutableTriple<String, Integer, ItemStack>> optional = CuriosApi.getCuriosHelper().findEquippedCurio(AtumItems.NUITS_VANISHING.get(), event.getEntity());
        if (optional.isPresent()) {
            if (!NuitsVanishingItem.TIMER.containsKey(event.getEntity()) && !NuitsVanishingItem.isLivingEntityMoving(event.getEntity())) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void renderMummyHelmet(RenderGuiOverlayEvent event) {
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
            bufferbuilder.vertex(0.0D, (double) height, -90.0D).uv(0.0F, 1.0F).endVertex();
            bufferbuilder.vertex((double) width, (double) height, -90.0D).uv(1.0F, 1.0F).endVertex();
            bufferbuilder.vertex((double) width, 0.0D, -90.0D).uv(1.0F, 0.0F).endVertex();
            bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0F, 0.0F).endVertex();
            tesselator.end();
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
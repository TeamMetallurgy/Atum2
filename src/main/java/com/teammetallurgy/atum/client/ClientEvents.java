package com.teammetallurgy.atum.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.IFogReductionItem;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.artifacts.nuit.NuitsVanishingItem;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.world.DimensionHelper;
import com.teammetallurgy.atum.world.SandstormHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {
    private static final ResourceLocation MUMMY_BLUR_TEXTURE = new ResourceLocation(Atum.MOD_ID, "textures/hud/mummyblur.png");

    @SubscribeEvent
    public static void renderFog(EntityViewRenderEvent.RenderFogEvent event) {
        float sandstormFog = AtumConfig.SANDSTORM.sandstormFog.get();
        LocalPlayer clientPlayer = Minecraft.getInstance().player;
        if (clientPlayer == null) return;
        Entity entity = event.getInfo().getEntity();
        Level world = entity.level;

        if (world.dimension() == Atum.ATUM && AtumConfig.GENERAL.fogEnabled.get()) {
            RenderSystem.fogMode(GlStateManager.FogMode.EXP);
            float fogDensity = 0.05F;

            if (entity instanceof Player) {
                Player player = (Player) entity;
                if (player.blockPosition().getY() <= 60) {
                    fogDensity += (float) (62 - player.blockPosition().getY()) * 0.00333F;
                }
                Optional<ResourceKey<Biome>> biome = world.getBiomeName(entity.blockPosition());
                if (biome.isPresent() && biome.get() == AtumBiomes.OASIS) {
                    fogDensity = fogDensity / 2.0F;
                }

                for (ItemStack armor : player.getArmorSlots()) {
                    if (armor.getItem() instanceof IFogReductionItem) {
                        EquipmentSlot slotType = Mob.getEquipmentSlotForItem(armor);
                        IFogReductionItem fogReductionItem = (IFogReductionItem) armor.getItem();
                        if (fogReductionItem.getSlotTypes().contains(slotType)) {
                            fogDensity = fogReductionItem.getFogReduction(fogDensity, armor);
                        }
                    }
                }
                if (player.getY() >= DimensionHelper.GROUND_LEVEL - 8) {
                    fogDensity *= 1 + sandstormFog - (sandstormFog - sandstormFog * SandstormHandler.INSTANCE.stormStrength);
                }
                RenderSystem.fogDensity(fogDensity);
                event.setResult(Event.Result.ALLOW);
            }
        }
    }

    @SubscribeEvent
    public static void onRender(RenderPlayerEvent.Pre event) {
        Optional<ImmutableTriple<String, Integer, ItemStack>> optional = CuriosApi.getCuriosHelper().findEquippedCurio(AtumItems.NUITS_VANISHING, event.getEntityLiving());
        if (optional.isPresent()) {
            if (!NuitsVanishingItem.TIMER.containsKey(event.getEntityLiving()) && !NuitsVanishingItem.isLivingEntityMoving(event.getEntityLiving())) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void renderMummyHelmet(RenderGameOverlayEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        Minecraft mc = Minecraft.getInstance();

        if (player != null && mc.options.getCameraType().isFirstPerson() && event.getType() == RenderGameOverlayEvent.ElementType.HELMET && player.getItemBySlot(EquipmentSlot.HEAD).getItem() == AtumItems.MUMMY_HELMET) {
            int width = mc.getWindow().getGuiScaledWidth();
            int height = mc.getWindow().getGuiScaledHeight();

            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableAlphaTest();
            mc.getTextureManager().bind(MUMMY_BLUR_TEXTURE);
            Tesselator tessellator = Tesselator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuilder();
            bufferbuilder.begin(7, DefaultVertexFormat.POSITION_TEX);
            bufferbuilder.vertex(0.0D, height, -90.0D).uv(0.0F, 1.0F).endVertex();
            bufferbuilder.vertex(width, height, -90.0D).uv(1.0F, 1.0F).endVertex();
            bufferbuilder.vertex(width, 0.0D, -90.0D).uv(1.0F, 0.0F).endVertex();
            bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0F, 0.0F).endVertex();
            tessellator.end();
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.enableAlphaTest();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
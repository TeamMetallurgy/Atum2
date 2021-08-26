package com.teammetallurgy.atum.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.IFogReductionItem;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.artifacts.nuit.NuitsVanishingItem;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.world.DimensionHelper;
import com.teammetallurgy.atum.world.SandstormHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
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
        ClientPlayerEntity clientPlayer = Minecraft.getInstance().player;
        if (clientPlayer == null) return;
        Entity entity = event.getInfo().getRenderViewEntity();
        World world = entity.world;

        if (world.getDimensionKey() == Atum.ATUM && AtumConfig.GENERAL.fogEnabled.get()) {
            RenderSystem.fogMode(GlStateManager.FogMode.EXP);
            float fogDensity = 0.05F;

            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;
                if (player.getPosition().getY() <= 60) {
                    fogDensity += (float) (62 - player.getPosition().getY()) * 0.00333F;
                }
                Optional<RegistryKey<Biome>> biome = world.func_242406_i(entity.getPosition());
                if (biome.isPresent() && biome.get() == AtumBiomes.OASIS) {
                    fogDensity = fogDensity / 2.0F;
                }

                for (ItemStack armor : player.getArmorInventoryList()) {
                    if (armor.getItem() instanceof IFogReductionItem) {
                        EquipmentSlotType slotType = MobEntity.getSlotForItemStack(armor);
                        IFogReductionItem fogReductionItem = (IFogReductionItem) armor.getItem();
                        if (fogReductionItem.getSlotTypes().contains(slotType)) {
                            fogDensity = fogReductionItem.getFogReduction(fogDensity, armor);
                        }
                    }
                }
                if (player.getPosY() >= DimensionHelper.GROUND_LEVEL - 8) {
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
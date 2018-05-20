package com.teammetallurgy.atum.client.render;

import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelShield;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(value = Side.CLIENT)
@SideOnly(Side.CLIENT)
public class RenderAtumsProtection extends TileEntityItemStackRenderer {
    private static ItemBakedBase bakedBase;
    private final ModelShield modelShield = new ModelShield();

    @Override
    public void renderByItem(@Nonnull ItemStack stack, float partialTicks) {
        Minecraft mc = Minecraft.getMinecraft();

        GlStateManager.pushMatrix();
        mc.getTextureManager().bindTexture(BannerTextures.SHIELD_BASE_TEXTURE);
        GlStateManager.scale(1.0F, -1.0F, -1.0F);
        this.modelShield.render();
        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        ModelResourceLocation modelLocation = new ModelResourceLocation(new ResourceLocation(Constants.MOD_ID, "atums_protection"), "inventory");
        bakedBase = new ItemBakedBase(event.getModelRegistry().getObject(modelLocation));
        event.getModelRegistry().putObject(modelLocation, bakedBase);
    }
}
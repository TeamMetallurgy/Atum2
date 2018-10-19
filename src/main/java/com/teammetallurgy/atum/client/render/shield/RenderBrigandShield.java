package com.teammetallurgy.atum.client.render.shield;

import com.teammetallurgy.atum.client.model.shield.ModelBrigandShield;
import com.teammetallurgy.atum.client.render.ItemBakedBase;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(value = Side.CLIENT)
public class RenderBrigandShield extends TileEntityItemStackRenderer {
    private final ModelBrigandShield modelShield = new ModelBrigandShield();
    private static ItemBakedBase bakedBase;
    private static String textureName;

    public RenderBrigandShield(String textureName) {
        RenderBrigandShield.textureName = textureName;
    }

    @Override
    public void renderByItem(@Nonnull ItemStack stack, float partialTicks) {
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/shield/" + textureName + ".png"));
        this.modelShield.render();
        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        ModelResourceLocation modelLocation = new ModelResourceLocation(new ResourceLocation(Constants.MOD_ID, textureName), "inventory");
        bakedBase = new ItemBakedBase(event.getModelRegistry().getObject(modelLocation));
        event.getModelRegistry().putObject(modelLocation, bakedBase);
    }
}
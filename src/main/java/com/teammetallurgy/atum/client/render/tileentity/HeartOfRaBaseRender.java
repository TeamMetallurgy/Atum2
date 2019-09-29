package com.teammetallurgy.atum.client.render.tileentity;

import com.mojang.blaze3d.platform.GlStateManager;
import com.teammetallurgy.atum.blocks.beacon.tileentity.TileEntityHeartOfRa;
import net.minecraft.client.renderer.tileentity.BeaconTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.DyeColor;
import net.minecraft.tileentity.BeaconTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HeartOfRaBaseRender extends TileEntityRenderer<TileEntityHeartOfRa> {
    private static final ResourceLocation BEAM = new ResourceLocation("textures/entity/beacon_beam.png");

    @Override
    public void render(TileEntityHeartOfRa heartOfRa, double x, double y, double z, float partialTicks, int destroyStage) {
        this.renderBeam(heartOfRa, x, y, z, partialTicks);
    }

    private void renderBeam(TileEntityHeartOfRa heartOfRa, double x, double y, double z, float partialTicks) {
        if (heartOfRa.getWorld() != null) {
            GlStateManager.pushMatrix();
            GlStateManager.alphaFunc(516, 0.1F);
            this.bindTexture(BEAM);
            GlStateManager.disableFog();
            GlStateManager.translated(0, 2.07D, 0);
            int height = 0;
            for (int j = 0; j < DyeColor.values().length - 1; ++j) {
                BeaconTileEntity.BeamSegment beam = new BeaconTileEntity.BeamSegment(DyeColor.RED.getColorComponentValues());
                BeaconTileEntityRenderer.renderBeamSegment(x, y, z, partialTicks, 1.0F, heartOfRa.getWorld().getGameTime(), height, 256 - heartOfRa.getPos().getY() - 16, beam.getColors(), 0.2D, 0.25D);
                height += beam.getHeight();
            }
            GlStateManager.enableFog();
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean isGlobalRenderer(TileEntityHeartOfRa heartOfRa) {
        return true;
    }
}
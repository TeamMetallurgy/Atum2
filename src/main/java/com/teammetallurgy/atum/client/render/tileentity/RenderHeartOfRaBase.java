package com.teammetallurgy.atum.client.render.tileentity;

import com.teammetallurgy.atum.blocks.beacon.tileentity.TileEntityHeartOfRa;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.DyeColor;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderHeartOfRaBase extends TileEntitySpecialRenderer<TileEntityHeartOfRa> {

    @Override
    public void render(TileEntityHeartOfRa heartOfRa, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        this.renderBeam(heartOfRa, x, y, z, partialTicks);
    }

    private void renderBeam(TileEntityHeartOfRa heartOfRa, double x, double y, double z, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.alphaFunc(516, 0.1F);
        this.bindTexture(TileEntityBeaconRenderer.TEXTURE_BEACON_BEAM);
        GlStateManager.disableFog();
        GlStateManager.translate(0, 2.07D, 0);
        int yOffset = 0;
        for (int j = 0; j < DyeColor.values().length - 1; ++j) {
            TileEntityBeacon.BeamSegment beam = new TileEntityBeacon.BeamSegment(DyeColor.RED.getColorComponentValues());
            TileEntityBeaconRenderer.renderBeamSegment(x, y, z, (double) partialTicks, 1.0F, heartOfRa.getWorld().getGameTime(), yOffset, 256 - heartOfRa.getPos().getY() - 16, beam.getColors());
            yOffset += beam.getHeight();
        }
        GlStateManager.enableFog();
        GlStateManager.popMatrix();
    }

    @Override
    public boolean isGlobalRenderer(TileEntityHeartOfRa heartOfRa) {
        return true;
    }
}
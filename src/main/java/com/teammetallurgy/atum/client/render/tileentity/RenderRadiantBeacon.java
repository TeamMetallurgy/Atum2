package com.teammetallurgy.atum.client.render.tileentity;

import com.teammetallurgy.atum.blocks.beacon.tileentity.TileEntityRadiantBeacon;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class RenderRadiantBeacon extends TileEntitySpecialRenderer<TileEntityRadiantBeacon> {

    @Override
    public void render(TileEntityRadiantBeacon radiantBeacon, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        this.renderBeacon(x, y, z, partialTicks, radiantBeacon.shouldBeamRender(), radiantBeacon.getBeamSegments(), (double) radiantBeacon.getWorld().getTotalWorldTime());
    }

    private void renderBeacon(double x, double y, double z, double partialTicks, double textureScale, List<TileEntityBeacon.BeamSegment> beamSegments, double totalWorldTime) {
        GlStateManager.alphaFunc(516, 0.1F);
        this.bindTexture(TileEntityBeaconRenderer.TEXTURE_BEACON_BEAM);

        if (textureScale > 0.0D) {
            GlStateManager.disableFog();
            int height = 0;

            for (TileEntityBeacon.BeamSegment beamSegment : beamSegments) {
                TileEntityBeaconRenderer.renderBeamSegment(x, y, z, partialTicks, textureScale, totalWorldTime, height, beamSegment.getHeight(), beamSegment.getColors());
                height += beamSegment.getHeight();
            }
            GlStateManager.enableFog();
        }
    }

    @Override
    public boolean isGlobalRenderer(TileEntityRadiantBeacon te) {
        return true;
    }
}
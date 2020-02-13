package com.teammetallurgy.atum.client.render.tileentity;

import com.mojang.blaze3d.platform.GlStateManager;
import com.teammetallurgy.atum.blocks.beacon.tileentity.RadiantBeaconTileEntity;
import net.minecraft.client.renderer.tileentity.BeaconTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.tileentity.BeaconTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class RadiantBeaconRender extends TileEntityRenderer<RadiantBeaconTileEntity> {
    private static final ResourceLocation BEAM = new ResourceLocation("textures/entity/beacon_beam.png");

    @Override
    public void render(RadiantBeaconTileEntity radiantBeacon, double x, double y, double z, float partialTicks, int destroyStage) {
        if (radiantBeacon.getWorld() != null) {
            this.renderBeacon(x, y, z, partialTicks, radiantBeacon.getBeamSegments(), radiantBeacon.getWorld().getGameTime());
        }
    }

    private void renderBeacon(double x, double y, double z, double partialTicks, List<BeaconTileEntity.BeamSegment> beamSegments, long totalWorldTime) {
        GlStateManager.alphaFunc(516, 0.1F);
        this.bindTexture(BEAM);
        GlStateManager.disableFog();
        int height = 0;

        for (int i = 0; i < beamSegments.size(); ++i) {
            BeaconTileEntity.BeamSegment segment = beamSegments.get(i);
            BeaconTileEntityRenderer.renderBeamSegment(x, y, z, partialTicks, 1.0F, totalWorldTime, height, i == beamSegments.size() - 1 ? 1024 : segment.getHeight(), segment.getColors(), 0.2D, 0.25D);
            height += segment.getHeight();
        }
        GlStateManager.enableFog();
    }

    @Override
    public boolean isGlobalRenderer(RadiantBeaconTileEntity te) {
        return true;
    }
}
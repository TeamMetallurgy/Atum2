package com.teammetallurgy.atum.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.teammetallurgy.atum.blocks.beacon.tileentity.RadiantBeaconTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.BeaconTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.BeaconTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class RadiantBeaconRender extends TileEntityRenderer<RadiantBeaconTileEntity> {
    private static final ResourceLocation BEAM = new ResourceLocation("textures/entity/beacon_beam.png");

    public RadiantBeaconRender(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(RadiantBeaconTileEntity radiantBeacon, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        if (radiantBeacon.getWorld() != null) {
            this.renderBeacon(matrixStack, buffer, partialTicks, radiantBeacon.getBeamSegments(), radiantBeacon.getWorld().getGameTime());
        }
    }

    private void renderBeacon(MatrixStack matrixStack, IRenderTypeBuffer buffer, float partialTicks, List<BeaconTileEntity.BeamSegment> beamSegments, long totalWorldTime) {
        int height = 0;
        for (int i = 0; i < beamSegments.size(); ++i) {
            BeaconTileEntity.BeamSegment segment = beamSegments.get(i);
            BeaconTileEntityRenderer.renderBeamSegment(matrixStack, buffer, BEAM, partialTicks, 1.0F, totalWorldTime, height, i == beamSegments.size() - 1 ? 1024 : segment.getHeight(), segment.getColors(), 0.2F, 0.25F);
            height += segment.getHeight();
        }
    }

    @Override
    public boolean isGlobalRenderer(RadiantBeaconTileEntity te) {
        return true;
    }
}
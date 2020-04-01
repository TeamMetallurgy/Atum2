package com.teammetallurgy.atum.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.teammetallurgy.atum.blocks.beacon.tileentity.HeartOfRaTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.BeaconTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.DyeColor;
import net.minecraft.tileentity.BeaconTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class HeartOfRaBaseRender extends TileEntityRenderer<HeartOfRaTileEntity> {
    private static final ResourceLocation BEAM = new ResourceLocation("textures/entity/beacon_beam.png");

    public HeartOfRaBaseRender(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(@Nonnull HeartOfRaTileEntity heartOfRa, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        this.renderBeam(heartOfRa, partialTicks, matrixStack, buffer);
    }

    private void renderBeam(HeartOfRaTileEntity heartOfRa, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer) {
        if (heartOfRa.getWorld() != null) {
            matrixStack.translate(0, 1.83D, 0);
            int height = 0;
            for (int j = 0; j < DyeColor.values().length - 1; ++j) {
                BeaconTileEntity.BeamSegment beam = new BeaconTileEntity.BeamSegment(DyeColor.RED.getColorComponentValues());
                BeaconTileEntityRenderer.renderBeamSegment(matrixStack, buffer, BEAM, partialTicks, 1.0F, heartOfRa.getWorld().getGameTime(), height, 256 - heartOfRa.getPos().getY() - 16, beam.getColors(), 0.2F, 0.25F);
                height += beam.getHeight();
            }
        }
    }

    @Override
    public boolean isGlobalRenderer(HeartOfRaTileEntity heartOfRa) {
        return true;
    }
}
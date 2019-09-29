package com.teammetallurgy.atum.client.render.tileentity;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.GlStateManager;
import com.teammetallurgy.atum.blocks.wood.BlockCrate;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.CrateTileEntity;
import com.teammetallurgy.atum.client.model.chest.CrateModel;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class CrateRender extends TileEntityRenderer<CrateTileEntity> {
    private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();
    private final CrateModel modelCrate = new CrateModel();

    @Override
    public void render(@Nonnull CrateTileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.enableDepthTest();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        BlockState state = te.hasWorld() ? te.getBlockState() : AtumBlocks.PALM_CRATE.getDefaultState().with(BlockCrate.FACING, Direction.SOUTH);

        if (destroyStage >= 0) {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scalef(4.0F, 4.0F, 1.0F);
            GlStateManager.translatef(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        } else {
            String name = Objects.requireNonNull(te.getBlockState().getBlock().getRegistryName()).getPath();
            ResourceLocation chestTexture = CACHE.get(name);

            if (chestTexture == null) {
                chestTexture = new ResourceLocation(Constants.MOD_ID, "textures/blocks/chest/" + name + ".png");
                CACHE.put(name, chestTexture);
            }
            this.bindTexture(chestTexture);
        }

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.translatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        GlStateManager.scalef(1.0F, -1.0F, -1.0F);
        float angle = state.get(BlockCrate.FACING).getHorizontalAngle();
        if ((double) Math.abs(angle) > 1.0E-5D) {
            GlStateManager.translatef(0.5F, 0.5F, 0.5F);
            GlStateManager.rotatef(angle, 0.0F, 1.0F, 0.0F);
            GlStateManager.translatef(-0.5F, -0.5F, -0.5F);
        }
        float lid = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;
        lid = 1.0F - lid;
        lid = 1.0F - lid * lid * lid;

        modelCrate.crateLid.rotateAngleY = (lid * ((float) Math.PI / 3.5F));
        modelCrate.renderAll();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
}
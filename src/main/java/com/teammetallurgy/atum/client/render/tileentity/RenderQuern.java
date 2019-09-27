package com.teammetallurgy.atum.client.render.tileentity;

import com.teammetallurgy.atum.blocks.machines.tileentity.QuernTileEntity;
import com.teammetallurgy.atum.client.model.QuernStoneModel;
import com.teammetallurgy.atum.utils.Constants;
import com.teammetallurgy.atum.utils.RenderUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class RenderQuern extends TileEntitySpecialRenderer<QuernTileEntity> {
    private static final ResourceLocation QUERN_STONE = new ResourceLocation(Constants.MOD_ID, "textures/blocks/quern_stone.png");
    private static final QuernStoneModel QUERN_STONE_MODEL = new QuernStoneModel();

    @Override
    public void render(@Nonnull QuernTileEntity quern, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        GlStateManager.scale(0.95F, 1.0F, 0.95F);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);

        int meta = quern.getBlockMetadata();
        float rotation = 0.0F;
        if (meta == 2) {
            rotation = 180.0F;
        }

        if (meta == 4) {
            rotation = 90.0F;
        }

        if (meta == 5) {
            rotation = -90.0F;
        }
        GlStateManager.rotate(rotation, 0.0F, 1.0F, 0.0F);

        if (destroyStage >= 0) {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 1.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        } else {
            this.bindTexture(QUERN_STONE);
        }

        float quernRotation = quern.getRotations();
        GlStateManager.rotate(-quernRotation, 0.0F, 1.0F, 0.0F);
        QUERN_STONE_MODEL.renderAll();
        GlStateManager.depthMask(true);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();

        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }

        ItemStack stack = quern.getStackInSlot(0);
        if (!stack.isEmpty()) {
            RenderUtils.renderItem(quern, stack, x, y - 0.7D, z, quernRotation, true);
        }
    }
}
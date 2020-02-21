package com.teammetallurgy.atum.client.render.tileentity;

import com.mojang.blaze3d.platform.GlStateManager;
import com.teammetallurgy.atum.blocks.machines.QuernBlock;
import com.teammetallurgy.atum.blocks.machines.tileentity.QuernTileEntity;
import com.teammetallurgy.atum.client.model.QuernStoneModel;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.utils.Constants;
import com.teammetallurgy.atum.utils.RenderUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class QuernRender extends TileEntityRenderer<QuernTileEntity> {
    private static final ResourceLocation QUERN_STONE = new ResourceLocation(Constants.MOD_ID, "textures/block/quern_stone.png");
    private static final QuernStoneModel QUERN_STONE_MODEL = new QuernStoneModel();

    @Override
    public void render(@Nonnull QuernTileEntity quern, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.translatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        GlStateManager.scalef(0.95F, 1.0F, 0.95F);
        GlStateManager.rotatef(180.0F, 0.0F, 0.0F, 1.0F);
        BlockState state = quern.hasWorld() ? quern.getBlockState() : AtumBlocks.QUERN.getDefaultState().with(QuernBlock.FACING, Direction.SOUTH);

        float angle = state.get(QuernBlock.FACING).getHorizontalAngle();
        if ((double) Math.abs(angle) > 1.0E-5D) {
            GlStateManager.rotatef(angle, 0.0F, 1.0F, 0.0F);
        }

        if (destroyStage >= 0) {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scalef(4.0F, 1.0F, 1.0F);
            GlStateManager.translatef(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        } else {
            this.bindTexture(QUERN_STONE);
        }

        float quernRotation = quern.getRotations();
        GlStateManager.rotatef(-quernRotation, 0.0F, 1.0F, 0.0F);
        QUERN_STONE_MODEL.renderAll();
        GlStateManager.depthMask(true);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
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
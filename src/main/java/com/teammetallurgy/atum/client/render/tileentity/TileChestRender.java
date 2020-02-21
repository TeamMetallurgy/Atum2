package com.teammetallurgy.atum.client.render.tileentity;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.GlStateManager;
import com.teammetallurgy.atum.blocks.base.ChestBaseBlock;
import com.teammetallurgy.atum.blocks.base.tileentity.ChestBaseTileEntity;
import com.teammetallurgy.atum.client.model.chest.SarcophagusModel;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.model.ChestModel;
import net.minecraft.client.renderer.tileentity.model.LargeChestModel;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class TileChestRender extends TileEntityRenderer<ChestBaseTileEntity> {
    private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();
    private final SarcophagusModel sarcophagus = new SarcophagusModel();
    private final ChestModel simpleChest = new ChestModel();
    private final ChestModel largeChest = new LargeChestModel();

    @Override
    public void render(@Nonnull ChestBaseTileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.enableDepthTest();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        BlockState state = te.hasWorld() ? te.getBlockState() : AtumBlocks.LIMESTONE_CHEST.getDefaultState().with(ChestBaseBlock.FACING, Direction.SOUTH);
        ChestType type = state.has(ChestBaseBlock.TYPE) ? state.get(ChestBaseBlock.TYPE) : ChestType.SINGLE;

        if (type != ChestType.LEFT) {
            boolean isDouble = type != ChestType.SINGLE;
            ChestModel modelChest = isDouble ? this.largeChest : this.simpleChest;
            if (destroyStage >= 0) {
                this.bindTexture(DESTROY_STAGES[destroyStage]);
                GlStateManager.matrixMode(5890);
                GlStateManager.pushMatrix();
                GlStateManager.scalef(isDouble ? 8.0F : 4.0F, 4.0F, 1.0F);
                GlStateManager.translatef(0.0625F, 0.0625F, 0.0625F);
                GlStateManager.matrixMode(5888);
            } else {
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                String name = Objects.requireNonNull(state.getBlock().getRegistryName()).getPath() + (isDouble ? "_double" : "");
                ResourceLocation chestTexture = CACHE.get(name);

                if (chestTexture == null) {
                    chestTexture = new ResourceLocation(Constants.MOD_ID, "textures/block/chest/" + name + ".png");
                    CACHE.put(name, chestTexture);
                }

                this.bindTexture(chestTexture);
            }

            GlStateManager.pushMatrix();
            GlStateManager.enableRescaleNormal();
            GlStateManager.translatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
            GlStateManager.scalef(1.0F, -1.0F, -1.0F);
            float angle = state.get(ChestBaseBlock.FACING).getHorizontalAngle();
            if ((double) Math.abs(angle) > 1.0E-5D) {
                GlStateManager.translatef(0.5F, 0.5F, 0.5F);
                GlStateManager.rotatef(angle, 0.0F, 1.0F, 0.0F);
                GlStateManager.translatef(-0.5F, -0.5F, -0.5F);
            }

            this.applyLidRotation(te, partialTicks, modelChest);
            modelChest.renderAll();
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

    private void applyLidRotation(@Nonnull ChestBaseTileEntity te, float partialTicks, ChestModel model) {
        float lidAngle = ((IChestLid) te).getLidAngle(partialTicks);
        lidAngle = 1.0F - lidAngle;
        lidAngle = 1.0F - lidAngle * lidAngle * lidAngle;
        model.getLid().rotateAngleX = -(lidAngle * 1.5707964F);
    }
}
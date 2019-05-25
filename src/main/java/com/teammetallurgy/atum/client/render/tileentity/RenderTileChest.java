package com.teammetallurgy.atum.client.render.tileentity;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.blocks.base.BlockChestBase;
import com.teammetallurgy.atum.blocks.base.tileentity.TileEntityChestBase;
import com.teammetallurgy.atum.client.model.chest.ModelSarcophagus;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;

@SideOnly(Side.CLIENT)
public class RenderTileChest extends TileEntitySpecialRenderer<TileEntityChestBase> {
    private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();

    private final ModelSarcophagus sarcophagus = new ModelSarcophagus();
    private final ModelChest normalChest = new ModelChest();
    private final ModelChest largeChest = new ModelLargeChest();

    @Override
    public void render(@Nonnull TileEntityChestBase te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        int meta;

        if (te.hasWorld()) {
            Block block = te.getBlockType();
            meta = te.getBlockMetadata();
            if (block instanceof BlockChestBase && meta == 0) {
                ((BlockChestBase) block).checkForSurroundingChests(te.getWorld(), te.getPos(), te.getWorld().getBlockState(te.getPos()));
                meta = te.getBlockMetadata();
            }
            te.checkForAdjacentChests();
        } else {
            meta = 0;
        }

        if (te.adjacentChestZNeg == null && te.adjacentChestXNeg == null) {
            ModelChest modelchest;

            if (te.adjacentChestXPos == null && te.adjacentChestZPos == null) {
                modelchest = this.normalChest;

                if (destroyStage >= 0) {
                    this.bindTexture(DESTROY_STAGES[destroyStage]);
                    GlStateManager.matrixMode(5890);
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(4.0F, 4.0F, 1.0F);
                    GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
                    GlStateManager.matrixMode(5888);
                } else {
                    String name = Objects.requireNonNull(te.getBlockType().getRegistryName()).getPath();
                    ResourceLocation chestTexture = CACHE.get(name);

                    if (chestTexture == null){
                        chestTexture = new ResourceLocation(Constants.MOD_ID, "textures/blocks/chest/" + name + ".png");
                        CACHE.put(name, chestTexture);
                    }

                    this.bindTexture(chestTexture);
                }
            } else {
                modelchest = this.largeChest;

                if (destroyStage >= 0) {
                    this.bindTexture(DESTROY_STAGES[destroyStage]);
                    GlStateManager.matrixMode(5890);
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(8.0F, 4.0F, 1.0F);
                    GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
                    GlStateManager.matrixMode(5888);
                } else {
                    String name = Objects.requireNonNull(te.getBlockType().getRegistryName()).getPath() + "_double";
                    ResourceLocation chestTexture = CACHE.get(name);

                    if (chestTexture == null){
                        chestTexture = new ResourceLocation(Constants.MOD_ID, "textures/blocks/chest/" + name + ".png");
                        CACHE.put(name, chestTexture);
                    }

                    this.bindTexture(chestTexture);
                }
            }

            GlStateManager.pushMatrix();
            GlStateManager.enableRescaleNormal();

            if (destroyStage < 0) {
                GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
            }

            GlStateManager.translate((float) x, (float) y + 1.0F, (float) z + 1.0F);
            GlStateManager.scale(1.0F, -1.0F, -1.0F);
            GlStateManager.translate(0.5F, 0.5F, 0.5F);
            int angle = 0;

            if (meta == 2) {
                angle = 180;
            }

            if (meta == 3) {
                angle = 0;
            }

            if (meta == 4) {
                angle = 90;
            }

            if (meta == 5) {
                angle = -90;
            }

            if (meta == 2 && te.adjacentChestXPos != null) {
                GlStateManager.translate(1.0F, 0.0F, 0.0F);
            }

            if (meta == 5 && te.adjacentChestZPos != null) {
                GlStateManager.translate(0.0F, 0.0F, -1.0F);
            }

            GlStateManager.rotate((float) angle, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(-0.5F, -0.5F, -0.5F);
            float lid = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;

            if (te.adjacentChestZNeg != null) {
                float adjacentLid = te.adjacentChestZNeg.prevLidAngle + (te.adjacentChestZNeg.lidAngle - te.adjacentChestZNeg.prevLidAngle) * partialTicks;
                if (adjacentLid > lid) {
                    lid = adjacentLid;
                }
            }

            if (te.adjacentChestXNeg != null) {
                float adjacentLid = te.adjacentChestXNeg.prevLidAngle + (te.adjacentChestXNeg.lidAngle - te.adjacentChestXNeg.prevLidAngle) * partialTicks;
                if (adjacentLid > lid) {
                    lid = adjacentLid;
                }
            }

            lid = 1.0F - lid;
            lid = 1.0F - lid * lid * lid;
            if (te.getBlockType() == AtumBlocks.SARCOPHAGUS) {
                GlStateManager.translate(1.0F, -0.5F, 0.5F);
                sarcophagus.lid.rotateAngleY = -lid / 2;
                sarcophagus.liddeco1.rotateAngleY = -lid / 2;
                sarcophagus.liddeco2.rotateAngleY = -lid / 2;
                sarcophagus.liddeco3.rotateAngleY = -lid / 2;
                sarcophagus.gemhead.rotateAngleY = -lid / 2;
                sarcophagus.gemchest.rotateAngleY = -lid / 2;
                sarcophagus.renderAll();
            } else {
                modelchest.chestLid.rotateAngleX = -(lid * ((float) Math.PI / 2F));
                modelchest.renderAll();
            }
            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if (destroyStage >= 0) {
                GlStateManager.matrixMode(5890);
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5888);
            }
        }
    }
}
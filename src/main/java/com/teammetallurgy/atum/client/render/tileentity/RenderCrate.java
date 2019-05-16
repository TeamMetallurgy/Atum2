package com.teammetallurgy.atum.client.render.tileentity;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.blocks.wood.BlockCrate;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.TileEntityCrate;
import com.teammetallurgy.atum.client.model.chest.ModelCrate;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;

@SideOnly(Side.CLIENT)
public class RenderCrate extends TileEntitySpecialRenderer<TileEntityCrate> {
    private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();
    private final ModelCrate modelCrate = new ModelCrate();

    @Override
    public void render(@Nonnull TileEntityCrate te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        int meta;

        if (te.hasWorld()) {
            Block block = te.getBlockType();
            meta = te.getBlockMetadata();
            if (block instanceof BlockCrate && meta == 0) {
                meta = te.getBlockMetadata();
            }
        } else {
            meta = 0;
        }

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

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();

        if (destroyStage < 0) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
        }

        GlStateManager.translate((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        GlStateManager.scale(1.0F, -1.0F, -1.0F);
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

        GlStateManager.rotate((float) angle, 0.0F, 1.0F, 0.0F);
        float lid = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;
        lid = 1.0F - lid;
        lid = 1.0F - lid * lid * lid;

        modelCrate.crateLid.rotateAngleY = (lid * ((float) Math.PI / 3.5F));
        modelCrate.renderAll();
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
package com.teammetallurgy.atum.client.render.tileentity;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.GlStateManager;
import com.teammetallurgy.atum.blocks.wood.CrateBlock;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.CrateTileEntity;
import com.teammetallurgy.atum.client.model.chest.CrateModel;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class CrateRender extends TileEntityRenderer<CrateTileEntity> {
    private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();
    private final CrateModel modelCrate = new CrateModel();

    public CrateRender(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(@Nonnull CrateTileEntity crate, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.enableDepthTest();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        BlockState state = crate.hasWorld() ? crate.getBlockState() : AtumBlocks.PALM_CRATE.getDefaultState().with(CrateBlock.FACING, Direction.SOUTH);

        if (destroyStage >= 0) {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(GL11.GL_TEXTURE);
            GlStateManager.pushMatrix();
            GlStateManager.scalef(4.0F, 4.0F, 1.0F);
            GlStateManager.translatef(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        } else {
            String name = Objects.requireNonNull(crate.getBlockState().getBlock().getRegistryName()).getPath();
            ResourceLocation chestTexture = CACHE.get(name);

            if (chestTexture == null) {
                chestTexture = new ResourceLocation(Constants.MOD_ID, "textures/block/chest/" + name + ".png");
                CACHE.put(name, chestTexture);
            }
            this.bindTexture(chestTexture);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.translatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        GlStateManager.scalef(1.0F, -1.0F, -1.0F);
        Direction direction = state.get(CrateBlock.FACING);
        if ((double) Math.abs(direction.getHorizontalAngle()) > 1.0E-5D) {
            GlStateManager.rotatef(direction.getHorizontalAngle(), 0.0F, 1.0F, 0.0F);
        }

        this.applyLidRotation(crate, partialTicks, this.modelCrate);
        this.modelCrate.renderAll();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }

    private void applyLidRotation(CrateTileEntity crate, float partialTicks, CrateModel crateModel) {
        float rotation = ((IChestLid) crate).getLidAngle(partialTicks);
        rotation = 1.0F - rotation;
        rotation = 1.0F - rotation * rotation * rotation;
        crateModel.crateLid.rotateAngleY = -(rotation * 1.5707964F);
    }
}
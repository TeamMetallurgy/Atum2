package com.teammetallurgy.atum.client.render.tileentity;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.teammetallurgy.atum.blocks.wood.CrateBlock;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.CrateTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.DualBrightnessCallback;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntityMerger;
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
    private ModelRenderer crateCore;
    private ModelRenderer crateLid;

    public CrateRender(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
        this.crateCore = new ModelRenderer(64, 64, 0, 0);
        this.crateCore.setRotationPoint(8.0F, 9.0F, 0.0F);
        this.crateCore.addBox(-16.0F, 0.0F, -8.0F, 16, 15, 16, 0.0F);
        this.crateLid = new ModelRenderer(64, 64, 0, 32);
        this.crateLid.setRotationPoint(8.0F, 8.0F, 0.0F);
        this.crateLid.addBox(-16.0F, 0.0F, -8.0F, 16, 1, 16, 0.0F);
    }

    @Override
    public void render(@Nonnull CrateTileEntity crate, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        BlockState state = crate.getWorld() != null ? crate.getBlockState() : AtumBlocks.PALM_CRATE.getDefaultState().with(CrateBlock.FACING, Direction.SOUTH);
        Block block = state.getBlock();
        matrixStack.push();
        float facingAngle = state.get(CrateBlock.FACING).getHorizontalAngle();
        matrixStack.translate(0.5D, 0.5D, 0.5D);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(-facingAngle));
        matrixStack.translate(-0.5D, -0.5D, -0.5D);
        TileEntityMerger.ICallbackWrapper<? extends ChestTileEntity> callbackWrapper = TileEntityMerger.ICallback::func_225537_b_;
        float lidAngle = crate.getLidAngle(partialTicks);
        lidAngle = 1.0F - lidAngle;
        lidAngle = 1.0F - lidAngle * lidAngle * lidAngle;
        int light = callbackWrapper.apply(new DualBrightnessCallback<>()).applyAsInt(combinedLight);
        IVertexBuilder vertexBuilder = getBuilder(crate, buffer);
        this.renderCrate(matrixStack, vertexBuilder, this.crateCore, this.crateLid, lidAngle, light, combinedOverlay);

        matrixStack.pop();
    }

    private void renderCrate(MatrixStack matrixStack, IVertexBuilder vertexBuilder, ModelRenderer core, ModelRenderer lid, float lidAngle, int light, int combinedOverlay) {
        lid.rotateAngleY = (lidAngle * ((float) Math.PI / 3.5F));
        core.render(matrixStack, vertexBuilder, light, combinedOverlay);
        lid.render(matrixStack, vertexBuilder, light, combinedOverlay);
    }

    private IVertexBuilder getBuilder(@Nonnull CrateTileEntity crate, @Nonnull IRenderTypeBuffer buffer) {
        String name = Objects.requireNonNull(crate.getBlockState().getBlock().getRegistryName()).getPath();
        ResourceLocation crateTexture = CACHE.get(name);

        if (crateTexture == null) {
            crateTexture = new ResourceLocation(Constants.MOD_ID, "textures/entity/chest/" + name + ".png");
            CACHE.put(name, crateTexture);
        }
        return buffer.getBuffer(RenderType.getEntityCutout(crateTexture));
    }
}
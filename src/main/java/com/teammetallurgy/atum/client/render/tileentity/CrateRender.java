package com.teammetallurgy.atum.client.render.tileentity;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.teammetallurgy.atum.blocks.wood.CrateBlock;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.CrateTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.utils.Constants;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.DualBrightnessCallback;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.IChestLid;
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
        BlockState state = crate.hasWorld() ? crate.getBlockState() : AtumBlocks.PALM_CRATE.getDefaultState().with(CrateBlock.FACING, Direction.SOUTH);
        String name = Objects.requireNonNull(crate.getBlockState().getBlock().getRegistryName()).getPath();
        ResourceLocation crateTexture = CACHE.get(name);

        if (crateTexture == null) {
            crateTexture = new ResourceLocation(Constants.MOD_ID, "textures/block/chest/" + name + ".png");
            CACHE.put(name, crateTexture);
        }

        matrixStack.push();
        matrixStack.translate(0.5F, 1.5F, 0.5F);
        matrixStack.scale(1.0F, -1.0F, -1.0F);
        Direction direction = state.get(CrateBlock.FACING);
        if ((double) Math.abs(direction.getHorizontalAngle()) > 1.0E-5D) {
            RenderSystem.rotatef(direction.getHorizontalAngle(), 0.0F, 1.0F, 0.0F);
        }

        this.applyLidRotation(crate, partialTicks);
        IVertexBuilder builder = buffer.getBuffer(RenderType.getEntityCutout(crateTexture));
        TileEntityMerger.ICallbackWrapper<?> callbackWrapper = TileEntityMerger.ICallback::func_225537_b_;
        int brightness = ((Int2IntFunction) callbackWrapper.apply(new DualBrightnessCallback())).applyAsInt(combinedLight);
        this.crateCore.render(matrixStack, builder, brightness, combinedOverlay);
        this.crateLid.render(matrixStack, builder, brightness, combinedOverlay);
        matrixStack.pop();
    }

    private void applyLidRotation(CrateTileEntity crate, float partialTicks) {
        float rotation = ((IChestLid) crate).getLidAngle(partialTicks);
        rotation = 1.0F - rotation;
        rotation = 1.0F - rotation * rotation * rotation;
        crateLid.rotateAngleY = -(rotation * 1.5707964F);
    }
}
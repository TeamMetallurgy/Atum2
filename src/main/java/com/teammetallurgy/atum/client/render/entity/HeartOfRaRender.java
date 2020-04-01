package com.teammetallurgy.atum.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.teammetallurgy.atum.entity.HeartOfRaEntity;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class HeartOfRaRender extends EntityRenderer<HeartOfRaEntity> {
    private static final ResourceLocation HEART_OF_RA_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/entity/heart_of_ra.png");
    private static final RenderType RENDER_TYPE = RenderType.getEntityCutoutNoCull(HEART_OF_RA_TEXTURE);
    private static final float CONSTANT = (float) Math.sin(0.7853981633974483D);
    private final ModelRenderer cube;
    private final ModelRenderer glass;
    private final ModelRenderer base;

    public HeartOfRaRender(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowSize = 0.5F;
        this.glass = new ModelRenderer(64, 32, 0, 0);
        this.glass.addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F);
        this.cube = new ModelRenderer(64, 32, 32, 0);
        this.cube.addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F);
        this.base = new ModelRenderer(64, 32, 0, 16);
        this.base.addBox(-6.0F, 0.0F, -6.0F, 12.0F, 4.0F, 12.0F);
    }

    @Override
    public void render(@Nonnull HeartOfRaEntity entity, float entityYaw, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int i) {
        matrixStack.push();
        float lvt_7_1_ = rotate(entity, partialTicks);
        float lvt_8_1_ = ((float) entity.innerRotation + partialTicks) * 3.0F;
        IVertexBuilder vertextBuilder = buffer.getBuffer(RENDER_TYPE);
        matrixStack.push();
        matrixStack.scale(2.0F, 2.0F, 2.0F);
        matrixStack.translate(0.0D, -0.1D, 0.0D);
        int overlay = OverlayTexture.NO_OVERLAY;
        this.base.render(matrixStack, vertextBuilder, i, overlay);

        matrixStack.rotate(Vector3f.YP.rotationDegrees(lvt_8_1_));
        matrixStack.translate(0.0D, (1.5F + lvt_7_1_ / 2.0F), 0.0D);
        matrixStack.rotate(new Quaternion(new Vector3f(CONSTANT, 0.0F, CONSTANT), 60.0F, true));
        this.glass.render(matrixStack, vertextBuilder, i, overlay);
        matrixStack.scale(0.875F, 0.875F, 0.875F);
        matrixStack.rotate(new Quaternion(new Vector3f(CONSTANT, 0.0F, CONSTANT), 60.0F, true));
        matrixStack.rotate(Vector3f.YP.rotationDegrees(lvt_8_1_));
        this.glass.render(matrixStack, vertextBuilder, i, overlay);
        matrixStack.scale(0.875F, 0.875F, 0.875F);
        matrixStack.rotate(new Quaternion(new Vector3f(CONSTANT, 0.0F, CONSTANT), 60.0F, true));
        matrixStack.rotate(Vector3f.YP.rotationDegrees(lvt_8_1_));
        this.cube.render(matrixStack, vertextBuilder, i, overlay);
        matrixStack.pop();
        matrixStack.pop();

        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, i);
    }

    public static float rotate(HeartOfRaEntity heartOfRa, float rotationModifier) {
        float rotation = (float) heartOfRa.innerRotation + rotationModifier;
        float rotationModified = MathHelper.sin(rotation * 0.2F) / 2.0F + 0.5F;
        rotationModified = (rotationModified * rotationModified + rotationModified) * 0.4F;
        return rotationModified - 1.4F;
    }

    @Override
    @Nonnull
    public ResourceLocation getEntityTexture(@Nonnull HeartOfRaEntity entity) {
        return HEART_OF_RA_TEXTURE;
    }
}
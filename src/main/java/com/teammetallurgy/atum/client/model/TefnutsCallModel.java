package com.teammetallurgy.atum.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.teammetallurgy.atum.Atum;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class TefnutsCallModel extends Model {
    public static final ResourceLocation TEFNUTS_CALL_TEXTURE = new ResourceLocation(Atum.MOD_ID, "textures/entity/tefnuts_call.png");
    public final ModelRenderer base;

    public TefnutsCallModel() {
        super(RenderType::getEntitySolid);
        this.textureHeight = 32;
        this.textureWidth = 32;
        ModelRenderer mr1 = new ModelRenderer(this, 12, 0);
        mr1.setRotationPoint(0.0F, 0.0F, 0.0F);
        mr1.addBox(-2.5F, -1.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        ModelRenderer mr2 = new ModelRenderer(this, 4, 0);
        mr2.setRotationPoint(0.0F, 0.0F, 0.0F);
        mr2.addBox(-1.5F, -2.0F, -0.5F, 3.0F, 4.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        ModelRenderer mr3 = new ModelRenderer(this, 16, 0);
        mr3.setRotationPoint(0.0F, 0.0F, 0.0F);
        mr3.addBox(-0.5F, -4.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.base = new ModelRenderer(this, 0, 0);
        this.base.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.base.addBox(-0.5F, 2.0F, -0.5F, 1.0F, 25.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        ModelRenderer mr4 = new ModelRenderer(this, 20, 0);
        mr4.mirror = true;
        mr4.setRotationPoint(0.0F, 0.0F, 0.0F);
        mr4.addBox(1.5F, -1.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.base.addChild(mr1);
        this.base.addChild(mr2);
        this.base.addChild(mr3);
        this.base.addChild(mr4);
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, @Nonnull IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.base.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
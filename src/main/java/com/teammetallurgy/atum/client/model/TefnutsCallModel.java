package com.teammetallurgy.atum.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teammetallurgy.atum.Atum;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class TefnutsCallModel extends Model {
    public static final ResourceLocation TEFNUTS_CALL_TEXTURE = new ResourceLocation(Atum.MOD_ID, "textures/entity/tefnuts_call.png");
    public final ModelPart base;

    public TefnutsCallModel() {
        super(RenderType::entitySolid);
        this.texHeight = 32;
        this.texWidth = 32;
        ModelPart mr1 = new ModelPart(this, 12, 0);
        mr1.setPos(0.0F, 0.0F, 0.0F);
        mr1.addBox(-2.5F, -1.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        ModelPart mr2 = new ModelPart(this, 4, 0);
        mr2.setPos(0.0F, 0.0F, 0.0F);
        mr2.addBox(-1.5F, -2.0F, -0.5F, 3.0F, 4.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        ModelPart mr3 = new ModelPart(this, 16, 0);
        mr3.setPos(0.0F, 0.0F, 0.0F);
        mr3.addBox(-0.5F, -4.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.base = new ModelPart(this, 0, 0);
        this.base.setPos(0.0F, 0.0F, 0.0F);
        this.base.addBox(-0.5F, 2.0F, -0.5F, 1.0F, 25.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        ModelPart mr4 = new ModelPart(this, 20, 0);
        mr4.mirror = true;
        mr4.setPos(0.0F, 0.0F, 0.0F);
        mr4.addBox(1.5F, -1.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.base.addChild(mr1);
        this.base.addChild(mr2);
        this.base.addChild(mr3);
        this.base.addChild(mr4);
    }

    @Override
    public void renderToBuffer(@Nonnull PoseStack matrixStack, @Nonnull VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.base.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
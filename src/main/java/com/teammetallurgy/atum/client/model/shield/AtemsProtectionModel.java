package com.teammetallurgy.atum.client.model.shield;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class AtemsProtectionModel extends AbstractShieldModel {
    private final ModelPart shieldCore;
    private final ModelPart handleCore;
    private final ModelPart gemStone;

    public AtemsProtectionModel(ModelPart part) {
        this.shieldCore = part.getChild("shield_core");
        this.handleCore = part.getChild("handle_core");
        this.gemStone = part.getChild("gem_stone");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition shieldCore = partdefinition.addOrReplaceChild("shield_core", CubeListBuilder.create().texOffs(0, 10).addBox(-5.0F, -2.0F, 0.0F, 10.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        shieldCore.addOrReplaceChild("shield_bottom_4", CubeListBuilder.create().texOffs(0, 22).addBox(-4.0F, 4.0F, 0.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 1.0F, 0.0F));
        shieldCore.addOrReplaceChild("shield_bottom_3", CubeListBuilder.create().texOffs(0, 4).addBox(-4.0F, 4.0F, 0.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        shieldCore.addOrReplaceChild("shield_bottom_1", CubeListBuilder.create().texOffs(0, 16).addBox(-6.0F, 2.0F, 0.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        shieldCore.addOrReplaceChild("shield_top_2", CubeListBuilder.create().texOffs(0, 6).addBox(-5.0F, -4.0F, 0.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        shieldCore.addOrReplaceChild("shield_bottom_2", CubeListBuilder.create().texOffs(0, 18).addBox(-5.0F, 3.0F, 0.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        shieldCore.addOrReplaceChild("shield_top_4", CubeListBuilder.create().texOffs(0, 2).addBox(-3.0F, -6.0F, 0.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        shieldCore.addOrReplaceChild("shield_top_3", CubeListBuilder.create().texOffs(0, 4).addBox(-4.0F, -5.0F, 0.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        shieldCore.addOrReplaceChild("shield_top_1", CubeListBuilder.create().texOffs(0, 8).addBox(-6.0F, -3.0F, 0.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition handleCore = partdefinition.addOrReplaceChild("handle_core", CubeListBuilder.create().texOffs(20, 0).addBox(-1.0F, -2.0F, 3.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        handleCore.addOrReplaceChild("handle_side_1", CubeListBuilder.create().texOffs(25, 3).addBox(-1.0F, 1.0F, 1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        handleCore.addOrReplaceChild("handle_side_2", CubeListBuilder.create().texOffs(25, 3).addBox(-1.0F, -2.0F, 1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("gem_stone", CubeListBuilder.create().texOffs(0, 24).addBox(-2.0F, -2.0F, -0.75F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.75F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void renderToBuffer(@Nonnull PoseStack matrixStack, @Nonnull VertexConsumer vertexBuilder, int i, int i1, float v, float v1, float v2, float v3) {
        matrixStack.pushPose();
        matrixStack.scale(1.0F / 0.75F, -1.0F / 0.75F, -1.0F / 0.75F);
        matrixStack.translate(0.0F, 0.0F, -0.03F);
        this.handleCore.render(matrixStack, vertexBuilder, i, i1, v, v1, v2, v3);
        this.shieldCore.render(matrixStack, vertexBuilder, i, i1, v, v1, v2, v3);
        this.gemStone.render(matrixStack, vertexBuilder, i, i1, v, v1, v2, v3);
        matrixStack.popPose();
    }
}
package com.teammetallurgy.atum.client.model.shield;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class BrigandShieldModel extends AbstractShieldModel {
    private final ModelPart shieldCore;
    private final ModelPart handleCore;

    public BrigandShieldModel(ModelPart part) {
        this.shieldCore = part.getChild("shield_core");
        this.handleCore = part.getChild("handle_core");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition shieldCore = partdefinition.addOrReplaceChild("shield_core", CubeListBuilder.create().texOffs(0, 10).addBox(-6.0F, -2.0F, 0.0F, 12.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        shieldCore.addOrReplaceChild("shield_bottom_2", CubeListBuilder.create().texOffs(0, 18).addBox(-5.0F, 3.0F, 0.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        shieldCore.addOrReplaceChild("shield_top_2", CubeListBuilder.create().texOffs(0, 6).addBox(-5.0F, -4.0F, 0.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        shieldCore.addOrReplaceChild("shield_top_1", CubeListBuilder.create().texOffs(0, 8).addBox(-6.0F, -3.0F, 0.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        shieldCore.addOrReplaceChild("shield_bottom_3", CubeListBuilder.create().texOffs(0, 20).addBox(-4.0F, 4.0F, 0.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        shieldCore.addOrReplaceChild("shield_top_3", CubeListBuilder.create().texOffs(0, 4).addBox(-4.0F, -5.0F, 0.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        shieldCore.addOrReplaceChild("shield_bottom_1", CubeListBuilder.create().texOffs(0, 16).addBox(-6.0F, 2.0F, 0.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        shieldCore.addOrReplaceChild("shield_bottom_4", CubeListBuilder.create().texOffs(0, 22).addBox(-4.0F, 4.0F, 0.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 1.0F, 0.0F));
        shieldCore.addOrReplaceChild("shield_top_4", CubeListBuilder.create().texOffs(0, 2).addBox(-3.0F, -6.0F, 0.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition handleCore = partdefinition.addOrReplaceChild("handle_core", CubeListBuilder.create().texOffs(20, 0).addBox(-1.0F, -2.0F, 3.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        handleCore.addOrReplaceChild("handle_side_2", CubeListBuilder.create().texOffs(25, 3).addBox(-1.0F, -2.0F, 1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        handleCore.addOrReplaceChild("handle_side_1", CubeListBuilder.create().texOffs(25, 3).addBox(-1.0F, 1.0F, 1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("shield_center", CubeListBuilder.create().texOffs(0, 25).addBox(-2.0F, -2.0F, -1.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void renderToBuffer(@Nonnull PoseStack poseStack, @Nonnull VertexConsumer vertexBuilder, int i, int i1, float v, float v1, float v2, float v3) {
        poseStack.pushPose();
        poseStack.scale(1.0F / 0.78F, -1.0F / 0.78F, -1.0F / 0.78F);
        poseStack.translate(0.0F, 0.0F, -0.025F);
        this.handleCore.render(poseStack, vertexBuilder, i, i1, v, v1, v2, v3);
        this.shieldCore.render(poseStack, vertexBuilder, i, i1, v, v1, v2, v3);
        poseStack.popPose();
    }
}
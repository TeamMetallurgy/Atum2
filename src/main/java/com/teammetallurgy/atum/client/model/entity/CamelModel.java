package com.teammetallurgy.atum.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teammetallurgy.atum.entity.animal.CamelEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;

public class CamelModel<T extends CamelEntity> extends EntityModel<T> {
    private final ModelPart body;
    private final ModelPart legBackLeft;
    private final ModelPart legFrontLeft;
    private final ModelPart legBackRight;
    private final ModelPart legFrontRight;
    private final ModelPart chestRight;
    private final ModelPart chestLeft;
    private final ModelPart head;
    private final ModelPart saddle2;
    private final ModelPart tail;
    private final ModelPart saddle1;

    public CamelModel(ModelPart part) {
        this.body = part.getChild("body");
        this.legBackLeft = part.getChild("leg_back_left");
        this.legFrontLeft = part.getChild("leg_front_left");
        this.legBackRight = part.getChild("leg_back_right");
        this.legFrontRight = part.getChild("leg_front_right");
        this.chestRight = part.getChild("chest_right");
        this.chestLeft = part.getChild("chest_left");
        this.head = part.getChild("head");
        this.saddle2 = part.getChild("saddle2");
        this.tail = part.getChild("tail");
        this.saddle1 = part.getChild("saddle1");
    }

    public static MeshDefinition createMesh(CubeDeformation cubeDeformation) {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(29, 0).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F, cubeDeformation), PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, 1.5708F, 0.0F, 0.0F));
        body.addOrReplaceChild("hump1", CubeListBuilder.create().texOffs(74, 0).addBox(-4.0F, -6.0F, 3.0F, 8.0F, 12.0F, 3.0F, cubeDeformation), PartPose.offset(0.0F, 0.0F, 0.0F));
        body.addOrReplaceChild("hump2", CubeListBuilder.create().texOffs(74, 16).addBox(-3.0F, -5.0F, 6.0F, 6.0F, 10.0F, 2.0F, cubeDeformation), PartPose.offset(0.0F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("leg_back_left", CubeListBuilder.create().texOffs(29, 29).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 14.0F, 4.0F, cubeDeformation), PartPose.offset(-3.5F, 10.0F, 6.0F));
        partdefinition.addOrReplaceChild("leg_front_left", CubeListBuilder.create().texOffs(29, 29).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 14.0F, 4.0F, cubeDeformation), PartPose.offset(-3.5F, 10.0F, -5.0F));
        partdefinition.addOrReplaceChild("leg_back_right", CubeListBuilder.create().texOffs(29, 29).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 14.0F, 4.0F, cubeDeformation), PartPose.offset(3.5F, 10.0F, 6.0F));
        partdefinition.addOrReplaceChild("leg_front_right", CubeListBuilder.create().texOffs(29, 29).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 14.0F, 4.0F, cubeDeformation), PartPose.offset(3.5F, 10.0F, -5.0F));
        partdefinition.addOrReplaceChild("chest_right", CubeListBuilder.create().texOffs(45, 28).addBox(-5.0F, 0.0F, 0.0F, 8.0F, 8.0F, 3.0F, cubeDeformation), PartPose.offsetAndRotation(8.5F, 3.0F, 3.0F, 0.0F, -1.5708F, 0.0F));
        partdefinition.addOrReplaceChild("chest_left", CubeListBuilder.create().texOffs(45, 41).addBox(-5.0F, 0.0F, 0.0F, 8.0F, 8.0F, 3.0F, cubeDeformation), PartPose.offsetAndRotation(-5.5F, 3.0F, 3.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 14).addBox(-3.0F, -16.0F, -8.0F, 6.0F, 14.0F, 6.0F, cubeDeformation), PartPose.offset(0.0F, 7.0F, -8.0F));
        head.addOrReplaceChild("neckheadlower", CubeListBuilder.create().texOffs(68, 30).addBox(-4.0F, -4.0F, -5.0F, 8.0F, 6.0F, 6.0F, cubeDeformation), PartPose.offset(0.0F, 0.0F, 0.0F));
        head.addOrReplaceChild("snout", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -15.0F, -14.0F, 6.0F, 4.0F, 6.0F, cubeDeformation), PartPose.offset(0.0F, 0.0F, 0.0F));
        head.addOrReplaceChild("ear_r", CubeListBuilder.create().texOffs(30, 0).addBox(3.0F, -16.0F, -6.0F, 1.0F, 3.0F, 2.0F, cubeDeformation), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));
        head.addOrReplaceChild("ear_l", CubeListBuilder.create().texOffs(30, 5).addBox(-4.0F, -16.0F, -6.0F, 1.0F, 3.0F, 2.0F, cubeDeformation), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("saddle2", CubeListBuilder.create().texOffs(100, 48).addBox(-5.0F, 2.0F, 8.0F, 10.0F, 4.0F, 4.0F, cubeDeformation), PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, 1.5708F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(94, 9).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 1.0F, 14.0F, cubeDeformation), PartPose.offsetAndRotation(0.0F, 2.0F, 10.0F, -1.5708F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("saddle1", CubeListBuilder.create().texOffs(101, 32).addBox(-4.0F, -6.0F, 6.0F, 8.0F, 12.0F, 2.0F, cubeDeformation), PartPose.offsetAndRotation(0.0F, 4.9F, 2.0F, 1.5708F, 0.0F, 0.0F));
        return meshdefinition;
    }

    @Override
    public void renderToBuffer(@Nonnull PoseStack matrixStack, @Nonnull VertexConsumer vertexBuilder, int limbSwing, int limbSwingAmount, float ageInTicks, float netheadModelYaw, float headModelPitch, float scale) {
        if (this.young) {
            matrixStack.pushPose();
            matrixStack.scale(0.71428573F, 0.64935064F, 0.7936508F);
            matrixStack.translate(0.0D, 1.3125D, 0.2199999988079071D);
            this.head.render(matrixStack, vertexBuilder, limbSwing, limbSwingAmount, ageInTicks, netheadModelYaw, headModelPitch, scale);
            matrixStack.popPose();
            matrixStack.pushPose();
            matrixStack.scale(0.625F, 0.45454544F, 0.45454544F);
            matrixStack.translate(0.0D, 2.0625D, 0.0D);
            this.body.render(matrixStack, vertexBuilder, limbSwing, limbSwingAmount, ageInTicks, netheadModelYaw, headModelPitch, scale);
            matrixStack.popPose();
            matrixStack.pushPose();
            matrixStack.scale(0.45454544F, 0.41322312F, 0.45454544F);
            matrixStack.translate(0.0D, 2.0625D, 0.0D);
            ImmutableList.of(this.legBackRight, this.legBackLeft, this.legFrontRight, this.legFrontLeft, this.chestLeft, this.chestRight, this.saddle1, this.saddle2).forEach((model) -> {
                model.render(matrixStack, vertexBuilder, limbSwing, limbSwingAmount, ageInTicks, netheadModelYaw, headModelPitch, scale);
            });
            matrixStack.popPose();
        } else {
            ImmutableList.of(this.head, this.body, this.legBackRight, this.legBackLeft, this.legFrontRight, this.legFrontLeft, this.chestLeft, this.chestRight, this.saddle1, this.saddle2).forEach((model) -> {
                model.render(matrixStack, vertexBuilder, limbSwing, limbSwingAmount, ageInTicks, netheadModelYaw, headModelPitch, scale);
            });
        }
    }

    @Override
    public void setupAnim(@Nonnull T camel, float limbSwing, float limbSwingAmount, float ageInTicks, float netheadModelYaw, float headModelPitch) {
        limbSwingAmount *= CamelEntity.CAMEL_RIDING_SPEED_AMOUNT;
        if (camel.isVehicle()) {
            this.head.xRot = Mth.cos(limbSwing * 0.6662F) * 0.025F * limbSwingAmount;
            Vec3 motion = camel.getDeltaMovement();
            this.tail.xRot = -45.5F + (Mth.sqrt((float) (Math.pow(motion.x, 2) + Math.pow(motion.z, 2))));
        } else {
            this.tail.xRot = -45.5F;
        }
        this.tail.zRot = Mth.cos(limbSwing * 0.6662F) * 0.1F * limbSwingAmount;

        this.head.xRot = headModelPitch * 0.017453292F;
        this.head.yRot = netheadModelYaw * 0.017453292F;
        this.body.xRot = 1.5707964F;
        this.legBackRight.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.legBackLeft.xRot = Mth.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
        this.legFrontRight.xRot = Mth.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
        this.legFrontLeft.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

        boolean isChild = camel.isBaby();
        boolean isSaddled = !isChild && camel.isSaddled();
        this.saddle1.visible = isSaddled;
        this.saddle2.visible = isSaddled;
        this.chestLeft.visible = camel.hasLeftCrate();
        this.chestRight.visible = camel.hasRightCrate();
    }
}
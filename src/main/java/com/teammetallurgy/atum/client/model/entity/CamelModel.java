package com.teammetallurgy.atum.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teammetallurgy.atum.entity.animal.CamelEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;

public class CamelModel<T extends CamelEntity> extends EntityModel<T> {
    private final ModelPart headModel;
    private final ModelPart body;
    private final ModelPart legBackRight;
    private final ModelPart legBackLeft;
    private final ModelPart legFrontRight;
    private final ModelPart legFrontLeft;
    private final ModelPart neckheadModelLower;
    private final ModelPart snout;
    private final ModelPart earRight;
    private final ModelPart earLeft;
    private final ModelPart hump1;
    private final ModelPart hump2;
    private final ModelPart tail;
    private final ModelPart chestRight;
    private final ModelPart chestLeft;
    public final ModelPart saddle1;
    public final ModelPart saddle2;

    public CamelModel(ModelPart part) {
        this.headModel = part.getChild("headModel");
        this.body = part.getChild("body");
        this.legBackRight = part.getChild("legBackRight");
        this.legBackLeft = part.getChild("legBackLeft");
        this.legFrontRight = part.getChild("legFrontRight");
        this.legFrontLeft = part.getChild("legFrontLeft");
        this.neckheadModelLower = part.getChild("neckheadModelLower");
        this.snout = part.getChild("snout");
        this.earRight = part.getChild("earRight");
        this.earLeft = part.getChild("earLeft");
        this.hump1 = part.getChild("hump1");
        this.hump2 = part.getChild("hump2");
        this.tail = part.getChild("tail");
        this.chestRight = part.getChild("chestRight");
        this.chestLeft = part.getChild("chestLeft");
        this.saddle1 = part.getChild("saddle1");
        this.saddle2 = part.getChild("saddle2");

        /*this.texWidth = 128;//TODO Needs re-exporting in Blockbench
        this.texHeight = 64;
        this.chestLeft = new ModelPart(this, 45, 41);
        this.chestLeft.setPos(5.5F, 3.0F, 3.0F);
        this.chestLeft.addBox(-3.0F, 0.0F, 0.0F, 8, 8, 3, size);
        this.setRotateAngle(chestLeft, 0.0F, 1.5707963267948966F, size);
        this.tail = new ModelPart(this, 94, 9);
        this.tail.setPos(0.0F, 2.0F, 10.0F);
        this.tail.addBox(-1.5F, -1.0F, 0.0F, 3, 1, 14, size);
        this.setRotateAngle(tail, -1.5707963267948966F, 0.0F, 0.0F);
        this.snout = new ModelPart(this, 0, 0);
        this.snout.setPos(0.0F, -10.0F, -8.0F);
        this.snout.addBox(-3.0F, -5.0F, -6.0F, 6, 4, 6, size);
        this.body = new ModelPart(this, 29, 0);
        this.body.setPos(0.0F, 5.0F, 2.0F);
        this.body.addBox(-6.0F, -10.0F, -7.0F, 12, 18, 10, size);
        this.setRotateAngle(body, 1.5707963267948966F, 0.0F, 0.0F);
        this.hump2 = new ModelPart(this, 74, 16);
        this.hump2.setPos(0.0F, 0.0F, 0.0F);
        this.hump2.addBox(-3.0F, -5.0F, 6.0F, 6, 10, 2, size);
        this.headModel = new ModelPart(this, 0, 14);
        this.headModel.setPos(0.0F, 7.0F, -8.0F);
        this.headModel.addBox(-3.0F, -16.0F, -8.0F, 6, 14, 6, size);
        this.earRight = new ModelPart(this, 30, 0);
        this.earRight.setPos(0.0F, 0.0F, 0.0F);
        this.earRight.addBox(-4.0F, -16.0F, -6.0F, 1, 3, 2, size);
        this.setRotateAngle(earRight, -0.08726646259971647F, 0.0F, 0.0F);
        this.earLeft = new ModelPart(this, 30, 5);
        this.earLeft.setPos(0.0F, 0.0F, 0.0F);
        this.earLeft.addBox(3.0F, -16.0F, -6.0F, 1, 3, 2, size);
        this.setRotateAngle(earLeft, -0.08726646259971647F, 0.0F, 0.0F);
        this.chestRight = new ModelPart(this, 45, 28);
        this.chestRight.setPos(-8.5F, 3.0F, 3.0F);
        this.chestRight.addBox(-3.0F, 0.0F, 0.0F, 8, 8, 3, size);
        this.setRotateAngle(chestRight, 0.0F, 1.5707963267948966F, 0.0F);
        this.neckheadModelLower = new ModelPart(this, 68, 30);
        this.neckheadModelLower.setPos(0.0F, 0.0F, 0.0F);
        this.neckheadModelLower.addBox(-4.0F, -4.0F, -5.0F, 8, 6, 6, size);
        this.hump1 = new ModelPart(this, 74, 0);
        this.hump1.setPos(0.0F, 0.0F, 0.0F);
        this.hump1.addBox(-4.0F, -6.0F, 3.0F, 8, 12, 3, size);
        this.legBackRight = new ModelPart(this, 29, 29);
        this.legBackRight.setPos(3.5F, 10.0F, -5.0F);
        this.legBackRight.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4, size);
        this.legBackLeft = new ModelPart(this, 29, 29);
        this.legBackLeft.setPos(-3.5F, 10.0F, -5.0F);
        this.legBackLeft.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4, size);
        this.legFrontRight = new ModelPart(this, 29, 29);
        this.legFrontRight.setPos(3.5F, 10.0F, 6.0F);
        this.legFrontRight.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4, size);
        this.legFrontLeft = new ModelPart(this, 29, 29);
        this.legFrontLeft.setPos(-3.5F, 10.0F, 6.0F);
        this.legFrontLeft.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4, size);
        this.saddle1 = new ModelPart(this, 101, 32);
        this.saddle1.setPos(0.0F, 4.9F, 2.0F);
        this.saddle1.addBox(-4.0F, -6.0F, 6.0F, 8, 12, 2, size);
        this.setRotateAngle(saddle1, 1.5707963267948966F, 0.0F, 0.0F);
        this.saddle2 = new ModelPart(this, 100, 48);
        this.saddle2.setPos(0.0F, 5.0F, 2.0F);
        this.saddle2.addBox(-5.0F, 2.0F, 8.0F, 10, 4, 4, size);
        this.setRotateAngle(saddle2, 1.5707963267948966F, 0.0F, 0.0F);
        this.headModel.addChild(this.snout);
        this.headModel.addChild(this.earRight);
        this.headModel.addChild(this.earLeft);
        this.headModel.addChild(this.neckheadModelLower);
        this.body.addChild(this.hump1);
        this.body.addChild(this.hump2);*/
    }

    @Override
    public void renderToBuffer(@Nonnull PoseStack matrixStack, @Nonnull VertexConsumer vertexBuilder, int limbSwing, int limbSwingAmount, float ageInTicks, float netheadModelYaw, float headModelPitch, float scale) {
        if (this.young) {
            matrixStack.pushPose();
            matrixStack.scale(0.71428573F, 0.64935064F, 0.7936508F);
            matrixStack.translate(0.0D, 1.3125D, 0.2199999988079071D);
            this.headModel.render(matrixStack, vertexBuilder, limbSwing, limbSwingAmount, ageInTicks, netheadModelYaw, headModelPitch, scale);
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
            ImmutableList.of(this.headModel, this.body, this.legBackRight, this.legBackLeft, this.legFrontRight, this.legFrontLeft, this.chestLeft, this.chestRight, this.saddle1, this.saddle2).forEach((model) -> {
                model.render(matrixStack, vertexBuilder, limbSwing, limbSwingAmount, ageInTicks, netheadModelYaw, headModelPitch, scale);
            });
        }
    }

    @Override
    public void setupAnim(@Nonnull T camel, float limbSwing, float limbSwingAmount, float ageInTicks, float netheadModelYaw, float headModelPitch) {
        limbSwingAmount *= CamelEntity.CAMEL_RIDING_SPEED_AMOUNT;
        if (camel.isVehicle()) {
            this.headModel.xRot = Mth.cos(limbSwing * 0.6662F) * 0.025F * limbSwingAmount;
            Vec3 motion = camel.getDeltaMovement();
            this.tail.xRot = -45.5F + (Mth.sqrt((float) (Math.pow(motion.x, 2) + Math.pow(motion.z, 2))));
        } else {
            this.tail.xRot = -45.5F;
        }
        this.tail.zRot = Mth.cos(limbSwing * 0.6662F) * 0.1F * limbSwingAmount;

        this.headModel.xRot = headModelPitch * 0.017453292F;
        this.headModel.yRot = netheadModelYaw * 0.017453292F;
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

    private void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
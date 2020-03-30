package com.teammetallurgy.atum.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.teammetallurgy.atum.entity.animal.CamelEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;

public class CamelModel<T extends CamelEntity> extends EntityModel<T> { //TODO Fix model movement
    private ModelRenderer headModel;
    private ModelRenderer body;
    private ModelRenderer legBackRight;
    private ModelRenderer legBackLeft;
    private ModelRenderer legFrontRight;
    private ModelRenderer legFrontLeft;
    private ModelRenderer neckheadModelLower;
    private ModelRenderer snout;
    private ModelRenderer earRight;
    private ModelRenderer earLeft;
    private ModelRenderer hump1;
    private ModelRenderer hump2;
    private ModelRenderer tail;
    private ModelRenderer chestRight;
    private ModelRenderer chestLeft;
    public ModelRenderer saddle1;
    public ModelRenderer saddle2;

    public CamelModel() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.chestLeft = new ModelRenderer(this, 45, 41);
        this.chestLeft.setRotationPoint(5.5F, 3.0F, 3.0F);
        this.chestLeft.addBox(-3.0F, 0.0F, 0.0F, 8, 8, 3, 0.0F);
        this.setRotateAngle(chestLeft, 0.0F, 1.5707963267948966F, 0.0F);
        this.tail = new ModelRenderer(this, 94, 9);
        this.tail.setRotationPoint(0.0F, 2.0F, 10.0F);
        this.tail.addBox(-1.5F, -1.0F, 0.0F, 3, 1, 14, 0.0F);
        this.setRotateAngle(tail, -1.5707963267948966F, 0.0F, 0.0F);
        this.snout = new ModelRenderer(this, 0, 0);
        this.snout.setRotationPoint(0.0F, -10.0F, -8.0F);
        this.snout.addBox(-3.0F, -5.0F, -6.0F, 6, 4, 6, 0.0F);
        this.body = new ModelRenderer(this, 29, 0);
        this.body.setRotationPoint(0.0F, 5.0F, 2.0F);
        this.body.addBox(-6.0F, -10.0F, -7.0F, 12, 18, 10, 0.0F);
        this.setRotateAngle(body, 1.5707963267948966F, 0.0F, 0.0F);
        this.hump2 = new ModelRenderer(this, 74, 16);
        this.hump2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hump2.addBox(-3.0F, -5.0F, 6.0F, 6, 10, 2, 0.0F);
        this.headModel = new ModelRenderer(this, 0, 14);
        this.headModel.setRotationPoint(0.0F, 7.0F, -8.0F);
        this.headModel.addBox(-3.0F, -16.0F, -8.0F, 6, 14, 6, 0.0F);
        this.earRight = new ModelRenderer(this, 30, 0);
        this.earRight.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.earRight.addBox(-4.0F, -16.0F, -6.0F, 1, 3, 2, 0.0F);
        this.setRotateAngle(earRight, -0.08726646259971647F, 0.0F, 0.0F);
        this.earLeft = new ModelRenderer(this, 30, 5);
        this.earLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.earLeft.addBox(3.0F, -16.0F, -6.0F, 1, 3, 2, 0.0F);
        this.setRotateAngle(earLeft, -0.08726646259971647F, 0.0F, 0.0F);
        this.chestRight = new ModelRenderer(this, 45, 28);
        this.chestRight.setRotationPoint(-8.5F, 3.0F, 3.0F);
        this.chestRight.addBox(-3.0F, 0.0F, 0.0F, 8, 8, 3, 0.0F);
        this.setRotateAngle(chestRight, 0.0F, 1.5707963267948966F, 0.0F);
        this.neckheadModelLower = new ModelRenderer(this, 68, 30);
        this.neckheadModelLower.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.neckheadModelLower.addBox(-4.0F, -4.0F, -5.0F, 8, 6, 6, 0.0F);
        this.hump1 = new ModelRenderer(this, 74, 0);
        this.hump1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hump1.addBox(-4.0F, -6.0F, 3.0F, 8, 12, 3, 0.0F);
        this.legBackRight = new ModelRenderer(this, 29, 29);
        this.legBackRight.setRotationPoint(3.5F, 10.0F, -5.0F);
        this.legBackRight.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4, 0.0F);
        this.legBackLeft = new ModelRenderer(this, 29, 29);
        this.legBackLeft.setRotationPoint(-3.5F, 10.0F, -5.0F);
        this.legBackLeft.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4, 0.0F);
        this.legFrontRight = new ModelRenderer(this, 29, 29);
        this.legFrontRight.setRotationPoint(3.5F, 10.0F, 6.0F);
        this.legFrontRight.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4, 0.0F);
        this.legFrontLeft = new ModelRenderer(this, 29, 29);
        this.legFrontLeft.setRotationPoint(-3.5F, 10.0F, 6.0F);
        this.legFrontLeft.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4, 0.0F);
        this.saddle1 = new ModelRenderer(this, 101, 32);
        this.saddle1.setRotationPoint(0.0F, 4.9F, 2.0F);
        this.saddle1.addBox(-4.0F, -6.0F, 6.0F, 8, 12, 2, 0.0F);
        this.setRotateAngle(saddle1, 1.5707963267948966F, 0.0F, 0.0F);
        this.saddle2 = new ModelRenderer(this, 100, 48);
        this.saddle2.setRotationPoint(0.0F, 5.0F, 2.0F);
        this.saddle2.addBox(-5.0F, 2.0F, 8.0F, 10, 4, 4, 0.0F);
        this.setRotateAngle(saddle2, 1.5707963267948966F, 0.0F, 0.0F);
        this.headModel.addChild(this.snout);
        this.headModel.addChild(this.earRight);
        this.headModel.addChild(this.earLeft);
        this.headModel.addChild(this.neckheadModelLower);
        this.body.addChild(this.hump1);
        this.body.addChild(this.hump2);
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, @Nonnull IVertexBuilder vertexBuilder, int limbSwing, int limbSwingAmount, float ageInTicks, float netheadModelYaw, float headModelPitch, float scale) {
        if (this.isChild) {
            matrixStack.push();
            matrixStack.scale(0.71428573F, 0.64935064F, 0.7936508F);
            matrixStack.translate(0.0D, 1.3125D, 0.2199999988079071D);
            this.headModel.render(matrixStack, vertexBuilder, limbSwing, limbSwingAmount, ageInTicks, netheadModelYaw, headModelPitch, scale);
            matrixStack.pop();
            matrixStack.push();
            matrixStack.scale(0.625F, 0.45454544F, 0.45454544F);
            matrixStack.translate(0.0D, 2.0625D, 0.0D);
            this.body.render(matrixStack, vertexBuilder, limbSwing, limbSwingAmount, ageInTicks, netheadModelYaw, headModelPitch, scale);
            matrixStack.pop();
            matrixStack.push();
            matrixStack.scale(0.45454544F, 0.41322312F, 0.45454544F);
            matrixStack.translate(0.0D, 2.0625D, 0.0D);
            ImmutableList.of(this.legBackRight, this.legBackLeft, this.legFrontRight, this.legFrontLeft, this.chestLeft, this.chestRight, this.saddle1, this.saddle2).forEach((model) -> {
                model.render(matrixStack, vertexBuilder, limbSwing, limbSwingAmount, ageInTicks, netheadModelYaw, headModelPitch, scale);
            });
            matrixStack.pop();
        } else {
            ImmutableList.of(this.headModel, this.body, this.legBackRight, this.legBackLeft, this.legFrontRight, this.legFrontLeft, this.chestLeft, this.chestRight, this.saddle1, this.saddle2).forEach((model) -> {
                model.render(matrixStack, vertexBuilder, limbSwing, limbSwingAmount, ageInTicks, netheadModelYaw, headModelPitch, scale);
            });
        }
    }

    @Override
    public void setRotationAngles(@Nonnull T camel, float limbSwing, float limbSwingAmount, float ageInTicks, float netheadModelYaw, float headModelPitch) {
        limbSwingAmount *= CamelEntity.CAMEL_RIDING_SPEED_AMOUNT;
        if (camel.isBeingRidden()) {
            this.headModel.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 0.025F * limbSwingAmount;
            Vec3d motion = camel.getMotion();
            this.tail.rotateAngleX = -45.5F + (MathHelper.sqrt(Math.pow(motion.x, 2) + Math.pow(motion.z, 2)));
        } else {
            this.tail.rotateAngleX = -45.5F;
        }
        this.tail.rotateAngleZ = MathHelper.cos(limbSwing * 0.6662F) * 0.1F * limbSwingAmount;

        this.headModel.rotateAngleX = headModelPitch * 0.017453292F;
        this.headModel.rotateAngleY = netheadModelYaw * 0.017453292F;
        this.body.rotateAngleX = 1.5707964F;
        this.legBackRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.legBackLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
        this.legFrontRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
        this.legFrontLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

        boolean isChild = camel.isChild();
        boolean isSaddled = !isChild && camel.isHorseSaddled();
        this.saddle1.showModel = isSaddled;
        this.saddle2.showModel = isSaddled;
        this.chestLeft.showModel = camel.hasLeftCrate();
        this.chestRight.showModel = camel.hasRightCrate();
    }

    private void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
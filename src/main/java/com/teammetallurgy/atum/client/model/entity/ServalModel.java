package com.teammetallurgy.atum.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.teammetallurgy.atum.entity.animal.ServalEntity;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.entity.model.ModelUtils;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

public class ServalModel<T extends ServalEntity> extends AgeableModel<T> {
    private final ModelRenderer body;
    private final ModelRenderer body_r1;
    private final ModelRenderer head;
    private final ModelRenderer tail;
    private final ModelRenderer tail_r1;
    private final ModelRenderer backLeftLeg;
    private final ModelRenderer backRightLeg;
    private final ModelRenderer frontLeftLeg;
    private final ModelRenderer frontRightLeg;
    private float field_217155_m;
    private float field_217156_n;
    private float field_217157_o;
    protected int state = 1;

    public ServalModel(float size) {
        super(true, 10.0F, 4.0F);
        textureWidth = 64;
        textureHeight = 32;

        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, 17.0F, 1.0F);
        body.setTextureOffset(50, 21).addBox(2.0F, -5.0F, -8.0F, 1.0F, 5.0F, 6.0F, size, false);
        body.setTextureOffset(50, 10).addBox(-3.0F, -5.0F, -8.0F, 1.0F, 5.0F, 6.0F, size, false);
        body.setTextureOffset(26, 25).addBox(-3.0F, -6.0F, -8.0F, 6.0F, 1.0F, 6.0F, size, false);
        body.setTextureOffset(54, 6).addBox(-1.9F, -2.0F, -9.0F, 4.0F, 3.0F, 1.0F, size, false);

        body_r1 = new ModelRenderer(this);
        body_r1.setRotationPoint(0.0F, 7.0F, -1.0F);
        this.body.addChild(body_r1);
        setRotationAngle(body_r1, 1.5708F, 0.0F, 0.0F);
        body_r1.setTextureOffset(20, 0).addBox(-2.0F, -7.0F, 6.0F, 4.0F, 16.0F, 6.0F, size, false);

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, 13.0F, -9.0F);
        head.setTextureOffset(0, 0).addBox(-2.5F, -2.0F, -3.0F, 5.0F, 4.0F, 5.0F, size, false);
        head.setTextureOffset(0, 28).addBox(-1.5F, -0.0156F, -4.0F, 3.0F, 2.0F, 2.0F, size, false);
        head.setTextureOffset(26, 22).addBox(-2.0F, -6.0F, -1.0F, 1.0F, 4.0F, 2.0F, size, false);
        head.setTextureOffset(20, 22).addBox(1.0F, -6.0F, -1.0F, 1.0F, 4.0F, 2.0F, size, false);

        tail = new ModelRenderer(this);
        tail.setRotationPoint(0.0F, 15.0F, 8.0F);

        tail_r1 = new ModelRenderer(this);
        tail_r1.setRotationPoint(0.0F, 9.0F, -8.0F);
        this.tail.addChild(tail_r1);
        setRotationAngle(tail_r1, 0.6109F, 0.0F, 0.0F);
        tail_r1.setTextureOffset(0, 15).addBox(-0.5F, -5.0F, 13.0F, 1.0F, 11.0F, 1.0F, size, false);

        backLeftLeg = new ModelRenderer(this);
        backLeftLeg.setRotationPoint(1.1F, 18.0F, 7.0F);
        backLeftLeg.setTextureOffset(8, 13).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, size, false);

        backRightLeg = new ModelRenderer(this);
        backRightLeg.setRotationPoint(-1.1F, 18.0F, 7.0F);
        backRightLeg.setTextureOffset(8, 13).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, size, false);

        frontLeftLeg = new ModelRenderer(this);
        frontLeftLeg.setRotationPoint(1.2F, 14.0F, -4.0F);
        frontLeftLeg.setTextureOffset(40, 0).addBox(-1.0F, -0.2F, -1.0F, 2.0F, 10.0F, 2.0F, size, false);

        frontRightLeg = new ModelRenderer(this);
        frontRightLeg.setRotationPoint(-1.2F, 14.0F, -4.0F);
        frontRightLeg.setTextureOffset(40, 0).addBox(-1.0F, -0.2F, -1.0F, 2.0F, 10.0F, 2.0F, size, false);
    }

    @Override
    @Nonnull
    protected Iterable<ModelRenderer> getHeadParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    @Nonnull
    protected Iterable<ModelRenderer> getBodyParts() {
        return ImmutableList.of(this.body, this.backLeftLeg, this.backRightLeg, this.frontLeftLeg, this.frontRightLeg, this.tail);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setLivingAnimations(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
        this.field_217155_m = entity.func_213408_v(partialTick);
        this.field_217156_n = entity.func_213421_w(partialTick);
        this.field_217157_o = entity.func_213424_x(partialTick);
        if (this.field_217155_m <= 0.0F) {
            this.head.rotateAngleX = 0.0F;
            this.head.rotateAngleZ = 0.0F;
            this.frontLeftLeg.rotateAngleX = 0.0F;
            this.frontLeftLeg.rotateAngleZ = 0.0F;
            this.frontRightLeg.rotateAngleX = 0.0F;
            this.frontRightLeg.rotateAngleZ = 0.0F;
            this.frontRightLeg.rotationPointX = -1.2F;
            this.backLeftLeg.rotateAngleX = 0.0F;
            this.backRightLeg.rotateAngleX = 0.0F;
            this.backRightLeg.rotateAngleZ = 0.0F;
            this.backRightLeg.rotationPointX = -1.1F;
            this.backRightLeg.rotationPointY = 18.0F;
        }

        super.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTick);
        if (entity.isEntitySleeping()) {
            this.body.rotateAngleX = ((float) Math.PI / 4F);
            this.body.rotationPointY += -4.0F;
            this.body.rotationPointZ += 5.0F;
            this.head.rotationPointY += -3.3F;
            ++this.head.rotationPointZ;
            this.tail.rotationPointY += 8.0F;
            this.tail.rotationPointZ += -2.0F;
            this.tail.rotateAngleX = 1.7278761F;
            this.frontLeftLeg.rotateAngleX = -0.15707964F;
            this.frontLeftLeg.rotationPointY = 16.1F;
            this.frontLeftLeg.rotationPointZ = -7.0F;
            this.frontRightLeg.rotateAngleX = -0.15707964F;
            this.frontRightLeg.rotationPointY = 16.1F;
            this.frontRightLeg.rotationPointZ = -7.0F;
            this.backLeftLeg.rotateAngleX = (-(float) Math.PI / 2F);
            this.backLeftLeg.rotationPointY = 21.0F;
            this.backLeftLeg.rotationPointZ = 1.0F;
            this.backRightLeg.rotateAngleX = (-(float) Math.PI / 2F);
            this.backRightLeg.rotationPointY = 21.0F;
            this.backRightLeg.rotationPointZ = 1.0F;
            this.state = 3;
        }
    }

    @Override
    public void setRotationAngles(@Nonnull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.baseRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        if (this.field_217155_m > 0.0F) {
            this.head.rotateAngleZ = ModelUtils.func_228283_a_(this.head.rotateAngleZ, -1.2707963F, this.field_217155_m);
            this.head.rotateAngleY = ModelUtils.func_228283_a_(this.head.rotateAngleY, 1.2707963F, this.field_217155_m);
            this.frontLeftLeg.rotateAngleX = -1.2707963F;
            this.frontRightLeg.rotateAngleX = -0.47079635F;
            this.frontRightLeg.rotateAngleZ = -0.2F;
            this.frontRightLeg.rotationPointX = -0.2F;
            this.backLeftLeg.rotateAngleX = -0.4F;
            this.backRightLeg.rotateAngleX = 0.5F;
            this.backRightLeg.rotateAngleZ = -0.5F;
            this.backRightLeg.rotationPointX = -0.3F;
            this.backRightLeg.rotationPointY = 20.0F;
        }

        if (this.field_217157_o > 0.0F) {
            this.head.rotateAngleX = ModelUtils.func_228283_a_(this.head.rotateAngleX, -0.58177644F, this.field_217157_o);
        }
    }

    public void baseRotationAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.rotateAngleX = headPitch * ((float)Math.PI / 180F);
        this.head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
        if (this.state != 3) {
            this.body.rotateAngleX = ((float)Math.PI / 2F);
            if (this.state == 2) {
                this.backLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount;
                this.backRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + 0.3F) * limbSwingAmount;
                this.frontLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI + 0.3F) * limbSwingAmount;
                this.frontRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;
            } else {
                this.backLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount;
                this.backRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;
                this.frontLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;
                this.frontRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount;
            }
        }

    }
}
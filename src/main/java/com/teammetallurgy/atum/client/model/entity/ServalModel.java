package com.teammetallurgy.atum.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.teammetallurgy.atum.entity.animal.ServalEntity;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.ModelUtils;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

import javax.annotation.Nonnull;

public class ServalModel<T extends ServalEntity> extends AgeableListModel<T> {
    private final ModelPart body;
    private final ModelPart body_r1;
    private final ModelPart head;
    private final ModelPart tail;
    private final ModelPart tail_r1;
    private final ModelPart backLeftLeg;
    private final ModelPart backRightLeg;
    private final ModelPart frontLeftLeg;
    private final ModelPart frontRightLeg;
    private float lieDownAmount;
    private float lieDownAmountTail;
    private float relaxStateOneAmount;
    protected int state = 1;

    public ServalModel(float size) {
        super(true, 10.0F, 4.0F);
        texWidth = 64;
        texHeight = 32;

        body = new ModelPart(this);
        body.texOffs(50, 21).addBox(2.0F, -5.0F, -8.0F, 1.0F, 5.0F, 6.0F, size, false);
        body.texOffs(50, 10).addBox(-3.0F, -5.0F, -8.0F, 1.0F, 5.0F, 6.0F, size, false);
        body.texOffs(26, 25).addBox(-3.0F, -6.0F, -8.0F, 6.0F, 1.0F, 6.0F, size, false);
        body.texOffs(54, 6).addBox(-1.9F, -2.0F, -9.0F, 4.0F, 3.0F, 1.0F, size, false);

        body_r1 = new ModelPart(this);
        this.body.addChild(body_r1);
        setRotationAngle(body_r1, 1.5708F, 0.0F, 0.0F);
        body_r1.texOffs(20, 0).addBox(-2.0F, -7.0F, 6.0F, 4.0F, 16.0F, 6.0F, size, false);

        head = new ModelPart(this);
        head.texOffs(0, 0).addBox(-2.5F, -2.0F, -3.0F, 5.0F, 4.0F, 5.0F, size, false);
        head.texOffs(0, 28).addBox(-1.5F, -0.0156F, -4.0F, 3.0F, 2.0F, 2.0F, size, false);
        head.texOffs(26, 22).addBox(-2.0F, -6.0F, -1.0F, 1.0F, 4.0F, 2.0F, size, false);
        head.texOffs(20, 22).addBox(1.0F, -6.0F, -1.0F, 1.0F, 4.0F, 2.0F, size, false);

        tail = new ModelPart(this);

        tail_r1 = new ModelPart(this);
        tail_r1.setPos(0.0F, 9.0F, -8.0F);
        this.tail.addChild(tail_r1);
        setRotationAngle(tail_r1, 0.6109F, 0.0F, 0.0F);
        tail_r1.texOffs(0, 15).addBox(-0.5F, -5.0F, 13.0F, 1.0F, 11.0F, 1.0F, size, false);

        backLeftLeg = new ModelPart(this);
        backLeftLeg.texOffs(8, 13).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, size, false);

        backRightLeg = new ModelPart(this);
        backRightLeg.texOffs(8, 13).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, size, false);

        frontLeftLeg = new ModelPart(this);
        frontLeftLeg.texOffs(40, 0).addBox(-1.0F, -0.2F, -1.0F, 2.0F, 10.0F, 2.0F, size, false);

        frontRightLeg = new ModelPart(this);
        frontRightLeg.texOffs(40, 0).addBox(-1.0F, -0.2F, -1.0F, 2.0F, 10.0F, 2.0F, size, false);
    }

    @Override
    @Nonnull
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    @Nonnull
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body, this.backLeftLeg, this.backRightLeg, this.frontLeftLeg, this.frontRightLeg, this.tail);
    }

    public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.getXRot() = x;
        modelRenderer.getYRot() = y;
        modelRenderer.zRot = z;
    }

    @Override
    public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
        this.lieDownAmount = entity.getLieDownAmount(partialTick);
        this.lieDownAmountTail = entity.getLieDownAmountTail(partialTick);
        this.relaxStateOneAmount = entity.getRelaxStateOneAmount(partialTick);
        if (this.lieDownAmount <= 0.0F) {
            this.head.getXRot() = 0.0F;
            this.head.zRot = 0.0F;
            this.frontLeftLeg.getXRot() = 0.0F;
            this.frontLeftLeg.zRot = 0.0F;
            this.frontRightLeg.getXRot() = 0.0F;
            this.frontRightLeg.zRot = 0.0F;
            this.frontRightLeg.x = -1.2F;
            this.backLeftLeg.getXRot() = 0.0F;
            this.backRightLeg.getXRot() = 0.0F;
            this.backRightLeg.zRot = 0.0F;
            this.backRightLeg.x = -1.1F;
            this.backRightLeg.y = 18.0F;
        }

        this.baseLivingAnimations(entity, limbSwing, limbSwingAmount, partialTick);
        if (entity.isInSittingPose()) {
            this.body.getXRot() = ((float) Math.PI / -4F);
            this.body.y += 2.0F;
            this.body.z += -4.0F;
            this.head.y += -3.3F;
            ++this.head.z;
            this.tail.y += 8.0F;
            this.tail.z += -2.0F;
            this.tail.getXRot() = 1.7278761F;
            this.frontLeftLeg.getXRot() = -0.15707964F;
            this.frontLeftLeg.y = 16.1F;
            this.frontLeftLeg.z = -7.0F;
            this.frontRightLeg.getXRot() = -0.15707964F;
            this.frontRightLeg.y = 16.1F;
            this.frontRightLeg.z = -7.0F;
            this.backLeftLeg.getXRot() = (-(float) Math.PI / 2F);
            this.backLeftLeg.y = 21.0F;
            this.backLeftLeg.z = 1.0F;
            this.backRightLeg.getXRot() = (-(float) Math.PI / 2F);
            this.backRightLeg.y = 21.0F;
            this.backRightLeg.z = 1.0F;
            this.state = 3;
        }
    }

    public void baseLivingAnimations(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
        body.setPos(0.0F, 17.0F, 1.0F);
        body_r1.setPos(0.0F, 7.0F, -1.0F);
        head.setPos(0.0F, 13.0F, -9.0F);
        tail.setPos(0.0F, 15.0F, 8.0F);
        backLeftLeg.setPos(1.1F, 18.0F, 7.0F);
        backRightLeg.setPos(-1.1F, 18.0F, 7.0F);
        frontLeftLeg.setPos(1.2F, 14.0F, -4.0F);
        frontRightLeg.setPos(-1.2F, 14.0F, -4.0F);
        this.tail.getXRot() = 0.9F;
        if (entity.isCrouching()) {
            ++this.body.y;
            this.head.y += 2.0F;
            ++this.tail.y;
            this.tail.getXRot() = ((float) Math.PI / 2F);
            this.state = 0;
        } else if (entity.isSprinting()) {
            this.tail.getXRot() = ((float) Math.PI / 2F);
            this.state = 2;
        } else {
            this.state = 1;
        }

    }

    @Override
    public void setupAnim(@Nonnull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.baseRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        if (this.lieDownAmount > 0.0F) {
            this.head.zRot = ModelUtils.rotlerpRad(this.head.zRot, -1.2707963F, this.lieDownAmount);
            this.head.getYRot() = ModelUtils.rotlerpRad(this.head.getYRot(), 1.2707963F, this.lieDownAmount);
            this.frontLeftLeg.getXRot() = -1.2707963F;
            this.frontRightLeg.getXRot() = -0.47079635F;
            this.frontRightLeg.zRot = -0.2F;
            this.frontRightLeg.x = -0.2F;
            this.backLeftLeg.getXRot() = -0.4F;
            this.backRightLeg.getXRot() = 0.5F;
            this.backRightLeg.zRot = -0.5F;
            this.backRightLeg.x = -0.3F;
            this.backRightLeg.y = 20.0F;
            this.tail.getXRot() = ModelUtils.rotlerpRad(this.tail.getXRot(), 0.8F, this.lieDownAmountTail);
        }

        if (this.relaxStateOneAmount > 0.0F) {
            this.head.getXRot() = ModelUtils.rotlerpRad(this.head.getXRot(), -0.58177644F, this.relaxStateOneAmount);
        }
    }

    public void baseRotationAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.getXRot() = headPitch * ((float) Math.PI / 180F);
        this.head.getYRot() = netHeadYaw * ((float) Math.PI / 180F);
        if (this.state != 3) {
            this.body.getXRot() = ((float) Math.PI / 180F);
            if (this.state == 2) {
                this.backLeftLeg.getXRot() = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
                this.backRightLeg.getXRot() = Mth.cos(limbSwing * 0.6662F + 0.3F) * limbSwingAmount;
                this.frontLeftLeg.getXRot() = Mth.cos(limbSwing * 0.6662F + (float) Math.PI + 0.3F) * limbSwingAmount;
                this.frontRightLeg.getXRot() = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
            } else {
                this.backLeftLeg.getXRot() = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
                this.backRightLeg.getXRot() = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
                this.frontLeftLeg.getXRot() = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
                this.frontRightLeg.getXRot() = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
            }
        }

    }
}
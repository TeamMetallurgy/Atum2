package com.teammetallurgy.atum.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.teammetallurgy.atum.entity.animal.DesertWolfEntity;
import net.minecraft.client.model.ColorableAgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class DesertWolfModel<T extends DesertWolfEntity> extends ColorableAgeableListModel<T> {
    private static final String REAL_HEAD = "real_head";
    private static final String UPPER_BODY = "upper_body";
    private static final String REAL_TAIL = "real_tail";
    private final ModelPart head;
    private final ModelPart realHead;
    private final ModelPart body;
    private final ModelPart rightHindLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftFrontLeg;
    private final ModelPart tail;
    private final ModelPart realTail;
    private final ModelPart upperBody;
    private static final int LEG_SIZE = 8;

    public DesertWolfModel(ModelPart part) {
        this.head = part.getChild("head");
        this.realHead = this.head.getChild("real_head");
        this.body = part.getChild("body");
        this.upperBody = part.getChild("upper_body");
        this.rightHindLeg = part.getChild("right_hind_leg");
        this.leftHindLeg = part.getChild("left_hind_leg");
        this.rightFrontLeg = part.getChild("right_front_leg");
        this.leftFrontLeg = part.getChild("left_front_leg");
        this.tail = part.getChild("tail");
        this.realTail = this.tail.getChild("real_tail");
    }

    @Override
    @Nonnull
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    @Nonnull
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body, this.rightHindLeg, this.leftHindLeg, this.rightFrontLeg, this.leftFrontLeg, this.tail, this.upperBody);
    }

    @Override
    public void prepareMobModel(T desertWolf, float limbSwing, float limbSwingAmount, float partialTickTime) {
        if (desertWolf.isAngry()) {
            this.tail.yRot = 0.0F;
        } else {
            this.tail.yRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        }

        if (desertWolf.isInSittingPose()) {
            this.upperBody.setPos(-1.0F, 16.0F, -3.0F);
            this.upperBody.xRot = 1.2566371F;
            this.upperBody.yRot = 0.0F;
            this.body.setPos(0.0F, 18.0F, 0.0F);
            this.body.xRot = ((float) Math.PI / 4F);
            this.tail.setPos(-1.0F, 21.0F, 6.0F);
            this.rightHindLeg.setPos(-2.5F, 22.7F, 2.0F);
            this.rightHindLeg.xRot = ((float) Math.PI * 1.5F);
            this.leftHindLeg.setPos(0.5F, 22.7F, 2.0F);
            this.leftHindLeg.xRot = ((float) Math.PI * 1.5F);
            this.rightFrontLeg.xRot = 5.811947F;
            this.rightFrontLeg.setPos(-2.49F, 17.0F, -4.0F);
            this.leftFrontLeg.xRot = 5.811947F;
            this.leftFrontLeg.setPos(0.51F, 17.0F, -4.0F);
        } else {
            this.body.setPos(0.0F, 14.0F, 2.0F);
            this.body.xRot = ((float) Math.PI / 2F);
            this.upperBody.setPos(-1.0F, 14.0F, -3.0F);
            this.upperBody.xRot = this.body.xRot;
            this.tail.setPos(-1.0F, 12.0F, 8.0F);
            this.rightHindLeg.setPos(-2.5F, 16.0F, 7.0F);
            this.leftHindLeg.setPos(0.5F, 16.0F, 7.0F);
            this.rightFrontLeg.setPos(-2.5F, 16.0F, -4.0F);
            this.leftFrontLeg.setPos(0.5F, 16.0F, -4.0F);
            this.rightHindLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.leftHindLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            this.rightFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            this.leftFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        }

        if (desertWolf.isVehicle()) {
            this.head.xRot = Mth.cos(limbSwing * 0.6662F) * 0.15F * limbSwingAmount;
        }

        this.realHead.zRot = desertWolf.getHeadRollAngle(partialTickTime) + desertWolf.getBodyRollAngle(partialTickTime, 0.0F);
        this.upperBody.zRot = desertWolf.getBodyRollAngle(partialTickTime, -0.08F);
        this.body.zRot = desertWolf.getBodyRollAngle(partialTickTime, -0.16F);
        this.realTail.zRot = desertWolf.getBodyRollAngle(partialTickTime, -0.2F);
    }

    @Override
    public void setupAnim(@Nonnull T desertWolf, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!desertWolf.isVehicle()) {
            this.head.xRot = headPitch * ((float) Math.PI / 180F);
        }
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.tail.xRot = ageInTicks;
    }
}
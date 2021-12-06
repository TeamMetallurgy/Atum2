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
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart wolfLeg1;
    private final ModelPart wolfLeg2;
    private final ModelPart wolfLeg3;
    private final ModelPart wolfLeg4;
    private final ModelPart tail;
    private final ModelPart mane;

    public DesertWolfModel(float size) {
        this.head = new ModelPart(this, 0, 0);
        this.head.addBox(-2.0F, -3.0F, -2.0F, 6, 6, 4, size);
        this.head.setPos(-1.0F, 13.5F, -7.0F);
        this.body = new ModelPart(this, 18, 14);
        this.body.addBox(-3.0F, -2.0F, -3.0F, 6, 9, 6, size);
        this.body.setPos(0.0F, 14.0F, 2.0F);
        this.mane = new ModelPart(this, 21, 0);
        this.mane.addBox(-3.0F, -3.0F, -3.0F, 8, 6, 7, size);
        this.mane.setPos(-1.0F, 14.0F, 2.0F);
        this.wolfLeg1 = new ModelPart(this, 0, 18);
        this.wolfLeg1.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, size);
        this.wolfLeg1.setPos(-2.5F, 16.0F, 7.0F);
        this.wolfLeg2 = new ModelPart(this, 0, 18);
        this.wolfLeg2.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, size);
        this.wolfLeg2.setPos(0.5F, 16.0F, 7.0F);
        this.wolfLeg3 = new ModelPart(this, 0, 18);
        this.wolfLeg3.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, size);
        this.wolfLeg3.setPos(-2.5F, 16.0F, -4.0F);
        this.wolfLeg4 = new ModelPart(this, 0, 18);
        this.wolfLeg4.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, size);
        this.wolfLeg4.setPos(0.5F, 16.0F, -4.0F);
        this.tail = new ModelPart(this, 9, 18);
        this.tail.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, size);
        this.tail.setPos(-1.0F, 12.0F, 8.0F);
        this.head.texOffs(16, 14).addBox(-2.0F, -5.0F, 0.0F, 2, 2, 1, size);
        this.head.texOffs(16, 14).addBox(2.0F, -5.0F, 0.0F, 2, 2, 1, size);
        this.head.texOffs(0, 10).addBox(-0.5F, 0.0F, -5.0F, 3, 3, 4, size);
    }

    @Override
    @Nonnull
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    @Nonnull
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body, this.wolfLeg1, this.wolfLeg2, this.wolfLeg3, this.wolfLeg4, this.tail, this.mane);
    }


    @Override
    public void prepareMobModel(T desertWolf, float limbSwing, float limbSwingAmount, float partialTickTime) {
        if (desertWolf.isAngry()) {
            this.tail.getYRot() = 0.0F;
        } else {
            float tailAmount = desertWolf.isVehicle() ? 0.5F : 1.4F;
            this.tail.getYRot() = Mth.cos(limbSwing * 0.6662F) * tailAmount * limbSwingAmount;
        }

        if (desertWolf.isInSittingPose()) {
            this.mane.setPos(-1.0F, 16.0F, -3.0F);
            this.mane.getXRot() = ((float) Math.PI * 2F / 5F);
            this.mane.getYRot() = 0.0F;
            this.body.setPos(0.0F, 18.0F, 0.0F);
            this.body.getXRot() = ((float) Math.PI / 4F);
            this.tail.setPos(-1.0F, 21.0F, 6.0F);
            this.wolfLeg1.setPos(-2.5F, 22.0F, 2.0F);
            this.wolfLeg1.getXRot() = ((float) Math.PI * 3F / 2F);
            this.wolfLeg2.setPos(0.5F, 22.0F, 2.0F);
            this.wolfLeg2.getXRot() = ((float) Math.PI * 3F / 2F);
            this.wolfLeg3.getXRot() = 5.811947F;
            this.wolfLeg3.setPos(-2.49F, 17.0F, -4.0F);
            this.wolfLeg4.getXRot() = 5.811947F;
            this.wolfLeg4.setPos(0.51F, 17.0F, -4.0F);
        } else {
            this.body.setPos(0.0F, 14.0F, 2.0F);
            this.body.getXRot() = ((float) Math.PI / 2F);
            this.mane.setPos(-1.0F, 14.0F, -3.0F);
            this.mane.getXRot() = this.body.getXRot();
            this.tail.setPos(-1.0F, 12.0F, 8.0F);
            this.wolfLeg1.setPos(-2.5F, 16.0F, 7.0F);
            this.wolfLeg2.setPos(0.5F, 16.0F, 7.0F);
            this.wolfLeg3.setPos(-2.5F, 16.0F, -4.0F);
            this.wolfLeg4.setPos(0.5F, 16.0F, -4.0F);
            this.wolfLeg1.getXRot() = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.wolfLeg2.getXRot() = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            this.wolfLeg3.getXRot() = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            this.wolfLeg4.getXRot() = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        }

        if (desertWolf.isVehicle()) {
            this.head.getXRot() = Mth.cos(limbSwing * 0.6662F) * 0.15F * limbSwingAmount;
        }

        this.head.zRot = desertWolf.getInterestedAngle(partialTickTime) + desertWolf.getShakeAngle(partialTickTime, 0.0F);
        this.mane.zRot = desertWolf.getShakeAngle(partialTickTime, -0.08F);
        this.body.zRot = desertWolf.getShakeAngle(partialTickTime, -0.16F);
        this.tail.zRot = desertWolf.getShakeAngle(partialTickTime, -0.2F);
    }

    @Override
    public void setupAnim(T desertWolf, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!desertWolf.isVehicle()) {
            this.head.getXRot() = headPitch * 0.017453292F;
        }
        this.head.getYRot() = netHeadYaw * 0.017453292F;
        this.tail.getXRot() = ageInTicks;
    }
}
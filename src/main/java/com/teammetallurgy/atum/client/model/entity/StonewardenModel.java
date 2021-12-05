package com.teammetallurgy.atum.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.teammetallurgy.atum.entity.stone.StonewardenEntity;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class StonewardenModel<T extends StonewardenEntity> extends ListModel<T> { //Copied from IronGolemModel, with a few changes
    private final ModelPart stonewardenHead;
    private final ModelPart stonewardenBody;
    public final ModelPart stonewardenRightArm;
    private final ModelPart stonewardenLeftArm;
    private final ModelPart stonewardenLeftLeg;
    private final ModelPart stonewardenRightLeg;

    public StonewardenModel() {
        this(0.0F, -7.0F);
    }

    public StonewardenModel(float yOffset, float yRotation) {
        this.stonewardenHead = (new ModelPart(this)).setTexSize(128, 128);
        this.stonewardenHead.setPos(0.0F, 0.0F + yRotation, -2.0F);
        this.stonewardenHead.texOffs(0, 0).addBox(-4.0F, -12.0F, -5.5F, 8, 10, 8, yOffset);
        this.stonewardenHead.texOffs(24, 0).addBox(-1.0F, -5.0F, -7.5F, 2, 4, 2, yOffset);
        this.stonewardenBody = (new ModelPart(this)).setTexSize(128, 128);
        this.stonewardenBody.setPos(0.0F, 0.0F + yRotation, 0.0F);
        this.stonewardenBody.texOffs(0, 40).addBox(-9.0F, -2.0F, -6.0F, 18, 12, 11, yOffset);
        this.stonewardenBody.texOffs(0, 70).addBox(-4.5F, 10.0F, -3.0F, 9, 5, 6, yOffset + 0.5F);
        this.stonewardenRightArm = (new ModelPart(this)).setTexSize(128, 128);
        this.stonewardenRightArm.setPos(0.0F, -7.0F, 0.0F);
        this.stonewardenRightArm.texOffs(60, 21).addBox(-13.0F, -2.5F, -3.0F, 4, 30, 6, yOffset);
        this.stonewardenLeftArm = (new ModelPart(this)).setTexSize(128, 128);
        this.stonewardenLeftArm.setPos(0.0F, -7.0F, 0.0F);
        this.stonewardenLeftArm.texOffs(60, 58).addBox(9.0F, -2.5F, -3.0F, 4, 30, 6, yOffset);
        this.stonewardenLeftLeg = (new ModelPart(this, 0, 22)).setTexSize(128, 128);
        this.stonewardenLeftLeg.setPos(-4.0F, 18.0F + yRotation, 0.0F);
        this.stonewardenLeftLeg.texOffs(37, 0).addBox(-3.5F, -3.0F, -3.0F, 6, 16, 5, yOffset);
        this.stonewardenRightLeg = (new ModelPart(this, 0, 22)).setTexSize(128, 128);
        this.stonewardenRightLeg.mirror = true;
        this.stonewardenRightLeg.texOffs(60, 0).setPos(5.0F, 18.0F + yRotation, 0.0F);
        this.stonewardenRightLeg.addBox(-3.5F, -3.0F, -3.0F, 6, 16, 5, yOffset);
    }

    @Override
    @Nonnull
    public Iterable<ModelPart> parts() {
        return ImmutableList.of(this.stonewardenHead, this.stonewardenBody, this.stonewardenLeftLeg, this.stonewardenRightLeg, this.stonewardenRightArm, this.stonewardenLeftArm);
    }

    @Override
    public void setupAnim(@Nonnull T stonewarden, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.stonewardenHead.yRot = netHeadYaw * 0.017453292F;
        this.stonewardenHead.xRot = headPitch * 0.017453292F;
        this.stonewardenLeftLeg.xRot = -1.5F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.stonewardenRightLeg.xRot = 1.5F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.stonewardenLeftLeg.yRot = 0.0F;
        this.stonewardenRightLeg.yRot = 0.0F;
    }

    @Override
    public void prepareMobModel(T stonewarden, float limbSwing, float limbSwingAmount, float partialTickTime) {
        int attackTimer = stonewarden.getAttackTimer();

        if (attackTimer > 0) {
            this.stonewardenRightArm.xRot = -2.0F + 1.5F * this.triangleWave((float) attackTimer - partialTickTime, 10.0F);
            this.stonewardenLeftArm.xRot = -2.0F + 1.5F * this.triangleWave((float) attackTimer - partialTickTime, 10.0F);
        } else {
            this.stonewardenRightArm.xRot = (-0.2F + 1.5F * this.triangleWave(limbSwing, 13.0F)) * limbSwingAmount;
            this.stonewardenLeftArm.xRot = (-0.2F - 1.5F * this.triangleWave(limbSwing, 13.0F)) * limbSwingAmount;
        }
    }

    private float triangleWave(float a, float b) {
        return (Math.abs(a % b - b * 0.5F) - b * 0.25F) / (b * 0.25F);
    }
}
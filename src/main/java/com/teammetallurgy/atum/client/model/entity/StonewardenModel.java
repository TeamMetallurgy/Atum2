package com.teammetallurgy.atum.client.model.entity;

import com.teammetallurgy.atum.entity.stone.StonewardenEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StonewardenModel<T extends StonewardenEntity> extends EntityModel<T> { //Copied from IronGolemModel, with a few changes
    private final ModelRenderer stonewardenHead;
    private final ModelRenderer stonewardenBody;
    public final ModelRenderer stonewardenRightArm;
    private final ModelRenderer stonewardenLeftArm;
    private final ModelRenderer stonewardenLeftLeg;
    private final ModelRenderer stonewardenRightLeg;

    public StonewardenModel() {
        this(0.0F);
    }

    public StonewardenModel(float yOffset) {
        this(yOffset, -7.0F);
    }

    public StonewardenModel(float yOffset, float yRotation) {
        this.stonewardenHead = (new ModelRenderer(this)).setTextureSize(128, 128);
        this.stonewardenHead.setRotationPoint(0.0F, 0.0F + yRotation, -2.0F);
        this.stonewardenHead.setTextureOffset(0, 0).addBox(-4.0F, -12.0F, -5.5F, 8, 10, 8, yOffset);
        this.stonewardenHead.setTextureOffset(24, 0).addBox(-1.0F, -5.0F, -7.5F, 2, 4, 2, yOffset);
        this.stonewardenBody = (new ModelRenderer(this)).setTextureSize(128, 128);
        this.stonewardenBody.setRotationPoint(0.0F, 0.0F + yRotation, 0.0F);
        this.stonewardenBody.setTextureOffset(0, 40).addBox(-9.0F, -2.0F, -6.0F, 18, 12, 11, yOffset);
        this.stonewardenBody.setTextureOffset(0, 70).addBox(-4.5F, 10.0F, -3.0F, 9, 5, 6, yOffset + 0.5F);
        this.stonewardenRightArm = (new ModelRenderer(this)).setTextureSize(128, 128);
        this.stonewardenRightArm.setRotationPoint(0.0F, -7.0F, 0.0F);
        this.stonewardenRightArm.setTextureOffset(60, 21).addBox(-13.0F, -2.5F, -3.0F, 4, 30, 6, yOffset);
        this.stonewardenLeftArm = (new ModelRenderer(this)).setTextureSize(128, 128);
        this.stonewardenLeftArm.setRotationPoint(0.0F, -7.0F, 0.0F);
        this.stonewardenLeftArm.setTextureOffset(60, 58).addBox(9.0F, -2.5F, -3.0F, 4, 30, 6, yOffset);
        this.stonewardenLeftLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(128, 128);
        this.stonewardenLeftLeg.setRotationPoint(-4.0F, 18.0F + yRotation, 0.0F);
        this.stonewardenLeftLeg.setTextureOffset(37, 0).addBox(-3.5F, -3.0F, -3.0F, 6, 16, 5, yOffset);
        this.stonewardenRightLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(128, 128);
        this.stonewardenRightLeg.mirror = true;
        this.stonewardenRightLeg.setTextureOffset(60, 0).setRotationPoint(5.0F, 18.0F + yRotation, 0.0F);
        this.stonewardenRightLeg.addBox(-3.5F, -3.0F, -3.0F, 6, 16, 5, yOffset);
    }

    @Override
    public void render(T stonewarden, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(stonewarden, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.stonewardenHead.render(scale);
        this.stonewardenBody.render(scale);
        this.stonewardenLeftLeg.render(scale);
        this.stonewardenRightLeg.render(scale);
        this.stonewardenRightArm.render(scale);
        this.stonewardenLeftArm.render(scale);
    }

    @Override
    public void setRotationAngles(T stonewarden, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.stonewardenHead.rotateAngleY = netHeadYaw * 0.017453292F;
        this.stonewardenHead.rotateAngleX = headPitch * 0.017453292F;
        this.stonewardenLeftLeg.rotateAngleX = -1.5F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.stonewardenRightLeg.rotateAngleX = 1.5F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.stonewardenLeftLeg.rotateAngleY = 0.0F;
        this.stonewardenRightLeg.rotateAngleY = 0.0F;
    }

    @Override
    public void setLivingAnimations(T stonewarden, float limbSwing, float limbSwingAmount, float partialTickTime) {
        int attackTimer = stonewarden.getAttackTimer();

        if (attackTimer > 0) {
            this.stonewardenRightArm.rotateAngleX = -2.0F + 1.5F * this.triangleWave((float) attackTimer - partialTickTime, 10.0F);
            this.stonewardenLeftArm.rotateAngleX = -2.0F + 1.5F * this.triangleWave((float) attackTimer - partialTickTime, 10.0F);
        } else {
            this.stonewardenRightArm.rotateAngleX = (-0.2F + 1.5F * this.triangleWave(limbSwing, 13.0F)) * limbSwingAmount;
            this.stonewardenLeftArm.rotateAngleX = (-0.2F - 1.5F * this.triangleWave(limbSwing, 13.0F)) * limbSwingAmount;
        }
    }

    private float triangleWave(float a, float b) {
        return (Math.abs(a % b - b * 0.5F) - b * 0.25F) / (b * 0.25F);
    }
}
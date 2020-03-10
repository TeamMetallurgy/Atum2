package com.teammetallurgy.atum.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.teammetallurgy.atum.entity.animal.DesertWolfEntity;
import net.minecraft.client.renderer.entity.model.TintedAgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class DesertWolfModel<T extends DesertWolfEntity> extends TintedAgeableModel<T> {
    private ModelRenderer head;
    private ModelRenderer body;
    private ModelRenderer wolfLeg1;
    private ModelRenderer wolfLeg2;
    private ModelRenderer wolfLeg3;
    private ModelRenderer wolfLeg4;
    private ModelRenderer tail;
    private ModelRenderer mane;

    public DesertWolfModel() {
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-2.0F, -3.0F, -2.0F, 6, 6, 4, 0.0F);
        this.head.setRotationPoint(-1.0F, 13.5F, -7.0F);
        this.body = new ModelRenderer(this, 18, 14);
        this.body.addBox(-3.0F, -2.0F, -3.0F, 6, 9, 6, 0.0F);
        this.body.setRotationPoint(0.0F, 14.0F, 2.0F);
        this.mane = new ModelRenderer(this, 21, 0);
        this.mane.addBox(-3.0F, -3.0F, -3.0F, 8, 6, 7, 0.0F);
        this.mane.setRotationPoint(-1.0F, 14.0F, 2.0F);
        this.wolfLeg1 = new ModelRenderer(this, 0, 18);
        this.wolfLeg1.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.wolfLeg1.setRotationPoint(-2.5F, 16.0F, 7.0F);
        this.wolfLeg2 = new ModelRenderer(this, 0, 18);
        this.wolfLeg2.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.wolfLeg2.setRotationPoint(0.5F, 16.0F, 7.0F);
        this.wolfLeg3 = new ModelRenderer(this, 0, 18);
        this.wolfLeg3.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.wolfLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F);
        this.wolfLeg4 = new ModelRenderer(this, 0, 18);
        this.wolfLeg4.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.wolfLeg4.setRotationPoint(0.5F, 16.0F, -4.0F);
        this.tail = new ModelRenderer(this, 9, 18);
        this.tail.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.tail.setRotationPoint(-1.0F, 12.0F, 8.0F);
        this.head.setTextureOffset(16, 14).addBox(-2.0F, -5.0F, 0.0F, 2, 2, 1, 0.0F);
        this.head.setTextureOffset(16, 14).addBox(2.0F, -5.0F, 0.0F, 2, 2, 1, 0.0F);
        this.head.setTextureOffset(0, 10).addBox(-0.5F, 0.0F, -5.0F, 3, 3, 4, 0.0F);
    }

    @Override
    @Nonnull
    protected Iterable<ModelRenderer> getHeadParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    @Nonnull
    protected Iterable<ModelRenderer> getBodyParts() {
        return ImmutableList.of(this.body, this.wolfLeg1, this.wolfLeg2, this.wolfLeg3, this.wolfLeg4, this.tail, this.mane);
    }


    @Override
    public void setLivingAnimations(T desertWolf, float limbSwing, float limbSwingAmount, float partialTickTime) {
        if (desertWolf.isAngry()) {
            this.tail.rotateAngleY = 0.0F;
        } else {
            float tailAmount = desertWolf.isBeingRidden() ? 0.5F : 1.4F;
            this.tail.rotateAngleY = MathHelper.cos(limbSwing * 0.6662F) * tailAmount * limbSwingAmount;
        }

        if (desertWolf.isSitting()) {
            this.mane.setRotationPoint(-1.0F, 16.0F, -3.0F);
            this.mane.rotateAngleX = ((float) Math.PI * 2F / 5F);
            this.mane.rotateAngleY = 0.0F;
            this.body.setRotationPoint(0.0F, 18.0F, 0.0F);
            this.body.rotateAngleX = ((float) Math.PI / 4F);
            this.tail.setRotationPoint(-1.0F, 21.0F, 6.0F);
            this.wolfLeg1.setRotationPoint(-2.5F, 22.0F, 2.0F);
            this.wolfLeg1.rotateAngleX = ((float) Math.PI * 3F / 2F);
            this.wolfLeg2.setRotationPoint(0.5F, 22.0F, 2.0F);
            this.wolfLeg2.rotateAngleX = ((float) Math.PI * 3F / 2F);
            this.wolfLeg3.rotateAngleX = 5.811947F;
            this.wolfLeg3.setRotationPoint(-2.49F, 17.0F, -4.0F);
            this.wolfLeg4.rotateAngleX = 5.811947F;
            this.wolfLeg4.setRotationPoint(0.51F, 17.0F, -4.0F);
        } else {
            this.body.setRotationPoint(0.0F, 14.0F, 2.0F);
            this.body.rotateAngleX = ((float) Math.PI / 2F);
            this.mane.setRotationPoint(-1.0F, 14.0F, -3.0F);
            this.mane.rotateAngleX = this.body.rotateAngleX;
            this.tail.setRotationPoint(-1.0F, 12.0F, 8.0F);
            this.wolfLeg1.setRotationPoint(-2.5F, 16.0F, 7.0F);
            this.wolfLeg2.setRotationPoint(0.5F, 16.0F, 7.0F);
            this.wolfLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F);
            this.wolfLeg4.setRotationPoint(0.5F, 16.0F, -4.0F);
            this.wolfLeg1.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.wolfLeg2.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            this.wolfLeg3.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            this.wolfLeg4.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        }

        if (desertWolf.isBeingRidden()) {
            this.head.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 0.15F * limbSwingAmount;
        }

        this.head.rotateAngleZ = desertWolf.getInterestedAngle(partialTickTime) + desertWolf.getShakeAngle(partialTickTime, 0.0F);
        this.mane.rotateAngleZ = desertWolf.getShakeAngle(partialTickTime, -0.08F);
        this.body.rotateAngleZ = desertWolf.getShakeAngle(partialTickTime, -0.16F);
        this.tail.rotateAngleZ = desertWolf.getShakeAngle(partialTickTime, -0.2F);
    }

    @Override
    public void setRotationAngles(T desertWolf, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!desertWolf.isBeingRidden()) {
            this.head.rotateAngleX = headPitch * 0.017453292F;
        }
        this.head.rotateAngleY = netHeadYaw * 0.017453292F;
        this.tail.rotateAngleX = ageInTicks;
    }
}
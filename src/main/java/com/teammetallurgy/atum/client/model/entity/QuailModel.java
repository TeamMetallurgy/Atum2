package com.teammetallurgy.atum.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.teammetallurgy.atum.entity.animal.QuailEntity;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

public class QuailModel<T extends QuailEntity> extends AgeableModel<T> {
    public ModelRenderer beak;
    public ModelRenderer head;
    public ModelRenderer body;
    public ModelRenderer rightWing;
    public ModelRenderer leftWing;
    public ModelRenderer leftFoot;
    public ModelRenderer rightFoot;
    public ModelRenderer headFeather;

    public QuailModel() {
        super();
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.leftFoot = new ModelRenderer(this, 26, 0);
        this.leftFoot.setRotationPoint(1.0F, 21.0F, 1.0F);
        this.leftFoot.addBox(-1.0F, 0.0F, -3.0F, 3, 3, 3, 0.0F);
        this.head = new ModelRenderer(this, 0, 0);
        this.head.setRotationPoint(0.0F, 17.0F, -4.0F);
        this.head.addBox(-1.5F, -6.0F, -2.0F, 3, 6, 3, 0.0F);
        this.rightWing = new ModelRenderer(this, 24, 13);
        this.rightWing.setRotationPoint(-3.5F, 15.0F, 0.0F);
        this.rightWing.addBox(0.0F, 0.0F, -3.0F, 1, 4, 6, 0.0F);
        this.body = new ModelRenderer(this, 0, 9);
        this.body.setRotationPoint(0.0F, 18.0F, 0.0F);
        this.body.addBox(-2.5F, -4.0F, -3.0F, 5, 8, 6, 0.0F);
        this.setRotateAngle(this.body, 1.5707963267948966F, 0.0F, 0.0F);
        this.rightFoot = new ModelRenderer(this, 26, 0);
        this.rightFoot.setRotationPoint(-2.0F, 21.0F, 1.0F);
        this.rightFoot.addBox(-1.0F, 0.0F, -3.0F, 3, 3, 3, 0.0F);
        this.headFeather = new ModelRenderer(this, 0, 24);
        this.headFeather.setRotationPoint(0.0F, 17.0F, -4.0F);
        this.headFeather.addBox(-0.5F, -8.0F, 2.0F - 1.0F, 1, 3, 1, 0.0F); //Z altered, compared to exported model
        this.setRotateAngle(this.headFeather, 0.7853981633974483F, 0.0F, 0.0F);
        this.leftWing = new ModelRenderer(this, 24, 13);
        this.leftWing.setRotationPoint(2.5F, 15.0F, 0.0F);
        this.leftWing.addBox(0.0F, 0.0F, -3.0F, 1, 4, 6, 0.0F);
        this.beak = new ModelRenderer(this, 14, 0);
        this.beak.setRotationPoint(0.0F, 17.0F, -4.0F);
        this.beak.addBox(-1.0F, -4.0F, -4.0F, 2, 2, 2, 0.0F);
    }

    @Override
    @Nonnull
    protected Iterable<ModelRenderer> getHeadParts() {
        return ImmutableList.of(this.head, this.beak, this.headFeather);
    }

    @Override
    @Nonnull
    protected Iterable<ModelRenderer> getBodyParts() {
        return ImmutableList.of(this.body, this.rightFoot, this.leftFoot, this.rightWing, this.leftWing);
    }

    @Override
    public void setRotationAngles(@Nonnull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.rotateAngleX = headPitch * ((float) Math.PI / 180F);
        this.head.rotateAngleY = netHeadYaw * ((float) Math.PI / 180F);
        this.beak.rotateAngleX = this.head.rotateAngleX;
        this.beak.rotateAngleY = this.head.rotateAngleY;
        this.headFeather.rotateAngleX = this.head.rotateAngleX;
        this.headFeather.rotateAngleY = this.head.rotateAngleY;
        this.body.rotateAngleX = ((float) Math.PI / 2F);
        this.rightFoot.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftFoot.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.rightWing.rotateAngleZ = ageInTicks;
        this.leftWing.rotateAngleZ = -ageInTicks;
    }

    private void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
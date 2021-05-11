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
    public ModelRenderer headFeather1;

    public QuailModel() {
        super(false, 4.0F, 2.0F); //isChildHeadScaled, childHeadOffsetY, childHeadOffsetZ
        this.beak = new ModelRenderer(this);
        this.beak.setRotationPoint(0.0F, 17.0F, -4.0F);
        this.beak.setTextureOffset(14, 0).addBox(-1.0F, -2.0F, -4.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        this.head = new ModelRenderer(this);
        this.head.setRotationPoint(0.0F, 17.0F, -4.0F);
        this.head.setTextureOffset(0, 0).addBox(-1.5F, -4.0F, -2.0F, 3.0F, 6.0F, 3.0F, 0.0F, false);
        this.headFeather = new ModelRenderer(this);
        this.headFeather.setRotationPoint(0.0F, 1.0F, 0.0F);
        this.head.addChild(this.headFeather);
        this.headFeather1 = new ModelRenderer(this);
        this.headFeather1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.headFeather.addChild(this.headFeather1);
        this.setRotateAngle(this.headFeather1, 0.0F, 0.0436F, 0.0F);
        this.headFeather1.setTextureOffset(0, 24).addBox(0.0F, -7.2218F, -3.5355F, 0.0F, 3.0F, 2.0F, 0.0F, false);
        this.body = new ModelRenderer(this);
        this.body.setRotationPoint(0.0F, 18.0F, 0.0F);
        this.setRotateAngle(this.body, 1.5708F, 0.0F, 0.0F);
        this.body.setTextureOffset(0, 9).addBox(-2.5F, -4.0F, -4.0F, 5.0F, 8.0F, 6.0F, 0.0F, false);
        this.rightWing = new ModelRenderer(this);
        this.rightWing.setRotationPoint(-2.5F, 16.0F, 0.0F);
        this.rightWing.setTextureOffset(24, 13).addBox(-1.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F, 0.0F, false);
        this.leftWing = new ModelRenderer(this);
        this.leftWing.setRotationPoint(2.0F, 16.0F, 0.0F);
        this.leftWing.setTextureOffset(24, 13).addBox(0.5F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F, 0.0F, false);
        this.leftFoot = new ModelRenderer(this);
        this.leftFoot.setRotationPoint(1.0F, 21.0F, 1.0F);
        this.leftFoot.setTextureOffset(26, 0).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 3.0F, 3.0F, 0.0F, false);
        this.rightFoot = new ModelRenderer(this);
        this.rightFoot.setRotationPoint(-2.0F, 21.0F, 1.0F);
        this.rightFoot.setTextureOffset(26, 0).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 3.0F, 3.0F, 0.0F, false);
    }

    @Override
    @Nonnull
    protected Iterable<ModelRenderer> getHeadParts() {
        return ImmutableList.of(this.head, this.beak);
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
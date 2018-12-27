package com.teammetallurgy.atum.client.model.entity;

import com.teammetallurgy.atum.entity.animal.EntityCamel;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import javax.annotation.Nonnull;

public class ModelCamel extends ModelQuadruped {
    private ModelRenderer neckheadlower;
    private ModelRenderer snout;
    private ModelRenderer ear_r;
    private ModelRenderer ear_l;
    private ModelRenderer hump1;
    private ModelRenderer hump2;
    private ModelRenderer tail;
    private ModelRenderer chest_right;
    private ModelRenderer chest_left;
    public ModelRenderer saddle1;
    public ModelRenderer saddle2;

    public ModelCamel(float textureOffset) {
        super(14, textureOffset);
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.chest_left = new ModelRenderer(this, 45, 41);
        this.chest_left.setRotationPoint(5.5F, 3.0F, 3.0F);
        this.chest_left.addBox(-3.0F, 0.0F, 0.0F, 8, 8, 3, 0.0F);
        this.setRotateAngle(chest_left, 0.0F, 1.5707963267948966F, 0.0F);
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
        this.head = new ModelRenderer(this, 0, 14);
        this.head.setRotationPoint(0.0F, 7.0F, -8.0F);
        this.head.addBox(-3.0F, -16.0F, -8.0F, 6, 14, 6, 0.0F);
        this.ear_r = new ModelRenderer(this, 30, 0);
        this.ear_r.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.ear_r.addBox(-4.0F, -16.0F, -6.0F, 1, 3, 2, 0.0F);
        this.setRotateAngle(ear_r, -0.08726646259971647F, 0.0F, 0.0F);
        this.ear_l = new ModelRenderer(this, 30, 5);
        this.ear_l.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.ear_l.addBox(3.0F, -16.0F, -6.0F, 1, 3, 2, 0.0F);
        this.setRotateAngle(ear_l, -0.08726646259971647F, 0.0F, 0.0F);
        this.chest_right = new ModelRenderer(this, 45, 28);
        this.chest_right.setRotationPoint(-8.5F, 3.0F, 3.0F);
        this.chest_right.addBox(-3.0F, 0.0F, 0.0F, 8, 8, 3, 0.0F);
        this.setRotateAngle(chest_right, 0.0F, 1.5707963267948966F, 0.0F);
        this.neckheadlower = new ModelRenderer(this, 68, 30);
        this.neckheadlower.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.neckheadlower.addBox(-4.0F, -4.0F, -5.0F, 8, 6, 6, 0.0F);
        this.hump1 = new ModelRenderer(this, 74, 0);
        this.hump1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hump1.addBox(-4.0F, -6.0F, 3.0F, 8, 12, 3, 0.0F);
        this.leg1 = new ModelRenderer(this, 29, 29);
        this.leg1.setRotationPoint(3.5F, 10.0F, -5.0F);
        this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4, 0.0F);
        this.leg2 = new ModelRenderer(this, 29, 29);
        this.leg2.setRotationPoint(-3.5F, 10.0F, -5.0F);
        this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4, 0.0F);
        this.leg3 = new ModelRenderer(this, 29, 29);
        this.leg3.setRotationPoint(3.5F, 10.0F, 6.0F);
        this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4, 0.0F);
        this.leg4 = new ModelRenderer(this, 29, 29);
        this.leg4.setRotationPoint(-3.5F, 10.0F, 6.0F);
        this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4, 0.0F);
        this.saddle1 = new ModelRenderer(this, 101, 32);
        this.saddle1.setRotationPoint(0.0F, 4.9F, 2.0F);
        this.saddle1.addBox(-4.0F, -6.0F, 6.0F, 8, 12, 2, 0.0F);
        this.setRotateAngle(saddle1, 1.5707963267948966F, 0.0F, 0.0F);
        this.saddle2 = new ModelRenderer(this, 100, 48);
        this.saddle2.setRotationPoint(0.0F, 5.0F, 2.0F);
        this.saddle2.addBox(-5.0F, 2.0F, 8.0F, 10, 4, 4, 0.0F);
        this.setRotateAngle(saddle2, 1.5707963267948966F, 0.0F, 0.0F);
        this.head.addChild(this.snout);
        this.head.addChild(this.ear_r);
        this.head.addChild(this.ear_l);
        this.head.addChild(this.neckheadlower);
        this.body.addChild(this.hump1);
        this.body.addChild(this.hump2);
    }

    @Override
    public void render(@Nonnull Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        EntityCamel camel = (EntityCamel) entity;
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, camel);
        boolean isChild = camel.isChild();
        boolean canHaveCrate = !camel.isChild() && camel.hasCrate();
        boolean isSaddled = !isChild && camel.isHorseSaddled();

        if (isSaddled) {
            this.saddle1.render(scale);
            this.saddle2.render(scale);
        }

        if (isChild) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, this.childYOffset * scale, this.childZOffset * scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.71428573F, 0.64935064F, 0.7936508F);
            GlStateManager.translate(0.0F, 22.0F * scale, 0.26F);
            this.head.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.625F, 0.45454544F, 0.45454544F);
            GlStateManager.translate(0.0F, 33.0F * scale, 0.0F);
            this.body.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.45454544F, 0.41322312F, 0.45454544F);
            GlStateManager.translate(0.0F, 33.0F * scale, 0.0F);
            this.leg1.render(scale);
            this.leg2.render(scale);
            this.leg3.render(scale);
            this.leg4.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.45454544F, 0.41322312F, 0.45454544F);
            GlStateManager.translate(0.0F, 37.0F * scale, 0.0F);
            this.tail.render(scale);
            GlStateManager.popMatrix();
        } else {
            this.head.render(scale);
            this.body.render(scale);
            this.tail.render(scale);
            this.leg1.render(scale);
            this.leg2.render(scale);
            this.leg3.render(scale);
            this.leg4.render(scale);
        }
        
        /*if(canHaveChest) {
        	this.chest_left.render(scale);
        	this.chest_right.render(scale);
        }*/
    }

    @Override
    public void setLivingAnimations(EntityLivingBase livingBase, float limbSwing, float limbSwingAmount, float partialTickTime) {
        super.setLivingAnimations(livingBase, limbSwing, limbSwingAmount, partialTickTime);
        EntityCamel camel = (EntityCamel) livingBase;
        boolean isSaddled = camel.isHorseSaddled();
        boolean isBeingRidden = camel.isBeingRidden();
        float rearingAmount = camel.getRearingAmount(partialTickTime);
        float rearing = 1.0F - rearingAmount;

        if (isSaddled) {
            /*this.horseSaddleBottom.rotationPointY = rearingAmount * 0.5F + rearing * 2.0F;
            this.horseSaddleBottom.rotationPointZ = rearingAmount * 11.0F + rearing * 2.0F;
            this.horseSaddleFront.rotationPointY = this.horseSaddleBottom.rotationPointY;
            this.horseSaddleBack.rotationPointY = this.horseSaddleBottom.rotationPointY;
            this.horseSaddleFront.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
            this.horseSaddleBack.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
            this.horseSaddleBottom.rotateAngleX = this.body.rotateAngleX;
            this.horseSaddleFront.rotateAngleX = this.body.rotateAngleX;
            this.horseSaddleBack.rotateAngleX = this.body.rotateAngleX;*/
        }
    }

    private void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
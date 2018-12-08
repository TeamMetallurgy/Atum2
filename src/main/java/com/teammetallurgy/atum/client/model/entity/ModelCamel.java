package com.teammetallurgy.atum.client.model.entity;

import com.teammetallurgy.atum.entity.EntityCamel;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class ModelCamel extends ModelBase {
    public ModelRenderer body;
    public ModelRenderer leg_b_l;
    public ModelRenderer leg_f_l;
    public ModelRenderer leg_b_r;
    public ModelRenderer leg_f_r;
    public ModelRenderer chest_right;
    public ModelRenderer chest_left;
    public ModelRenderer neckheadupper;
    public ModelRenderer tail;
    public ModelRenderer hump1;
    public ModelRenderer hump2;
    public ModelRenderer neckheadlower;
    public ModelRenderer snout;
    public ModelRenderer ear_r;
    public ModelRenderer ear_l;

    public ModelCamel() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.chest_left = new ModelRenderer(this, 45, 41);
        this.chest_left.setRotationPoint(5.5F, 3.0F, 3.0F);
        this.chest_left.addBox(-3.0F, 0.0F, 0.0F, 8, 8, 3, 0.0F);
        this.setRotateAngle(chest_left, 0.0F, 1.5707963267948966F, 0.0F);
        this.leg_f_l = new ModelRenderer(this, 29, 29);
        this.leg_f_l.setRotationPoint(3.5F, 10.0F, -5.0F);
        this.leg_f_l.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4, 0.0F);
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
        this.leg_b_l = new ModelRenderer(this, 29, 29);
        this.leg_b_l.setRotationPoint(3.5F, 10.0F, 6.0F);
        this.leg_b_l.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4, 0.0F);
        this.hump2 = new ModelRenderer(this, 74, 16);
        this.hump2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hump2.addBox(-3.0F, -5.0F, 6.0F, 6, 10, 2, 0.0F);
        this.neckheadupper = new ModelRenderer(this, 0, 14);
        this.neckheadupper.setRotationPoint(0.0F, 7.0F, -8.0F);
        this.neckheadupper.addBox(-3.0F, -16.0F, -8.0F, 6, 14, 6, 0.0F);
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
        this.leg_f_r = new ModelRenderer(this, 29, 29);
        this.leg_f_r.setRotationPoint(-3.5F, 10.0F, -5.0F);
        this.leg_f_r.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4, 0.0F);
        this.leg_b_r = new ModelRenderer(this, 29, 29);
        this.leg_b_r.setRotationPoint(-3.5F, 10.0F, 6.0F);
        this.leg_b_r.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4, 0.0F);
        this.neckheadlower = new ModelRenderer(this, 68, 30);
        this.neckheadlower.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.neckheadlower.addBox(-4.0F, -4.0F, -5.0F, 8, 6, 6, 0.0F);
        this.hump1 = new ModelRenderer(this, 74, 0);
        this.hump1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hump1.addBox(-4.0F, -6.0F, 3.0F, 8, 12, 3, 0.0F);
        this.neckheadupper.addChild(this.snout);
        this.body.addChild(this.hump2);
        this.neckheadupper.addChild(this.ear_r);
        this.neckheadupper.addChild(this.ear_l);
        this.neckheadupper.addChild(this.neckheadlower);
        this.body.addChild(this.hump1);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        //this.chest_left.render(scale);
        this.leg_f_l.render(scale);
        this.tail.render(scale);
        this.body.render(scale);
        this.leg_b_l.render(scale);
        this.neckheadupper.render(scale);
        //this.chest_right.render(scale);
        this.leg_f_r.render(scale);
        this.leg_b_r.render(scale);
    }
    @Override
    public void setLivingAnimations(EntityLivingBase livingBase, float limbSwing, float limbSwingAmount, float partialTickTime) {
        EntityCamel desertWolf = (EntityCamel) livingBase;
        float speed = 0.5f;
        this.neckheadupper.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 0.4F * limbSwingAmount;
        this.snout.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 0.4F * limbSwingAmount;
        this.leg_f_r.rotateAngleX = MathHelper.cos(speed * limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leg_f_l.rotateAngleX = MathHelper.cos(speed * limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.leg_b_r.rotateAngleX = MathHelper.cos(speed * limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.leg_b_l.rotateAngleX = MathHelper.cos(speed * limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}

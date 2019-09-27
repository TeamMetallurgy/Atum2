package com.teammetallurgy.atum.client.model.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import com.teammetallurgy.atum.entity.animal.CamelEntity;
import net.minecraft.client.renderer.entity.model.QuadrupedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;

public class CamelModel<T extends CamelEntity> extends QuadrupedModel<T> {
    private RendererModel neckheadModellower;
    private RendererModel snout;
    private RendererModel ear_r;
    private RendererModel ear_l;
    private RendererModel hump1;
    private RendererModel hump2;
    private RendererModel tail;
    private RendererModel chest_right;
    private RendererModel chest_left;
    public RendererModel saddle1;
    public RendererModel saddle2;

    public CamelModel(float textureOffset) {
        super(14, textureOffset);
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.chest_left = new RendererModel(this, 45, 41);
        this.chest_left.setRotationPoint(5.5F, 3.0F, 3.0F);
        this.chest_left.addBox(-3.0F, 0.0F, 0.0F, 8, 8, 3, 0.0F);
        this.setRotateAngle(chest_left, 0.0F, 1.5707963267948966F, 0.0F);
        this.tail = new RendererModel(this, 94, 9);
        this.tail.setRotationPoint(0.0F, 2.0F, 10.0F);
        this.tail.addBox(-1.5F, -1.0F, 0.0F, 3, 1, 14, 0.0F);
        this.setRotateAngle(tail, -1.5707963267948966F, 0.0F, 0.0F);
        this.snout = new RendererModel(this, 0, 0);
        this.snout.setRotationPoint(0.0F, -10.0F, -8.0F);
        this.snout.addBox(-3.0F, -5.0F, -6.0F, 6, 4, 6, 0.0F);
        this.body = new RendererModel(this, 29, 0);
        this.body.setRotationPoint(0.0F, 5.0F, 2.0F);
        this.body.addBox(-6.0F, -10.0F, -7.0F, 12, 18, 10, 0.0F);
        this.setRotateAngle(body, 1.5707963267948966F, 0.0F, 0.0F);
        this.hump2 = new RendererModel(this, 74, 16);
        this.hump2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hump2.addBox(-3.0F, -5.0F, 6.0F, 6, 10, 2, 0.0F);
        this.headModel = new RendererModel(this, 0, 14);
        this.headModel.setRotationPoint(0.0F, 7.0F, -8.0F);
        this.headModel.addBox(-3.0F, -16.0F, -8.0F, 6, 14, 6, 0.0F);
        this.ear_r = new RendererModel(this, 30, 0);
        this.ear_r.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.ear_r.addBox(-4.0F, -16.0F, -6.0F, 1, 3, 2, 0.0F);
        this.setRotateAngle(ear_r, -0.08726646259971647F, 0.0F, 0.0F);
        this.ear_l = new RendererModel(this, 30, 5);
        this.ear_l.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.ear_l.addBox(3.0F, -16.0F, -6.0F, 1, 3, 2, 0.0F);
        this.setRotateAngle(ear_l, -0.08726646259971647F, 0.0F, 0.0F);
        this.chest_right = new RendererModel(this, 45, 28);
        this.chest_right.setRotationPoint(-8.5F, 3.0F, 3.0F);
        this.chest_right.addBox(-3.0F, 0.0F, 0.0F, 8, 8, 3, 0.0F);
        this.setRotateAngle(chest_right, 0.0F, 1.5707963267948966F, 0.0F);
        this.neckheadModellower = new RendererModel(this, 68, 30);
        this.neckheadModellower.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.neckheadModellower.addBox(-4.0F, -4.0F, -5.0F, 8, 6, 6, 0.0F);
        this.hump1 = new RendererModel(this, 74, 0);
        this.hump1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hump1.addBox(-4.0F, -6.0F, 3.0F, 8, 12, 3, 0.0F);
        this.legBackRight = new RendererModel(this, 29, 29);
        this.legBackRight.setRotationPoint(3.5F, 10.0F, -5.0F);
        this.legBackRight.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4, 0.0F);
        this.legBackLeft = new RendererModel(this, 29, 29);
        this.legBackLeft.setRotationPoint(-3.5F, 10.0F, -5.0F);
        this.legBackLeft.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4, 0.0F);
        this.legFrontRight = new RendererModel(this, 29, 29);
        this.legFrontRight.setRotationPoint(3.5F, 10.0F, 6.0F);
        this.legFrontRight.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4, 0.0F);
        this.legFrontLeft = new RendererModel(this, 29, 29);
        this.legFrontLeft.setRotationPoint(-3.5F, 10.0F, 6.0F);
        this.legFrontLeft.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4, 0.0F);
        this.saddle1 = new RendererModel(this, 101, 32);
        this.saddle1.setRotationPoint(0.0F, 4.9F, 2.0F);
        this.saddle1.addBox(-4.0F, -6.0F, 6.0F, 8, 12, 2, 0.0F);
        this.setRotateAngle(saddle1, 1.5707963267948966F, 0.0F, 0.0F);
        this.saddle2 = new RendererModel(this, 100, 48);
        this.saddle2.setRotationPoint(0.0F, 5.0F, 2.0F);
        this.saddle2.addBox(-5.0F, 2.0F, 8.0F, 10, 4, 4, 0.0F);
        this.setRotateAngle(saddle2, 1.5707963267948966F, 0.0F, 0.0F);
        this.headModel.addChild(this.snout);
        this.headModel.addChild(this.ear_r);
        this.headModel.addChild(this.ear_l);
        this.headModel.addChild(this.neckheadModellower);
        this.body.addChild(this.hump1);
        this.body.addChild(this.hump2);
    }

    @Override
    public void render(@Nonnull T camel, float limbSwing, float limbSwingAmount, float ageInTicks, float netheadModelYaw, float headModelPitch, float scale) {
        this.setRotationAngles(camel, limbSwing, limbSwingAmount, ageInTicks, netheadModelYaw, headModelPitch, scale);
        boolean isChild = camel.isChild();
        boolean isSaddled = !isChild && camel.isHorseSaddled();

        if (isChild) {
            GlStateManager.pushMatrix();
            GlStateManager.translatef(0.0F, this.childYOffset * scale, this.childZOffset * scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scalef(0.71428573F, 0.64935064F, 0.7936508F);
            GlStateManager.translatef(0.0F, 22.0F * scale, 0.26F);
            this.headModel.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scalef(0.625F, 0.45454544F, 0.45454544F);
            GlStateManager.translatef(0.0F, 33.0F * scale, 0.0F);
            this.body.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scalef(0.45454544F, 0.41322312F, 0.45454544F);
            GlStateManager.translatef(0.0F, 33.0F * scale, 0.0F);
            this.legBackRight.render(scale);
            this.legBackLeft.render(scale);
            this.legFrontRight.render(scale);
            this.legFrontLeft.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scalef(0.45454544F, 0.41322312F, 0.45454544F);
            GlStateManager.translatef(0.0F, 37.0F * scale, 0.0F);
            this.tail.render(scale);
            GlStateManager.popMatrix();
        } else {
            this.headModel.render(scale);
            this.body.render(scale);
            this.tail.render(scale);
            this.legBackRight.render(scale);
            this.legBackLeft.render(scale);
            this.legFrontRight.render(scale);
            this.legFrontLeft.render(scale);

            if (isSaddled) {
                this.saddle1.render(scale);
                this.saddle2.render(scale);
            }

            if (camel.hasLeftCrate()) {
                this.chest_left.render(scale);
            }
            if (camel.hasRightCrate()) {
                this.chest_right.render(scale);
            }

        }
    }

    @Override
    public void setRotationAngles(T camel, float limbSwing, float limbSwingAmount, float ageInTicks, float netheadModelYaw, float headModelPitch, float scaleFactor) {
        limbSwingAmount *= CamelEntity.CAMEL_RIDING_SPEED_AMOUNT;
        super.setRotationAngles(camel, limbSwing, limbSwingAmount, ageInTicks, netheadModelYaw, headModelPitch, scaleFactor);

        if (camel.isBeingRidden()) {
            this.headModel.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 0.025F * limbSwingAmount;
            Vec3d motion = camel.getMotion();
            this.tail.rotateAngleX = -45.5F + (MathHelper.sqrt(Math.pow(motion.x, 2) + Math.pow(motion.z, 2)));
        } else {
            this.tail.rotateAngleX = -45.5F;
        }
        this.tail.rotateAngleZ = MathHelper.cos(limbSwing * 0.6662F) * 0.1F * limbSwingAmount;
    }

    private void setRotateAngle(RendererModel modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
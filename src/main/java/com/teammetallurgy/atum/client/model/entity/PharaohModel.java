package com.teammetallurgy.atum.client.model.entity;

import com.teammetallurgy.atum.entity.undead.PharaohEntity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class PharaohModel<T extends PharaohEntity> extends BipedModel<T> {
    private final ModelRenderer waist;
    private final ModelRenderer hat;
    private final ModelRenderer beard;
    private final ModelRenderer rightItem;
    private final ModelRenderer leftItem;

    public PharaohModel(float modelSize) {
        super(modelSize);
        this.textureWidth = 64;
        this.textureHeight = 96;

        this.bipedBody = new ModelRenderer(this);
        this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedBody.setTextureOffset(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);
        this.bipedBody.setTextureOffset(0, 75).addBox(-6.0F, 3.0F, -1.5F, 12.0F, 18.0F, 3.0F, 0.9F, false);
        this.bipedBody.setTextureOffset(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.1F, false);
        this.bipedBody.setTextureOffset(16, 48).addBox(-4.0F, 9.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.1F, false);
        this.bipedBody.setTextureOffset(16, 64).addBox(-4.0F, 9.0F, -2.0F, 8.0F, 4.0F, 4.0F, 0.25F, false);

        this.waist = new ModelRenderer(this);
        this.waist.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.bipedBody.addChild(this.waist);

        this.bipedHead = new ModelRenderer(this);
        this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedHead.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

        this.hat = new ModelRenderer(this);
        this.hat.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedHead.addChild(this.hat);
        this.hat.setTextureOffset(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.1F, false);
        this.hat.setTextureOffset(34, 83).addBox(-6.0F, -10.0F, -2.0F, 12.0F, 10.0F, 3.0F, 0.0F, false);

        this.beard = new ModelRenderer(this);
        this.beard.setRotationPoint(0.0F, 0.0F, 2.0F);
        this.hat.addChild(this.beard);
        this.setRotationAngle(this.beard, 1.2217F, 0.0F, 0.0F);
        this.beard.setTextureOffset(50, 76).addBox(-1.0F, -5.7071F, -7.2929F, 2.0F, 0.0F, 5.0F, 0.0F, false);

        this.bipedRightArm = new ModelRenderer(this);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.bipedRightArm.setTextureOffset(48, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, 0.0F, false);
        this.bipedRightArm.setTextureOffset(48, 57).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 3.0F, 4.0F, 0.25F, false);
        this.bipedRightArm.setTextureOffset(48, 70).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, 0.5F, false);
        this.bipedRightArm.setTextureOffset(48, 44).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 2.0F, 4.0F, 0.1F, false);

        this.rightItem = new ModelRenderer(this);
        this.rightItem.setRotationPoint(-1.0F, 7.0F, 1.0F);
        this.bipedRightArm.addChild(this.rightItem);

        this.bipedLeftArm = new ModelRenderer(this);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.bipedLeftArm.setTextureOffset(40, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, 0.0F, true);
        this.bipedLeftArm.setTextureOffset(48, 50).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 3.0F, 4.0F, 0.25F, false);
        this.bipedLeftArm.setTextureOffset(48, 64).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, 0.5F, false);
        this.bipedLeftArm.setTextureOffset(48, 38).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 2.0F, 4.0F, 0.1F, false);

        this.leftItem = new ModelRenderer(this);
        this.leftItem.setRotationPoint(1.0F, 7.0F, 1.0F);
        this.bipedLeftArm.addChild(this.leftItem);

        this.bipedRightLeg = new ModelRenderer(this);
        this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
        this.bipedRightLeg.setTextureOffset(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, 0.0F, false);

        this.bipedLeftLeg = new ModelRenderer(this);
        this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
        this.bipedLeftLeg.setTextureOffset(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, 0.0F, true);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
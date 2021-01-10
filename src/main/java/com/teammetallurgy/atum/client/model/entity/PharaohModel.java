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
        textureWidth = 64;
        textureHeight = 96;

        waist = new ModelRenderer(this);
        waist.setRotationPoint(0.0F, 12.0F, 0.0F);

        bipedBody = new ModelRenderer(this);
        bipedBody.setRotationPoint(0.0F, -12.0F, 0.0F);
        bipedBody.addChild(waist);
        bipedBody.setTextureOffset(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);
        bipedBody.setTextureOffset(0, 75).addBox(-6.0F, 3.0F, -1.5F, 12.0F, 18.0F, 3.0F, 0.9F, false);
        bipedBody.setTextureOffset(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.1F, false);
        bipedBody.setTextureOffset(16, 48).addBox(-4.0F, 9.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.1F, false);
        bipedBody.setTextureOffset(16, 64).addBox(-4.0F, 9.0F, -2.0F, 8.0F, 4.0F, 4.0F, 0.25F, false);

        bipedHead = new ModelRenderer(this);
        bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        bipedHead.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

        hat = new ModelRenderer(this);
        hat.setRotationPoint(0.0F, 0.0F, 0.0F);
        bipedHead.addChild(hat);
        hat.setTextureOffset(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.25F, false);
        hat.setTextureOffset(34, 83).addBox(-6.0F, -10.0F, -2.0F, 12.0F, 10.0F, 3.0F, 0.0F, false);

        beard = new ModelRenderer(this);
        beard.setRotationPoint(0.0F, 0.0F, 2.0F);
        hat.addChild(beard);
        setRotationAngle(beard, 1.2217F, 0.0F, 0.0F);
        beard.setTextureOffset(50, 76).addBox(-1.0F, -5.7071F, -7.2929F, 2.0F, 0.0F, 5.0F, 0.0F, false);

        bipedRightArm = new ModelRenderer(this);
        bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        bipedRightArm.setTextureOffset(48, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, 0.0F, false);
        bipedRightArm.setTextureOffset(48, 57).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 3.0F, 4.0F, 0.25F, false);
        bipedRightArm.setTextureOffset(48, 70).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, 0.5F, false);
        bipedRightArm.setTextureOffset(48, 44).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 2.0F, 4.0F, 0.1F, false);

        rightItem = new ModelRenderer(this);
        rightItem.setRotationPoint(-1.0F, 7.0F, 1.0F);
        bipedRightArm.addChild(rightItem);


        bipedLeftArm = new ModelRenderer(this);
        bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        bipedLeftArm.setTextureOffset(40, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, 0.0F, true);
        bipedLeftArm.setTextureOffset(48, 50).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 3.0F, 4.0F, 0.25F, false);
        bipedLeftArm.setTextureOffset(48, 64).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, 0.5F, false);
        bipedLeftArm.setTextureOffset(48, 38).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 2.0F, 4.0F, 0.1F, false);

        leftItem = new ModelRenderer(this);
        leftItem.setRotationPoint(1.0F, 7.0F, 1.0F);
        bipedLeftArm.addChild(leftItem);


        bipedRightLeg = new ModelRenderer(this);
        bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
        bipedRightLeg.setTextureOffset(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, 0.0F, false);

        bipedLeftLeg = new ModelRenderer(this);
        bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
        bipedLeftLeg.setTextureOffset(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, 0.0F, true);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
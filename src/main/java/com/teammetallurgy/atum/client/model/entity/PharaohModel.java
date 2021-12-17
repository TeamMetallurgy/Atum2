package com.teammetallurgy.atum.client.model.entity;

import com.teammetallurgy.atum.entity.undead.PharaohEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class PharaohModel<T extends PharaohEntity> extends HumanoidModel<T> {

    public PharaohModel(ModelPart part) {
        super(part);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bipedBody = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 75).addBox(-6.0F, 3.0F, -1.5F, 12.0F, 18.0F, 3.0F, new CubeDeformation(0.9F))
                .texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.1F))
                .texOffs(16, 48).addBox(-4.0F, 9.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.1F))
                .texOffs(16, 64).addBox(-4.0F, 9.0F, -2.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        bipedBody.addOrReplaceChild("waist", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));
        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bipedRightArm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(48, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(48, 57).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.25F))
                .texOffs(48, 70).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.5F))
                .texOffs(48, 44).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(-5.0F, 2.0F, 0.0F));
        bipedRightArm.addOrReplaceChild("right_item", CubeListBuilder.create(), PartPose.offset(-1.0F, 7.0F, 1.0F));

        PartDefinition bipedLeftArm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(48, 50).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.25F))
                .texOffs(48, 64).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.5F))
                .texOffs(48, 38).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(5.0F, 2.0F, 0.0F));
        bipedLeftArm.addOrReplaceChild("left_item", CubeListBuilder.create(), PartPose.offset(1.0F, 7.0F, 1.0F));
        partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 12.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, 12.0F, 0.0F));

        PartDefinition bipedHeadware = partdefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.25F))
                .texOffs(34, 83).addBox(-6.0F, -10.0F, -2.0F, 12.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        bipedHeadware.addOrReplaceChild("beard", CubeListBuilder.create().texOffs(50, 76).addBox(-1.0F, -5.7071F, -7.2929F, 2.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 1.2217F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 96);
    }
}
package com.teammetallurgy.atum.client.model.entity;

import com.teammetallurgy.atum.entity.undead.PharaohEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class PharaohModel<T extends PharaohEntity> extends HumanoidModel<T> {
    private final ModelPart waist;
    private final ModelPart beard;
    private final ModelPart rightItem;
    private final ModelPart leftItem;

    public PharaohModel(ModelPart part) {
        super(part);
        this.waist = part.getChild("waist");
        this.beard = part.getChild("beard");
        this.rightItem = part.getChild("rightItem");
        this.leftItem = part.getChild("leftItem");

        /*
        this.body = new ModelPart(this); //TODO Needs re-exporting in Blockbench
        this.body.setPos(0.0F, 0.0F, 0.0F);
        this.body.texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);
        this.body.texOffs(0, 75).addBox(-6.0F, 3.0F, -1.5F, 12.0F, 18.0F, 3.0F, 0.9F, false);
        this.body.texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.1F, false);
        this.body.texOffs(16, 48).addBox(-4.0F, 9.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.1F, false);
        this.body.texOffs(16, 64).addBox(-4.0F, 9.0F, -2.0F, 8.0F, 4.0F, 4.0F, 0.25F, false);

        this.waist = new ModelPart(this);
        this.waist.setPos(0.0F, 12.0F, 0.0F);
        this.body.addChild(this.waist);

        this.head = new ModelPart(this);
        this.head.setPos(0.0F, 0.0F, 0.0F);
        this.head.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

        this.hat = new ModelPart(this);
        this.hat.setPos(0.0F, 0.0F, 0.0F);
        this.hat.texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.25F, false);
        this.hat.texOffs(34, 83).addBox(-6.0F, -10.0F, -2.0F, 12.0F, 10.0F, 3.0F, 0.0F, false);

        this.beard = new ModelPart(this);
        this.beard.setPos(0.0F, 0.0F, 2.0F);
        this.hat.addChild(this.beard);
        this.setRotationAngle(this.beard, 1.2217F, 0.0F, 0.0F);
        this.beard.texOffs(50, 76).addBox(-1.0F, -5.7071F, -7.2929F, 2.0F, 0.0F, 5.0F, 0.0F, false);

        this.rightArm = new ModelPart(this);
        this.rightArm.setPos(-5.0F, 2.0F, 0.0F);
        this.rightArm.texOffs(48, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, 0.0F, false);
        this.rightArm.texOffs(48, 57).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 3.0F, 4.0F, 0.25F, false);
        this.rightArm.texOffs(48, 70).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, 0.5F, false);
        this.rightArm.texOffs(48, 44).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 2.0F, 4.0F, 0.1F, false);

        this.rightItem = new ModelPart(this);
        this.rightItem.setPos(-1.0F, 7.0F, 1.0F);
        this.rightArm.addChild(this.rightItem);

        this.leftArm = new ModelPart(this);
        this.leftArm.setPos(5.0F, 2.0F, 0.0F);
        this.leftArm.texOffs(40, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, 0.0F, true);
        this.leftArm.texOffs(48, 50).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 3.0F, 4.0F, 0.25F, false);
        this.leftArm.texOffs(48, 64).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, 0.5F, false);
        this.leftArm.texOffs(48, 38).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 2.0F, 4.0F, 0.1F, false);

        this.leftItem = new ModelPart(this);
        this.leftItem.setPos(1.0F, 7.0F, 1.0F);
        this.leftArm.addChild(this.leftItem);

        this.rightLeg = new ModelPart(this);
        this.rightLeg.setPos(-2.0F, 12.0F, 0.0F);
        this.rightLeg.texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, 0.0F, false);

        this.leftLeg = new ModelPart(this);
        this.leftLeg.setPos(2.0F, 12.0F, 0.0F);
        this.leftLeg.texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, 0.0F, true);*/
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        return LayerDefinition.create(meshDefinition, 64, 96);
    }
}
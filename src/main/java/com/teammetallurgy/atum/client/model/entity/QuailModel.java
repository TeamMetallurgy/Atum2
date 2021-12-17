package com.teammetallurgy.atum.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.teammetallurgy.atum.entity.animal.QuailEntity;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

import javax.annotation.Nonnull;

public class QuailModel<T extends QuailEntity> extends AgeableListModel<T> {
    private final ModelPart beak;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    private final ModelPart leftFoot;
    private final ModelPart rightFoot;

    public QuailModel(ModelPart part) {
        super(false, 4.0F, 2.0F); //isChildHeadScaled, childHeadOffsetY, childHeadOffsetZ
        this.beak = part.getChild("beak");
        this.head = part.getChild("head");
        this.body = part.getChild("body");
        this.rightWing = part.getChild("right_wing");
        this.leftWing = part.getChild("left_wing");
        this.leftFoot = part.getChild("left_foot");
        this.rightFoot = part.getChild("right_foot");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("beak", CubeListBuilder.create().texOffs(14, 0).addBox(-1.0F, -2.0F, -4.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 17.0F, -4.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -4.0F, -2.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 17.0F, -4.0F));

        PartDefinition headFeather = head.addOrReplaceChild("head_feather", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 0.0F));
        headFeather.addOrReplaceChild("head_feather_r1", CubeListBuilder.create().texOffs(0, 24).addBox(0.0F, -7.2218F, -3.5355F, 0.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0436F, 0.0F));
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 9).addBox(-2.5F, -4.0F, -4.0F, 5.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 18.0F, 0.0F, 1.5708F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(24, 13).addBox(-1.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 16.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(24, 13).addBox(0.5F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 16.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_foot", CubeListBuilder.create().texOffs(26, 0).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 21.0F, 1.0F));
        partdefinition.addOrReplaceChild("right_foot", CubeListBuilder.create().texOffs(26, 0).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 21.0F, 1.0F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    @Nonnull
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.head, this.beak);
    }

    @Override
    @Nonnull
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body, this.rightFoot, this.leftFoot, this.rightWing, this.leftWing);
    }

    @Override
    public void setupAnim(@Nonnull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch * ((float) Math.PI / 180F);
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.beak.xRot = this.head.xRot;
        this.beak.yRot = this.head.yRot;
        this.body.xRot = ((float) Math.PI / 2F);
        this.rightFoot.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftFoot.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.rightWing.zRot = ageInTicks;
        this.leftWing.zRot = -ageInTicks;
    }

    private void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
package com.teammetallurgy.atum.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.teammetallurgy.atum.entity.animal.QuailEntity;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

import javax.annotation.Nonnull;

public class QuailModel<T extends QuailEntity> extends AgeableListModel<T> {
    public ModelPart beak;
    public ModelPart head;
    public ModelPart body;
    public ModelPart rightWing;
    public ModelPart leftWing;
    public ModelPart leftFoot;
    public ModelPart rightFoot;
    public ModelPart headFeather;
    public ModelPart headFeather1;

    public QuailModel(ModelPart part) {
        super(false, 4.0F, 2.0F); //isChildHeadScaled, childHeadOffsetY, childHeadOffsetZ
        this.beak = part.getChild("beak");
        this.head = part.getChild("head");
        this.body = part.getChild("body");
        this.rightWing = part.getChild("rightWing");
        this.leftWing = part.getChild("leftWing");
        this.leftFoot = part.getChild("leftFoot");
        this.rightFoot = part.getChild("rightFoot");
        this.headFeather = part.getChild("headFeather");
        /*this.beak = new ModelPart(this); //TODO Needs re-exporting from Blockbench
        this.beak.setPos(0.0F, 17.0F, -4.0F);
        this.beak.texOffs(14, 0).addBox(-1.0F, -2.0F, -4.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        this.head = new ModelPart(this);
        this.head.setPos(0.0F, 17.0F, -4.0F);
        this.head.texOffs(0, 0).addBox(-1.5F, -4.0F, -2.0F, 3.0F, 6.0F, 3.0F, 0.0F, false);
        this.headFeather = new ModelPart(this);
        this.headFeather.setPos(0.0F, 1.0F, 0.0F);
        this.head.addChild(this.headFeather);
        this.headFeather1 = new ModelPart(this);
        this.headFeather1.setPos(0.0F, 0.0F, 0.0F);
        this.headFeather.addChild(this.headFeather1);
        this.setRotateAngle(this.headFeather1, 0.0F, 0.0436F, 0.0F);
        this.headFeather1.texOffs(0, 24).addBox(0.0F, -7.2218F, -3.5355F, 0.0F, 3.0F, 2.0F, 0.0F, false);
        this.body = new ModelPart(this);
        this.body.setPos(0.0F, 18.0F, 0.0F);
        this.setRotateAngle(this.body, 1.5708F, 0.0F, 0.0F);
        this.body.texOffs(0, 9).addBox(-2.5F, -4.0F, -4.0F, 5.0F, 8.0F, 6.0F, 0.0F, false);
        this.rightWing = new ModelPart(this);
        this.rightWing.setPos(-2.5F, 16.0F, 0.0F);
        this.rightWing.texOffs(24, 13).addBox(-1.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F, 0.0F, false);
        this.leftWing = new ModelPart(this);
        this.leftWing.setPos(2.0F, 16.0F, 0.0F);
        this.leftWing.texOffs(24, 13).addBox(0.5F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F, 0.0F, false);
        this.leftFoot = new ModelPart(this);
        this.leftFoot.setPos(1.0F, 21.0F, 1.0F);
        this.leftFoot.texOffs(26, 0).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 3.0F, 3.0F, 0.0F, false);
        this.rightFoot = new ModelPart(this);
        this.rightFoot.setPos(-2.0F, 21.0F, 1.0F);
        this.rightFoot.texOffs(26, 0).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 3.0F, 3.0F, 0.0F, false);*/
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        return LayerDefinition.create(meshDefinition, 64, 32);
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
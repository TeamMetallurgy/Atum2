package com.teammetallurgy.atum.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.teammetallurgy.atum.entity.animal.ServalEntity;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.ModelUtils;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

import javax.annotation.Nonnull;

public class ServalModel<T extends ServalEntity> extends AgeableListModel<T> {
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart tail;
    private final ModelPart backLeftLeg;
    private final ModelPart backRightLeg;
    private final ModelPart frontLeftLeg;
    private final ModelPart frontRightLeg;
    private float lieDownAmount;
    private float lieDownAmountTail;
    private float relaxStateOneAmount;
    protected int state = 1;

    public ServalModel(ModelPart part) {
        super(true, 10.0F, 4.0F);
        this.body = part.getChild("body");
        this.head = part.getChild("head");
        this.tail = part.getChild("tail");
        this.backLeftLeg = part.getChild("backLeftLeg");
        this.backRightLeg = part.getChild("backRightLeg");
        this.frontLeftLeg = part.getChild("frontLeftLeg");
        this.frontRightLeg = part.getChild("frontRightLeg");
    }

    public static MeshDefinition createMesh(CubeDeformation cubeDeformation) {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(50, 21).addBox(2.0F, -5.0F, -8.0F, 1.0F, 5.0F, 6.0F, cubeDeformation)
                .texOffs(50, 10).addBox(-3.0F, -5.0F, -8.0F, 1.0F, 5.0F, 6.0F, cubeDeformation)
                .texOffs(26, 25).addBox(-3.0F, -6.0F, -8.0F, 6.0F, 1.0F, 6.0F, cubeDeformation)
                .texOffs(54, 6).addBox(-1.9F, -2.0F, -9.0F, 4.0F, 3.0F, 1.0F, cubeDeformation), PartPose.offset(0.0F, 17.0F, 1.0F));
        body.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(20, 0).addBox(-2.0F, -7.0F, 6.0F, 4.0F, 16.0F, 6.0F, cubeDeformation), PartPose.offsetAndRotation(0.0F, 7.0F, -1.0F, 1.5708F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -2.0F, -3.0F, 5.0F, 4.0F, 5.0F, cubeDeformation)
                .texOffs(0, 28).addBox(-1.5F, -0.0156F, -4.0F, 3.0F, 2.0F, 2.0F, cubeDeformation)
                .texOffs(26, 22).addBox(-2.0F, -6.0F, -1.0F, 1.0F, 4.0F, 2.0F, cubeDeformation)
                .texOffs(20, 22).addBox(1.0F, -6.0F, -1.0F, 1.0F, 4.0F, 2.0F, cubeDeformation), PartPose.offset(0.0F, 13.0F, -9.0F));

        PartDefinition tail = partdefinition.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, 15.0F, 8.0F));
        tail.addOrReplaceChild("tail_r1", CubeListBuilder.create().texOffs(0, 15).addBox(-0.5F, -5.0F, 13.0F, 1.0F, 11.0F, 1.0F, cubeDeformation), PartPose.offsetAndRotation(0.0F, 9.0F, -8.0F, 0.6109F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("backLeftLeg", CubeListBuilder.create().texOffs(8, 13).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, cubeDeformation), PartPose.offset(1.1F, 18.0F, 7.0F));
        partdefinition.addOrReplaceChild("backRightLeg", CubeListBuilder.create().texOffs(8, 13).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, cubeDeformation), PartPose.offset(-1.1F, 18.0F, 7.0F));
        partdefinition.addOrReplaceChild("frontLeftLeg", CubeListBuilder.create().texOffs(40, 0).addBox(-1.0F, -0.2F, -1.0F, 2.0F, 10.0F, 2.0F, cubeDeformation), PartPose.offset(1.2F, 14.0F, -4.0F));
        partdefinition.addOrReplaceChild("frontRightLeg", CubeListBuilder.create().texOffs(40, 0).addBox(-1.0F, -0.2F, -1.0F, 2.0F, 10.0F, 2.0F, cubeDeformation), PartPose.offset(-1.2F, 14.0F, -4.0F));

        return meshdefinition;
    }

    @Override
    @Nonnull
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    @Nonnull
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body, this.backLeftLeg, this.backRightLeg, this.frontLeftLeg, this.frontRightLeg, this.tail);
    }

    public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    @Override
    public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
        this.lieDownAmount = entity.getLieDownAmount(partialTick);
        this.lieDownAmountTail = entity.getLieDownAmountTail(partialTick);
        this.relaxStateOneAmount = entity.getRelaxStateOneAmount(partialTick);
        if (this.lieDownAmount <= 0.0F) {
            this.head.xRot = 0.0F;
            this.head.zRot = 0.0F;
            this.frontLeftLeg.xRot = 0.0F;
            this.frontLeftLeg.zRot = 0.0F;
            this.frontRightLeg.xRot = 0.0F;
            this.frontRightLeg.zRot = 0.0F;
            this.frontRightLeg.x = -1.2F;
            this.backLeftLeg.xRot = 0.0F;
            this.backRightLeg.xRot = 0.0F;
            this.backRightLeg.zRot = 0.0F;
            this.backRightLeg.x = -1.1F;
            this.backRightLeg.y = 18.0F;
        }

        this.baseLivingAnimations(entity, limbSwing, limbSwingAmount, partialTick);
        if (entity.isInSittingPose()) {
            this.body.xRot = ((float) Math.PI / -4F);
            this.body.y += 2.0F;
            this.body.z += -4.0F;
            this.head.y += -3.3F;
            ++this.head.z;
            this.tail.y += 8.0F;
            this.tail.z += -2.0F;
            this.tail.xRot = 1.7278761F;
            this.frontLeftLeg.xRot = -0.15707964F;
            this.frontLeftLeg.y = 16.1F;
            this.frontLeftLeg.z = -7.0F;
            this.frontRightLeg.xRot = -0.15707964F;
            this.frontRightLeg.y = 16.1F;
            this.frontRightLeg.z = -7.0F;
            this.backLeftLeg.xRot = (-(float) Math.PI / 2F);
            this.backLeftLeg.y = 21.0F;
            this.backLeftLeg.z = 1.0F;
            this.backRightLeg.xRot = (-(float) Math.PI / 2F);
            this.backRightLeg.y = 21.0F;
            this.backRightLeg.z = 1.0F;
            this.state = 3;
        }
    }

    public void baseLivingAnimations(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
        body.setPos(0.0F, 17.0F, 1.0F);
        //body_r1.setPos(0.0F, 7.0F, -1.0F); //TODO Check if it´s fine without
        head.setPos(0.0F, 13.0F, -9.0F);
        tail.setPos(0.0F, 15.0F, 8.0F);
        backLeftLeg.setPos(1.1F, 18.0F, 7.0F);
        backRightLeg.setPos(-1.1F, 18.0F, 7.0F);
        frontLeftLeg.setPos(1.2F, 14.0F, -4.0F);
        frontRightLeg.setPos(-1.2F, 14.0F, -4.0F);
        this.tail.xRot = 0.9F;
        if (entity.isCrouching()) {
            ++this.body.y;
            this.head.y += 2.0F;
            ++this.tail.y;
            this.tail.xRot = ((float) Math.PI / 2F);
            this.state = 0;
        } else if (entity.isSprinting()) {
            this.tail.xRot = ((float) Math.PI / 2F);
            this.state = 2;
        } else {
            this.state = 1;
        }

    }

    @Override
    public void setupAnim(@Nonnull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.baseRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        if (this.lieDownAmount > 0.0F) {
            this.head.zRot = ModelUtils.rotlerpRad(this.head.zRot, -1.2707963F, this.lieDownAmount);
            this.head.yRot = ModelUtils.rotlerpRad(this.head.yRot, 1.2707963F, this.lieDownAmount);
            this.frontLeftLeg.xRot = -1.2707963F;
            this.frontRightLeg.xRot = -0.47079635F;
            this.frontRightLeg.zRot = -0.2F;
            this.frontRightLeg.x = -0.2F;
            this.backLeftLeg.xRot = -0.4F;
            this.backRightLeg.xRot = 0.5F;
            this.backRightLeg.zRot = -0.5F;
            this.backRightLeg.x = -0.3F;
            this.backRightLeg.y = 20.0F;
            this.tail.xRot = ModelUtils.rotlerpRad(this.tail.xRot, 0.8F, this.lieDownAmountTail);
        }

        if (this.relaxStateOneAmount > 0.0F) {
            this.head.xRot = ModelUtils.rotlerpRad(this.head.xRot, -0.58177644F, this.relaxStateOneAmount);
        }
    }

    public void baseRotationAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch * ((float) Math.PI / 180F);
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        if (this.state != 3) {
            this.body.xRot = ((float) Math.PI / 180F);
            if (this.state == 2) {
                this.backLeftLeg.xRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
                this.backRightLeg.xRot = Mth.cos(limbSwing * 0.6662F + 0.3F) * limbSwingAmount;
                this.frontLeftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI + 0.3F) * limbSwingAmount;
                this.frontRightLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
            } else {
                this.backLeftLeg.xRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
                this.backRightLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
                this.frontLeftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
                this.frontRightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
            }
        }
    }
}
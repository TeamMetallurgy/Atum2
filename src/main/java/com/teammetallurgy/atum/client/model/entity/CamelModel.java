package com.teammetallurgy.atum.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teammetallurgy.atum.entity.animal.CamelEntity;
import net.minecraft.client.animation.definitions.CamelAnimation;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

import javax.annotation.Nonnull;

public class CamelModel<T extends CamelEntity> extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart[] saddleParts;
    private final ModelPart[] ridingParts;
    private final ModelPart chestRight;
    private final ModelPart chestLeft;

    public CamelModel(ModelPart part) {
        this.root = part;
        ModelPart modelpart = part.getChild("body");
        this.head = modelpart.getChild("head");
        this.saddleParts = new ModelPart[]{modelpart.getChild("saddle"), this.head.getChild("bridle")};
        this.ridingParts = new ModelPart[]{this.head.getChild("reins")};
        this.chestRight = modelpart.getChild("chest_right");
        this.chestLeft = modelpart.getChild("chest_left");
    }

    public static MeshDefinition createMesh(CubeDeformation cubeDeformation) {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition partdefinition1 = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 25).addBox(-7.5F, -12.0F, -23.5F, 15.0F, 12.0F, 27.0F), PartPose.offset(0.0F, 4.0F, 9.5F));
        partdefinition1.addOrReplaceChild("hump", CubeListBuilder.create().texOffs(74, 0).addBox(-4.5F, -5.0F, -5.5F, 9.0F, 5.0F, 11.0F), PartPose.offset(0.0F, -12.0F, -10.0F));
        partdefinition1.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(122, 0).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 14.0F, 0.0F), PartPose.offset(0.0F, -9.0F, 3.5F));
        PartDefinition partdefinition2 = partdefinition1.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(60, 24)
                        .addBox(-3.5F, -7.0F, -15.0F, 7.0F, 8.0F, 19.0F)
                        .texOffs(21, 0)
                        .addBox(-3.5F, -21.0F, -15.0F, 7.0F, 14.0F, 7.0F)
                        .texOffs(50, 0)
                        .addBox(-2.5F, -21.0F, -21.0F, 5.0F, 5.0F, 6.0F),
                PartPose.offset(0.0F, -3.0F, -19.5F)
        );
        partdefinition2.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(45, 0).addBox(-0.5F, 0.5F, -1.0F, 3.0F, 1.0F, 2.0F), PartPose.offset(2.5F, -21.0F, -9.5F));
        partdefinition2.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(67, 0).addBox(-2.5F, 0.5F, -1.0F, 3.0F, 1.0F, 2.0F), PartPose.offset(-2.5F, -21.0F, -9.5F));
        partdefinition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(58, 16).addBox(-2.5F, 2.0F, -2.5F, 5.0F, 21.0F, 5.0F), PartPose.offset(4.9F, 1.0F, 9.5F));
        partdefinition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(94, 16).addBox(-2.5F, 2.0F, -2.5F, 5.0F, 21.0F, 5.0F), PartPose.offset(-4.9F, 1.0F, 9.5F));
        partdefinition.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, 2.0F, -2.5F, 5.0F, 21.0F, 5.0F), PartPose.offset(4.9F, 1.0F, -10.5F));
        partdefinition.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(0, 26).addBox(-2.5F, 2.0F, -2.5F, 5.0F, 21.0F, 5.0F), PartPose.offset(-4.9F, 1.0F, -10.5F));
        partdefinition1.addOrReplaceChild(
                "saddle",
                CubeListBuilder.create()
                        .texOffs(74, 64)
                        .addBox(-4.5F, -17.0F, -15.5F, 9.0F, 5.0F, 11.0F, cubeDeformation)
                        .texOffs(92, 114)
                        .addBox(-3.5F, -20.0F, -15.5F, 7.0F, 3.0F, 11.0F, cubeDeformation)
                        .texOffs(0, 89)
                        .addBox(-7.5F, -12.0F, -23.5F, 15.0F, 12.0F, 27.0F, cubeDeformation),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );
        partdefinition2.addOrReplaceChild(
                "reins",
                CubeListBuilder.create()
                        .texOffs(98, 42)
                        .addBox(3.51F, -18.0F, -17.0F, 0.0F, 7.0F, 15.0F)
                        .texOffs(84, 57)
                        .addBox(-3.5F, -18.0F, -2.0F, 7.0F, 7.0F, 0.0F)
                        .texOffs(98, 42)
                        .addBox(-3.51F, -18.0F, -17.0F, 0.0F, 7.0F, 15.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );
        partdefinition2.addOrReplaceChild(
                "bridle",
                CubeListBuilder.create()
                        .texOffs(60, 87)
                        .addBox(-3.5F, -7.0F, -15.0F, 7.0F, 8.0F, 19.0F, cubeDeformation)
                        .texOffs(21, 64)
                        .addBox(-3.5F, -21.0F, -15.0F, 7.0F, 14.0F, 7.0F, cubeDeformation)
                        .texOffs(50, 64)
                        .addBox(-2.5F, -21.0F, -21.0F, 5.0F, 5.0F, 6.0F, cubeDeformation)
                        .texOffs(74, 70)
                        .addBox(2.5F, -19.0F, -18.0F, 1.0F, 2.0F, 2.0F)
                        .texOffs(74, 70)
                        .mirror()
                        .addBox(-3.5F, -19.0F, -18.0F, 1.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );
        partdefinition1.addOrReplaceChild("chest_right", CubeListBuilder.create().texOffs(45, 28).addBox(-5.0F, 0.0F, 0.0F, 8.0F, 8.0F, 3.0F, cubeDeformation), PartPose.offsetAndRotation(8.5F, 3.0F, 3.0F, 0.0F, -1.5708F, 0.0F));
        partdefinition1.addOrReplaceChild("chest_left", CubeListBuilder.create().texOffs(45, 41).addBox(-5.0F, 0.0F, 0.0F, 8.0F, 8.0F, 3.0F, cubeDeformation), PartPose.offsetAndRotation(-5.5F, 3.0F, 3.0F, 0.0F, -1.5708F, 0.0F));

        return meshdefinition;
    }

    @Override
    public void setupAnim(@Nonnull T camel, float limbSwing, float limbSwingAmount, float ageInTicks, float netheadModelYaw, float headModelPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(camel, netheadModelYaw, headModelPitch, ageInTicks);
        this.toggleInvisibleParts(camel);
        this.animateWalk(CamelAnimation.CAMEL_WALK, limbSwing, limbSwingAmount, 2.0F, 2.5F);
        this.animate(camel.sitAnimationState, CamelAnimation.CAMEL_SIT, ageInTicks, 1.0F);
        this.animate(camel.sitPoseAnimationState, CamelAnimation.CAMEL_SIT_POSE, ageInTicks, 1.0F);
        this.animate(camel.sitUpAnimationState, CamelAnimation.CAMEL_STANDUP, ageInTicks, 1.0F);
        this.animate(camel.idleAnimationState, CamelAnimation.CAMEL_IDLE, ageInTicks, 1.0F);
        this.animate(camel.dashAnimationState, CamelAnimation.CAMEL_DASH, ageInTicks, 1.0F);

        this.chestLeft.visible = camel.hasLeftCrate();
        this.chestRight.visible = camel.hasRightCrate();
    }

    private void applyHeadRotation(T camel, float netheadModelYaw, float headModelPitch, float ageInTicks) {
        netheadModelYaw = Mth.clamp(netheadModelYaw, -30.0F, 30.0F);
        headModelPitch = Mth.clamp(headModelPitch, -25.0F, 45.0F);
        if (camel.getJumpCooldown() > 0) {
            float f = ageInTicks - (float)camel.tickCount;
            float f1 = 45.0F * ((float)camel.getJumpCooldown() - f) / 55.0F;
            headModelPitch = Mth.clamp(headModelPitch + f1, -25.0F, 70.0F);
        }

        this.head.yRot = netheadModelYaw * (float) (Math.PI / 180.0);
        this.head.xRot = headModelPitch * (float) (Math.PI / 180.0);
    }

    private void toggleInvisibleParts(T camel) {
        boolean flag = camel.isSaddled();
        boolean flag1 = camel.isVehicle();

        for(ModelPart modelpart : this.saddleParts) {
            modelpart.visible = flag;
        }

        for(ModelPart modelpart1 : this.ridingParts) {
            modelpart1.visible = flag1 && flag;
        }
    }

    @Override
    public void renderToBuffer(@Nonnull PoseStack poseStack, @Nonnull VertexConsumer vertexConsumer, int limbSwing, int limbSwingAmount, float ageInTicks, float netheadModelYaw, float headModelPitch, float scale) {
        if (this.young) {
            poseStack.pushPose();
            poseStack.scale(0.45F, 0.45F, 0.45F);
            poseStack.translate(0.0F, 1.834375F, 0.0F);
            this.root().render(poseStack, vertexConsumer, limbSwing, limbSwingAmount, ageInTicks, netheadModelYaw, headModelPitch, scale);
            poseStack.popPose();
        } else {
            this.root().render(poseStack, vertexConsumer, limbSwing, limbSwingAmount, ageInTicks, netheadModelYaw, headModelPitch, scale);
        }
    }

    @Override
    @Nonnull
    public ModelPart root() {
        return this.root;
    }
}
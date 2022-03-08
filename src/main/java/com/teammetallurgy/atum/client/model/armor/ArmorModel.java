package com.teammetallurgy.atum.client.model.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;

import javax.annotation.Nonnull;

public class ArmorModel extends HumanoidModel<LivingEntity> {
    private final EquipmentSlot slot;

    public ArmorModel(ModelPart part, EquipmentSlot slot) {
        super(part);
        this.slot = slot;
    }

    public EquipmentSlot getSlot() {
        return this.slot;
    }

    @Override
    public void setupAnim(@Nonnull LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) { //Fixes armor on armor stands
        if (!(entity instanceof ArmorStand armorStand)) {
            super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            return;
        }

        this.head.xRot = ((float) Math.PI / 180F) * armorStand.getHeadPose().getX();
        this.head.yRot = ((float) Math.PI / 180F) * armorStand.getHeadPose().getY();
        this.head.zRot = ((float) Math.PI / 180F) * armorStand.getHeadPose().getZ();
        this.head.setPos(0.0F, 1.0F, 0.0F);
        this.body.xRot = ((float) Math.PI / 180F) * armorStand.getBodyPose().getX();
        this.body.yRot = ((float) Math.PI / 180F) * armorStand.getBodyPose().getY();
        this.body.zRot = ((float) Math.PI / 180F) * armorStand.getBodyPose().getZ();
        this.leftArm.xRot = ((float) Math.PI / 180F) * armorStand.getLeftArmPose().getX();
        this.leftArm.yRot = ((float) Math.PI / 180F) * armorStand.getLeftArmPose().getY();
        this.leftArm.zRot = ((float) Math.PI / 180F) * armorStand.getLeftArmPose().getZ();
        this.rightArm.xRot = ((float) Math.PI / 180F) * armorStand.getRightArmPose().getX();
        this.rightArm.yRot = ((float) Math.PI / 180F) * armorStand.getRightArmPose().getY();
        this.rightArm.zRot = ((float) Math.PI / 180F) * armorStand.getRightArmPose().getZ();
        this.leftLeg.xRot = ((float) Math.PI / 180F) * armorStand.getLeftLegPose().getX();
        this.leftLeg.yRot = ((float) Math.PI / 180F) * armorStand.getLeftLegPose().getY();
        this.leftLeg.zRot = ((float) Math.PI / 180F) * armorStand.getLeftLegPose().getZ();
        this.leftLeg.setPos(1.9F, 11.0F, 0.0F);
        this.rightLeg.xRot = ((float) Math.PI / 180F) * armorStand.getRightLegPose().getX();
        this.rightLeg.yRot = ((float) Math.PI / 180F) * armorStand.getRightLegPose().getY();
        this.rightLeg.zRot = ((float) Math.PI / 180F) * armorStand.getRightLegPose().getZ();
        this.rightLeg.setPos(-1.9F, 11.0F, 0.0F);
        this.hat.copyFrom(this.head);
    }
}
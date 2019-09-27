package com.teammetallurgy.atum.client.model.entity;

import com.teammetallurgy.atum.entity.stone.StonewardenEntity;
import net.minecraft.client.model.ModelIronGolem;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StonewardenModel extends ModelIronGolem {

    @Override
    public void setLivingAnimations(LivingEntity livingBase, float limbSwing, float limbSwingAmount, float partialTickTime) {
        StonewardenEntity stonewarden = (StonewardenEntity) livingBase;
        int attackTimer = stonewarden.getAttackTimer();

        if (attackTimer > 0) {
            this.ironGolemRightArm.rotateAngleX = -2.0F + 1.5F * this.triangleWave((float) attackTimer - partialTickTime, 10.0F);
            this.ironGolemLeftArm.rotateAngleX = -2.0F + 1.5F * this.triangleWave((float) attackTimer - partialTickTime, 10.0F);
        } else {
            this.ironGolemRightArm.rotateAngleX = (-0.2F + 1.5F * this.triangleWave(limbSwing, 13.0F)) * limbSwingAmount;
            this.ironGolemLeftArm.rotateAngleX = (-0.2F - 1.5F * this.triangleWave(limbSwing, 13.0F)) * limbSwingAmount;
        }
    }

    private float triangleWave(float a, float b) {
        return (Math.abs(a % b - b * 0.5F) - b * 0.25F) / (b * 0.25F);
    }
}
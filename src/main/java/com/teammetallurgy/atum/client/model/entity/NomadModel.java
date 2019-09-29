package com.teammetallurgy.atum.client.model.entity;

import com.teammetallurgy.atum.entity.bandit.NomadEntity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NomadModel<T extends NomadEntity> extends PlayerModel<T> {

    public NomadModel() {
        this(0.0F);
    }

    public NomadModel(float size) {
        super(size, false);
    }

    @Override
    public void setLivingAnimations(T nomad, float limbSwing, float limbSwingAmount, float partialTickTime) {
        this.rightArmPose = BipedModel.ArmPose.EMPTY;
        this.leftArmPose = BipedModel.ArmPose.EMPTY;
        ItemStack itemstack = nomad.getHeldItem(Hand.MAIN_HAND);

        if (itemstack.getItem() instanceof BowItem && nomad.isAggressive()) {
            if (nomad.getPrimaryHand() == HandSide.RIGHT) {
                this.rightArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
            } else {
                this.leftArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
            }
        }
        super.setLivingAnimations(nomad, limbSwing, limbSwingAmount, partialTickTime);
    }
}
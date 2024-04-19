package com.teammetallurgy.atum.client.model.entity;

import com.teammetallurgy.atum.entity.bandit.NomadEntity;
import com.teammetallurgy.atum.misc.StackHelper;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NomadModel<T extends NomadEntity> extends PlayerModel<T> {

    public NomadModel(EntityRendererProvider.Context context) {
        super(context.bakeLayer(ModelLayers.PLAYER), false);
    }

    @Override
    public void prepareMobModel(T nomad, float limbSwing, float limbSwingAmount, float partialTickTime) {
        this.rightArmPose = HumanoidModel.ArmPose.EMPTY;
        this.leftArmPose = HumanoidModel.ArmPose.EMPTY;
        InteractionHand hand = StackHelper.getUsedHand(nomad.getMainHandItem(), BowItem.class);
        ItemStack heldStack = nomad.getItemInHand(hand);

        if (heldStack.getItem() instanceof BowItem && nomad.isAggressive()) {
            if (nomad.getMainArm() == HumanoidArm.RIGHT) {
                this.rightArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
            } else {
                this.leftArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
            }
        }
        super.prepareMobModel(nomad, limbSwing, limbSwingAmount, partialTickTime);
    }
}
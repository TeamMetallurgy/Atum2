package com.teammetallurgy.atum.client.model.entity;

import com.teammetallurgy.atum.entity.bandit.NomadEntity;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.Hand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelNomad extends ModelPlayer {

    public ModelNomad() {
        super(0.0F, false);
    }

    @Override
    public void setLivingAnimations(LivingEntity livingBase, float limbSwing, float limbSwingAmount, float partialTickTime) {
        this.rightArmPose = ModelBiped.ArmPose.EMPTY;
        this.leftArmPose = ModelBiped.ArmPose.EMPTY;
        ItemStack itemstack = livingBase.getHeldItem(Hand.MAIN_HAND);

        if (itemstack.getItem() instanceof ItemBow && ((NomadEntity) livingBase).isSwingingArms()) {
            if (livingBase.getPrimaryHand() == EnumHandSide.RIGHT) {
                this.rightArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
            } else {
                this.leftArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
            }
        }
        super.setLivingAnimations(livingBase, limbSwing, limbSwingAmount, partialTickTime);
    }
}

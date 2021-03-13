package com.teammetallurgy.atum.client.model.armor;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, value = Dist.CLIENT)
public class RaArmorModel extends ArmorModel {
    private boolean hasFullSet;

    public RaArmorModel(EquipmentSlotType slot, boolean hasFullSet) {
        super(slot);
        this.hasFullSet = hasFullSet;
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, @Nonnull IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        EquipmentSlotType slot = this.getSlot();

        this.bipedHeadwear.showModel = false; //Hide vanilla stuff

        this.bipedHead.showModel = slot == EquipmentSlotType.HEAD;
        this.bipedBody.showModel = slot == EquipmentSlotType.CHEST;
        this.bipedLeftArm.showModel = slot == EquipmentSlotType.CHEST;
        this.bipedRightArm.showModel = slot == EquipmentSlotType.CHEST;
        this.bipedLeftLeg.showModel = slot == EquipmentSlotType.LEGS;
        this.bipedRightLeg.showModel = slot == EquipmentSlotType.LEGS;
        /*this.leftBoot.showModel = slot == EquipmentSlotType.FEET; //TODO Uncommen
        this.rightBoot.showModel = slot == EquipmentSlotType.FEET;
        this.leftCape.showModel = this.hasFullSet;
        this.rightCape.showModel = this.hasFullSet;

        this.leftBoot.copyModelAngles(this.bipedLeftLeg);
        this.rightBoot.copyModelAngles(this.bipedRightLeg);
        this.rightBoot.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.leftBoot.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);*/

        super.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @SubscribeEvent
    public static void onPlayerRender(RenderPlayerEvent event) {
        PlayerEntity player = event.getPlayer();
        PlayerModel<AbstractClientPlayerEntity> playerModel = event.getRenderer().getEntityModel();
        if (player.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == AtumItems.HALO_OF_RA) {
            playerModel.bipedHeadwear.showModel = false;
        }
        if (player.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() == AtumItems.BODY_OF_RA) {
            playerModel.bipedBodyWear.showModel = false;
            playerModel.bipedLeftArmwear.showModel = false;
            playerModel.bipedRightArmwear.showModel = false;
        }
        if (player.getItemStackFromSlot(EquipmentSlotType.LEGS).getItem() == AtumItems.LEGS_OF_RA || player.getItemStackFromSlot(EquipmentSlotType.FEET).getItem() == AtumItems.FEET_OF_RA) {
            playerModel.bipedLeftLegwear.showModel = false;
            playerModel.bipedRightLegwear.showModel = false;
        }
    }
}
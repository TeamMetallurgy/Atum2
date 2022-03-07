package com.teammetallurgy.atum.client.model.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.client.ClientHandler;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, value = Dist.CLIENT)
public class RaArmorModel extends ArmorModel {
    private boolean hasFullSet;
    private final ModelPart leftBoot;
    private final ModelPart rightBoot;
    private final ModelPart rightCape;
    private final ModelPart leftCape;

    public RaArmorModel(ModelPart part, EquipmentSlot slot, boolean hasFullSet) {
        super(part, slot);
        this.hasFullSet = hasFullSet;
        this.rightCape = part.getChild("right_cape");
        this.leftCape = part.getChild("left_cape");
        this.leftBoot = part.getChild("left_boot");
        this.rightBoot = part.getChild("right_boot");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bipedBody = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        bipedBody.addOrReplaceChild("chestplate", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, -22.0F, -8.5F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition bipedRightArm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.1F))
                .texOffs(8, 86).mirror().addBox(-3.0F, 6.0F, -2.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.4F)).mirror(false)
                .texOffs(48, 88).mirror().addBox(-3.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.35F)).mirror(false)
                .texOffs(48, 80).mirror().addBox(-3.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.65F)).mirror(false), PartPose.offset(-5.0F, 2.0F, 0.0F));
        bipedRightArm.addOrReplaceChild("right_shoulder_blade", CubeListBuilder.create().texOffs(18, 90).mirror().addBox(-14.0F, -3.0F, -1.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offset(10.0F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("right_cape", CubeListBuilder.create().texOffs(56, 64).mirror().addBox(-7.5F + 5.0F, 4.0F - 2.0F, 2.6F, 4.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, -2.0F, 0.0F));

        PartDefinition bipedLeftArm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)).mirror(false)
                .texOffs(52, 30).mirror().addBox(1.0F, 6.0F, -2.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.4F)).mirror(false)
                .texOffs(32, 88).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.35F)).mirror(false)
                .texOffs(32, 80).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.65F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));
        bipedLeftArm.addOrReplaceChild("left_shoulder_blade", CubeListBuilder.create().texOffs(18, 82).mirror().addBox(0.0F, -3.0F, -1.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("left_cape", CubeListBuilder.create().texOffs(48, 64).mirror().addBox(3.5F - 5.0F, -20.0F + 22.0F, 2.6F, 4.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.0F, 22.0F, 0.0F));

        PartDefinition bipedHead = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.1F))
                .texOffs(24, 0).addBox(-5.0F, -6.0F, -5.0F, 10.0F, 4.0F, 2.0F, new CubeDeformation(-0.25F))
                .texOffs(58, 0).addBox(-1.0F, -9.0F, -4.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(34, 50).addBox(-6.0F, -10.0F, -2.0F, 12.0F, 10.0F, 3.0F, new CubeDeformation(0.4F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        bipedHead.addOrReplaceChild("horns", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));
        bipedHead.addOrReplaceChild("postiche", CubeListBuilder.create().texOffs(0, 86).addBox(-1.0F, -3.0F, -7.0F, 2.0F, 0.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition hat = bipedHead.addOrReplaceChild("head_top", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.4F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        hat.addOrReplaceChild("horn_left_r1", CubeListBuilder.create().texOffs(24, 51).mirror().addBox(-8.0F, -14.0F, -1.0F, 4.0F, 11.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(24, 51).addBox(4.0F, -14.0F, -1.0F, 4.0F, 11.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.1F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 59).addBox(-2.1F, -3.0F, -2.0F, 4.0F, 15.0F, 4.0F, new CubeDeformation(0.4F))
                .texOffs(0, 67).addBox(-2.1F, -3.0F, -2.0F, 4.0F, 15.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offset(-1.9F, 12.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 32).addBox(-1.9F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 40).addBox(-1.9F, -3.0F, -2.0F, 4.0F, 15.0F, 4.0F, new CubeDeformation(0.4F))
                .texOffs(0, 48).addBox(-1.9F, -3.0F, -2.0F, 4.0F, 15.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offset(1.9F, 12.0F, 0.0F));

        //Boots manually edited in code
        partdefinition.addOrReplaceChild("left_boot", CubeListBuilder.create().texOffs(32, 63).mirror().addBox(0.1F - 2, -6.2F + 12, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.1F)).mirror(false)
                .texOffs(29, 41).mirror().addBox(0.4F - 2, -2.0F + 12, -3.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offset(0.0F, 24.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_boot", CubeListBuilder.create().texOffs(32, 63).mirror().addBox(-4.1F + 2, -6.2F + 12, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.1F)).mirror(false)
                .texOffs(29, 41).mirror().addBox(-3.4F + 2, -2.0F + 12, -3.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 96);
    }

    @Override
    public void renderToBuffer(@Nonnull PoseStack matrixStack, @Nonnull VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        EquipmentSlot slot = this.getSlot();

        this.hat.visible = false; //Hide vanilla stuff

        this.head.visible = slot == EquipmentSlot.HEAD;
        this.body.visible = slot == EquipmentSlot.CHEST;
        this.leftArm.visible = slot == EquipmentSlot.CHEST;
        this.rightArm.visible = slot == EquipmentSlot.CHEST;
        this.leftLeg.visible = slot == EquipmentSlot.LEGS;
        this.rightLeg.visible = slot == EquipmentSlot.LEGS;
        this.leftBoot.visible = slot == EquipmentSlot.FEET;
        this.rightBoot.visible = slot == EquipmentSlot.FEET;
        this.leftCape.visible = this.hasFullSet;
        this.rightCape.visible = this.hasFullSet;

        this.leftCape.copyFrom(this.leftArm);
        this.rightCape.copyFrom(this.rightArm);
        this.leftBoot.copyFrom(this.leftLeg);
        this.rightBoot.copyFrom(this.rightLeg);

        this.leftCape.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.rightCape.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.rightBoot.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.leftBoot.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);

        super.renderToBuffer(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @SubscribeEvent
    public static void onPlayerRender(RenderPlayerEvent event) {
        Player player = event.getPlayer();
        PlayerModel<AbstractClientPlayer> playerModel = event.getRenderer().getModel();
        if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() == AtumItems.HALO_OF_RA.get()) {
            playerModel.hat.visible = false;
            playerModel.head.visible = false;
        }
        if (player.getItemBySlot(EquipmentSlot.CHEST).getItem() == AtumItems.BODY_OF_RA.get()) {
            playerModel.jacket.visible = false;
            playerModel.leftSleeve.visible = false;
            playerModel.rightSleeve.visible = false;
        }
        if (player.getItemBySlot(EquipmentSlot.LEGS).getItem() == AtumItems.LEGS_OF_RA.get() || player.getItemBySlot(EquipmentSlot.FEET).getItem() == AtumItems.FEET_OF_RA.get()) {
            playerModel.leftPants.visible = false;
            playerModel.rightPants.visible = false;
        }
    }
}
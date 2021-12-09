package com.teammetallurgy.atum.client.model.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.client.ClientHandler;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, value = Dist.CLIENT)
public class AtemArmorModel extends ArmorModel {
	private boolean hasFullSet;
	private final ModelPart fins;
	private final ModelPart hat;
	private final ModelPart postiche;
	private final ModelPart chestplate;
	private final ModelPart rightShoulderBlade;
	private final ModelPart leftShoulderBlade;
	private final ModelPart leftBoot;
	private final ModelPart rightBoot;
	private final ModelPart leftCape;
	private final ModelPart rightCape;

	public AtemArmorModel(EquipmentSlot slot, boolean hasFullSet) {
		super(slot);
		ModelPart part = Minecraft.getInstance().getEntityModels().bakeLayer(ClientHandler.ATEM_ARMOR);
		this.hasFullSet = hasFullSet;
		this.fins = part.getChild("fins");
		this.hat = part.getChild("hat");
		this.postiche = part.getChild("postiche");
		this.chestplate = part.getChild("chestplate");
		this.rightShoulderBlade = part.getChild("rightShoulderBlade");
		this.leftShoulderBlade = part.getChild("leftShoulderBlade");
		this.leftBoot = part.getChild("leftBoot");
		this.rightBoot = part.getChild("rightBoot");
		this.leftCape = part.getChild("leftCape");
		this.rightCape = part.getChild("rightCape");
		/*this.texWidth = 64; //TODO Requires to be re-exported in Blockbench
		this.texHeight = 96;

		body = new ModelPart(this);
		body.setPos(0.0F, 0.0F, 0.0F);
		body.texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.2F, false);

		chestplate = new ModelPart(this);
		chestplate.setPos(0.0F, 24.0F, 0.0F);
		body.addChild(chestplate);
		setRotationAngle(chestplate, -0.2618F, 0.0F, 0.0F);
		chestplate.texOffs(16, 32).addBox(-4.0F, -22.0F, -8.5F, 8.0F, 4.0F, 4.0F, 0.0F, false);

		rightArm = new ModelPart(this);
		rightArm.setPos(-5.0F, 2.0F, 0.0F);
		rightArm.texOffs(40, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.1F, false);
		rightArm.texOffs(8, 86).addBox(-3.0F, 6.0F, -2.0F, 2.0F, 2.0F, 4.0F, 0.4F, true);
		rightArm.texOffs(48, 88).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.35F, true);
		rightArm.texOffs(48, 80).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.65F, true);

		rightShoulderBlade = new ModelPart(this);
		rightShoulderBlade.setPos(10.0F, 0.0F, 0.0F);
		rightArm.addChild(rightShoulderBlade);
		rightShoulderBlade.texOffs(18, 90).addBox(-14.0F, -3.0F, -1.0F, 4.0F, 4.0F, 2.0F, 0.1F, true);

		rightCape = new ModelPart(this);
		rightCape.setPos(5.0F, -2.0F, 0.0F);
		rightArm.addChild(rightCape);
		rightCape.texOffs(56, 64).addBox(-7.5F, 4.0F, 2.6F, 4.0F, 16.0F, 0.0F, 0.0F, true);

		leftArm = new ModelPart(this);
		leftArm.setPos(5.0F, 2.0F, 0.0F);
		leftArm.texOffs(40, 16).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.1F, true);
		leftArm.texOffs(52, 30).addBox(1.0F, 6.0F, -2.0F, 2.0F, 2.0F, 4.0F, 0.4F, true);
		leftArm.texOffs(32, 88).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.35F, true);
		leftArm.texOffs(32, 80).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.65F, true);

		leftShoulderBlade = new ModelPart(this);
		leftShoulderBlade.setPos(0.0F, 0.0F, 0.0F);
		leftArm.addChild(leftShoulderBlade);
		leftShoulderBlade.texOffs(18, 82).addBox(0.0F, -3.0F, -1.0F, 4.0F, 4.0F, 2.0F, 0.1F, true);

		leftCape = new ModelPart(this);
		leftCape.setPos(-5.0F, 22.0F, 0.0F);
		leftArm.addChild(leftCape);
		leftCape.texOffs(48, 64).addBox(3.5F, -20.0F, 2.6F, 4.0F, 16.0F, 0.0F, 0.0F, true);

		head = new ModelPart(this);
		head.setPos(0.0F, 0.0F, 0.0F);
		head.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.1F, false);
		head.texOffs(24, 0).addBox(-2.0F, -6.0F, -5.0F, 4.0F, 4.0F, 1.0F, -0.25F, false);
		head.texOffs(58, 0).addBox(-1.0F, -9.0F, -4.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);
		head.texOffs(34, 50).addBox(-6.0F, -10.0F, -2.0F, 12.0F, 10.0F, 3.0F, 0.4F, false);

		fins = new ModelPart(this);
		fins.setPos(0.0F, 0.0F, 0.0F);
		head.addChild(fins);
		setRotationAngle(fins, 0.7854F, 0.0F, 0.0F);
		fins.texOffs(0, 87).addBox(5.0F, -5.0F, 2.0F, 0.0F, 3.0F, 6.0F, 0.1F, false);
		fins.texOffs(0, 87).addBox(-5.0F, -5.0F, 2.0F, 0.0F, 3.0F, 6.0F, 0.1F, false);

		postiche = new ModelPart(this);
		postiche.setPos(0.0F, 0.0F, 0.0F);
		head.addChild(postiche);
		setRotationAngle(postiche, 0.7854F, 0.0F, 0.0F);
		postiche.texOffs(0, 89).addBox(-1.0F, -3.0F, -7.0F, 2.0F, 0.0F, 4.0F, 0.1F, false);

		hat = new ModelPart(this);
		hat.setPos(0.0F, 0.0F, 0.0F);
		head.addChild(hat);
		hat.texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.4F, false);

		rightLeg = new ModelPart(this);
		rightLeg.setPos(-1.9F, 12.0F, 0.0F);
		rightLeg.texOffs(0, 16).addBox(-2.1F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		rightLeg.texOffs(16, 59).addBox(-2.1F, -3.0F, -2.0F, 4.0F, 15.0F, 4.0F, 0.4F, false);
		rightLeg.texOffs(0, 67).addBox(-2.1F, -3.0F, -2.0F, 4.0F, 15.0F, 4.0F, 0.3F, false);

		leftLeg = new ModelPart(this);
		leftLeg.setPos(1.9F, 12.0F, 0.0F);
		leftLeg.texOffs(0, 32).addBox(-1.9F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		leftLeg.texOffs(16, 40).addBox(-1.9F, -3.0F, -2.0F, 4.0F, 15.0F, 4.0F, 0.4F, false);
		leftLeg.texOffs(0, 48).addBox(-1.9F, -3.0F, -2.0F, 4.0F, 15.0F, 4.0F, 0.3F, false);

		//Boots manually edited in code
		rightBoot = new ModelPart(this);
		rightBoot.texOffs(32, 63).addBox(-4.1F + 2, -6.2F + 12, -2.0F, 4.0F, 6.0F, 4.0F, 0.2F, true);
		rightBoot.texOffs(56, 36).addBox(-3.4F + 2, -2.0F + 12, -3.0F, 3.0F, 2.0F, 1.0F, 0.1F, true);

		leftBoot = new ModelPart(this);
		leftBoot.texOffs(32, 63).addBox(0.1F - 2, -6.2F + 12, -2.0F, 4.0F, 6.0F, 4.0F, 0.2F, true);
		leftBoot.texOffs(56, 36).addBox(0.4F - 2, -2.0F + 12, -3.0F, 3.0F, 2.0F, 1.0F, 0.1F, true);*/
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

		this.leftBoot.copyFrom(this.leftLeg);
		this.rightBoot.copyFrom(this.rightLeg);
		this.rightBoot.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		this.leftBoot.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);

		super.renderToBuffer(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@SubscribeEvent
	public static void onPlayerRender(RenderPlayerEvent event) {
		Player player = event.getPlayer();
		PlayerModel<AbstractClientPlayer> playerModel = event.getRenderer().getModel();
		if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() == AtumItems.EYES_OF_ATEM) {
			playerModel.hat.visible = false;
		}
		if (player.getItemBySlot(EquipmentSlot.CHEST).getItem() == AtumItems.BODY_OF_ATEM) {
			playerModel.jacket.visible = false;
			playerModel.leftSleeve.visible = false;
			playerModel.rightSleeve.visible = false;
		}
		if (player.getItemBySlot(EquipmentSlot.LEGS).getItem() == AtumItems.LEGS_OF_ATEM || player.getItemBySlot(EquipmentSlot.FEET).getItem() == AtumItems.FEET_OF_ATEM) {
			playerModel.leftPants.visible = false;
			playerModel.rightPants.visible = false;
		}
	}
}
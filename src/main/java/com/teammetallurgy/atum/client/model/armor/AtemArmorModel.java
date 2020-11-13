package com.teammetallurgy.atum.client.model.armor;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.inventory.EquipmentSlotType;

import javax.annotation.Nonnull;

public class AtemArmorModel extends ArmorModel {
	private final ModelRenderer fins;
	private final ModelRenderer hat;
	private final ModelRenderer postiche;
	private final ModelRenderer chestplate;
	private final ModelRenderer rightShoulderBlade;
	private final ModelRenderer leftShoulderBlade;
	private final ModelRenderer leftBoot;
	private final ModelRenderer rightBoot;
	private final ModelRenderer leftCape;
	private final ModelRenderer rightCape;
	private boolean hasFullSet;

	public AtemArmorModel(EquipmentSlotType slot, boolean hasFullSet) {
		super(slot);
		this.textureWidth = 64;
		this.textureHeight = 96;
		this.hasFullSet = hasFullSet;

		bipedBody = new ModelRenderer(this);
		bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedBody.setTextureOffset(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.3F, false);

		chestplate = new ModelRenderer(this);
		chestplate.setRotationPoint(0.0F, 24.0F, 0.0F);
		bipedBody.addChild(chestplate);
		setRotationAngle(chestplate, -0.2618F, 0.0F, 0.0F);
		chestplate.setTextureOffset(16, 32).addBox(-4.0F, -22.0F, -8.5F, 8.0F, 4.0F, 4.0F, 0.0F, false);

		bipedRightArm = new ModelRenderer(this);
		bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		bipedRightArm.setTextureOffset(40, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.3F, false);
		bipedRightArm.setTextureOffset(8, 86).addBox(-3.0F, 6.0F, -2.0F, 2.0F, 2.0F, 4.0F, 0.5F, true);
		bipedRightArm.setTextureOffset(48, 88).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.35F, true);
		bipedRightArm.setTextureOffset(48, 80).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.7F, true);

		rightShoulderBlade = new ModelRenderer(this);
		rightShoulderBlade.setRotationPoint(10.0F, 0.0F, 0.0F);
		bipedRightArm.addChild(rightShoulderBlade);
		rightShoulderBlade.setTextureOffset(18, 90).addBox(-14.0F, -3.0F, -1.0F, 4.0F, 4.0F, 2.0F, 0.1F, true);

		rightCape = new ModelRenderer(this);
		rightCape.setRotationPoint(5.0F, -2.0F, 0.0F);
		bipedRightArm.addChild(rightCape);
		rightCape.setTextureOffset(56, 64).addBox(-7.5F, 4.0F, 2.6F, 4.0F, 16.0F, 0.0F, 0.0F, true);

		bipedLeftArm = new ModelRenderer(this);
		bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		bipedLeftArm.setTextureOffset(40, 16).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.3F, true);
		bipedLeftArm.setTextureOffset(52, 30).addBox(1.0F, 6.0F, -2.0F, 2.0F, 2.0F, 4.0F, 0.5F, true);
		bipedLeftArm.setTextureOffset(32, 88).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.35F, true);
		bipedLeftArm.setTextureOffset(32, 80).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.7F, true);

		leftShoulderBlade = new ModelRenderer(this);
		leftShoulderBlade.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedLeftArm.addChild(leftShoulderBlade);
		leftShoulderBlade.setTextureOffset(18, 82).addBox(0.0F, -3.0F, -1.0F, 4.0F, 4.0F, 2.0F, 0.1F, true);

		leftCape = new ModelRenderer(this);
		leftCape.setRotationPoint(-5.0F, 22.0F, 0.0F);
		bipedLeftArm.addChild(leftCape);
		leftCape.setTextureOffset(48, 64).addBox(3.5F, -20.0F, 2.6F, 4.0F, 16.0F, 0.0F, 0.0F, true);

		bipedHead = new ModelRenderer(this);
		bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.6F, false);
		bipedHead.setTextureOffset(24, 0).addBox(-2.0F, -6.0F, -5.0F, 4.0F, 4.0F, 1.0F, -0.25F, false);
		bipedHead.setTextureOffset(58, 0).addBox(-1.0F, -9.0F, -4.0F, 2.0F, 2.0F, 1.0F, 0.2F, false);
		bipedHead.setTextureOffset(34, 50).addBox(-6.0F, -10.0F, -2.0F, 12.0F, 10.0F, 3.0F, 0.7F, false);

		fins = new ModelRenderer(this);
		fins.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.addChild(fins);
		setRotationAngle(fins, 0.7854F, 0.0F, 0.0F);
		fins.setTextureOffset(0, 87).addBox(5.0F, -5.0F, 2.0F, 0.0F, 3.0F, 6.0F, 0.1F, false);
		fins.setTextureOffset(0, 87).addBox(-5.0F, -5.0F, 2.0F, 0.0F, 3.0F, 6.0F, 0.1F, false);

		postiche = new ModelRenderer(this);
		postiche.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.addChild(postiche);
		setRotationAngle(postiche, 0.7854F, 0.0F, 0.0F);
		postiche.setTextureOffset(0, 89).addBox(-1.0F, -3.0F, -7.0F, 2.0F, 0.0F, 4.0F, 0.1F, false);

		hat = new ModelRenderer(this);
		hat.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.addChild(hat);
		hat.setTextureOffset(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.7F, false);

		bipedRightLeg = new ModelRenderer(this);
		bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
		bipedRightLeg.setTextureOffset(0, 16).addBox(-2.1F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.1F, false);
		bipedRightLeg.setTextureOffset(16, 59).addBox(-2.1F, -3.0F, -2.0F, 4.0F, 15.0F, 4.0F, 0.65F, false);
		bipedRightLeg.setTextureOffset(0, 67).addBox(-2.1F, -3.0F, -2.0F, 4.0F, 15.0F, 4.0F, 0.32F, false);

		bipedLeftLeg = new ModelRenderer(this);
		bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
		bipedLeftLeg.setTextureOffset(0, 32).addBox(-1.9F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.1F, false);
		bipedLeftLeg.setTextureOffset(16, 40).addBox(-1.9F, -3.0F, -2.0F, 4.0F, 15.0F, 4.0F, 0.65F, false);
		bipedLeftLeg.setTextureOffset(0, 48).addBox(-1.9F, -3.0F, -2.0F, 4.0F, 15.0F, 4.0F, 0.32F, false);
		
		//Boots manually edited in code
		rightBoot = new ModelRenderer(this);
		rightBoot.setTextureOffset(32, 63).addBox(-4.1F + 2, -6.2F + 12, -2.0F, 4.0F, 6.0F, 4.0F, 0.2F, true);
		rightBoot.setTextureOffset(56, 36).addBox(-3.4F + 2, -2.0F + 12, -3.0F, 3.0F, 2.0F, 1.0F, 0.1F, true);

		leftBoot = new ModelRenderer(this);
		leftBoot.setTextureOffset(32, 63).addBox(0.1F - 2, -6.2F + 12, -2.0F, 4.0F, 6.0F, 4.0F, 0.2F, true);
		leftBoot.setTextureOffset(56, 36).addBox(0.4F - 2, -2.0F + 12, -3.0F, 3.0F, 2.0F, 1.0F, 0.1F, true);
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
		this.leftBoot.showModel = slot == EquipmentSlotType.FEET;
		this.rightBoot.showModel = slot == EquipmentSlotType.FEET;
		this.leftCape.showModel = this.hasFullSet;
		this.rightCape.showModel = this.hasFullSet;

		this.leftBoot.copyModelAngles(this.bipedLeftLeg);
		this.rightBoot.copyModelAngles(this.bipedRightLeg);
		this.rightBoot.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		this.leftBoot.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);

		super.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
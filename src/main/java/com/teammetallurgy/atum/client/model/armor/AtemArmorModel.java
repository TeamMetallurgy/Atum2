package com.teammetallurgy.atum.client.model.armor;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.inventory.EquipmentSlotType;

import javax.annotation.Nonnull;

public class AtemArmorModel extends ArmorModel {
	private final ModelRenderer waist;
	private final ModelRenderer chestplate;
	private final ModelRenderer hat;
	private final ModelRenderer fins;
	private final ModelRenderer postiche;
	private final ModelRenderer rightShoulderBlade;
	private final ModelRenderer leftShoulderBlade;
	private final ModelRenderer cape;

	public AtemArmorModel(EquipmentSlotType slot) {
		super(slot);
		this.textureWidth = 64;
		this.textureHeight = 96;

		bipedBody = new ModelRenderer(this);
		bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedBody.setTextureOffset(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);

		bipedRightArm = new ModelRenderer(this);
		bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		bipedBody.addChild(bipedRightArm);
		bipedRightArm.setTextureOffset(40, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		bipedRightArm.setTextureOffset(52, 46).addBox(-3.0F, 6.0F, -2.0F, 2.0F, 2.0F, 4.0F, 0.25F, true);
		bipedRightArm.setTextureOffset(48, 88).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.25F, true);
		bipedRightArm.setTextureOffset(48, 80).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.5F, true);

		rightShoulderBlade = new ModelRenderer(this);
		rightShoulderBlade.setRotationPoint(10.0F, 0.0F, 0.0F);
		bipedRightArm.addChild(rightShoulderBlade);
		rightShoulderBlade.setTextureOffset(18, 90).addBox(-14.0F, -3.0F, -1.0F, 4.0F, 4.0F, 2.0F, 0.0F, true);

		bipedLeftArm = new ModelRenderer(this);
		bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		bipedBody.addChild(bipedLeftArm);
		bipedLeftArm.setTextureOffset(40, 16).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);
		bipedLeftArm.setTextureOffset(52, 30).addBox(1.0F, 6.0F, -2.0F, 2.0F, 2.0F, 4.0F, 0.25F, true);
		bipedLeftArm.setTextureOffset(32, 88).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.25F, true);
		bipedLeftArm.setTextureOffset(32, 80).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.5F, true);

		leftShoulderBlade = new ModelRenderer(this);
		leftShoulderBlade.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedLeftArm.addChild(leftShoulderBlade);
		leftShoulderBlade.setTextureOffset(18, 82).addBox(0.0F, -3.0F, -1.0F, 4.0F, 4.0F, 2.0F, 0.0F, true);

		chestplate = new ModelRenderer(this);
		chestplate.setRotationPoint(0.0F, 24.0F, 0.0F);
		bipedBody.addChild(chestplate);
		setRotationAngle(chestplate, -0.2618F, 0.0F, 0.0F);
		chestplate.setTextureOffset(16, 32).addBox(-4.0F, -22.0F, -8.0F, 8.0F, 4.0F, 4.0F, -0.1F, false);

		cape = new ModelRenderer(this);
		cape.setRotationPoint(0.0F, 0.0F, 0.0F);
		cape.setTextureOffset(56, 64).addBox(-7.5F, 4.0F, 2.35F, 4.0F, 16.0F, 0.0F, 0.0F, true);
		cape.setTextureOffset(48, 64).addBox(3.5F, 4.0F, 2.35F, 4.0F, 16.0F, 0.0F, 0.0F, true);

		bipedHead = new ModelRenderer(this);
		bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
		bipedHead.setTextureOffset(24, 0).addBox(-2.0F, -6.0F, -5.0F, 4.0F, 4.0F, 1.0F, -0.25F, false);
		bipedHead.setTextureOffset(58, 0).addBox(-1.0F, -9.0F, -4.0F, 2.0F, 2.0F, 1.0F, 0.1F, false);
		bipedHead.setTextureOffset(34, 50).addBox(-6.0F, -10.0F, -2.0F, 12.0F, 10.0F, 3.0F, 0.0F, false);

		fins = new ModelRenderer(this);
		fins.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.addChild(fins);
		setRotationAngle(fins, 0.7854F, 0.0F, 0.0F);
		fins.setTextureOffset(0, 87).addBox(4.5F, -5.0F, 2.0F, 0.0F, 3.0F, 6.0F, 0.0F, false);
		fins.setTextureOffset(0, 87).addBox(-4.5F, -5.0F, 2.0F, 0.0F, 3.0F, 6.0F, 0.0F, false);

		postiche = new ModelRenderer(this);
		postiche.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.addChild(postiche);
		setRotationAngle(postiche, 0.7854F, 0.0F, 0.0F);
		postiche.setTextureOffset(0, 89).addBox(-1.0F, -3.0F, -6.0F, 2.0F, 0.0F, 4.0F, 0.0F, false);

		hat = new ModelRenderer(this);
		hat.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.addChild(hat);
		hat.setTextureOffset(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.25F, false);

		waist = new ModelRenderer(this);
		waist.setRotationPoint(0.0F, 12.0F, 0.0F);

		bipedRightLeg = new ModelRenderer(this);
		bipedRightLeg.setRotationPoint(-1.9F, 0.0F, 0.0F);
		waist.addChild(bipedRightLeg);
		bipedRightLeg.setTextureOffset(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		bipedRightLeg.setTextureOffset(16, 59).addBox(-2.1F, -3.0F, -2.0F, 4.0F, 15.0F, 4.0F, 0.25F, false);
		bipedRightLeg.setTextureOffset(0, 67).addBox(-2.1F, -3.0F, -2.0F, 4.0F, 15.0F, 4.0F, 0.1F, false);

		bipedLeftLeg = new ModelRenderer(this);
		bipedLeftLeg.setRotationPoint(1.9F, 0.0F, 0.0F);
		waist.addChild(bipedLeftLeg);
		bipedLeftLeg.setTextureOffset(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);
		bipedLeftLeg.setTextureOffset(16, 40).addBox(-1.9F, -3.0F, -2.0F, 4.0F, 15.0F, 4.0F, 0.25F, false);
		bipedLeftLeg.setTextureOffset(0, 48).addBox(-1.9F, -3.0F, -2.0F, 4.0F, 15.0F, 4.0F, 0.1F, false);
	}

	@Override
	public void render(@Nonnull MatrixStack matrixStack, @Nonnull IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		EquipmentSlotType slot = this.getSlot();

		bipedHeadwear.showModel = false; //Hide vanilla stuff

		bipedHead.showModel = slot == EquipmentSlotType.HEAD;
		bipedBody.showModel = slot == EquipmentSlotType.CHEST;
		bipedLeftArm.showModel = slot == EquipmentSlotType.CHEST;
		bipedRightArm.showModel = slot == EquipmentSlotType.CHEST;
		waist.showModel = slot == EquipmentSlotType.LEGS;
		bipedLeftLeg.showModel = slot == EquipmentSlotType.LEGS;
		bipedRightLeg.showModel = slot == EquipmentSlotType.LEGS;
		//leftBoot.showModel = slot == EquipmentSlotType.FEET;
		//rightBoot.showModel = slot == EquipmentSlotType.FEET;
		this.cape.showModel = false; //TODO Show when all pieces are worn


		super.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
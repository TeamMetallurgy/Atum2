package com.teammetallurgy.atum.client.render.entity.layer;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.client.model.entity.DesertWolfModel;
import com.teammetallurgy.atum.entity.animal.DesertWolfEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;

import javax.annotation.Nonnull;
import java.util.Map;

public class DesertWolfArmorLayer extends LayerRenderer<DesertWolfEntity, DesertWolfModel<DesertWolfEntity>> {
    private static final Map<DesertWolfEntity.ArmorType, ResourceLocation> CACHE = Util.make(Maps.newEnumMap(DesertWolfEntity.ArmorType.class), (m) -> {
        m.put(DesertWolfEntity.ArmorType.NONE, null);
        m.put(DesertWolfEntity.ArmorType.IRON, new ResourceLocation(Atum.MOD_ID, "textures/entity/armor/desert_wolf_armor_iron.png"));
        m.put(DesertWolfEntity.ArmorType.GOLD, new ResourceLocation(Atum.MOD_ID, "textures/entity/armor/desert_wolf_armor_gold.png"));
        m.put(DesertWolfEntity.ArmorType.DIAMOND, new ResourceLocation(Atum.MOD_ID, "textures/entity/armor/desert_wolf_armor_diamond.png"));
    });
    private final DesertWolfModel<DesertWolfEntity> model = new DesertWolfModel<>(0.1F);

    public DesertWolfArmorLayer(IEntityRenderer<DesertWolfEntity, DesertWolfModel<DesertWolfEntity>> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int packedLight, DesertWolfEntity desertWolf, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack armor = desertWolf.getArmor();
        if (!armor.isEmpty()) {
            DesertWolfEntity.ArmorType armorType = DesertWolfEntity.ArmorType.getByItemStack(armor);
            this.getEntityModel().copyModelAttributesTo(this.model);
            this.model.setLivingAnimations(desertWolf, limbSwing, limbSwingAmount, partialTicks);
            this.model.setRotationAngles(desertWolf, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            IVertexBuilder ivertexbuilder = buffer.getBuffer(RenderType.getEntityCutoutNoCull(CACHE.get(armorType)));
            this.model.render(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
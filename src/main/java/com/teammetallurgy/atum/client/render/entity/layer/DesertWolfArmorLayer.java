package com.teammetallurgy.atum.client.render.entity.layer;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.client.ClientHandler;
import com.teammetallurgy.atum.client.model.entity.DesertWolfModel;
import com.teammetallurgy.atum.entity.animal.DesertWolfEntity;
import net.minecraft.Util;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Map;

public class DesertWolfArmorLayer extends RenderLayer<DesertWolfEntity, DesertWolfModel<DesertWolfEntity>> {
    private static final Map<DesertWolfEntity.ArmorType, ResourceLocation> CACHE = Util.make(Maps.newEnumMap(DesertWolfEntity.ArmorType.class), (m) -> {
        m.put(DesertWolfEntity.ArmorType.NONE, null);
        m.put(DesertWolfEntity.ArmorType.IRON, new ResourceLocation(Atum.MOD_ID, "textures/entity/armor/desert_wolf_armor_iron.png"));
        m.put(DesertWolfEntity.ArmorType.GOLD, new ResourceLocation(Atum.MOD_ID, "textures/entity/armor/desert_wolf_armor_gold.png"));
        m.put(DesertWolfEntity.ArmorType.DIAMOND, new ResourceLocation(Atum.MOD_ID, "textures/entity/armor/desert_wolf_armor_diamond.png"));
    });
    private final DesertWolfModel<DesertWolfEntity> model;

    public DesertWolfArmorLayer(RenderLayerParent<DesertWolfEntity, DesertWolfModel<DesertWolfEntity>> entityRenderer, EntityModelSet modelSet) {
        super(entityRenderer);
        this.model = new DesertWolfModel<>(modelSet.bakeLayer(ClientHandler.DESERT_WOLF_ARMOR));
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int packedLight, DesertWolfEntity desertWolf, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack armor = desertWolf.getArmor();
        if (!armor.isEmpty()) {
            DesertWolfEntity.ArmorType armorType = DesertWolfEntity.ArmorType.getByItemStack(armor);
            this.getParentModel().copyPropertiesTo(this.model);
            this.model.prepareMobModel(desertWolf, limbSwing, limbSwingAmount, partialTicks);
            this.model.setupAnim(desertWolf, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            VertexConsumer ivertexbuilder = buffer.getBuffer(RenderType.entityCutoutNoCull(CACHE.get(armorType)));
            this.model.renderToBuffer(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
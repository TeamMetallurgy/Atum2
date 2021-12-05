package com.teammetallurgy.atum.client.render.entity.layer;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.client.model.entity.CamelModel;
import com.teammetallurgy.atum.entity.animal.CamelEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.Util;

import javax.annotation.Nonnull;
import java.util.Map;

public class CamelArmorLayer extends RenderLayer<CamelEntity, CamelModel<CamelEntity>> {
    private static final Map<CamelEntity.ArmorType, ResourceLocation> CACHE = Util.make(Maps.newEnumMap(CamelEntity.ArmorType.class), (m) -> {
        m.put(CamelEntity.ArmorType.NONE, null);
        m.put(CamelEntity.ArmorType.IRON, new ResourceLocation(Atum.MOD_ID, "textures/entity/armor/camel_armor_iron.png"));
        m.put(CamelEntity.ArmorType.GOLD, new ResourceLocation(Atum.MOD_ID, "textures/entity/armor/camel_armor_gold.png"));
        m.put(CamelEntity.ArmorType.DIAMOND, new ResourceLocation(Atum.MOD_ID, "textures/entity/armor/camel_armor_diamond.png"));
    });
    private final CamelModel<CamelEntity> model = new CamelModel<>(0.1F);

    public CamelArmorLayer(RenderLayerParent<CamelEntity, CamelModel<CamelEntity>> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int packedLight, CamelEntity camel, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack armor = camel.getArmor();
        if (!armor.isEmpty()) {
            CamelEntity.ArmorType armorType = CamelEntity.ArmorType.getByItemStack(armor);
            this.getParentModel().copyPropertiesTo(this.model);
            this.model.prepareMobModel(camel, limbSwing, limbSwingAmount, partialTicks);
            this.model.setupAnim(camel, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            VertexConsumer ivertexbuilder = buffer.getBuffer(RenderType.entityCutoutNoCull(CACHE.get(armorType)));
            this.model.renderToBuffer(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
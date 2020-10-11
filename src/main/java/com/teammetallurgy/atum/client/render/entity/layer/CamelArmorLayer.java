package com.teammetallurgy.atum.client.render.entity.layer;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.client.model.entity.CamelModel;
import com.teammetallurgy.atum.entity.animal.CamelEntity;
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

public class CamelArmorLayer extends LayerRenderer<CamelEntity, CamelModel<CamelEntity>> {
    private static final Map<CamelEntity.ArmorType, ResourceLocation> CACHE = Util.make(Maps.newEnumMap(CamelEntity.ArmorType.class), (m) -> {
        m.put(CamelEntity.ArmorType.NONE, null);
        m.put(CamelEntity.ArmorType.IRON, new ResourceLocation(Atum.MOD_ID, "textures/entity/armor/camel_armor_iron.png"));
        m.put(CamelEntity.ArmorType.GOLD, new ResourceLocation(Atum.MOD_ID, "textures/entity/armor/camel_armor_gold.png"));
        m.put(CamelEntity.ArmorType.DIAMOND, new ResourceLocation(Atum.MOD_ID, "textures/entity/armor/camel_armor_diamond.png"));
    });
    private final CamelModel<CamelEntity> model = new CamelModel<>(0.1F);

    public CamelArmorLayer(IEntityRenderer<CamelEntity, CamelModel<CamelEntity>> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int packedLight, CamelEntity camel, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack armor = camel.getArmor();
        if (!armor.isEmpty()) {
            CamelEntity.ArmorType armorType = CamelEntity.ArmorType.getByItemStack(armor);
            this.getEntityModel().copyModelAttributesTo(this.model);
            this.model.setLivingAnimations(camel, limbSwing, limbSwingAmount, partialTicks);
            this.model.setRotationAngles(camel, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            IVertexBuilder ivertexbuilder = buffer.getBuffer(RenderType.getEntityCutoutNoCull(CACHE.get(armorType)));
            this.model.render(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
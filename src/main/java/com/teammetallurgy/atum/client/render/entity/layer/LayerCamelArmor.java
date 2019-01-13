package com.teammetallurgy.atum.client.render.entity.layer;

import com.teammetallurgy.atum.client.model.entity.ModelCamel;
import com.teammetallurgy.atum.client.render.entity.mobs.RenderCamel;
import com.teammetallurgy.atum.entity.animal.EntityCamel;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class LayerCamelArmor implements LayerRenderer<EntityCamel> {
    private final RenderCamel renderer;
    private final ModelCamel model = new ModelCamel(0.6F);

    public LayerCamelArmor(RenderCamel renderCamel) {
        this.renderer = renderCamel;
    }

    @Override
    public void doRenderLayer(@Nonnull EntityCamel camel, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        ItemStack camelArmor = camel.getArmor();
        if (!camelArmor.isEmpty()) {
            EntityCamel.ArmorType armorType = EntityCamel.ArmorType.getByItemStack(camelArmor);
            this.renderer.bindTexture(armorType.getTextureName());
            this.model.setModelAttributes(this.renderer.getMainModel());
            this.model.render(camel, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
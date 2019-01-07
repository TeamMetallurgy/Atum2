package com.teammetallurgy.atum.client.render.entity.layer;

import com.teammetallurgy.atum.client.model.entity.ModelCamel;
import com.teammetallurgy.atum.client.render.entity.mobs.RenderCamel;
import com.teammetallurgy.atum.entity.animal.EntityCamel;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class LayerCamelArmor implements LayerRenderer<EntityCamel> {
    private static final ResourceLocation CAMEL_ARMOR = new ResourceLocation(Constants.MOD_ID, "textures/entities/armor/camel_armor.png");
    private final RenderCamel renderer;
    private final ModelCamel model = new ModelCamel(0.6F);

    public LayerCamelArmor(RenderCamel renderCamel) {
        this.renderer = renderCamel;
    }

    @Override
    public void doRenderLayer(@Nonnull EntityCamel camel, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (false) { //TODO When armor is added
            this.renderer.bindTexture(CAMEL_ARMOR);
            this.model.setModelAttributes(this.renderer.getMainModel());
            this.model.render(camel, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
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
import java.util.Objects;

@SideOnly(Side.CLIENT)
public class LayerCamelCarpet implements LayerRenderer<EntityCamel> {
    private static final ResourceLocation[] CARPET_TEXTURES = new ResourceLocation[]{carpetLocation("white"), carpetLocation("orange"), carpetLocation("magenta"), carpetLocation("light_blue"), carpetLocation("yellow"), carpetLocation("lime"), carpetLocation("pink"), carpetLocation("gray"), carpetLocation("silver"), carpetLocation("cyan"), carpetLocation("purple"), carpetLocation("blue"), carpetLocation("brown"), carpetLocation("green"), carpetLocation("red"), carpetLocation("black")};
    private final RenderCamel renderer;
    private final ModelCamel model = new ModelCamel(0.5F);

    public LayerCamelCarpet(RenderCamel renderCamel) {
        this.renderer = renderCamel;
    }

    @Override
    public void doRenderLayer(@Nonnull EntityCamel camel, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (camel.hasColor()) {
            this.renderer.bindTexture(CARPET_TEXTURES[Objects.requireNonNull(camel.getColor()).getMetadata()]);
            this.model.setModelAttributes(this.renderer.getMainModel());
            this.model.render(camel, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }

    private static ResourceLocation carpetLocation(String color) {
        return new ResourceLocation(Constants.MOD_ID, "textures/entities/camel_carpet/camel_carpet_" + color + ".png");
    }
}
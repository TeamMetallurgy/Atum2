package com.teammetallurgy.atum.client.render.entity.mobs;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.client.ClientHandler;
import com.teammetallurgy.atum.client.model.entity.CamelModel;
import com.teammetallurgy.atum.client.render.entity.layer.CamelArmorLayer;
import com.teammetallurgy.atum.client.render.entity.layer.CamelDecorLayer;
import com.teammetallurgy.atum.entity.animal.CamelEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class CamelRender extends MobRenderer<CamelEntity, CamelModel<CamelEntity>> {
    private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();

    public CamelRender(EntityRendererProvider.Context context) {
        super(context, new CamelModel<>(context.bakeLayer(ClientHandler.CAMEL)), 0.7F);
        this.addLayer(new CamelDecorLayer(this, context.getModelSet()));
        this.addLayer(new CamelArmorLayer(this, context.getModelSet()));
    }

    @Override
    @Nonnull
    public ResourceLocation getTextureLocation(@Nonnull CamelEntity camel) {
        String textureName = camel.getTexture();

        ResourceLocation location = CACHE.get(textureName);
        if (location == null) {
            location = new ResourceLocation(Atum.MOD_ID, "textures/entity/camel_" + textureName + ".png");
            CACHE.put(textureName, location);
        }
        return location;
    }
}
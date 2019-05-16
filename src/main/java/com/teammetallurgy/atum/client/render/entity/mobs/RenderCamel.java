package com.teammetallurgy.atum.client.render.entity.mobs;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.client.model.entity.ModelCamel;
import com.teammetallurgy.atum.client.render.entity.layer.LayerCamelArmor;
import com.teammetallurgy.atum.client.render.entity.layer.LayerCamelCarpet;
import com.teammetallurgy.atum.entity.animal.EntityCamel;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class RenderCamel extends RenderLiving<EntityCamel> {
    private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();
    private static final ResourceLocation GIRAFI = new ResourceLocation(Constants.MOD_ID, "textures/entity/camel_girafi.png");

    public RenderCamel(RenderManager renderManager) {
        super(renderManager, new ModelCamel(0.0F), 0.7F);
        this.addLayer(new LayerCamelArmor(this));
        this.addLayer(new LayerCamelCarpet(this));
    }

    @Override
    @Nullable
    protected ResourceLocation getEntityTexture(@Nonnull EntityCamel camel) {
        String texture = camel.getTexture();
        ResourceLocation location = CACHE.get(texture);

        if (camel.hasCustomName()) {
            String name = camel.getCustomNameTag();
            if (name.equalsIgnoreCase("girafi")) {
                return GIRAFI;
            }
        }
        if (location == null) {
            location = new ResourceLocation(texture);
            CACHE.put(texture, location);
        }
        return location;
    }
}
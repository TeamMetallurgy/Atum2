package com.teammetallurgy.atum.client.render.entity.mobs;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.client.model.entity.ModelCamel;
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

    public RenderCamel(RenderManager renderManager) {
        super(renderManager, new ModelCamel(), 0.7F);
    }

    @Override
    @Nullable
    protected ResourceLocation getEntityTexture(@Nonnull EntityCamel camel) {
        String texture = camel.getTexture();
        ResourceLocation location = CACHE.get(texture);

        if (camel.hasCustomName()) {
            String name = camel.getCustomNameTag();
            if (name.equalsIgnoreCase("girafi")) {
                location = this.getCamelTexture("girafi");
            }
        }
        if (location == null) {
            location = new ResourceLocation(texture);
            CACHE.put(texture, location);
        }
        return location;
    }

    private ResourceLocation getCamelTexture(String fileName) {
        return new ResourceLocation(Constants.MOD_ID, "textures/entities/camel_" + fileName + ".png");
    }
}
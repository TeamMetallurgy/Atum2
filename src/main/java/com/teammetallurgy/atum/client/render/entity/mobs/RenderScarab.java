package com.teammetallurgy.atum.client.render.entity.mobs;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.entity.animal.EntityScarab;
import net.minecraft.client.model.ModelEnderMite;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class RenderScarab extends RenderLiving<EntityScarab> {
    private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();

    public RenderScarab(RenderManager manager) {
        super(manager, new ModelEnderMite(), 0.3F);
    }

    @Override
    protected float getDeathMaxRotation(EntityScarab scarab) {
        return 180.0F;
    }

    @Override
    @Nullable
    protected ResourceLocation getEntityTexture(@Nonnull EntityScarab entity) {
        String texture = entity.getTexture();
        ResourceLocation location = CACHE.get(texture);

        if (location == null) {
            location = new ResourceLocation(texture);
            CACHE.put(texture, location);
        }
        return location;
    }
}
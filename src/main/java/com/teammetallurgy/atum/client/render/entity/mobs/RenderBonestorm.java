package com.teammetallurgy.atum.client.render.entity.mobs;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.client.model.entity.ModelBonestorm;
import com.teammetallurgy.atum.entity.undead.EntityBonestorm;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class RenderBonestorm extends RenderLiving<EntityBonestorm> {
    private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();

    public RenderBonestorm(RenderManager renderManager) {
        super(renderManager, new ModelBonestorm(), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityBonestorm entity) {
        String texture = entity.getTexture();
        ResourceLocation location = CACHE.get(texture);

        if (location == null) {
            location = new ResourceLocation(texture);
            CACHE.put(texture, location);
        }
        return location;
    }
}
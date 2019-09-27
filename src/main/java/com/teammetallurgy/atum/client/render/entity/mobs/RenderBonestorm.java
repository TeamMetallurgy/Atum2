package com.teammetallurgy.atum.client.render.entity.mobs;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.client.model.entity.BonestormModel;
import com.teammetallurgy.atum.entity.undead.BonestormEntity;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class RenderBonestorm extends RenderLiving<BonestormEntity> {
    private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();

    public RenderBonestorm(RenderManager renderManager) {
        super(renderManager, new BonestormModel(), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull BonestormEntity entity) {
        String texture = entity.getTexture();
        ResourceLocation location = CACHE.get(texture);

        if (location == null) {
            location = new ResourceLocation(texture);
            CACHE.put(texture, location);
        }
        return location;
    }
}
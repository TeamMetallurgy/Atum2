package com.teammetallurgy.atum.client.render.entity.mobs;

import com.teammetallurgy.atum.entity.animal.ScarabEntity;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EndermiteModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class ScarabRender extends MobRenderer<ScarabEntity, EndermiteModel<ScarabEntity>> {
    private static final ResourceLocation SCARAB_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/entity/scarab.png");
    private static final ResourceLocation SCARAB_GOLDEN_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/entity/scarab_golden.png");

    public ScarabRender(EntityRendererManager manager) {
        super(manager, new EndermiteModel<>(), 0.3F);
    }

    @Override
    protected float getDeathMaxRotation(ScarabEntity scarab) {
        return 180.0F;
    }

    @Override
    @Nonnull
    public ResourceLocation getEntityTexture(@Nonnull ScarabEntity entity) {
        if (entity.getVariant() == 1) {
            return SCARAB_GOLDEN_TEXTURE;
        } else {
            return SCARAB_TEXTURE;
        }
    }
}
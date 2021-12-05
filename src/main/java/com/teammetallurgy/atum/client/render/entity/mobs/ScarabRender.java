package com.teammetallurgy.atum.client.render.entity.mobs;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.animal.ScarabEntity;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.model.EndermiteModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class ScarabRender extends MobRenderer<ScarabEntity, EndermiteModel<ScarabEntity>> {
    private static final ResourceLocation SCARAB_TEXTURE = new ResourceLocation(Atum.MOD_ID, "textures/entity/scarab.png");
    private static final ResourceLocation SCARAB_GOLDEN_TEXTURE = new ResourceLocation(Atum.MOD_ID, "textures/entity/scarab_golden.png");

    public ScarabRender(EntityRenderDispatcher manager) {
        super(manager, new EndermiteModel<>(), 0.3F);
    }

    @Override
    protected float getFlipDegrees(ScarabEntity scarab) {
        return 180.0F;
    }

    @Override
    @Nonnull
    public ResourceLocation getTextureLocation(@Nonnull ScarabEntity entity) {
        if (entity.getVariant() == 1) {
            return SCARAB_GOLDEN_TEXTURE;
        } else {
            return SCARAB_TEXTURE;
        }
    }
}
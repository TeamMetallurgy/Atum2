package com.teammetallurgy.atum.client.render.entity.mobs;

import com.teammetallurgy.atum.entity.animal.EntityScarab;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.model.ModelEnderMite;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class RenderScarab extends RenderLiving<EntityScarab> {
    private static final ResourceLocation SCARAB_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/entity/scarab.png");
    private static final ResourceLocation SCARAB_GOLDEN_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/entity/scarab_golden.png");

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
        if (entity.getVariant() == 1) {
            return SCARAB_GOLDEN_TEXTURE;
        } else {
            return SCARAB_TEXTURE;
        }
    }
}
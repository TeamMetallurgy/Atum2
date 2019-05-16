package com.teammetallurgy.atum.client.render.entity.mobs;

import com.teammetallurgy.atum.entity.animal.EntityDesertRabbit;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.model.ModelRabbit;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RenderDesertRabbit extends RenderLiving<EntityDesertRabbit> {
    private static final ResourceLocation PALE = new ResourceLocation(Constants.MOD_ID, "textures/entity/rabbit_pale.png");
    private static final ResourceLocation SANDY = new ResourceLocation(Constants.MOD_ID, "textures/entity/rabbit_sandy.png");
    private static final ResourceLocation HAZEL = new ResourceLocation(Constants.MOD_ID, "textures/entity/rabbit_hazel.png");
    private static final ResourceLocation UMBER = new ResourceLocation(Constants.MOD_ID, "textures/entity/rabbit_umber.png");
    private static final ResourceLocation UMBER_DARK = new ResourceLocation(Constants.MOD_ID, "textures/entity/rabbit_umber_dark.png");

    private static final ResourceLocation IRON = new ResourceLocation(Constants.MOD_ID, "textures/entity/rabbit_iron.png");

    public RenderDesertRabbit(RenderManager manager) {
        super(manager, new ModelRabbit(), 0.3F);
    }

    @Override
    @Nullable
    protected ResourceLocation getEntityTexture(@Nonnull EntityDesertRabbit rabbit) {
        if (rabbit.hasCustomName()) {
            if (rabbit.getCustomNameTag().equalsIgnoreCase("iron") || rabbit.getCustomNameTag().equalsIgnoreCase("nutz")) {
                return IRON;
            }
        }
        switch (rabbit.getRabbitType()) {
            case 0:
            default:
                return PALE;
            case 1:
                return SANDY;
            case 2:
                return HAZEL;
            case 3:
                return UMBER;
            case 4:
                return UMBER_DARK;
        }
    }
}
package com.teammetallurgy.atum.client.render.entity.mobs;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.animal.DesertRabbitEntity;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.model.RabbitModel;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class DesertRabbitRender extends MobRenderer<DesertRabbitEntity, RabbitModel<DesertRabbitEntity>> {
    private static final ResourceLocation PALE = new ResourceLocation(Atum.MOD_ID, "textures/entity/rabbit_pale.png");
    private static final ResourceLocation SANDY = new ResourceLocation(Atum.MOD_ID, "textures/entity/rabbit_sandy.png");
    private static final ResourceLocation HAZEL = new ResourceLocation(Atum.MOD_ID, "textures/entity/rabbit_hazel.png");
    private static final ResourceLocation UMBER = new ResourceLocation(Atum.MOD_ID, "textures/entity/rabbit_umber.png");
    private static final ResourceLocation UMBER_DARK = new ResourceLocation(Atum.MOD_ID, "textures/entity/rabbit_umber_dark.png");
    private static final ResourceLocation IRON = new ResourceLocation(Atum.MOD_ID, "textures/entity/rabbit_iron.png");

    public DesertRabbitRender(EntityRenderDispatcher manager) {
        super(manager, new RabbitModel<>(), 0.3F);
    }

    @Override
    @Nonnull
    public ResourceLocation getTextureLocation(@Nonnull DesertRabbitEntity rabbit) {
        ResourceLocation location;
        switch (rabbit.getRabbitType()) {
            case 0:
            default:
                location = PALE;
                break;
            case 1:
                location = SANDY;
                break;
            case 2:
                location = HAZEL;
                break;
            case 3:
                location = UMBER;
                break;
            case 4:
                location = UMBER_DARK;
                break;
        }

        if (rabbit.hasCustomName() && rabbit.getCustomName() != null) {
            String customName = rabbit.getCustomName().getString();
            if (customName.equalsIgnoreCase("iron") || customName.equalsIgnoreCase("nutz") || customName.equalsIgnoreCase("vequinox")) {
                location = IRON;
            }
        }
        return location;
    }
}
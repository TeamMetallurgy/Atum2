package com.teammetallurgy.atum.client.render.entity.mobs;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.client.ClientHandler;
import com.teammetallurgy.atum.client.model.entity.ServalModel;
import com.teammetallurgy.atum.client.render.entity.layer.ServalCollorLayer;
import com.teammetallurgy.atum.entity.animal.ServalEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nonnull;

public class ServalRenderer extends MobRenderer<ServalEntity, ServalModel<ServalEntity>> {
    private static final ResourceLocation AZURE = new ResourceLocation(Atum.MOD_ID, "textures/entity/serval/azure.png");

    public ServalRenderer(EntityRendererProvider.Context context) {
        super(context, new ServalModel<>(context.bakeLayer(ClientHandler.SERVAL)), 0.5F);
        this.addLayer(new ServalCollorLayer(this, context.getModelSet()));
    }

    @Override
    @Nonnull
    public ResourceLocation getTextureLocation(ServalEntity entity) {
        if (entity.hasCustomName() && entity.getCustomName() != null) {
            String customName = entity.getCustomName().getString();
            if (customName.equalsIgnoreCase("azure") || customName.equalsIgnoreCase("azu") || customName.equalsIgnoreCase("azuriuz")) {
                return AZURE;
            }
        }
        return entity.getResourceLocation();
    }

    @Override
    protected void setupRotations(@Nonnull ServalEntity serval, @Nonnull PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupRotations(serval, poseStack, ageInTicks, rotationYaw, partialTicks);
        float f = serval.getLieDownAmount(partialTicks);
        if (f > 0.0F) {
            poseStack.translate((0.4F * f), (0.15F * f), (0.1F * f));
            poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.rotLerp(f, 0.0F, 90.0F)));
            BlockPos blockpos = serval.blockPosition();

            for (Player player : serval.level.getEntitiesOfClass(Player.class, (new AABB(blockpos)).inflate(2.0D, 2.0D, 2.0D))) {
                if (player.isSleeping()) {
                    poseStack.translate(0.15F * f, 0.0D, 0.0D);
                    break;
                }
            }
        }
    }
}
package com.teammetallurgy.atum.client.render.entity.mobs;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.client.model.entity.ServalModel;
import com.teammetallurgy.atum.client.render.entity.layer.ServalCollorLayer;
import com.teammetallurgy.atum.entity.animal.ServalEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.Nonnull;

public class ServalRenderer extends MobRenderer<ServalEntity, ServalModel<ServalEntity>> {
    private static final ResourceLocation AZURE = new ResourceLocation(Atum.MOD_ID, "textures/entity/serval/azure.png");

    public ServalRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ServalModel<>(0.0F), 0.5F);
        this.addLayer(new ServalCollorLayer(this));
    }

    @Override
    @Nonnull
    public ResourceLocation getEntityTexture(ServalEntity entity) {
        if (entity.hasCustomName() && entity.getCustomName().getString().equals("azure")) {
            return AZURE;
        } else {
            return entity.getCatTypeName();
        }
    }

    @Override
    protected void applyRotations(@Nonnull ServalEntity serval, @Nonnull MatrixStack matrixStack, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(serval, matrixStack, ageInTicks, rotationYaw, partialTicks);
        float f = serval.func_213408_v(partialTicks);
        if (f > 0.0F) {
            matrixStack.translate((0.4F * f), (0.15F * f), (0.1F * f));
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(MathHelper.interpolateAngle(f, 0.0F, 90.0F)));
            BlockPos blockpos = serval.getPosition();

            for (PlayerEntity player : serval.world.getEntitiesWithinAABB(PlayerEntity.class, (new AxisAlignedBB(blockpos)).grow(2.0D, 2.0D, 2.0D))) {
                if (player.isSleeping()) {
                    matrixStack.translate(0.15F * f, 0.0D, 0.0D);
                    break;
                }
            }
        }
    }
}
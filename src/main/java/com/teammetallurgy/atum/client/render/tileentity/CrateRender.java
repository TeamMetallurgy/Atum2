package com.teammetallurgy.atum.client.render.tileentity;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.wood.CrateBlock;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.CrateTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class CrateRender extends BlockEntityRenderer<CrateTileEntity> {
    private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();
    private final ModelPart crateCore;
    private final ModelPart crateLid;

    public CrateRender(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
        this.crateCore = new ModelPart(64, 64, 0, 0);
        this.crateCore.setPos(8.0F, 9.0F, 0.0F);
        this.crateCore.addBox(-16.0F, 0.0F, -8.0F, 16, 15, 16, 0.0F);
        this.crateLid = new ModelPart(64, 64, 0, 32);
        this.crateLid.setPos(8.0F, 8.0F, 0.0F);
        this.crateLid.addBox(-16.0F, 0.0F, -8.0F, 16, 1, 16, 0.0F);
    }

    @Override
    public void render(@Nonnull CrateTileEntity crate, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        BlockState state = crate.getLevel() != null ? crate.getBlockState() : AtumBlocks.PALM_CRATE.defaultBlockState().setValue(CrateBlock.FACING, Direction.SOUTH);
        Block block = state.getBlock();
        if (block instanceof CrateBlock) {
            matrixStack.pushPose();
            float facingAngle = state.getValue(CrateBlock.FACING).toYRot();
            matrixStack.translate(0.5D, 0.5D, 0.5D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(-facingAngle));
            matrixStack.translate(-0.5D, -0.5D, -0.5D);
            float lidAngle = crate.getLidAngle(partialTicks);
            lidAngle = 1.0F - lidAngle;
            lidAngle = 1.0F - lidAngle * lidAngle * lidAngle;
            VertexConsumer vertexBuilder = getBuilder(crate, buffer);
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
            matrixStack.translate(-0.5D, -1.5D, 0.5D);

            DoubleBlockCombiner.NeighborCombineResult<?> callbackWrapper = DoubleBlockCombiner.Combiner::acceptNone;
            int brightness = ((Int2IntFunction) callbackWrapper.apply(new BrightnessCombiner())).applyAsInt(combinedLight);
            this.renderCrate(matrixStack, vertexBuilder, this.crateCore, this.crateLid, lidAngle, brightness, combinedOverlay);
            matrixStack.popPose();
        }
    }

    private void renderCrate(PoseStack matrixStack, VertexConsumer vertexBuilder, ModelPart core, ModelPart lid, float lidAngle, int light, int combinedOverlay) {
        lid.zRot = (lidAngle * ((float) Math.PI / 10.0F));
        core.render(matrixStack, vertexBuilder, light, combinedOverlay);
        lid.render(matrixStack, vertexBuilder, light, combinedOverlay);
    }

    private VertexConsumer getBuilder(@Nonnull CrateTileEntity crate, @Nonnull MultiBufferSource buffer) {
        String name = Objects.requireNonNull(crate.getBlockState().getBlock().getRegistryName()).getPath();
        ResourceLocation crateTexture = CACHE.get(name);

        if (crateTexture == null) {
            crateTexture = new ResourceLocation(Atum.MOD_ID, "textures/entity/chest/" + name + ".png");
            CACHE.put(name, crateTexture);
        }
        return buffer.getBuffer(RenderType.entityCutout(crateTexture));
    }
}
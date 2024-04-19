package com.teammetallurgy.atum.client.render.tileentity;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.wood.CrateBlock;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.CrateTileEntity;
import com.teammetallurgy.atum.client.ClientHandler;
import com.teammetallurgy.atum.init.AtumBlocks;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class CrateRender implements BlockEntityRenderer<CrateTileEntity> {
    private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();
    private final ModelPart crateCore;
    private final ModelPart crateLid;

    public CrateRender(BlockEntityRendererProvider.Context context) {
        ModelPart part = context.bakeLayer(ClientHandler.CRATE);
        this.crateCore = part.getChild("crate_core");
        this.crateLid = part.getChild("crate_lid");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("crate_core", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, 0.0F, -8.0F, 16, 15, 16), PartPose.offset(8.0F, 9.0F, 0.0F));
        partDefinition.addOrReplaceChild("crate_lid", CubeListBuilder.create().texOffs(0, 32).addBox(-16.0F, 0.0F, -8.0F, 16, 1, 16), PartPose.offset(8.0F, 8.0F, 0.0F));
        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public void render(@Nonnull CrateTileEntity crate, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        BlockState state = crate.getLevel() != null ? crate.getBlockState() : AtumBlocks.PALM_CRATE.get().defaultBlockState().setValue(CrateBlock.FACING, Direction.SOUTH);
        Block block = state.getBlock();
        if (block instanceof CrateBlock) {
            poseStack.pushPose();
            float facingAngle = state.getValue(CrateBlock.FACING).toYRot();
            poseStack.translate(0.5D, 0.5D, 0.5D);
            poseStack.mulPose(Axis.YP.rotationDegrees(-facingAngle));
            poseStack.translate(-0.5D, -0.5D, -0.5D);
            float lidAngle = crate.getOpenNess(partialTicks);
            lidAngle = 1.0F - lidAngle;
            lidAngle = 1.0F - lidAngle * lidAngle * lidAngle;
            VertexConsumer vertexBuilder = getBuilder(crate, buffer);
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
            poseStack.translate(-0.5D, -1.5D, 0.5D);

            DoubleBlockCombiner.NeighborCombineResult<?> callbackWrapper = DoubleBlockCombiner.Combiner::acceptNone;
            int brightness = ((Int2IntFunction) callbackWrapper.apply(new BrightnessCombiner())).applyAsInt(combinedLight);
            this.renderCrate(poseStack, vertexBuilder, this.crateCore, this.crateLid, lidAngle, brightness, combinedOverlay);
            poseStack.popPose();
        }
    }

    private void renderCrate(PoseStack poseStack, VertexConsumer vertexBuilder, ModelPart core, ModelPart lid, float lidAngle, int light, int combinedOverlay) {
        lid.zRot = (lidAngle * ((float) Math.PI / 10.0F));
        core.render(poseStack, vertexBuilder, light, combinedOverlay);
        lid.render(poseStack, vertexBuilder, light, combinedOverlay);
    }

    private VertexConsumer getBuilder(@Nonnull CrateTileEntity crate, @Nonnull MultiBufferSource buffer) {
        String name = Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(crate.getBlockState().getBlock())).getPath();
        ResourceLocation crateTexture = CACHE.get(name);

        if (crateTexture == null) {
            crateTexture = new ResourceLocation(Atum.MOD_ID, "textures/entity/chest/" + name + ".png");
            CACHE.put(name, crateTexture);
        }
        return buffer.getBuffer(RenderType.entityCutout(crateTexture));
    }
}
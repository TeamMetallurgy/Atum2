package com.teammetallurgy.atum.client.render.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.SarcophagusBlock;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.SarcophagusTileEntity;
import com.teammetallurgy.atum.client.ClientHandler;
import com.teammetallurgy.atum.init.AtumBlocks;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;

import javax.annotation.Nonnull;

public class SarcophagusRender implements BlockEntityRenderer<SarcophagusTileEntity> {
    private static final ResourceLocation SARCOPHAGUS = new ResourceLocation(Atum.MOD_ID, "textures/entity/chest/sarcophagus.png");
    private static final RenderType SARCOPHAGUS_RENDER = RenderType.entityCutout(SARCOPHAGUS);
    public ModelPart sarcophagusBase;
    public ModelPart sarcophagusLid;
    public ModelPart sarcophagusLiddeco1;
    public ModelPart sarcophagusLiddeco2;
    public ModelPart sarcophagusLiddeco3;
    public ModelPart sarcophagusGemchest;
    public ModelPart sarcophagusGemhead;

    public SarcophagusRender(BlockEntityRendererProvider.Context context) {
        ModelPart part = context.bakeLayer(ClientHandler.SARCOPHAGUS);
        this.sarcophagusBase = part.getChild("sarcophagus_base");
        this.sarcophagusLid = part.getChild("sarcophagus_lid");
        this.sarcophagusLiddeco1 = part.getChild("sarcophagus_liddeco_1");
        this.sarcophagusLiddeco2 = part.getChild("sarcophagus_liddeco_2");
        this.sarcophagusLiddeco3 = part.getChild("sarcophagus_liddeco_3");
        this.sarcophagusGemchest = part.getChild("sarcophagus_gemchest");
        this.sarcophagusGemhead = part.getChild("sarcophagus_gemhead");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("sarcophagus_base", CubeListBuilder.create().texOffs(0, 19).addBox(-16.0F, 0.0F, -16.0F, 30, 10, 14), PartPose.offset(1.0F, 14.0F, 9.0F));
        partDefinition.addOrReplaceChild("sarcophagus_lid", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, -2.0F, -16.0F, 30, 2, 14), PartPose.offset(1.0F, 14.0F, 9.0F));
        partDefinition.addOrReplaceChild("sarcophagus_liddeco_1", CubeListBuilder.create().texOffs(48, 51).addBox(-15.0F, -3.0F, -15.0F, 28, 1, 12), PartPose.offset(1.0F, 14.0F, 9.0F));
        partDefinition.addOrReplaceChild("sarcophagus_liddeco_2", CubeListBuilder.create().texOffs(90, 0).addBox(-14.0F, -4.0F, -13.0F, 8, 1, 8), PartPose.offset(1.0F, 14.0F, 9.0F));
        partDefinition.addOrReplaceChild("sarcophagus_liddeco_3", CubeListBuilder.create().texOffs(0, 45).addBox(-4.0F, -4.0F, -13.0F, 15, 1, 8), PartPose.offset(1.0F, 14.0F, 9.0F));
        partDefinition.addOrReplaceChild("sarcophagus_gemchest", CubeListBuilder.create().texOffs(0, 45).addBox(0.0F, -4.5F, -10.0F, 2, 2, 2), PartPose.offset(1.0F, 14.0F, 9.0F));
        partDefinition.addOrReplaceChild("sarcophagus_gemhead", CubeListBuilder.create().texOffs(0, 45).addBox(-12.0F, -4.5F, -10.0F, 2, 2, 2), PartPose.offset(1.0F, 14.0F, 9.0F));
        return LayerDefinition.create(meshDefinition, 128, 64);
    }

    @Override
    public void render(SarcophagusTileEntity sarcophagus, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Level level = sarcophagus.getLevel();
        boolean worldNotNull = level != null;
        BlockState state = worldNotNull ? sarcophagus.getBlockState() : AtumBlocks.SARCOPHAGUS.get().defaultBlockState().setValue(SarcophagusBlock.FACING, Direction.SOUTH);
        ChestType type = state.hasProperty(SarcophagusBlock.TYPE) ? state.getValue(SarcophagusBlock.TYPE) : ChestType.SINGLE;
        Block block = state.getBlock();
        if (block instanceof SarcophagusBlock sarcophagusBlock) { //Actually left side, but whatever
            poseStack.pushPose();
            Direction facing = state.getValue(SarcophagusBlock.FACING);
            float facingAngle = facing.toYRot();
            if (facing == Direction.NORTH || facing == Direction.SOUTH) {
                facingAngle = facing.getOpposite().toYRot();
            }
            poseStack.translate(0.5D, 0.5D, 0.5D);
            poseStack.mulPose(Axis.YP.rotationDegrees(facingAngle));
            poseStack.translate(-0.5D, -0.5D, -0.5D);
            DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> callbackWrapper;
            if (worldNotNull) {
                callbackWrapper = sarcophagusBlock.combine(state, level, sarcophagus.getBlockPos(), true);
            } else {
                callbackWrapper = DoubleBlockCombiner.Combiner::acceptNone;
            }
            int light = callbackWrapper.apply(new BrightnessCombiner<>()).applyAsInt(combinedLight);
            VertexConsumer vertexBuilder = buffer.getBuffer(SARCOPHAGUS_RENDER);
            poseStack.translate(0.0D, 1.5D, 0.5D);
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));

            if (type == ChestType.RIGHT) {
                float lidAngle = callbackWrapper.apply(SarcophagusBlock.opennessCombiner(sarcophagus)).get(partialTicks);
                lidAngle = 1.0F - lidAngle;
                lidAngle = 1.0F - lidAngle * lidAngle * lidAngle;
                renderSarcophagus(poseStack, vertexBuilder, this.sarcophagusBase, this.sarcophagusLid, this.sarcophagusLiddeco1, this.sarcophagusLiddeco2, this.sarcophagusLiddeco3, this.sarcophagusGemhead, this.sarcophagusGemchest, lidAngle, light, combinedOverlay);
            } else if (level == null) { //Inventory render
                poseStack.scale(0.75F, 0.75F, 0.75F);
                poseStack.translate(-0.7D, 0.3D, 0.0D);
                renderSarcophagus(poseStack, vertexBuilder, this.sarcophagusBase, this.sarcophagusLid, this.sarcophagusLiddeco1, this.sarcophagusLiddeco2, this.sarcophagusLiddeco3, this.sarcophagusGemhead, this.sarcophagusGemchest, 0, light, combinedOverlay);
            }
            poseStack.popPose();
        }
    }

    private void renderSarcophagus(PoseStack poseStack, VertexConsumer vertexBuilder, ModelPart base, ModelPart lid, ModelPart liddeco1, ModelPart liddeco2, ModelPart liddeco3, ModelPart gemhead, ModelPart gemchest, float lidAngle, int light, int combinedOverlay) {
        lid.yRot = -lidAngle / 2;
        liddeco1.yRot = -lidAngle / 2;
        liddeco2.yRot = -lidAngle / 2;
        liddeco3.yRot = -lidAngle / 2;
        gemhead.yRot = -lidAngle / 2;
        gemchest.yRot = -lidAngle / 2;
        base.render(poseStack, vertexBuilder, light, combinedOverlay);
        lid.render(poseStack, vertexBuilder, light, combinedOverlay);
        liddeco1.render(poseStack, vertexBuilder, light, combinedOverlay);
        liddeco2.render(poseStack, vertexBuilder, light, combinedOverlay);
        liddeco3.render(poseStack, vertexBuilder, light, combinedOverlay);
        gemhead.render(poseStack, vertexBuilder, light, combinedOverlay);
        gemchest.render(poseStack, vertexBuilder, light, combinedOverlay);
    }
}
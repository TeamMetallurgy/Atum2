package com.teammetallurgy.atum.client.render.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.SarcophagusBlock;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.SarcophagusTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Vector3f;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class SarcophagusRender extends BlockEntityRenderer<SarcophagusTileEntity> {
    private static final ResourceLocation SARCOPHAGUS = new ResourceLocation(Atum.MOD_ID, "textures/entity/chest/sarcophagus.png");
    private static final RenderType SARCOPHAGUS_RENDER = RenderType.entityCutout(SARCOPHAGUS);
    public ModelPart sarcophagusBase;
    public ModelPart sarcophagusLid;
    public ModelPart sarcophagusLiddeco1;
    public ModelPart sarcophagusLiddeco2;
    public ModelPart sarcophagusLiddeco3;
    public ModelPart sarcophagusGemchest;
    public ModelPart sarcophagusGemhead;

    public SarcophagusRender(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
        this.sarcophagusLid = new ModelPart(128, 64, 0, 0);
        this.sarcophagusLid.setPos(1.0F, 14.0F, 9.0F);
        this.sarcophagusLid.addBox(-16.0F, -2.0F, -16.0F, 30, 2, 14, 0.0F);
        this.sarcophagusGemchest = new ModelPart(128, 64, 0, 45);
        this.sarcophagusGemchest.setPos(1.0F, 14.0F, 9.0F);
        this.sarcophagusGemchest.addBox(0.0F, -4.5F, -10.0F, 2, 2, 2, 0.0F);
        this.sarcophagusBase = new ModelPart(128, 64, 0, 19);
        this.sarcophagusBase.setPos(1.0F, 14.0F, 9.0F);
        this.sarcophagusBase.addBox(-16.0F, 0.0F, -16.0F, 30, 10, 14, 0.0F);
        this.sarcophagusLiddeco3 = new ModelPart(128, 64, 0, 45);
        this.sarcophagusLiddeco3.setPos(1.0F, 14.0F, 9.0F);
        this.sarcophagusLiddeco3.addBox(-4.0F, -4.0F, -13.0F, 15, 1, 8, 0.0F);
        this.sarcophagusGemhead = new ModelPart(128, 64, 0, 45);
        this.sarcophagusGemhead.setPos(1.0F, 14.0F, 9.0F);
        this.sarcophagusGemhead.addBox(-12.0F, -4.5F, -10.0F, 2, 2, 2, 0.0F);
        this.sarcophagusLiddeco1 = new ModelPart(128, 64, 48, 51);
        this.sarcophagusLiddeco1.setPos(1.0F, 14.0F, 9.0F);
        this.sarcophagusLiddeco1.addBox(-15.0F, -3.0F, -15.0F, 28, 1, 12, 0.0F);
        this.sarcophagusLiddeco2 = new ModelPart(128, 64, 90, 0);
        this.sarcophagusLiddeco2.setPos(1.0F, 14.0F, 9.0F);
        this.sarcophagusLiddeco2.addBox(-14.0F, -4.0F, -13.0F, 8, 1, 8, 0.0F);
    }

    @Override
    public void render(SarcophagusTileEntity sarcophagus, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Level world = sarcophagus.getLevel();
        boolean worldNotNull = world != null;
        BlockState state = worldNotNull ? sarcophagus.getBlockState() : AtumBlocks.SARCOPHAGUS.defaultBlockState().setValue(SarcophagusBlock.FACING, Direction.SOUTH);
        ChestType type = state.hasProperty(SarcophagusBlock.TYPE) ? state.getValue(SarcophagusBlock.TYPE) : ChestType.SINGLE;
        Block block = state.getBlock();
        if (block instanceof SarcophagusBlock) { //Actually left side, but whatever
            SarcophagusBlock sarcophagusBlock = (SarcophagusBlock) block;
            matrixStack.pushPose();
            Direction facing = state.getValue(SarcophagusBlock.FACING);
            float facingAngle = facing.toYRot();
            if (facing == Direction.NORTH || facing == Direction.SOUTH) {
                facingAngle = facing.getOpposite().toYRot();
            }
            matrixStack.translate(0.5D, 0.5D, 0.5D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(facingAngle));
            matrixStack.translate(-0.5D, -0.5D, -0.5D);
            DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> callbackWrapper;
            if (worldNotNull) {
                callbackWrapper = sarcophagusBlock.combine(state, world, sarcophagus.getBlockPos(), true);
            } else {
                callbackWrapper = DoubleBlockCombiner.Combiner::acceptNone;
            }
            int light = callbackWrapper.apply(new BrightnessCombiner<>()).applyAsInt(combinedLight);
            VertexConsumer vertexBuilder = buffer.getBuffer(SARCOPHAGUS_RENDER);
            matrixStack.translate(0.0D, 1.5D, 0.5D);
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));

            if (type == ChestType.RIGHT) {
                float lidAngle = callbackWrapper.apply(SarcophagusBlock.opennessCombiner(sarcophagus)).get(partialTicks);
                lidAngle = 1.0F - lidAngle;
                lidAngle = 1.0F - lidAngle * lidAngle * lidAngle;
                renderSarcophagus(matrixStack, vertexBuilder, this.sarcophagusBase, this.sarcophagusLid, this.sarcophagusLiddeco1, this.sarcophagusLiddeco2, this.sarcophagusLiddeco3, this.sarcophagusGemhead, this.sarcophagusGemchest, lidAngle, light, combinedOverlay);
            } else if (world == null) { //Inventory render
                matrixStack.scale(0.75F, 0.75F, 0.75F);
                matrixStack.translate(-0.7D, 0.3D, 0.0D);
                renderSarcophagus(matrixStack, vertexBuilder, this.sarcophagusBase, this.sarcophagusLid, this.sarcophagusLiddeco1, this.sarcophagusLiddeco2, this.sarcophagusLiddeco3, this.sarcophagusGemhead, this.sarcophagusGemchest, 0, light, combinedOverlay);
            }
            matrixStack.popPose();
        }
    }

    private void renderSarcophagus(PoseStack matrixStack, VertexConsumer vertexBuilder, ModelPart base, ModelPart lid, ModelPart liddeco1, ModelPart liddeco2, ModelPart liddeco3, ModelPart gemhead, ModelPart gemchest, float lidAngle, int light, int combinedOverlay) {
        lid.yRot = -lidAngle / 2;
        liddeco1.yRot = -lidAngle / 2;
        liddeco2.yRot = -lidAngle / 2;
        liddeco3.yRot = -lidAngle / 2;
        gemhead.yRot = -lidAngle / 2;
        gemchest.yRot = -lidAngle / 2;
        base.render(matrixStack, vertexBuilder, light, combinedOverlay);
        lid.render(matrixStack, vertexBuilder, light, combinedOverlay);
        liddeco1.render(matrixStack, vertexBuilder, light, combinedOverlay);
        liddeco2.render(matrixStack, vertexBuilder, light, combinedOverlay);
        liddeco3.render(matrixStack, vertexBuilder, light, combinedOverlay);
        gemhead.render(matrixStack, vertexBuilder, light, combinedOverlay);
        gemchest.render(matrixStack, vertexBuilder, light, combinedOverlay);
    }
}
package com.teammetallurgy.atum.client.render.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.machines.QuernBlock;
import com.teammetallurgy.atum.blocks.machines.tileentity.QuernTileEntity;
import com.teammetallurgy.atum.client.RenderUtils;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class QuernRender extends BlockEntityRenderer<QuernTileEntity> {
    private static final ResourceLocation QUERN_STONE = new ResourceLocation(Atum.MOD_ID, "textures/block/quern_stone.png");
    private static final RenderType QUERN_RENDER = RenderType.entityCutout(QUERN_STONE);
    public ModelPart core;
    private ModelPart coreLeft;
    private ModelPart coreFront;
    private ModelPart coreBack;
    private ModelPart coreRight;
    private ModelPart handle;

    public QuernRender(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
        this.coreBack = new ModelPart(64, 64, 0, 34);
        this.coreBack.setPos(0.0F, 0.0F, 0.0F);
        this.coreBack.addBox(-4.0F, -2.0F, 5.0F, 8, 4, 1, 0.0F);
        this.coreLeft = new ModelPart(64, 64, 0, 48);
        this.coreLeft.setPos(0.0F, 0.0F, 0.0F);
        this.coreLeft.addBox(-6.0F, -2.0F, -4.0F, 1, 4, 8, 0.0F);
        this.coreRight = new ModelPart(64, 64, 0, 48);
        this.coreRight.setPos(0.0F, 0.0F, 0.0F);
        this.coreRight.addBox(5.0F, -2.0F, -4.0F, 1, 4, 8, 0.0F);
        this.coreFront = new ModelPart(64, 64, 0, 34);
        this.coreFront.setPos(0.0F, 0.0F, 0.0F);
        this.coreFront.addBox(-4.0F, -2.0F, -6.0F, 8, 4, 1, 0.0F);
        this.core = new ModelPart(64, 64, 0, 18);
        this.core.setPos(0.0F, 20.0F, 0.0F);
        this.core.addBox(-5.0F, -2.0F, -5.0F, 10, 4, 10, 0.0F);
        this.handle = new ModelPart(64, 64, 0, 0);
        this.handle.setPos(0.0F, 0.0F, 0.0F);
        this.handle.addBox(-5.0F, -5.0F, -3.0F, 1, 3, 1, 0.0F);
        this.core.addChild(this.coreBack);
        this.core.addChild(this.coreLeft);
        this.core.addChild(this.coreRight);
        this.core.addChild(this.coreFront);
        this.core.addChild(this.handle);
    }

    @Override
    public void render(@Nonnull QuernTileEntity quern, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Level world = quern.getLevel();
        boolean hasWorld = world != null;
        BlockState state = hasWorld ? quern.getBlockState() : AtumBlocks.QUERN.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
        Block block = state.getBlock();

        if (block instanceof QuernBlock) {
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 1.5D, 0.5D);
            matrixStack.scale(0.95F, 1.0F, 0.95F);
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-180));
            float angle = state.getValue(QuernBlock.FACING).toYRot();
            if ((double) Math.abs(angle) > 1.0E-5D) {
                matrixStack.mulPose(Vector3f.YP.rotationDegrees(angle));
            }

            float quernRotation = quern.getRotations();
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(-quernRotation));

            VertexConsumer builder = buffer.getBuffer(QUERN_RENDER);
            DoubleBlockCombiner.NeighborCombineResult<?> callbackWrapper = DoubleBlockCombiner.Combiner::acceptNone;
            int brightness = ((Int2IntFunction) callbackWrapper.apply(new BrightnessCombiner())).applyAsInt(combinedLight);
            this.core.render(matrixStack, builder, brightness, combinedOverlay);
            matrixStack.popPose();

            ItemStack stack = quern.getItem(0);
            if (!stack.isEmpty()) {
                RenderUtils.renderItem(quern, stack, quernRotation, -0.7D, true, true, matrixStack, buffer, combinedLight, combinedOverlay);
            }
        }
    }
}
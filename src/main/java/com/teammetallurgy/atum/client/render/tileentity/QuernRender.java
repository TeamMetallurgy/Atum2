package com.teammetallurgy.atum.client.render.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.machines.QuernBlock;
import com.teammetallurgy.atum.blocks.machines.tileentity.QuernTileEntity;
import com.teammetallurgy.atum.client.ClientHandler;
import com.teammetallurgy.atum.client.RenderUtils;
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
public class QuernRender implements BlockEntityRenderer<QuernTileEntity> {
    private static final ResourceLocation QUERN_STONE = new ResourceLocation(Atum.MOD_ID, "textures/block/quern_stone.png");
    private static final RenderType QUERN_RENDER = RenderType.entityCutout(QUERN_STONE);
    private final ModelPart core;
    private final ModelPart coreLeft;
    private final ModelPart coreRight;
    private final ModelPart coreFront;
    private final ModelPart coreBack;
    private final ModelPart handle;

    public QuernRender(BlockEntityRendererProvider.Context context) {
        ModelPart part = context.bakeLayer(ClientHandler.QUERN);
        this.core = part.getChild("core");
        this.coreLeft = this.core.getChild("core_left");
        this.coreRight = this.core.getChild("core_right");
        this.coreFront = this.core.getChild("core_front");
        this.coreBack = this.core.getChild("core_back");
        this.handle = this.core.getChild("handle");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        PartDefinition core = partDefinition.addOrReplaceChild("core", CubeListBuilder.create().texOffs(0, 18).addBox(-5.0F, -2.0F, -5.0F, 10, 4, 10), PartPose.offset(0.0F, 20.0F, 0.0F));
        core.addOrReplaceChild("core_left", CubeListBuilder.create().texOffs(0, 48).addBox(-6.0F, -2.0F, -4.0F, 1, 4, 8), PartPose.offset(0.0F, 0.0F, 0.0F));
        core.addOrReplaceChild("core_right", CubeListBuilder.create().texOffs(0, 48).addBox(5.0F, -2.0F, -4.0F, 1, 4, 8), PartPose.offset(0.0F, 0.0F, 0.0F));
        core.addOrReplaceChild("core_front", CubeListBuilder.create().texOffs(0, 34).addBox(-4.0F, -2.0F, -6.0F, 8, 4, 1), PartPose.offset(0.0F, 0.0F, 0.0F));
        core.addOrReplaceChild("core_back", CubeListBuilder.create().texOffs(0, 34).addBox(-4.0F, -2.0F, 5.0F, 8, 4, 1), PartPose.offset(0.0F, 0.0F, 0.0F));
        core.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -5.0F, -3.0F, 1, 3, 1), PartPose.offset(0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public void render(@Nonnull QuernTileEntity quern, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Level level = quern.getLevel();
        boolean hasWorld = level != null;
        BlockState state = hasWorld ? quern.getBlockState() : AtumBlocks.QUERN.get().defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
        Block block = state.getBlock();

        if (block instanceof QuernBlock) {
            poseStack.pushPose();
            poseStack.translate(0.5D, 1.5D, 0.5D);
            poseStack.scale(0.95F, 1.0F, 0.95F);
            poseStack.mulPose(Axis.ZP.rotationDegrees(-180));
            float angle = state.getValue(QuernBlock.FACING).toYRot();
            if ((double) Math.abs(angle) > 1.0E-5D) {
                poseStack.mulPose(Axis.YP.rotationDegrees(angle));
            }

            float quernRotation = quern.getRotations();
            poseStack.mulPose(Axis.YP.rotationDegrees(-quernRotation));

            VertexConsumer builder = buffer.getBuffer(QUERN_RENDER);
            DoubleBlockCombiner.NeighborCombineResult<?> callbackWrapper = DoubleBlockCombiner.Combiner::acceptNone;
            int brightness = ((Int2IntFunction) callbackWrapper.apply(new BrightnessCombiner())).applyAsInt(combinedLight);
            this.core.render(poseStack, builder, brightness, combinedOverlay);
            poseStack.popPose();

            ItemStack stack = quern.getItem(0);
            if (!stack.isEmpty()) {
                RenderUtils.renderItem(quern, stack, quernRotation, -0.7D, true, true, poseStack, buffer, combinedLight, combinedOverlay);
            }
        }
    }
}
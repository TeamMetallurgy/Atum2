package com.teammetallurgy.atum.client.render.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.curio.CurioDisplayBlock;
import com.teammetallurgy.atum.blocks.curio.tileentity.CurioDisplayTileEntity;
import com.teammetallurgy.atum.client.ClientHandler;
import com.teammetallurgy.atum.client.RenderUtils;
import net.minecraft.client.Minecraft;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public abstract class CurioDisplayTileEntityRender implements BlockEntityRenderer<CurioDisplayTileEntity> {
    private final ModelPart displayStand;
    private final ModelPart displayStand1;
    private final ModelPart displayStand2;
    private final ModelPart displayStand3;

    public CurioDisplayTileEntityRender(BlockEntityRendererProvider.Context context) {
        ModelPart part = context.bakeLayer(ClientHandler.CURIO_DISPLAY);
        this.displayStand = part.getChild("display_stand");
        this.displayStand1 = part.getChild("display_stand_1");
        this.displayStand2 = part.getChild("display_stand_2");
        this.displayStand3 = part.getChild("display_stand_3");
    }

    public abstract Block getBlock();

    public static LayerDefinition createLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("display_stand", CubeListBuilder.create().texOffs(48, 0).addBox(-2.0F, -7.0F, -2.0F, 4.0F, 6.0F, 4.0F), PartPose.offset(0.0F, 24.0F, 0.0F));
        partDefinition.addOrReplaceChild("display_stand_1", CubeListBuilder.create().texOffs(24, 21).addBox(-5.0F, -8.0F, -5.0F, 10.0F, 1.0F, 10.0F), PartPose.offset(0.0F, 24.0F, 0.0F));
        partDefinition.addOrReplaceChild("display_stand_2", CubeListBuilder.create().texOffs(24, 21).addBox(-5.0F, -1.0F, -5.0F, 10.0F, 1.0F, 10.0F), PartPose.offset(0.0F, 24.0F, 0.0F));
        partDefinition.addOrReplaceChild("display_stand_3", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -16.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.offset(0.0F, 24.0F, 0.0F));
        return LayerDefinition.create(meshDefinition, 64, 32);
    }

    @Override
    public void render(@Nonnull CurioDisplayTileEntity tileEntity, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Level world = tileEntity.getLevel();
        boolean hasWorld = world != null;
        BlockState state = hasWorld ? tileEntity.getBlockState() : getBlock().defaultBlockState();
        Block block = state.getBlock();

        if (block instanceof CurioDisplayBlock) {
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 1.5D, 0.5D);
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-180));

            RenderType CURIO_DISPLAY_RENDER = RenderType.entityCutout(new ResourceLocation(Atum.MOD_ID, "textures/block/" + getBlock().getRegistryName().getPath() + ".png"));
            VertexConsumer builder = buffer.getBuffer(CURIO_DISPLAY_RENDER);
            this.displayStand.render(matrixStack, builder, combinedLight, combinedOverlay);
            this.displayStand1.render(matrixStack, builder, combinedLight, combinedOverlay);
            this.displayStand2.render(matrixStack, builder, combinedLight, combinedOverlay);
            this.displayStand3.render(matrixStack, builder, combinedLight, combinedOverlay);
            matrixStack.popPose();

            ItemStack stack = tileEntity.getItem(0);
            if (!stack.isEmpty()) {
                RenderUtils.renderItem(tileEntity, stack, Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation(), -0.5D, false, !(stack.getItem() instanceof BlockItem), -360.0F, matrixStack, buffer, combinedLight, combinedOverlay);
            }
        }
    }
}
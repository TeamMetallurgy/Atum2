package com.teammetallurgy.atum.client.render.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.curio.CurioDisplayBlock;
import com.teammetallurgy.atum.blocks.curio.tileentity.CurioDisplayTileEntity;
import com.teammetallurgy.atum.client.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public abstract class CurioDisplayTileEntityRender extends BlockEntityRenderer<CurioDisplayTileEntity> {
    private final ModelPart displayStand;
    private final ModelPart displayStand1;
    private final ModelPart displayStand2;
    private final ModelPart displayStand3;

    public CurioDisplayTileEntityRender(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
        this.displayStand = new ModelPart(64, 32, 48, 0);
        this.displayStand.addBox(-2.0F, -7.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);
        this.displayStand.setPos(0.0F, 24.0F, 0.0F);
        this.displayStand1 = new ModelPart(64, 32, 24, 21);
        this.displayStand1.addBox(-5.0F, -8.0F, -5.0F, 10.0F, 1.0F, 10.0F, 0.0F, false);
        this.displayStand1.setPos(0.0F, 24.0F, 0.0F);
        this.displayStand2 = new ModelPart(64, 32, 24, 21);
        this.displayStand2.addBox(-5.0F, -1.0F, -5.0F, 10.0F, 1.0F, 10.0F, 0.0F, false);
        this.displayStand2.setPos(0.0F, 24.0F, 0.0F);
        this.displayStand3 = new ModelPart(64, 32, 0, 0);
        this.displayStand3.texOffs(0, 0).addBox(-4.0F, -16.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        this.displayStand3.setPos(0.0F, 24.0F, 0.0F);
    }

    public abstract Block getBlock();

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

            RenderType CURIO_DISPLAY_RENDER = RenderType.entityCutout(new ResourceLocation(Atum.MOD_ID, "textures/block/"+ getBlock().getRegistryName().getPath() + ".png"));
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
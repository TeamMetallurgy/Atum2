package com.teammetallurgy.atum.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.curio.CurioDisplayBlock;
import com.teammetallurgy.atum.blocks.curio.tileentity.CurioDisplayTileEntity;
import com.teammetallurgy.atum.client.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public abstract class CurioDisplayTileEntityRender extends TileEntityRenderer<CurioDisplayTileEntity> {
    private final ModelRenderer displayStand;
    private final ModelRenderer displayStand1;
    private final ModelRenderer displayStand2;
    private final ModelRenderer displayStand3;

    public CurioDisplayTileEntityRender(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
        this.displayStand = new ModelRenderer(64, 32, 48, 0);
        this.displayStand.addBox(-2.0F, -7.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);
        this.displayStand.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.displayStand1 = new ModelRenderer(64, 32, 24, 21);
        this.displayStand1.addBox(-5.0F, -8.0F, -5.0F, 10.0F, 1.0F, 10.0F, 0.0F, false);
        this.displayStand1.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.displayStand2 = new ModelRenderer(64, 32, 24, 21);
        this.displayStand2.addBox(-5.0F, -1.0F, -5.0F, 10.0F, 1.0F, 10.0F, 0.0F, false);
        this.displayStand2.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.displayStand3 = new ModelRenderer(64, 32, 0, 0);
        this.displayStand3.setTextureOffset(0, 0).addBox(-4.0F, -16.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        this.displayStand3.setRotationPoint(0.0F, 24.0F, 0.0F);
    }

    public abstract Block getBlock();

    @Override
    public void render(@Nonnull CurioDisplayTileEntity tileEntity, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        World world = tileEntity.getWorld();
        boolean hasWorld = world != null;
        BlockState state = hasWorld ? tileEntity.getBlockState() : getBlock().getDefaultState();
        Block block = state.getBlock();

        if (block instanceof CurioDisplayBlock) {
            matrixStack.push();
            matrixStack.translate(0.5D, 1.5D, 0.5D);
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(-180));

            RenderType CURIO_DISPLAY_RENDER = RenderType.getEntityCutout(new ResourceLocation(Atum.MOD_ID, "textures/block/"+ getBlock().getRegistryName().getPath() + ".png"));
            IVertexBuilder builder = buffer.getBuffer(CURIO_DISPLAY_RENDER);
            this.displayStand.render(matrixStack, builder, combinedLight, combinedOverlay);
            this.displayStand1.render(matrixStack, builder, combinedLight, combinedOverlay);
            this.displayStand2.render(matrixStack, builder, combinedLight, combinedOverlay);
            this.displayStand3.render(matrixStack, builder, combinedLight, combinedOverlay);
            matrixStack.pop();

            ItemStack stack = tileEntity.getStackInSlot(0);
            if (!stack.isEmpty()) {
                RenderUtils.renderItem(tileEntity, stack, Minecraft.getInstance().getRenderManager().getCameraOrientation(), -0.5D, false, !(stack.getItem() instanceof BlockItem), -360.0F, matrixStack, buffer, combinedLight, combinedOverlay);
            }
        }
    }
}
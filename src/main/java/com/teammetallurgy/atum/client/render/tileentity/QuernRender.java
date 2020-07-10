package com.teammetallurgy.atum.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.machines.QuernBlock;
import com.teammetallurgy.atum.blocks.machines.tileentity.QuernTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.misc.RenderUtils;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.DualBrightnessCallback;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class QuernRender extends TileEntityRenderer<QuernTileEntity> {
    private static final ResourceLocation QUERN_STONE = new ResourceLocation(Atum.MOD_ID, "textures/block/quern_stone.png");
    private static final RenderType QUERN_RENDER = RenderType.getEntityCutout(QUERN_STONE);
    public ModelRenderer core;
    private ModelRenderer coreLeft;
    private ModelRenderer coreFront;
    private ModelRenderer coreBack;
    private ModelRenderer coreRight;
    private ModelRenderer handle;

    public QuernRender(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
        this.coreBack = new ModelRenderer(64, 64, 0, 34);
        this.coreBack.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.coreBack.addBox(-4.0F, -2.0F, 5.0F, 8, 4, 1, 0.0F);
        this.coreLeft = new ModelRenderer(64, 64, 0, 48);
        this.coreLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.coreLeft.addBox(-6.0F, -2.0F, -4.0F, 1, 4, 8, 0.0F);
        this.coreRight = new ModelRenderer(64, 64, 0, 48);
        this.coreRight.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.coreRight.addBox(5.0F, -2.0F, -4.0F, 1, 4, 8, 0.0F);
        this.coreFront = new ModelRenderer(64, 64, 0, 34);
        this.coreFront.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.coreFront.addBox(-4.0F, -2.0F, -6.0F, 8, 4, 1, 0.0F);
        this.core = new ModelRenderer(64, 64, 0, 18);
        this.core.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.core.addBox(-5.0F, -2.0F, -5.0F, 10, 4, 10, 0.0F);
        this.handle = new ModelRenderer(64, 64, 0, 0);
        this.handle.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.handle.addBox(-5.0F, -5.0F, -3.0F, 1, 3, 1, 0.0F);
        this.core.addChild(this.coreBack);
        this.core.addChild(this.coreLeft);
        this.core.addChild(this.coreRight);
        this.core.addChild(this.coreFront);
        this.core.addChild(this.handle);
    }

    @Override
    public void render(@Nonnull QuernTileEntity quern, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        World world = quern.getWorld();
        boolean hasWorld = world != null;
        BlockState state = hasWorld ? quern.getBlockState() : AtumBlocks.QUERN.getDefaultState().with(ChestBlock.FACING, Direction.SOUTH);
        Block block = state.getBlock();

        if (block instanceof QuernBlock) {
            matrixStack.push();
            matrixStack.translate(0.5D, 1.5D, 0.5D);
            matrixStack.scale(0.95F, 1.0F, 0.95F);
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(-180));
            float angle = state.get(QuernBlock.FACING).getHorizontalAngle();
            if ((double) Math.abs(angle) > 1.0E-5D) {
                matrixStack.rotate(Vector3f.YP.rotationDegrees(angle));
            }

            float quernRotation = quern.getRotations();
            matrixStack.rotate(Vector3f.YP.rotationDegrees(-quernRotation));

            IVertexBuilder builder = buffer.getBuffer(QUERN_RENDER);
            TileEntityMerger.ICallbackWrapper<?> callbackWrapper = TileEntityMerger.ICallback::func_225537_b_;
            int brightness = ((Int2IntFunction) callbackWrapper.apply(new DualBrightnessCallback())).applyAsInt(combinedLight);
            this.core.render(matrixStack, builder, brightness, combinedOverlay);
            matrixStack.pop();

            ItemStack stack = quern.getStackInSlot(0);
            if (!stack.isEmpty()) {
                RenderUtils.renderItem(quern, stack, quernRotation, -0.7D, true, matrixStack, buffer, combinedLight, combinedOverlay);
            }
        }
    }
}
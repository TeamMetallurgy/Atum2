package com.teammetallurgy.atum.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.SarcophagusBlock;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.SarcophagusTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.DualBrightnessCallback;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class SarcophagusRender extends TileEntityRenderer<SarcophagusTileEntity> {
    private static final ResourceLocation SARCOPHAGUS = new ResourceLocation(Constants.MOD_ID, "textures/entity/chest/sarcophagus.png");
    private static final RenderType SARCOPHAGUS_RENDER = RenderType.getEntityCutout(SARCOPHAGUS);
    public ModelRenderer sarcophagusBase;
    public ModelRenderer sarcophagusLid;
    public ModelRenderer sarcophagusLiddeco1;
    public ModelRenderer sarcophagusLiddeco2;
    public ModelRenderer sarcophagusLiddeco3;
    public ModelRenderer sarcophagusGemchest;
    public ModelRenderer sarcophagusGemhead;

    public SarcophagusRender(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
        this.sarcophagusLid = new ModelRenderer(128, 64, 0, 0);
        this.sarcophagusLid.setRotationPoint(1.0F, 14.0F, 9.0F);
        this.sarcophagusLid.addBox(-16.0F, -2.0F, -16.0F, 30, 2, 14, 0.0F);
        this.sarcophagusGemchest = new ModelRenderer(128, 64, 0, 45);
        this.sarcophagusGemchest.setRotationPoint(1.0F, 14.0F, 9.0F);
        this.sarcophagusGemchest.addBox(0.0F, -4.5F, -10.0F, 2, 2, 2, 0.0F);
        this.sarcophagusBase = new ModelRenderer(128, 64, 0, 19);
        this.sarcophagusBase.setRotationPoint(1.0F, 14.0F, 9.0F);
        this.sarcophagusBase.addBox(-16.0F, 0.0F, -16.0F, 30, 10, 14, 0.0F);
        this.sarcophagusLiddeco3 = new ModelRenderer(128, 64, 0, 45);
        this.sarcophagusLiddeco3.setRotationPoint(1.0F, 14.0F, 9.0F);
        this.sarcophagusLiddeco3.addBox(-4.0F, -4.0F, -13.0F, 15, 1, 8, 0.0F);
        this.sarcophagusGemhead = new ModelRenderer(128, 64, 0, 45);
        this.sarcophagusGemhead.setRotationPoint(1.0F, 14.0F, 9.0F);
        this.sarcophagusGemhead.addBox(-12.0F, -4.5F, -10.0F, 2, 2, 2, 0.0F);
        this.sarcophagusLiddeco1 = new ModelRenderer(128, 64, 48, 51);
        this.sarcophagusLiddeco1.setRotationPoint(1.0F, 14.0F, 9.0F);
        this.sarcophagusLiddeco1.addBox(-15.0F, -3.0F, -15.0F, 28, 1, 12, 0.0F);
        this.sarcophagusLiddeco2 = new ModelRenderer(128, 64, 90, 0);
        this.sarcophagusLiddeco2.setRotationPoint(1.0F, 14.0F, 9.0F);
        this.sarcophagusLiddeco2.addBox(-14.0F, -4.0F, -13.0F, 8, 1, 8, 0.0F);
    }

    @Override
    public void render(SarcophagusTileEntity sarcophagus, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        World world = sarcophagus.getWorld();
        boolean worldNotNull = world != null;
        BlockState state = worldNotNull ? sarcophagus.getBlockState() : AtumBlocks.SARCOPHAGUS.getDefaultState().with(SarcophagusBlock.FACING, Direction.SOUTH);
        ChestType type = state.has(SarcophagusBlock.TYPE) ? state.get(SarcophagusBlock.TYPE) : ChestType.SINGLE;
        Block block = state.getBlock();
        if (block instanceof SarcophagusBlock) { //Actually left side, but whatever
            SarcophagusBlock sarcophagusBlock = (SarcophagusBlock) block;
            matrixStack.push();
            Direction facing = state.get(SarcophagusBlock.FACING);
            float facingAngle = facing.getHorizontalAngle();
            if (facing == Direction.NORTH || facing == Direction.SOUTH) {
                facingAngle = facing.getOpposite().getHorizontalAngle();
            }
            matrixStack.translate(0.5D, 0.5D, 0.5D);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(facingAngle));
            matrixStack.translate(-0.5D, -0.5D, -0.5D);
            TileEntityMerger.ICallbackWrapper<? extends ChestTileEntity> callbackWrapper;
            if (worldNotNull) {
                callbackWrapper = sarcophagusBlock.func_225536_a_(state, world, sarcophagus.getPos(), true);
            } else {
                callbackWrapper = TileEntityMerger.ICallback::func_225537_b_;
            }
            int light = callbackWrapper.apply(new DualBrightnessCallback<>()).applyAsInt(combinedLight);
            IVertexBuilder vertexBuilder = buffer.getBuffer(SARCOPHAGUS_RENDER);
            matrixStack.translate(0.0D, 1.5D, 0.5D);
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(180.0F));

            if (type == ChestType.RIGHT) {
                float lidAngle = callbackWrapper.apply(SarcophagusBlock.func_226917_a_(sarcophagus)).get(partialTicks);
                lidAngle = 1.0F - lidAngle;
                lidAngle = 1.0F - lidAngle * lidAngle * lidAngle;
                renderSarcophagus(matrixStack, vertexBuilder, this.sarcophagusBase, this.sarcophagusLid, this.sarcophagusLiddeco1, this.sarcophagusLiddeco2, this.sarcophagusLiddeco3, this.sarcophagusGemhead, this.sarcophagusGemchest, lidAngle, light, combinedOverlay);
            } else if (world == null) { //Inventory render
                matrixStack.scale(0.75F, 0.75F, 0.75F);
                matrixStack.translate(-0.7D, 0.3D, 0.0D);
                renderSarcophagus(matrixStack, vertexBuilder, this.sarcophagusBase, this.sarcophagusLid, this.sarcophagusLiddeco1, this.sarcophagusLiddeco2, this.sarcophagusLiddeco3, this.sarcophagusGemhead, this.sarcophagusGemchest, 0, light, combinedOverlay);
            }
            matrixStack.pop();
        }
    }

    private void renderSarcophagus(MatrixStack matrixStack, IVertexBuilder vertexBuilder, ModelRenderer base, ModelRenderer lid, ModelRenderer liddeco1, ModelRenderer liddeco2, ModelRenderer liddeco3, ModelRenderer gemhead, ModelRenderer gemchest, float lidAngle, int light, int combinedOverlay) {
        lid.rotateAngleY = -lidAngle / 2;
        liddeco1.rotateAngleY = -lidAngle / 2;
        liddeco2.rotateAngleY = -lidAngle / 2;
        liddeco3.rotateAngleY = -lidAngle / 2;
        gemhead.rotateAngleY = -lidAngle / 2;
        gemchest.rotateAngleY = -lidAngle / 2;
        base.render(matrixStack, vertexBuilder, light, combinedOverlay);
        lid.render(matrixStack, vertexBuilder, light, combinedOverlay);
        liddeco1.render(matrixStack, vertexBuilder, light, combinedOverlay);
        liddeco2.render(matrixStack, vertexBuilder, light, combinedOverlay);
        liddeco3.render(matrixStack, vertexBuilder, light, combinedOverlay);
        gemhead.render(matrixStack, vertexBuilder, light, combinedOverlay);
        gemchest.render(matrixStack, vertexBuilder, light, combinedOverlay);
    }
}
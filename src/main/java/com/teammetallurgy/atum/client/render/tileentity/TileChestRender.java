package com.teammetallurgy.atum.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.teammetallurgy.atum.blocks.base.tileentity.ChestBaseTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class TileChestRender extends TileEntityRenderer<ChestBaseTileEntity> {
    //Sarcophagus
    public ModelRenderer sarcophagusBase;
    public ModelRenderer sarcophagusLid;
    public ModelRenderer sarcophagusLiddeco1;
    public ModelRenderer sarcophagusLiddeco2;
    public ModelRenderer sarcophagusLiddeco3;
    public ModelRenderer sarcophagusGemchest;
    public ModelRenderer sarcophagusGemhead;

    public TileChestRender(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
        //Sarcophagus
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
        //        this.sarcophagusLid.render(0.0625F); TODO, kept for later
        //        this.sarcophagusBase.render(0.0625F);
        //        this.sarcophagusLiddeco1.render(0.0625F);
        //        this.sarcophagusLiddeco2.render(0.0625F);
        //        this.sarcophagusLiddeco3.render(0.0625F);
        //        this.sarcophagusGemhead.render(0.0625F);
        //        this.sarcophagusGemchest.render(0.0625F);
    }

    @Override
    public void render(@Nonnull ChestBaseTileEntity chest, float v, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer renderTypeBuffer, int i, int i1) {

    }
    /*private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap(); //TODO
    private final SarcophagusModel sarcophagus = new SarcophagusModel();
    private final ChestModel simpleChest = new ChestModel();
    private final ChestModel largeChest = new LargeChestModel();

    @Override
    public void render(@Nonnull ChestBaseTileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.enableDepthTest();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        BlockState state = te.hasWorld() ? te.getBlockState() : AtumBlocks.LIMESTONE_CHEST.getDefaultState().with(ChestBaseBlock.FACING, Direction.SOUTH);
        ChestType type = state.has(ChestBaseBlock.TYPE) ? state.get(ChestBaseBlock.TYPE) : ChestType.SINGLE;

        if (type != ChestType.LEFT) {
            boolean isDouble = type != ChestType.SINGLE;
            ChestModel modelChest = isDouble ? this.largeChest : this.simpleChest;
            if (destroyStage >= 0) {
                this.bindTexture(DESTROY_STAGES[destroyStage]);
                GlStateManager.matrixMode(5890);
                GlStateManager.pushMatrix();
                GlStateManager.scalef(isDouble ? 8.0F : 4.0F, 4.0F, 1.0F);
                GlStateManager.translatef(0.0625F, 0.0625F, 0.0625F);
                GlStateManager.matrixMode(5888);
            } else {
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                String name = Objects.requireNonNull(state.getBlock().getRegistryName()).getPath() + (isDouble ? "_double" : "");
                ResourceLocation chestTexture = CACHE.get(name);

                if (chestTexture == null) {
                    chestTexture = new ResourceLocation(Constants.MOD_ID, "textures/block/chest/" + name + ".png");
                    CACHE.put(name, chestTexture);
                }

                this.bindTexture(chestTexture);
            }

            GlStateManager.pushMatrix();
            GlStateManager.enableRescaleNormal();
            GlStateManager.translatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
            GlStateManager.scalef(1.0F, -1.0F, -1.0F);
            float angle = state.get(ChestBaseBlock.FACING).getHorizontalAngle();
            if ((double) Math.abs(angle) > 1.0E-5D) {
                GlStateManager.translatef(0.5F, 0.5F, 0.5F);
                GlStateManager.rotatef(angle, 0.0F, 1.0F, 0.0F);
                GlStateManager.translatef(-0.5F, -0.5F, -0.5F);
            }

            this.applyLidRotation(te, partialTicks, modelChest);
            modelChest.renderAll();
            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

            if (destroyStage >= 0) {
                GlStateManager.matrixMode(5890);
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5888);
            }
        }
    }

    private void applyLidRotation(@Nonnull ChestBaseTileEntity te, float partialTicks, ChestModel model) {
        float lidAngle = ((IChestLid) te).getLidAngle(partialTicks);
        lidAngle = 1.0F - lidAngle;
        lidAngle = 1.0F - lidAngle * lidAngle * lidAngle;
        model.getLid().rotateAngleX = -(lidAngle * 1.5707964F);
    }*/
}
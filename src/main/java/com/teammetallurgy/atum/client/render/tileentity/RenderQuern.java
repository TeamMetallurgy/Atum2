package com.teammetallurgy.atum.client.render.tileentity;

import com.teammetallurgy.atum.blocks.machines.tileentity.TileEntityQuern;
import com.teammetallurgy.atum.client.model.ModelQuern;
import com.teammetallurgy.atum.client.render.ItemBakedBase;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = Side.CLIENT)
public class RenderQuern extends TileEntitySpecialRenderer<TileEntityQuern> {
    private static final ResourceLocation QUERN = new ResourceLocation(Constants.MOD_ID, "textures/blocks/quern.png");
    private static ItemBakedBase bakedBase;
    private static final ModelQuern modelQuern = new ModelQuern();

    @Override
    public void render(@Nonnull TileEntityQuern quern, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);

        int meta = quern.getBlockMetadata();
        float rotation = 0.0F;

        if (meta == 2) {
            rotation = 180.0F;
        }

        if (meta == 4) {
            rotation = 90.0F;
        }

        if (meta == 5) {
            rotation = -90.0F;
        }
        GlStateManager.rotate(rotation, 0.0F, 1.0F, 0.0F);

        if (destroyStage >= 0) {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 2.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        } else {
            this.bindTexture(QUERN);
        }

        modelQuern.renderAll();
        GlStateManager.depthMask(true);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();

        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        /*ModelResourceLocation modelLocation = new ModelResourceLocation(QUERN, "inventory");
        bakedBase = new ItemBakedBase(event.getModelRegistry().getObject(modelLocation));
        event.getModelRegistry().putObject(modelLocation, bakedBase);*/
    }
}
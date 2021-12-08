package com.teammetallurgy.atum.client.gui.block;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.inventory.container.block.GodforgeContainer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class GodforgeScreen extends AbstractContainerScreen<GodforgeContainer> {
    private static final ResourceLocation GODFORGE_GUI = new ResourceLocation(Atum.MOD_ID, "textures/gui/godforge.png");

    public GodforgeScreen(GodforgeContainer godforge, Inventory playerInventory, Component title) {
        super(godforge, playerInventory, title);
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@Nonnull PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindForSetup(GODFORGE_GUI);
        int width = (this.width - this.imageWidth) / 2;
        int height = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, width, height, 0, 0, this.imageWidth, this.imageHeight);

        if (this.menu.isBurning()) {
            int burnLeft = this.menu.getBurnLeftScaled();
            this.blit(matrixStack, width + 57, height + 49 - burnLeft, 176, 13 - burnLeft, 14, burnLeft + 1);
        }

        int l = this.menu.getCookProgressionScaled();
        this.blit(matrixStack, width + 80, height + 35, 176, 14, l + 1, 24);

        if (this.menu.fuelSlot.hasItem()) {
            this.renderEmptySlot(matrixStack, width + 55, height + 52);
        }
    }

    private void renderEmptySlot(@Nonnull PoseStack matrixStack, int x, int y) {
        this.blit(matrixStack, x, y, 7, 83, 18, 18);
    }
}
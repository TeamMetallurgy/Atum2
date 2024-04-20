package com.teammetallurgy.atum.client.gui.block;

import com.mojang.blaze3d.systems.RenderSystem;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.inventory.container.block.GodforgeContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class GodforgeScreen extends AbstractContainerScreen<GodforgeContainer> {
    private static final ResourceLocation GODFORGE_GUI = new ResourceLocation(Atum.MOD_ID, "textures/gui/godforge.png");

    public GodforgeScreen(GodforgeContainer godforge, Inventory playerInventory, Component title) {
        super(godforge, playerInventory, title);
    }

    @Override
    public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@Nonnull GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GODFORGE_GUI);
        int width = (this.width - this.imageWidth) / 2;
        int height = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(GODFORGE_GUI, width, height, 0, 0, this.imageWidth, this.imageHeight);

        if (this.menu.isBurning()) {
            int burnLeft = this.menu.getBurnLeftScaled();
            guiGraphics.blit(GODFORGE_GUI, width + 57, height + 49 - burnLeft, 176, 13 - burnLeft, 14, burnLeft + 1);
        }

        int l = this.menu.getCookProgressionScaled();
        guiGraphics.blit(GODFORGE_GUI, width + 80, height + 35, 176, 14, l + 1, 24);

        if (this.menu.fuelSlot.hasItem()) {
            this.renderEmptySlot(GODFORGE_GUI, guiGraphics, width + 55, height + 52);
        }
    }

    private void renderEmptySlot(ResourceLocation location, @Nonnull GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(location, x, y, 7, 83, 18, 18);
    }
}
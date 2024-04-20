package com.teammetallurgy.atum.client.gui.block;

import com.mojang.blaze3d.systems.RenderSystem;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.inventory.container.block.KilnContainer;
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
public class KilnScreen extends AbstractContainerScreen<KilnContainer> {
    public static final ResourceLocation KILN_GUI = new ResourceLocation(Atum.MOD_ID, "textures/gui/kiln.png");
    private final Inventory playerInventory;

    public KilnScreen(KilnContainer kiln, Inventory playerInv, Component title) {
        super(kiln, playerInv, title);
        this.playerInventory = playerInv;
        this.imageHeight = 192;
    }

    @Override
    public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Component s = this.title;
        guiGraphics.drawString(this.font, s, this.font.width(s) / 2, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventory.getDisplayName(), 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(@Nonnull GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, KILN_GUI);
        int width = (this.width - this.imageWidth) / 2;
        int height = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(KILN_GUI, width, height, 0, 0, this.imageWidth, this.imageHeight);

        if (this.menu.isBurning()) {
            int burn = this.menu.getBurnLeftScaled();
            guiGraphics.blit(KILN_GUI, width + 37, height + 32 + 12 - burn, 176, 12 - burn, 14, burn + 1);
        }

        int cookProgress = this.menu.getCookProgressionScaled();
        guiGraphics.blit(KILN_GUI, width + 78, height + 52, 176, 14, 19, cookProgress);
    }
}
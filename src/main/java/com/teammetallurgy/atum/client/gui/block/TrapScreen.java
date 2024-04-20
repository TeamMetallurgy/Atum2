package com.teammetallurgy.atum.client.gui.block;

import com.mojang.blaze3d.systems.RenderSystem;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.inventory.container.block.TrapContainer;
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
public class TrapScreen extends AbstractContainerScreen<TrapContainer> {
    private static final ResourceLocation TRAP_GUI = new ResourceLocation(Atum.MOD_ID, "textures/gui/trap.png");
    private final Inventory playerInventory;

    public TrapScreen(TrapContainer trap, Inventory playerInventory, Component title) {
        super(trap, playerInventory, title);
        this.playerInventory = playerInventory;
    }

    @Override
    public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 8, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventory.getDisplayName(), 8, this.imageHeight - 128, 4210752, false);
    }

    @Override
    protected void renderBg(@Nonnull GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TRAP_GUI);
        int width = (this.width - this.imageWidth) / 2;
        int height = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(TRAP_GUI, width, height, 0, 0, this.imageWidth, this.imageHeight);

        if (this.menu.isBurning()) {
            int burnLeft = this.menu.getBurnLeftScaled();
            guiGraphics.blit(TRAP_GUI, width + 80, height + 15 - burnLeft, 176, 12 - burnLeft, 14, burnLeft + 1);
        }
    }
}
package com.teammetallurgy.atum.client.gui.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.inventory.container.block.GodforgeContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class GodforgeScreen extends ContainerScreen<GodforgeContainer> {
    private static final ResourceLocation GODFORGE_GUI = new ResourceLocation(Atum.MOD_ID, "textures/gui/godforge.png");

    public GodforgeScreen(GodforgeContainer godforge, PlayerInventory playerInventory, ITextComponent title) {
        super(godforge, playerInventory, title);
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(GODFORGE_GUI);
        int width = (this.width - this.xSize) / 2;
        int height = (this.height - this.ySize) / 2;
        this.blit(matrixStack, width, height, 0, 0, this.xSize, this.ySize);

        if (this.container.isBurning()) {
            int burnLeft = this.container.getBurnLeftScaled();
            this.blit(matrixStack, width + 57, height + 49 - burnLeft, 176, 13 - burnLeft, 14, burnLeft + 1);
        }

        int l = this.container.getCookProgressionScaled();
        this.blit(matrixStack, width + 80, height + 35, 176, 14, l + 1, 24);

        if (this.container.fuelSlot.getHasStack()) {
            this.renderEmptySlot(matrixStack, width + 55, height + 52);
        }
    }

    private void renderEmptySlot(@Nonnull MatrixStack matrixStack, int x, int y) {
        this.blit(matrixStack, x, y, 7, 83, 18, 18);
    }
}
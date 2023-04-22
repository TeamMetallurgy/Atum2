package com.teammetallurgy.atum.client.gui.block;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.inventory.container.block.GodforgeContainer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
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
    public void render(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@Nonnull PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GODFORGE_GUI);
        int width = (this.width - this.imageWidth) / 2;
        int height = (this.height - this.imageHeight) / 2;
        this.blit(poseStack, width, height, 0, 0, this.imageWidth, this.imageHeight);

        if (this.menu.isBurning()) {
            int burnLeft = this.menu.getBurnLeftScaled();
            this.blit(poseStack, width + 57, height + 49 - burnLeft, 176, 13 - burnLeft, 14, burnLeft + 1);
        }

        int l = this.menu.getCookProgressionScaled();
        this.blit(poseStack, width + 80, height + 35, 176, 14, l + 1, 24);

        if (this.menu.fuelSlot.hasItem()) {
            this.renderEmptySlot(poseStack, width + 55, height + 52);
        }
    }

    private void renderEmptySlot(@Nonnull PoseStack poseStack, int x, int y) {
        this.blit(poseStack, x, y, 7, 83, 18, 18);
    }
}
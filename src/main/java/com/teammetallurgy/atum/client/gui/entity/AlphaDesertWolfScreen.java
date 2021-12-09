package com.teammetallurgy.atum.client.gui.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.inventory.container.entity.AlphaDesertWolfContainer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class AlphaDesertWolfScreen extends AbstractContainerScreen<AlphaDesertWolfContainer> {
    private static final ResourceLocation CAMEL_GUI_TEXTURE = new ResourceLocation(Atum.MOD_ID, "textures/gui/desert_wolf.png");
    private float mousePosx;
    private float mousePosY;

    public AlphaDesertWolfScreen(AlphaDesertWolfContainer container, Inventory playerInv, Component title) {
        super(container, playerInv, title);
    }

    @Override
    protected void renderLabels(@Nonnull PoseStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, this.title, 8, 6, 4210752);
        this.font.draw(matrixStack, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752);
    }

    @Override
    protected void renderBg(@Nonnull PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, CAMEL_GUI_TEXTURE);
        int width = (this.width - this.imageWidth) / 2;
        int height = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, width, height, 0, 0, this.imageWidth, this.imageHeight);

        if (this.menu.desertWolf != null) {
            if (this.menu.desertWolf.isAlpha()) {
                this.blit(matrixStack, width + 7, height + 35 - 18, 18, this.imageHeight, 18, 18); //Saddle
            }
            this.blit(matrixStack, width + 7, height + 35, 0, this.imageHeight, 18, 18); //Armor
            InventoryScreen.renderEntityInInventory(width + 51, height + 60, 17, (float) (width + 51) - this.mousePosx, (float) (height + 75 - 50) - this.mousePosY, this.menu.desertWolf);
        }
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        this.mousePosx = (float) mouseX;
        this.mousePosY = (float) mouseY;
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }
}
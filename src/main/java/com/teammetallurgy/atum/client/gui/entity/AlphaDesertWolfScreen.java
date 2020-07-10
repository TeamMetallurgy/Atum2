package com.teammetallurgy.atum.client.gui.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.inventory.container.entity.AlphaDesertWolfContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AlphaDesertWolfScreen extends ContainerScreen<AlphaDesertWolfContainer> {
    private static final ResourceLocation CAMEL_GUI_TEXTURE = new ResourceLocation(Atum.MOD_ID, "textures/gui/desert_wolf.png");
    private float mousePosx;
    private float mousePosY;

    public AlphaDesertWolfScreen(AlphaDesertWolfContainer container, PlayerInventory playerInv, ITextComponent title) {
        super(container, playerInv, title);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString(this.title.getFormattedText(), 8, 6, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(CAMEL_GUI_TEXTURE);
        int width = (this.width - this.xSize) / 2;
        int height = (this.height - this.ySize) / 2;
        this.blit(width, height, 0, 0, this.xSize, this.ySize);

        if (this.container.desertWolf != null) {
            if (this.container.desertWolf.isAlpha()) {
                this.blit(width + 7, height + 35 - 18, 18, this.ySize, 18, 18); //Saddle
            }
            this.blit(width + 7, height + 35, 0, this.ySize, 18, 18); //Armor
            InventoryScreen.drawEntityOnScreen(width + 51, height + 60, 17, (float) (width + 51) - this.mousePosx, (float) (height + 75 - 50) - this.mousePosY, this.container.desertWolf);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        this.mousePosx = (float) mouseX;
        this.mousePosY = (float) mouseY;
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
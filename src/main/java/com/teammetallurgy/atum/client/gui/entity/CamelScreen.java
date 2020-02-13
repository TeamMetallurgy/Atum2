package com.teammetallurgy.atum.client.gui.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import com.teammetallurgy.atum.inventory.container.entity.CamelContainer;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CamelScreen extends ContainerScreen<CamelContainer> {
    private static final ResourceLocation CAMEL_GUI_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/gui/camel.png");
    private final PlayerInventory playerInventory;
    private float mousePosX;
    private float mousePosY;

    public CamelScreen(CamelContainer container, PlayerInventory playerInv, ITextComponent title) {
        super(container, playerInv, title);
        this.playerInventory = playerInv;
        this.ySize = 236;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString(this.title.getFormattedText(), 8, 6, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(CAMEL_GUI_TEXTURE);
        int width = (this.width - this.xSize) / 2;
        int height = (this.height - this.ySize) / 2;
        this.blit(width, height, 0, 0, this.xSize, this.ySize);

        if (this.container.camel != null) {
            if (this.container.camel.hasLeftCrate()) {
                this.blit(width + 7, height + 85, this.xSize, 0, this.container.camel.getInventoryColumns() * 18, 54); //Left Crate
            }
            if (this.container.camel.hasRightCrate()) {
                this.blit(width + 97, height + 85, this.xSize, 0, this.container.camel.getInventoryColumns() * 18, 54); //Right Crate
            }
            InventoryScreen.drawEntityOnScreen(width + 88, height + 50, 17, (float) (width + 51) - this.mousePosX, (float) (height + 75 - 50) - this.mousePosY, this.container.camel);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        this.mousePosX = (float) mouseX;
        this.mousePosY = (float) mouseY;
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
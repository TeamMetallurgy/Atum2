package com.teammetallurgy.atum.client.gui.entity;

import com.teammetallurgy.atum.entity.animal.CamelEntity;
import com.teammetallurgy.atum.inventory.container.entity.ContainerCamel;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiCamel extends GuiContainer {
    private static final ResourceLocation CAMEL_GUI_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/gui/camel.png");
    private final IInventory playerInventory;
    private final IInventory camelInventory;
    private final CamelEntity camel;
    private float mousePosx;
    private float mousePosY;

    public GuiCamel(IInventory playerInv, IInventory camelInv, CamelEntity camel) {
        super(new ContainerCamel(playerInv, camelInv, camel, Minecraft.getInstance().player));
        this.playerInventory = playerInv;
        this.camelInventory = camelInv;
        this.camel = camel;
        this.allowUserInput = false;
        this.ySize = 236;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(this.camelInventory.getDisplayName().getUnformattedText(), 8, 6, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(CAMEL_GUI_TEXTURE);
        int width = (this.getWidth() - this.xSize) / 2;
        int height = (this.getHeight() - this.ySize) / 2;
        this.drawTexturedModalRect(width, height, 0, 0, this.xSize, this.ySize);

        if (this.camel != null) {
            if (this.camel.hasLeftCrate()) {
                this.drawTexturedModalRect(width + 7, height + 85, this.xSize, 0, this.camel.getInventoryColumns() * 18, 54); //Left Crate
            }
            if (this.camel.hasRightCrate()) {
                this.drawTexturedModalRect(width + 97, height + 85, this.xSize, 0, this.camel.getInventoryColumns() * 18, 54); //Right Crate
            }
            GuiInventory.drawEntityOnScreen(width + 88, height + 50, 17, (float) (width + 51) - this.mousePosx, (float) (height + 75 - 50) - this.mousePosY, this.camel);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.mousePosx = (float) mouseX;
        this.mousePosY = (float) mouseY;
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
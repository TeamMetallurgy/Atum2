package com.teammetallurgy.atum.client.gui.entity;

import com.teammetallurgy.atum.entity.animal.EntityCamel;
import com.teammetallurgy.atum.inventory.entity.ContainerCamel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCamel extends GuiContainer {
    private static final ResourceLocation CAMEL_GUI_TEXTURE = new ResourceLocation("textures/gui/container/horse.png");
    private final IInventory playerInventory;
    private final IInventory camelInventory;
    private final EntityCamel camel;
    private float mousePosx;
    private float mousePosY;

    public GuiCamel(IInventory playerInv, IInventory camelInv, EntityCamel camel) {
        super(new ContainerCamel(playerInv, camelInv, camel, Minecraft.getMinecraft().player));
        this.playerInventory = playerInv;
        this.camelInventory = camelInv;
        this.camel = camel;
        this.allowUserInput = false;
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
        int width = (this.width - this.xSize) / 2;
        int height = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(width, height, 0, 0, this.xSize, this.ySize);

        if (this.camel != null) {
            if (this.camel.hasCrate()) {
                this.drawTexturedModalRect(width + 79, height + 17, 0, this.ySize, this.camel.getInventoryColumns() * 18, 54);
            }
            this.drawTexturedModalRect(width + 7, height + 35 - 18, 18, this.ySize + 54, 18, 18); //Saddle
            this.drawTexturedModalRect(width + 7, height + 35, 0, this.ySize + 54, 18, 18); //Armor
            this.drawTexturedModalRect(width + 7, height + 35 + 18, 36, this.ySize + 54, 18, 18); //Carpet
            GuiInventory.drawEntityOnScreen(width + 51, height + 60, 17, (float) (width + 51) - this.mousePosx, (float) (height + 75 - 50) - this.mousePosY, this.camel);
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
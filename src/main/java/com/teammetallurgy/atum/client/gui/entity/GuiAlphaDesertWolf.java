package com.teammetallurgy.atum.client.gui.entity;

import com.teammetallurgy.atum.entity.animal.EntityDesertWolf;
import com.teammetallurgy.atum.inventory.container.entity.ContainerAlphaDesertWolf;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAlphaDesertWolf extends GuiContainer {
    private static final ResourceLocation CAMEL_GUI_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/gui/desert_wolf.png");
    private final IInventory playerInventory;
    private final IInventory wolfInventory;
    private final EntityDesertWolf desertWolf;
    private float mousePosx;
    private float mousePosY;

    public GuiAlphaDesertWolf(IInventory playerInv, IInventory wolfInventory, EntityDesertWolf desertWolf) {
        super(new ContainerAlphaDesertWolf(playerInv, wolfInventory, desertWolf, Minecraft.getMinecraft().player));
        this.playerInventory = playerInv;
        this.wolfInventory = wolfInventory;
        this.desertWolf = desertWolf;
        this.allowUserInput = false;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(this.wolfInventory.getDisplayName().getUnformattedText(), 8, 6, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(CAMEL_GUI_TEXTURE);
        int width = (this.width - this.xSize) / 2;
        int height = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(width, height, 0, 0, this.xSize, this.ySize);

        if (this.desertWolf != null) {
            if (this.desertWolf.isAlpha()) {
                this.drawTexturedModalRect(width + 7, height + 35 - 18, 18, this.ySize, 18, 18); //Saddle
            }
            this.drawTexturedModalRect(width + 7, height + 35, 0, this.ySize, 18, 18); //Armor
            GuiInventory.drawEntityOnScreen(width + 51, height + 60, 17, (float) (width + 51) - this.mousePosx, (float) (height + 75 - 50) - this.mousePosY, this.desertWolf);
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
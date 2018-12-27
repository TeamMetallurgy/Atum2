package com.teammetallurgy.atum.client.gui.block;

import com.teammetallurgy.atum.blocks.trap.tileentity.TileEntityTrap;
import com.teammetallurgy.atum.inventory.block.ContainerTrap;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiTrap extends GuiContainer {
    private static final ResourceLocation TRAP_GUI = new ResourceLocation(Constants.MOD_ID, "textures/gui/trap.png");
    private final InventoryPlayer playerInventory;
    private final IInventory trapInventory;

    public GuiTrap(InventoryPlayer playerInventory, IInventory trapInventory) {
        super(new ContainerTrap(playerInventory, trapInventory));
        this.playerInventory = playerInventory;
        this.trapInventory = trapInventory;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = this.trapInventory.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(name, 8, 6, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 128, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TRAP_GUI);
        int width = (this.width - this.xSize) / 2;
        int height = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(width, height, 0, 0, this.xSize, this.ySize);

        if (TileEntityTrap.isBurning(this.trapInventory)) {
            int burnLeft = this.getBurnLeftScaled(13);
            this.drawTexturedModalRect(width + 80, height + 15 - burnLeft, 176, 12 - burnLeft, 14, burnLeft + 1);
        }
    }

    private int getBurnLeftScaled(int pixels) {
        int currentItemBurnTime = this.trapInventory.getField(1);
        if (currentItemBurnTime == 0) {
            currentItemBurnTime = 200;
        }
        return this.trapInventory.getField(0) * pixels / currentItemBurnTime;
    }
}
package com.teammetallurgy.atum.client.gui.block;

import com.teammetallurgy.atum.blocks.machines.tileentity.TileEntityKiln;
import com.teammetallurgy.atum.inventory.container.block.ContainerKiln;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiKiln extends GuiContainer {
    public static final ResourceLocation KILN_GUI = new ResourceLocation(Constants.MOD_ID, "textures/gui/kiln.png");
    private final InventoryPlayer playerInventory;
    private final IInventory kiln;

    public GuiKiln(InventoryPlayer playerInv, IInventory kiln) {
        super(new ContainerKiln(playerInv, kiln));
        this.playerInventory = playerInv;
        this.kiln = kiln;
        this.ySize = 192;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.kiln.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(s, this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(KILN_GUI);
        int width = (this.width - this.xSize) / 2;
        int height = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(width, height, 0, 0, this.xSize, this.ySize);

        if (TileEntityKiln.isBurning(this.kiln)) {
            int burn = this.getBurnLeftScaled(13);
            this.drawTexturedModalRect(width + 37, height + 32 + 12 - burn, 176, 12 - burn, 14, burn + 1);
        }

        int cookProgress = this.getCookProgressScaled(8);
        this.drawTexturedModalRect(width + 78, height + 52, 176, 14, 19, cookProgress);
    }

    private int getCookProgressScaled(int pixels) {
        int i = this.kiln.getField(2);
        int j = this.kiln.getField(3);
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }

    private int getBurnLeftScaled(int pixels) {
        int field = this.kiln.getField(1);
        if (field == 0) {
            field = 200;
        }
        return this.kiln.getField(0) * pixels / field;
    }
}
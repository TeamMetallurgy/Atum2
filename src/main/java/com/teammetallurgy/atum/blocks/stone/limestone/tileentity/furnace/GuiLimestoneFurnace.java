package com.teammetallurgy.atum.blocks.stone.limestone.tileentity.furnace;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiLimestoneFurnace extends ContainerScreen {
    private final PlayerInventory playerInventory;
    private IInventory tileLimestoneFurnace;

    public GuiLimestoneFurnace(PlayerInventory playerInv, IInventory furnaceInv) {
        super(new ContainerLimestoneFurnace(playerInv, furnaceInv));
        this.playerInventory = playerInv;
        this.tileLimestoneFurnace = furnaceInv;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.tileLimestoneFurnace.getDisplayName().getUnformattedText();
        this.font.drawString(s, this.xSize / 2 - this.font.getStringWidth(s) / 2, 6, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/furnace.png"));
        int i = (this.getWidth() - this.xSize) / 2;
        int j = (this.getHeight() - this.ySize) / 2;
        this.blit(i, j, 0, 0, this.xSize, this.ySize);

        if (TileEntityLimestoneFurnace.isBurning(this.tileLimestoneFurnace)) {
            int k = this.getBurnLeftScaled(13);
            this.blit(i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
        }
        int l = this.getCookProgressScaled(24);
        this.blit(i + 79, j + 34, 176, 14, l + 1, 16);
    }

    private int getCookProgressScaled(int pixels) {
        int i = this.tileLimestoneFurnace.getField(2);
        int j = this.tileLimestoneFurnace.getField(3);
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }

    private int getBurnLeftScaled(int pixels) {
        int i = this.tileLimestoneFurnace.getField(1);
        if (i == 0) {
            i = 200;
        }
        return this.tileLimestoneFurnace.getField(0) * pixels / i;
    }
}
package com.teammetallurgy.atum.client.gui.block;

import com.mojang.blaze3d.platform.GlStateManager;
import com.teammetallurgy.atum.blocks.trap.tileentity.TrapTileEntity;
import com.teammetallurgy.atum.inventory.container.block.ContainerTrap;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TrapScreen extends ContainerScreen {
    private static final ResourceLocation TRAP_GUI = new ResourceLocation(Constants.MOD_ID, "textures/gui/trap.png");
    private final PlayerInventory playerInventory;
    private final IInventory trapInventory;

    public TrapScreen(PlayerInventory playerInventory, IInventory trapInventory) {
        super(new ContainerTrap(playerInventory, trapInventory));
        this.playerInventory = playerInventory;
        this.trapInventory = trapInventory;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = this.trapInventory.getDisplayName().getUnformattedText();
        this.font.drawString(name, 8, 6, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 128, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TRAP_GUI);
        int width = (this.getWidth() - this.xSize) / 2;
        int height = (this.getHeight() - this.ySize) / 2;
        this.blit(width, height, 0, 0, this.xSize, this.ySize);

        if (TrapTileEntity.isBurning(this.trapInventory)) {
            int burnLeft = this.getBurnLeftScaled(13);
            this.blit(width + 80, height + 15 - burnLeft, 176, 12 - burnLeft, 14, burnLeft + 1);
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
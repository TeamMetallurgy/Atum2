package com.teammetallurgy.atum.client.gui.block;

import com.mojang.blaze3d.platform.GlStateManager;
import com.teammetallurgy.atum.blocks.machines.tileentity.KilnTileEntity;
import com.teammetallurgy.atum.inventory.container.block.ContainerKiln;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class KilnScreen extends ContainerScreen<ContainerKiln> {
    public static final ResourceLocation KILN_GUI = new ResourceLocation(Constants.MOD_ID, "textures/gui/kiln.png");
    private final PlayerInventory playerInventory;

    public KilnScreen(ContainerKiln kiln, PlayerInventory playerInv, ITextComponent title) {
        super(kiln, playerInv, title);
        this.playerInventory = playerInv;
        this.ySize = 192;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.title.getFormattedText();
        this.font.drawString(s, this.font.getStringWidth(s) / 2, 6, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(KILN_GUI);
        int width = (this.width - this.xSize) / 2;
        int height = (this.height - this.ySize) / 2;
        this.blit(width, height, 0, 0, this.xSize, this.ySize);

        if (KilnTileEntity.isBurning(this.container.kiln)) {
            int burn = this.getBurnLeftScaled(13);
            this.blit(width + 37, height + 32 + 12 - burn, 176, 12 - burn, 14, burn + 1);
        }

        int cookProgress = this.getCookProgressScaled(8);
        this.blit(width + 78, height + 52, 176, 14, 19, cookProgress);
    }

    private int getCookProgressScaled(int pixels) {
        int i = this.container.kiln.getField(2);
        int j = this.container.kiln.getField(3);
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }

    private int getBurnLeftScaled(int pixels) {
        int field = this.kiln.getField(1);
        if (field == 0) {
            field = 200;
        }
        return this.container.kiln.getField(0) * pixels / field;
    }
}
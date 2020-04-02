package com.teammetallurgy.atum.client.gui.block;

import com.mojang.blaze3d.systems.RenderSystem;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.inventory.container.block.TrapContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TrapScreen extends ContainerScreen<TrapContainer> {
    private static final ResourceLocation TRAP_GUI = new ResourceLocation(Atum.MOD_ID, "textures/gui/trap.png");
    private final PlayerInventory playerInventory;

    public TrapScreen(TrapContainer trap, PlayerInventory playerInventory, ITextComponent title) {
        super(trap, playerInventory, title);
        this.playerInventory = playerInventory;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = this.title.getFormattedText();
        this.font.drawString(name, 8, 6, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8, this.ySize - 128, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(TRAP_GUI);
        int width = (this.width - this.xSize) / 2;
        int height = (this.height - this.ySize) / 2;
        this.blit(width, height, 0, 0, this.xSize, this.ySize);

        if (this.container.isBurning()) {
            int burnLeft = this.container.getBurnLeftScaled();
            this.blit(width + 80, height + 15 - burnLeft, 176, 12 - burnLeft, 14, burnLeft + 1);
        }
    }
}
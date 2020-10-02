package com.teammetallurgy.atum.client.gui.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.inventory.container.block.KilnContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class KilnScreen extends ContainerScreen<KilnContainer> {
    public static final ResourceLocation KILN_GUI = new ResourceLocation(Atum.MOD_ID, "textures/gui/kiln.png");
    private final PlayerInventory playerInventory;

    public KilnScreen(KilnContainer kiln, PlayerInventory playerInv, ITextComponent title) {
        super(kiln, playerInv, title);
        this.playerInventory = playerInv;
        this.ySize = 192;
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY) {
        ITextComponent s = this.title;
        this.font.func_243248_b(matrixStack, s, (float) this.font.getStringPropertyWidth(s) / 2, 6, 4210752);
        this.font.func_243248_b(matrixStack, this.playerInventory.getDisplayName(), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(KILN_GUI);
        int width = (this.width - this.xSize) / 2;
        int height = (this.height - this.ySize) / 2;
        this.blit(matrixStack, width, height, 0, 0, this.xSize, this.ySize);

        if (this.container.isBurning()) {
            int burn = this.container.getBurnLeftScaled();
            this.blit(matrixStack, width + 37, height + 32 + 12 - burn, 176, 12 - burn, 14, burn + 1);
        }

        int cookProgress = this.container.getCookProgressionScaled();
        this.blit(matrixStack, width + 78, height + 52, 176, 14, 19, cookProgress);
    }
}
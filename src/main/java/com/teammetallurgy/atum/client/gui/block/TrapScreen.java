package com.teammetallurgy.atum.client.gui.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.inventory.container.block.TrapContainer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class TrapScreen extends AbstractContainerScreen<TrapContainer> {
    private static final ResourceLocation TRAP_GUI = new ResourceLocation(Atum.MOD_ID, "textures/gui/trap.png");
    private final Inventory playerInventory;

    public TrapScreen(TrapContainer trap, Inventory playerInventory, Component title) {
        super(trap, playerInventory, title);
        this.playerInventory = playerInventory;
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(@Nonnull PoseStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, this.title, 8, 6, 4210752);
        this.font.draw(matrixStack, this.playerInventory.getDisplayName(), 8, this.imageHeight - 128, 4210752);
    }

    @Override
    protected void renderBg(@Nonnull PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bind(TRAP_GUI);
        int width = (this.width - this.imageWidth) / 2;
        int height = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, width, height, 0, 0, this.imageWidth, this.imageHeight);

        if (this.menu.isBurning()) {
            int burnLeft = this.menu.getBurnLeftScaled();
            this.blit(matrixStack, width + 80, height + 15 - burnLeft, 176, 12 - burnLeft, 14, burnLeft + 1);
        }
    }
}
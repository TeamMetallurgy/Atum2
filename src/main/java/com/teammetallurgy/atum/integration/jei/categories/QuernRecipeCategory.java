package com.teammetallurgy.atum.integration.jei.categories;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.recipes.QuernRecipe;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.integration.jei.JEIIntegration;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.awt.*;

public class QuernRecipeCategory implements IRecipeCategory<QuernRecipe> {
    private static final ResourceLocation QUERN_GUI = new ResourceLocation(Atum.MOD_ID, "textures/gui/quern.png");
    private final IDrawableStatic background;
    private final IDrawable icon;

    public QuernRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(QUERN_GUI, 0, -6, 82, 32);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(AtumBlocks.QUERN.get()));
    }

    @Override
    @Nonnull
    public RecipeType<QuernRecipe> getRecipeType() {
        return JEIIntegration.QUERN;
    }

    @Override
    @Nonnull
    public Component getTitle() {
        return Component.translatable(Atum.MOD_ID + "." + JEIIntegration.QUERN.getUid().getPath());
    }

    @Override
    @Nonnull
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    @Nonnull
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull QuernRecipe recipe, @Nonnull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 11).addIngredients(recipe.getIngredients().get(0)); // Input

        ClientLevel clientLevel = Minecraft.getInstance().level;
        if (clientLevel != null) {
            builder.addSlot(RecipeIngredientRole.INPUT, 61, 11).addItemStack(recipe.getResultItem(clientLevel.registryAccess())).addTooltipCallback((recipeSlotView, tooltip) -> { // Output
                boolean showAdvanced = Minecraft.getInstance().options.advancedItemTooltips || Screen.hasShiftDown();
                if (showAdvanced) {
                    tooltip.add(Component.translatable("jei.tooltip.recipe.id", recipe.getId()).withStyle(ChatFormatting.DARK_GRAY));
                }
            });
        }
    }

    @Override
    public void draw(QuernRecipe recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull PoseStack poseStack, double mouseX, double mouseY) {
        Minecraft.getInstance().font.draw(poseStack, Component.translatable("gui.atum.rotations", recipe.getRotations()), 32, 0, Color.gray.getRGB());
        this.icon.draw(poseStack, 29, 8);
    }
}
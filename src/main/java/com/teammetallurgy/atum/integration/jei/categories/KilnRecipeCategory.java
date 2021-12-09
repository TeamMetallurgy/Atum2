package com.teammetallurgy.atum.integration.jei.categories;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.recipes.KilnRecipe;
import com.teammetallurgy.atum.client.gui.block.KilnScreen;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.integration.jei.JEIIntegration;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.awt.*;

public class KilnRecipeCategory implements IRecipeCategory<KilnRecipe> {
    private final IDrawableStatic background;
    private final IDrawable icon;
    private final IDrawableAnimated animatedFlame;
    private final IDrawableAnimated arrow;

    public KilnRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(KilnScreen.KILN_GUI, 35, 14, 71, 84);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(AtumBlocks.KILN));
        IDrawableStatic staticFlame = guiHelper.createDrawable(KilnScreen.KILN_GUI, 176, 0, 14, 14);
        animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);
        arrow = guiHelper.drawableBuilder(KilnScreen.KILN_GUI, 176, 14, 19, 8).buildAnimated(200, IDrawableAnimated.StartDirection.TOP, false);
    }

    @Override
    @Nonnull
    public ResourceLocation getUid() {
        return JEIIntegration.KILN;
    }

    @Override
    @Nonnull
    public Class<? extends KilnRecipe> getRecipeClass() {
        return KilnRecipe.class;
    }

    @Override
    @Nonnull
    public Component getTitle() {
        return new TranslatableComponent(Atum.MOD_ID + "." + getUid().getPath());
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
    public void setIngredients(@Nonnull KilnRecipe recipe, @Nonnull IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull KilnRecipe recipe, @Nonnull IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(0, true, 35, 0);
        guiItemStacks.init(5, false, 35, 48);
        guiItemStacks.set(ingredients);

        guiItemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
            if (slotIndex >= 5) {
                boolean showAdvanced = Minecraft.getInstance().options.advancedItemTooltips || Screen.hasShiftDown();
                if (showAdvanced) {
                    tooltip.add(new TranslatableComponent("jei.tooltip.recipe.id", recipe.getId()).withStyle(ChatFormatting.DARK_GRAY));
                }
            }
        });
    }

    @Override
    public void draw(KilnRecipe recipe, @Nonnull PoseStack matrixStack, double mouseX, double mouseY) {
        animatedFlame.draw(matrixStack, 1, 17);
        arrow.draw(matrixStack, 43, 38);

        float experience = recipe.getExperience();
        if (experience > 0) {
            TranslatableComponent experienceString = new TranslatableComponent("gui.jei.category.smelting.experience", experience);
            Minecraft.getInstance().font.draw(matrixStack, experienceString, -1, this.background.getHeight() - 13, Color.gray.getRGB());
        }
    }
}
package com.teammetallurgy.atum.integration.jei.categories;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.recipes.SpinningWheelRecipe;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.integration.jei.JEIIntegration;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import java.awt.*;

public class SpinningWheelRecipeCategory implements IRecipeCategory<SpinningWheelRecipe> {
    public static final ResourceLocation SPINNING_WHEEL_GUI = new ResourceLocation(Atum.MOD_ID, "textures/gui/spinning_wheel.png");
    private final IDrawableStatic background;
    private final IDrawable icon;

    public SpinningWheelRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(SPINNING_WHEEL_GUI, 0, -6, 68, 32);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(AtumBlocks.SPINNING_WHEEL));
    }

    @Override
    @Nonnull
    public ResourceLocation getUid() {
        return JEIIntegration.SPINNING_WHEEL;
    }

    @Override
    @Nonnull
    public Class<? extends SpinningWheelRecipe> getRecipeClass() {
        return SpinningWheelRecipe.class;
    }

    @Override
    @Nonnull
    public String getTitle() {
        return new TranslationTextComponent(Atum.MOD_ID + "." + getUid().getPath()).getFormattedText();
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
    public void setIngredients(@Nonnull SpinningWheelRecipe recipe, @Nonnull IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull SpinningWheelRecipe recipe, @Nonnull IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(0, true, 0, 10);
        guiItemStacks.init(1, false, 46, 10);
        guiItemStacks.set(ingredients);

        guiItemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
            if (slotIndex == 1) {
                boolean showAdvanced = Minecraft.getInstance().gameSettings.advancedItemTooltips || Screen.hasShiftDown();
                if (showAdvanced) {
                    tooltip.add(new TranslationTextComponent("jei.tooltip.recipe.id", recipe.getId()).applyTextStyles(TextFormatting.DARK_GRAY).getFormattedText());
                }
            }
        });
    }

    @Override
    public void draw(SpinningWheelRecipe recipe, double mouseX, double mouseY) {
        Minecraft mc = Minecraft.getInstance();
        mc.fontRenderer.drawString(new TranslationTextComponent("gui.atum.rotations", recipe.getRotations()).getFormattedText(), 25, 0, Color.gray.getRGB());
    }
}
package com.teammetallurgy.atum.integration.jei.spinningwheel;

import com.teammetallurgy.atum.integration.jei.JEIIntegration;
import com.teammetallurgy.atum.utils.AtumUtils;
import com.teammetallurgy.atum.utils.Constants;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;

public class SpinningWheelRecipeCategory implements IRecipeCategory<SpinningWheelRecipeWrapper> {
    public static final ResourceLocation SPINNING_WHEEL_GUI = new ResourceLocation(Constants.MOD_ID, "textures/gui/spinning_wheel.png");
    private final IDrawableStatic background;

    public SpinningWheelRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(SPINNING_WHEEL_GUI, 0, -6, 68, 32);
    }

    @Override
    @Nonnull
    public String getUid() {
        return JEIIntegration.SPINNING_WHEEL;
    }

    @Override
    @Nonnull
    public String getTitle() {
        return AtumUtils.format(getUid());
    }

    @Override
    @Nonnull
    public String getModName() {
        return Constants.MOD_NAME;
    }

    @Override
    @Nonnull
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull SpinningWheelRecipeWrapper recipeWrapper, @Nonnull IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(0, true, 0, 10);
        guiItemStacks.init(1, false, 46, 10);
        guiItemStacks.set(ingredients);

        guiItemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
            if (slotIndex == 1) {
                boolean showAdvanced = Minecraft.getMinecraft().gameSettings.advancedItemTooltips || GuiScreen.isShiftKeyDown();
                if (showAdvanced) {
                    tooltip.add(TextFormatting.DARK_GRAY + AtumUtils.format("jei.tooltip.recipe.id", recipeWrapper.getId()));
                }
            }
        });
    }
}
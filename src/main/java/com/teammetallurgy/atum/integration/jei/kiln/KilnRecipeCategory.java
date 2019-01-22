package com.teammetallurgy.atum.integration.jei.kiln;

import com.teammetallurgy.atum.client.gui.block.GuiKiln;
import com.teammetallurgy.atum.integration.jei.JEIIntegration;
import com.teammetallurgy.atum.utils.AtumUtils;
import com.teammetallurgy.atum.utils.Constants;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;

public class KilnRecipeCategory implements IRecipeCategory<KilnRecipeWrapper> {
    private final IDrawableStatic background;
    private final IDrawableAnimated animatedFlame;
    private final IDrawableAnimated arrow;

    public KilnRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(GuiKiln.KILN_GUI, 35, 14, 71, 84);
        IDrawableStatic staticFlame = guiHelper.createDrawable(GuiKiln.KILN_GUI, 176, 0, 14, 14);
        animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);
        arrow = guiHelper.drawableBuilder(GuiKiln.KILN_GUI, 176, 14, 19, 8).buildAnimated(200, IDrawableAnimated.StartDirection.TOP, false);
    }

    @Override
    @Nonnull
    public String getUid() {
        return JEIIntegration.KILN;
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
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull KilnRecipeWrapper recipeWrapper, @Nonnull IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(0, true, 35, 0);
        guiItemStacks.init(5, false, 35, 48);
        guiItemStacks.set(ingredients);

        guiItemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
            if (slotIndex >= 5) {
                boolean showAdvanced = Minecraft.getMinecraft().gameSettings.advancedItemTooltips || GuiScreen.isShiftKeyDown();
                if (showAdvanced) {
                    tooltip.add(TextFormatting.DARK_GRAY + AtumUtils.format("jei.tooltip.recipe.id", recipeWrapper.getId()));
                }
            }
        });
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        animatedFlame.draw(minecraft, 1, 17);
        arrow.draw(minecraft, 43, 38);
    }
}
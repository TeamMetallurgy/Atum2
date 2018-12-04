package com.teammetallurgy.atum.integration.jei.quern;

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
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class QuernRecipeCategory implements IRecipeCategory<QuernRecipeWrapper> {
    private static final ResourceLocation QUERN_GUI = new ResourceLocation(Constants.MOD_ID, "textures/gui/quern.png");
    private final IDrawableStatic background;

    public QuernRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(QUERN_GUI, 0, -6, 82, 32);
    }

    @Override
    @Nonnull
    public String getUid() {
        return JEIIntegration.QUERN;
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
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull QuernRecipeWrapper recipeWrapper, @Nonnull IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(0, true, 0, 10);
        guiItemStacks.init(1, false, 60, 10);
        guiItemStacks.set(ingredients);
    }
}
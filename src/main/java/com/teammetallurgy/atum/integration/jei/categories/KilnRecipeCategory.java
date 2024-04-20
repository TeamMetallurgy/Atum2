package com.teammetallurgy.atum.integration.jei.categories;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.recipes.KilnRecipe;
import com.teammetallurgy.atum.client.gui.block.KilnScreen;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.integration.jei.JEIIntegration;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
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
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(AtumBlocks.KILN.get()));
        IDrawableStatic staticFlame = guiHelper.createDrawable(KilnScreen.KILN_GUI, 176, 0, 14, 14);
        animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);
        arrow = guiHelper.drawableBuilder(KilnScreen.KILN_GUI, 176, 14, 19, 8).buildAnimated(200, IDrawableAnimated.StartDirection.TOP, false);
    }

    @Override
    @Nonnull
    public RecipeType<KilnRecipe> getRecipeType() {
        return JEIIntegration.KILN;
    }

    @Override
    @Nonnull
    public Component getTitle() {
        return Component.translatable(Atum.MOD_ID + "." + JEIIntegration.KILN.getUid().getPath());
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
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull KilnRecipe recipe, @Nonnull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 36, 1).addIngredients(recipe.getIngredients().get(0)); // Input

        ClientLevel clientLevel = Minecraft.getInstance().level;
        if (clientLevel != null) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 36, 49).addItemStack(recipe.getResultItem(clientLevel.registryAccess())); //Output
        }
    }


    @Override
    public void draw(KilnRecipe recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        animatedFlame.draw(guiGraphics, 1, 17);
        arrow.draw(guiGraphics, 43, 38);

        float experience = recipe.getExperience();
        if (experience > 0) {
            Component experienceString = Component.translatable("gui.jei.category.smelting.experience", experience);
            Font font = Minecraft.getInstance().font;
            guiGraphics.drawString(font, experienceString, -1, this.background.getHeight() - 13, Color.gray.getRGB());
        }
    }
}
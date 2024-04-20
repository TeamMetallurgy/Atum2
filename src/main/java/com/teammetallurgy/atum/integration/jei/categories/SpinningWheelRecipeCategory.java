package com.teammetallurgy.atum.integration.jei.categories;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.recipes.SpinningWheelRecipe;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.awt.*;

public class SpinningWheelRecipeCategory implements IRecipeCategory<SpinningWheelRecipe> {
    public static final ResourceLocation SPINNING_WHEEL_GUI = new ResourceLocation(Atum.MOD_ID, "textures/gui/spinning_wheel.png");
    private final IDrawableStatic background;
    private final IDrawable icon;

    public SpinningWheelRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(SPINNING_WHEEL_GUI, 0, -6, 68, 32);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(AtumBlocks.SPINNING_WHEEL.get()));
    }

    @Override
    @Nonnull
    public RecipeType<SpinningWheelRecipe> getRecipeType() {
        return JEIIntegration.SPINNING_WHEEL;
    }

    @Override
    @Nonnull
    public Component getTitle() {
        return Component.translatable(Atum.MOD_ID + "." + JEIIntegration.SPINNING_WHEEL.getUid().getPath());
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
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull SpinningWheelRecipe recipe, @Nonnull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 11).addIngredients(recipe.getIngredients().get(0)); // Input

        ClientLevel clientLevel = Minecraft.getInstance().level;
        if (clientLevel != null) {
            builder.addSlot(RecipeIngredientRole.INPUT, 47, 11).addItemStack(recipe.getResultItem(clientLevel.registryAccess())); // Output
        }
    }

    @Override
    public void draw(SpinningWheelRecipe recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;
        guiGraphics.drawString(font, Component.translatable("gui.atum.rotations", recipe.getRotations()), 25, 0, Color.gray.getRGB());
    }
}
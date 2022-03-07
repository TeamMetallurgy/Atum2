package com.teammetallurgy.atum.integration.jei.categories;

import com.mojang.blaze3d.vertex.PoseStack;
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
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(AtumBlocks.SPINNING_WHEEL.get()));
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
    public void setIngredients(@Nonnull SpinningWheelRecipe recipe, @Nonnull IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull SpinningWheelRecipe recipe, @Nonnull IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(0, true, 0, 10);
        guiItemStacks.init(1, false, 46, 10);
        guiItemStacks.set(ingredients);

        guiItemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
            if (slotIndex == 1) {
                boolean showAdvanced = Minecraft.getInstance().options.advancedItemTooltips || Screen.hasShiftDown();
                if (showAdvanced) {
                    tooltip.add(new TranslatableComponent("jei.tooltip.recipe.id", recipe.getId()).withStyle(ChatFormatting.DARK_GRAY));
                }
            }
        });
    }

    @Override
    public void draw(SpinningWheelRecipe recipe, @Nonnull PoseStack matrixStack, double mouseX, double mouseY) {
        Minecraft mc = Minecraft.getInstance();
        mc.font.draw(matrixStack, new TranslatableComponent("gui.atum.rotations", recipe.getRotations()), 25, 0, Color.gray.getRGB());
    }
}
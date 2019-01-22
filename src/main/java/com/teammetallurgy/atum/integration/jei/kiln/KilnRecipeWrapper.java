package com.teammetallurgy.atum.integration.jei.kiln;

import com.teammetallurgy.atum.utils.AtumUtils;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KilnRecipeWrapper implements IRecipeWrapper {
    private final ResourceLocation id;
    private final List<List<ItemStack>> inputs;
    private final ItemStack output;
    private final float experience;

    public KilnRecipeWrapper(ResourceLocation id, NonNullList<ItemStack> input, ItemStack output, float experience) {
        List<ItemStack> inputList = new ArrayList<>(input);
        this.id = id;
        this.inputs = Collections.singletonList(inputList);
        this.output = output;
        this.experience = experience;
    }

    public ResourceLocation getId() {
        return id;
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, this.inputs);
        ingredients.setOutput(VanillaTypes.ITEM, this.output);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        if (this.experience > 0) {
            String experienceString = AtumUtils.format("gui.jei.category.smelting.experience", this.experience);
            minecraft.fontRenderer.drawString(experienceString, -1, recipeHeight - 13, Color.gray.getRGB());
        }
    }
}
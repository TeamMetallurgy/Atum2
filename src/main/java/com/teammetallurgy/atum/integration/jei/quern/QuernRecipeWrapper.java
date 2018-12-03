package com.teammetallurgy.atum.integration.jei.quern;

import com.google.common.base.Preconditions;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.utils.AtumUtils;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuernRecipeWrapper implements IRecipeWrapper {
    private final IDrawable quern;
    private final List<List<ItemStack>> inputs;
    private final ItemStack output;
    private final String rotations;

    public QuernRecipeWrapper(IGuiHelper guiHelper, NonNullList<ItemStack> input, ItemStack output, int quernRotations) {
        Preconditions.checkArgument(quernRotations > 0, "quern rotations must be greater than 0");
        List<ItemStack> inputList = new ArrayList<>(input);
        this.inputs = Collections.singletonList(inputList);
        this.output = output;
        this.rotations = AtumUtils.format("gui.atum.category.quern.rotations", quernRotations);
        this.quern = guiHelper.createDrawableIngredient(new ItemStack(AtumBlocks.QUERN));
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, this.inputs);
        ingredients.setOutput(VanillaTypes.ITEM, this.output);
    }

    @Override
    public void drawInfo(Minecraft mc, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        mc.fontRenderer.drawString(rotations, 32, 0, Color.gray.getRGB());
        quern.draw(mc, 29, 8);
    }
}
package com.teammetallurgy.atum.integration.jei.spinningwheel;

import com.google.common.base.Preconditions;
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

public class SpinningWheelRecipeWrapper implements IRecipeWrapper {
    private final ResourceLocation id;
    private final List<List<ItemStack>> inputs;
    private final ItemStack output;
    private final String rotations;

    public SpinningWheelRecipeWrapper(ResourceLocation id, NonNullList<ItemStack> input, @Nonnull ItemStack output, int wheelRotations) {
        Preconditions.checkArgument(wheelRotations > 0, "wheel rotations must be greater than 0");
        List<ItemStack> inputList = new ArrayList<>(input);
        this.id = id;
        this.inputs = Collections.singletonList(inputList);
        this.output = output;
        this.rotations = AtumUtils.format("gui.atum.rotations", wheelRotations);
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
    public void drawInfo(Minecraft mc, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        mc.fontRenderer.drawString(rotations, 25, 0, Color.gray.getRGB());
        mc.fontRenderer.drawString(AtumUtils.format("gui.atum.rotations", 3), 4, 0, Color.gray.getRGB());
    }
}
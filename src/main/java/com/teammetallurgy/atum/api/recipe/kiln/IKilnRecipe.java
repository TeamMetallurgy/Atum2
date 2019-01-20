package com.teammetallurgy.atum.api.recipe.kiln;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;

public interface IKilnRecipe extends IForgeRegistryEntry<IKilnRecipe> {

    /**
     * @return The list of input stacks, that should be processed
     */
    NonNullList<ItemStack> getInput();

    /**
     * @param stack The stack input to check validity of
     * @return Whether the input is valid or not
     */
    boolean isValidInput(@Nonnull ItemStack stack);

    /**
     * @return The output stack after the input have been processed
     */
    @Nonnull
    ItemStack getOutput();

    /**
     * @return The amount of experience received after cooking the recipe
     */
    float getExperience();
}
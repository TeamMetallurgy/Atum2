package com.teammetallurgy.atum.api.recipe.quern;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;

public interface IQuernRecipe extends IForgeRegistryEntry<IQuernRecipe> {

    /**
     * @return The list of input stacks, that should be querned
     */
    NonNullList<ItemStack> getInput();

    /**
     * @param stack The stack input to check validity of
     * @return Whether the input is valid or not
     */
    boolean isValidInput(@Nonnull ItemStack stack);

    /**
     * @return The main output after querning the input
     */
    ItemStack getOutput();

    /**
     * @return The amount of rotations the quern have to rotate, to quern something.
     */
    int getRotations();
}
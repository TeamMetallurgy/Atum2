package com.teammetallurgy.atum.api.recipe.spinningwheel;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;

public interface ISpinningWheelRecipe extends IForgeRegistryEntry<ISpinningWheelRecipe> {

    /**
     * @return The list of input stacks, that should be spun
     */
    NonNullList<ItemStack> getInput();

    /**
     * @param stack The stack input to check validity of
     * @return Whether the input is valid or not
     */
    boolean isValidInput(@Nonnull ItemStack stack);

    /**
     * @return The main output after having spun the input
     */
    @Nonnull
    ItemStack getOutput();

    /**
     * @return The amount of wheel rotations the Spinning Wheel have to spin, for the input to be spun.
     */
    int getRotations();
}
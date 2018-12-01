package com.teammetallurgy.atum.api.recipe.quern;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;

public class QuernRecipe extends IForgeRegistryEntry.Impl<IQuernRecipe> implements IQuernRecipe {
    private final NonNullList<ItemStack> inputs;
    private final ItemStack output;
    private final ItemStack outputSecondary;
    private final int rotations;

    public QuernRecipe(String input, ItemStack output, int duration) {
        this(OreDictionary.getOres(input), output, ItemStack.EMPTY, duration);
    }

    public QuernRecipe(Block input, ItemStack output, int duration) {
        this(new ItemStack(input), output, duration);
    }

    public QuernRecipe(Item input, ItemStack output, int duration) {
        this(new ItemStack(input), output, duration);
    }

    public QuernRecipe(ItemStack input, ItemStack output, int duration) {
        this(input, output, ItemStack.EMPTY, duration);
    }

    public QuernRecipe(ItemStack input, ItemStack output, ItemStack outputSecondary, int duration) {
        this(NonNullList.withSize(1, input), output, outputSecondary, duration);
    }

    private QuernRecipe(NonNullList<ItemStack> input, ItemStack output, ItemStack outputSecondary, int rotations) {
        this.inputs = input;
        this.output = output;
        this.outputSecondary = outputSecondary;
        this.rotations = rotations;
    }

    @Override
    @Nonnull
    public NonNullList<ItemStack> getInput() {
        return this.inputs;
    }

    @Override
    public boolean isValidInput(@Nonnull ItemStack stack) {
        for (final ItemStack validInput : this.inputs) {
            if (stack.isItemEqual(validInput)) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Nonnull
    public ItemStack getOutput() {
        return this.output;
    }

    @Override
    @Nonnull
    public ItemStack getSecondaryOutput() {
        return this.outputSecondary;
    }

    @Override
    public int getRotations() {
        return this.rotations;
    }
}
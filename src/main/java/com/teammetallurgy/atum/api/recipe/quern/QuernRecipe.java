package com.teammetallurgy.atum.api.recipe.quern;

import com.teammetallurgy.atum.utils.StackHelper;
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
    private final int rotations;

    public QuernRecipe(String input, ItemStack output, int duration) {
        this(OreDictionary.getOres(input), output, duration);
    }

    public QuernRecipe(Block input, ItemStack output, int rotations) {
        this(new ItemStack(input), output, rotations);
    }

    public QuernRecipe(Item input, ItemStack output, int rotations) {
        this(new ItemStack(input), output, rotations);
    }

    public QuernRecipe(ItemStack input, ItemStack output, int rotations) {
        this(NonNullList.withSize(1, input), output, rotations);
    }

    private QuernRecipe(NonNullList<ItemStack> input, ItemStack output, int rotations) {
        this.inputs = input;
        this.output = output;
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
            if (StackHelper.areStacksEqualIgnoreSize(stack, validInput)) {
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
    public int getRotations() {
        return this.rotations;
    }
}
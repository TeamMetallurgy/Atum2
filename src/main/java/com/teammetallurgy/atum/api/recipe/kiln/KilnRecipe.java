package com.teammetallurgy.atum.api.recipe.kiln;

import com.teammetallurgy.atum.utils.StackHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.Tag;
import net.minecraft.util.NonNullList;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;

public class KilnRecipe extends ForgeRegistryEntry<IKilnRecipe> implements IKilnRecipe {
    private final NonNullList<ItemStack> inputs;
    private final ItemStack output;
    private final float experience;

    public KilnRecipe(Block input, @Nonnull ItemStack output, float experience) {
        this(new ItemStack(input), output, experience);
    }

    public KilnRecipe(Item input, @Nonnull ItemStack output, float experience) {
        this(new ItemStack(input), output, experience);
    }

    public KilnRecipe(@Nonnull ItemStack input, @Nonnull ItemStack output, float experience) {
        this(NonNullList.withSize(1, input), output, experience);
    }

    public KilnRecipe(Tag<Item> input, @Nonnull ItemStack output, float experience) {
        this(Ingredient.fromTag(input), output, experience);
    }

    public KilnRecipe(Ingredient input, @Nonnull ItemStack output, float experience) {
        this(NonNullList.from(ItemStack.EMPTY, input.getMatchingStacks()), output, experience);
    }

    private KilnRecipe(NonNullList<ItemStack> input, @Nonnull ItemStack output, float experience) {
        this.inputs = input;
        this.output = output;
        this.experience = experience;
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
    public float getExperience() {
        return experience;
    }
}
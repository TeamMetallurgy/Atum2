package com.teammetallurgy.atum.api.recipe;

import com.teammetallurgy.atum.misc.StackHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public abstract class AbstractAtumRecipe<C extends Container> implements Recipe<C> {
    protected final Ingredient input;
    protected final ItemStack output;
    protected final RecipeType<?> type;

    public AbstractAtumRecipe(RecipeType<?> type, Ingredient input, @Nonnull ItemStack output) {
        this.type = type;
        this.input = input;
        this.output = output;
    }

    @Override
    @Nonnull
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> inputs = NonNullList.create();
        inputs.add(this.input);
        return inputs;
    }

    @Override
    @Nonnull
    public ItemStack getResultItem(@Nonnull RegistryAccess registryAccess) {
        return this.output;
    }

    @Override
    public boolean matches(@Nonnull C inv, @Nonnull Level level) {
        for (Ingredient ingredient : this.getIngredients()) {
            if (StackHelper.areIngredientsEqualIgnoreSize(ingredient, inv.getItem(0))) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Nonnull
    public ItemStack assemble(@Nonnull C inv, @Nonnull RegistryAccess registryAccess) {
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    @Nonnull
    public RecipeType<?> getType() {
        return this.type;
    }

    @Override
    public boolean isSpecial() {
        return true; //Workaround for recipe category warning
    }
}
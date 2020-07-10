package com.teammetallurgy.atum.api.recipe;

import com.teammetallurgy.atum.misc.StackHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public abstract class AbstractAtumRecipe<C extends IInventory> implements IRecipe<C> {
    protected final Ingredient input;
    protected final ItemStack output;
    protected final IRecipeType<?> type;
    protected final ResourceLocation id;

    public AbstractAtumRecipe(IRecipeType<?> type, ResourceLocation id, Ingredient input, @Nonnull ItemStack output) {
        this.type = type;
        this.id = id;
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
    public ItemStack getRecipeOutput() {
        return this.output;
    }

    @Override
    public boolean matches(@Nonnull C inv, @Nonnull World world) {
        for (Ingredient ingredient : this.getIngredients()) {
            if (StackHelper.areIngredientsEqualIgnoreSize(ingredient, inv.getStackInSlot(0))) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Nonnull
    public ItemStack getCraftingResult(@Nonnull C inv) {
        return this.output.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    @Nonnull
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    @Nonnull
    public IRecipeType<?> getType() {
        return this.type;
    }
}
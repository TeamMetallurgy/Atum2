package com.teammetallurgy.atum.integration.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.action.recipe.ActionRemoveRecipeByOutput;
import com.blamejared.crafttweaker.api.action.recipe.ActionRemoveRecipeByOutputInput;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.AtumRecipeTypes;
import com.teammetallurgy.atum.api.recipe.recipes.QuernRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.atum.Quern")
public class CTQuern implements IRecipeManager<QuernRecipe> {

    @ZenCodeType.Method
    public void addRecipe(IIngredient input, IItemStack output, int rotations) {
        CraftTweakerAPI.apply(new ActionAddRecipe<>(this, new RecipeHolder<>(new ResourceLocation(Atum.MOD_ID, "quern"), new QuernRecipe(input.asVanillaIngredient(), output.getInternal(), rotations)), "quern"));
    }

    @ZenCodeType.Method
    public void removeRecipeByOutput(IItemStack output) {
        CraftTweakerAPI.apply(new ActionRemoveRecipeByOutput<>(this, output));
    }

    @ZenCodeType.Method
    public void removeRecipeByOutputInput(IItemStack output, IItemStack input) {
        CraftTweakerAPI.apply(new ActionRemoveRecipeByOutputInput<>(this, output, input));
    }

    @Override
    public RecipeType<QuernRecipe> getRecipeType() {
        return AtumRecipeTypes.QUERN.get();
    }
}
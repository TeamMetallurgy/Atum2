/*package com.teammetallurgy.atum.integration.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionRemoveRecipeByOutput;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionRemoveRecipeByOutputInput;
import com.teammetallurgy.atum.api.recipe.IAtumRecipeType;
import com.teammetallurgy.atum.api.recipe.recipes.QuernRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.atum.Quern")
public class CTQuern implements IRecipeManager {

    @ZenCodeType.Method
    public void addRecipe(IItemStack input, IItemStack output, int rotations) {
        CraftTweakerAPI.apply(new ActionAddRecipe(this, new QuernRecipe(input.getInternal(), output.getInternal(), rotations), "quern"));
    }

    @ZenCodeType.Method
    public void removeRecipeByOutput(IItemStack output) {
        CraftTweakerAPI.apply(new ActionRemoveRecipeByOutput(this, output));
    }

    @ZenCodeType.Method
    public void removeRecipeByOutputInput(IItemStack output, IItemStack input) {
        CraftTweakerAPI.apply(new ActionRemoveRecipeByOutputInput(this, output, input));
    }

    @Override
    public RecipeType<QuernRecipe> getRecipeType() {
        return IAtumRecipeType.QUERN;
    }
}*/
package com.teammetallurgy.atum.integration.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.base.IAction;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.action.recipe.ActionRemoveRecipeByOutput;
import com.blamejared.crafttweaker.api.action.recipe.ActionRemoveRecipeByOutputInput;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.teammetallurgy.atum.api.recipe.AtumRecipeTypes;
import com.teammetallurgy.atum.api.recipe.recipes.KilnRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.atum.Kiln")
public class CTKiln implements IRecipeManager<KilnRecipe> {

    @ZenCodeType.Method
    public void addRecipe(IItemStack input, IItemStack output, float experience) {
        this.addRecipe(input, output, experience, 75);
    }

    @ZenCodeType.Method
    public void addRecipe(IItemStack input, IItemStack output, float experience, int cookTime) {
        CraftTweakerAPI.apply(new ActionAddRecipe<>(this, new KilnRecipe(input.getInternal(), output.getInternal(), experience, cookTime), "kiln"));
    }

    @ZenCodeType.Method
    public void removeRecipeByOutput(IItemStack output) {
        CraftTweakerAPI.apply(new ActionRemoveRecipeByOutput<>(this, output));
    }

    @ZenCodeType.Method
    public void removeRecipeByOutputInput(IItemStack output, IItemStack input) {
        CraftTweakerAPI.apply(new ActionRemoveRecipeByOutputInput<>(this, output, input));
    }

    @ZenCodeType.Method
    public static void blacklist(String id) {
        CraftTweakerAPI.apply(new Blacklist(id));
    }

    @Override
    public RecipeType<KilnRecipe> getRecipeType() {
        return AtumRecipeTypes.KILN;
    }

    private static class Blacklist implements IAction {
        private final String id;

        Blacklist(String id) {
            this.id = id;
        }

        @Override
        public void apply() {
            final ResourceLocation location = new ResourceLocation(this.id);
            if (!AtumRecipeTypes.kilnBlacklist.contains(location)) {
                AtumRecipeTypes.kilnBlacklist.add(location);
            }
        }

        @Override
        public String describe() {
            return "Blacklisted " + this.id + " from being in a Kiln recipe";
        }
    }
}
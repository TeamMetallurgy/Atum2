package com.teammetallurgy.atum.integration.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.actions.IAction;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionRemoveRecipeByOutput;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionRemoveRecipeByOutputInput;
import com.teammetallurgy.atum.api.recipe.IAtumRecipeType;
import com.teammetallurgy.atum.api.recipe.recipes.KilnRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.atum.Kiln")
public class CTKiln implements IRecipeManager {

    @ZenCodeType.Method
    public void addRecipe(IItemStack input, IItemStack output, float experience) {
        this.addRecipe(input, output, experience, 75);
    }

    @ZenCodeType.Method
    public void addRecipe(IItemStack input, IItemStack output, float experience, int cookTime) {
        CraftTweakerAPI.apply(new ActionAddRecipe(this, new KilnRecipe(input.getInternal(), output.getInternal(), experience, cookTime), "kiln"));
    }

    @ZenCodeType.Method
    public void removeRecipeByOutput(IItemStack output) {
        CraftTweakerAPI.apply(new ActionRemoveRecipeByOutput(this, output));
    }

    @ZenCodeType.Method
    public void removeRecipeByOutputInput(IItemStack output, IItemStack input) {
        CraftTweakerAPI.apply(new ActionRemoveRecipeByOutputInput(this, output, input));
    }

    @ZenCodeType.Method
    public static void blacklist(String id) {
        CraftTweakerAPI.apply(new Blacklist(id));
    }

    @Override
    public IRecipeType<KilnRecipe> getRecipeType() {
        return IAtumRecipeType.KILN;
    }

    private static class Blacklist implements IAction {
        private final String id;

        Blacklist(String id) {
            this.id = id;
        }

        @Override
        public void apply() {
            final ResourceLocation location = new ResourceLocation(this.id);
            if (!IAtumRecipeType.kilnBlacklist.contains(location)) {
                IAtumRecipeType.kilnBlacklist.add(location);
            }
        }

        @Override
        public String describe() {
            return "Blacklisted " + this.id + " from being in a Kiln recipe";
        }
    }
}
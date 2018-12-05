package com.teammetallurgy.atum.integration.crafttweaker;

import com.teammetallurgy.atum.api.recipe.RecipeHandlers;
import com.teammetallurgy.atum.api.recipe.quern.QuernRecipe;
import com.teammetallurgy.atum.utils.AtumRegistry;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.atum.Quern")
public class CTQuern {

    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output, int rotations) {
        CraftTweakerAPI.apply(new Add(CraftTweakerMC.getItemStack(input), CraftTweakerMC.getItemStack(output), rotations));
    }

    @ZenMethod
    public static void removeRecipe() {
        CraftTweakerAPI.apply(new Remove());
    }

    private static class Add implements IAction {
        private ItemStack input, output;
        private int rotations;

        Add(ItemStack input, ItemStack output, int rotations) {
            this.input = input;
            this.output = output;
            this.rotations = rotations;
        }

        @Override
        public void apply() {
            CTLists.QUERN_ADDITIONS.add(new QuernRecipe(this.input, this.output, this.rotations));
        }

        @Override
        public String describe() {
            return "Added new Quern recipe. Input: " + input.getDisplayName() + " Output: " + output.getDisplayName();
        }
    }

    private static class Remove implements IAction {

        Remove() {
        }

        @Override
        public void apply() {
            System.out.println("Apply Remove Quern Recipe");
            RecipeHandlers.quernRecipes.getValuesCollection().removeAll(RecipeHandlers.quernRecipes.getValuesCollection());
        }

        @Override
        public String describe() {
            return "";
        }
    }
}
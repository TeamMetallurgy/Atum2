package com.teammetallurgy.atum.integration.crafttweaker;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.RecipeHandlers;
import com.teammetallurgy.atum.api.recipe.quern.IQuernRecipe;
import com.teammetallurgy.atum.api.recipe.quern.QuernRecipe;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.LinkedList;
import java.util.List;

@ZenRegister
@ZenClass("mods.atum.Quern")
public class CTQuern {
    public static final List<IQuernRecipe> REMOVALS = new LinkedList<>();
    public static final List<IQuernRecipe> ADDITIONS = new LinkedList<>();

    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output, int rotations) {
        CraftTweakerAPI.apply(new Add(CraftTweakerMC.getItemStack(input), CraftTweakerMC.getItemStack(output), rotations));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        CraftTweakerAPI.apply(new Remove(CraftTweakerMC.getItemStack(input)));
    }

    private static class Add implements IAction {
        private ItemStack input, output;
        private int rotations;

        private Add(ItemStack input, ItemStack output, int rotations) {
            this.input = input;
            this.output = output;
            this.rotations = rotations;
        }

        @Override
        public void apply() {
            ADDITIONS.add(new QuernRecipe(this.input, this.output, this.rotations));
        }

        @Override
        public String describe() {
            return "Added new Quern recipe. Input: " + input.getDisplayName() + " Output: " + output.getDisplayName();
        }
    }

    private static class Remove implements IAction {
        private ItemStack input;

        public Remove(ItemStack input) {
            this.input = input;
        }

        @Override
        public void apply() {
            for (IQuernRecipe quernRecipe : RecipeHandlers.quernRecipes.getValuesCollection()) {
                if (!quernRecipe.isValidInput(this.input)) {
                    Atum.LOG.error("No Quern recipe exists for " + this.input);
                    return;
                } else {
                    REMOVALS.add(quernRecipe);
                }
            }
        }

        @Override
        public String describe() {
            return "Removed Quern recipes with " + this.input.getDisplayName() + " as the input";
        }
    }
}
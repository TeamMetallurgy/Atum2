package com.teammetallurgy.atum.integration.crafttweaker;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.RecipeHandlers;
import com.teammetallurgy.atum.api.recipe.spinningwheel.SpinningWheelRecipe;
import com.teammetallurgy.atum.init.AtumItems;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Objects;

@ZenRegister
@ZenClass("mods.atum.SpinningWheel")
public class CTSpinningWheel {

    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output, int rotations) {
        CraftTweakerAPI.apply(new Add(CraftTweakerMC.getItemStack(input), CraftTweakerMC.getItemStack(output), rotations));
    }

    @ZenMethod
    public static void addInput(IItemStack input, int rotations) {
        CraftTweakerAPI.apply(new Add(CraftTweakerMC.getItemStack(input), new ItemStack(AtumItems.LINEN_THREAD), rotations));
    }

    @ZenMethod
    public static void removeRecipe(String id) {
        CraftTweakerAPI.apply(new Remove(id));
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
            ResourceLocation registryName = new ResourceLocation("crafttweaker", Objects.requireNonNull(this.input.getItem().getRegistryName()).getPath());
            RecipeHandlers.spinningWheelRecipes.register(new SpinningWheelRecipe(this.input, this.output, this.rotations).setRegistryName(registryName));
        }

        @Override
        public String describe() {
            return "Added new Spinning Wheel recipe. Input: " + input.getDisplayName() + " Output: " + output.getDisplayName();
        }
    }

    private static class Remove implements IAction {
        private String id;

        Remove(String id) {
            this.id = id;
        }

        @Override
        public void apply() {
            final ResourceLocation location = new ResourceLocation(id);
            if (!RecipeHandlers.spinningWheelRecipes.containsKey(location)) {
                Atum.LOG.error("No Spinning Wheel recipe exists called: " + this.id);
            } else {
                RecipeHandlers.spinningWheelRecipes.remove(location);
            }
        }

        @Override
        public String describe() {
            return "Removed Spinning Wheel recipe: " + this.id;
        }
    }
}
package com.teammetallurgy.atum.integration.crafttweaker;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.RecipeHandlers;
import com.teammetallurgy.atum.api.recipe.kiln.KilnRecipe;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;
import java.util.Objects;

@ZenRegister
@ZenClass("mods.atum.Kiln")
public class CTKiln {

    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output, double experience) {
        CraftTweakerAPI.apply(new Add(CraftTweakerMC.getItemStack(input), CraftTweakerMC.getItemStack(output), (float) experience));
    }

    @ZenMethod
    public static void removeRecipe(String id) {
        CraftTweakerAPI.apply(new Remove(id));
    }

    @ZenMethod
    public static void blacklist(IItemStack ingredient) {
        CraftTweakerAPI.apply(new Blacklist(CraftTweakerMC.getItemStack(ingredient)));
    }

    private static class Add implements IAction {
        private ItemStack input, output;
        private float experience;

        Add(@Nonnull ItemStack input, @Nonnull ItemStack output, float experience) {
            this.input = input;
            this.output = output;
            this.experience = experience;
        }

        @Override
        public void apply() {
            ResourceLocation registryName = new ResourceLocation("crafttweaker", Objects.requireNonNull(this.input.getItem().getRegistryName()).getPath());
            RecipeHandlers.kilnRecipes.register(new KilnRecipe(this.input, this.output, this.experience).setRegistryName(registryName));
        }

        @Override
        public String describe() {
            return "Added new Kiln recipe. Input: " + input.getDisplayName() + " Output: " + output.getDisplayName();
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
            if (!RecipeHandlers.kilnRecipes.containsKey(location)) {
                Atum.LOG.error("No Kiln recipe exists called: " + this.id);
            } else {
                RecipeHandlers.kilnRecipes.remove(location);
            }
        }

        @Override
        public String describe() {
            return "Removed Kiln recipe: " + this.id;
        }
    }

    private static class Blacklist implements IAction {
        private ItemStack blacklistedStack;

        Blacklist(@Nonnull ItemStack blacklistedStack) {
            this.blacklistedStack = blacklistedStack;
        }

        @Override
        public void apply() {
            if (!RecipeHandlers.kilnBlacklist.contains(this.blacklistedStack)) {
                RecipeHandlers.kilnBlacklist.add(this.blacklistedStack);
            }
        }

        @Override
        public String describe() {
            return "Blacklisted " + this.blacklistedStack + " from being in a Kiln recipe";
        }
    }
}
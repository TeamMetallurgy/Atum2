package com.teammetallurgy.atum.integration.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.actions.IAction;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.RecipeHandlers;
import com.teammetallurgy.atum.api.recipe.kiln.KilnRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

import javax.annotation.Nonnull;
import java.util.Objects;

@ZenRegister
@ZenCodeType.Name("mods.atum.Kiln")
public class CTKiln {

    @ZenCodeType.Method
    public static void addRecipe(IItemStack input, IItemStack output, double experience) {
        CraftTweakerAPI.apply(new Add(input.getInternal(), output.getInternal(), (float) experience));
    }

    @ZenCodeType.Method
    public static void removeRecipe(String id) {
        CraftTweakerAPI.apply(new Remove(id));
    }

    @ZenCodeType.Method
    public static void blacklist(String id) {
        CraftTweakerAPI.apply(new Blacklist(id));
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
        private String id;

        Blacklist(String id) {
            this.id = id;
        }

        @Override
        public void apply() {
            final ResourceLocation location = new ResourceLocation(this.id);
            if (!RecipeHandlers.kilnBlacklist.contains(location)) {
                RecipeHandlers.kilnBlacklist.add(location);
            }
        }

        @Override
        public String describe() {
            return "Blacklisted " + this.id + " from being in a Kiln recipe";
        }
    }
}
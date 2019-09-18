package com.teammetallurgy.atum.integration.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.actions.IAction;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.RecipeHandlers;
import com.teammetallurgy.atum.api.recipe.quern.QuernRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

import javax.annotation.Nonnull;
import java.util.Objects;

@ZenRegister
@ZenCodeType.Name("mods.atum.Quern")
public class CTQuern {

    @ZenCodeType.Method
    public static void addRecipe(IItemStack input, IItemStack output, int rotations) {
        CraftTweakerAPI.apply(new Add(input.getInternal(), output.getInternal(), rotations));
    }

    @ZenCodeType.Method
    public static void removeRecipe(String id) {
        CraftTweakerAPI.apply(new Remove(id));
    }

    private static class Add implements IAction {
        private ItemStack input, output;
        private int rotations;

        Add(@Nonnull ItemStack input, @Nonnull ItemStack output, int rotations) {
            this.input = input;
            this.output = output;
            this.rotations = rotations;
        }

        @Override
        public void apply() {
            ResourceLocation registryName = new ResourceLocation("crafttweaker", Objects.requireNonNull(this.input.getItem().getRegistryName()).getPath());
            RecipeHandlers.quernRecipes.register(new QuernRecipe(this.input, this.output, this.rotations).setRegistryName(registryName));
        }

        @Override
        public String describe() {
            return "Added new Quern recipe. Input: " + input.getDisplayName() + " Output: " + output.getDisplayName();
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
            if (!RecipeHandlers.quernRecipes.containsKey(location)) {
                Atum.LOG.error("No Quern recipe exists called: " + this.id);
            } else {
                RecipeHandlers.quernRecipes.remove(location);
            }
        }

        @Override
        public String describe() {
            return "Removed Quern recipe: " + this.id;
        }
    }
}
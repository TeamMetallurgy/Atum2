package com.teammetallurgy.atum.integration.crafttweaker;

import com.teammetallurgy.atum.api.recipe.RecipeHandlers;
import com.teammetallurgy.atum.api.recipe.quern.IQuernRecipe;
import com.teammetallurgy.atum.api.recipe.quern.QuernRecipe;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.atum.Quern")
@Mod.EventBusSubscriber
public class CTQuern {

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
        private ItemStack input;

        Remove(ItemStack input) {
            this.input = input;
        }

        @Override
        public void apply() {
            System.out.println("Apply Remove Quern Recipe");
            CTLists.QUERN_REMOVALS.addAll(RecipeHandlers.quernRecipes.getValuesCollection());
        }

        @Override
        public String describe() {
            return "Removed Quern recipes with " + this.input.getDisplayName() + " as the input";
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void removeQuernRecipe(RegistryEvent.Register<IQuernRecipe> event) {
        System.out.println("Lowest priority quern removal");
        System.out.println(CTLists.QUERN_REMOVALS);
        for (IQuernRecipe quernRecipe : CTLists.QUERN_REMOVALS) {
            System.out.println("Recipe: " + quernRecipe);
            event.getRegistry().getValuesCollection().remove(quernRecipe);
        }
    }
}
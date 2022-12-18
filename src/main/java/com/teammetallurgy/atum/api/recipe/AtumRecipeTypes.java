package com.teammetallurgy.atum.api.recipe;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.recipes.KilnRecipe;
import com.teammetallurgy.atum.api.recipe.recipes.QuernRecipe;
import com.teammetallurgy.atum.api.recipe.recipes.SpinningWheelRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class AtumRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPE_DEFERRED = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Atum.MOD_ID);
    public static RegistryObject<RecipeType<KilnRecipe>> KILN = register("kiln");
    public static RegistryObject<RecipeType<QuernRecipe>> QUERN = register("quern");
    public static RegistryObject<RecipeType<SpinningWheelRecipe>> SPINNING_WHEEL = register("spinning_wheel");
    /**
     * Instance for the Kiln recipe blacklist.
     * Used to blacklist what Furnace recipes the Kiln grabs
     * Can be used by other mods or in a CraftTweaker script, see {@link CTKiln}
     */
    public static final List<ResourceLocation> kilnBlacklist = new ArrayList<>();

    static <T extends Recipe<?>> RegistryObject<RecipeType<T>> register(final String key) {
        ResourceLocation id = new ResourceLocation(Atum.MOD_ID, key);
        return RECIPE_TYPE_DEFERRED.register(id.toString(), () -> new RecipeType<T>() {
            @Override
            public String toString() {
                return id.toString();
            }
        });
    }
}
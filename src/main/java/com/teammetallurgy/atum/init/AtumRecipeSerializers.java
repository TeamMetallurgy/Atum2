package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.RotationRecipe;
import com.teammetallurgy.atum.api.recipe.recipes.KilnRecipe;
import com.teammetallurgy.atum.api.recipe.recipes.QuernRecipe;
import com.teammetallurgy.atum.api.recipe.recipes.SpinningWheelRecipe;
import com.teammetallurgy.atum.misc.recipe.MapExtendingScrollRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AtumRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER_DEFERRED = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Atum.MOD_ID);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<MapExtendingScrollRecipe>> MAP_EXTENDING_SCROLL = register("map_extending_scroll", new SimpleCraftingRecipeSerializer<>(MapExtendingScrollRecipe::new));
    public static final DeferredHolder<RecipeSerializer<?>, KilnRecipe.Serializer<KilnRecipe>> KILN = register("kiln", new KilnRecipe.Serializer<>(KilnRecipe::new, 75));
    public static final DeferredHolder<RecipeSerializer<?>, RotationRecipe.Serializer<QuernRecipe>> QUERN = register("quern", new RotationRecipe.Serializer<>(QuernRecipe::new));
    public static final DeferredHolder<RecipeSerializer<?>, RotationRecipe.Serializer<SpinningWheelRecipe>> SPINNING_WHEEL = register("spinning_wheel", new RotationRecipe.Serializer<>(SpinningWheelRecipe::new));

    private static <S extends RecipeSerializer<T>, T extends Recipe<?>> DeferredHolder<RecipeSerializer<?>, S> register(String name, S recipeSerializer) {
        return RECIPE_SERIALIZER_DEFERRED.register(name, () -> recipeSerializer);
    }
}
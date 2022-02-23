package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.RotationRecipe;
import com.teammetallurgy.atum.api.recipe.recipes.KilnRecipe;
import com.teammetallurgy.atum.api.recipe.recipes.QuernRecipe;
import com.teammetallurgy.atum.api.recipe.recipes.SpinningWheelRecipe;
import com.teammetallurgy.atum.misc.recipe.MapExtendingScrollRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class AtumRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER_DEFERRED = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Atum.MOD_ID);
    public static final RegistryObject<SimpleRecipeSerializer<MapExtendingScrollRecipe>> MAP_EXTENDING_SCROLL = register("map_extending_scroll", new SimpleRecipeSerializer<>(MapExtendingScrollRecipe::new));
    public static final RegistryObject<KilnRecipe.Serializer<KilnRecipe>> KILN = register("kiln", new KilnRecipe.Serializer<>(KilnRecipe::new, 75));
    public static final RegistryObject<RotationRecipe.Serializer<QuernRecipe>> QUERN = register("quern", new RotationRecipe.Serializer<>(QuernRecipe::new, false));
    public static final RegistryObject<RotationRecipe.Serializer<SpinningWheelRecipe>> SPINNING_WHEEL = register("spinning_wheel", new RotationRecipe.Serializer<>(SpinningWheelRecipe::new, true));

    private static <S extends RecipeSerializer<T>, T extends Recipe<?>> RegistryObject<S> register(String name, S recipeSerializer) {
        System.out.println("GIRAFI: " + name);
        return RECIPE_SERIALIZER_DEFERRED.register(name, () -> recipeSerializer);
    }
}
package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.RotationRecipe;
import com.teammetallurgy.atum.api.recipe.recipes.KilnRecipe;
import com.teammetallurgy.atum.api.recipe.recipes.QuernRecipe;
import com.teammetallurgy.atum.api.recipe.recipes.SpinningWheelRecipe;
import com.teammetallurgy.atum.misc.recipe.MapExtendingScrollRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumRecipeSerializers {
    private static final List<RecipeSerializer<?>> SERIALIZERS = new ArrayList<>();
    public static final SimpleRecipeSerializer<MapExtendingScrollRecipe> MAP_EXTENDING_SCROLL = register("map_extending_scroll", new SimpleRecipeSerializer<>(MapExtendingScrollRecipe::new));
    public static final KilnRecipe.Serializer<KilnRecipe> KILN = register("kiln", new KilnRecipe.Serializer<>(KilnRecipe::new, 75));
    public static final RotationRecipe.Serializer<QuernRecipe> QUERN = register("quern", new RotationRecipe.Serializer<>(QuernRecipe::new, false));
    public static final RotationRecipe.Serializer<SpinningWheelRecipe> SPINNING_WHEEL = register("spinning_wheel", new RotationRecipe.Serializer<>(SpinningWheelRecipe::new, true));

    private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String name, S recipeSerializer) {
        recipeSerializer.setRegistryName(new ResourceLocation(Atum.MOD_ID, name));
        SERIALIZERS.add(recipeSerializer);
        return recipeSerializer;
    }

    @SubscribeEvent
    public static void registerRecipeSerializers(RegistryEvent.Register<RecipeSerializer<?>> event) {
        for (RecipeSerializer<?> serializer : SERIALIZERS) {
            event.getRegistry().register(serializer);
        }
    }
}
package com.teammetallurgy.atum.api.recipe;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.recipes.KilnRecipe;
import com.teammetallurgy.atum.api.recipe.recipes.QuernRecipe;
import com.teammetallurgy.atum.api.recipe.recipes.SpinningWheelRecipe;
import com.teammetallurgy.atum.integration.crafttweaker.CTKiln;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumRecipeTypes {
    public static RecipeType<KilnRecipe> KILN;
    public static RecipeType<QuernRecipe> QUERN;
    public static RecipeType<SpinningWheelRecipe> SPINNING_WHEEL;
    /**
     * Instance for the Kiln recipe blacklist.
     * Used to blacklist what Furnace recipes the Kiln grabs
     * Can be used by other mods or in a CraftTweaker script, see {@link CTKiln}
     */
    public static final List<ResourceLocation> kilnBlacklist = new ArrayList<>();

    static <T extends Recipe<?>> RecipeType<T> register(final String key) {
        ResourceLocation id = new ResourceLocation(Atum.MOD_ID, key);
        return Registry.register(Registry.RECIPE_TYPE, id, new RecipeType<T>() {
            @Override
            public String toString() {
                return id.toString();
            }
        });
    }

    @SubscribeEvent
    public static void registerRecipeTypes(RegistryEvent.Register<RecipeSerializer<?>> event) {
        KILN = register("kiln");
        QUERN = register("quern");
        SPINNING_WHEEL = register("spinning_wheel");
    }
}
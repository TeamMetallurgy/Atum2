package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.RotationRecipe;
import com.teammetallurgy.atum.api.recipe.recipes.KilnRecipe;
import com.teammetallurgy.atum.api.recipe.recipes.QuernRecipe;
import com.teammetallurgy.atum.api.recipe.recipes.SpinningWheelRecipe;
import com.teammetallurgy.atum.misc.recipe.DisenchantRecipe;
import com.teammetallurgy.atum.misc.recipe.MapExtendingScrollRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(value = Atum.MOD_ID)
public class AtumRecipeSerializers {
    private static final List<IRecipeSerializer<?>> SERIALIZERS = new ArrayList<>();
    public static final SpecialRecipeSerializer<MapExtendingScrollRecipe> MAP_EXTENDING_SCROLL = register("map_extending_scroll", new SpecialRecipeSerializer<>(MapExtendingScrollRecipe::new));
    public static final SpecialRecipeSerializer<DisenchantRecipe> DISENCHANT = register("disenchant", new SpecialRecipeSerializer<>(DisenchantRecipe::new));
    public static final KilnRecipe.Serializer<KilnRecipe> KILN = register("kiln", new KilnRecipe.Serializer<>(KilnRecipe::new, 75));
    public static final RotationRecipe.Serializer<QuernRecipe> QUERN = register("quern", new RotationRecipe.Serializer<>(QuernRecipe::new, false));
    public static final RotationRecipe.Serializer<SpinningWheelRecipe> SPINNING_WHEEL = register("spinning_wheel", new RotationRecipe.Serializer<>(SpinningWheelRecipe::new, true));

    private static <S extends IRecipeSerializer<T>, T extends IRecipe<?>> S register(String name, S recipeSerializer) {
        recipeSerializer.setRegistryName(new ResourceLocation(Atum.MOD_ID, name));
        SERIALIZERS.add(recipeSerializer);
        return recipeSerializer;
    }

    @SubscribeEvent
    public static void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        for (IRecipeSerializer<?> serializer : SERIALIZERS) {
            event.getRegistry().register(serializer);
        }
    }
}
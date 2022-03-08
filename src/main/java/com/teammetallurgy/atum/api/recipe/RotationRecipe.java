package com.teammetallurgy.atum.api.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class RotationRecipe<C extends Container> extends AbstractAtumRecipe<C> {
    protected final int rotations;

    public RotationRecipe(RecipeType<?> recipeType, ResourceLocation id, Ingredient input, @Nonnull ItemStack output, int rotations) {
        super(recipeType, id, input, output);
        this.rotations = rotations;
    }

    public int getRotations() {
        return this.rotations;
    }

    public static class Serializer<C extends RotationRecipe<? extends Container>> extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<C> {
        private final Serializer.IFactory<C> factory;
        private final boolean inputCanHaveCount;

        public Serializer(Serializer.IFactory<C> factory, boolean inputCanHaveCount) {
            this.factory = factory;
            this.inputCanHaveCount = inputCanHaveCount;
        }

        @Override
        @Nonnull
        public C fromJson(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
            JsonObject inputObject = GsonHelper.getAsJsonObject(json, "ingredient");
            Ingredient input;
            if (inputObject.has("tag")) { //Only read as Ingredient directly, when it's a tag
                Ingredient ingredient = CraftingHelper.getIngredient(inputObject);
                if (this.inputCanHaveCount && inputObject.has("count")) {
                    List<ItemStack> ingredientStacks = new ArrayList<>();
                    for (ItemStack stack : ingredient.getItems()) {
                        ingredientStacks.add(new ItemStack(stack.getItem(), GsonHelper.getAsInt(inputObject, "count", 1)));
                    }
                    input = Ingredient.of(ingredientStacks.toArray(new ItemStack[0]));
                } else {
                    input = ingredient;
                }
                Item tagItem = Registry.ITEM.get(new ResourceLocation(GsonHelper.getAsString(inputObject, "tag")));
                if (tagItem == null || tagItem == Items.AIR) { //Support empty tags, for mod support
                    input = Ingredient.EMPTY;
                }
            } else if (!this.inputCanHaveCount) {
                input = CraftingHelper.getIngredient(inputObject);
            } else {
                ItemStack stack = CraftingHelper.getItemStack(inputObject, true);
                input = Ingredient.of(stack);
            }

            if (!json.has("result")) {
                throw new JsonSyntaxException("Missing result, expected to find a string or object");
            }
            ItemStack output;
            if (json.get("result").isJsonObject()) {
                output = new ItemStack(ShapedRecipe.itemFromJson(GsonHelper.getAsJsonObject(json, "result")));
            } else {
                String result = GsonHelper.getAsString(json, "result");
                ResourceLocation resultID = new ResourceLocation(result);
                output = new ItemStack(ForgeRegistries.ITEMS.getValue(resultID));
            }
            int rotations = GsonHelper.getAsInt(json, "rotations", 0);
            return this.factory.create(id, input, output, rotations);
        }

        @Override
        public C fromNetwork(@Nonnull ResourceLocation id, @Nonnull FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack stack = buffer.readItem();
            int rotations = buffer.readInt();
            return this.factory.create(id, ingredient, stack, rotations);
        }

        @Override
        public void toNetwork(@Nonnull FriendlyByteBuf buffer, C recipe) {
            recipe.input.toNetwork(buffer);
            buffer.writeItem(recipe.output);
            buffer.writeInt(recipe.rotations);
        }

        public interface IFactory<T extends RotationRecipe<? extends Container>> {
            T create(ResourceLocation id, Ingredient input, @Nonnull ItemStack output, int rotations);
        }
    }
}
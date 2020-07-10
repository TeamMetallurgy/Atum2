package com.teammetallurgy.atum.api.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class RotationRecipe<C extends IInventory> extends AbstractAtumRecipe<C> {
    protected final int rotations;

    public RotationRecipe(IRecipeType<?> recipeType, ResourceLocation id, Ingredient input, @Nonnull ItemStack output, int rotations) {
        super(recipeType, id, input, output);
        this.rotations = rotations;
    }

    public int getRotations() {
        return this.rotations;
    }

    public static class Serializer<C extends RotationRecipe<? extends IInventory>> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<C> {
        private final Serializer.IFactory<C> factory;
        private final boolean inputCanHaveCount;

        public Serializer(Serializer.IFactory<C> factory, boolean inputCanHaveCount) {
            this.factory = factory;
            this.inputCanHaveCount = inputCanHaveCount;
        }

        @Override
        @Nonnull
        public C read(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
            JsonObject inputObject = JSONUtils.getJsonObject(json, "ingredient");
            Ingredient input;
            if (inputObject.has("tag")) { //Only read as Ingredient directly, when it's a tag
                ResourceLocation tagLocation = new ResourceLocation(JSONUtils.getString(inputObject, "tag"));
                Tag<Item> tag = ItemTags.getCollection().get(tagLocation);
                if (tag != null && !tag.getAllElements().isEmpty()) { //Support empty tags, for mod support
                    Ingredient ingredient = CraftingHelper.getIngredient(inputObject);
                    if (this.inputCanHaveCount && inputObject.has("count")) {
                        List<ItemStack> ingredientStacks = new ArrayList<>();
                        for (ItemStack stack : ingredient.getMatchingStacks()) {
                            ingredientStacks.add(new ItemStack(stack.getItem(), JSONUtils.getInt(inputObject, "count", 1)));
                        }
                        input = Ingredient.fromStacks(ingredientStacks.toArray(new ItemStack[0]));
                    } else {
                        input = ingredient;
                    }
                } else {
                    input = Ingredient.EMPTY;
                }
            } else if (!this.inputCanHaveCount) {
                input = CraftingHelper.getIngredient(inputObject);
            } else {
                ItemStack stack = CraftingHelper.getItemStack(inputObject, true);
                input = Ingredient.fromStacks(stack);
            }

            if (!json.has("result")) {
                throw new JsonSyntaxException("Missing result, expected to find a string or object");
            }
            ItemStack output;
            if (json.get("result").isJsonObject()) {
                output = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            } else {
                String result = JSONUtils.getString(json, "result");
                ResourceLocation resultID = new ResourceLocation(result);
                output = new ItemStack(Registry.ITEM.getValue(resultID).orElseThrow(() -> new IllegalStateException("Item: " + result + " does not exist")));
            }
            int rotations = JSONUtils.getInt(json, "rotations", 0);
            return this.factory.create(id, input, output, rotations);
        }

        @Override
        public C read(@Nonnull ResourceLocation id, @Nonnull PacketBuffer buffer) {
            Ingredient ingredient = Ingredient.read(buffer);
            ItemStack stack = buffer.readItemStack();
            int rotations = buffer.readInt();
            return this.factory.create(id, ingredient, stack, rotations);
        }

        @Override
        public void write(@Nonnull PacketBuffer buffer, C recipe) {
            recipe.input.write(buffer);
            buffer.writeItemStack(recipe.output);
            buffer.writeInt(recipe.rotations);
        }

        public interface IFactory<T extends RotationRecipe<? extends IInventory>> {
            T create(ResourceLocation id, Ingredient input, @Nonnull ItemStack output, int rotations);
        }
    }
}
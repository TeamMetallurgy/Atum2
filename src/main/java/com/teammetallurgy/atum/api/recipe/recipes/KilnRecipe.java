package com.teammetallurgy.atum.api.recipe.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.AbstractAtumRecipe;
import com.teammetallurgy.atum.api.recipe.IAtumRecipeType;
import com.teammetallurgy.atum.blocks.machines.tileentity.KilnTileEntity;
import com.teammetallurgy.atum.init.AtumRecipeSerializers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.SetTag;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.Objects;

public class KilnRecipe extends AbstractAtumRecipe<KilnTileEntity> {
    protected final float experience;
    protected final int cookTime;

    public KilnRecipe(@Nonnull ItemStack input, @Nonnull ItemStack output, float experience, int cookTime) {
        this(Ingredient.of(input), output, experience, cookTime);
    }

    public KilnRecipe(SetTag<Item> input, @Nonnull ItemStack output, float experience, int cookTime) {
        this(Ingredient.of(input), output, experience, cookTime);
    }

    public KilnRecipe(Ingredient input, @Nonnull ItemStack output, float experience, int cookTime) {
        this(new ResourceLocation(Atum.MOD_ID, "kiln_" + Objects.requireNonNull(input.getItems()[0].getItem().getRegistryName()).getPath() + "_to_" + Objects.requireNonNull(output.getItem().getRegistryName()).getPath()), input, output, experience, cookTime);
    }

    public KilnRecipe(ResourceLocation id, Ingredient input, @Nonnull ItemStack output, float experience, int cookTime) {
        super(IAtumRecipeType.KILN, id, input, output);
        this.experience = experience;
        this.cookTime = cookTime;
    }

    public float getExperience() {
        return experience;
    }

    public int getCookTime() {
        return cookTime;
    }

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return AtumRecipeSerializers.KILN;
    }

    public static class Serializer<T extends KilnRecipe> extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<T> {
        private final Serializer.IFactory<T> factory;
        private final int cookTime;

        public Serializer(Serializer.IFactory<T> factory, int cookTime) {
            this.factory = factory;
            this.cookTime = cookTime;
        }

        @Override
        @Nonnull
        public T fromJson(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
            JsonElement jsonelement = GsonHelper.isArrayNode(json, "ingredient") ? GsonHelper.getAsJsonArray(json, "ingredient") : GsonHelper.getAsJsonObject(json, "ingredient");
            Ingredient ingredient = Ingredient.fromJson(jsonelement);
            //Forge: Check if primitive string to keep vanilla or a object which can contain a count field.
            if (!json.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
            ItemStack itemstack;
            if (json.get("result").isJsonObject()) itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            else {
                String s1 = GsonHelper.getAsString(json, "result");
                ResourceLocation resourcelocation = new ResourceLocation(s1);
                itemstack = new ItemStack(ForgeRegistries.ITEMS.getValue(resourcelocation));
            }
            float f = GsonHelper.getAsFloat(json, "experience", 0.0F);
            int i = GsonHelper.getAsInt(json, "cookingtime", this.cookTime);
            return this.factory.create(id, ingredient, itemstack, f, i);
        }

        @Override
        public T fromNetwork(@Nonnull ResourceLocation id, @Nonnull FriendlyByteBuf buffer) {
            Ingredient input = Ingredient.fromNetwork(buffer);
            ItemStack output = buffer.readItem();
            float experience = buffer.readFloat();
            int cookTime = buffer.readVarInt();
            return this.factory.create(id, input, output, experience, cookTime);
        }

        @Override
        public void toNetwork(@Nonnull FriendlyByteBuf buffer, T recipe) {
            recipe.input.toNetwork(buffer);
            buffer.writeItem(recipe.output);
            buffer.writeFloat(recipe.experience);
            buffer.writeVarInt(recipe.cookTime);
        }

        public interface IFactory<T extends KilnRecipe> {
            T create(ResourceLocation id, Ingredient input, @Nonnull ItemStack output, float experience, int cookTime);
        }
    }
}
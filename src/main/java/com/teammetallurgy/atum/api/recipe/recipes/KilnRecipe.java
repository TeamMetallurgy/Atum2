package com.teammetallurgy.atum.api.recipe.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.AbstractAtumRecipe;
import com.teammetallurgy.atum.api.recipe.IAtumRecipeType;
import com.teammetallurgy.atum.blocks.machines.tileentity.KilnTileEntity;
import com.teammetallurgy.atum.init.AtumRecipeSerializers;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.Tag;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;

public class KilnRecipe extends AbstractAtumRecipe<KilnTileEntity> {
    protected final float experience;
    protected final int cookTime;

    public KilnRecipe(@Nonnull ItemStack input, @Nonnull ItemStack output, float experience, int cookTime) {
        this(Ingredient.fromStacks(input), output, experience, cookTime);
    }

    public KilnRecipe(Tag<Item> input, @Nonnull ItemStack output, float experience, int cookTime) {
        this(Ingredient.fromTag(input), output, experience, cookTime);
    }

    public KilnRecipe(Ingredient input, @Nonnull ItemStack output, float experience, int cookTime) {
        this(new ResourceLocation(Atum.MOD_ID, "kiln"), input, output, experience, cookTime);
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
    public IRecipeSerializer<?> getSerializer() {
        return AtumRecipeSerializers.KILN;
    }

    public static class Serializer<T extends KilnRecipe> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T> {
        private final Serializer.IFactory<T> factory;
        private final int cookTime;

        public Serializer(Serializer.IFactory<T> factory, int cookTime) {
            this.factory = factory;
            this.cookTime = cookTime;
        }

        @Override
        @Nonnull
        public T read(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
            JsonElement jsonelement = JSONUtils.isJsonArray(json, "ingredient") ? JSONUtils.getJsonArray(json, "ingredient") : JSONUtils.getJsonObject(json, "ingredient");
            Ingredient ingredient = Ingredient.deserialize(jsonelement);
            //Forge: Check if primitive string to keep vanilla or a object which can contain a count field.
            if (!json.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
            ItemStack itemstack;
            if (json.get("result").isJsonObject()) itemstack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            else {
                String s1 = JSONUtils.getString(json, "result");
                ResourceLocation resourcelocation = new ResourceLocation(s1);
                itemstack = new ItemStack(ForgeRegistries.ITEMS.getValue(resourcelocation));
            }
            float f = JSONUtils.getFloat(json, "experience", 0.0F);
            int i = JSONUtils.getInt(json, "cookingtime", this.cookTime);
            return this.factory.create(id, ingredient, itemstack, f, i);
        }

        @Override
        public T read(@Nonnull ResourceLocation id, @Nonnull PacketBuffer buffer) {
            Ingredient input = Ingredient.read(buffer);
            ItemStack output = buffer.readItemStack();
            float experience = buffer.readFloat();
            int cookTime = buffer.readVarInt();
            return this.factory.create(id, input, output, experience, cookTime);
        }

        @Override
        public void write(@Nonnull PacketBuffer buffer, T recipe) {
            recipe.input.write(buffer);
            buffer.writeItemStack(recipe.output);
            buffer.writeFloat(recipe.experience);
            buffer.writeVarInt(recipe.cookTime);
        }

        public interface IFactory<T extends KilnRecipe> {
            T create(ResourceLocation id, Ingredient input, @Nonnull ItemStack output, float experience, int cookTime);
        }
    }
}
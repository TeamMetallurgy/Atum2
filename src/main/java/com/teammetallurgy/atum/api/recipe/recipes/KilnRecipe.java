package com.teammetallurgy.atum.api.recipe.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teammetallurgy.atum.api.recipe.AbstractAtumRecipe;
import com.teammetallurgy.atum.api.recipe.AtumRecipeTypes;
import com.teammetallurgy.atum.blocks.machines.tileentity.KilnTileEntity;
import com.teammetallurgy.atum.init.AtumRecipeSerializers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.common.crafting.CraftingHelper;

import javax.annotation.Nonnull;

public class KilnRecipe extends AbstractAtumRecipe<KilnTileEntity> {
    protected final float experience;
    protected final int cookTime;

    public KilnRecipe(@Nonnull ItemStack input, @Nonnull ItemStack output, float experience, int cookTime) {
        this(Ingredient.of(input), output, experience, cookTime);
    }

    public KilnRecipe(TagKey<Item> input, @Nonnull ItemStack output, float experience, int cookTime) {
        this(Ingredient.of(input), output, experience, cookTime);
    }

    public KilnRecipe(Ingredient input, @Nonnull ItemStack output, float experience, int cookTime) {
        super(AtumRecipeTypes.KILN.get(), input, output);
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
        return AtumRecipeSerializers.KILN.get();
    }

    public static class Serializer<T extends KilnRecipe> implements RecipeSerializer<T> {
        private final Serializer.IFactory<T> factory;
        public final Codec<T> codec;

        public Serializer(Serializer.IFactory<T> factory, int cookTime) {
            this.factory = factory;
            this.codec = RecordCodecBuilder.create(
                    r -> r.group(
                                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(kilnRecipe -> kilnRecipe.input),
                                    CraftingHelper.smeltingResultCodec().fieldOf("result").forGetter(kilnRecipe -> kilnRecipe.output),
                                    Codec.FLOAT.fieldOf("experience").orElse(0.0F).forGetter(kilnRecipe -> kilnRecipe.experience),
                                    Codec.INT.fieldOf("cookingtime").orElse(cookTime).forGetter(kilnRecipe -> kilnRecipe.cookTime)
                            )
                            .apply(r, factory::create)

            );
        }

        @Override
        @Nonnull
        public Codec<T> codec() {
            return codec;
        }
        @Override
        @Nonnull
        public T fromNetwork(@Nonnull FriendlyByteBuf buffer) {
            Ingredient input = Ingredient.fromNetwork(buffer);
            ItemStack output = buffer.readItem();
            float experience = buffer.readFloat();
            int cookTime = buffer.readVarInt();
            return this.factory.create(input, output, experience, cookTime);
        }

        @Override
        public void toNetwork(@Nonnull FriendlyByteBuf buffer, T recipe) {
            recipe.input.toNetwork(buffer);
            buffer.writeItem(recipe.output);
            buffer.writeFloat(recipe.experience);
            buffer.writeVarInt(recipe.cookTime);
        }

        public interface IFactory<T extends KilnRecipe> {
            T create(Ingredient input, @Nonnull ItemStack output, float experience, int cookTime);
        }
    }
}
package com.teammetallurgy.atum.api.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nonnull;

public abstract class RotationRecipe<C extends Container> extends AbstractAtumRecipe<C> { //TODO Move input Inredient to "SizedIngredient" in 1.20.5
    protected final int rotations;

    public RotationRecipe(RecipeType<?> recipeType, Ingredient input, @Nonnull ItemStack output, int rotations) {
        super(recipeType, input, output);
        this.rotations = rotations;
    }

    public int getRotations() {
        return this.rotations;
    }

    public static class Serializer<C extends RotationRecipe<? extends Container>> implements RecipeSerializer<C> {
        private final Serializer.IFactory<C> factory;
        public final Codec<C> codec;

        public Serializer(Serializer.IFactory<C> factory) {
            this.factory = factory;
            this.codec = RecordCodecBuilder.create(
                    r -> r.group(
                                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(rotationRecipe -> rotationRecipe.input),
                                    ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("result").forGetter(rotationRecipe -> rotationRecipe.output),
                                    Codec.INT.fieldOf("rotations").orElse(1).forGetter(rotationRecipe -> rotationRecipe.rotations)
                            )
                            .apply(r, factory::create)

            );
        }

        @Override
        @Nonnull
        public Codec<C> codec() {
            return codec;
        }

        @Override
        @Nonnull
        public C fromNetwork(@Nonnull FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack stack = buffer.readItem();
            int rotations = buffer.readInt();
            return this.factory.create(ingredient, stack, rotations);
        }

        @Override
        public void toNetwork(@Nonnull FriendlyByteBuf buffer, C recipe) {
            recipe.input.toNetwork(buffer);
            buffer.writeItem(recipe.output);
            buffer.writeInt(recipe.rotations);
        }

        public interface IFactory<T extends RotationRecipe<? extends Container>> {
            T create(Ingredient input, @Nonnull ItemStack output, int rotations);
        }
    }
}
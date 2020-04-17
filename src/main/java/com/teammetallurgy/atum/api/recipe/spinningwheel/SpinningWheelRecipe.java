package com.teammetallurgy.atum.api.recipe.spinningwheel;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.recipe.AbstractAtumRecipe;
import com.teammetallurgy.atum.api.recipe.IAtumRecipeType;
import com.teammetallurgy.atum.blocks.machines.tileentity.SpinningWheelTileEntity;
import com.teammetallurgy.atum.init.AtumRecipeSerializers;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.Tag;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class SpinningWheelRecipe extends AbstractAtumRecipe<SpinningWheelTileEntity> {
    private final int rotations;

    public SpinningWheelRecipe(Block input, @Nonnull ItemStack output, int rotations) {
        this(new ItemStack(input), output, rotations);
    }

    public SpinningWheelRecipe(Item input, @Nonnull ItemStack output, int rotations) {
        this(new ItemStack(input), output, rotations);
    }

    public SpinningWheelRecipe(@Nonnull ItemStack input, @Nonnull ItemStack output, int rotations) {
        this(Ingredient.fromStacks(input), output, rotations);
    }

    public SpinningWheelRecipe(Tag<Item> input, @Nonnull ItemStack output, int rotations) {
        this(Ingredient.fromTag(input), output, rotations);
    }

    public SpinningWheelRecipe(Ingredient input, @Nonnull ItemStack output, int rotations) {
        this(new ResourceLocation(Atum.MOD_ID, "spinning_wheel"), input, output, rotations);
    }

    public SpinningWheelRecipe(ResourceLocation id, Ingredient input, @Nonnull ItemStack output, int rotations) {
        super(IAtumRecipeType.SPINNING_WHEEL, id, input, output);
        this.rotations = rotations;
    }

    public int getRotations() {
        return this.rotations;
    }

    @Override
    @Nonnull
    public IRecipeSerializer<?> getSerializer() {
        return AtumRecipeSerializers.SPINNING_WHEEL_SERIALIZER;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SpinningWheelRecipe> {

        @Override
        @Nonnull
        public SpinningWheelRecipe read(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
            JsonObject inputObject = JSONUtils.getJsonObject(json, "ingredient");
            Ingredient input;
            if (inputObject.has("tag")) { //Only read as Ingredient directly, when it's a tag
                Ingredient ingredient = CraftingHelper.getIngredient(inputObject);
                if (inputObject.has("count")) {
                    List<ItemStack> ingredientStacks = new ArrayList<>();
                    for (ItemStack stack : ingredient.getMatchingStacks()) {
                        ingredientStacks.add(new ItemStack(stack.getItem(), JSONUtils.getInt(inputObject, "count", 1)));
                    }
                    input = Ingredient.fromStacks(ingredientStacks.toArray(new ItemStack[0]));
                } else {
                    input = ingredient;
                }
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
            return new SpinningWheelRecipe(id, input, output, rotations);
        }

        @Override
        public SpinningWheelRecipe read(@Nonnull ResourceLocation id, @Nonnull PacketBuffer buffer) {
            Ingredient ingredient = Ingredient.read(buffer);
            ItemStack stack = buffer.readItemStack();
            int rotations = buffer.readInt();
            return new SpinningWheelRecipe(id, ingredient, stack, rotations);
        }

        @Override
        public void write(@Nonnull PacketBuffer buffer, SpinningWheelRecipe recipe) {
            recipe.input.write(buffer);
            buffer.writeItemStack(recipe.output);
            buffer.writeInt(recipe.rotations);
        }
    }
}
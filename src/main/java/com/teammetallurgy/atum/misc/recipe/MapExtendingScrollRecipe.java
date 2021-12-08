package com.teammetallurgy.atum.misc.recipe;

import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumRecipeSerializers;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import javax.annotation.Nonnull;

public class MapExtendingScrollRecipe extends ShapedRecipe {

    public MapExtendingScrollRecipe(ResourceLocation location) {
        super(location, "", 3, 3, NonNullList.of(Ingredient.EMPTY, Ingredient.of(AtumItems.SCROLL), Ingredient.of(AtumItems.SCROLL), Ingredient.of(AtumItems.SCROLL), Ingredient.of(AtumItems.SCROLL), Ingredient.of(Items.FILLED_MAP), Ingredient.of(AtumItems.SCROLL), Ingredient.of(AtumItems.SCROLL), Ingredient.of(AtumItems.SCROLL), Ingredient.of(AtumItems.SCROLL)), new ItemStack(Items.MAP));
    }

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return AtumRecipeSerializers.MAP_EXTENDING_SCROLL;
    }

    @Override
    public boolean matches(@Nonnull CraftingContainer inv, @Nonnull Level world) {
        if (!super.matches(inv, world)) {
            return false;
        } else {
            ItemStack stack = ItemStack.EMPTY;

            for (int i = 0; i < inv.getContainerSize() && stack.isEmpty(); ++i) {
                ItemStack slotStack = inv.getItem(i);

                if (slotStack.getItem() == Items.FILLED_MAP) {
                    stack = slotStack;
                }
            }
            if (stack.isEmpty()) {
                return false;
            } else {
                MapItemSavedData mapdata = MapItem.getSavedData(stack, world);

                if (mapdata == null) {
                    return false;
                } else if (mapdata.isExplorationMap()) {
                    return false;
                } else {
                    return mapdata.scale < 4;
                }
            }
        }
    }

    @Override
    @Nonnull
    public ItemStack assemble(@Nonnull CraftingContainer container) {
        ItemStack stack = ItemStack.EMPTY;

        for(int i = 0; i < container.getContainerSize() && stack.isEmpty(); ++i) {
            ItemStack containerStack = container.getItem(i);
            if (containerStack.is(Items.FILLED_MAP)) {
                stack = containerStack;
            }
        }

        stack = stack.copy();
        stack.setCount(1);
        stack.getOrCreateTag().putInt("map_scale_direction", 1);
        return stack;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
}
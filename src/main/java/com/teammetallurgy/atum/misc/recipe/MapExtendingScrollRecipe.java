package com.teammetallurgy.atum.misc.recipe;

import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumRecipeSerializers;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;

import javax.annotation.Nonnull;

public class MapExtendingScrollRecipe extends ShapedRecipe {

    public MapExtendingScrollRecipe(ResourceLocation location) {
        super(location, "", 3, 3, NonNullList.from(Ingredient.EMPTY, Ingredient.fromItems(AtumItems.SCROLL), Ingredient.fromItems(AtumItems.SCROLL), Ingredient.fromItems(AtumItems.SCROLL), Ingredient.fromItems(AtumItems.SCROLL), Ingredient.fromItems(Items.FILLED_MAP), Ingredient.fromItems(AtumItems.SCROLL), Ingredient.fromItems(AtumItems.SCROLL), Ingredient.fromItems(AtumItems.SCROLL), Ingredient.fromItems(AtumItems.SCROLL)), new ItemStack(Items.MAP));
    }

    @Override
    @Nonnull
    public IRecipeSerializer<?> getSerializer() {
        return AtumRecipeSerializers.MAP_EXTENDING_SCROLL_SERIALIZER;
    }

    @Override
    public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World world) {
        if (!super.matches(inv, world)) {
            return false;
        } else {
            ItemStack stack = ItemStack.EMPTY;

            for (int i = 0; i < inv.getSizeInventory() && stack.isEmpty(); ++i) {
                ItemStack slotStack = inv.getStackInSlot(i);

                if (slotStack.getItem() == Items.FILLED_MAP) {
                    stack = slotStack;
                }
            }
            if (stack.isEmpty()) {
                return false;
            } else {
                MapData mapdata = FilledMapItem.getMapData(stack, world);

                if (mapdata == null) {
                    return false;
                } else if (this.isExplorationMap(mapdata)) {
                    return false;
                } else {
                    return mapdata.scale < 4;
                }
            }
        }
    }

    private boolean isExplorationMap(MapData mapData) {
        if (mapData.mapDecorations != null) {
            for (MapDecoration decoration : mapData.mapDecorations.values()) {
                if (decoration.getType() == MapDecoration.Type.MANSION || decoration.getType() == MapDecoration.Type.MONUMENT) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    @Nonnull
    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack stack = ItemStack.EMPTY;

        for (int i = 0; i < inv.getSizeInventory() && stack.isEmpty(); ++i) {
            ItemStack slotStack = inv.getStackInSlot(i);
            if (slotStack.getItem() == Items.FILLED_MAP) {
                stack = slotStack;
            }
        }
        stack = stack.copy();
        stack.setCount(1);

        if (stack.getTag() == null) {
            stack.setTag(new CompoundNBT());
        }
        stack.getTag().putInt("map_scale_direction", 1);
        return stack;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }
}
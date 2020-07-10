package com.teammetallurgy.atum.misc.recipe;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumRecipeSerializers;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class DisenchantRecipe extends SpecialRecipe {

    public DisenchantRecipe(ResourceLocation location) {
        super(location);
    }

    @Override
    @Nonnull
    public IRecipeSerializer<?> getSerializer() {
        return AtumRecipeSerializers.DISENCHANT;
    }

    @Override
    public boolean matches(@Nonnull CraftingInventory crafting, @Nonnull World world) {
        ItemStack stack = ItemStack.EMPTY;
        List<ItemStack> list = Lists.newArrayList();

        for (int i = 0; i < crafting.getSizeInventory(); ++i) {
            ItemStack craftingStack = crafting.getStackInSlot(i);

            if (!craftingStack.isEmpty()) {
                if (craftingStack.isEnchanted()) {
                    if (!stack.isEmpty()) {
                        return false;
                    }
                    stack = craftingStack;
                } else {
                    if (craftingStack.getItem() != AtumItems.DISENCHANTING_SCROLL) {
                        return false;
                    }
                    list.add(craftingStack);
                }
            }
        }
        return !stack.isEmpty() && list.size() == 1;
    }

    @Override
    @Nonnull
    public ItemStack getCraftingResult(@Nonnull CraftingInventory crafting) {
        ItemStack stack = ItemStack.EMPTY;
        ItemStack enchantedStack = null;

        for (int k = 0; k < crafting.getSizeInventory(); ++k) {
            ItemStack craftingStack = crafting.getStackInSlot(k);

            if (!craftingStack.isEmpty()) {
                if (craftingStack.isEnchanted()) {
                    enchantedStack = craftingStack;

                    if (!stack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    stack = craftingStack.copy();
                    stack.setCount(1);
                } else {
                    if (craftingStack.getItem() != AtumItems.DISENCHANTING_SCROLL) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }

        if (enchantedStack == null) {
            return ItemStack.EMPTY;
        } else {
            CompoundNBT tag = stack.getTag();
            if (tag != null) {
                tag.remove("Enchantments");
                return stack;
            }
            return ItemStack.EMPTY;
        }
    }

    @Override
    @Nonnull
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public NonNullList<ItemStack> getRemainingItems(@Nonnull CraftingInventory crafting) {
        return NonNullList.withSize(crafting.getSizeInventory(), ItemStack.EMPTY);
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }
}
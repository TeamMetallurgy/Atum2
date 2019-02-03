package com.teammetallurgy.atum.utils.recipe;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.List;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class RecipeDisenchant extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    public RecipeDisenchant() {
        setRegistryName(new ResourceLocation(Constants.MOD_ID, "disenchant"));
    }

    @Override
    public boolean matches(@Nonnull InventoryCrafting crafting, @Nonnull World world) {
        ItemStack stack = ItemStack.EMPTY;
        List<ItemStack> list = Lists.newArrayList();

        for (int i = 0; i < crafting.getSizeInventory(); ++i) {
            ItemStack craftingStack = crafting.getStackInSlot(i);

            if (!craftingStack.isEmpty()) {
                if (craftingStack.isItemEnchanted()) {
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
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting crafting) {
        ItemStack stack = ItemStack.EMPTY;
        ItemStack enchantedStack = null;

        for (int k = 0; k < crafting.getSizeInventory(); ++k) {
            ItemStack craftingStack = crafting.getStackInSlot(k);

            if (!craftingStack.isEmpty()) {
                if (craftingStack.isItemEnchanted()) {
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
            NBTTagCompound tag = stack.getTagCompound();
            if (tag != null) {
                tag.removeTag("ench");
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
    public NonNullList<ItemStack> getRemainingItems(@Nonnull InventoryCrafting crafting) {
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

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        event.getRegistry().register(new RecipeDisenchant());
    }
}
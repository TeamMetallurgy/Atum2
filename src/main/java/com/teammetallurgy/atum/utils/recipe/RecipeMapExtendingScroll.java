package com.teammetallurgy.atum.utils.recipe;

import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class RecipeMapExtendingScroll extends ShapedRecipes {
    
    public RecipeMapExtendingScroll() {
        super("", 3, 3, NonNullList.from(Ingredient.EMPTY, Ingredient.fromItems(AtumItems.SCROLL), Ingredient.fromItems(AtumItems.SCROLL), Ingredient.fromItems(AtumItems.SCROLL), Ingredient.fromItems(AtumItems.SCROLL), Ingredient.fromItem(Items.FILLED_MAP), Ingredient.fromItems(AtumItems.SCROLL), Ingredient.fromItems(AtumItems.SCROLL), Ingredient.fromItems(AtumItems.SCROLL), Ingredient.fromItems(AtumItems.SCROLL)), new ItemStack(Items.MAP));
        setRegistryName(new ResourceLocation(Constants.MOD_ID, "map_extending_scroll"));
    }
    
    @Override
    public boolean matches(InventoryCrafting inv, World world) {
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
                MapData mapdata = Items.FILLED_MAP.getMapData(stack, world);

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
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack stack = ItemStack.EMPTY;

        for (int i = 0; i < inv.getSizeInventory() && stack.isEmpty(); ++i) {
            ItemStack slotStack = inv.getStackInSlot(i);
            if (slotStack.getItem() == Items.FILLED_MAP) {
                stack = slotStack;
            }
        }
        stack = stack.copy();
        stack.setCount(1);

        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setInteger("map_scale_direction", 1);
        return stack;
    }
    
    @Override
    public boolean isDynamic() {
        return true;
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        event.getRegistry().register(new RecipeMapExtendingScroll());
    }
}
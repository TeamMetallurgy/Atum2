package com.teammetallurgy.atum.utils.recipe;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.items.ItemTexturedArmor;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.List;

@Mod.EventBusSubscriber
public class RecipesDesertArmorDyes extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    public RecipesDesertArmorDyes() {
        setRegistryName(new ResourceLocation(Constants.MOD_ID, "desert_armor_dyes"));
    }

    @Override
    public boolean matches(@Nonnull InventoryCrafting crafting, @Nonnull World world) {
        ItemStack stack = ItemStack.EMPTY;
        List<ItemStack> list = Lists.newArrayList();

        for (int i = 0; i < crafting.getSizeInventory(); ++i) {
            ItemStack craftingStack = crafting.getStackInSlot(i);

            if (!craftingStack.isEmpty()) {
                if (craftingStack.getItem() instanceof ItemTexturedArmor && ((ItemTexturedArmor) craftingStack.getItem()).isDyeable()) {
                    if (!stack.isEmpty()) {
                        return false;
                    }
                    stack = craftingStack;
                } else {
                    if (craftingStack.getItem() != Items.DYE) {
                        return false;
                    }
                    list.add(craftingStack);
                }
            }
        }
        return !stack.isEmpty() && !list.isEmpty();
    }

    @Override
    @Nonnull
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting crafting) {
        ItemStack stack = ItemStack.EMPTY;
        int[] aint = new int[3];
        int i = 0;
        int j = 0;
        ItemTexturedArmor armor = null;

        for (int k = 0; k < crafting.getSizeInventory(); ++k) {
            ItemStack craftingStack = crafting.getStackInSlot(k);

            if (!craftingStack.isEmpty()) {
                if (craftingStack.getItem() instanceof ItemTexturedArmor && ((ItemTexturedArmor) craftingStack.getItem()).isDyeable()) {
                    armor = (ItemTexturedArmor) craftingStack.getItem();

                    if (!stack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }

                    stack = craftingStack.copy();
                    stack.setCount(1);

                    if (armor.hasColor(craftingStack)) {
                        int l = armor.getColor(stack);
                        float f = (float) (l >> 16 & 255) / 255.0F;
                        float f1 = (float) (l >> 8 & 255) / 255.0F;
                        float f2 = (float) (l & 255) / 255.0F;
                        i = (int) ((float) i + Math.max(f, Math.max(f1, f2)) * 255.0F);
                        aint[0] = (int) ((float) aint[0] + f * 255.0F);
                        aint[1] = (int) ((float) aint[1] + f1 * 255.0F);
                        aint[2] = (int) ((float) aint[2] + f2 * 255.0F);
                        ++j;
                    }
                } else {
                    if (craftingStack.getItem() != Items.DYE) {
                        return ItemStack.EMPTY;
                    }

                    float[] afloat = EnumDyeColor.byDyeDamage(craftingStack.getMetadata()).getColorComponentValues();
                    int l1 = (int) (afloat[0] * 255.0F);
                    int i2 = (int) (afloat[1] * 255.0F);
                    int j2 = (int) (afloat[2] * 255.0F);
                    i += Math.max(l1, Math.max(i2, j2));
                    aint[0] += l1;
                    aint[1] += i2;
                    aint[2] += j2;
                    ++j;
                }
            }
        }

        if (armor == null) {
            return ItemStack.EMPTY;
        } else {
            int i1 = aint[0] / j;
            int j1 = aint[1] / j;
            int k1 = aint[2] / j;
            float f3 = (float) i / (float) j;
            float f4 = (float) Math.max(i1, Math.max(j1, k1));
            i1 = (int) ((float) i1 * f3 / f4);
            j1 = (int) ((float) j1 * f3 / f4);
            k1 = (int) ((float) k1 * f3 / f4);
            int lvt_12_3_ = (i1 << 8) + j1;
            lvt_12_3_ = (lvt_12_3_ << 8) + k1;
            armor.setColor(stack, lvt_12_3_);
            return stack;
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
        NonNullList<ItemStack> stacks = NonNullList.withSize(crafting.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < stacks.size(); ++i) {
            ItemStack stack = crafting.getStackInSlot(i);
            stacks.set(i, ForgeHooks.getContainerItem(stack));
        }
        return stacks;
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
        event.getRegistry().register(new RecipesDesertArmorDyes());
    }
}
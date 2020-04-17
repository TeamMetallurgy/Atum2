package com.teammetallurgy.atum.blocks.machines.tileentity;

import com.teammetallurgy.atum.api.recipe.IAtumRecipeType;
import com.teammetallurgy.atum.api.recipe.RecipeHandlers;
import com.teammetallurgy.atum.api.recipe.kiln.IKilnRecipe;
import com.teammetallurgy.atum.blocks.machines.KilnBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SpongeBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

import static net.minecraftforge.common.Tags.Items.*;

public class KilnTileEntity extends KilnBaseTileEntity {

    @Override
    public void tick() {
        if (!isPrimary()) {
            return;
        }

        boolean isBurning = this.isBurning();
        boolean markDirty = false;

        if (this.isBurning()) {
            int burnTime = this.furnaceData.get(0);
            this.furnaceData.set(--burnTime, 0);
        }

        if (!this.world.isRemote) {
            ItemStack fuelStack = this.items.get(4);

            if (this.isBurning() || !fuelStack.isEmpty() && !this.getInputs().isEmpty()) {
                boolean canSmeltAny = false;
                for (int i = 0; i <= 4; i++) {
                    canSmeltAny |= this.canSmelt(i, 5, 8) != -1;
                }

                if (!this.isBurning() && canSmeltAny) {
                    this.furnaceData.set(ForgeHooks.getBurnTime(fuelStack), 0);
                    this.furnaceData.set(this.furnaceData.get(0), 1);

                    if (this.isBurning()) {
                        markDirty = true;
                        if (!fuelStack.isEmpty()) {
                            Item fuelItemCached = fuelStack.getItem();
                            fuelStack.shrink(1);

                            if (fuelStack.isEmpty()) {
                                ItemStack containerStack = fuelItemCached.getContainerItem(fuelStack);
                                this.items.set(4, containerStack);
                            }
                        }
                    }
                }

                if (this.isBurning() && canSmeltAny) {
                    int cookTime = this.furnaceData.get(2);
                    this.furnaceData.set(++cookTime, 2);
                    if (this.furnaceData.get(2) == this.furnaceData.get(3)) { //cookTime == cookTimeTotal
                        this.furnaceData.set(0, 2); //cookTime
                        this.furnaceData.set(0, 3); //cookTimeTotal
                        if (!this.isInputEmpty()) {
                            this.furnaceData.set(this.getCookTime(), 3); //Set cookTimeTotal to getCookTime
                        }
                        for (int i = 0; i <= 4; i++) {
                            this.smeltItem(i, 5, 8);
                        }
                        markDirty = true;
                    }
                } else {
                    this.furnaceData.set(0, 2); //cookTime
                }
            } else if ((!this.isBurning() && this.furnaceData.get(2) > 0) || this.isInputEmpty()) {
                this.furnaceData.set(MathHelper.clamp(this.furnaceData.get(2) - 2, 0, this.furnaceData.get(3)), 2);
            }

            if (isBurning != this.isBurning()) {
                markDirty = true;
                world.setBlockState(pos, world.getBlockState(pos).with(KilnBlock.LIT, this.isBurning()));
                BlockPos secondaryKilnPos = KilnBlock.getSecondaryKilnFromPrimary(world, pos);
                if (secondaryKilnPos != null) {
                    BlockState secondaryState = world.getBlockState(secondaryKilnPos);
                    if (secondaryState.getBlock() == AtumBlocks.KILN) {
                        world.setBlockState(secondaryKilnPos, secondaryState.with(KilnBlock.LIT, this.isBurning()));
                    }
                }
            }
        }

        if (markDirty) {
            this.markDirty();
        }
    }

    private boolean isInputEmpty() {
        for (int i = 0; i <= 4; i++) {
            if (!this.items.get(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (!isPrimary()) {
            KilnBaseTileEntity primary = getPrimary();
            if (primary != null) {
                primary.setInventorySlotContents(index, stack);
            }
            return;
        }

        ItemStack slotStack = this.items.get(index);
        boolean isValid = !stack.isEmpty() && stack.isItemEqual(slotStack) && ItemStack.areItemStackTagsEqual(stack, slotStack);

        this.items.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index <= 3 && !isValid) {
            this.furnaceData.set(this.getCookTime(), 3); //cookTimeTotal
            this.markDirty();
        }
    }

    public boolean isBurning() {
        if (!isPrimary()) {
            KilnBaseTileEntity primary = getPrimary();
            if (primary != null) {
                return ((KilnTileEntity) primary).isBurning();
            }
        }
        return this.furnaceData.get(0) > 0; //burnTime
    }

    private int canSmelt(int inputSlot, int outputSlotStart, int outputSlotEnd) {
        if (this.items.get(inputSlot).isEmpty()) {
            return -1;
        } else {
            ItemStack result = this.getSmeltingResult(this.items.get(inputSlot));

            if (result.isEmpty()) {
                return -1;
            } else {
                for (int outputSlot = outputSlotStart; outputSlot <= outputSlotEnd; outputSlot++) {
                    ItemStack output = this.items.get(outputSlot);

                    if (output.isEmpty()) {
                        return outputSlot;
                    } else if (!output.isItemEqual(result)) {
                        continue;
                    } else if (output.getCount() + result.getCount() <= this.getInventoryStackLimit() && output.getCount() + result.getCount() <= output.getMaxStackSize()) {
                        return outputSlot;
                    } else {
                        if (output.getCount() + result.getCount() <= result.getMaxStackSize())
                            return outputSlot;
                    }
                }
            }
        }
        return -1;
    }

    private void smeltItem(int inputSlot, int outputSlotStart, int outputSlotEnd) {
        int outputSlot = this.canSmelt(inputSlot, outputSlotStart, outputSlotEnd);
        if (outputSlot != -1) {
            ItemStack input = this.items.get(inputSlot);
            ItemStack result = this.getSmeltingResult(input);
            ItemStack output = this.items.get(outputSlot);

            if (output.isEmpty()) {
                this.items.set(outputSlot, result.copy());
            } else if (output.getItem() == result.getItem()) {
                output.grow(result.getCount());
            }
            input.shrink(1);
        }
    }

    @Nonnull
    private ItemStack getSmeltingResult(@Nonnull ItemStack input) {
        for (IKilnRecipe kilnRecipe : RecipeHandlers.kilnRecipes) {
            if (compareItemStacks(kilnRecipe.getInput().get(0), input)) {
                return kilnRecipe.getOutput();
            }
        }
        return ItemStack.EMPTY;
    }

    private boolean compareItemStacks(@Nonnull ItemStack stack1, @Nonnull ItemStack stack2) {
        return stack2.getItem() == stack1.getItem();
    }

    private List<ItemStack> getInputs() {
        return Arrays.asList(this.items.get(0), this.items.get(1), this.items.get(2), this.items.get(3));
    }

    public static boolean canKilnNotSmelt(Ingredient ingredient) {
        for (ItemStack stack : ingredient.getMatchingStacks()) {
            return canKilnNotSmelt(stack);
        }
        return true;
    }

    public static boolean canKilnNotSmelt(ItemStack stack) {
        Item item = stack.getItem();
        Block block = Block.getBlockFromItem(stack.getItem());

        return IAtumRecipeType.kilnBlacklist.contains(item.getRegistryName()) || IAtumRecipeType.kilnBlacklist.contains(block.getRegistryName()) ||
                item.isFood() || block instanceof OreBlock || item.isIn(ItemTags.COALS) || item.isIn(ORES_COAL) || item.isIn(STORAGE_BLOCKS_COAL) ||
                item.isIn(ItemTags.PLANKS) || item.isIn(ItemTags.LOGS) || item.isIn(RODS_WOODEN) || item.isIn(ItemTags.SMALL_FLOWERS) ||
                item.isIn(ORES) || item.isIn(INGOTS) && !item.isIn(INGOTS_BRICK) || item.isIn(NUGGETS) || item.isIn(GEMS) || item.isIn(DUSTS) ||
                item.isIn(DYES) || item.isIn(SLIMEBALLS) || item.isIn(LEATHER) || block instanceof SpongeBlock;
    }
}
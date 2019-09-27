package com.teammetallurgy.atum.blocks.machines.tileentity;

import com.teammetallurgy.atum.api.recipe.RecipeHandlers;
import com.teammetallurgy.atum.api.recipe.kiln.IKilnRecipe;
import com.teammetallurgy.atum.blocks.machines.BlockKiln;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SpongeBlock;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

import static net.minecraftforge.common.Tags.Items.*;

public class KilnTileEntity extends KilnBaseTileEntity implements ITickable {
    private int burnTime;
    private int currentItemBurnTime;
    private int cookTime;
    private int totalCookTime;

    @Override
    public void update() {
        if (!isPrimary()) {
            return;
        }

        boolean isBurning = this.isBurning();
        boolean markDirty = false;

        if (this.isBurning()) {
            --this.burnTime;
        }

        if (!this.world.isRemote) {
            ItemStack fuelStack = this.inventory.get(4);

            if (this.isBurning() || !fuelStack.isEmpty() && !this.getInputs().isEmpty()) {
                boolean canSmeltAny = false;
                for (int i = 0; i <= 4; i++) {
                    canSmeltAny |= this.canSmelt(i, 5, 8) != -1;
                }

                if (!this.isBurning() && canSmeltAny) {
                    this.burnTime = TileEntityFurnace.getItemBurnTime(fuelStack);
                    this.currentItemBurnTime = this.burnTime;

                    if (this.isBurning()) {
                        markDirty = true;
                        if (!fuelStack.isEmpty()) {
                            fuelStack.shrink(1);
                        }
                    }
                }

                if (this.isBurning() && canSmeltAny) {
                    ++this.cookTime;
                    if (this.cookTime == this.totalCookTime) {
                        this.cookTime = 0;
                        this.totalCookTime = 0;
                        if (!this.isInputEmpty()) {
                            this.totalCookTime = this.getCookTime();
                        }
                        for (int i = 0; i <= 4; i++)
                            this.smeltItem(i, 5, 8);
                        markDirty = true;
                    }
                } else {
                    this.cookTime = 0;
                }
            } else if ((!this.isBurning() && this.cookTime > 0) || this.isInputEmpty()) {
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
            }

            if (isBurning != this.isBurning()) {
                markDirty = true;
                world.setBlockState(pos, world.getBlockState(pos).with(BlockKiln.IS_BURNING, this.isBurning()));
                BlockPos secondaryKilnPos = BlockKiln.getSecondaryKilnFromPrimary(world, pos);
                if (secondaryKilnPos != null) {
                    BlockState secondaryState = world.getBlockState(secondaryKilnPos);
                    if (secondaryState.getBlock() == AtumBlocks.KILN) {
                        world.setBlockState(secondaryKilnPos, secondaryState.with(BlockKiln.IS_BURNING, this.isBurning()));
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
            if (!this.inventory.get(i).isEmpty()) {
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

        ItemStack slotStack = this.inventory.get(index);
        boolean isValid = !stack.isEmpty() && stack.isItemEqual(slotStack) && ItemStack.areItemStackTagsEqual(stack, slotStack);

        this.fillWithLoot(null);
        this.getItems().set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index <= 3 && !isValid) {
            this.totalCookTime = this.getCookTime();
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
        return this.burnTime > 0;
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean isBurning(IInventory inventory) {
        return inventory.getField(0) > 0;
    }

    private int getCookTime() {
        return 200;
    }

    private int canSmelt(int inputSlot, int outputSlotStart, int outputSlotEnd) {
        if (this.inventory.get(inputSlot).isEmpty()) {
            return -1;
        } else {
            ItemStack result = this.getSmeltingResult(this.inventory.get(inputSlot));

            if (result.isEmpty()) {
                return -1;
            } else {
                for (int outputSlot = outputSlotStart; outputSlot <= outputSlotEnd; outputSlot++) {
                    ItemStack output = this.inventory.get(outputSlot);

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
            ItemStack input = this.inventory.get(inputSlot);
            ItemStack result = this.getSmeltingResult(input);
            ItemStack output = this.inventory.get(outputSlot);

            if (output.isEmpty()) {
                this.inventory.set(outputSlot, result.copy());
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
        return stack2.getItem() == stack1.getItem() && (stack1.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
    }

    private List<ItemStack> getInputs() {
        return Arrays.asList(this.inventory.get(0), this.inventory.get(1), this.inventory.get(2), this.inventory.get(3));
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

        return RecipeHandlers.kilnBlacklist.contains(item.getRegistryName()) || RecipeHandlers.kilnBlacklist.contains(block.getRegistryName()) ||
                item.isFood() || block instanceof OreBlock || item.isIn(ItemTags.COALS) || item.isIn(ORES_COAL) || item.isIn(STORAGE_BLOCKS_COAL) ||
                item.isIn(ItemTags.PLANKS) || item.isIn(ItemTags.LOGS) || item.isIn(RODS_WOODEN) || item.isIn(ItemTags.SMALL_FLOWERS) ||
                item.isIn(ORES) || item.isIn(INGOTS) && !item.isIn(INGOTS_BRICK) || item.isIn(NUGGETS) || item.isIn(GEMS) || item.isIn(DUSTS) ||
                item.isIn(DYES) || item.isIn(SLIMEBALLS) || item.isIn(LEATHER) || block instanceof SpongeBlock;
    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0:
                return this.burnTime;
            case 1:
                return this.currentItemBurnTime;
            case 2:
                return this.cookTime;
            case 3:
                return this.totalCookTime;
            default:
                return 0;
        }
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0:
                this.burnTime = value;
                break;
            case 1:
                this.currentItemBurnTime = value;
                break;
            case 2:
                this.cookTime = value;
                break;
            case 3:
                this.totalCookTime = value;
        }
    }

    @Override
    public int getFieldCount() {
        return 4;
    }

    @Override
    public void readFromNBT(CompoundNBT compound) {
        super.readFromNBT(compound);
        this.burnTime = compound.getInt("BurnTime");
        this.cookTime = compound.getInt("CookTime");
        this.totalCookTime = compound.getInt("CookTimeTotal");
        this.currentItemBurnTime = TileEntityFurnace.getItemBurnTime(this.inventory.get(4));
    }

    @Override
    @Nonnull
    public CompoundNBT writeToNBT(CompoundNBT compound) {
        super.writeToNBT(compound);
        compound.putInt("BurnTime", (short) this.burnTime);
        compound.putInt("CookTime", (short) this.cookTime);
        compound.putInt("CookTimeTotal", (short) this.totalCookTime);
        return compound;
    }
}
package com.teammetallurgy.atum.blocks.machines.tileentity;

import com.teammetallurgy.atum.api.recipe.RecipeHandlers;
import com.teammetallurgy.atum.api.recipe.kiln.IKilnRecipe;
import com.teammetallurgy.atum.blocks.machines.BlockKiln;
import com.teammetallurgy.atum.init.AtumBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockSponge;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemCoal;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class TileEntityKiln extends TileEntityKilnBase implements ITickable {
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
                BlockPos secondaryKilnPos = BlockKiln.getSecondaryKilnFromPrimary(world, pos);
                if (secondaryKilnPos != null) {
                	IBlockState secondaryState = world.getBlockState(secondaryKilnPos);
                	if(secondaryState.getBlock() == AtumBlocks.KILN)
                		world.setBlockState(secondaryKilnPos, secondaryState.withProperty(BlockKiln.IS_BURNING, this.isBurning()));
                	else
                		System.out.println("Secondary Kiln block is " + secondaryState.getBlock() + " at " + secondaryKilnPos);
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
            getPrimary().setInventorySlotContents(index, stack);
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
            return ((TileEntityKiln) getPrimary()).isBurning();
        }
        return this.burnTime > 0;
    }

    @SideOnly(Side.CLIENT)
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

    public static boolean canKilnNotSmelt(@Nonnull ItemStack stack) {
        Item item = stack.getItem();
        Block block = Block.getBlockFromItem(stack.getItem());

        String oreName = "";
        for (int oreId : OreDictionary.getOreIDs(stack)) {
            oreName = OreDictionary.getOreName(oreId).toLowerCase(Locale.ENGLISH);
        }
        return RecipeHandlers.kilnBlacklist.contains(item.getRegistryName()) || RecipeHandlers.kilnBlacklist.contains(block.getRegistryName()) ||
                item instanceof ItemFood || block instanceof BlockOre || item instanceof ItemCoal ||
                oreName.contains("plank") || oreName.contains("log") || oreName.contains("stick") || oreName.contains("torch") || oreName.contains("plant") || oreName.contains("sugarcane") ||
                oreName.contains("ore") || oreName.contains("ingot") && !oreName.contains("ingotbrick") || oreName.contains("nugget") || oreName.contains("gem") || oreName.contains("dust") || oreName.contains("crushed") ||
                oreName.contains("dye") || oreName.contains("slime") || oreName.contains("leather") || oreName.contains("rubber") || block instanceof BlockSponge;
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
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.burnTime = compound.getInteger("BurnTime");
        this.cookTime = compound.getInteger("CookTime");
        this.totalCookTime = compound.getInteger("CookTimeTotal");
        this.currentItemBurnTime = TileEntityFurnace.getItemBurnTime(this.inventory.get(4));
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("BurnTime", (short) this.burnTime);
        compound.setInteger("CookTime", (short) this.cookTime);
        compound.setInteger("CookTimeTotal", (short) this.totalCookTime);
        return compound;
    }
}
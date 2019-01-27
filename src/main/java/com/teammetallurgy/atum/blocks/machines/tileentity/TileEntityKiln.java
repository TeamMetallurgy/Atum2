package com.teammetallurgy.atum.blocks.machines.tileentity;

import com.teammetallurgy.atum.api.recipe.RecipeHandlers;
import com.teammetallurgy.atum.api.recipe.kiln.IKilnRecipe;
import com.teammetallurgy.atum.blocks.machines.BlockKiln;
import com.teammetallurgy.atum.utils.StackHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockSponge;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemCoal;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ITickable;
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
    public void update() { //TODO
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
                if (!this.isBurning() && this.canSmelt()) {
                    this.burnTime = TileEntityFurnace.getItemBurnTime(fuelStack);
                    this.currentItemBurnTime = this.burnTime;

                    if (this.isBurning()) {
                        markDirty = true;

                        if (!fuelStack.isEmpty()) {
                            Item item = fuelStack.getItem();
                            fuelStack.shrink(1);

                            if (fuelStack.isEmpty()) {
                                ItemStack item1 = item.getContainerItem(fuelStack);
                                this.inventory.set(4, item1);
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canSmelt()) {
                    ++this.cookTime;

                    if (this.cookTime == this.totalCookTime) {
                        this.cookTime = 0;
                        this.totalCookTime = this.getCookTime(this.inventory.get(0));
                        this.smeltItem();
                        markDirty = true;
                    }
                } else {
                    this.cookTime = 0;
                }
            } else if (!this.isBurning() && this.cookTime > 0) {
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
            }

            if (isBurning != this.isBurning()) {
                markDirty = true;
                world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockKiln.IS_BURNING, this.isBurning()));
            }
        }
        if (markDirty) {
            this.markDirty();
        }
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (!isPrimary()) {
            getPrimary().setInventorySlotContents(index, stack);
            return;
        }

        super.setInventorySlotContents(index, stack);
        ItemStack slotStack = this.inventory.get(index);
        boolean isValid = !stack.isEmpty() && stack.isItemEqual(slotStack) && ItemStack.areItemStackTagsEqual(stack, slotStack);

        if (index == 0 && !isValid) {
            this.totalCookTime = this.getCookTime(stack);
            this.cookTime = 0;
            this.markDirty();
        }
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

    public int getCookTime(@Nonnull ItemStack stack) {
        return 200;
    }

    private boolean canSmelt() {
        if (this.getInputs().isEmpty()) {
            return false;
        } else {
            ItemStack smeltingResult = this.getSmeltingResult(this.getInputs());

            if (smeltingResult.isEmpty()) {
                return false;
            } else {
                for (ItemStack output : this.getOutputs()) {
                    if (output.isEmpty()) {
                        return true;
                    } else if (!output.isItemEqual(smeltingResult)) {
                        return false;
                    } else if (output.getCount() + smeltingResult.getCount() <= this.getInventoryStackLimit() && output.getCount() + smeltingResult.getCount() <= output.getMaxStackSize()) {
                        return true;
                    } else {
                        return output.getCount() + smeltingResult.getCount() <= smeltingResult.getMaxStackSize();
                    }
                }
                return false;
            }
        }
    }

    public void smeltItem() { //TODO
        if (this.canSmelt()) {
            List<ItemStack> inputs = this.getInputs();
            List<ItemStack> outputs = Arrays.asList(this.inventory.get(5), this.inventory.get(6), this.inventory.get(7), this.inventory.get(8));

            ItemStack smeltingResult = getSmeltingResult(inputs);
            for (ItemStack output : outputs) {
                for (int index = 0; index < outputs.size(); index++) {
                    if (output.isEmpty()) {
                        this.inventory.set(5, smeltingResult.copy());
                    } else if (output.getItem() == smeltingResult.getItem()) {
                        output.grow(smeltingResult.getCount());
                    }
                }
            }

            if (inputs.contains(new ItemStack(Item.getItemFromBlock(Blocks.SPONGE), 1, 1)) && !this.inventory.get(4).isEmpty() && this.inventory.get(4).getItem() == Items.BUCKET) {
                this.inventory.set(4, new ItemStack(Items.WATER_BUCKET));
            }

            for (ItemStack input : inputs) {
                if (!input.isEmpty()) {
                    input.shrink(1);
                }
            }
        }
    }

    @Nonnull
    private ItemStack getSmeltingResult(List<ItemStack> inputs) {
        for (ItemStack input : inputs) {
            for (IKilnRecipe kilnRecipe : RecipeHandlers.kilnRecipes) {
                if (StackHelper.areStacksEqualIgnoreSize(kilnRecipe.getInput().get(0), input)) { //TODO Maybe
                    return kilnRecipe.getOutput();
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Nonnull
    private ItemStack getOutput(List<ItemStack> outputs) {
        for (ItemStack output : outputs) {
            for (IKilnRecipe kilnRecipe : RecipeHandlers.kilnRecipes) {
                if (StackHelper.areStacksEqualIgnoreSize(kilnRecipe.getOutput(), output)) { //TODO Maybe
                    return kilnRecipe.getOutput();
                }
            }
        }
        return ItemStack.EMPTY;
    }

    private List<ItemStack> getInputs() {
        return Arrays.asList(this.inventory.get(0), this.inventory.get(1), this.inventory.get(2), this.inventory.get(3));
    }

    private List<ItemStack> getOutputs() {
        return Arrays.asList(this.inventory.get(5), this.inventory.get(6), this.inventory.get(7), this.inventory.get(8));
    }

    public static boolean canKilnNotSmelt(@Nonnull ItemStack stack) {
        Item item = stack.getItem();
        Block block = Block.getBlockFromItem(stack.getItem());

        String oreName = "";
        for (int oreId : OreDictionary.getOreIDs(stack)) {
            oreName = OreDictionary.getOreName(oreId).toLowerCase(Locale.ENGLISH);
        }
        return item instanceof ItemFood || block instanceof BlockOre || item instanceof ItemCoal ||
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
}
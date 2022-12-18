package com.teammetallurgy.atum.inventory.container.block;

import com.teammetallurgy.atum.api.recipe.AtumRecipeTypes;
import com.teammetallurgy.atum.blocks.machines.tileentity.KilnTileEntity;
import com.teammetallurgy.atum.init.AtumMenuType;
import com.teammetallurgy.atum.inventory.container.slot.FuelSlot;
import com.teammetallurgy.atum.inventory.container.slot.KilnOutputSlot;
import com.teammetallurgy.atum.misc.recipe.RecipeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

import javax.annotation.Nonnull;

public class KilnContainer extends AbstractContainerMenu {
    public KilnTileEntity kilnInventory;
    private ContainerData kilnData;

    public KilnContainer(int windowID, Inventory playerInventory, BlockPos pos) {
        super(AtumMenuType.KILN.get(), windowID);
        Player player = playerInventory.player;
        this.kilnInventory = (KilnTileEntity) player.level.getBlockEntity(pos);
        checkContainerSize(kilnInventory, kilnInventory.getContainerSize());
        ContainerData kilnData = kilnInventory.kilnData;
        checkContainerDataCount(kilnData, kilnData.getCount());
        this.kilnData = kilnData;
        //Input Slots
        for (int row = 0; row < 2; ++row) {
            for (int slot = 0; slot < 2; ++slot) {
                this.addSlot(new Slot(kilnInventory, slot + row * 2, 71 + slot * 18, 15 + row * 18));
            }
        }
        //Fuel Slot
        this.addSlot(new FuelSlot(kilnInventory, 4, 36, 48));
        //Output Slots
        for (int row = 0; row < 2; ++row) {
            for (int slot = 0; slot < 2; ++slot) {
                this.addSlot(new KilnOutputSlot(player, kilnInventory, 5 + (slot + row * 2), 71 + slot * 18, 63 + row * 18));
            }
        }
        //Player Inventory
        for (int row = 0; row < 3; ++row) {
            for (int slot = 0; slot < 9; ++slot) {
                this.addSlot(new Slot(playerInventory, slot + row * 9 + 9, 8 + slot * 18, 84 + row * 18 + 26));
            }
        }
        //Player Hotbar
        for (int slot = 0; slot < 9; ++slot) {
            this.addSlot(new Slot(playerInventory, slot, 8 + slot * 18, 142 + 26));
        }

        this.addDataSlots(kilnData);
    }

    @Override
    @Nonnull
    public ItemStack quickMoveStack(@Nonnull Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        int slotFuel = 4;
        int internalStart = 0;
        int internalEnd = 3;
        int outputStart = 5;
        int outputEnd = 8;
        int playerStart = 9;
        int playerEnd = 35;
        int hotbarStart = 36;
        int hotbarEnd = 44;

        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index == slotFuel) {
                if (!this.moveItemStackTo(slotStack, playerStart, hotbarEnd + 1, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, stack);
            } else if ((index < internalStart || index > internalEnd) && (index < outputStart || index > outputEnd)) {
                if (getRecipe(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, internalStart, internalEnd + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (AbstractFurnaceBlockEntity.isFuel(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, slotFuel, slotFuel + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= playerStart && index <= playerEnd) {
                    if (!this.moveItemStackTo(slotStack, hotbarStart, hotbarEnd + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= hotbarStart && index <= hotbarEnd && !this.moveItemStackTo(slotStack, playerStart, playerEnd + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, playerStart, hotbarEnd + 1, false)) {
                return ItemStack.EMPTY;
            }
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, slotStack);
        }
        return stack;
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        return this.kilnInventory.stillValid(player);
    }

    protected boolean getRecipe(@Nonnull ItemStack stack) {
        Level world = this.kilnInventory.getLevel();
        RecipeManager recipeManager = world.getRecipeManager();
        return recipeManager.getRecipeFor((RecipeType) AtumRecipeTypes.KILN.get(), new SimpleContainer(stack), world).isPresent() || RecipeHelper.isValidRecipeInput(RecipeHelper.getKilnRecipesFromFurnace(recipeManager), stack);
    }

    public int getCookProgressionScaled() {
        int cookTime = this.kilnData.get(2);
        int cookTimeTotal = this.kilnData.get(3);
        return cookTimeTotal != 0 && cookTime != 0 ? cookTime * 8 / cookTimeTotal : 0;
    }

    public int getBurnLeftScaled() {
        int recipesUsed = this.kilnData.get(1);
        if (recipesUsed == 0) {
            recipesUsed = 200;
        }

        return this.kilnData.get(0) * 13 / recipesUsed;
    }

    public boolean isBurning() {
        return this.kilnData.get(0) > 0;
    }
}
package com.teammetallurgy.atum.blocks.machines.tileentity;

import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GlassblowerFurnaceTileEntity extends AbstractFurnaceBlockEntity {

    public GlassblowerFurnaceTileEntity() {
        super(AtumTileEntities.GLASSBLOWER_FURNACE, RecipeType.SMELTING);
    }

    @Override
    @Nonnull
    protected Component getDefaultName() {
        return new TranslatableComponent("atum.container.glassblower_furnace");
    }

    @Override
    @Nonnull
    protected AbstractContainerMenu createMenu(int id, @Nonnull Inventory player) {
        return new FurnaceMenu(id, player, this, this.dataAccess);
    }

    public int getGlassBlowerCookTime(@Nonnull ItemStack output, boolean fromTick) {
        int cookTime = super.getTotalCookTime();
        return isGlassOutput(output) ? cookTime / 4 : cookTime;
    }

    public boolean isGlassOutput(@Nonnull ItemStack output) {
        Item item = output.getItem();
        return Tags.Items.GLASS.contains(item);
    }

    @Override
    public void tick() {
        boolean flag = this.isBurning();
        boolean flag1 = false;
        if (this.isBurning()) {
            --this.litTime;
        }

        if (!this.level.isClientSide) {
            ItemStack itemstack = this.items.get(1);
            if (this.isBurning() || !itemstack.isEmpty() && !this.items.get(0).isEmpty()) {
                Recipe<?> irecipe = this.level.getRecipeManager().getRecipeFor((RecipeType<AbstractCookingRecipe>) this.recipeType, this, this.level).orElse(null);
                if (!this.isBurning() && this.canBurn(irecipe)) {
                    this.litTime = this.getBurnDuration(itemstack);
                    this.litDuration = this.litTime;
                    if (this.isBurning()) {
                        flag1 = true;
                        if (itemstack.hasContainerItem())
                            this.items.set(1, itemstack.getContainerItem());
                        else if (!itemstack.isEmpty()) {
                            itemstack.shrink(1);
                            if (itemstack.isEmpty()) {
                                this.items.set(1, itemstack.getContainerItem());
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canBurn(irecipe)) {
                    ++this.cookingProgress;
                    if (this.cookingProgress == this.cookingTotalTime) {
                        this.cookingProgress = 0;
                        this.cookingTotalTime = this.getGlassBlowerCookTime(irecipe.getResultItem(), true);
                        this.smelt(irecipe);
                        flag1 = true;
                    }
                } else {
                    this.cookingProgress = 0;
                }
            } else if (!this.isBurning() && this.cookingProgress > 0) {
                this.cookingProgress = Mth.clamp(this.cookingProgress - 2, 0, this.cookingTotalTime);
            }

            if (flag != this.isBurning()) {
                flag1 = true;
                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(AbstractFurnaceBlock.LIT, this.isBurning()), 3);
            }
        }

        if (flag1) {
            this.setChanged();
        }
    }

    private boolean isBurning() {
        return this.litTime > 0;
    }

    @Override
    public void setItem(int index, @Nonnull ItemStack stack) {
        ItemStack slotStack = this.items.get(index);
        boolean flag = !stack.isEmpty() && stack.sameItem(slotStack) && ItemStack.tagMatches(stack, slotStack);
        this.items.set(index, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }

        if (index == 0 && !flag) {
            this.cookingTotalTime = this.getGlassBlowerCookTime(slotStack, false);
            this.cookingProgress = 0;
            this.setChanged();
        }
    }

    //Copied from AbstractFurnaceTileEntity
    private void smelt(@Nullable Recipe<?> recipe) {
        if (recipe != null && this.canBurn(recipe)) {
            ItemStack itemstack = this.items.get(0);
            ItemStack itemstack1 = recipe.getResultItem();
            ItemStack itemstack2 = this.items.get(2);
            if (itemstack2.isEmpty()) {
                this.items.set(2, itemstack1.copy());
            } else if (itemstack2.getItem() == itemstack1.getItem()) {
                itemstack2.grow(itemstack1.getCount());
            }

            if (!this.level.isClientSide) {
                this.setRecipeUsed(recipe);
            }

            if (itemstack.getItem() == Blocks.WET_SPONGE.asItem() && !this.items.get(1).isEmpty() && this.items.get(1).getItem() == Items.BUCKET) {
                this.items.set(1, new ItemStack(Items.WATER_BUCKET));
            }
            itemstack.shrink(1);
        }
    }
}
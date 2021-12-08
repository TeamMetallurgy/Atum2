package com.teammetallurgy.atum.blocks.machines.tileentity;

import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GlassblowerFurnaceTileEntity extends AbstractFurnaceBlockEntity {

    public GlassblowerFurnaceTileEntity(BlockPos pos, BlockState state) {
        super(AtumTileEntities.GLASSBLOWER_FURNACE.get(), pos, state, RecipeType.SMELTING);
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

    public int getGlassBlowerCookTime(Level level, @Nonnull ItemStack output, Container container) {
        int cookTime = getTotalCookTime(level, RecipeType.SMELTING, container);
        return isGlassOutput(output) ? cookTime / 4 : cookTime;
    }

    public boolean isGlassOutput(@Nonnull ItemStack output) {
        Item item = output.getItem();
        return Tags.Items.GLASS.contains(item);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, GlassblowerFurnaceTileEntity glassblower) {
        boolean flag = glassblower.isBurning();
        boolean flag1 = false;
        if (glassblower.isBurning()) {
            --glassblower.litTime;
        }

        if (!level.isClientSide) {
            ItemStack itemstack = glassblower.items.get(1);
            if (glassblower.isBurning() || !itemstack.isEmpty() && !this.items.get(0).isEmpty()) {
                Recipe<?> irecipe = level.getRecipeManager().getRecipeFor((RecipeType<AbstractCookingRecipe>) this.recipeType, glassblower, level).orElse(null);
                if (!glassblower.isBurning() && glassblower.canBurn(irecipe)) {
                    glassblower.litTime = glassblower.getBurnDuration(itemstack);
                    glassblower.litDuration = glassblower.litTime;
                    if (glassblower.isBurning()) {
                        flag1 = true;
                        if (itemstack.hasContainerItem())
                            glassblower.items.set(1, itemstack.getContainerItem());
                        else if (!itemstack.isEmpty()) {
                            itemstack.shrink(1);
                            if (itemstack.isEmpty()) {
                                glassblower.items.set(1, itemstack.getContainerItem());
                            }
                        }
                    }
                }

                if (glassblower.isBurning() && glassblower.canBurn(irecipe)) {
                    ++glassblower.cookingProgress;
                    if (glassblower.cookingProgress == glassblower.cookingTotalTime) {
                        glassblower.cookingProgress = 0;
                        glassblower.cookingTotalTime = glassblower.getGlassBlowerCookTime(irecipe.getResultItem(), true);
                        glassblower.smelt(irecipe);
                        flag1 = true;
                    }
                } else {
                    glassblower.cookingProgress = 0;
                }
            } else if (!glassblower.isBurning() && glassblower.cookingProgress > 0) {
                glassblower.cookingProgress = Mth.clamp(glassblower.cookingProgress - 2, 0, glassblower.cookingTotalTime);
            }

            if (flag != glassblower.isBurning()) {
                flag1 = true;
                glassblower.level.setBlock(glassblower.worldPosition, state.setValue(AbstractFurnaceBlock.LIT, glassblower.isBurning()), 3);
            }
        }

        if (flag1) {
            glassblower.setChanged();
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
package com.teammetallurgy.atum.blocks.machines.tileentity;

import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
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
    private final RecipeManager.CachedCheck<Container, ? extends AbstractCookingRecipe> quickCheck;

    public GlassblowerFurnaceTileEntity(BlockPos pos, BlockState state) {
        super(AtumTileEntities.GLASSBLOWER_FURNACE.get(), pos, state, RecipeType.SMELTING);
        this.quickCheck = RecipeManager.createCheck(RecipeType.SMELTING);
    }

    @Override
    @Nonnull
    protected Component getDefaultName() {
        return Component.translatable("atum.container.glassblower_furnace");
    }

    @Override
    @Nonnull
    protected AbstractContainerMenu createMenu(int id, @Nonnull Inventory player) {
        return new FurnaceMenu(id, player, this, this.dataAccess);
    }

    public int getGlassBlowerCookTime(Level level, @Nonnull ItemStack output) {
        int cookTime = this.quickCheck.getRecipeFor(this, level).map(AbstractCookingRecipe::getCookingTime).orElse(200);;
        return isGlassOutput(output) ? cookTime / 4 : cookTime;
    }

    public boolean isGlassOutput(@Nonnull ItemStack output) {
        return output.is(Tags.Items.GLASS);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, GlassblowerFurnaceTileEntity glassblower) {
        boolean flag = glassblower.isBurning();
        boolean flag1 = false;
        if (glassblower.isBurning()) {
            --glassblower.litTime;
        }

        if (!level.isClientSide) {
            ItemStack itemstack = glassblower.items.get(1);
            if (glassblower.isBurning() || !itemstack.isEmpty() && !glassblower.items.get(0).isEmpty()) {
                Recipe<?> irecipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, glassblower, level).orElse(null); //TODO Might have to do this the same way as vanilla. Look into. Might be able to fix Glassblower Furnace wonkyness?
                if (irecipe != null) {
                    int maxStackSize = glassblower.getMaxStackSize();
                    if (!glassblower.isBurning() && glassblower.canBurn(irecipe, glassblower.items, maxStackSize)) {
                        glassblower.litTime = glassblower.getBurnDuration(itemstack);
                        glassblower.litDuration = glassblower.litTime;
                        if (glassblower.isBurning()) {
                            flag1 = true;
                            if (itemstack.hasCraftingRemainingItem())
                                glassblower.items.set(1, itemstack.getCraftingRemainingItem());
                            else if (!itemstack.isEmpty()) {
                                itemstack.shrink(1);
                                if (itemstack.isEmpty()) {
                                    glassblower.items.set(1, itemstack.getCraftingRemainingItem());
                                }
                            }
                        }
                    }

                    if (glassblower.isBurning() && glassblower.canBurn(irecipe, glassblower.items, maxStackSize)) {
                        ++glassblower.cookingProgress;
                        if (glassblower.cookingProgress == glassblower.cookingTotalTime) {
                            glassblower.cookingProgress = 0;
                            glassblower.cookingTotalTime = glassblower.getGlassBlowerCookTime(level, irecipe.getResultItem(level.registryAccess()));
                            glassblower.burn(irecipe, glassblower.items, maxStackSize);
                            flag1 = true;
                        }
                    } else {
                        glassblower.cookingProgress = 0;
                    }
                }
            } else if (!glassblower.isBurning() && glassblower.cookingProgress > 0) {
                glassblower.cookingProgress = Mth.clamp(glassblower.cookingProgress - 2, 0, glassblower.cookingTotalTime);
            }

            if (flag != glassblower.isBurning()) {
                flag1 = true;
                level.setBlock(pos, state.setValue(AbstractFurnaceBlock.LIT, glassblower.isBurning()), 3);
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
            this.cookingTotalTime = this.getGlassBlowerCookTime(this.level, slotStack);
            this.cookingProgress = 0;
            this.setChanged();
        }
    }

    //Copied from AbstractFurnaceBlockEntity
    private boolean burn(@Nullable Recipe<?> p_155027_, NonNullList<ItemStack> p_155028_, int p_155029_) {
        if (p_155027_ != null && this.canBurn(p_155027_, p_155028_, p_155029_) && this.level != null) {
            ItemStack itemstack = p_155028_.get(0);
            ItemStack itemstack1 = ((Recipe<WorldlyContainer>) p_155027_).assemble(this, this.level.registryAccess());
            ItemStack itemstack2 = p_155028_.get(2);
            if (itemstack2.isEmpty()) {
                p_155028_.set(2, itemstack1.copy());
            } else if (itemstack2.is(itemstack1.getItem())) {
                itemstack2.grow(itemstack1.getCount());
            }

            if (itemstack.is(Blocks.WET_SPONGE.asItem()) && !p_155028_.get(1).isEmpty() && p_155028_.get(1).is(Items.BUCKET)) {
                p_155028_.set(1, new ItemStack(Items.WATER_BUCKET));
            }

            itemstack.shrink(1);
            return true;
        } else {
            return false;
        }
    }

    //Copied from AbstractFurnaceBlockEntity
    public boolean canBurn(@Nullable Recipe<?> p_155006_, NonNullList<ItemStack> p_155007_, int p_155008_) {
        if (!p_155007_.get(0).isEmpty() && p_155006_ != null && this.level != null) {
            ItemStack itemstack = ((Recipe<WorldlyContainer>) p_155006_).assemble(this, this.level.registryAccess());
            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack itemstack1 = p_155007_.get(2);
                if (itemstack1.isEmpty()) {
                    return true;
                } else if (!itemstack1.sameItem(itemstack)) {
                    return false;
                } else if (itemstack1.getCount() + itemstack.getCount() <= p_155008_ && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
                    return true;
                } else {
                    return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        } else {
            return false;
        }
    }
}
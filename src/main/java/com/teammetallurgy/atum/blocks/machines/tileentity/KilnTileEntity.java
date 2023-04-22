package com.teammetallurgy.atum.blocks.machines.tileentity;

import com.teammetallurgy.atum.api.recipe.AtumRecipeTypes;
import com.teammetallurgy.atum.api.recipe.recipes.KilnRecipe;
import com.teammetallurgy.atum.blocks.machines.KilnBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumTileEntities;
import com.teammetallurgy.atum.inventory.container.block.KilnContainer;
import com.teammetallurgy.atum.misc.StackHelper;
import com.teammetallurgy.atum.misc.recipe.RecipeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SpongeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraftforge.common.Tags.Items.*;

public class KilnTileEntity extends KilnBaseTileEntity { //TODO Partial rewrite needed, changes in vanilla
    public int burnTime;
    public int recipesUsed;
    public int cookTime;
    public int cookTimeTotal;
    public final ContainerData kilnData = new ContainerData() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return KilnTileEntity.this.burnTime;
                case 1:
                    return KilnTileEntity.this.recipesUsed;
                case 2:
                    return KilnTileEntity.this.cookTime;
                case 3:
                    return KilnTileEntity.this.cookTimeTotal;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    KilnTileEntity.this.burnTime = value;
                    break;
                case 1:
                    KilnTileEntity.this.recipesUsed = value;
                    break;
                case 2:
                    KilnTileEntity.this.cookTime = value;
                    break;
                case 3:
                    KilnTileEntity.this.cookTimeTotal = value;
            }

        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    public KilnTileEntity(BlockPos pos, BlockState state) {
        super(AtumTileEntities.KILN.get(), pos ,state);
    }


    public static void serverTick(Level level, BlockPos pos, BlockState state, KilnTileEntity kiln) {
        if (!kiln.isPrimary()) {
            return;
        }

        boolean isBurning = kiln.isBurning();
        boolean markDirty = false;

        if (kiln.isBurning()) {
            --kiln.burnTime;
        }

        if (level != null && !level.isClientSide) {
            ItemStack fuelStack = kiln.inventory.get(4);

            if (kiln.isBurning() || !fuelStack.isEmpty() && !kiln.getInputs().isEmpty()) {
                boolean canSmeltAny = false;
                for (int i = 0; i <= 4; i++) {
                    canSmeltAny |= kiln.canSmelt(i, 5, 8) != -1;
                }

                if (!kiln.isBurning() && canSmeltAny) {
                    kiln.burnTime = ForgeHooks.getBurnTime(fuelStack, AtumRecipeTypes.KILN.get());
                    kiln.recipesUsed = kiln.burnTime;

                    if (kiln.isBurning()) {
                        markDirty = true;
                        if (!fuelStack.isEmpty()) {
                            Item fuelItemCached = fuelStack.getItem();
                            fuelStack.shrink(1);

                            if (fuelStack.isEmpty()) {
                                ItemStack containerStack = fuelItemCached.getCraftingRemainingItem(fuelStack);
                                kiln.inventory.set(4, containerStack);
                            }
                        }
                    }
                }

                if (kiln.isBurning() && canSmeltAny) {
                    ++kiln.cookTime;
                    if (kiln.cookTime == kiln.cookTimeTotal) {
                        kiln.cookTime = 0;
                        kiln.cookTimeTotal = 0;
                        if (!kiln.isInputEmpty()) {
                            kiln.cookTimeTotal = kiln.getCookTime();
                        }
                        for (int i = 0; i <= 4; i++) {
                            kiln.smeltItem(i, 5, 8);
                        }
                        markDirty = true;
                    }
                } else {
                    kiln.cookTime = 0;
                }
            } else if ((!kiln.isBurning() && kiln.cookTime > 0) || kiln.isInputEmpty()) {
                kiln.cookTime = Mth.clamp(kiln.cookTime - 2, 0, kiln.cookTimeTotal);
            }

            if (isBurning != kiln.isBurning()) {
                markDirty = true;
                level.setBlockAndUpdate(pos, state.setValue(KilnBlock.LIT, kiln.isBurning()));
                BlockPos secondaryKilnPos = KilnBlock.getSecondaryKilnFromPrimary(level, pos);
                if (secondaryKilnPos != null) {
                    BlockState secondaryState = level.getBlockState(secondaryKilnPos);
                    if (secondaryState.getBlock() == AtumBlocks.KILN.get()) {
                        level.setBlockAndUpdate(secondaryKilnPos, secondaryState.setValue(KilnBlock.LIT, kiln.isBurning()));
                    }
                }
            }
        }

        if (markDirty) {
            kiln.setChanged();
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
    public void setItem(int index, @Nonnull ItemStack stack) {
        if (!isPrimary()) {
            KilnBaseTileEntity primary = getPrimary();
            if (primary != null) {
                primary.setItem(index, stack);
            }
        }

        ItemStack slotStack = this.inventory.get(index);
        boolean isValid = !stack.isEmpty() && stack.sameItem(slotStack) && ItemStack.tagMatches(stack, slotStack);

        this.inventory.set(index, stack);

        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }

        if (index <= 3 && !isValid) {
            this.cookTimeTotal = this.getCookTime();
            this.setChanged();
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
                    } else if (!output.sameItem(result)) {
                        continue;
                    } else if (output.getCount() + result.getCount() <= this.getMaxStackSize() && output.getCount() + result.getCount() <= output.getMaxStackSize()) {
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
                this.inventory.set(outputSlot, result);
            } else if (output.getItem() == result.getItem()) {
                output.grow(result.getCount());
            }
            input.shrink(1);
        }
    }

    @Nonnull
    private ItemStack getSmeltingResult(@Nonnull ItemStack input) {
        if (this.level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel) level;
            RecipeManager recipeManager = serverLevel.getRecipeManager();
            List<KilnRecipe> recipes = new ArrayList<>(RecipeHelper.getRecipes(recipeManager, AtumRecipeTypes.KILN.get()));
            recipes.addAll(RecipeHelper.getKilnRecipesFromFurnace(recipeManager, this.level));
            for (KilnRecipe kilnRecipe : recipes) {
                for (Ingredient ingredient : kilnRecipe.getIngredients()) {
                    if (StackHelper.areIngredientsEqualIgnoreSize(ingredient, input)) {
                        return kilnRecipe.assemble(this, this.level.registryAccess());
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }

    private List<ItemStack> getInputs() {
        return Arrays.asList(this.inventory.get(0), this.inventory.get(1), this.inventory.get(2), this.inventory.get(3));
    }

    protected int getCookTime() {
        Level level = this.level;
        return level != null ? level.getRecipeManager().getRecipeFor(AtumRecipeTypes.KILN.get(), this, level).map(KilnRecipe::getCookTime).orElse(200) : 200;
    }

    @Override
    protected AbstractContainerMenu createMenu(int windowID, @Nonnull Inventory playerInventory) {
        return new KilnContainer(windowID, playerInventory, this.worldPosition);
    }

    public static boolean canKilnNotSmelt(Ingredient ingredient) {
        for (ItemStack stack : ingredient.getItems()) {
            return canKilnNotSmelt(stack);
        }
        return true;
    }

    public static boolean canKilnNotSmelt(ItemStack stack) {
        Item item = stack.getItem();
        Block block = Block.byItem(stack.getItem());

        return AtumRecipeTypes.kilnBlacklist.contains(ForgeRegistries.ITEMS.getKey(item)) || AtumRecipeTypes.kilnBlacklist.contains(ForgeRegistries.BLOCKS.getKey(block)) ||
                item.isEdible() || stack.is(ItemTags.COALS) ||stack.is( ORES_COAL) || stack.is(STORAGE_BLOCKS_COAL) ||
                stack.is(ItemTags.PLANKS) || stack.is(ItemTags.LOGS) || stack.is(RODS_WOODEN) || stack.is(ItemTags.SMALL_FLOWERS) ||
                stack.is(ORES) || stack.is(INGOTS) && !stack.is(INGOTS_BRICK) || stack.is(NUGGETS) || stack.is(GEMS) || stack.is(DUSTS) || 
                stack.is(DYES) || stack.is(SLIMEBALLS) || stack.is(LEATHER) || block instanceof SpongeBlock;
    }

    @Override
    public void load(@Nonnull CompoundTag tag) {
        super.load(tag);
        this.burnTime = tag.getInt("BurnTime");
        this.cookTime = tag.getInt("CookTime");
        this.cookTimeTotal = tag.getInt("CookTimeTotal");
        this.recipesUsed = ForgeHooks.getBurnTime(this.inventory.get(4), AtumRecipeTypes.KILN.get());
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("BurnTime", this.burnTime);
        tag.putInt("CookTime", this.cookTime);
        tag.putInt("CookTimeTotal", this.cookTimeTotal);
    }
}
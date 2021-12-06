package com.teammetallurgy.atum.blocks.machines.tileentity;

import com.teammetallurgy.atum.api.recipe.IAtumRecipeType;
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
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.SpongeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraftforge.common.Tags.Items.*;

public class KilnTileEntity extends KilnBaseTileEntity implements TickableBlockEntity {
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

    public KilnTileEntity() {
        super(AtumTileEntities.KILN);
    }

    @Override
    public void tick() {
        if (!isPrimary()) {
            return;
        }

        boolean isBurning = this.isBurning();
        boolean markDirty = false;

        if (this.isBurning()) {
            --this.burnTime;
        }

        if (this.level != null && !this.level.isClientSide) {
            ItemStack fuelStack = this.inventory.get(4);

            if (this.isBurning() || !fuelStack.isEmpty() && !this.getInputs().isEmpty()) {
                boolean canSmeltAny = false;
                for (int i = 0; i <= 4; i++) {
                    canSmeltAny |= this.canSmelt(i, 5, 8) != -1;
                }

                if (!this.isBurning() && canSmeltAny) {
                    this.burnTime = ForgeHooks.getBurnTime(fuelStack);
                    this.recipesUsed = burnTime;

                    if (this.isBurning()) {
                        markDirty = true;
                        if (!fuelStack.isEmpty()) {
                            Item fuelItemCached = fuelStack.getItem();
                            fuelStack.shrink(1);

                            if (fuelStack.isEmpty()) {
                                ItemStack containerStack = fuelItemCached.getContainerItem(fuelStack);
                                this.inventory.set(4, containerStack);
                            }
                        }
                    }
                }

                if (this.isBurning() && canSmeltAny) {
                    ++this.cookTime;
                    if (this.cookTime == this.cookTimeTotal) {
                        this.cookTime = 0;
                        this.cookTimeTotal = 0;
                        if (!this.isInputEmpty()) {
                            this.cookTimeTotal = this.getCookTime();
                        }
                        for (int i = 0; i <= 4; i++) {
                            this.smeltItem(i, 5, 8);
                        }
                        markDirty = true;
                    }
                } else {
                    this.cookTime = 0;
                }
            } else if ((!this.isBurning() && this.cookTime > 0) || this.isInputEmpty()) {
                this.cookTime = Mth.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
            }

            if (isBurning != this.isBurning()) {
                markDirty = true;
                level.setBlockAndUpdate(worldPosition, level.getBlockState(worldPosition).setValue(KilnBlock.LIT, this.isBurning()));
                BlockPos secondaryKilnPos = KilnBlock.getSecondaryKilnFromPrimary(level, worldPosition);
                if (secondaryKilnPos != null) {
                    BlockState secondaryState = level.getBlockState(secondaryKilnPos);
                    if (secondaryState.getBlock() == AtumBlocks.KILN) {
                        level.setBlockAndUpdate(secondaryKilnPos, secondaryState.setValue(KilnBlock.LIT, this.isBurning()));
                    }
                }
            }
        }

        if (markDirty) {
            this.setChanged();
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
            ServerLevel serverWorld = (ServerLevel) level;
            RecipeManager recipeManager = serverWorld.getRecipeManager();
            List<KilnRecipe> recipes = new ArrayList<>(RecipeHelper.getRecipes(recipeManager, IAtumRecipeType.KILN));
            recipes.addAll(RecipeHelper.getKilnRecipesFromFurnace(recipeManager));
            for (KilnRecipe kilnRecipe : recipes) {
                for (Ingredient ingredient : kilnRecipe.getIngredients()) {
                    if (StackHelper.areIngredientsEqualIgnoreSize(ingredient, input)) {
                        return kilnRecipe.assemble(this);
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
        Level world = this.level;
        return world != null ? world.getRecipeManager().getRecipeFor(IAtumRecipeType.KILN, this, world).map(KilnRecipe::getCookTime).orElse(200) : 200;
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

        return IAtumRecipeType.kilnBlacklist.contains(item.getRegistryName()) || IAtumRecipeType.kilnBlacklist.contains(block.getRegistryName()) ||
                item.isEdible() || block instanceof OreBlock || item.is(ItemTags.COALS) || item.is(ORES_COAL) || item.is(STORAGE_BLOCKS_COAL) ||
                item.is(ItemTags.PLANKS) || item.is(ItemTags.LOGS) || item.is(RODS_WOODEN) || item.is(ItemTags.SMALL_FLOWERS) ||
                item.is(ORES) || item.is(INGOTS) && !item.is(INGOTS_BRICK) || item.is(NUGGETS) || item.is(GEMS) || item.is(DUSTS) ||
                item.is(DYES) || item.is(SLIMEBALLS) || item.is(LEATHER) || block instanceof SpongeBlock;
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundTag compound) {
        super.load(state, compound);
        this.burnTime = compound.getInt("BurnTime");
        this.cookTime = compound.getInt("CookTime");
        this.cookTimeTotal = compound.getInt("CookTimeTotal");
        this.recipesUsed = ForgeHooks.getBurnTime(this.inventory.get(4));
    }

    @Override
    @Nonnull
    public CompoundTag save(@Nonnull CompoundTag compound) {
        super.save(compound);
        compound.putInt("BurnTime", this.burnTime);
        compound.putInt("CookTime", this.cookTime);
        compound.putInt("CookTimeTotal", this.cookTimeTotal);
        return compound;
    }
}
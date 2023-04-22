package com.teammetallurgy.atum.blocks.machines.tileentity;

import com.teammetallurgy.atum.api.recipe.AtumRecipeTypes;
import com.teammetallurgy.atum.api.recipe.recipes.QuernRecipe;
import com.teammetallurgy.atum.blocks.base.tileentity.InventoryBaseTileEntity;
import com.teammetallurgy.atum.blocks.machines.QuernBlock;
import com.teammetallurgy.atum.init.AtumTileEntities;
import com.teammetallurgy.atum.misc.StackHelper;
import com.teammetallurgy.atum.misc.recipe.RecipeHelper;
import com.teammetallurgy.atum.network.NetworkHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

public class QuernTileEntity extends InventoryBaseTileEntity implements WorldlyContainer {
    private int currentRotation;
    private int quernRotations;

    public QuernTileEntity(BlockPos pos, BlockState state) {
        super(AtumTileEntities.QUERN.get(), pos, state, 1);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, QuernTileEntity quern) {
        if (level != null && !level.isClientSide) {
            if (quern.currentRotation >= 360) {
                quern.currentRotation = 0;
                quern.quernRotations += 1;
            }

            if (quern.getItem(0).isEmpty()) {
                quern.quernRotations = 0;
            }

            if (quern.quernRotations > 0) {
                if (level instanceof ServerLevel serverLevel) {
                    Collection<QuernRecipe> recipes = RecipeHelper.getRecipes(serverLevel.getRecipeManager(), AtumRecipeTypes.QUERN.get());
                    for (QuernRecipe quernRecipe : recipes) {
                        for (Ingredient ingredient : quernRecipe.getIngredients()) {
                            if (StackHelper.areIngredientsEqualIgnoreSize(ingredient, quern.getItem(0)) && quernRecipe.getRotations() == quern.quernRotations) {
                                quern.removeItem(0, 1);
                                quern.outputItems(quernRecipe.assemble(quern, level.registryAccess()), level, pos);
                                quern.quernRotations = 0;
                                quern.setChanged();
                            }
                        }
                    }
                }
            }
        }
    }

    private void outputItems(@Nonnull ItemStack stack, Level level, BlockPos pos) {
        Direction facing = level.getBlockState(pos).getValue(QuernBlock.FACING).getOpposite();
        BlockEntity tileEntity = level.getBlockEntity(pos.relative(facing));
        if (tileEntity instanceof WorldlyContainer && ((WorldlyContainer) tileEntity).getSlotsForFace(facing).length > 0 || tileEntity instanceof Container && ((Container) tileEntity).getContainerSize() > 0) {
            Container inventory = ((Container) tileEntity);
            stack = HopperBlockEntity.addItem(this, inventory, stack, facing);
        } else if (tileEntity != null) {
            LazyOptional<IItemHandler> capability = tileEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, facing);
            if (capability.isPresent()) {
                IItemHandler itemHandler = capability.orElse(null);
                if (itemHandler != null) {
                    stack = ItemHandlerHelper.insertItem(itemHandler, stack, false);
                }
            }
        }

        if (!stack.isEmpty()) {
            StackHelper.spawnItemStack(level, (double) facing.getStepX() + pos.getX() + 0.5D, (double) pos.getY() + 0.15D, (double) facing.getStepZ() + pos.getZ() + 0.5, stack);
            if (level.isClientSide) {
                level.playLocalSound((double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.CHICKEN_EGG, SoundSource.BLOCKS, 1.0F, 0.4F, false);
            }
        }
    }

    @Override
    public boolean canPlaceItem(int index, @Nonnull ItemStack stack) {
        return RecipeHelper.isItemValidForSlot(this.level, stack, AtumRecipeTypes.QUERN.get());
    }

    public int getRotations() {
        return this.currentRotation;
    }

    public void setRotations(int rotations) {
        this.currentRotation = rotations;
    }

    @Override
    public void load(@Nonnull CompoundTag tag) {
        super.load(tag);
        this.currentRotation = tag.getInt("currentRotation");
        this.quernRotations = tag.getInt("quernRotations");
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("currentRotation", this.currentRotation);
        tag.putInt("quernRotations", this.quernRotations);
    }

    @Override
    protected AbstractContainerMenu createMenu(int windowID, @Nonnull Inventory playerInventory) {
        return null;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection manager, ClientboundBlockEntityDataPacket packet) {
        super.onDataPacket(manager, packet);
        if (packet.getTag() != null) {
            this.load(packet.getTag());
            this.setChanged();
        }
    }

    @Override
    @Nonnull
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.level instanceof ServerLevel) {
            final Packet<?> packet = this.getUpdatePacket();
            NetworkHandler.sendToTracking((ServerLevel) this.level, this.worldPosition, packet, false);
        }
    }

    @Override
    @Nonnull
    public int[] getSlotsForFace(@Nonnull Direction side) {
        return new int[0];
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, @Nonnull ItemStack stack, Direction facing) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, @Nonnull ItemStack stack, @Nonnull Direction facing) {
        return false;
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction direction) {
        if (capability == ForgeCapabilities.ITEM_HANDLER) {
            return LazyOptional.empty();
        } else {
            return super.getCapability(capability, direction);
        }
    }
}
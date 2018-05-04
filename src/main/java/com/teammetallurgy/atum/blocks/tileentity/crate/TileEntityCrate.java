package com.teammetallurgy.atum.blocks.tileentity.crate;

import com.teammetallurgy.atum.blocks.BlockAtumPlank;
import com.teammetallurgy.atum.blocks.BlockCrate;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.util.Constants.NBT;

import javax.annotation.Nonnull;

public class TileEntityCrate extends TileEntityLockableLoot implements ITickable {
    private static int inventorySize = 27;
    private NonNullList<ItemStack> inventory = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
    private String customName;
    private int ticksSinceSync;
    public int numPlayersUsing;
    public float lidAngle;
    public float prevLidAngle;

    public TileEntityCrate() {
    }

    @Override
    public int getSizeInventory() {
        return inventorySize;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    @Nonnull
    public String getName() {
        return this.hasCustomName() ? this.customName : getDefaultName();
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null && !this.customName.isEmpty();
    }

    @Override
    public void setCustomName(@Nonnull String name) {
        customName = name;
    }

    private String getDefaultName() {
        BlockAtumPlank.WoodType type = BlockAtumPlank.WoodType.byIndex(BlockAtumPlank.WoodType.values().length);
        String name = "container.crate.";
        switch (type) {
            case PALM:
                name += "palm";
                break;
            case DEADWOOD:
                name += "deadwood";
                break;
            default:
                name += "invaild";
        }
        return name;
    }

    @Override
    @Nonnull
    public Container createContainer(@Nonnull InventoryPlayer playerInventory, @Nonnull EntityPlayer player) {
        this.fillWithLoot(player);
        return new ContainerCrate(playerInventory, this, player);
    }

    @Override
    @Nonnull
    protected NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    public void update() {
        int i = this.pos.getX();
        int j = this.pos.getY();
        int k = this.pos.getZ();
        ++this.ticksSinceSync;

        if (!this.world.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + i + j + k) % 200 == 0) {
            this.numPlayersUsing = 0;
            float f = 5.0F;

            for (EntityPlayer entityplayer : this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB((double) ((float) i - 5.0F), (double) ((float) j - 5.0F), (double) ((float) k - 5.0F), (double) ((float) (i + 1) + 5.0F), (double) ((float) (j + 1) + 5.0F), (double) ((float) (k + 1) + 5.0F)))) {
                if (entityplayer.openContainer instanceof ContainerCrate) {
                    IInventory iinventory = ((ContainerCrate) entityplayer.openContainer).getCrateInventory();

                    if (iinventory == this) {
                        ++this.numPlayersUsing;
                    }
                }
            }
        }
        this.prevLidAngle = this.lidAngle;

        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {
            double d1 = (double) i + 0.5D;
            double d2 = (double) k + 0.5D;

            this.world.playSound(null, d1, (double) j + 0.5D, d2, SoundEvents.BLOCK_WOODEN_TRAPDOOR_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
            float f2 = this.lidAngle;

            if (this.numPlayersUsing > 0) {
                this.lidAngle += 0.1F;
            } else {
                this.lidAngle -= 0.1F;
            }

            if (this.lidAngle > 1.0F) {
                this.lidAngle = 1.0F;
            }

            if (this.lidAngle < 0.5F && f2 >= 0.5F) {
                double d3 = (double) i + 0.5D;
                double d0 = (double) k + 0.5D;

                this.world.playSound(null, d3, (double) j + 0.5D, d0, SoundEvents.BLOCK_WOODEN_TRAPDOOR_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (this.lidAngle < 0.0F) {
                this.lidAngle = 0.0F;
            }
        }
    }

    @Override
    public void openInventory(@Nonnull EntityPlayer player) {
        if (!player.isSpectator()) {
            if (this.numPlayersUsing < 0) {
                this.numPlayersUsing = 0;
            }

            ++this.numPlayersUsing;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
            this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
        }
    }

    @Override
    public void closeInventory(@Nonnull EntityPlayer player) {
        if (!player.isSpectator() && this.getBlockType() instanceof BlockCrate) {
            --this.numPlayersUsing;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
            this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
        }
    }

    @Override
    @Nonnull
    public String getGuiID() {
        return String.valueOf(new ResourceLocation(Constants.MOD_ID, "crate"));
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        this.inventory = NonNullList.withSize(inventorySize, ItemStack.EMPTY);

        if (!this.checkLootAndRead(compound)) {
            ItemStackHelper.loadAllItems(compound, inventory);
        }
        if (compound.hasKey("CustomName", NBT.TAG_STRING)) {
            customName = compound.getString("CustomName");
        }
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, inventory);
        }
        if (hasCustomName()) {
            compound.setString("CustomName", customName);
        }
        return compound;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.inventory) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
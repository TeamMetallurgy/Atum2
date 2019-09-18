package com.teammetallurgy.atum.blocks.wood.tileentity.crate;

import com.teammetallurgy.atum.blocks.base.tileentity.TileEntityInventoryBase;
import com.teammetallurgy.atum.blocks.wood.BlockCrate;
import com.teammetallurgy.atum.inventory.container.block.ContainerCrate;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nonnull;

public class TileEntityCrate extends TileEntityInventoryBase implements ITickable {
    private int ticksSinceSync;
    private int numPlayersUsing;
    public float lidAngle;
    public float prevLidAngle;

    public TileEntityCrate() {
        super(27);
    }

    @Override
    public void update() {
        int x = this.pos.getX();
        int y = this.pos.getY();
        int z = this.pos.getZ();
        ++this.ticksSinceSync;

        if (!this.world.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + x + y + z) % 200 == 0) {
            this.numPlayersUsing = 0;

            for (PlayerEntity entityplayer : this.world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB((double) ((float) x - 5.0F), (double) ((float) y - 5.0F), (double) ((float) z - 5.0F), (double) ((float) (x + 1) + 5.0F), (double) ((float) (y + 1) + 5.0F), (double) ((float) (z + 1) + 5.0F)))) {
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
            this.world.playSound(null, (double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, SoundEvents.BLOCK_WOODEN_TRAPDOOR_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
            float lidAngleCached = this.lidAngle;

            if (this.numPlayersUsing > 0) {
                this.lidAngle += 0.1F;
            } else {
                this.lidAngle -= 0.1F;
            }
            if (this.lidAngle > 1.0F) {
                this.lidAngle = 1.0F;
            }

            if (this.lidAngle < 0.5F && lidAngleCached >= 0.5F) {
                this.world.playSound(null, (double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, SoundEvents.BLOCK_WOODEN_TRAPDOOR_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (this.lidAngle < 0.0F) {
                this.lidAngle = 0.0F;
            }
        }
    }

    @Override
    public void openInventory(@Nonnull PlayerEntity player) {
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
    public void closeInventory(@Nonnull PlayerEntity player) {
        if (!player.isSpectator() && this.getBlockType() instanceof BlockCrate) {
            --this.numPlayersUsing;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
            this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
        }
    }

    @Override
    @Nonnull
    public Container createContainer(@Nonnull InventoryPlayer playerInventory, @Nonnull PlayerEntity player) {
        this.fillWithLoot(player);
        return new ContainerCrate(playerInventory, this, player);
    }

    @Override
    @Nonnull
    public String getGuiID() {
        return String.valueOf(new ResourceLocation(Constants.MOD_ID, "crate"));
    }
}
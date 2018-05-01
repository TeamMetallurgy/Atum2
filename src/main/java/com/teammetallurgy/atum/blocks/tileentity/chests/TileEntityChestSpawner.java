package com.teammetallurgy.atum.blocks.tileentity.chests;

import com.teammetallurgy.atum.blocks.BlockChestSpawner;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.NonNullList;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TileEntityChestSpawner extends TileEntityChest {

    private final MobSpawnerBaseLogic spawnerLogic = new MobSpawnerBaseLogic() {
        @Override
        public void broadcastEvent(int id) {
            TileEntityChestSpawner.this.world.addBlockEvent(TileEntityChestSpawner.this.pos, Blocks.MOB_SPAWNER, id, 0);
        }

        @Override
        public World getSpawnerWorld() {
            return TileEntityChestSpawner.this.world;
        }

        @Override
        @Nonnull
        public BlockPos getSpawnerPosition() {
            return TileEntityChestSpawner.this.pos;
        }

        @Override
        public void setNextSpawnData(@Nonnull WeightedSpawnerEntity spawnerEntity) {
            super.setNextSpawnData(spawnerEntity);

            if (this.getSpawnerWorld() != null) {
                IBlockState state = this.getSpawnerWorld().getBlockState(this.getSpawnerPosition());
                this.getSpawnerWorld().notifyBlockUpdate(TileEntityChestSpawner.this.pos, state, state, 4);
            }
        }
    };

    private NonNullList<ItemStack> chestContents = NonNullList.withSize(27, ItemStack.EMPTY);

    public TileEntityChestSpawner() {
        //this.spawnerLogic.setEntityName(entityName()); //TODO
    }

    private String entityName() {
        int entityID = (int) (Math.random() * 6.0D);
        switch (entityID) {
            case 0:
                return "AtumMummy";
            case 1:
                return "AtumBanditWarrior";
            case 2:
                return "AtumBanditArcher";
            case 3:
                return "AtumDustySkeleton";
            case 4:
                return "AtumDesertGhost";
            case 5:
                return "AtumStoneSoldier";
            case 6:
                return "AtumDesertWolf";
            default:
                return "";
        }
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.chestContents) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.chestContents = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);

        if (!this.checkLootAndRead(compound)) {
            ItemStackHelper.loadAllItems(compound, this.chestContents);
        }

        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }

        this.spawnerLogic.readFromNBT(compound);
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
        super.writeToNBT(compound);

        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, this.chestContents);
        }

        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }

        this.spawnerLogic.writeToNBT(compound);

        return compound;
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull EntityPlayer player) {
        double d0 = 4.0D;
        double d1 = 3.0D;
        List<EntityMob> list = super.world.getEntitiesWithinAABB(EntityMob.class, new AxisAlignedBB((double) super.pos.getX() - d0, (double) super.pos.getY() - d1, (double) super.pos.getZ() - d0, (double) super.pos.getX() + d0, (double) super.pos.getY() + d1, (double) super.pos.getZ() + d0));
        if (!list.isEmpty()) {
            if (!super.world.isRemote) {
                player.sendMessage(new TextComponentTranslation("chat.atum.enemies"));
            }
            return false;
        } else {
            return this.world.getTileEntity(this.pos) == this && player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        if (!player.isSpectator() && this.getBlockType() instanceof BlockChestSpawner) {
            --this.numPlayersUsing;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);

            this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);

            if (this.getChestType() == BlockChest.Type.TRAP) {
                this.world.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType(), false);
            }
        }
    }

    @Override
    public void invalidate() {
        this.tileEntityInvalid = true;
        this.updateContainingBlockInfo();
    }

    @Override
    @Nonnull
    protected NonNullList<ItemStack> getItems() {
        return this.chestContents;
    }

    @Override
    public void update() {
        this.spawnerLogic.updateSpawner();
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
    }

    @Override
    @Nonnull
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound tag = this.writeToNBT(new NBTTagCompound());
        tag.removeTag("SpawnPotentials");
        return tag;
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        return this.spawnerLogic.setDelayToMin(id) || super.receiveClientEvent(id, type);
    }

    @Override
    public boolean onlyOpsCanSetNbt() {
        return true;
    }

    public MobSpawnerBaseLogic getSpawnerBaseLogic() {
        return this.spawnerLogic;
    }
}
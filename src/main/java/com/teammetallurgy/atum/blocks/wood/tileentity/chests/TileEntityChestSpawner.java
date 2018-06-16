package com.teammetallurgy.atum.blocks.wood.tileentity.chests;

import com.teammetallurgy.atum.blocks.BlockChestSpawner;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

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
                IBlockState iblockstate = this.getSpawnerWorld().getBlockState(this.getSpawnerPosition());
                this.getSpawnerWorld().notifyBlockUpdate(TileEntityChestSpawner.this.pos, iblockstate, iblockstate, 4);
            }
        }

        @Override
        public void updateSpawner() {
            if (!world.isDaytime()) {
                setEntityId(getNightTime(spawnPool));
            } else {
                setEntityId(getDayTime(spawnPool));
            }
            super.updateSpawner();
        }
    };
    private NonNullList<ItemStack> chestContents = NonNullList.withSize(27, ItemStack.EMPTY);
    private int spawnPool;

    public TileEntityChestSpawner() {
        spawnPool = MathHelper.getInt(new Random(), 0, 2);
    }

    private static ResourceLocation getDayTime(int spawnPool) {
        switch (spawnPool) {
            case 0:
                return AtumEntities.BARBARIAN.getRegistryName();
            case 1:
                return AtumEntities.BRIGAND.getRegistryName();
            case 2:
                return AtumEntities.NOMAD.getRegistryName();
            default:
                return AtumEntities.BARBARIAN.getRegistryName();
        }
    }

    private static ResourceLocation getNightTime(int spawnPool) {
        switch (spawnPool) {
            case 0:
                return AtumEntities.MUMMY.getRegistryName();
            case 1:
                return AtumEntities.FORSAKEN.getRegistryName();
            case 2:
                return AtumEntities.BONESTORM.getRegistryName();
            default:
                return AtumEntities.MUMMY.getRegistryName();
        }
    }

    @Override
    public void update() {
        super.update();
        this.spawnerLogic.updateSpawner();
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
        this.spawnerLogic.readFromNBT(compound);
        spawnPool = compound.getInteger("spawnPool");
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
        super.writeToNBT(compound);
        this.spawnerLogic.writeToNBT(compound);
        compound.setInteger("spawnPool", spawnPool);

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
    @Nonnull
    protected NonNullList<ItemStack> getItems() {
        return this.chestContents;
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
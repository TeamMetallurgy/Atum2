package com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity;

import com.teammetallurgy.atum.blocks.base.tileentity.TileEntityChestBase;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.BlockSarcophagus;
import com.teammetallurgy.atum.entity.undead.EntityPharaoh;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumSounds;
import com.teammetallurgy.atum.utils.AtumUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.DifficultyInstance;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntitySarcophagus extends TileEntityChestBase {
    public static final String SARCOPHAGUS_CONTAINER = "atum.container.sarcophagus";
    public boolean hasSpawned = false;
    public boolean isOpenable = false;

    public TileEntitySarcophagus() {
        super(false, true, AtumBlocks.SARCOPHAGUS);
    }

    @Override
    @Nonnull
    public String getName() {
        return this.hasCustomName() ? this.customName : SARCOPHAGUS_CONTAINER;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet) {
        super.onDataPacket(manager, packet);
        this.readFromNBT(packet.getNbtCompound());
    }

    @Override
    @Nonnull
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.hasSpawned = compound.getBoolean("spawned");
        this.isOpenable = compound.getBoolean("openable");
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setBoolean("spawned", this.hasSpawned);
        compound.setBoolean("openable", this.isOpenable);
        return compound;
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull EntityPlayer player) {
        return this.isOpenable && super.isUsableByPlayer(player);
    }

    public void setOpenable() {
        this.isOpenable = true;
        this.markDirty();
        IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
    }

    public void spawn(EntityPlayer player, DifficultyInstance difficulty) {
        if (!world.isRemote) {
            EntityPharaoh pharaoh = new EntityPharaoh(world);
            pharaoh.onInitialSpawn(difficulty, null);
            EnumFacing blockFacing = world.getBlockState(pos).getValue(BlockSarcophagus.FACING);
            pharaoh.setLocationAndAngles(pos.getX(), pos.getY() + 1, pos.getZ(), blockFacing.getHorizontalAngle() + 90, 0.0F);
            pharaoh.rotationYawHead = blockFacing.getHorizontalAngle() + 90;
            pharaoh.setSarcophagusPos(pos);
            world.spawnEntity(pharaoh);
            pharaoh.spawnGuards(pharaoh.getPosition().offset(blockFacing, 1).down());
            pharaoh.spawnExplosionParticle();
            this.hasSpawned = true;

            for (EntityPlayerMP playerMP : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
                playerMP.sendMessage(new TextComponentString(EntityPharaoh.God.getGod(pharaoh.getVariant()).getColor() + pharaoh.getName() + " " + AtumUtils.format("chat.atum.summonPharaoh") + " " + player.getGameProfile().getName()));
            }
        }
        this.world.playSound(pos.getX(), pos.getY(), pos.getZ(), AtumSounds.PHARAOH_SPAWN, SoundCategory.HOSTILE, 0.8F, 1.0F, true);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return this.isOpenable && super.isItemValidForSlot(index, stack);
    }

    @Override
    @Nonnull
    public ItemStack decrStackSize(int index, int count) {
        return this.isOpenable ? super.decrStackSize(index, count) : ItemStack.EMPTY;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return this.isOpenable && super.hasCapability(capability, facing);
    }
}
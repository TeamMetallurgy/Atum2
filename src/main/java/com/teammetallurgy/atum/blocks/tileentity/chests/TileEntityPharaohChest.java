package com.teammetallurgy.atum.blocks.tileentity.chests;

import com.teammetallurgy.atum.blocks.BlockPharaohChest;
import com.teammetallurgy.atum.entity.EntityPharaoh;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumSounds;
import com.teammetallurgy.atum.utils.AtumUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nonnull;

public class TileEntityPharaohChest extends TileEntityChest implements IInventory {
    private boolean hasSpawned = false;
    private boolean isOpenable = false;

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
    }

    public boolean hasSpawned() {
        return this.hasSpawned;
    }

    public void spawn(EntityPlayer player) {
        EntityPharaoh pharaoh = new EntityPharaoh(this.world, true);
        pharaoh.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(AtumItems.SCEPTER));
        pharaoh.setPosition(pos.getX(), pos.getY(), pos.getZ());
        pharaoh.setSarcophagusPos(pos);
        if (!this.world.isRemote) {
            this.world.spawnEntity(pharaoh);
        }
        pharaoh.spawnExplosionParticle();
        this.hasSpawned = true;

        IBlockState state = world.getBlockState(pos);
        EnumFacing facing = state.getValue(BlockPharaohChest.FACING);
        pharaoh.trySpawnMummy(pos.offset(facing.rotateY()));
        pharaoh.trySpawnMummy(pos.offset(facing.rotateYCCW()));

        if (!this.world.isRemote) {
            for (EntityPlayerMP playerMP : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
                playerMP.sendMessage(new TextComponentString(TextFormatting.YELLOW + pharaoh.getName() + " " + AtumUtils.format("chat.atum.summonPharaoh") + " " + player.getGameProfile().getName()));
            }
            this.world.playSound(null, player.getPosition(), AtumSounds.PHARAOH_SPAWN, SoundCategory.HOSTILE, 1.0F, 1.0F);
        }

    }

    public void setPharaohDespawned() {
        this.hasSpawned = false;
    }
}
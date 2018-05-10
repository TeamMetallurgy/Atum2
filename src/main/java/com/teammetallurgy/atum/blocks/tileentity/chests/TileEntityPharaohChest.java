package com.teammetallurgy.atum.blocks.tileentity.chests;

import com.teammetallurgy.atum.entity.EntityMummy;
import com.teammetallurgy.atum.entity.EntityPharaoh;
import com.teammetallurgy.atum.init.AtumSounds;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nonnull;
import java.util.List;

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
        EntityPharaoh pharaoh = new EntityPharaoh(super.world);
        pharaoh.setPosition((double) this.pos.getX() + 0.5D, (double) (this.pos.getX() + 1), (double) this.pos.getX() + 0.5D);
        pharaoh.link(this.pos);
        if (!super.world.isRemote) {
            super.world.spawnEntity(pharaoh);
        }

        pharaoh.spawnExplosionParticle();
        this.hasSpawned = true;
        EntityMummy mummy1 = new EntityMummy(super.world);
        mummy1.setPosition((double) this.pos.getX() + 0.5D, (double) this.pos.getX(), (double) this.pos.getX() - 0.5D);
        if (!super.world.isRemote) {
            super.world.spawnEntity(mummy1);
        }

        mummy1.spawnExplosionParticle();
        EntityMummy mummy2 = new EntityMummy(super.world);
        mummy2.setPosition((double) this.pos.getX() + 0.5D, (double) this.pos.getX(), (double) this.pos.getX() + 1.5D);
        if (!super.world.isRemote) {
            super.world.spawnEntity(mummy2);
        }

        mummy2.spawnExplosionParticle();
        if (!this.world.isRemote) {
            List<EntityPlayerMP> players = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();

            for (EntityPlayerMP p : players) {
                p.sendMessage(new TextComponentTranslation(pharaoh.getName() + " " + "chat.atum.summonPharaoh" + " " + player.getGameProfile().getName()));
            }
        }

        if (!this.world.isRemote) {
            this.world.playSound(player, player.getPosition(), AtumSounds.PHARAOH_SPAWN, SoundCategory.HOSTILE, 1.0F, 1.0F);
        }

    }

    public void setPharaohDespawned() {
        this.hasSpawned = false;
    }
}
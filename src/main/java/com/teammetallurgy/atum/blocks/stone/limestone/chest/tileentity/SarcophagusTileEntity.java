package com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity;

import com.teammetallurgy.atum.blocks.base.tileentity.ChestBaseTileEntity;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.SarcophagusBlock;
import com.teammetallurgy.atum.entity.undead.PharaohEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumSounds;
import com.teammetallurgy.atum.init.AtumTileEntities;
import com.teammetallurgy.atum.network.NetworkHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.ChestType;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SarcophagusTileEntity extends ChestBaseTileEntity {
    public static final String SARCOPHAGUS_CONTAINER = "atum.container.sarcophagus";
    public boolean hasSpawned;
    public boolean isOpenable;

    public SarcophagusTileEntity() {
        super(AtumTileEntities.SARCOPHAGUS, false, true, AtumBlocks.SARCOPHAGUS);
    }

    @Nullable
    @Override
    public ITextComponent getCustomName() {
        return new TranslationTextComponent(SARCOPHAGUS_CONTAINER);
    }


    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager manager, SUpdateTileEntityPacket packet) {
        super.onDataPacket(manager, packet);
        this.read(this.getBlockState(), packet.getNbtCompound());
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void read(@Nonnull BlockState state, @Nonnull CompoundNBT compound) {
        super.read(state, compound);
        this.hasSpawned = compound.getBoolean("spawned");
        this.isOpenable = compound.getBoolean("openable");
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        super.write(compound);
        compound.putBoolean("spawned", this.hasSpawned);
        compound.putBoolean("openable", this.isOpenable);
        return compound;
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull PlayerEntity player) {
        return this.isOpenable && super.isUsableByPlayer(player);
    }

    public void setOpenable() {
        this.isOpenable = true;
        this.markDirty();
        if (this.world instanceof ServerWorld) {
            final IPacket<?> packet = this.getUpdatePacket();
            NetworkHandler.sendToTracking((ServerWorld) this.world, this.pos, packet, false);
        }
    }

    public void spawn(PlayerEntity player, DifficultyInstance difficulty) {
        if (this.world != null && !this.world.isRemote) {
            PharaohEntity pharaoh = AtumEntities.PHARAOH.create(this.world);
            pharaoh.onInitialSpawn((IServerWorld) this.world, difficulty, SpawnReason.TRIGGERED, null, null);
            Direction blockFacing = world.getBlockState(pos).get(SarcophagusBlock.FACING);
            pharaoh.setLocationAndAngles(pos.getX(), pos.getY() + 1, pos.getZ(), blockFacing.getHorizontalAngle() + 90, 0.0F);
            pharaoh.rotationYawHead = blockFacing.getHorizontalAngle() + 90;
            pharaoh.setSarcophagusPos(pos);
            world.addEntity(pharaoh);
            pharaoh.spawnGuards(pharaoh.getPosition().offset(blockFacing, 2).down());
            pharaoh.spawnExplosionParticle();
            this.hasSpawned = true;

            if (this.world instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) this.world;
                for (ServerPlayerEntity playerMP : serverWorld.getServer().getPlayerList().getPlayers()) {
                    playerMP.sendMessage(pharaoh.getName().deepCopy().appendString(" ").append(new TranslationTextComponent("chat.atum.summon_pharaoh")).appendString(" " + player.getGameProfile().getName()).mergeStyle(PharaohEntity.God.getGod(pharaoh.getVariant()).getColor()), Util.DUMMY_UUID);
                }
            }
        }
        this.world.playSound(pos.getX(), pos.getY(), pos.getZ(), AtumSounds.PHARAOH_SPAWN, SoundCategory.HOSTILE, 0.8F, 1.0F, true);
    }

    @Override
    protected void playSound(@Nonnull SoundEvent sound) { //Overridden to change sound
        ChestType chestType = this.getBlockState().get(ChestBlock.TYPE);
        if (chestType != ChestType.LEFT) {
            double x = (double)this.pos.getX() + 0.5D;
            double y = (double)this.pos.getY() + 0.5D;
            double z = (double)this.pos.getZ() + 0.5D;
            if (chestType == ChestType.RIGHT) {
                Direction direction = ChestBlock.getDirectionToAttached(this.getBlockState());
                x += (double)direction.getXOffset() * 0.5D;
                z += (double)direction.getZOffset() * 0.5D;
            }
            this.world.playSound(null, x, y, z, SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.05F);
        }
    }

    @Override
    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
        return this.isOpenable && super.isItemValidForSlot(index, stack);
    }

    @Override
    @Nonnull
    public ItemStack decrStackSize(int index, int count) {
        return this.isOpenable ? super.decrStackSize(index, count) : ItemStack.EMPTY;
    }

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nonnull Direction direction) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return this.isOpenable ? super.getCapability(capability, direction) : LazyOptional.empty();
        } else {
            return super.getCapability(capability, direction);
        }
    }
}
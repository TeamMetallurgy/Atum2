package com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity;

import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.blocks.base.tileentity.ChestBaseTileEntity;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.SarcophagusBlock;
import com.teammetallurgy.atum.entity.undead.PharaohEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumSounds;
import com.teammetallurgy.atum.init.AtumTileEntities;
import com.teammetallurgy.atum.network.NetworkHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SarcophagusTileEntity extends ChestBaseTileEntity {
    public static final String SARCOPHAGUS_CONTAINER = "atum.container.sarcophagus";
    public boolean hasSpawned;
    public boolean isOpenable;

    public SarcophagusTileEntity(BlockPos pos, BlockState state) {
        super(AtumTileEntities.SARCOPHAGUS.get(), pos, state, false, true, AtumBlocks.SARCOPHAGUS.get());
    }

    @Nullable
    @Override
    public Component getCustomName() {
        return Component.translatable(SARCOPHAGUS_CONTAINER);
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
    public void load(@Nonnull CompoundTag tag) {
        super.load(tag);
        this.hasSpawned = tag.getBoolean("spawned");
        this.isOpenable = tag.getBoolean("openable");
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("spawned", this.hasSpawned);
        tag.putBoolean("openable", this.isOpenable);
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        return this.isOpenable && super.stillValid(player);
    }

    public void setOpenable() {
        this.isOpenable = true;
        this.setChanged();
        if (this.level instanceof ServerLevel) {
            final Packet<?> packet = this.getUpdatePacket();
            NetworkHandler.sendToTracking((ServerLevel) this.level, this.worldPosition, packet, false);
        }
    }

    public void spawn(Player player, DifficultyInstance difficulty, RandomSource randomSource, @Nullable God god) {
        if (this.level != null && !this.level.isClientSide) {
            PharaohEntity pharaoh = AtumEntities.PHARAOH.get().create(this.level);
            if (pharaoh != null) {
                pharaoh.setDropsGodSpecificLoot(god != null);
                pharaoh.finalizeSpawn((ServerLevelAccessor) this.level, difficulty, god == null ? MobSpawnType.TRIGGERED : MobSpawnType.CONVERSION, null, null);
                if (god != null) {
                    pharaoh.setVariantWithAbilities(god.ordinal(), randomSource, difficulty);
                }
                Direction blockFacing = level.getBlockState(worldPosition).getValue(SarcophagusBlock.FACING);
                pharaoh.moveTo(this.worldPosition.getX(), this.worldPosition.getY() + 1, this.worldPosition.getZ(), blockFacing.toYRot() + 90, 0.0F);
                pharaoh.yHeadRot = blockFacing.toYRot() + 90;
                pharaoh.setSarcophagusPos(this.worldPosition);
                this.level.addFreshEntity(pharaoh);
                pharaoh.spawnGuards(pharaoh.blockPosition().relative(blockFacing, 2).below());
                pharaoh.spawnAnim();

                if (this.level instanceof ServerLevel serverLevel) {
                    God godVariant = God.getGod(pharaoh.getVariant());
                    Style pharaohStyle = pharaoh.getName().getStyle();
                    for (ServerPlayer playerMP : serverLevel.getServer().getPlayerList().getPlayers()) {
                        playerMP.sendSystemMessage(pharaoh.getName().copy().setStyle(pharaohStyle.withColor(godVariant.getColor())).append(Component.translatable("chat.atum.pharaoh_worshiper").withStyle(ChatFormatting.WHITE)).append(godVariant.getDisplayName().setStyle(pharaohStyle.withColor(godVariant.getColor()))).append(Component.translatable("chat.atum.pharaoh_awakened").withStyle(ChatFormatting.WHITE)).append(player.getName().copy().withStyle(ChatFormatting.YELLOW)));
                    }
                }
            }
        }
        this.level.playLocalSound(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), AtumSounds.PHARAOH_SPAWN.get(), SoundSource.HOSTILE, 0.8F, 1.0F, true);

        for (Direction horizontal : Direction.Plane.HORIZONTAL) {
            BlockEntity tileEntityOffset = this.level.getBlockEntity(this.worldPosition.relative(horizontal));
            if (tileEntityOffset instanceof SarcophagusTileEntity) {
                ((SarcophagusTileEntity) tileEntityOffset).hasSpawned = true;
            }
        }
        this.hasSpawned = true;
    }

    @Override
    public boolean canPlaceItem(int index, @Nonnull ItemStack stack) {
        return this.isOpenable && super.canPlaceItem(index, stack);
    }

    @Override
    @Nonnull
    public ItemStack removeItem(int index, int count) {
        return this.isOpenable ? super.removeItem(index, count) : ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nonnull Direction direction) {
        if (capability == ForgeCapabilities.ITEM_HANDLER) {
            return this.isOpenable ? super.getCapability(capability, direction) : LazyOptional.empty();
        } else {
            return super.getCapability(capability, direction);
        }
    }
}
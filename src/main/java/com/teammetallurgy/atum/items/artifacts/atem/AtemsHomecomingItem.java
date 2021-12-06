package com.teammetallurgy.atum.items.artifacts.atem;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.Set;

public class AtemsHomecomingItem extends Item implements IArtifact {

    public AtemsHomecomingItem() {
        super(new Item.Properties().durability(20).tab(Atum.GROUP).rarity(Rarity.RARE));
    }

    @Override
    public God getGod() {
        return God.ATEM;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.VANISHING_CURSE || enchantment == Enchantments.MENDING || enchantment == Enchantments.UNBREAKING;
    }

    @Override
    public int getItemEnchantability(@Nonnull ItemStack stack) {
        return 1;
    }

    @Override
    public boolean isValidRepairItem(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return repair.getItem() == AtumItems.NEBU_INGOT;
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, Player player, @Nonnull InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);

        if (recall(world, player) != null) {
            if (!player.isCreative()) {
                heldStack.hurtAndBreak(1, player, (e) -> {
                    e.broadcastBreakEvent(hand);
                });
            }
        }

        return new InteractionResultHolder<>(InteractionResult.PASS, heldStack);
    }

    public static BlockPos recall(Level world, Player player) {
        BlockPos pos = null;
        if (world instanceof ServerLevel) {
            ServerLevel serverWorld = (ServerLevel) world;
            if (player instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer) player;
                pos = serverPlayer.getRespawnPosition(); //Bed pos
                if (pos == null) {
                    BlockPos spawnPointPos = serverWorld.getSharedSpawnPos();
                    while (spawnPointPos.getY() > 1 && world.isEmptyBlock(spawnPointPos)) {
                        spawnPointPos = spawnPointPos.below();
                    }
                    while (!world.canSeeSkyFromBelowWater(spawnPointPos)) {
                        spawnPointPos = spawnPointPos.above();
                    }
                    pos = new BlockPos(spawnPointPos.getX(), spawnPointPos.getY(), spawnPointPos.getZ());
                }
            }
        }
        if (pos != null) {
            teleport(world, player, pos.getX(), pos.getY(), pos.getZ());
            onTeleport(world, player);
        }
        return pos;
    }

    private static void teleport(Level world, Entity entity, int x, int y, int z) {
        float yaw = entity.getYRot();
        float pitch = entity.getXRot();
        if (entity instanceof ServerPlayer) {
            Set<ClientboundPlayerPositionPacket.RelativeArgument> set = EnumSet.noneOf(ClientboundPlayerPositionPacket.RelativeArgument.class);
            float f = Mth.wrapDegrees(yaw);
            float f1 = Mth.wrapDegrees(pitch);

            entity.stopRiding();
            onTeleport(world, entity);
            ((ServerPlayer) entity).connection.teleport(x, y, z, f, f1, set);
            entity.setYHeadRot(f);
        } else {
            float f2 = Mth.wrapDegrees(yaw);
            float f3 = Mth.wrapDegrees(pitch);
            f3 = Mth.clamp(f3, -90.0F, 90.0F);
            entity.moveTo(x, y, z, f2, f3);
            entity.setYHeadRot(f2);
        }

        if (!(entity instanceof LivingEntity) || !((LivingEntity) entity).isFallFlying()) {
            Vec3 motion = entity.getDeltaMovement();
            entity.setDeltaMovement(new Vec3(motion.x, 0.0D, motion.z));
            entity.setOnGround(true);
        }
    }

    private static void onTeleport(Level world, Entity entity) {
        if (world instanceof ServerLevel) {
            float timesRandom = world.random.nextFloat() * 4.0F;
            float cosRandom = world.random.nextFloat() * ((float) Math.PI * 2F);
            double x = Mth.cos(cosRandom) * timesRandom;
            double y = 0.01D + (world.random.nextDouble() * 0.5D);
            double z = Mth.sin(cosRandom) * timesRandom;
            ServerLevel serverWorld = (ServerLevel) world;
            serverWorld.sendParticles(AtumParticles.LIGHT_SPARKLE, entity.getX() + x * 0.1D, entity.getY() + 0.3D, entity.getZ() + z * 0.1D, 250, 0.25D, y, 0.25D, 0.005D);
        }
        world.playSound(null, entity.blockPosition(), SoundEvents.SHULKER_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
    }
}

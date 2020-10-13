package com.teammetallurgy.atum.items.artifacts.atem;

import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.tools.AmuletItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

public class AtemsHomecomingItem extends AmuletItem { //TODO Test if works

    public AtemsHomecomingItem() {
        super(new Item.Properties().maxDamage(20));
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, PlayerEntity player, @Nonnull Hand hand) {
        ItemStack heldStack = player.getHeldItem(hand);
        BlockPos pos = null;
        if (world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) world;
            if (player instanceof ServerPlayerEntity) {
                ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
                Optional<Vector3d> optional = PlayerEntity.func_242374_a(serverWorld, serverPlayer.func_241140_K_(), serverPlayer.func_242109_L(), serverPlayer.func_241142_M_(), false);
                pos = optional.isPresent() ? new BlockPos(optional.get()) : null;
                if (pos == null) {
                    BlockPos spawnPointPos = serverWorld.getSpawnPoint();
                    while (spawnPointPos.getY() > 1 && world.isAirBlock(spawnPointPos)) {
                        spawnPointPos = spawnPointPos.down();
                    }
                    while (!world.canBlockSeeSky(spawnPointPos)) {
                        spawnPointPos = spawnPointPos.up();
                    }
                    pos = new BlockPos(spawnPointPos.getX(), spawnPointPos.getY(), spawnPointPos.getZ());
                }
            }
        }
        if (pos != null) {
            teleport(world, player, hand, pos.getX(), pos.getY(), pos.getZ());
            if (!player.isCreative()) {
                heldStack.damageItem(1, player, (e) -> {
                    e.sendBreakAnimation(hand);
                });
            }
            onTeleport(world, player);
        }
        return new ActionResult<>(ActionResultType.PASS, heldStack);
    }

    private static void teleport(World world, Entity entity, Hand hand, int x, int y, int z) {
        float yaw = entity.rotationYaw;
        float pitch = entity.rotationPitch;
        if (entity instanceof ServerPlayerEntity) {
            Set<SPlayerPositionLookPacket.Flags> set = EnumSet.noneOf(SPlayerPositionLookPacket.Flags.class);
            float f = MathHelper.wrapDegrees(yaw);
            float f1 = MathHelper.wrapDegrees(pitch);

            entity.stopRiding();
            onTeleport(world, entity);
            ((ServerPlayerEntity) entity).connection.setPlayerLocation(x, y, z, f, f1, set);
            entity.setRotationYawHead(f);
        } else {
            float f2 = MathHelper.wrapDegrees(yaw);
            float f3 = MathHelper.wrapDegrees(pitch);
            f3 = MathHelper.clamp(f3, -90.0F, 90.0F);
            entity.setLocationAndAngles(x, y, z, f2, f3);
            entity.setRotationYawHead(f2);
        }

        if (!(entity instanceof LivingEntity) || !((LivingEntity) entity).isElytraFlying()) {
            Vector3d motion = entity.getMotion();
            entity.setMotion(new Vector3d(motion.x, 0.0D, motion.z));
            entity.setOnGround(true);
        }
    }

    private static void onTeleport(World world, Entity entity) {
        if (world instanceof ServerWorld) {
            float timesRandom = world.rand.nextFloat() * 4.0F;
            float cosRandom = world.rand.nextFloat() * ((float) Math.PI * 2F);
            double x = MathHelper.cos(cosRandom) * timesRandom;
            double y = 0.01D + (world.rand.nextDouble() * 0.5D);
            double z = MathHelper.sin(cosRandom) * timesRandom;
            ServerWorld serverWorld = (ServerWorld) world;
            serverWorld.spawnParticle(AtumParticles.LIGHT_SPARKLE, entity.getPosX() + x * 0.1D, entity.getPosY() + 0.3D, entity.getPosZ() + z * 0.1D, 250, 0.25D, y, 0.25D, 0.005D);
        }
        world.playSound(null, entity.getPosition(), SoundEvents.ENTITY_SHULKER_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
    }
}
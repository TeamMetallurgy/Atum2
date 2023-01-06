package com.teammetallurgy.atum.items.artifacts.atem;

import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.misc.SpawnHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.AbstractMap;

public class AtemsHomecomingItem extends Item implements IArtifact {

    public AtemsHomecomingItem() {
        super(new Item.Properties().durability(20).rarity(Rarity.RARE));
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
    public int getEnchantmentValue(@Nonnull ItemStack stack) {
        return 1;
    }

    @Override
    public boolean isValidRepairItem(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return repair.getItem() == AtumItems.NEBU_INGOT.get();
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
        if (!(world instanceof ServerLevel serverLevel) ||
                !(player instanceof ServerPlayer serverPlayer))
            return null;
        AbstractMap.SimpleEntry<ServerLevel, BlockPos> pair = SpawnHelper.validateAndGetSpawnPoint(serverLevel, serverPlayer, 2);
        ServerLevel spawnLevel = pair.getKey();
        BlockPos spawnPos = pair.getValue();

        playTeleportEffects(world, player);
        serverPlayer.teleportTo(spawnLevel, spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, player.getYRot(), player.getXRot());
        playTeleportEffects(spawnLevel, player);
        return spawnPos;
    }

    private static void playTeleportEffects(Level level, Entity entity) {
        if (level instanceof ServerLevel serverLevel) {
            float timesRandom = serverLevel.random.nextFloat() * 4.0F;
            float cosRandom = serverLevel.random.nextFloat() * ((float) Math.PI * 2F);
            double x = Mth.cos(cosRandom) * timesRandom;
            double y = 0.01D + (serverLevel.random.nextDouble() * 0.5D);
            double z = Mth.sin(cosRandom) * timesRandom;
            BlockPos pos = entity.blockPosition();
            serverLevel.sendParticles(AtumParticles.LIGHT_SPARKLE.get(), entity.getX() + x * 0.1D, entity.getY() + 0.3D, entity.getZ() + z * 0.1D, 250, 0.25D, y, 0.25D, 0.005D);
            serverLevel.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.SHULKER_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }
}

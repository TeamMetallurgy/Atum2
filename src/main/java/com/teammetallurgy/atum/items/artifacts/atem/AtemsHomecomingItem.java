package com.teammetallurgy.atum.items.artifacts.atem;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.world.DimensionHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.*;

public class AtemsHomecomingItem extends Item implements IArtifact {

    public AtemsHomecomingItem() {
        super(new Item.Properties().maxDamage(20).group(Atum.GROUP).rarity(Rarity.RARE));
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
    public boolean getIsRepairable(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return repair.getItem() == AtumItems.NEBU_INGOT;
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, PlayerEntity player, @Nonnull Hand hand) {
        ItemStack heldStack = player.getHeldItem(hand);

        if (recall(world, player) != null) {
            if (!player.isCreative()) {
                heldStack.damageItem(1, player, (e) -> {
                    e.sendBreakAnimation(hand);
                });
            }
        }

        return new ActionResult<>(ActionResultType.PASS, heldStack);
    }

    public static BlockPos recall(World world, PlayerEntity player) {
        if (!(world instanceof ServerWorld) || !(player instanceof ServerPlayerEntity)) return null;
        ServerWorld serverWorld = (ServerWorld) world;
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        AbstractMap.SimpleEntry<ServerWorld, BlockPos> pair = DimensionHelper.validateAndGetSpawnPoint(serverWorld, serverPlayer, true);
        ServerWorld spawnWorld = pair.getKey();
        BlockPos spawnPos = pair.getValue();

        playTeleportEffects(world, player);
        serverPlayer.teleport(spawnWorld, spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, player.rotationYaw, player.rotationPitch);
        playTeleportEffects(spawnWorld, player);
        return spawnPos;
    }

    private static void playTeleportEffects(World world, Entity entity) {
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

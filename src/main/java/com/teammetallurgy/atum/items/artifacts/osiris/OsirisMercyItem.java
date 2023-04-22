package com.teammetallurgy.atum.items.artifacts.osiris;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.artifacts.AmuletItem;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.event.DropRulesEvent;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class OsirisMercyItem extends AmuletItem implements IArtifact {

    public OsirisMercyItem() {
        super(new Item.Properties().durability(1000));
    }

    @Override
    public God getGod() {
        return God.OSIRIS;
    }

    @Override
    public boolean isEnchantable(@Nonnull ItemStack stack) {
        return false;
    }

    @SubscribeEvent
    public static void onCuriosDrop(DropRulesEvent event) {
        if (event.getEntity() instanceof Player player) {
            CompoundTag playerData = getPlayerData(player);
            if (!player.level.isClientSide && playerData.contains("Inventory")) { //Keep all Curios items
                CuriosApi.getCuriosHelper().getEquippedCurios(player).ifPresent(handler -> {
                    for (int i = 0; i < handler.getSlots(); ++i) {
                        int finalI = i;
                        event.addOverride(stack -> stack == handler.getStackInSlot(finalI), ICurio.DropRule.ALWAYS_KEEP);
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        LivingEntity livingEntity = event.getEntity();
        Optional<ImmutableTriple<String, Integer, ItemStack>> optional = CuriosApi.getCuriosHelper().findEquippedCurio(AtumItems.OSIRIS_MERCY.get(), livingEntity);
        if (optional.isPresent()) {
            if (event.getEntity() instanceof Player) {
                ItemStack anubisMercy = optional.get().getRight();
                Player player = (Player) livingEntity;
                if (!player.level.isClientSide) {
                    anubisMercy.hurtAndBreak(334, player, (e) -> {
                        e.broadcastBreakEvent(e.getUsedItemHand());
                    });

                    ListTag tagList = new ListTag();
                    player.getInventory().save(tagList);

                    getPlayerData(player).put("Inventory", tagList);

                    player.getInventory().items.clear();
                    player.getInventory().armor.clear();
                    player.getInventory().offhand.clear();
                }

                if (player.level instanceof ServerLevel serverLevel) {
                    RandomSource random = serverLevel.random;
                    double y = Mth.nextDouble(random, 0.01D, 0.1D);
                    serverLevel.sendParticles(AtumParticles.ANUBIS_SKULL.get(), player.getX() + (random.nextDouble() - 0.5D) * (double) player.getBbWidth(), player.getY() + 1.0D, player.getZ() + (random.nextDouble() - 0.5D) * (double) player.getBbWidth(), 22, 0.04D, y, 0.0D, 0.075D);
                    serverLevel.sendParticles(AtumParticles.ANUBIS_SKULL.get(), player.getX() + (random.nextDouble() - 0.5D) * (double) player.getBbWidth(), player.getY() + 1.0D, player.getZ() + (random.nextDouble() - 0.5D) * (double) player.getBbWidth(), 22, 0.0D, y, 0.04D, 0.075D);
                    serverLevel.sendParticles(AtumParticles.ANUBIS_SKULL.get(), player.getX() + (random.nextDouble() - 0.5D) * (double) player.getBbWidth(), player.getY() + 1.0D, player.getZ() + (random.nextDouble() - 0.5D) * (double) player.getBbWidth(), 22, -0.04D, y, 0.0D, 0.075D);
                    serverLevel.sendParticles(AtumParticles.ANUBIS_SKULL.get(), player.getX() + (random.nextDouble() - 0.5D) * (double) player.getBbWidth(), player.getY() + 1.0D, player.getZ() + (random.nextDouble() - 0.5D) * (double) player.getBbWidth(), 22, 0.0D, y, -0.04D, 0.075D);
                }
                player.level.playSound(null, player.blockPosition(), SoundEvents.GHAST_DEATH, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        CompoundTag playerData = getPlayerData(player);
        if (!player.level.isClientSide && playerData.contains("Inventory")) {
            ListTag tagList = playerData.getList("Inventory", 10);
            player.getInventory().load(tagList);
            getPlayerData(player).remove("Inventory");
        }
    }

    private static CompoundTag getPlayerData(Player player) {
        if (!player.getPersistentData().contains(Player.PERSISTED_NBT_TAG)) {
            player.getPersistentData().put(Player.PERSISTED_NBT_TAG, new CompoundTag());
        }
        return player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, List<Component> tooltip, @Nonnull TooltipFlag tooltipType) {
        int remaining = (stack.getMaxDamage() - stack.getDamageValue()) / 332;
        tooltip.add(Component.translatable("atum.tooltip.uses_remaining", remaining));

        tooltip.add(Component.translatable(Atum.MOD_ID + "." + Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(this)).getPath() + ".disenchantment_curse").withStyle(ChatFormatting.DARK_RED));
    }
}
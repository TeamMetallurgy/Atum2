package com.teammetallurgy.atum.items.artifacts.anubis;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.tools.AmuletItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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
public class AnubisMercyItem extends AmuletItem implements IArtifact {

    public AnubisMercyItem() {
        super(new Item.Properties().maxDamage(1000));
    }

    @Override
    public God getGod() {
        return God.ANUBIS;
    }

    @Override
    public boolean isEnchantable(@Nonnull ItemStack stack) {
        return false;
    }

    @SubscribeEvent
    public static void onCuriosDrop(DropRulesEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            CompoundNBT playerData = getPlayerData(player);
            if (!player.world.isRemote && playerData.contains("Inventory")) { //Keep all Curios items
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
        LivingEntity livingEntity = event.getEntityLiving();
        Optional<ImmutableTriple<String, Integer, ItemStack>> optional = CuriosApi.getCuriosHelper().findEquippedCurio(AtumItems.ANUBIS_MERCY, livingEntity);
        if (optional.isPresent()) {
            if (event.getEntityLiving() instanceof PlayerEntity) {
                ItemStack anubisMercy = optional.get().getRight();
                PlayerEntity player = (PlayerEntity) livingEntity;
                if (!player.world.isRemote) {
                    anubisMercy.damageItem(334, player, (e) -> {
                        e.sendBreakAnimation(e.getActiveHand());
                    });

                    ListNBT tagList = new ListNBT();
                    player.inventory.write(tagList);

                    getPlayerData(player).put("Inventory", tagList);

                    player.inventory.mainInventory.clear();
                    player.inventory.armorInventory.clear();
                    player.inventory.offHandInventory.clear();
                }

                if (player.world instanceof ServerWorld) {
                    ServerWorld serverWorld = (ServerWorld) player.world;
                    double y = MathHelper.nextDouble(random, 0.01D, 0.1D);
                    serverWorld.spawnParticle(AtumParticles.ANUBIS_SKULL, player.getPosX() + (random.nextDouble() - 0.5D) * (double) player.getWidth(), player.getPosY() + 1.0D, player.getPosZ() + (random.nextDouble() - 0.5D) * (double) player.getWidth(), 22, 0.04D, y, 0.0D, 0.075D);
                    serverWorld.spawnParticle(AtumParticles.ANUBIS_SKULL, player.getPosX() + (random.nextDouble() - 0.5D) * (double) player.getWidth(), player.getPosY() + 1.0D, player.getPosZ() + (random.nextDouble() - 0.5D) * (double) player.getWidth(), 22, 0.0D, y, 0.04D, 0.075D);
                    serverWorld.spawnParticle(AtumParticles.ANUBIS_SKULL, player.getPosX() + (random.nextDouble() - 0.5D) * (double) player.getWidth(), player.getPosY() + 1.0D, player.getPosZ() + (random.nextDouble() - 0.5D) * (double) player.getWidth(), 22, -0.04D, y, 0.0D, 0.075D);
                    serverWorld.spawnParticle(AtumParticles.ANUBIS_SKULL, player.getPosX() + (random.nextDouble() - 0.5D) * (double) player.getWidth(), player.getPosY() + 1.0D, player.getPosZ() + (random.nextDouble() - 0.5D) * (double) player.getWidth(), 22, 0.0D, y, -0.04D, 0.075D);
                }
                player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_GHAST_DEATH, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
        }
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        PlayerEntity player = event.getPlayer();
        CompoundNBT playerData = getPlayerData(player);
        if (!player.world.isRemote && playerData.contains("Inventory")) {
            ListNBT tagList = playerData.getList("Inventory", 10);
            player.inventory.read(tagList);
            getPlayerData(player).remove("Inventory");
        }
    }

    private static CompoundNBT getPlayerData(PlayerEntity player) {
        if (!player.getPersistentData().contains(PlayerEntity.PERSISTED_NBT_TAG)) {
            player.getPersistentData().put(PlayerEntity.PERSISTED_NBT_TAG, new CompoundNBT());
        }
        return player.getPersistentData().getCompound(PlayerEntity.PERSISTED_NBT_TAG);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, @Nonnull ITooltipFlag tooltipType) {
        int remaining = (stack.getMaxDamage() - stack.getDamage()) / 332;
        tooltip.add(new TranslationTextComponent("atum.tooltip.uses_remaining", remaining));

        tooltip.add(new TranslationTextComponent(Atum.MOD_ID + "." + Objects.requireNonNull(this.getRegistryName()).getPath() + ".disenchantment_curse").mergeStyle(TextFormatting.DARK_RED));
    }
}
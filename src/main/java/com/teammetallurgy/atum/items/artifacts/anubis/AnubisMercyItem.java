package com.teammetallurgy.atum.items.artifacts.anubis;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.AmuletItem;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class AnubisMercyItem extends AmuletItem {

    public AnubisMercyItem() {
        super(new Item.Properties().maxDamage(1000));
    }

    @Override
    public boolean isEnchantable(@Nonnull ItemStack stack) {
        return false;
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntityLiving();
        Hand hand = entity.getHeldItem(Hand.OFF_HAND).getItem() == AtumItems.ANUBIS_MERCY ? Hand.OFF_HAND : Hand.MAIN_HAND;
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            ItemStack heldStack = player.getHeldItem(hand);
            /*if (IS_BAUBLES_INSTALLED && getAmulet(player).getItem() == AtumItems.ANUBIS_MERCY) {
                heldStack = getAmulet(player);
            }*/
            if (heldStack.getItem() == AtumItems.ANUBIS_MERCY) {
                if (!player.world.isRemote) {
                    heldStack.damageItem(334, player, (e) -> {
                        e.sendBreakAnimation(hand);
                    });

                    ListNBT tagList = new ListNBT();
                    player.inventory.write(tagList);

                    /*if (IS_BAUBLES_INSTALLED) {
                        for (int slot = 0; slot < getBaublesInventory(player).getSizeInventory(); ++slot) {
                            if (!getBaublesInventory(player).getStackInSlot(slot).isEmpty()) {
                                CompoundNBT tag = new CompoundNBT();
                                tag.putByte("Slot", (byte) (slot + 200));
                                getBaublesInventory(player).getStackInSlot(slot).writeToNBT(tag);
                                tagList.add(tag);
                            }
                        }
                        getBaublesInventory(player).clear();
                    }*/
                    getPlayerData(player).put("Inventory", tagList);

                    player.inventory.mainInventory.clear();
                    player.inventory.armorInventory.clear();
                    player.inventory.offHandInventory.clear();
                }

                double y = MathHelper.nextDouble(random, 0.01D, 0.1D);
                for (int l = 0; l < 22; ++l) {
                    Atum.proxy.spawnParticle(AtumParticles.Types.ANUBIS_SKULL, player, player.posX + (random.nextDouble() - 0.5D) * (double) player.getWidth(), player.posY + 1.0D, player.posZ + (random.nextDouble() - 0.5D) * (double) player.getWidth(), 0.04D, y, 0.0D);
                    Atum.proxy.spawnParticle(AtumParticles.Types.ANUBIS_SKULL, player, player.posX + (random.nextDouble() - 0.5D) * (double) player.getWidth(), player.posY + 1.0D, player.posZ + (random.nextDouble() - 0.5D) * (double) player.getWidth(), 0.0D, y, 0.04D);
                    Atum.proxy.spawnParticle(AtumParticles.Types.ANUBIS_SKULL, player, player.posX + (random.nextDouble() - 0.5D) * (double) player.getWidth(), player.posY + 1.0D, player.posZ + (random.nextDouble() - 0.5D) * (double) player.getWidth(), -0.04D, y, 0.0D);
                    Atum.proxy.spawnParticle(AtumParticles.Types.ANUBIS_SKULL, player, player.posX + (random.nextDouble() - 0.5D) * (double) player.getWidth(), player.posY + 1.0D, player.posZ + (random.nextDouble() - 0.5D) * (double) player.getWidth(), 0.0D, y, -0.04D);
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

            /*if (IS_BAUBLES_INSTALLED) {
                getBaublesInventory(player).clear();
                for (int count = 0; count < tagList.size(); ++count) {
                    CompoundNBT tag = tagList.getCompound(count);
                    int j = tag.getByte("Slot") & 255;
                    ItemStack stack = ItemStack.read(tag);
                    if (!stack.isEmpty()) {
                        if (j >= 200 && j < getBaublesInventory(player).getSizeInventory() + 200) {
                            getBaublesInventory(player).setInventorySlotContents(j - 200, stack);
                        }
                    }
                }
            }*/
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
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag tooltipType) {
        int remaining = (stack.getMaxDamage() - stack.getDamage()) / 332;
        tooltip.add(new TranslationTextComponent("atum.tooltip.uses_remaining", remaining));

        tooltip.add(new TranslationTextComponent(Constants.MOD_ID + "." + Objects.requireNonNull(this.getRegistryName()).getPath() + ".disenchantment_curse").applyTextStyle(TextFormatting.DARK_RED));
    }
}
package com.teammetallurgy.atum.items.artifacts.anubis;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber
public class ItemAnubisMercy extends Item {

    public ItemAnubisMercy() {
        super();
        this.setMaxStackSize(1);
        this.setMaxDamage(1000);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    @Nonnull
    public EnumRarity getRarity(@Nonnull ItemStack stack) {
        return EnumRarity.RARE;
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        EnumHand hand = entity.getHeldItem(EnumHand.OFF_HAND).getItem() == AtumItems.ANUBIS_MERCY ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
        ItemStack heldStack = entity.getHeldItem(hand);
        if (event.getEntityLiving() instanceof EntityPlayer && heldStack.getItem() == AtumItems.ANUBIS_MERCY) {
            EntityPlayer player = (EntityPlayer) entity;

            if (!entity.world.isRemote ) {
                heldStack.damageItem(334, player);
                NBTTagList tagList = new NBTTagList();
                player.inventory.writeToNBT(tagList);
                getPlayerData(player).setTag("Inventory", tagList);
                player.inventory.mainInventory.clear();
                player.inventory.armorInventory.clear();
                player.inventory.offHandInventory.clear();
            }
            for (int l = 0; l < 12; ++l) {
                Atum.proxy.spawnParticle(AtumParticles.Types.ANUBIS, player, player.posX + (itemRand.nextDouble() - 0.5D) * (double) player.width, player.posY + 1.0D, player.posZ + (itemRand.nextDouble() - 0.5D) * (double) player.width, 0.05D, 0.0D, 0.0D);
                Atum.proxy.spawnParticle(AtumParticles.Types.ANUBIS, player, player.posX + (itemRand.nextDouble() - 0.5D) * (double) player.width, player.posY + 1.0D, player.posZ + (itemRand.nextDouble() - 0.5D) * (double) player.width, 0.0D, 0.0D, 0.05D);
                Atum.proxy.spawnParticle(AtumParticles.Types.ANUBIS, player, player.posX + (itemRand.nextDouble() - 0.5D) * (double) player.width, player.posY + 1.0D, player.posZ + (itemRand.nextDouble() - 0.5D) * (double) player.width, -0.05D, 0.0D, 0.0D);
                Atum.proxy.spawnParticle(AtumParticles.Types.ANUBIS, player, player.posX + (itemRand.nextDouble() - 0.5D) * (double) player.width, player.posY + 1.0D, player.posZ + (itemRand.nextDouble() - 0.5D) * (double) player.width, 0.0D, 0.0D, -0.05D);
            }
            player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_GHAST_DEATH, SoundCategory.PLAYERS, 1.0F, 1.0F);
        }
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        EntityPlayer player = event.player;
        NBTTagCompound tag = getPlayerData(player);
        if (!event.player.world.isRemote && tag.hasKey("Inventory")) {
            NBTTagList tagList = tag.getTagList("Inventory", 10);
            player.inventory.readFromNBT(tagList);

            getPlayerData(player).removeTag("Inventory");
        }
    }

    private static NBTTagCompound getPlayerData(EntityPlayer player) {
        if (!player.getEntityData().hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
            player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
        }
        return player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag tooltipType) {
        if (Keyboard.isKeyDown(42)) {
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getTranslationKey() + ".line1"));
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getTranslationKey() + ".line2"));
        } else {
            tooltip.add(I18n.format(this.getTranslationKey() + ".line3") + " " + TextFormatting.DARK_GRAY + "[SHIFT]");
        }

        int remaining = stack.getMaxDamage() - stack.getItemDamage();
        String localizedRemaining = I18n.format("tooltip.atum.usesRemaining", remaining);
        tooltip.add(localizedRemaining);
    }
}
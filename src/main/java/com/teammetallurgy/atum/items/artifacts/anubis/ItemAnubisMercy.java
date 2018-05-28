package com.teammetallurgy.atum.items.artifacts.anubis;

import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
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
        this.setMaxDamage(16);
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
        if (!entity.world.isRemote && event.getEntityLiving() instanceof EntityPlayer && heldStack.getItem() == AtumItems.ANUBIS_MERCY) {
            EntityPlayer player = (EntityPlayer) entity;

            heldStack.damageItem(1, player);
            NBTTagList tagList = new NBTTagList();
            player.inventory.writeToNBT(tagList);
            getPlayerData(player).setTag("Inventory", tagList);
            player.inventory.mainInventory.clear();
            player.inventory.armorInventory.clear();
            player.inventory.offHandInventory.clear();
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
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getUnlocalizedName() + ".line1"));
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getUnlocalizedName() + ".line2"));
        } else {
            tooltip.add(I18n.format(this.getUnlocalizedName() + ".line3") + " " + TextFormatting.DARK_GRAY + "[SHIFT]");
        }

        int remaining = stack.getMaxDamage() - stack.getItemDamage();
        String localizedRemaining = I18n.format("tooltip.atum.usesRemaining", remaining);
        tooltip.add(localizedRemaining);
    }
}
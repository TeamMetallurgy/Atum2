package com.teammetallurgy.atum.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;

import javax.annotation.Nonnull;

/**
 * Various helper methods, based around {@link ItemStack}
 */
public class StackHelper {

    /**
     * Gets the NBTTagCompound from the ItemStack.
     * If the ItemStack does not have any NBTTagCompounds, a new empty one will be given
     *
     * @param stack the stack you wish to check the NBTTagCompound of
     * @return the stacks tag
     */
    public static NBTTagCompound getTag(@Nonnull ItemStack stack) {
        if (!hasTag(stack)) {
            stack.setTagCompound(new NBTTagCompound());
        }
        return stack.getTagCompound();
    }

    /**
     * Checks if the ItemStack have a NBTTagCompound associated with it
     *
     * @param stack the stack
     * @return whether or not the stack have a tag
     */
    public static boolean hasTag(@Nonnull ItemStack stack) {
        return stack.hasTagCompound();
    }

    /**
     * Checks if the ItemStack have the specified NBTTagCompound key
     *
     * @param stack the stack
     * @param string the NBTTagCompound string key
     * @return whether the stack have the key or not
     */
    public static boolean hasKey(@Nonnull ItemStack stack, String string) {
        return stack.getTagCompound() != null && stack.getTagCompound().hasKey(string);
    }

    /*
     * Gives the specified ItemStack to the player
     */
    public static void giveItem(EntityPlayer player, EnumHand hand, @Nonnull ItemStack stack) {
        if (player.getHeldItem(hand).isEmpty()) {
            player.setHeldItem(hand, stack);
        } else if (!player.inventory.addItemStackToInventory(stack)) {
            player.dropItem(stack, false);
        } else if (player instanceof EntityPlayerMP) {
            ((EntityPlayerMP) player).sendContainerToPlayer(player.inventoryContainer);
        }
    }
}
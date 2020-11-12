package com.teammetallurgy.atum.misc;

import com.teammetallurgy.atum.Atum;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * Various helper methods, based around {@link ItemStack}
 */
public class StackHelper {

    /**
     * Gets the CompoundNBT from the ItemStack.
     * If the ItemStack does not have any NBTTagCompounds, a new empty one will be given
     *
     * @param stack the stack you wish to check the CompoundNBT of
     * @return the stacks tag
     */
    public static CompoundNBT getTag(@Nonnull ItemStack stack) {
        if (!hasTag(stack)) {
            stack.setTag(new CompoundNBT());
        }
        return stack.getTag();
    }

    /**
     * Checks if the ItemStack have a CompoundNBT associated with it
     *
     * @param stack the stack
     * @return whether or not the stack have a tag
     */
    public static boolean hasTag(@Nonnull ItemStack stack) {
        return stack.hasTag();
    }

    /**
     * Checks if the ItemStack have the specified CompoundNBT key
     *
     * @param stack  the stack
     * @param string the CompoundNBT string key
     * @return whether the stack have the key or not
     */
    public static boolean hasKey(@Nonnull ItemStack stack, String string) {
        return stack.getTag() != null && stack.getTag().contains(string);
    }

    public static Block getBlockFromName(String name) {
        return getBlockFromName(new ResourceLocation(Atum.MOD_ID, name));
    }

    public static Block getBlockFromName(ResourceLocation location) {
        return ForgeRegistries.BLOCKS.getValue(location);
    }

    /*
     * Gives the specified ItemStack to the player
     */
    public static void giveItem(PlayerEntity player, Hand hand, @Nonnull ItemStack stack) {
        if (!player.inventory.addItemStackToInventory(stack)) {
            player.dropItem(stack, false);
        } else if (player instanceof ServerPlayerEntity) {
            ((ServerPlayerEntity) player).sendContainerToPlayer(player.container);
        }
    }

    public static void dropInventoryItems(World world, BlockPos pos, IInventory inventory) {
        for (int slot = 0; slot < inventory.getSizeInventory(); ++slot) {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (!stack.isEmpty()) {
                spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
            }
        }
    }

    public static void spawnItemStack(World world, double x, double y, double z, @Nonnull ItemStack stack) {
        final Random random = new Random();
        float xOffset = random.nextFloat() * 0.8F + 0.1F;
        float yOffset = random.nextFloat() * 0.8F + 0.1F;
        float zOffset = random.nextFloat() * 0.8F + 0.1F;
        while (!stack.isEmpty()) {
            ItemEntity itemEntity = new ItemEntity(world, x + (double) xOffset, y + (double) yOffset, z + (double) zOffset, stack.split(random.nextInt(21) + 10));
            itemEntity.setDefaultPickupDelay();
            itemEntity.setMotion(random.nextGaussian() * 0.05000000074505806D, random.nextGaussian() * 0.05000000074505806D + 0.20000000298023224D, random.nextGaussian() * 0.05000000074505806D);
            if (!world.isRemote) {
                world.addEntity(itemEntity);
            }
        }
    }

    public static boolean areIngredientsEqualIgnoreSize(@Nonnull Ingredient ingredientA, @Nonnull ItemStack stackB) {
        for (ItemStack stack : ingredientA.getMatchingStacks()) {
            if (areStacksEqualIgnoreSize(stack, stackB)) {
                return true;
            }
        }
        return false;
    }

    public static boolean areStacksEqualIgnoreSize(@Nonnull ItemStack stackA, @Nonnull ItemStack stackB) {
        if (stackA.isEmpty() || stackB.isEmpty()) {
            return false;
        } else if (stackA.getItem() != stackB.getItem()) {
            return false;
        } else {
            return (stackA.getTag() == null || stackA.getTag().equals(stackB.getTag())) && stackA.areCapsCompatible(stackB);
        }
    }

    public static Hand getUsedHand(@Nonnull ItemStack stackMainHand, Class<? extends Item> clazz) {
        return clazz.isAssignableFrom(stackMainHand.getItem().getClass()) ? Hand.MAIN_HAND : Hand.OFF_HAND;
    }

    public static boolean hasFullArmorSet(LivingEntity livingEntity, Item head, Item chest, Item legs, Item feet) {
        return livingEntity.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == head &&
                livingEntity.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() == chest &&
                livingEntity.getItemStackFromSlot(EquipmentSlotType.LEGS).getItem() == legs &&
                livingEntity.getItemStackFromSlot(EquipmentSlotType.FEET).getItem() == feet;
    }
}
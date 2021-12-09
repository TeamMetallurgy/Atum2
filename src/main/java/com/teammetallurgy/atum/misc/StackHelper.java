package com.teammetallurgy.atum.misc;

import com.teammetallurgy.atum.Atum;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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
    public static CompoundTag getTag(@Nonnull ItemStack stack) {
        if (!hasTag(stack)) {
            stack.setTag(new CompoundTag());
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

    public static Item getItemFromName(String name) {
        return getItemFromName(new ResourceLocation(Atum.MOD_ID, name));
    }

    public static Item getItemFromName(ResourceLocation location) {
        return ForgeRegistries.ITEMS.getValue(location);
    }

    /*
     * Gives the specified ItemStack to the player
     */
    public static void giveItem(Player player, InteractionHand hand, @Nonnull ItemStack stack) {
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        } else if (player instanceof ServerPlayer) {
            //((ServerPlayer) player).refreshContainer(player.inventoryMenu); //TODO
        }
    }

    public static void dropInventoryItems(Level world, BlockPos pos, Container inventory) {
        for (int slot = 0; slot < inventory.getContainerSize(); ++slot) {
            ItemStack stack = inventory.getItem(slot);
            if (!stack.isEmpty()) {
                spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
            }
        }
    }

    public static void spawnItemStack(Level world, double x, double y, double z, @Nonnull ItemStack stack) {
        final Random random = new Random();
        float xOffset = random.nextFloat() * 0.8F + 0.1F;
        float yOffset = random.nextFloat() * 0.8F + 0.1F;
        float zOffset = random.nextFloat() * 0.8F + 0.1F;
        while (!stack.isEmpty()) {
            ItemEntity itemEntity = new ItemEntity(world, x + (double) xOffset, y + (double) yOffset, z + (double) zOffset, stack.split(random.nextInt(21) + 10));
            itemEntity.setDefaultPickUpDelay();
            itemEntity.setDeltaMovement(random.nextGaussian() * 0.05000000074505806D, random.nextGaussian() * 0.05000000074505806D + 0.20000000298023224D, random.nextGaussian() * 0.05000000074505806D);
            if (!world.isClientSide) {
                world.addFreshEntity(itemEntity);
            }
        }
    }

    public static boolean areIngredientsEqualIgnoreSize(@Nonnull Ingredient ingredientA, @Nonnull ItemStack stackB) {
        for (ItemStack stack : ingredientA.getItems()) {
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

    public static InteractionHand getUsedHand(@Nonnull ItemStack stackMainHand, Class<? extends Item> clazz) {
        return clazz.isAssignableFrom(stackMainHand.getItem().getClass()) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }

    public static boolean hasFullArmorSet(LivingEntity livingEntity, Item head, Item chest, Item legs, Item feet) {
        return livingEntity.getItemBySlot(EquipmentSlot.HEAD).getItem() == head &&
                livingEntity.getItemBySlot(EquipmentSlot.CHEST).getItem() == chest &&
                livingEntity.getItemBySlot(EquipmentSlot.LEGS).getItem() == legs &&
                livingEntity.getItemBySlot(EquipmentSlot.FEET).getItem() == feet;
    }
}
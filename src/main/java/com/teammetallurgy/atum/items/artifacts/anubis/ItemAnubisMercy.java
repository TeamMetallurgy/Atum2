package com.teammetallurgy.atum.items.artifacts.anubis;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.ItemAmulet;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
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

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class ItemAnubisMercy extends ItemAmulet {

    public ItemAnubisMercy() {
        super();
        this.setMaxDamage(1000);
    }

    @Override
    public boolean isEnchantable(@Nonnull ItemStack stack) {
        return false;
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        EnumHand hand = entity.getHeldItem(EnumHand.OFF_HAND).getItem() == AtumItems.ANUBIS_MERCY ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            ItemStack heldStack = player.getHeldItem(hand);
            if (IS_BAUBLES_INSTALLED && getAmulet(player).getItem() == AtumItems.ANUBIS_MERCY) {
                heldStack = getAmulet(player);
            }
            if (heldStack.getItem() == AtumItems.ANUBIS_MERCY) {
                if (!player.world.isRemote) {
                    heldStack.damageItem(334, player);

                    NBTTagList tagList = new NBTTagList();
                    player.inventory.writeToNBT(tagList);

                    if (IS_BAUBLES_INSTALLED) {
                        for (int slot = 0; slot < getBaublesInventory(player).getSizeInventory(); ++slot) {
                            if (!getBaublesInventory(player).getStackInSlot(slot).isEmpty()) {
                                NBTTagCompound tag = new NBTTagCompound();
                                tag.setByte("Slot", (byte) (slot + 200));
                                getBaublesInventory(player).getStackInSlot(slot).writeToNBT(tag);
                                tagList.appendTag(tag);
                            }
                        }
                        getBaublesInventory(player).clear();
                    }
                    getPlayerData(player).setTag("Inventory", tagList);

                    player.inventory.mainInventory.clear();
                    player.inventory.armorInventory.clear();
                    player.inventory.offHandInventory.clear();
                }

                double y = MathHelper.nextDouble(itemRand, 0.01D, 0.1D);
                for (int l = 0; l < 22; ++l) {
                    Atum.proxy.spawnParticle(AtumParticles.Types.ANUBIS_SKULL, player, player.posX + (itemRand.nextDouble() - 0.5D) * (double) player.width, player.posY + 1.0D, player.posZ + (itemRand.nextDouble() - 0.5D) * (double) player.width, 0.04D, y, 0.0D);
                    Atum.proxy.spawnParticle(AtumParticles.Types.ANUBIS_SKULL, player, player.posX + (itemRand.nextDouble() - 0.5D) * (double) player.width, player.posY + 1.0D, player.posZ + (itemRand.nextDouble() - 0.5D) * (double) player.width, 0.0D, y, 0.04D);
                    Atum.proxy.spawnParticle(AtumParticles.Types.ANUBIS_SKULL, player, player.posX + (itemRand.nextDouble() - 0.5D) * (double) player.width, player.posY + 1.0D, player.posZ + (itemRand.nextDouble() - 0.5D) * (double) player.width, -0.04D, y, 0.0D);
                    Atum.proxy.spawnParticle(AtumParticles.Types.ANUBIS_SKULL, player, player.posX + (itemRand.nextDouble() - 0.5D) * (double) player.width, player.posY + 1.0D, player.posZ + (itemRand.nextDouble() - 0.5D) * (double) player.width, 0.0D, y, -0.04D);
                }
                player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_GHAST_DEATH, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
        }
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        EntityPlayer player = event.player;
        NBTTagCompound playerData = getPlayerData(player);
        if (!event.player.world.isRemote && playerData.hasKey("Inventory")) {
            NBTTagList tagList = playerData.getTagList("Inventory", 10);
            player.inventory.readFromNBT(tagList);

            if (IS_BAUBLES_INSTALLED) {
                getBaublesInventory(player).clear();
                for (int count = 0; count < tagList.tagCount(); ++count) {
                    NBTTagCompound tag = tagList.getCompoundTagAt(count);
                    int j = tag.getByte("Slot") & 255;
                    ItemStack stack = new ItemStack(tag);
                    if (!stack.isEmpty()) {
                        if (j >= 200 && j < getBaublesInventory(player).getSizeInventory() + 200) {
                            getBaublesInventory(player).setInventorySlotContents(j - 200, stack);
                        }
                    }
                }
            }
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
            tooltip.add(TextFormatting.DARK_RED + I18n.format(this.getTranslationKey() + ".line4"));
        }

        int remaining = stack.getMaxDamage() - stack.getItemDamage();
        String localizedRemaining = I18n.format("tooltip.atum.usesRemaining", remaining);
        tooltip.add(localizedRemaining);
    }
}
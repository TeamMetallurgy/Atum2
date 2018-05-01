package com.teammetallurgy.atum.items.artifacts;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(value = Side.CLIENT)
public class ItemAnubisMercy extends Item {

    public ItemAnubisMercy() {
        super();
        this.setMaxDamage(20);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @SubscribeEvent
    public void onDamage(LivingHurtEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            ItemStack stack = ItemStack.EMPTY;
            NonNullList<ItemStack> damageAmount = player.inventory.mainInventory;
            int resistance = damageAmount.size();

            for (int i = 0; i < resistance; ++i) {
                ItemStack currStack = damageAmount.get(i);
                if (!currStack.isEmpty() && currStack.getItem() == this) {
                    stack = currStack;
                    break;
                }
            }

            if (stack.isEmpty()) {
                return;
            }

            float amount = event.getAmount();
            if (!event.getSource().isUnblockable()) {
                amount = (event.getAmount() * (25 - player.getTotalArmorValue()) + player.getAbsorptionAmount()) / 25.0F;
            }

            if (player.isPotionActive(MobEffects.RESISTANCE)) {
                resistance = 25 - (player.getActivePotionEffect(MobEffects.RESISTANCE).getAmplifier() + 1) * 5;
                amount = amount * (float) resistance / 25.0F;
            }

            if (Math.ceil((double) amount) >= (double) player.getHealth()) {
                event.setCanceled(true);
                this.respawnPlayer(event.getEntityLiving().world, player);
                player.setHealth(player.getMaxHealth());
                player.getFoodStats().setFoodLevel(20);
                player.getFoodStats().setFoodSaturationLevel(20.0F);
                stack.damageItem(1, player);
                if (stack.getItemDamage() >= 20) {
                    stack = ItemStack.EMPTY;
                }
            }
        }

    }

    private void respawnPlayer(World world, EntityPlayer player) {
        BlockPos spawn = player.getBedLocation(player.dimension);
        if (spawn == null) {
            spawn = world.getSpawnPoint();
        }

        if (spawn == null) {
            spawn = world.getSpawnPoint();
        }

        spawn = EntityPlayer.getBedSpawnLocation(world, spawn, false);
        if (spawn == null) {
            spawn = world.getSpawnPoint();
        }

        player.rotationPitch = 0.0F;
        player.rotationYaw = 0.0F;
        player.setPositionAndUpdate((double) spawn.getX() + 0.5D, (double) spawn.getY() + 0.1D, (double) spawn.getZ());

        while (!world.getCollisionBoxes(player, player.getEntityBoundingBox()).isEmpty()) {
            player.setPosition(player.posX, player.posY + 1.0D, player.posZ);
        }
    }

    @Override
    @Nonnull
    public EnumRarity getRarity(@Nonnull ItemStack stack) {
        return EnumRarity.RARE;
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
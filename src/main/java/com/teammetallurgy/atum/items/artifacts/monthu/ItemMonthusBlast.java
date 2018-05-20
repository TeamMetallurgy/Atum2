package com.teammetallurgy.atum.items.artifacts.monthu;

import com.teammetallurgy.atum.entity.arrow.EntityArrowExplosive;
import com.teammetallurgy.atum.items.tools.ItemBaseBow;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class ItemMonthusBlast extends ItemBaseBow {

    public ItemMonthusBlast() {
        super();
        this.setMaxDamage(650);
        this.setRepairItem(Items.DIAMOND);
    }

    @Override
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    @Nonnull
    public EnumRarity getRarity(@Nonnull ItemStack stack) {
        return EnumRarity.RARE;
    }

    @Override
    public void onPlayerStoppedUsing(@Nonnull ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            boolean infinity = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack ammoStack = this.findAmmo(player);

            int i = this.getMaxItemUseDuration(stack) - timeLeft;
            i = ForgeEventFactory.onArrowLoose(stack, world, player, i, !ammoStack.isEmpty() || infinity);
            if (i < 0) return;

            if (!ammoStack.isEmpty() || infinity) {
                if (ammoStack.isEmpty()) {
                    ammoStack = new ItemStack(Items.ARROW);
                }
                float f = getArrowVelocity(i);

                if ((double) f >= 0.1D) {
                    boolean hasArrow = player.capabilities.isCreativeMode || (ammoStack.getItem() instanceof ItemArrow && ((ItemArrow) ammoStack.getItem()).isInfinite(ammoStack, stack, player));

                    if (!world.isRemote) {
                        EntityArrowExplosive explosiveArrow = new EntityArrowExplosive(world, player);
                        explosiveArrow.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, f * 3.0F, 1.0F);

                        if (f == 1.0F) {
                            explosiveArrow.setIsCritical(true);
                        }
                        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

                        if (j > 0) {
                            explosiveArrow.setDamage(explosiveArrow.getDamage() + (double) j * 0.5D + 0.5D);
                        }
                        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

                        if (k > 0) {
                            explosiveArrow.setKnockbackStrength(k);
                        }
                        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
                            explosiveArrow.setFire(100);
                        }
                        stack.damageItem(1, player);

                        if (hasArrow || player.capabilities.isCreativeMode && (ammoStack.getItem() == Items.SPECTRAL_ARROW || ammoStack.getItem() == Items.TIPPED_ARROW)) {
                            explosiveArrow.pickupStatus = EntityArrowExplosive.PickupStatus.CREATIVE_ONLY;
                        }
                        world.spawnEntity(explosiveArrow);
                    }
                    world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                    if (!hasArrow && !player.capabilities.isCreativeMode) {
                        ammoStack.shrink(1);

                        if (ammoStack.isEmpty()) {
                            player.inventory.deleteStack(ammoStack);
                        }
                    }
                    player.addStat(Objects.requireNonNull(StatList.getObjectUseStats(this)));
                }
            }
        }
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
    }
}
package com.teammetallurgy.atum.items.artifacts;

import com.teammetallurgy.atum.entity.arrow.EntityArrowDoubleShot;
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

public class ItemNeithsAudacity extends ItemBaseBow {

    public ItemNeithsAudacity() {
        super();
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

    @Override
    public void onPlayerStoppedUsing(@Nonnull ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            boolean flag = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack ammoStack = this.findAmmo(player);

            int i = this.getMaxItemUseDuration(stack) - timeLeft;
            i = ForgeEventFactory.onArrowLoose(stack, world, (EntityPlayer) entityLiving, i, !ammoStack.isEmpty() || flag);
            if (i < 0) return;

            if (!ammoStack.isEmpty() || flag) {
                if (ammoStack.isEmpty()) {
                    ammoStack = new ItemStack(Items.ARROW);
                }

                float f = getArrowVelocity(i);

                if ((double) f >= 0.1D) {
                    boolean flagAmmo = flag && ammoStack.getItem() instanceof ItemArrow;

                    if (!world.isRemote) {
                        EntityArrowDoubleShot entityarrow = new EntityArrowDoubleShot(world, player);
                        entityarrow.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, f * 2.0F, 1.0F);
                        EntityArrowDoubleShot entityarrow1 = new EntityArrowDoubleShot(world, player);
                        entityarrow1.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, f * 2.0F, 1.0F);
                        entityarrow.motionX += Math.random() * 0.4D - 0.2D;
                        entityarrow.motionY += Math.random() * 0.4D - 0.2D;
                        entityarrow.motionZ += Math.random() * 0.4D - 0.2D;
                        entityarrow1.motionX += Math.random() * 0.4D - 0.2D;
                        entityarrow1.motionY += Math.random() * 0.4D - 0.2D;
                        entityarrow1.motionZ += Math.random() * 0.4D - 0.2D;
                        entityarrow.setDamage(entityarrow.getDamage() + 0.5D);
                        entityarrow1.setDamage(entityarrow.getDamage() + 0.5D);

                        if (f == 1.0F) {
                            entityarrow.setIsCritical(true);
                            entityarrow1.setIsCritical(true);
                        }

                        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
                        if (j > 0) {
                            entityarrow.setDamage(entityarrow.getDamage() + (double) j * 0.5D + 0.5D);
                            entityarrow1.setDamage(entityarrow.getDamage() + (double) j * 0.5D + 0.5D);
                        }

                        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
                        if (k > 0) {
                            entityarrow.setKnockbackStrength(k);
                            entityarrow1.setKnockbackStrength(k);
                        }

                        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
                            entityarrow.setFire(100);
                            entityarrow1.setFire(100);
                        }

                        stack.damageItem(1, player);

                        if (flagAmmo) {
                            entityarrow.pickupStatus = EntityArrowDoubleShot.PickupStatus.CREATIVE_ONLY;
                        }

                        world.spawnEntity(entityarrow);
                        world.spawnEntity(entityarrow1);
                    }

                    world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                    if (!flagAmmo) {
                        ammoStack.shrink(1);

                        if (ammoStack.isEmpty()) {
                            player.inventory.deleteStack(ammoStack);
                            player.inventory.deleteStack(ammoStack);
                        }
                    }

                    player.addStat(StatList.getObjectUseStats(this));

                }
            }
        }
    }

    @Override
    public boolean getIsRepairable(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return repair.getItem() == Items.DIAMOND;
    }
}
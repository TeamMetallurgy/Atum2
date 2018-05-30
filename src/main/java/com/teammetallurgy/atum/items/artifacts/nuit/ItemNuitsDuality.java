package com.teammetallurgy.atum.items.artifacts.nuit;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.projectile.arrow.CustomArrow;
import com.teammetallurgy.atum.entity.projectile.arrow.EntityArrowDoubleShotBlack;
import com.teammetallurgy.atum.entity.projectile.arrow.EntityArrowDoubleShotWhite;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.tools.ItemBaseBow;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
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

public class ItemNuitsDuality extends ItemBaseBow {

    public ItemNuitsDuality() {
        super();
        this.setRepairItem(Items.DIAMOND);
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
    public void onPlayerStoppedUsing(@Nonnull ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            boolean infinity = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack ammoStack = this.findAmmo(player);
            int maxUses = this.getMaxItemUseDuration(stack) - timeLeft;
            maxUses = ForgeEventFactory.onArrowLoose(stack, world, player, maxUses, !ammoStack.isEmpty() || infinity);
            if (maxUses < 0) return;

            if (!ammoStack.isEmpty() || infinity) {
                if (ammoStack.isEmpty()) {
                    ammoStack = new ItemStack(Items.ARROW);
                }
                float velocity = getArrowVelocity(maxUses);

                this.onVelocity(world, player, velocity);

                if ((double) velocity >= 0.1D) {
                    boolean hasArrow = player.capabilities.isCreativeMode || (ammoStack.getItem() instanceof ItemArrow && ((ItemArrow) ammoStack.getItem()).isInfinite(ammoStack, stack, player));

                    if (!world.isRemote) {
                        EntityArrowDoubleShotBlack doubleShotLower = new EntityArrowDoubleShotBlack(world, player);
                        doubleShotLower.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, velocity * 2.0F, 1.0F);
                        EntityArrowDoubleShotWhite doubleShotHigher = new EntityArrowDoubleShotWhite(world, player);
                        doubleShotHigher.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, velocity * 2.0F, 1.0F);
                        doubleShotLower.motionX += MathHelper.floor(MathHelper.nextDouble(world.rand, Math.random(), 0.3D));
                        doubleShotLower.motionZ += MathHelper.floor(MathHelper.nextDouble(world.rand, Math.random(), 0.3D));
                        doubleShotHigher.motionX += MathHelper.floor(MathHelper.nextDouble(world.rand, Math.random(), 0.3D));
                        doubleShotHigher.motionY += 0.2D;
                        doubleShotHigher.motionZ += MathHelper.floor(MathHelper.nextDouble(world.rand, Math.random(), 0.3D));

                        if (velocity == 1.0F) {
                            doubleShotLower.setIsCritical(true);
                            doubleShotHigher.setIsCritical(true);
                        }
                        int powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

                        if (powerLevel > 0) {
                            doubleShotLower.setDamage(doubleShotLower.getDamage() + (double) powerLevel + 0.5D);
                            doubleShotHigher.setDamage(doubleShotHigher.getDamage() + (double) powerLevel + 0.5D);
                        }
                        int punchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

                        if (punchLevel > 0) {
                            doubleShotLower.setKnockbackStrength(punchLevel);
                            doubleShotHigher.setKnockbackStrength(punchLevel);
                        }
                        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
                            doubleShotLower.setFire(100);
                            doubleShotHigher.setFire(100);
                        }
                        stack.damageItem(1, player);

                        if (hasArrow || player.capabilities.isCreativeMode && (ammoStack.getItem() == Items.SPECTRAL_ARROW || ammoStack.getItem() == Items.TIPPED_ARROW)) {
                            doubleShotLower.pickupStatus = CustomArrow.PickupStatus.CREATIVE_ONLY;
                            doubleShotHigher.pickupStatus = CustomArrow.PickupStatus.CREATIVE_ONLY;
                        }
                        world.spawnEntity(doubleShotLower);
                        world.spawnEntity(doubleShotHigher);
                    }
                    world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);

                    if (!hasArrow && !player.capabilities.isCreativeMode) {
                        ammoStack.shrink(2);

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
    protected EntityArrow setArrow(ItemStack stack, World world, EntityPlayer player, float velocity) {
        return new EntityArrowDoubleShotWhite(world, player);
    }

    @Override
    protected void onVelocity(World world, EntityPlayer player, float velocity) {
        if (velocity == 1.0F) {
            for (int l = 0; l < 24; ++l) {
                Atum.proxy.spawnParticle(AtumParticles.Types.NUIT, player, player.posX + (world.rand.nextDouble() - 0.5D) * (double) player.width, player.posY + world.rand.nextDouble() * (double) player.height, player.posZ + (world.rand.nextDouble() - 0.5D) * (double) player.width, 0.0D, 0.0D, 0.0D);
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
package com.teammetallurgy.atum.items.artifacts.isis;

import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.entity.projectile.arrow.ArrowDoubleEntity;
import com.teammetallurgy.atum.entity.projectile.arrow.CustomArrow;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.tools.BaseBowItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.ForgeEventFactory;

import javax.annotation.Nonnull;

public class IsisDivisionItem extends BaseBowItem implements IArtifact {

    public IsisDivisionItem() {
        super(new Item.Properties().rarity(Rarity.RARE).durability(650), AtumItems.NEBU_INGOT);
    }

    @Override
    public God getGod() {
        return God.ISIS;
    }

    @Override
    public void releaseUsing(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
            boolean infinity = player.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
            ItemStack ammoStack = player.getProjectile(stack);
            int maxUses = this.getUseDuration(stack) - timeLeft;
            maxUses = ForgeEventFactory.onArrowLoose(stack, level, player, maxUses, !ammoStack.isEmpty() || infinity);
            if (maxUses < 0) return;

            if (!ammoStack.isEmpty() || infinity) {
                if (ammoStack.isEmpty()) {
                    ammoStack = new ItemStack(Items.ARROW);
                }
                float velocity = getPowerForTime(maxUses);

                this.onVelocity(level, player, velocity);

                if (!((double) velocity < 0.1D)) {
                    boolean hasArrow = player.getAbilities().instabuild || (ammoStack.getItem() instanceof ArrowItem && ((ArrowItem) ammoStack.getItem()).isInfinite(ammoStack, stack, player));

                    if (!level.isClientSide) {
                        ArrowDoubleEntity doubleShotLower = new ArrowDoubleEntity(level, player);
                        doubleShotLower.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, velocity * 2.0F, 1.0F);
                        ArrowDoubleEntity doubleShotHigher = new ArrowDoubleEntity(level, player);
                        doubleShotHigher.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, velocity * 2.0F, 1.0F);
                        doubleShotLower.getDeltaMovement().add(Mth.floor(Mth.nextDouble(level.random, Math.random(), 0.3D)), 0.0F, Mth.floor(Mth.nextDouble(level.random, Math.random(), 0.3D)));
                        doubleShotHigher.getDeltaMovement().add(Mth.floor(Mth.nextDouble(level.random, Math.random(), 0.3D)), 0.2D, Mth.floor(Mth.nextDouble(level.random, Math.random(), 0.3D)));

                        if (velocity == 1.0F) {
                            doubleShotLower.setCritArrow(true);
                            doubleShotHigher.setCritArrow(true);
                        }
                        int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);

                        if (powerLevel > 0) {
                            doubleShotLower.setBaseDamage(doubleShotLower.getBaseDamage() + (double) powerLevel + 0.5D);
                            doubleShotHigher.setBaseDamage(doubleShotHigher.getBaseDamage() + (double) powerLevel + 0.5D);
                        }
                        int punchLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);

                        if (punchLevel > 0) {
                            doubleShotLower.setKnockback(punchLevel);
                            doubleShotHigher.setKnockback(punchLevel);
                        }
                        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
                            doubleShotLower.setSecondsOnFire(100);
                            doubleShotHigher.setSecondsOnFire(100);
                        }
                        stack.hurtAndBreak(1, player, (e) -> {
                            e.broadcastBreakEvent(player.getUsedItemHand());
                        });

                        if (hasArrow || player.getAbilities().instabuild && (ammoStack.getItem() == Items.SPECTRAL_ARROW || ammoStack.getItem() == Items.TIPPED_ARROW)) {
                            doubleShotLower.pickup = CustomArrow.Pickup.CREATIVE_ONLY;
                            doubleShotHigher.pickup = CustomArrow.Pickup.CREATIVE_ONLY;
                        }
                        level.addFreshEntity(doubleShotLower);
                        level.addFreshEntity(doubleShotHigher);
                    }
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.random.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);

                    if (!hasArrow && !player.getAbilities().instabuild) {
                        ammoStack.shrink(2);

                        if (ammoStack.isEmpty()) {
                            player.getInventory().removeItem(ammoStack);
                        }
                    }
                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    @Override
    protected AbstractArrow setArrow(@Nonnull ItemStack stack, Level level, Player player, float velocity) {
        return new ArrowDoubleEntity(level, player);
    }
}
package com.teammetallurgy.atum.items.tools;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.projectile.arrow.CustomArrow;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class BaseBowItem extends BowItem {
    private final Supplier<Item> repairItem;

    public BaseBowItem(Item.Properties properties, Supplier<Item> repairItem) {
        super(properties.tab(Atum.GROUP));
        this.repairItem = repairItem;
    }

    @Override
    public void releaseUsing(@Nonnull ItemStack stack, @Nonnull Level world, @Nonnull LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
            boolean infinity = player.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
            ItemStack ammoStack = player.getProjectile(stack);

            int maxUses = this.getArrowLoose(stack, timeLeft);
            maxUses = ForgeEventFactory.onArrowLoose(stack, world, player, maxUses, !ammoStack.isEmpty() || infinity);
            if (maxUses < 0) return;

            if (!ammoStack.isEmpty() || infinity) {
                if (ammoStack.isEmpty()) {
                    ammoStack = new ItemStack(Items.ARROW);
                }
                float velocity = getPowerForTime(maxUses);

                this.onVelocity(world, player, velocity);

                if (!((double) velocity < 0.1D)) {
                    boolean hasArrow = player.getAbilities().instabuild || (ammoStack.getItem() instanceof ArrowItem && ((ArrowItem) ammoStack.getItem()).isInfinite(ammoStack, stack, player));

                    if (!world.isClientSide) {
                        AbstractArrow arrow = setArrow(ammoStack, world, player, velocity);
                        onShoot(arrow, player, velocity);

                        if (velocity == 1.0F) {
                            arrow.setCritArrow(true);
                        }
                        int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);

                        if (powerLevel > 0) {
                            arrow.setBaseDamage(arrow.getBaseDamage() + (double) powerLevel * 0.5D + 0.5D);
                        }
                        int punchLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);

                        if (punchLevel > 0) {
                            arrow.setKnockback(punchLevel);
                        }
                        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
                            arrow.setSecondsOnFire(100);
                        }
                        stack.hurtAndBreak(1, player, (e) -> {
                            e.broadcastBreakEvent(player.getUsedItemHand());
                        });

                        if (hasArrow || player.getAbilities().instabuild && (ammoStack.getItem() == Items.SPECTRAL_ARROW || ammoStack.getItem() == Items.TIPPED_ARROW)) {
                            arrow.pickup = CustomArrow.Pickup.CREATIVE_ONLY;
                        }
                        world.addFreshEntity(arrow);
                    }
                    world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (world.random.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);

                    if (!hasArrow && !player.getAbilities().instabuild) {
                        ammoStack.shrink(1);

                        if (ammoStack.isEmpty()) {
                            player.getInventory().removeItem(ammoStack);
                        }
                    }
                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    protected AbstractArrow setArrow(@Nonnull ItemStack stack, Level world, Player player, float velocity) {
        ArrowItem ArrowItem = (ArrowItem) (stack.getItem() instanceof ArrowItem ? stack.getItem() : Items.ARROW);
        return ArrowItem.createArrow(world, stack, player);
    }

    protected void onVelocity(Level world, Player player, float velocity) {
    }

    protected void onShoot(AbstractArrow arrow, Player player, float velocity) {
        arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, velocity * 3.0F, 1.0F);
    }

    public int getArrowLoose(@Nonnull ItemStack stack, int timeLeft) {
        return this.getUseDuration(stack) - timeLeft;
    }

    public float getDrawbackSpeed(@Nonnull ItemStack stack, LivingEntity entity) {
        return (float) (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
    }

    @Override
    public boolean isValidRepairItem(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return repair.getItem() == this.repairItem.get();
    }
}
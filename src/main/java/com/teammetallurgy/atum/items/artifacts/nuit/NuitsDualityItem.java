package com.teammetallurgy.atum.items.artifacts.nuit;

import com.teammetallurgy.atum.entity.projectile.arrow.CustomArrow;
import com.teammetallurgy.atum.entity.projectile.arrow.EntityArrowDoubleShotBlack;
import com.teammetallurgy.atum.entity.projectile.arrow.EntityArrowDoubleShotWhite;
import com.teammetallurgy.atum.items.tools.BaseBowItem;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.*;
import net.minecraft.stats.Stats;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nonnull;

public class NuitsDualityItem extends BaseBowItem {

    public NuitsDualityItem() {
        super(new Item.Properties().rarity(Rarity.RARE));
        this.setRepairItem(Items.DIAMOND);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public void onPlayerStoppedUsing(@Nonnull ItemStack stack, @Nonnull World world, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;
            boolean infinity = player.abilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack ammoStack = player.findAmmo(stack);
            int maxUses = this.getUseDuration(stack) - timeLeft;
            maxUses = ForgeEventFactory.onArrowLoose(stack, world, player, maxUses, !ammoStack.isEmpty() || infinity);
            if (maxUses < 0) return;

            if (!ammoStack.isEmpty() || infinity) {
                if (ammoStack.isEmpty()) {
                    ammoStack = new ItemStack(Items.ARROW);
                }
                float velocity = getArrowVelocity(maxUses);

                this.onVelocity(world, player, velocity);

                if (!((double) velocity < 0.1D)) {
                    boolean hasArrow = player.abilities.isCreativeMode || (ammoStack.getItem() instanceof ArrowItem && ((ArrowItem) ammoStack.getItem()).isInfinite(ammoStack, stack, player));

                    if (!world.isRemote) {
                        EntityArrowDoubleShotBlack doubleShotLower = new EntityArrowDoubleShotBlack(world, player);
                        doubleShotLower.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, velocity * 2.0F, 1.0F);
                        EntityArrowDoubleShotWhite doubleShotHigher = new EntityArrowDoubleShotWhite(world, player);
                        doubleShotHigher.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, velocity * 2.0F, 1.0F);
                        doubleShotLower.getMotion().add(MathHelper.floor(MathHelper.nextDouble(world.rand, Math.random(), 0.3D)), 0.0F, MathHelper.floor(MathHelper.nextDouble(world.rand, Math.random(), 0.3D)));
                        doubleShotHigher.getMotion().add(MathHelper.floor(MathHelper.nextDouble(world.rand, Math.random(), 0.3D)), 0.2D, MathHelper.floor(MathHelper.nextDouble(world.rand, Math.random(), 0.3D)));

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
                        stack.damageItem(1, player, (e) -> {
                            e.sendBreakAnimation(player.getActiveHand());
                        });

                        if (hasArrow || player.abilities.isCreativeMode && (ammoStack.getItem() == Items.SPECTRAL_ARROW || ammoStack.getItem() == Items.TIPPED_ARROW)) {
                            doubleShotLower.pickupStatus = CustomArrow.PickupStatus.CREATIVE_ONLY;
                            doubleShotHigher.pickupStatus = CustomArrow.PickupStatus.CREATIVE_ONLY;
                        }
                        world.addEntity(doubleShotLower);
                        world.addEntity(doubleShotHigher);
                    }
                    world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);

                    if (!hasArrow && !player.abilities.isCreativeMode) {
                        ammoStack.shrink(2);

                        if (ammoStack.isEmpty()) {
                            player.inventory.deleteStack(ammoStack);
                        }
                    }
                    player.addStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    @Override
    protected ArrowEntity setArrow(@Nonnull ItemStack stack, World world, PlayerEntity player, float velocity) {
        return new EntityArrowDoubleShotWhite(world, player);
    }
}
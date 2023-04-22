package com.teammetallurgy.atum.items.tools;

import com.teammetallurgy.atum.Atum;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class GreatswordItem extends SwordItem {
    private static final Object2FloatMap<Player> cooldown = new Object2FloatOpenHashMap<>();

    public GreatswordItem(Tier tier) {
        super(tier, 8, -3.2F, new Item.Properties());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttack(AttackEntityEvent event) {
        Player player = event.getEntity();
        if (player.level.isClientSide) return;
        if (event.getTarget() instanceof LivingEntity) {
            if (player.getMainHandItem().getItem() instanceof GreatswordItem) {
                cooldown.put(player, player.getAttackStrengthScale(0.5F));
            }
        }
    }

    @Override
    public boolean hurtEnemy(@Nonnull ItemStack stack, @Nonnull LivingEntity target, @Nonnull LivingEntity attacker) {
        if (attacker instanceof Player && cooldown.containsKey(attacker)) {
            if (cooldown.getFloat(attacker) == 1.0F) {
                Player player = (Player) attacker;
                Level level = player.level;
                float sweeping = 1.0F + EnchantmentHelper.getSweepingDamageRatio(player) * (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);

                for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, target.getBoundingBox().inflate(1.75D, 0.25D, 1.75D))) {
                    if (entity != player && entity != target && !player.isAlliedTo(entity) && player.distanceToSqr(entity) < 11.0D) {
                        entity.knockback(1.0F + EnchantmentHelper.getKnockbackBonus(player), Mth.sin(player.getYRot() * 0.017453292F), -Mth.cos(player.getYRot() * 0.017453292F));
                        entity.hurt(entity.damageSources().playerAttack(player), sweeping);
                        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, player.getSoundSource(), 1.0F, 1.0F);
                    }
                }
            }
            cooldown.removeFloat(attacker);
        }
        return super.hurtEnemy(stack, target, attacker);
    }
}
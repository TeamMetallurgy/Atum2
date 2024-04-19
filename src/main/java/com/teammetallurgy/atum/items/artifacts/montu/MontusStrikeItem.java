package com.teammetallurgy.atum.items.artifacts.montu;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.api.material.AtumMaterialTiers;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.tools.BattleAxeItem;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class MontusStrikeItem extends BattleAxeItem implements IArtifact {
    private static final Object2FloatMap<Player> COOLDOWN = new Object2FloatOpenHashMap<>();

    public MontusStrikeItem() {
        super(AtumMaterialTiers.NEBU, 5.1F, -2.6F, new Item.Properties().rarity(Rarity.RARE));
    }

    @Override
    public God getGod() {
        return God.MONTU;
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack stack) {
        return 7200;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttack(AttackEntityEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;
        if (event.getTarget() instanceof LivingEntity) {
            if (player.getMainHandItem().getItem() == AtumItems.MONTUS_STRIKE.get()) {
                COOLDOWN.put(player, player.getAttackStrengthScale(0.5F));
            }
        }
    }

    @Override
    public boolean hurtEnemy(@Nonnull ItemStack stack, @Nonnull LivingEntity target, @Nonnull LivingEntity attacker) {
        if (attacker instanceof Player && COOLDOWN.containsKey(attacker)) {
            if (COOLDOWN.getFloat(attacker) == 1.0F) {
                Player player = (Player) attacker;
                Level level = player.level();
                float damage = 1.0F + EnchantmentHelper.getSweepingDamageRatio(player) * (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);

                for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, target.getBoundingBox().inflate(2.0D, 0.25D, 2.0D))) {
                    if (entity != player && entity != target && !player.isAlliedTo(entity) && player.distanceToSqr(entity) < 12.0D) {
                        entity.knockback(1.0F + EnchantmentHelper.getKnockbackBonus(player), Mth.sin(player.getYRot() * 0.017453292F), -Mth.cos(player.getYRot() * 0.017453292F));
                        entity.hurt(entity.damageSources().playerAttack(player), damage);
                        if (entity.level() instanceof ServerLevel serverLevel) {
                            double d0 = -Mth.sin(player.getYRot() * 0.017453292F);
                            double d1 = Mth.cos(player.getYRot() * 0.017453292F);
                            serverLevel.sendParticles(AtumParticles.MONTU.get(), target.getX() + d0, target.getY() + 1.1D, target.getZ() + d1, 20, 0.0D, 0.0D, 0.0D, 0.0D);
                            serverLevel.sendParticles(AtumParticles.MONTU.get(), entity.getX() + d0, entity.getY() + 1.1D, entity.getZ() + d1, 20, 0.0D, 0.0D, 0.0D, 0.0D);
                        }
                        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, player.getSoundSource(), 1.0F, 1.0F);
                    }
                }
            }
            COOLDOWN.removeFloat(attacker);
        }
        return super.hurtEnemy(stack, target, attacker);
    }
}
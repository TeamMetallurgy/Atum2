package com.teammetallurgy.atum.items.artifacts.montu;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.tools.BattleAxeItem;
import com.teammetallurgy.atum.utils.Constants;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class MontusStrikeItem extends BattleAxeItem {
    private static final Object2FloatMap<PlayerEntity> cooldown = new Object2FloatOpenHashMap<>();

    @Override
    public int getMaxItemUseDuration(@Nonnull ItemStack stack) {
        return 7200;
    }


    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttack(AttackEntityEvent event) {
        PlayerEntity player = event.getEntityPlayer();
        if (player.world.isRemote) return;
        if (event.getTarget() instanceof LivingEntity) {
            if (player.getHeldItemMainhand().getItem() == AtumItems.MONTUS_STRIKE) {
                cooldown.put(player, player.getCooledAttackStrength(0.5F));
            }
        }
    }

    @Override
    public boolean hitEntity(@Nonnull ItemStack stack, LivingEntity target, @Nullable LivingEntity attacker) {
        if (attacker instanceof PlayerEntity && cooldown.containsKey(attacker)) {
            if (cooldown.get(attacker) == 1.0F) {
                PlayerEntity player = (PlayerEntity) attacker;
                World world = player.world;
                float damage = 1.0F + EnchantmentHelper.getSweepingDamageRatio(player) * (float) player.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();

                for (LivingEntity entity : world.getEntitiesWithinAABB(LivingEntity.class, target.getEntityBoundingBox().grow(2.0D, 0.25D, 2.0D))) {
                    if (entity != player && entity != target && !player.isOnSameTeam(entity) && player.getDistanceSq(entity) < 12.0D) {
                        entity.knockBack(player, 1.0F + EnchantmentHelper.getKnockbackModifier(player), (double) MathHelper.sin(player.rotationYaw * 0.017453292F), (double) (-MathHelper.cos(player.rotationYaw * 0.017453292F)));
                        entity.attackEntityFrom(DamageSource.causePlayerDamage(player), damage);
                        for (int amount = 0; amount < 20; amount++) {
                            double d0 = (double) (-MathHelper.sin(player.rotationYaw * 0.017453292F));
                            double d1 = (double) MathHelper.cos(player.rotationYaw * 0.017453292F);
                            Atum.proxy.spawnParticle(AtumParticles.Types.MONTU, target, target.posX + d0, target.posY + 1.1D, target.posZ + d1, 0.0D, 0.0D, 0.0D);
                            Atum.proxy.spawnParticle(AtumParticles.Types.MONTU, entity, entity.posX + d0, entity.posY + 1.1D, entity.posZ + d1, 0.0D, 0.0D, 0.0D);
                        }
                        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0F, 1.0F);
                    }
                }
            }
            cooldown.remove(attacker);
        }
        return super.hitEntity(stack, target, attacker);
    }
}
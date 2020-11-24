package com.teammetallurgy.atum.items.artifacts.montu;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.AtumMats;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.tools.BattleAxeItem;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class MontusStrikeItem extends BattleAxeItem {
    private static final Object2FloatMap<PlayerEntity> cooldown = new Object2FloatOpenHashMap<>();

    public MontusStrikeItem() {
        super(AtumMats.NEBU, 5.1F, -2.6F, new Item.Properties().rarity(Rarity.RARE));
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack stack) {
        return 7200;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttack(AttackEntityEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player.world.isRemote) return;
        if (event.getTarget() instanceof LivingEntity) {
            if (player.getHeldItemMainhand().getItem() == AtumItems.MONTUS_STRIKE) {
                cooldown.put(player, player.getCooledAttackStrength(0.5F));
            }
        }
    }

    @Override
    public boolean hitEntity(@Nonnull ItemStack stack, @Nonnull LivingEntity target, @Nonnull LivingEntity attacker) {
        if (attacker instanceof PlayerEntity && cooldown.containsKey(attacker)) {
            if (cooldown.getFloat(attacker) == 1.0F) {
                PlayerEntity player = (PlayerEntity) attacker;
                World world = player.world;
                float damage = 1.0F + EnchantmentHelper.getSweepingDamageRatio(player) * (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);

                for (LivingEntity entity : world.getEntitiesWithinAABB(LivingEntity.class, target.getBoundingBox().grow(2.0D, 0.25D, 2.0D))) {
                    if (entity != player && entity != target && !player.isOnSameTeam(entity) && player.getDistanceSq(entity) < 12.0D) {
                        entity.applyKnockback(1.0F + EnchantmentHelper.getKnockbackModifier(player), MathHelper.sin(player.rotationYaw * 0.017453292F), -MathHelper.cos(player.rotationYaw * 0.017453292F));
                        entity.attackEntityFrom(DamageSource.causePlayerDamage(player), damage);
                        if (entity.world instanceof ServerWorld) {
                            ServerWorld serverWorld = (ServerWorld) entity.world;
                            double d0 = -MathHelper.sin(player.rotationYaw * 0.017453292F);
                            double d1 = MathHelper.cos(player.rotationYaw * 0.017453292F);
                            serverWorld.spawnParticle(AtumParticles.MONTU, target.getPosX() + d0, target.getPosY() + 1.1D, target.getPosZ() + d1, 20, 0.0D, 0.0D, 0.0D, 0.0D);
                            serverWorld.spawnParticle(AtumParticles.MONTU, entity.getPosX() + d0, entity.getPosY() + 1.1D, entity.getPosZ() + d1, 20, 0.0D, 0.0D, 0.0D, 0.0D);
                        }
                        world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0F, 1.0F);
                    }
                }
            }
            cooldown.removeFloat(attacker);
        }
        return super.hitEntity(stack, target, attacker);
    }
}
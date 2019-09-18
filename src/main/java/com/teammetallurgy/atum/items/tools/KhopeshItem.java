package com.teammetallurgy.atum.items.tools;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.utils.Constants;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class KhopeshItem extends SwordItem {
    private static final Object2FloatMap<PlayerEntity> cooldown = new Object2FloatOpenHashMap<>();

    public KhopeshItem(IItemTier itemTier) {
        super(itemTier, 3, -2.6F, new Item.Properties().group(Atum.GROUP));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttack(AttackEntityEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player.world.isRemote) return;
        if (event.getTarget() instanceof LivingEntity) {
            if (player.getHeldItemMainhand().getItem() instanceof KhopeshItem) {
                cooldown.put(player, player.getCooledAttackStrength(0.5F));
            }
        }
    }

    @Override
    public boolean hitEntity(@Nonnull ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity && cooldown.containsKey(attacker)) {
            if (cooldown.getFloat(attacker) == 1.0F) {
                PlayerEntity player = (PlayerEntity) attacker;
                World world = player.world;
                float sweeping = 1.0F + EnchantmentHelper.getSweepingDamageRatio(player) * (float) player.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue();

                for (LivingEntity entity : world.getEntitiesWithinAABB(LivingEntity.class, target.getBoundingBox().grow(1.25D, 0.25D, 1.25D))) {
                    if (entity != player && entity != target && !player.isOnSameTeam(entity) && player.getDistanceSq(entity) < 10.0D) {
                        entity.knockBack(player, 1.0F + EnchantmentHelper.getKnockbackModifier(player), MathHelper.sin(player.rotationYaw * 0.017453292F), (-MathHelper.cos(player.rotationYaw * 0.017453292F)));
                        entity.attackEntityFrom(DamageSource.causePlayerDamage(player), sweeping);
                        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0F, 1.0F);
                    }
                }
            }
            cooldown.removeFloat(attacker);
        }
        return super.hitEntity(stack, target, attacker);
    }
}
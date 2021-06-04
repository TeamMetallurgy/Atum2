package com.teammetallurgy.atum.items.tools;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.misc.StackHelper;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
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

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class KhopeshItem extends SwordItem {
    private static final Object2FloatMap<PlayerEntity> COOLDOWN = new Object2FloatOpenHashMap<>();

    public KhopeshItem(IItemTier itemTier) {
        this(itemTier, new Item.Properties());
    }

    public KhopeshItem(IItemTier itemTier, Item.Properties properties) {
        super(itemTier, 3, -2.6F, properties.group(Atum.GROUP));
    }

    public boolean getIsOffHand(@Nonnull ItemStack stack) {
        return StackHelper.hasKey(stack, "is_offhand") && stack.getTag() != null && stack.getTag().getBoolean("is_offhand");
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttack(AttackEntityEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player.world.isRemote) return;
        if (event.getTarget() instanceof LivingEntity) {
            if (player.getHeldItemMainhand().getItem() instanceof KhopeshItem) {
                COOLDOWN.put(player, player.getCooledAttackStrength(0.5F));
            }
        }
    }

    @Override
    public boolean hitEntity(@Nonnull ItemStack stack, @Nonnull LivingEntity target, @Nonnull LivingEntity attacker) {
        if (attacker instanceof PlayerEntity && COOLDOWN.containsKey(attacker)) {
            if (COOLDOWN.getFloat(attacker) == 1.0F) {
                PlayerEntity player = (PlayerEntity) attacker;
                World world = player.world;
                float sweeping = 1.0F + EnchantmentHelper.getSweepingDamageRatio(player) * (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);

                for (LivingEntity entity : world.getEntitiesWithinAABB(LivingEntity.class, target.getBoundingBox().grow(1.25D, 0.25D, 1.25D))) {
                    if (entity != player && entity != target && !player.isOnSameTeam(entity) && player.getDistanceSq(entity) < 10.0D) {
                        entity.applyKnockback(1.0F + EnchantmentHelper.getKnockbackModifier(player), MathHelper.sin(player.rotationYaw * 0.017453292F), (-MathHelper.cos(player.rotationYaw * 0.017453292F)));
                        entity.attackEntityFrom(DamageSource.causePlayerDamage(player), sweeping);
                        world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0F, 1.0F);
                    }
                }
            }
            COOLDOWN.removeFloat(attacker);
        }
        return super.hitEntity(stack, target, attacker);
    }
}
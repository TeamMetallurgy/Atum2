package com.teammetallurgy.atum.items.tools;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.stone.StoneBaseEntity;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class GauntletItem extends SwordItem {
    protected static final Object2FloatMap<LivingEntity> cooldown = new Object2FloatOpenHashMap<>();

    protected GauntletItem(IItemTier tier, Item.Properties properties) {
        super(tier, 2, -2.2F, properties.group(Atum.GROUP));
    }

    @Override
    public boolean canApplyAtEnchantingTable(@Nonnull ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment) && enchantment != Enchantments.SWEEPING;
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        LivingEntity target = event.getEntityLiving();
        Entity source = event.getSource().getTrueSource();
        if (!(target instanceof StoneBaseEntity) && source instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity) source;
            if (attacker.getHeldItemMainhand().getItem() instanceof GauntletItem) {
                float knockback = 0.0F;
                if (cooldown.getFloat(attacker) == 1.0F) {
                    knockback = 1.0F;
                }
                target.addVelocity((-MathHelper.sin(attacker.rotationYaw * 3.1415927F / 180.0F) * knockback * 0.5F), 0.1D, (MathHelper.cos(attacker.rotationYaw * 3.1415927F / 180.0F) * knockback * 0.5F));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttack(AttackEntityEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player.world.isRemote) return;
        if (event.getTarget() instanceof LivingEntity && !(event.getTarget() instanceof StoneBaseEntity)) {
            if (player.getHeldItemMainhand().getItem() instanceof GauntletItem) {
                cooldown.put(player, player.getCooledAttackStrength(0.5F));
            }
        }
    }
}
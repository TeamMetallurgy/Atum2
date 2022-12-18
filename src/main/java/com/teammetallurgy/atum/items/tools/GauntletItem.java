package com.teammetallurgy.atum.items.tools;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.stone.StoneBaseEntity;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class GauntletItem extends SwordItem {
    protected static final Object2FloatMap<LivingEntity> COOLDOWN = new Object2FloatOpenHashMap<>();

    protected GauntletItem(Tier tier, Item.Properties properties) {
        super(tier, 2, -2.2F, properties.tab(Atum.GROUP));
    }

    @Override
    public boolean canApplyAtEnchantingTable(@Nonnull ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment) && enchantment != Enchantments.SWEEPING_EDGE;
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        Entity source = event.getSource().getEntity();
        if (!(target instanceof StoneBaseEntity) && source instanceof LivingEntity attacker) {
            if (attacker.getMainHandItem().getItem() instanceof GauntletItem) {
                float knockback = 0.0F;
                if (COOLDOWN.getFloat(attacker) == 1.0F) {
                    knockback = 1.0F;
                }
                target.push((-Mth.sin(attacker.getYRot() * 3.1415927F / 180.0F) * knockback * 0.5F), 0.1D, (Mth.cos(attacker.getYRot() * 3.1415927F / 180.0F) * knockback * 0.5F));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttack(AttackEntityEvent event) {
        Player player = event.getEntity();
        if (player.level.isClientSide) return;
        if (event.getTarget() instanceof LivingEntity && !(event.getTarget() instanceof StoneBaseEntity)) {
            if (player.getMainHandItem().getItem() instanceof GauntletItem) {
                COOLDOWN.put(player, player.getAttackStrengthScale(0.5F));
            }
        }
    }
}
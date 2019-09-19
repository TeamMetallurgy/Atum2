package com.teammetallurgy.atum.items.artifacts.shu;

import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.tools.BattleAxeItem;
import com.teammetallurgy.atum.utils.Constants;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Rarity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class ShusExileItem extends BattleAxeItem {
    private static final Object2FloatMap<PlayerEntity> cooldown = new Object2FloatOpenHashMap<>();

    public ShusExileItem() {
        super(ItemTier.DIAMOND, 4.5F, -2.9F, new Item.Properties().rarity(Rarity.RARE));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttack(AttackEntityEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player.world.isRemote) return;
        if (event.getTarget() instanceof LivingEntity && player.getHeldItemMainhand().getItem() == AtumItems.SHUS_EXILE) {
            cooldown.put(player, player.getCooledAttackStrength(0.5F));
        }
    }

    @SubscribeEvent
    public static void onKnockback(LivingKnockBackEvent event) {
        Entity attacker = event.getAttacker();
        if (attacker instanceof PlayerEntity && cooldown.containsKey(attacker)) {
            PlayerEntity player = (PlayerEntity) attacker;
            if (player.getHeldItemMainhand().getItem() == AtumItems.SHUS_EXILE && cooldown.getFloat(attacker) == 1.0F) {
                LivingEntity target = event.getEntityLiving();
                event.setStrength(event.getStrength() * 3F);
                double x = MathHelper.nextDouble(random, 0.0001D, 0.02D);
                double z = MathHelper.nextDouble(random, 0.0001D, 0.02D);
                for (int l = 0; l < 12; ++l) {
                    target.world.addParticle(AtumParticles.SHU, target.posX + (random.nextDouble() - 0.5D) * (double) target.getWidth(), target.posY + target.getEyeHeight(), target.posZ + (random.nextDouble() - 0.5D) * (double) target.getWidth(), x, 0.04D, -z);
                }
            }
            cooldown.removeFloat(attacker);
        }
    }
}
package com.teammetallurgy.atum.items.tools;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.stone.EntityStoneBase;
import com.teammetallurgy.atum.utils.Constants;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class ClubItem extends SwordItem {
    private static final Object2FloatMap<LivingEntity> cooldown = new Object2FloatOpenHashMap<>();

    public ClubItem(IItemTier itemTier) {
        super(itemTier, 13, -3.4F, new Item.Properties().group(Atum.GROUP));
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        LivingEntity target = event.getEntityLiving();
        Entity source = event.getSource().getTrueSource();
        if (!(target instanceof EntityStoneBase) && source instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity) source;
            if (attacker.getHeldItemMainhand().getItem() instanceof ClubItem) {
                float knockback = 0.0F;
                if (cooldown.getFloat(attacker) == 1.0F) {
                    knockback = 1.8F;
                }
                target.addVelocity(-MathHelper.sin(attacker.rotationYaw * 3.1415927F / 180.0F) * knockback * 0.5F, 0.1D, MathHelper.cos(attacker.rotationYaw * 3.1415927F / 180.0F) * knockback * 0.5F);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttack(AttackEntityEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player.world.isRemote) return;
        if (event.getTarget() instanceof LivingEntity && !(event.getTarget() instanceof EntityStoneBase)) {
            if (player.getHeldItemMainhand().getItem() instanceof ClubItem) {
                cooldown.put(player, player.getCooledAttackStrength(0.5F));
            }
        }
    }
}
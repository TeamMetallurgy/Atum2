package com.teammetallurgy.atum.items.artifacts.seth;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.tools.DaggerItem;
import com.teammetallurgy.atum.utils.Constants;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class SethsStingItem extends DaggerItem {
    private static final Object2FloatMap<PlayerEntity> cooldown = new Object2FloatOpenHashMap<>();

    public SethsStingItem() {
        super(ToolMaterial.DIAMOND);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    @Nonnull
    public EnumRarity getRarity(@Nonnull ItemStack stack) {
        return EnumRarity.RARE;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttack(AttackEntityEvent event) {
        PlayerEntity player = event.getEntityPlayer();
        if (player.world.isRemote) return;
        if (event.getTarget() instanceof LivingEntity) {
            if (player.getHeldItemMainhand().getItem() == AtumItems.SETHS_STING) {
                cooldown.put(player, player.getCooledAttackStrength(0.5F));
            }
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        Entity trueSource = event.getSource().getTrueSource();
        if (trueSource instanceof PlayerEntity && cooldown.containsKey(trueSource)) {
            if (cooldown.get(trueSource) == 1.0F) {
                LivingEntity target = event.getEntityLiving();
                target.addPotionEffect(new EffectInstance(Effects.POISON, 80, 2));
                for (int l = 0; l < 14; ++l) {
                    Atum.proxy.spawnParticle(AtumParticles.Types.SETH, trueSource, target.posX + (random.nextDouble() - 0.5D) * (double) target.width, target.posY + random.nextDouble() * (double) target.height, target.posZ + (random.nextDouble() - 0.5D) * (double) target.width, 0.0D, 10.0D, 0.0D);
                }
            }
            cooldown.remove(trueSource);
        }
    }
}
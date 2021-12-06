package com.teammetallurgy.atum.items.artifacts.nuit;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.AtumMats;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.tools.KhopeshItem;
import com.teammetallurgy.atum.misc.StackHelper;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class NuitsQuarterItem extends KhopeshItem implements IArtifact {
    private static final Object2BooleanMap<LivingEntity> IS_BLOCKING = new Object2BooleanOpenHashMap<>();

    public NuitsQuarterItem() {
        super(AtumMats.NEBU, new Item.Properties().rarity(Rarity.RARE));
    }

    @Override
    public God getGod() {
        return God.NUIT;
    }

    @Override
    @Nonnull
    public UseAnim getUseAnimation(@Nonnull ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack stack) {
        return this.getIsOffHand(stack) ? 72000 : 0;
    }

    @Override
    public boolean isShield(@Nonnull ItemStack stack, @Nullable LivingEntity entity) {
        return this.getIsOffHand(stack);
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, @Nonnull Player player, @Nonnull InteractionHand hand) {
        CompoundTag tag = StackHelper.getTag(player.getItemInHand(hand));
        if (hand == InteractionHand.OFF_HAND) {
            player.startUsingItem(InteractionHand.OFF_HAND);
            tag.putBoolean("is_offhand", true);
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(InteractionHand.OFF_HAND));
        }
        tag.putBoolean("is_offhand", false);
        return super.use(world, player, hand);
    }

    @Override
    public boolean hurtEnemy(@Nonnull ItemStack stack, @Nonnull LivingEntity target, @Nonnull LivingEntity attacker) {
        if (random.nextFloat() <= 0.25F) {
            applyWeakness(target, attacker, attacker.getOffhandItem().getItem() == AtumItems.NUITS_IRE);
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void onUseTick(@Nonnull Level world, @Nonnull LivingEntity livingEntity, @Nonnull ItemStack stack, int count) {
        super.onUseTick(world, livingEntity, stack, count);
        IS_BLOCKING.putIfAbsent(livingEntity, true);
    }

    @Override
    public void releaseUsing(@Nonnull ItemStack stack, @Nonnull Level world, @Nonnull LivingEntity livingEntity, int timeLeft) {
        super.releaseUsing(stack, world, livingEntity, timeLeft);
        IS_BLOCKING.removeBoolean(livingEntity);
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        Entity trueSource = event.getSource().getDirectEntity();
        LivingEntity livingEntity = event.getEntityLiving();
        if (trueSource instanceof LivingEntity && livingEntity instanceof Player && IS_BLOCKING.getBoolean(livingEntity) && random.nextFloat() <= 0.25F) {
            applyWeakness((LivingEntity) trueSource, event.getEntityLiving(), event.getEntityLiving().getMainHandItem().getItem() == AtumItems.NUITS_IRE);
            IS_BLOCKING.removeBoolean(livingEntity);
        }
    }

    private static void applyWeakness(LivingEntity target, LivingEntity attacker, boolean isNuitsIreHeld) {
        if (attacker != target) {
            if (target.level instanceof ServerLevel) {
                ServerLevel serverWorld = (ServerLevel) target.level;
                serverWorld.sendParticles(AtumParticles.NUIT_BLACK, target.getX() + (random.nextDouble() - 0.5D) * (double) target.getBbWidth(), target.getY() + (target.getBbHeight() / 1.5D), target.getZ() + (random.nextDouble() - 0.5D) * (double) target.getBbWidth(), 8, 0.01D, 0.0D, 0.01D, 0.02D);
            }
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, isNuitsIreHeld ? 2 : 1));
        }
    }
}
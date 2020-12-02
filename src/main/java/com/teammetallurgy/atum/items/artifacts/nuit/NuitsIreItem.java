package com.teammetallurgy.atum.items.artifacts.nuit;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.AtumMats;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.tools.KhopeshItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class NuitsIreItem extends KhopeshItem implements IArtifact {
    private boolean isOffhand = false;
    private static boolean isBlocking = false;

    public NuitsIreItem() {
        super(AtumMats.NEBU, new Item.Properties().rarity(Rarity.RARE));
    }

    @Override
    public God getGod() {
        return God.NUIT;
    }

    @Override
    @Nonnull
    public UseAction getUseAction(@Nonnull ItemStack stack) {
        return UseAction.BLOCK;
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack stack) {
        return isOffhand ? 72000 : 0;
    }

    @Override
    public boolean isShield(@Nonnull ItemStack stack, @Nullable LivingEntity entity) {
        return isOffhand;
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull PlayerEntity player, @Nonnull Hand hand) {
        if (hand == Hand.OFF_HAND) {
            player.setActiveHand(Hand.OFF_HAND);
            this.isOffhand = true;
            return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(Hand.OFF_HAND));
        }
        this.isOffhand = false;
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public boolean hitEntity(@Nonnull ItemStack stack, @Nonnull LivingEntity target, @Nonnull LivingEntity attacker) {
        if (random.nextFloat() <= 0.25F) {
            applyWither(target, attacker, attacker.getHeldItemOffhand().getItem() == AtumItems.NUITS_QUARTER);
        }
        return super.hitEntity(stack, target, attacker);
    }

    @SubscribeEvent
    public static void onUse(LivingEntityUseItemEvent.Tick event) {
        LivingEntity entity = event.getEntityLiving();
        if (entity instanceof PlayerEntity && entity.getHeldItem(Hand.OFF_HAND).getItem() == AtumItems.NUITS_IRE) {
            isBlocking = true;
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        Entity trueSource = event.getSource().getImmediateSource();
        if (trueSource instanceof LivingEntity && event.getEntityLiving() instanceof PlayerEntity && isBlocking && random.nextFloat() <= 0.25F) {
            applyWither((LivingEntity) trueSource, event.getEntityLiving(), event.getEntityLiving().getHeldItemMainhand().getItem() == AtumItems.NUITS_QUARTER);
            isBlocking = false;
        }
    }

    private static void applyWither(LivingEntity target, LivingEntity attacker, boolean isNuitsQuarterHeld) {
        if (attacker != target) {
            if (target.world instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) target.world;
                serverWorld.spawnParticle(AtumParticles.NUIT_WHITE, target.getPosX() + (random.nextDouble() - 0.5D) * (double) target.getWidth(), target.getPosY() + (target.getHeight() / 1.5D), target.getPosZ() + (random.nextDouble() - 0.5D) * (double) target.getWidth(), 8, 0.01D, 0.0D, 0.01D, 0.02D);
            }
            target.addPotionEffect(new EffectInstance(Effects.WITHER, 60, isNuitsQuarterHeld ? 2 : 1));
        }
    }
}
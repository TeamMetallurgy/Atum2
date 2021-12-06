package com.teammetallurgy.atum.items.artifacts.anubis;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.platform.InputConstants;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.AtumMats;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.entity.stone.StoneBaseEntity;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.misc.StackHelper;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class AnubisWrathItem extends SwordItem implements IArtifact {
    private static final Object2FloatMap<Player> COOLDOWN = new Object2FloatOpenHashMap<>();
    private float attackDamage = 5.0F;

    public AnubisWrathItem() {
        super(AtumMats.NEBU, 0, 0.0F, new Item.Properties().rarity(Rarity.RARE).tab(Atum.GROUP));
    }

    @Override
    public God getGod() {
        return God.ANUBIS;
    }

    @Override
    public float getDamage() {
        return this.attackDamage;
    }

    @Override
    public boolean showDurabilityBar(@Nonnull ItemStack stack) {
        return !InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT) ? super.showDurabilityBar(stack) : getSouls(stack) > 0;
    }

    @Override
    public double getDurabilityForDisplay(@Nonnull ItemStack stack) {
        return !InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT) ? super.getDurabilityForDisplay(stack) : (double) (getSoulUpgradeTier(getTier(stack)) - Math.min(getSouls(stack), 500)) / (double) getSoulUpgradeTier(getTier(stack));
    }

    @Override
    public int getRGBDurabilityForDisplay(@Nonnull ItemStack stack) {
        return !InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT) ? super.getRGBDurabilityForDisplay(stack) : 12452784;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttack(AttackEntityEvent event) {
        Player player = event.getPlayer();
        if (player.level.isClientSide) return;
        if (player.getMainHandItem().getItem() == AtumItems.ANUBIS_WRATH && getTier(player.getMainHandItem()) == 3) {
            if (event.getTarget() instanceof LivingEntity && ((LivingEntity) event.getTarget()).getMobType() != MobType.UNDEAD && !(event.getTarget() instanceof StoneBaseEntity)) {
                COOLDOWN.put(player, player.getAttackStrengthScale(0.5F));
            }
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        Entity trueSource = event.getSource().getEntity();
        if (trueSource instanceof Player && COOLDOWN.containsKey(trueSource)) {
            if (COOLDOWN.getFloat(trueSource) == 1.0F) {
                event.setAmount(event.getAmount() * 2);
                LivingEntity entity = event.getEntityLiving();
                double y = Mth.nextDouble(random, 0.02D, 0.1D);
                if (entity.level instanceof ServerLevel) {
                    ServerLevel serverWorld = (ServerLevel) entity.level;
                    serverWorld.sendParticles(AtumParticles.ANUBIS, entity.getX() + (random.nextDouble() - 0.5D) * (double) entity.getBbWidth(), entity.getY() + entity.getEyeHeight(), entity.getZ() + (random.nextDouble() - 0.5D) * (double) entity.getBbWidth(), 5, 0.0D, y, 0.0D, 0.04D);
                }
            }
            COOLDOWN.removeFloat(trueSource);
        }
    }

    @SubscribeEvent
    public static void onKill(LivingDeathEvent event) {
        Entity source = event.getSource().getEntity();
        if (source instanceof LivingEntity && ((LivingEntity) source).getMainHandItem().getItem() == AtumItems.ANUBIS_WRATH) {
            ItemStack heldStack = ((LivingEntity) source).getMainHandItem();
            CompoundTag tag = StackHelper.getTag(heldStack);
            tag.putInt("souls", getSouls(heldStack) + 1);
            if (getSouls(heldStack) == 50 || getSouls(heldStack) == 150 || getSouls(heldStack) == 500) {
                source.level.playSound(null, source.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, 0.0F);
                if (source instanceof Player) {
                    ((Player) source).displayClientMessage(new TranslatableComponent(heldStack.getDescriptionId().replace("item.", "") + ".levelup").withStyle(ChatFormatting.DARK_PURPLE), true);
                }
            }
        }
        if (event.getEntityLiving() instanceof Player) {
            Player player = (Player) event.getEntityLiving();
            Inventory inv = player.getInventory();
            if (inv.contains(findAnubisWrath(player))) {
                CompoundTag tag = StackHelper.getTag(findAnubisWrath(player));
                tag.putInt("souls", getSouls(findAnubisWrath(player)) / 2);
                player.level.playSound(null, player.blockPosition(), SoundEvents.GHAST_DEATH, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
    }

    @Nonnull
    private static ItemStack findAnubisWrath(Player player) {
        if (player.getItemInHand(InteractionHand.OFF_HAND).getItem() == AtumItems.ANUBIS_WRATH) {
            return player.getItemInHand(InteractionHand.OFF_HAND);
        } else if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == AtumItems.ANUBIS_WRATH) {
            return player.getItemInHand(InteractionHand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
                ItemStack stack = player.getInventory().getItem(i);
                if (stack.getItem() == AtumItems.ANUBIS_WRATH) {
                    return stack;
                }
            }
            return ItemStack.EMPTY;
        }
    }

    @Override
    @Nonnull
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(@Nonnull EquipmentSlot slot, @Nonnull ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        if (slot == EquipmentSlot.MAINHAND) {
            int tier = getTier(stack);
            this.attackDamage = tier == 3 ? 9.0F : tier + 5.0F;
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.getDamage(), AttributeModifier.Operation.ADDITION));
            double speed = tier == 0 ? 0.6D : tier == 1 ? 0.7D : tier == 2 ? 0.8D : tier == 3 ? 1.0D : 0;
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", speed - 3.0D, AttributeModifier.Operation.ADDITION));
        }
        return builder.build();
    }

    public static int getSouls(@Nonnull ItemStack stack) {
        return StackHelper.hasKey(stack, "souls") && stack.getTag() != null ? stack.getTag().getInt("souls") : 0;
    }

    public static int getTier(@Nonnull ItemStack stack) {
        int souls = getSouls(stack);
        return souls < 50 ? 0 : souls < 150 ? 1 : souls < 500 ? 2 : 3;
    }

    private static int getSoulUpgradeTier(int tier) {
        return tier == 0 ? 50 : tier == 1 ? 150 : tier == 2 ? 500 : 500;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@Nonnull ItemStack stack, Level world, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag tooltipType) {
        String itemIdentifier = "atum." + Objects.requireNonNull(stack.getItem().getRegistryName()).getPath() + ".tooltip";
        if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
            tooltip.add(new TranslatableComponent(itemIdentifier + ".line1" + (getTier(stack) == 3 ? ".soul_unraveler" : ".soul_drinker")).withStyle(ChatFormatting.DARK_PURPLE));
            tooltip.add(new TranslatableComponent(itemIdentifier + ".line2" + (getTier(stack) == 3 ? ".soul_unraveler" : ".soul_drinker")).withStyle(ChatFormatting.DARK_PURPLE));
        } else {
            tooltip.add(new TranslatableComponent(itemIdentifier + (getTier(stack) == 3 ? ".soul_unraveler" : ".soul_drinker"))
                    .append(" ").append(new TranslatableComponent(Atum.MOD_ID + ".tooltip.shift").withStyle(ChatFormatting.DARK_GRAY)));
        }
        if (tooltipType.isAdvanced()) {
            tooltip.add(new TranslatableComponent(itemIdentifier + ".kills", getSouls(stack)).withStyle(ChatFormatting.DARK_RED));
        }
    }
}
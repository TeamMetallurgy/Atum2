package com.teammetallurgy.atum.items.artifacts.anubis;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.platform.InputConstants;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.api.material.AtumMaterialTiers;
import com.teammetallurgy.atum.entity.stone.StoneBaseEntity;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.misc.StackHelper;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
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
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class AnubisWrathItem extends SwordItem implements IArtifact {
    private static final Object2FloatMap<Player> COOLDOWN = new Object2FloatOpenHashMap<>();
    private float attackDamage = 5.0F;

    public AnubisWrathItem() {
        super(AtumMaterialTiers.NEBU, 0, 0.0F, new Item.Properties().rarity(Rarity.RARE));
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
    public boolean isBarVisible(@Nonnull ItemStack stack) {
        return !InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT) ? super.isBarVisible(stack) : getSouls(stack) > 0;
    }

    @Override
    public int getBarWidth(@Nonnull ItemStack stack) {
        return !InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT) ? super.getBarWidth(stack) : (getSoulUpgradeTier(getTier(stack)) - Math.min(getSouls(stack), 500)) / getSoulUpgradeTier(getTier(stack));
    }

    @Override
    public int getBarColor(@Nonnull ItemStack stack) {
        return !InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT) ? super.getBarColor(stack) : 12452784;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttack(AttackEntityEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;
        if (player.getMainHandItem().getItem() == AtumItems.ANUBIS_WRATH.get() && getTier(player.getMainHandItem()) == 3) {
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
                LivingEntity entity = event.getEntity();
                RandomSource random = entity.level().random;
                double y = Mth.nextDouble(random, 0.02D, 0.1D);
                if (entity.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(AtumParticles.ANUBIS.get(), entity.getX() + (random.nextDouble() - 0.5D) * (double) entity.getBbWidth(), entity.getY() + entity.getEyeHeight(), entity.getZ() + (random.nextDouble() - 0.5D) * (double) entity.getBbWidth(), 5, 0.0D, y, 0.0D, 0.04D);
                }
            }
            COOLDOWN.removeFloat(trueSource);
        }
    }

    @SubscribeEvent
    public static void onKill(LivingDeathEvent event) {
        Entity source = event.getSource().getEntity();
        if (source instanceof LivingEntity && ((LivingEntity) source).getMainHandItem().getItem() == AtumItems.ANUBIS_WRATH.get()) {
            ItemStack heldStack = ((LivingEntity) source).getMainHandItem();
            CompoundTag tag = StackHelper.getTag(heldStack);
            tag.putInt("souls", getSouls(heldStack) + 1);
            if (getSouls(heldStack) == 50 || getSouls(heldStack) == 150 || getSouls(heldStack) == 500) {
                source.level().playSound(null, source.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, 0.0F);
                if (source instanceof Player) {
                    ((Player) source).displayClientMessage(Component.translatable(heldStack.getDescriptionId().replace("item.", "") + ".levelup").withStyle(ChatFormatting.DARK_PURPLE), true);
                }
            }
        }
        if (event.getEntity() instanceof Player player) {
            Inventory inv = player.getInventory();
            if (inv.contains(findAnubisWrath(player))) {
                CompoundTag tag = StackHelper.getTag(findAnubisWrath(player));
                tag.putInt("souls", getSouls(findAnubisWrath(player)) / 2);
                player.level().playSound(null, player.blockPosition(), SoundEvents.GHAST_DEATH, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
    }

    @Nonnull
    private static ItemStack findAnubisWrath(Player player) {
        if (player.getItemInHand(InteractionHand.OFF_HAND).getItem() == AtumItems.ANUBIS_WRATH.get()) {
            return player.getItemInHand(InteractionHand.OFF_HAND);
        } else if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == AtumItems.ANUBIS_WRATH.get()) {
            return player.getItemInHand(InteractionHand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
                ItemStack stack = player.getInventory().getItem(i);
                if (stack.getItem() == AtumItems.ANUBIS_WRATH.get()) {
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
    public void appendHoverText(@Nonnull ItemStack stack, Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag tooltipType) {
        String itemIdentifier = "atum." + Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(stack.getItem())).getPath() + ".tooltip";
        MutableComponent title = Component.translatable(itemIdentifier + (getTier(stack) == 3 ? ".soul_unraveler" : ".soul_drinker")).withStyle(ChatFormatting.GRAY);
        if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
            tooltip.add(title.append(": ").append(Component.translatable(itemIdentifier + ".line1" + (getTier(stack) == 3 ? ".soul_unraveler" : ".soul_drinker")).withStyle(ChatFormatting.DARK_GRAY)));
            tooltip.add(Component.translatable(itemIdentifier + ".line2" + (getTier(stack) == 3 ? ".soul_unraveler" : ".soul_drinker")).withStyle(ChatFormatting.DARK_GRAY));
        } else {
            tooltip.add(title.append(" ").append(Component.translatable(Atum.MOD_ID + ".tooltip.shift").withStyle(ChatFormatting.DARK_GRAY)));
        }
        if (tooltipType.isAdvanced()) {
            tooltip.add(Component.translatable(itemIdentifier + ".kills", getSouls(stack)).withStyle(ChatFormatting.DARK_RED));
        }
    }
}
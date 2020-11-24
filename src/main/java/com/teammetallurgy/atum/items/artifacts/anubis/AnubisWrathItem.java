package com.teammetallurgy.atum.items.artifacts.anubis;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.AtumMats;
import com.teammetallurgy.atum.entity.stone.StoneBaseEntity;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.misc.StackHelper;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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
public class AnubisWrathItem extends SwordItem {
    private static final Object2FloatMap<PlayerEntity> COOLDOWN = new Object2FloatOpenHashMap<>();
    private float attackDamage = 5.0F;

    public AnubisWrathItem() {
        super(AtumMats.NEBU, 0, 0.0F, new Item.Properties().rarity(Rarity.RARE).group(Atum.GROUP));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    public boolean showDurabilityBar(@Nonnull ItemStack stack) {
        return !InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT) ? super.showDurabilityBar(stack) : getSouls(stack) > 0;
    }

    @Override
    public double getDurabilityForDisplay(@Nonnull ItemStack stack) {
        return !InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT) ? super.getDurabilityForDisplay(stack) : (double) (getSoulUpgradeTier(getTier(stack)) - Math.min(getSouls(stack), 500)) / (double) getSoulUpgradeTier(getTier(stack));
    }

    @Override
    public int getRGBDurabilityForDisplay(@Nonnull ItemStack stack) {
        return !InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT) ? super.getRGBDurabilityForDisplay(stack) : 12452784;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttack(AttackEntityEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player.world.isRemote) return;
        if (player.getHeldItemMainhand().getItem() == AtumItems.ANUBIS_WRATH && getTier(player.getHeldItemMainhand()) == 3) {
            if (event.getTarget() instanceof LivingEntity && ((LivingEntity) event.getTarget()).getCreatureAttribute() != CreatureAttribute.UNDEAD && !(event.getTarget() instanceof StoneBaseEntity)) {
                COOLDOWN.put(player, player.getCooledAttackStrength(0.5F));
            }
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        Entity trueSource = event.getSource().getTrueSource();
        if (trueSource instanceof PlayerEntity && COOLDOWN.containsKey(trueSource)) {
            if (COOLDOWN.getFloat(trueSource) == 1.0F) {
                event.setAmount(event.getAmount() * 2);
                LivingEntity entity = event.getEntityLiving();
                double y = MathHelper.nextDouble(random, 0.02D, 0.1D);
                if (entity.world instanceof ServerWorld) {
                    ServerWorld serverWorld = (ServerWorld) entity.world;
                    serverWorld.spawnParticle(AtumParticles.ANUBIS, entity.getPosX() + (random.nextDouble() - 0.5D) * (double) entity.getWidth(), entity.getPosY() + entity.getEyeHeight(), entity.getPosZ() + (random.nextDouble() - 0.5D) * (double) entity.getWidth(), 5, 0.0D, y, 0.0D, 0.04D);
                }
            }
            COOLDOWN.removeFloat(trueSource);
        }
    }

    @SubscribeEvent
    public static void onKill(LivingDeathEvent event) {
        Entity source = event.getSource().getTrueSource();
        if (source instanceof LivingEntity && ((LivingEntity) source).getHeldItemMainhand().getItem() == AtumItems.ANUBIS_WRATH) {
            ItemStack heldStack = ((LivingEntity) source).getHeldItemMainhand();
            CompoundNBT tag = StackHelper.getTag(heldStack);
            tag.putInt("souls", getSouls(heldStack) + 1);
            if (getSouls(heldStack) == 50 || getSouls(heldStack) == 150 || getSouls(heldStack) == 500) {
                source.world.playSound(null, source.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0F, 0.0F);
                if (source instanceof PlayerEntity) {
                    ((PlayerEntity) source).sendStatusMessage(new TranslationTextComponent(heldStack.getTranslationKey() + ".levelup").deepCopy().mergeStyle(TextFormatting.DARK_PURPLE), true);
                }
            }
        }
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            PlayerInventory inv = player.inventory;
            if (inv.hasItemStack(findAnubisWrath(player))) {
                CompoundNBT tag = StackHelper.getTag(findAnubisWrath(player));
                tag.putInt("souls", getSouls(findAnubisWrath(player)) / 2);
                player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_GHAST_DEATH, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
        }
    }

    @Nonnull
    private static ItemStack findAnubisWrath(PlayerEntity player) {
        if (player.getHeldItem(Hand.OFF_HAND).getItem() == AtumItems.ANUBIS_WRATH) {
            return player.getHeldItem(Hand.OFF_HAND);
        } else if (player.getHeldItem(Hand.MAIN_HAND).getItem() == AtumItems.ANUBIS_WRATH) {
            return player.getHeldItem(Hand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if (stack.getItem() == AtumItems.ANUBIS_WRATH) {
                    return stack;
                }
            }
            return ItemStack.EMPTY;
        }
    }

    @Override
    @Nonnull
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(@Nonnull EquipmentSlotType slot, @Nonnull ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        if (slot == EquipmentSlotType.MAINHAND) {
            int tier = getTier(stack);
            this.attackDamage = tier == 3 ? 9.0F : tier + 5.0F;
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.getAttackDamage(), AttributeModifier.Operation.ADDITION));
            double speed = tier == 0 ? 0.6D : tier == 1 ? 0.7D : tier == 2 ? 0.8D : tier == 3 ? 1.0D : 0;
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", speed - 3.0D, AttributeModifier.Operation.ADDITION));
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
    public void addInformation(@Nonnull ItemStack stack, World world, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag tooltipType) {
        String itemIdentifier = "atum." + Objects.requireNonNull(stack.getItem().getRegistryName()).getPath() + ".tooltip";
        if (InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
            tooltip.add(new TranslationTextComponent(itemIdentifier + ".line1" + (getTier(stack) == 3 ? ".soul_unraveler" : ".soul_drinker")).mergeStyle(TextFormatting.DARK_PURPLE));
            tooltip.add(new TranslationTextComponent(itemIdentifier + ".line2" + (getTier(stack) == 3 ? ".soul_unraveler" : ".soul_drinker")).mergeStyle(TextFormatting.DARK_PURPLE));
        } else {
            tooltip.add(new TranslationTextComponent(itemIdentifier + (getTier(stack) == 3 ? ".soul_unraveler" : ".soul_drinker"))
                    .appendString(" ").append(new TranslationTextComponent(Atum.MOD_ID + ".tooltip.shift").mergeStyle(TextFormatting.DARK_GRAY)));
        }
        if (tooltipType.isAdvanced()) {
            tooltip.add(new TranslationTextComponent(itemIdentifier + ".kills", getSouls(stack)).mergeStyle(TextFormatting.DARK_RED));
        }
    }
}
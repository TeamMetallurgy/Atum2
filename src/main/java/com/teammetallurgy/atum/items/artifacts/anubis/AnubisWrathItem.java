package com.teammetallurgy.atum.items.artifacts.anubis;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.stone.StoneBaseEntity;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.utils.Constants;
import com.teammetallurgy.atum.utils.StackHelper;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
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
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class AnubisWrathItem extends SwordItem {
    private static final Object2FloatMap<PlayerEntity> cooldown = new Object2FloatOpenHashMap<>();
    private final float attackDamage;

    public AnubisWrathItem() {
        super(ItemTier.DIAMOND, 0, 0.0F, new Item.Properties().rarity(Rarity.RARE).group(Atum.GROUP));
        this.addPropertyOverride(new ResourceLocation("tier"), new IItemPropertyGetter() {
            @OnlyIn(Dist.CLIENT)
            public float call(@Nonnull ItemStack stack, @Nullable World world, @Nullable LivingEntity entity) {
                return (float) getTier(stack);
            }
        });
        int tier = getTier(new ItemStack(this)); //TODO test
        this.attackDamage = tier == 3 ? 9.0F : tier + 5.0F;
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
        return !InputMappings.isKeyDown(Minecraft.getInstance().mainWindow.getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT) ? super.showDurabilityBar(stack) : getSouls(stack) > 0;
    }

    @Override
    public double getDurabilityForDisplay(@Nonnull ItemStack stack) {
        return !InputMappings.isKeyDown(Minecraft.getInstance().mainWindow.getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT) ? super.getDurabilityForDisplay(stack) : (double) (getSoulUpgradeTier(getTier(stack)) - Math.min(getSouls(stack), 500)) / (double) getSoulUpgradeTier(getTier(stack));
    }

    @Override
    public int getRGBDurabilityForDisplay(@Nonnull ItemStack stack) {
        return !InputMappings.isKeyDown(Minecraft.getInstance().mainWindow.getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT) ? super.getRGBDurabilityForDisplay(stack) : 12452784;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttack(AttackEntityEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player.world.isRemote) return;
        if (player.getHeldItemMainhand().getItem() == AtumItems.ANUBIS_WRATH && getTier(player.getHeldItemMainhand()) == 3) {
            if (event.getTarget() instanceof LivingEntity && ((LivingEntity) event.getTarget()).getCreatureAttribute() != CreatureAttribute.UNDEAD && !(event.getTarget() instanceof StoneBaseEntity)) {
                cooldown.put(player, player.getCooledAttackStrength(0.5F));
            }
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        Entity trueSource = event.getSource().getTrueSource();
        if (trueSource instanceof PlayerEntity && cooldown.containsKey(trueSource)) {
            if (cooldown.getFloat(trueSource) == 1.0F) {
                event.setAmount(event.getAmount() * 2);
                LivingEntity entity = event.getEntityLiving();
                double y = MathHelper.nextDouble(random, 0.02D, 0.13D);
                for (int l = 0; l < 5; ++l) {
                    entity.world.addParticle(AtumParticles.ANUBIS, entity.posX + (random.nextDouble() - 0.5D) * (double) entity.getWidth(), entity.posY + entity.getEyeHeight(), entity.posZ + (random.nextDouble() - 0.5D) * (double) entity.getWidth(), 0.0D, y, 0.0D);
                }
            }
            cooldown.removeFloat(trueSource);
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
                    ((PlayerEntity) source).sendStatusMessage(new TranslationTextComponent(heldStack.getTranslationKey() + ".levelup").setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)), true);
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
    public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EquipmentSlotType slot, @Nonnull ItemStack stack) {
        Multimap<String, AttributeModifier> map = HashMultimap.create();
        if (slot == EquipmentSlotType.MAINHAND) {
            int tier = getTier(stack);
            map.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
            double speed = tier == 0 ? 0.6D : tier == 1 ? 0.7D : tier == 2 ? 0.8D : tier == 3 ? 1.0D : 0;
            map.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", speed - 3.0D, AttributeModifier.Operation.ADDITION));
        }
        return map;
    }

    private static int getSouls(@Nonnull ItemStack stack) {
        return StackHelper.hasKey(stack, "souls") && stack.getTag() != null ? stack.getTag().getInt("souls") : 0;
    }

    private static int getTier(@Nonnull ItemStack stack) {
        int souls = getSouls(stack);
        return souls < 50 ? 0 : souls < 150 ? 1 : souls < 500 ? 2 : 3;
    }

    private static int getSoulUpgradeTier(int tier) {
        return tier == 0 ? 50 : tier == 1 ? 150 : tier == 2 ? 500 : 500;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag tooltipType) {
        String itemIdentifier = Objects.requireNonNull(stack.getItem().getRegistryName()).getPath() + ".tooltip";
        if (InputMappings.isKeyDown(Minecraft.getInstance().mainWindow.getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
            tooltip.add(new TranslationTextComponent(itemIdentifier + ".line1" + (getTier(stack) == 3 ? ".soulUnraveler" : ".soulDrinker")).applyTextStyle(TextFormatting.DARK_PURPLE));
            tooltip.add(new TranslationTextComponent(itemIdentifier + ".line2" + (getTier(stack) == 3 ? ".soulUnraveler" : ".soulDrinker")).applyTextStyle(TextFormatting.DARK_PURPLE));
        } else {
            tooltip.add(new TranslationTextComponent(itemIdentifier + (getTier(stack) == 3 ? ".soulUnraveler" : ".soulDrinker"))
                    .appendText(" ").appendSibling(new TranslationTextComponent(Constants.MOD_ID + ".tooltip.shift").applyTextStyle(TextFormatting.DARK_GRAY)));
        }
        if (tooltipType.isAdvanced()) {
            tooltip.add(new TranslationTextComponent(itemIdentifier + ".kills", getSouls(stack)).applyTextStyle(TextFormatting.DARK_RED));
        }
    }
}
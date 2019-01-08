package com.teammetallurgy.atum.items.artifacts.anubis;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.utils.StackHelper;
import gnu.trove.map.TObjectFloatMap;
import gnu.trove.map.hash.TObjectFloatHashMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber
public class ItemAnubisWrath extends ItemSword {
    private static final TObjectFloatMap<EntityPlayer> cooldown = new TObjectFloatHashMap<>();

    public ItemAnubisWrath() {
        super(ToolMaterial.DIAMOND);
        this.addPropertyOverride(new ResourceLocation("tier"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(@Nonnull ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
                return (float) getTier(stack);
            }
        });
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    @Nonnull
    public EnumRarity getRarity(@Nonnull ItemStack stack) {
        return EnumRarity.RARE;
    }

    @Override
    public boolean showDurabilityBar(@Nonnull ItemStack stack) {
        return getSouls(stack) > 0;
    }

    @Override
    public double getDurabilityForDisplay(@Nonnull ItemStack stack) {
        return (double) (getSoulUpgradeTier(getTier(stack)) - getSouls(stack)) / (double) getSoulUpgradeTier(getTier(stack));
    }

    @Override
    public int getRGBDurabilityForDisplay(@Nonnull ItemStack stack) {
        return 12452784;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttack(AttackEntityEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        if (player.world.isRemote) return;
        if (player.getHeldItemMainhand().getItem() == AtumItems.ANUBIS_WRATH && getTier(player.getHeldItemMainhand()) == 3) {
            if (event.getTarget() instanceof EntityLivingBase && ((EntityLivingBase) event.getTarget()).getCreatureAttribute() != EnumCreatureAttribute.UNDEAD) {
                cooldown.put(player, player.getCooledAttackStrength(0.5F));
            }
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        Entity trueSource = event.getSource().getTrueSource();
        if (trueSource instanceof EntityPlayer && cooldown.get(trueSource) == 1.0F) {
            event.setAmount(event.getAmount() * 2);
            cooldown.remove(trueSource);
            EntityLivingBase entity = event.getEntityLiving();
            double y = MathHelper.nextDouble(itemRand, 0.02D, 0.13D);
            for (int l = 0; l < 5; ++l) {
                Atum.proxy.spawnParticle(AtumParticles.Types.ANUBIS, entity, entity.posX + (itemRand.nextDouble() - 0.5D) * (double) entity.width, entity.posY + entity.getEyeHeight(), entity.posZ + (itemRand.nextDouble() - 0.5D) * (double) entity.width, 0.0D, y, 0.0D);
            }
        }
    }

    @SubscribeEvent
    public static void onKill(LivingDeathEvent event) {
        Entity source = event.getSource().getTrueSource();
        if (source instanceof EntityLivingBase && ((EntityLivingBase) source).getHeldItemMainhand().getItem() == AtumItems.ANUBIS_WRATH) {
            ItemStack heldStack = ((EntityLivingBase) source).getHeldItemMainhand();
            NBTTagCompound tag = StackHelper.getTag(heldStack);
            tag.setInteger("souls", getSouls(heldStack) + 1);
            if (getSouls(heldStack) == 50 || getSouls(heldStack) == 150 || getSouls(heldStack) == 500) {
                source.world.playSound(null, source.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0F, 0.0F);
                if (source instanceof EntityPlayer) {
                    ((EntityPlayer) source).sendStatusMessage(new TextComponentTranslation(heldStack.getTranslationKey() + ".levelup").setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)), true);
                }
            }
        }
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            InventoryPlayer inv = player.inventory;
            if (inv.hasItemStack(findAnubisWrath(player))) {
                NBTTagCompound tag = StackHelper.getTag(findAnubisWrath(player));
                tag.setInteger("souls", getSouls(findAnubisWrath(player)) / 2);
                player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_GHAST_DEATH, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
        }
    }

    @Nonnull
    private static ItemStack findAnubisWrath(EntityPlayer player) {
        if (player.getHeldItem(EnumHand.OFF_HAND).getItem() == AtumItems.ANUBIS_WRATH) {
            return player.getHeldItem(EnumHand.OFF_HAND);
        } else if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() == AtumItems.ANUBIS_WRATH) {
            return player.getHeldItem(EnumHand.MAIN_HAND);
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
    public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EntityEquipmentSlot slot, @Nonnull ItemStack stack) {
        Multimap<String, AttributeModifier> map = HashMultimap.create();
        if (slot == EntityEquipmentSlot.MAINHAND) {
            int tier = getTier(stack);
            map.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", tier == 3 ? 9.0D : (double) tier + 5.0D, 0));
            double speed = tier == 0 ? 0.6D : tier == 1 ? 0.7D : tier == 2 ? 0.8D : tier == 3 ? 1.0D : 0;
            map.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", speed - 3.0D, 0));
        }
        return map;
    }

    private static int getSouls(@Nonnull ItemStack stack) {
        return StackHelper.hasKey(stack, "souls") ? Objects.requireNonNull(stack.getTagCompound()).getInteger("souls") : 0;
    }

    private static int getTier(@Nonnull ItemStack stack) {
        int souls = getSouls(stack);
        return souls < 50 ? 0 : souls < 150 ? 1 : souls < 500 ? 2 : 3;
    }

    private static int getSoulUpgradeTier(int tier) {
        return tier == 0 ? 50 : tier == 1 ? 150 : tier == 2 ? 500 : 500;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag tooltipType) {
        if (Keyboard.isKeyDown(42)) {
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getTranslationKey() + ".line1" + (getTier(stack) == 3 ? ".soulUnraveler" : ".soulDrinker")));
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getTranslationKey() + ".line2" + (getTier(stack) == 3 ? ".soulUnraveler" : ".soulDrinker")));
        } else {
            tooltip.add(I18n.format(this.getTranslationKey() + (getTier(stack) == 3 ? ".soulUnraveler" : ".soulDrinker")) + " " + TextFormatting.DARK_GRAY + "[SHIFT]");
        }
        if (tooltipType.isAdvanced()) {
            tooltip.add(TextFormatting.DARK_RED + I18n.format(this.getTranslationKey() + ".kills", getSouls(stack)));
        }
    }
}
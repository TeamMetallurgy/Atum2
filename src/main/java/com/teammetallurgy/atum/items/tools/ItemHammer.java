package com.teammetallurgy.atum.items.tools;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import gnu.trove.map.TObjectFloatMap;
import gnu.trove.map.hash.TObjectFloatHashMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.UUID;

public class ItemHammer extends ItemSword {
    private static final AttributeModifier STUN = new AttributeModifier(UUID.fromString("b4ebf092-fe62-4250-b945-7dc45b2f1036"), "Hammer stun", -1000.0D, 0);
    private static final TObjectFloatMap<EntityPlayer> cooldown = new TObjectFloatHashMap<>();
    protected static int stunTimer = 0;
    private final float damage;

    public ItemHammer(ToolMaterial material) {
        super(material);
        this.damage = material.getAttackDamage() + 17.0F;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onHurt(LivingHurtEvent event) {
        Entity trueSource = event.getSource().getTrueSource();
        if (trueSource instanceof EntityPlayer && cooldown.get(trueSource) == 1.0F) {
            EntityLivingBase target = event.getEntityLiving();
            ModifiableAttributeInstance attribute = (ModifiableAttributeInstance) target.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
            if (!attribute.hasModifier(STUN)) {
                attribute.applyModifier(STUN);
                this.onStun(target);
            }
            cooldown.remove(trueSource);
        }
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    protected void onStun(EntityLivingBase target) {
        stunTimer = 400;
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (stunTimer > 1) {
            stunTimer--;
        }
        ModifiableAttributeInstance attribute = (ModifiableAttributeInstance) event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        if (stunTimer == 1 && attribute.hasModifier(STUN)) {
            attribute.removeModifier(STUN);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onAttack(AttackEntityEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        if (player.world.isRemote) return;
        if (event.getTarget() instanceof EntityLivingBase && player.getHeldItemMainhand().getItem() instanceof ItemHammer) {
            cooldown.put(player, player.getCooledAttackStrength(0.5F));
        }
    }

    @Override
    @Nonnull
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
        Multimap<String, AttributeModifier> map = HashMultimap.create();
        if (slot == EntityEquipmentSlot.MAINHAND) {
            map.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.damage, 0));
            map.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -3.55D, 0));
        }
        return map;
    }
}
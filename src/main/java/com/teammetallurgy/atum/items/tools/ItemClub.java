package com.teammetallurgy.atum.items.tools;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemSword;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.UUID;

@Mod.EventBusSubscriber
public class ItemClub extends ItemSword {
    private static final AttributeModifier STUN = new AttributeModifier(UUID.fromString("b4ebf092-fe62-4250-b945-7dc45b2f1036"), "Club stun", -1000.0D, 0);
    private static int stunTimer = 0;
    private final float damage;

    public ItemClub(ToolMaterial material) {
        super(material);
        this.setCreativeTab(null);
        this.damage = material.getAttackDamage() + 13.0F;
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        Entity trueSource = event.getSource().getTrueSource();
        if (trueSource instanceof EntityLivingBase && ((EntityLivingBase) trueSource).getHeldItemMainhand().getItem() instanceof ItemClub) {
            EntityLivingBase entity = event.getEntityLiving();
            ModifiableAttributeInstance attribute = (ModifiableAttributeInstance) entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
            if (!attribute.hasModifier(STUN)) {
                stunTimer = 1;
            }
        }
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (stunTimer > 1) {
            stunTimer--;
        }
        ModifiableAttributeInstance attribute = (ModifiableAttributeInstance) event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        if (stunTimer == 1 && attribute.hasModifier(STUN)) {
            attribute.removeModifier(STUN);
        }
    }

    @Override
    @Nonnull
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
        Multimap<String, AttributeModifier> map = HashMultimap.create();
        if (slot == EntityEquipmentSlot.MAINHAND) {
            map.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.damage, 0));
            map.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -3.4D, 0));
        }
        return map;
    }
}
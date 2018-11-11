package com.teammetallurgy.atum.items.tools;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemSword;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber
public class ItemDagger extends ItemSword {
    private final float damage;

    public ItemDagger(ToolMaterial material) {
        super(material);
        this.setCreativeTab(null);
        this.damage = material.getAttackDamage() + 2.0F;
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        EntityLivingBase target = event.getEntityLiving();
        Entity source = event.getSource().getTrueSource();
        if (source instanceof EntityLivingBase && ((EntityLivingBase) source).getHeldItemMainhand().getItem() instanceof ItemDagger) {
            System.out.println("asdasd");
            target.setVelocity(0, 0, 0);
        }
    }

    @Override
    @Nonnull
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
        Multimap<String, AttributeModifier> map = HashMultimap.create();
        if (slot == EntityEquipmentSlot.MAINHAND) {
            map.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.damage, 0));
            map.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.0D, 0));
        }
        return map;
    }
}
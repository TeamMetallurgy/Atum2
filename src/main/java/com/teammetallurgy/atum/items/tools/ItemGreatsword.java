package com.teammetallurgy.atum.items.tools;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.teammetallurgy.atum.entity.bandit.EntityBarbarian;
import com.teammetallurgy.atum.entity.stone.EntityStoneBase;
import gnu.trove.map.TObjectFloatMap;
import gnu.trove.map.hash.TObjectFloatHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemSword;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber
public class ItemGreatsword extends ItemSword {
    private static final TObjectFloatMap<EntityLivingBase> cooldown = new TObjectFloatHashMap<>();
    private final float damage;

    public ItemGreatsword(ToolMaterial material) {
        super(material);
        this.setCreativeTab(null);
        this.damage = material.getAttackDamage() + 8.0F;
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        EntityLivingBase target = event.getEntityLiving();
        Entity source = event.getSource().getTrueSource();
        if (!(target instanceof EntityStoneBase) && source instanceof EntityLivingBase) {
            EntityLivingBase attacker = (EntityLivingBase) source;
            if (attacker.getHeldItemMainhand().getItem() instanceof ItemGreatsword) {
                float knockback = 1.2F;
                if (target.getName().equalsIgnoreCase("revanace")) {
                    knockback = 50.0F;
                } else if (attacker instanceof EntityBarbarian) {
                    knockback = 2.0F;
                } else if (cooldown.get(attacker) == 1.0F) {
                    knockback = 3.0F;
                }
                target.addVelocity((double) (-MathHelper.sin(attacker.rotationYaw * 3.1415927F / 180.0F) * knockback * 0.5F), 0.1D, (double) (MathHelper.cos(attacker.rotationYaw * 3.1415927F / 180.0F) * knockback * 0.5F));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttack(AttackEntityEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        if (player.world.isRemote) return;
        if (event.getTarget() instanceof EntityLivingBase && !(event.getTarget() instanceof EntityStoneBase)) {
            if (player.getHeldItemMainhand().getItem() instanceof ItemGreatsword) {
                cooldown.put(player, player.getCooledAttackStrength(0.5F));
            }
        }
    }

    @Override
    @Nonnull
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
        Multimap<String, AttributeModifier> map = HashMultimap.create();
        if (slot == EntityEquipmentSlot.MAINHAND) {
            map.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.damage, 0));
            map.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -3.2D, 0));
        }
        return map;
    }
}
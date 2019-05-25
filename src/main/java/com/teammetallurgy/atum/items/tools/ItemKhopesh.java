package com.teammetallurgy.atum.items.tools;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.teammetallurgy.atum.utils.Constants;
import gnu.trove.map.TObjectFloatMap;
import gnu.trove.map.hash.TObjectFloatHashMap;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class ItemKhopesh extends ItemSword {
    private static final TObjectFloatMap<EntityPlayer> cooldown = new TObjectFloatHashMap<>();
    private final float damage;

    public ItemKhopesh(ToolMaterial material) {
        super(material);
        this.damage = material.getAttackDamage() + 3.0F;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttack(AttackEntityEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        if (player.world.isRemote) return;
        if (event.getTarget() instanceof EntityLivingBase) {
            if (player.getHeldItemMainhand().getItem() instanceof ItemKhopesh) {
                cooldown.put(player, player.getCooledAttackStrength(0.5F));
            }
        }
    }

    @Override
    public boolean hitEntity(@Nonnull ItemStack stack, EntityLivingBase target, @Nullable EntityLivingBase attacker) {
        if (attacker instanceof EntityPlayer && cooldown.containsKey(attacker)) {
            if (cooldown.get(attacker) == 1.0F) {
                EntityPlayer player = (EntityPlayer) attacker;
                World world = player.world;
                float sweeping = 1.0F + EnchantmentHelper.getSweepingDamageRatio(player) * (float) player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();

                for (EntityLivingBase entity : world.getEntitiesWithinAABB(EntityLivingBase.class, target.getEntityBoundingBox().grow(1.25D, 0.25D, 1.25D))) {
                    if (entity != player && entity != target && !player.isOnSameTeam(entity) && player.getDistanceSq(entity) < 10.0D) {
                        entity.knockBack(player, 1.0F + EnchantmentHelper.getKnockbackModifier(player), (double) MathHelper.sin(player.rotationYaw * 0.017453292F), (double) (-MathHelper.cos(player.rotationYaw * 0.017453292F)));
                        entity.attackEntityFrom(DamageSource.causePlayerDamage(player), sweeping);
                        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0F, 1.0F);
                    }
                }
            }
            cooldown.remove(attacker);
        }
        return super.hitEntity(stack, target, attacker);
    }

    @Override
    @Nonnull
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
        Multimap<String, AttributeModifier> map = HashMultimap.create();
        if (slot == EntityEquipmentSlot.MAINHAND) {
            map.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.damage, 0));
            map.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.6D, 0));
        }
        return map;
    }
}
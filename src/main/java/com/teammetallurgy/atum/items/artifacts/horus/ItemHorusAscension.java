package com.teammetallurgy.atum.items.artifacts.horus;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.stone.EntityStoneBase;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import gnu.trove.map.TObjectFloatMap;
import gnu.trove.map.hash.TObjectFloatHashMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
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
import java.util.Random;

@Mod.EventBusSubscriber
public class ItemHorusAscension extends ItemSword {
    private static final TObjectFloatMap<EntityPlayer> cooldown = new TObjectFloatHashMap<>();

    public ItemHorusAscension() {
        super(ToolMaterial.DIAMOND);
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

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttack(AttackEntityEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        if (player.world.isRemote) return;
        if (event.getTarget() instanceof EntityLivingBase) {
            if (player.getHeldItemMainhand().getItem() == AtumItems.HORUSS_ASCENSION) {
                cooldown.put(player, player.getCooledAttackStrength(0.5F));
            }
        }
    }

    @Override
    public boolean hitEntity(@Nonnull ItemStack stack, EntityLivingBase target, @Nullable EntityLivingBase attacker) {
        if (cooldown.get(attacker) == 1.0F ) {
            knockUp(target, attacker, itemRand);
        }
        return super.hitEntity(stack, target, attacker);
    }

    public static void knockUp(EntityLivingBase target, EntityLivingBase attacker, Random random) {
        if (attacker != null && !(target instanceof EntityStoneBase)) {
            if (!attacker.world.isRemote) {
                double dx = target.posX - attacker.posX;
                double dz = target.posZ - attacker.posZ;
                double magnitude = Math.sqrt(dx * dx + dz * dz);
                dx /= magnitude;
                dz /= magnitude;
                target.isAirBorne = true;
                target.addVelocity(dx / 2.0D, 1.5D, dz / 2.0D);
                if (target.motionY > 0.9D) {
                    target.motionY = 0.9D;
                }
            }
            for (int amount = 0; amount < 50; ++amount) {
                Atum.proxy.spawnParticle(AtumParticles.Types.HORUS, target, target.posX, target.posY + 0.3D, target.posZ, 0.0D, 0.01D + random.nextDouble() * 0.4D, 0.0D);
            }
        }
    }

    @Override
    @Nonnull
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
        Multimap<String, AttributeModifier> map = HashMultimap.create();
        if (slot == EntityEquipmentSlot.MAINHAND) {
            map.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 5.0D, 0));
            map.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.7D, 0));
        }
        return map;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag tooltipType) {
        if (Keyboard.isKeyDown(42)) {
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getTranslationKey() + ".line1"));
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getTranslationKey() + ".line2"));
        } else {
            tooltip.add(I18n.format(this.getTranslationKey() + ".line3") + " " + TextFormatting.DARK_GRAY + "[SHIFT]");
        }
    }

    @Override
    public boolean getIsRepairable(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return repair.getItem() == Items.DIAMOND;
    }
}
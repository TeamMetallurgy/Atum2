package com.teammetallurgy.atum.items.artifacts.atum;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.ItemAtumShield;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.List;

@Mod.EventBusSubscriber
public class ItemAtumsProtection extends ItemAtumShield {
    private static boolean isBlocking = false;

    public ItemAtumsProtection() {
        super();
        this.setMaxDamage(336);
        this.setRepairItem(Items.DIAMOND);
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

    @SubscribeEvent
    public static void onUse(LivingEntityUseItemEvent.Tick event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (entity instanceof EntityPlayer && entity.getHeldItem(entity.getActiveHand()).getItem() == AtumItems.ATUMS_PROTECTION) {
            isBlocking = true;
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        Entity trueSource = event.getSource().getTrueSource();
        if (trueSource instanceof EntityLivingBase && isBlocking && ((EntityLivingBase) trueSource).getCreatureAttribute() == EnumCreatureAttribute.UNDEAD && itemRand.nextFloat() <= 0.50F) {
            EntityLivingBase entity = event.getEntityLiving();
            trueSource.setFire(8);
            trueSource.attackEntityFrom(DamageSource.GENERIC, 2.0F);
            for (int l = 0; l < 26; ++l) {
                Atum.proxy.spawnParticle(AtumParticles.Types.LIGHT_SPARKLE, entity, entity.posX + (itemRand.nextDouble() - 0.5D) * (double) entity.width, entity.posY + itemRand.nextDouble() * (double) entity.height, entity.posZ + (itemRand.nextDouble() - 0.5D) * (double) entity.width, 0.0D, 0.0D, 0.0D);
            }
            isBlocking = false;
        }
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, World world, List<String> tooltip, ITooltipFlag tooltipType) {
        if (Keyboard.isKeyDown(42)) {
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getUnlocalizedName() + ".line1"));
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getUnlocalizedName() + ".line2"));
        } else {
            tooltip.add(I18n.format(this.getUnlocalizedName() + ".line3") + " " + TextFormatting.DARK_GRAY + "[SHIFT]");
        }
    }
}
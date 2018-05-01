package com.teammetallurgy.atum.items.artifacts;

import com.teammetallurgy.atum.entity.EntityStoneguard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemNusFlux extends ItemSword {

    public ItemNusFlux(ToolMaterial material) {
        super(material);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public boolean hitEntity(@Nonnull ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (!attacker.world.isRemote && Math.random() > 0.75D && !(target instanceof EntityStoneguard)) {
            double dx = target.posX - attacker.posX;
            double dz = target.posZ - attacker.posZ;
            double magnitude = Math.sqrt(dx * dx + dz * dz);
            dx /= magnitude;
            dz /= magnitude;
            target.isAirBorne = true;
            target.addVelocity(dx / 2.0D, 1.5D, dz / 2.0D);
            if (target.motionY > 1.0D) {
                target.motionY = 1.0D;
            }

            // entity.attackEntityFrom(DamageSource.generic,
            // this.getDamageVsEntity(entity, par1ItemStack));
            if (attacker.world.isRemote) {
                this.spawnParticle(target);
            }
        }
        return super.hitEntity(stack, target, attacker);
    }

    @SideOnly(Side.CLIENT)
    public void spawnParticle(Entity entity) {
        Minecraft.getMinecraft().effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT);
    }

    @Override
    @Nonnull
    public EnumRarity getRarity(@Nonnull ItemStack stack) {
        return EnumRarity.RARE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag tooltipType) {
        if (Keyboard.isKeyDown(42)) {
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getUnlocalizedName() + ".line1"));
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getUnlocalizedName() + ".line2"));
        } else {
            tooltip.add(I18n.format(this.getUnlocalizedName() + ".line3") + " " + TextFormatting.DARK_GRAY + "[SHIFT]");
        }
    }

    @Override
    public boolean getIsRepairable(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return repair.getItem() == Items.DIAMOND;
    }
}
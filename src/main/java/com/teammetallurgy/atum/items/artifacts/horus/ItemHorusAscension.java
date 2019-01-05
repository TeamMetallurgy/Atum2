package com.teammetallurgy.atum.items.artifacts.horus;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.stone.EntityStoneBase;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.tools.ItemGauntlet;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ItemHorusAscension extends ItemGauntlet {

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

    @Override
    public boolean hitEntity(@Nonnull ItemStack stack, EntityLivingBase target, @Nullable EntityLivingBase attacker) {
        if (cooldown.get(attacker) == 1.0F) {
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
                target.addVelocity(dx / 2.0D, 1.5D, dz / 2.0D);
                if (target.motionY > 0.9D) {
                    target.motionY = 0.9D;
                }
            }
            double x = MathHelper.nextDouble(itemRand, 0.0001D, 0.04D);
            double z = MathHelper.nextDouble(itemRand, 0.0001D, 0.04D);
            for (int amount = 0; amount < 50; ++amount) {
                Atum.proxy.spawnParticle(AtumParticles.Types.HORUS, target, target.posX, target.posY + 0.3D, target.posZ, x, 0.01D + random.nextDouble() * 0.4D, -z);
            }
        }
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
}
package com.teammetallurgy.atum.items.artifacts.shu;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.projectile.arrow.EntityArrowQuickdraw;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.tools.ItemBaseBow;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemShusBreath extends ItemBaseBow {

    public ItemShusBreath() {
        super();
        this.setMaxDamage(650);
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
    protected EntityArrow setArrow(ItemStack stack, World world, EntityPlayer player, float velocity) {
        return new EntityArrowQuickdraw(world, player);
    }

    @Override
    protected float getDrawbackSpeed(@Nonnull ItemStack stack, EntityLivingBase entity) {
        return (float) (stack.getMaxItemUseDuration() - entity.getItemInUseCount()) / 10.0F;
    }

    @Override
    protected void onVelocity(World world, EntityPlayer player, float velocity) {
        if (velocity == 1.0F) {
            for (int l = 0; l < 12; ++l) {
                Atum.proxy.spawnParticle(AtumParticles.Types.SHU, player, player.posX + (world.rand.nextDouble() - 0.5D) * (double) player.width, player.posY + 1.0D, player.posZ + (world.rand.nextDouble() - 0.5D) * (double) player.width, 0.05D, 0.0D, 0.0D);
                Atum.proxy.spawnParticle(AtumParticles.Types.SHU, player, player.posX + (world.rand.nextDouble() - 0.5D) * (double) player.width, player.posY + 1.0D, player.posZ + (world.rand.nextDouble() - 0.5D) * (double) player.width, 0.0D, 0.0D, 0.05D);
                Atum.proxy.spawnParticle(AtumParticles.Types.SHU, player, player.posX + (world.rand.nextDouble() - 0.5D) * (double) player.width, player.posY + 1.0D, player.posZ + (world.rand.nextDouble() - 0.5D) * (double) player.width, -0.05D, 0.0D, 0.0D);
                Atum.proxy.spawnParticle(AtumParticles.Types.SHU, player, player.posX + (world.rand.nextDouble() - 0.5D) * (double) player.width, player.posY + 1.0D, player.posZ + (world.rand.nextDouble() - 0.5D) * (double) player.width, 0.0D, 0.0D, -0.05D);
            }
        }
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
}
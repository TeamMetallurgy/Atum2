package com.teammetallurgy.atum.items.artifacts.ra;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.projectile.arrow.CustomArrow;
import com.teammetallurgy.atum.entity.projectile.arrow.EntityArrowFire;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.tools.BaseBowItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
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

public class RasFuryItem extends BaseBowItem {

    public RasFuryItem() {
        super();
        this.setMaxDamage(650);
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

    @Override
    protected CustomArrow setArrow(ItemStack stack, World world, EntityPlayer player, float velocity) {
        return new EntityArrowFire(world, player);
    }

    @Override
    protected void onVelocity(World world, EntityPlayer player, float velocity) {
        if (velocity == 1.0F) {
            for (int amount = 0; amount < 20; ++amount) {
                float timesRandom = world.rand.nextFloat() * 4.0F;
                float cosRandom = world.rand.nextFloat() * ((float) Math.PI * 2F);
                double x = (double) (MathHelper.cos(cosRandom) * timesRandom) * 0.1D;
                double y = 0.01D + world.rand.nextDouble() * 0.1D;
                double z = (double) (MathHelper.sin(cosRandom) * timesRandom) * 0.1D;
                Atum.proxy.spawnParticle(AtumParticles.Types.RA_FIRE, player, player.posX, player.posY + 0.7D, player.posZ + z * 0.1D, x / 10, y, z / 10);
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
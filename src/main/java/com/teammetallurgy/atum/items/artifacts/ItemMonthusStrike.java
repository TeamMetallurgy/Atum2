package com.teammetallurgy.atum.items.artifacts;

import com.teammetallurgy.atum.entity.EntityPharaoh;
import com.teammetallurgy.atum.entity.EntityStoneguard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemMonthusStrike extends ItemAxe {

    public ItemMonthusStrike(ToolMaterial material) {
        super(material);
    }

    @Override
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public int getMaxItemUseDuration(@Nonnull ItemStack stack) {
        return 7200;
    }

    @Override
    @Nonnull
    public EnumAction getItemUseAction(@Nonnull ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public void onPlayerStoppedUsing(@Nonnull ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft) {
        int j = this.getMaxItemUseDuration(stack) - timeLeft;
        if (j > 21) {
            AxisAlignedBB bb = entityLiving.getEntityBoundingBox().expand(3.0D, 3.0D, 3.0D);
            List<EntityLiving> list = world.getEntitiesWithinAABB(EntityLiving.class, bb);

            for (EntityLiving entity : list) {
                if (entity != entityLiving && !(entity instanceof EntityStoneguard) && !(entity instanceof EntityPharaoh)) {
                    double dx = entity.posX - entityLiving.posX;
                    double dz = entity.posZ - entityLiving.posZ;
                    double magnitude = Math.sqrt(dx * dx + dz * dz);
                    dx /= magnitude;
                    dz /= magnitude;
                    entity.isAirBorne = true;
                    entity.addVelocity(dx * 2.5D, 0.3D, dz * 2.5D);
                    if (entity.motionY > 0.4000000059604645D) {
                        entity.motionY = 0.4000000059604645D;
                    }

                    if (world.isRemote) {
                        this.spawnParticle(entity);
                    }
                }
            }
            stack.damageItem(4, entityLiving);
        }
    }

    @SideOnly(Side.CLIENT)
    public void spawnParticle(Entity entity) {
        Minecraft.getMinecraft().effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, @Nonnull EnumHand hand) {
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
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
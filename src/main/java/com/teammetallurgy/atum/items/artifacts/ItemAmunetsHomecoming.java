package com.teammetallurgy.atum.items.artifacts;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemAmunetsHomecoming extends Item {

    public ItemAmunetsHomecoming() {
        super();
        this.setMaxDamage(20);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {// TODO FIX
        ItemStack stack = player.getHeldItem(hand);
        BlockPos spawn = player.getBedLocation(player.dimension);
        if (spawn == null) {
            spawn = world.getSpawnPoint();
        }

        if (spawn == null) {
            spawn = world.getSpawnPoint();
        }

        spawn = EntityPlayer.getBedSpawnLocation(world, spawn, false);
        if (spawn == null) {
            spawn = world.getSpawnPoint();
        }

        player.rotationPitch = 0.0F;
        player.rotationYaw = 0.0F;
        player.setPositionAndUpdate((double) spawn.getX() + 0.5D, (double) spawn.getY() + 0.1D, (double) spawn.getZ());

        while (!world.getCollisionBoxes(player, player.getEntityBoundingBox()).isEmpty()) {
            player.setPosition(player.posX, player.posY + 1.0D, player.posZ);
        }

        stack.damageItem(1, player);
        return new ActionResult<>(EnumActionResult.PASS, stack);
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
}
package com.teammetallurgy.atum.items.artifacts.shu;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumParticles;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ItemShusSwiftness extends Item {
    private static final AttributeModifier SPEED_BOOST = new AttributeModifier(UUID.fromString("f51280de-21d2-47f5-bc9a-e55ef1acfe2d"), "Shu's Swiftness speed boost", 0.025D, 0);

    public ItemShusSwiftness() {
        super();
        this.setMaxDamage(12000);
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
    public void onUpdate(@Nonnull ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            ModifiableAttributeInstance attribute = (ModifiableAttributeInstance) player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
            if (player.onGround) {
                if (player.getHeldItem(EnumHand.OFF_HAND).getItem() == this) {
                    this.doEffect(world, player, stack);
                } else if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() == this) {
                    this.doEffect(world, player, stack);
                } else if (attribute.hasModifier(SPEED_BOOST)) {
                    attribute.removeModifier(SPEED_BOOST);
                }
            }
        }
    }

    private void doEffect(World world, EntityPlayer player, @Nonnull ItemStack heldStack) {
        ModifiableAttributeInstance attribute = (ModifiableAttributeInstance) player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        if (player.moveForward != 0.0F) {
            for (int l = 0; l < 2; ++l) {
                Atum.proxy.spawnParticle(AtumParticles.Types.ISIS, player, player.posX + (world.rand.nextDouble() - 0.5D) * (double) player.width, player.posY + world.rand.nextDouble() * (double) player.height, player.posZ + (world.rand.nextDouble() - 0.5D) * (double) player.width, 0.0D, 0.0D, 0.0D);
            }
        }
        if (!world.isRemote) {
            if (!attribute.hasModifier(SPEED_BOOST)) {
                attribute.applyModifier(SPEED_BOOST);
            }
            if (!player.capabilities.isCreativeMode) {
                heldStack.damageItem(1, player);
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

        double remaining = ((stack.getMaxDamage() - stack.getItemDamage()) / 12) / 100.0D;
        String localizedRemaining = I18n.format("tooltip.atum.minutesRemaining", remaining);
        tooltip.add(localizedRemaining);
    }

    @Override
    public boolean getIsRepairable(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return repair.getItem() == Items.DIAMOND;
    }
}
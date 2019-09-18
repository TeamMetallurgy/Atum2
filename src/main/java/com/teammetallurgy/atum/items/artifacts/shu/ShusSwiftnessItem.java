package com.teammetallurgy.atum.items.artifacts.shu;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.AmuletItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

public class ShusSwiftnessItem extends AmuletItem {
    private static final AttributeModifier SPEED_BOOST = new AttributeModifier(UUID.fromString("f51280de-21d2-47f5-bc9a-e55ef1acfe2d"), "Shu's Swiftness speed boost", 0.025D, 0);

    public ShusSwiftnessItem() {
        super();
        this.setMaxDamage(12000);
    }

    @Override
    @Optional.Method(modid = "baubles")
    public void onWornTick(ItemStack stack, LivingEntity livingBase) {
        if (livingBase instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) livingBase;
            ModifiableAttributeInstance attribute = (ModifiableAttributeInstance) player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
            if (player.onGround) {
                if (stack.getItem() == this) {
                    this.doEffect(player.world, player, stack);
                } else if (attribute.hasModifier(SPEED_BOOST)) {
                    attribute.removeModifier(SPEED_BOOST);
                }
            }
        }
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
                Atum.proxy.spawnParticle(AtumParticles.Types.SHU, player, player.posX + (world.rand.nextDouble() - 0.5D) * (double) player.width, player.posY + 0.2D, player.posZ + (world.rand.nextDouble() - 0.5D) * (double) player.width, 0.0D, 0.0D, 0.0D);
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
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getTranslationKey() + ".line1"));
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getTranslationKey() + ".line2"));
        } else {
            tooltip.add(I18n.format(this.getTranslationKey() + ".line3") + " " + TextFormatting.DARK_GRAY + "[SHIFT]");
        }

        double remaining = ((double)(stack.getMaxDamage() - stack.getItemDamage()) / 12) / 100.0D;
        DecimalFormat format = new DecimalFormat("#.##");
        String localizedRemaining = I18n.format("tooltip.atum.minutesRemaining",  format.format(remaining));
        tooltip.add(localizedRemaining);
    }
}
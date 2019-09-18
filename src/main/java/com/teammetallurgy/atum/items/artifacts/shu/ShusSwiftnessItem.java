package com.teammetallurgy.atum.items.artifacts.shu;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.AmuletItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Optional;

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
        if (livingBase instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) livingBase;
            ModifiableAttributeInstance attribute = (ModifiableAttributeInstance) player.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
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
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            ModifiableAttributeInstance attribute = (ModifiableAttributeInstance) player.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
            if (player.onGround) {
                if (player.getHeldItem(Hand.OFF_HAND).getItem() == this) {
                    this.doEffect(world, player, stack);
                } else if (player.getHeldItem(Hand.MAIN_HAND).getItem() == this) {
                    this.doEffect(world, player, stack);
                } else if (attribute.hasModifier(SPEED_BOOST)) {
                    attribute.removeModifier(SPEED_BOOST);
                }
            }
        }
    }

    private void doEffect(World world, PlayerEntity player, @Nonnull ItemStack heldStack) {
        ModifiableAttributeInstance attribute = (ModifiableAttributeInstance) player.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        if (player.moveForward != 0.0F) {
            for (int l = 0; l < 2; ++l) {
                Atum.proxy.spawnParticle(AtumParticles.Types.SHU, player, player.posX + (world.rand.nextDouble() - 0.5D) * (double) player.getWidth(), player.posY + 0.2D, player.posZ + (world.rand.nextDouble() - 0.5D) * (double) player.getWidth(), 0.0D, 0.0D, 0.0D);
            }
        }
        if (!world.isRemote) {
            if (!attribute.hasModifier(SPEED_BOOST)) {
                attribute.applyModifier(SPEED_BOOST);
            }
            if (!player.abilities.isCreativeMode) {
                heldStack.damageItem(1, player);
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag tooltipType) {
        super.addInformation(stack, world, tooltip, tooltipType);
        double remaining = ((double) (stack.getMaxDamage() - stack.getDamage()) / 12) / 100.0D;
        DecimalFormat format = new DecimalFormat("#.##");
        tooltip.add(new TranslationTextComponent("atum.tooltip.minutesRemaining", format.format(remaining)));
    }
}
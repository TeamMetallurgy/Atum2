package com.teammetallurgy.atum.items.artifacts.shu;

import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.artifacts.AmuletItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

public class ShusSwiftnessItem extends AmuletItem implements IArtifact {
    private static final AttributeModifier SPEED_BOOST = new AttributeModifier(UUID.fromString("f51280de-21d2-47f5-bc9a-e55ef1acfe2d"), "Shu's Swiftness speed boost", 0.025D, AttributeModifier.Operation.ADDITION);

    public ShusSwiftnessItem() {
        super(new Item.Properties());
    }

    @Override
    public God getGod() {
        return God.SHU;
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, @Nonnull ItemStack stack) {
        ModifiableAttributeInstance attribute = livingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (livingEntity.isOnGround() && (livingEntity.moveForward > 0.0F || livingEntity.isSprinting())) {
            this.doEffect(livingEntity, stack);
        } else {
            if (attribute != null && attribute.hasModifier(SPEED_BOOST)) {
                attribute.removeModifier(SPEED_BOOST);
            }
        }
    }

    private void doEffect(LivingEntity livingEntity, @Nonnull ItemStack stack) {
        World world = livingEntity.getEntityWorld();
        ModifiableAttributeInstance attribute = livingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
        for (int l = 0; l < 2; ++l) {
            world.addParticle(AtumParticles.SHU, livingEntity.getPosX() + (world.rand.nextDouble() - 0.5D) * (double) livingEntity.getWidth(), livingEntity.getPosY() + 0.2D, livingEntity.getPosZ() + (world.rand.nextDouble() - 0.5D) * (double) livingEntity.getWidth(), 0.0D, 0.0D, 0.0D);
        }
        if (attribute != null && !attribute.hasModifier(SPEED_BOOST)) {
            attribute.applyNonPersistentModifier(SPEED_BOOST);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag tooltipType) {
        super.addInformation(stack, world, tooltip, tooltipType);
        double remaining = ((double) (stack.getMaxDamage() - stack.getDamage()) / 12) / 100.0D;
        DecimalFormat format = new DecimalFormat("#.##");
        tooltip.add(new TranslationTextComponent("atum.tooltip.minutes_remaining", format.format(remaining)));
    }
}
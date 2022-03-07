package com.teammetallurgy.atum.items.artifacts.shu;

import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.artifacts.AmuletItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.UUID;

public class ShusSwiftnessItem extends AmuletItem implements IArtifact {
    private static final AttributeModifier SPEED_BOOST = new AttributeModifier(UUID.fromString("f51280de-21d2-47f5-bc9a-e55ef1acfe2d"), "Shu's Swiftness speed boost", 0.025D, AttributeModifier.Operation.ADDITION);

    public ShusSwiftnessItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public God getGod() {
        return God.SHU;
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, @Nonnull ItemStack stack) {
        AttributeInstance attribute = livingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (livingEntity.isOnGround() && (livingEntity.zza > 0.0F || livingEntity.isSprinting())) {
            this.doEffect(livingEntity, stack);
        } else {
            if (attribute != null && attribute.hasModifier(SPEED_BOOST)) {
                attribute.removeModifier(SPEED_BOOST);
            }
        }
    }

    private void doEffect(LivingEntity livingEntity, @Nonnull ItemStack stack) {
        Level world = livingEntity.getCommandSenderWorld();
        AttributeInstance attribute = livingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
        for (int l = 0; l < 2; ++l) {
            world.addParticle(AtumParticles.SHU.get(), livingEntity.getX() + (world.random.nextDouble() - 0.5D) * (double) livingEntity.getBbWidth(), livingEntity.getY() + 0.2D, livingEntity.getZ() + (world.random.nextDouble() - 0.5D) * (double) livingEntity.getBbWidth(), 0.0D, 0.0D, 0.0D);
        }
        if (attribute != null && !attribute.hasModifier(SPEED_BOOST)) {
            attribute.addTransientModifier(SPEED_BOOST);
        }
    }
}
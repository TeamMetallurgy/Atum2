package com.teammetallurgy.atum.items.artifacts.shu;

import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.artifacts.AmuletItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.UUID;

public class ShusSwiftnessItem extends AmuletItem implements IArtifact {
    private static final AttributeModifier SPEED_BOOST = new AttributeModifier(UUID.fromString("f51280de-21d2-47f5-bc9a-e55ef1acfe2d"), "Shu's Swiftness speed boost", 0.025D, AttributeModifier.Operation.ADDITION);

    public ShusSwiftnessItem() {
        super(new Item.Properties().maxStackSize(1));
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
}
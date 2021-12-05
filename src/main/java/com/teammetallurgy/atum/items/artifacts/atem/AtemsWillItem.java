package com.teammetallurgy.atum.items.artifacts.atem;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.AtumMats;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

public class AtemsWillItem extends SwordItem implements IArtifact {

    public AtemsWillItem() {
        super(AtumMats.NEBU, 3, -2.4F, new Item.Properties().rarity(Rarity.RARE).group(Atum.GROUP));
    }

    @Override
    public God getGod() {
        return God.ATEM;
    }

    @Override
    public boolean hitEntity(@Nonnull ItemStack stack, @Nonnull LivingEntity target, @Nonnull LivingEntity attacker) {
        if (random.nextDouble() <= 0.15D) {
            attacker.addPotionEffect(new EffectInstance(Effects.RESISTANCE, (MathHelper.nextInt(random, 5, 20)) * 20, 0, false, false));
        }
        return super.hitEntity(stack, target, attacker);
    }
}
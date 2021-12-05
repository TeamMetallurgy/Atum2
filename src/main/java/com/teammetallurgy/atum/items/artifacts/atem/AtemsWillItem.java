package com.teammetallurgy.atum.items.artifacts.atem;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.AtumMats;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.util.Mth;

import javax.annotation.Nonnull;

public class AtemsWillItem extends SwordItem implements IArtifact {

    public AtemsWillItem() {
        super(AtumMats.NEBU, 3, -2.4F, new Item.Properties().rarity(Rarity.RARE).tab(Atum.GROUP));
    }

    @Override
    public God getGod() {
        return God.ATEM;
    }

    @Override
    public boolean hurtEnemy(@Nonnull ItemStack stack, @Nonnull LivingEntity target, @Nonnull LivingEntity attacker) {
        if (random.nextDouble() <= 0.15D) {
            attacker.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, (Mth.nextInt(random, 5, 20)) * 20, 0, false, false));
        }
        return super.hurtEnemy(stack, target, attacker);
    }
}
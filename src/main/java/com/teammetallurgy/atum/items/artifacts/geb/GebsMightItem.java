package com.teammetallurgy.atum.items.artifacts.geb;

import com.teammetallurgy.atum.api.AtumMats;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.tools.HammerItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import java.util.Random;

public class GebsMightItem extends HammerItem implements IArtifact {

    public GebsMightItem() {
        super(AtumMats.NEBU, new Item.Properties().rarity(Rarity.RARE));
    }

    @Override
    public God getGod() {
        return God.GEB;
    }

    @Override
    protected void onStun(LivingEntity target) {
        STUN.put(target, 80);
        if (target.level instanceof ServerLevel serverLevel) {
            Random random = serverLevel.random;
            double d0 = random.nextGaussian() * 0.02D;
            double d1 = random.nextGaussian() * 0.02D;
            double d2 = random.nextGaussian() * 0.02D;
            serverLevel.sendParticles(AtumParticles.GEB.get(), target.getX(), target.getY() + target.getEyeHeight() - 0.1D, target.getZ(), 35, d0, d1, d2, 0.04D);
        }
    }
}
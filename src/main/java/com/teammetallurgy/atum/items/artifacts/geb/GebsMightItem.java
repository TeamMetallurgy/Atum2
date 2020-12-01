package com.teammetallurgy.atum.items.artifacts.geb;

import com.teammetallurgy.atum.api.AtumMats;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.tools.HammerItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.world.server.ServerWorld;

public class GebsMightItem extends HammerItem {

    public GebsMightItem() {
        super(AtumMats.NEBU, new Item.Properties().rarity(Rarity.RARE));
    }

    @Override
    protected void onStun(LivingEntity target) {
        STUN.put(target, 80);
        if (target.world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) target.world;
            double d0 = random.nextGaussian() * 0.02D;
            double d1 = random.nextGaussian() * 0.02D;
            double d2 = random.nextGaussian() * 0.02D;
            serverWorld.spawnParticle(AtumParticles.GEB, target.getPosX(), target.getPosY() + target.getEyeHeight() - 0.1D, target.getPosZ(), 35, d0, d1, d2, 0.04D);
        }
    }
}
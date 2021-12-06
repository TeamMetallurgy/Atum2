package com.teammetallurgy.atum.items.artifacts.horus;

import com.teammetallurgy.atum.api.AtumMats;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.entity.stone.StoneBaseEntity;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.tools.GauntletItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import java.util.Random;

public class HorusAscensionItem extends GauntletItem implements IArtifact {

    public HorusAscensionItem() {
        super(AtumMats.NEBU, new Item.Properties().rarity(Rarity.RARE));
    }

    @Override
    public God getGod() {
        return God.HORUS;
    }

    @Override
    public boolean hurtEnemy(@Nonnull ItemStack stack, @Nonnull LivingEntity target, @Nonnull LivingEntity attacker) {
        if (COOLDOWN.getFloat(attacker) == 1.0F) {
            knockUp(target, attacker, random);
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    public static void knockUp(LivingEntity target, LivingEntity attacker, Random random) {
        if (attacker != null && !(target instanceof StoneBaseEntity)) {
            if (!attacker.level.isClientSide) {
                double dx = target.getX() - attacker.getX();
                double dz = target.getZ() - attacker.getZ();
                double magnitude = Math.sqrt(dx * dx + dz * dz);
                dx /= magnitude;
                dz /= magnitude;
                target.push(dx / 2.0D, 1.5D, dz / 2.0D);
                Vec3 motion = target.getDeltaMovement();
                if (motion.y > 0.9D) {
                    target.setDeltaMovement(motion.x, 0.9D, motion.z);
                }
            }
            if (target.level instanceof ServerLevel) {
                ServerLevel serverWorld = (ServerLevel) target.level;
                double x = Mth.nextDouble(random, 0.0001D, 0.04D);
                double z = Mth.nextDouble(random, 0.0001D, 0.04D);
                serverWorld.sendParticles(AtumParticles.HORUS, target.getX(), target.getY() + 0.9D, target.getZ(), 65, x, 0.9D, -z, 0.005D);
            }
        }
    }
}
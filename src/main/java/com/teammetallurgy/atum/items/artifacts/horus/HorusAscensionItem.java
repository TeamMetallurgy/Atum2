package com.teammetallurgy.atum.items.artifacts.horus;

import com.teammetallurgy.atum.entity.stone.StoneBaseEntity;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.tools.GauntletItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Rarity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Random;

public class HorusAscensionItem extends GauntletItem {

    public HorusAscensionItem() {
        super(ItemTier.DIAMOND, new Item.Properties().rarity(Rarity.RARE));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public boolean hitEntity(@Nonnull ItemStack stack, @Nonnull LivingEntity target, @Nonnull LivingEntity attacker) {
        if (cooldown.getFloat(attacker) == 1.0F) {
            knockUp(target, attacker, random);
        }
        return super.hitEntity(stack, target, attacker);
    }

    public static void knockUp(LivingEntity target, LivingEntity attacker, Random random) {
        if (attacker != null && !(target instanceof StoneBaseEntity)) {
            if (!attacker.world.isRemote) {
                double dx = target.getPosX() - attacker.getPosX();
                double dz = target.getPosZ() - attacker.getPosZ();
                double magnitude = Math.sqrt(dx * dx + dz * dz);
                dx /= magnitude;
                dz /= magnitude;
                target.addVelocity(dx / 2.0D, 1.5D, dz / 2.0D);
                Vector3d motion = target.getMotion();
                if (motion.y > 0.9D) {
                    target.setMotion(motion.x, 0.9D, motion.z);
                }
            }
            if (target.world instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) target.world;
                double x = MathHelper.nextDouble(random, 0.0001D, 0.04D);
                double z = MathHelper.nextDouble(random, 0.0001D, 0.04D);
                serverWorld.spawnParticle(AtumParticles.HORUS, target.getPosX(), target.getPosY() + 0.9D, target.getPosZ(), 65, x, 0.9D, -z, 0.005D);
            }
        }
    }
}
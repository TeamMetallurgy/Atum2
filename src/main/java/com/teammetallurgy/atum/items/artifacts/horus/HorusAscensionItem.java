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
import net.minecraft.util.math.Vec3d;
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
    public boolean hitEntity(@Nonnull ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (cooldown.getFloat(attacker) == 1.0F) {
            knockUp(target, attacker, random);
        }
        return super.hitEntity(stack, target, attacker);
    }

    public static void knockUp(LivingEntity target, LivingEntity attacker, Random random) {
        if (attacker != null && !(target instanceof StoneBaseEntity)) {
            if (!attacker.world.isRemote) {
                double dx = target.posX - attacker.posX;
                double dz = target.posZ - attacker.posZ;
                double magnitude = Math.sqrt(dx * dx + dz * dz);
                dx /= magnitude;
                dz /= magnitude;
                target.addVelocity(dx / 2.0D, 1.5D, dz / 2.0D);
                Vec3d motion = target.getMotion();
                if (motion.y > 0.9D) {
                    target.setMotion(motion.x, 0.9D, motion.z);
                }
            }
            double x = MathHelper.nextDouble(random, 0.0001D, 0.04D);
            double z = MathHelper.nextDouble(random, 0.0001D, 0.04D);
            for (int amount = 0; amount < 50; ++amount) {
                target.world.addParticle(AtumParticles.HORUS, target.posX, target.posY + 0.3D, target.posZ, x, 0.01D + random.nextDouble() * 0.4D, -z);
            }
        }
    }
}
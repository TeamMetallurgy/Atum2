package com.teammetallurgy.atum.entity.projectile.arrow;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityArrowDoubleShotWhite extends CustomArrow {

    public EntityArrowDoubleShotWhite(World world) {
        super(world);
    }

    public EntityArrowDoubleShotWhite(World world, EntityLivingBase shooter) {
        super(world, shooter);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.getIsCritical()) {
            for (int l = 0; l < 8; ++l) {
                Atum.proxy.spawnParticle(AtumParticles.Types.NUIT_WHITE, this, this.posX + (world.rand.nextDouble() - 0.5D) * (double) this.width, this.posY + world.rand.nextDouble() * (double) this.height, this.posZ + (world.rand.nextDouble() - 0.5D) * (double) this.width, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/arrow/arrow_double_white.png");
    }
}
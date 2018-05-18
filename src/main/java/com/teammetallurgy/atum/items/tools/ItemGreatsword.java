package com.teammetallurgy.atum.items.tools;

import com.teammetallurgy.atum.entity.EntityPharaoh;
import com.teammetallurgy.atum.entity.EntityStoneguard;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

public class ItemGreatsword extends ItemSword {

    public ItemGreatsword(ToolMaterial material) {
        super(material);
    }

    @Override
    public boolean hitEntity(@Nonnull ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (!(target instanceof EntityStoneguard) && !(target instanceof EntityPharaoh)) {
            float j = 1.2F;
            target.addVelocity((double) (-MathHelper.sin(attacker.rotationYaw * 3.1415927F / 180.0F) * j * 0.5F), 0.1D, (double) (MathHelper.cos(attacker.rotationYaw * 3.1415927F / 180.0F) * j * 0.5F));
        }
        return super.hitEntity(stack, target, attacker);
    }
}
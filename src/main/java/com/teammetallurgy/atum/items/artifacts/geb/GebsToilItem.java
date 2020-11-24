package com.teammetallurgy.atum.items.artifacts.geb;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.AtumMats;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class GebsToilItem extends ShovelItem {

    public GebsToilItem() {
        super(AtumMats.NEBU, 2.0F, -3.0F, new Item.Properties().rarity(Rarity.RARE).group(Atum.GROUP));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public boolean onBlockDestroyed(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull BlockState state, @Nonnull BlockPos pos, @Nonnull LivingEntity entityLiving) {
        super.onBlockDestroyed(stack, world, state, pos, entityLiving);
        if (entityLiving instanceof PlayerEntity && !world.isRemote) {
            ((PlayerEntity) entityLiving).getFoodStats().addExhaustion(-0.005F);
            if (random.nextFloat() <= 0.10F) {
                world.addEntity(new ExperienceOrbEntity(world, pos.getX(), pos.getY(), pos.getZ(), 1));
            }
        }
        return true;
    }
}
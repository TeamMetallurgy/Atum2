package com.teammetallurgy.atum.items.artifacts.geb;

import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.api.material.AtumMaterialTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class GebsToilItem extends ShovelItem implements IArtifact {

    public GebsToilItem() {
        super(AtumMaterialTiers.NEBU, 2.0F, -3.0F, new Item.Properties().rarity(Rarity.RARE));
    }

    @Override
    public God getGod() {
        return God.GEB;
    }

    @Override
    public boolean mineBlock(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull BlockState state, @Nonnull BlockPos pos, @Nonnull LivingEntity entityLiving) {
        super.mineBlock(stack, level, state, pos, entityLiving);
        if (entityLiving instanceof Player && !level.isClientSide) {
            ((Player) entityLiving).getFoodData().addExhaustion(-0.005F);
            if (level.random.nextFloat() <= 0.10F) {
                level.addFreshEntity(new ExperienceOrb(level, pos.getX(), pos.getY(), pos.getZ(), 1));
            }
        }
        return true;
    }
}
package com.teammetallurgy.atum.blocks.vegetation;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nonnull;
import java.util.Random;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class OphidianTongueBlock extends VineBlock {
    private static final BooleanProperty HAS_FLOWERS = BooleanProperty.create("flowers");

    public OphidianTongueBlock() {
        super(Properties.of(Material.REPLACEABLE_PLANT).noCollission().randomTicks().strength(0.2F).sound(SoundType.GRASS));
        this.registerDefaultState(this.stateDefinition.any().setValue(UP, Boolean.FALSE).setValue(NORTH, Boolean.FALSE).setValue(EAST, Boolean.FALSE).setValue(SOUTH, Boolean.FALSE).setValue(WEST, Boolean.FALSE).setValue(HAS_FLOWERS, false));
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        if (!world.isClientSide && state.getValue(HAS_FLOWERS) && entity instanceof LivingEntity) {
            LivingEntity livingBase = (LivingEntity) entity;
            if (livingBase instanceof Player) {
                Player player = (Player) livingBase;
                if (!player.isCreative()) {
                    player.addEffect(new MobEffectInstance(MobEffects.POISON, 35));
                }
            } else {
                livingBase.addEffect(new MobEffectInstance(MobEffects.POISON, 35));
            }
        }
    }

    @Override
    public void tick(@Nonnull BlockState state, ServerLevel world, @Nonnull BlockPos pos, @Nonnull Random rand) {
        super.tick(state, world, pos, rand);
        if (!world.isClientSide && !state.getValue(HAS_FLOWERS) && rand.nextDouble() <= 0.03D) {
            world.setBlock(pos, state.setValue(HAS_FLOWERS, true), 2);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
        container.add(UP, NORTH, EAST, SOUTH, WEST, HAS_FLOWERS);
    }
}
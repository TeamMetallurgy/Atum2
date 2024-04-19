package com.teammetallurgy.atum.blocks.vegetation;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import javax.annotation.Nonnull;

public class OphidianTongueBlock extends VineBlock {
    private static final BooleanProperty HAS_FLOWERS = BooleanProperty.create("flowers");

    public OphidianTongueBlock() {
        super(Properties.of().mapColor(MapColor.PLANT).replaceable().ignitedByLava().pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0.2F).sound(SoundType.GRASS));
        this.registerDefaultState(this.stateDefinition.any().setValue(UP, Boolean.FALSE).setValue(NORTH, Boolean.FALSE).setValue(EAST, Boolean.FALSE).setValue(SOUTH, Boolean.FALSE).setValue(WEST, Boolean.FALSE).setValue(HAS_FLOWERS, false));
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && state.getValue(HAS_FLOWERS) && entity instanceof LivingEntity livingBase) {
            if (livingBase instanceof Player player) {
                if (!player.isCreative()) {
                    player.addEffect(new MobEffectInstance(MobEffects.POISON, 35));
                }
            } else {
                livingBase.addEffect(new MobEffectInstance(MobEffects.POISON, 35));
            }
        }
    }

    @Override
    public void tick(@Nonnull BlockState state, ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
        super.tick(state, level, pos, rand);
        if (!level.isClientSide && !state.getValue(HAS_FLOWERS) && rand.nextDouble() <= 0.03D) {
            level.setBlock(pos, state.setValue(HAS_FLOWERS, true), 2);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
        container.add(UP, NORTH, EAST, SOUTH, WEST, HAS_FLOWERS);
    }
}
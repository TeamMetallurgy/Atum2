package com.teammetallurgy.atum.blocks.vegetation;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.VineBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.Random;

public class OphidianTongueBlock extends VineBlock {
    private static final BooleanProperty HAS_FLOWERS = BooleanProperty.create("flowers");

    public OphidianTongueBlock() {
        super(Properties.create(Material.TALL_PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0.2F).sound(SoundType.PLANT));
        this.setDefaultState(this.stateContainer.getBaseState().with(UP, Boolean.FALSE).with(NORTH, Boolean.FALSE).with(EAST, Boolean.FALSE).with(SOUTH, Boolean.FALSE).with(WEST, Boolean.FALSE).with(HAS_FLOWERS, false));
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isRemote && state.get(HAS_FLOWERS) && entity instanceof LivingEntity) {
            LivingEntity livingBase = (LivingEntity) entity;
            if (livingBase instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) livingBase;
                if (!player.isCreative()) {
                    player.addPotionEffect(new EffectInstance(Effects.POISON, 35));
                }
            } else {
                livingBase.addPotionEffect(new EffectInstance(Effects.POISON, 35));
            }
        }
    }

    @Override
    public void tick(@Nonnull BlockState state, ServerWorld world, @Nonnull BlockPos pos, @Nonnull Random rand) {
        super.tick(state, world, pos, rand);
        if (!world.isRemote && !state.get(HAS_FLOWERS) && rand.nextDouble() <= 0.03D) {
            world.setBlockState(pos, state.with(HAS_FLOWERS, true), 2);
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
        container.add(UP, NORTH, EAST, SOUTH, WEST, HAS_FLOWERS);
    }
}
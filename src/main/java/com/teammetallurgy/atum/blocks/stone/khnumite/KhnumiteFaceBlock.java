package com.teammetallurgy.atum.blocks.stone.khnumite;

import com.teammetallurgy.atum.entity.stone.StoneguardEntity;
import com.teammetallurgy.atum.entity.stone.StonewardenEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockMaterialPredicate;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import javax.annotation.Nonnull;

public class KhnumiteFaceBlock extends HorizontalDirectionalBlock implements IKhnumite {
    private BlockPattern stoneguardBasePattern;
    private BlockPattern stoneguardPattern;
    private BlockPattern stonewardenBasePattern;
    private BlockPattern stonewardenPattern;

    public KhnumiteFaceBlock() {
        super(Block.Properties.of(Material.STONE, MaterialColor.CLAY).strength(2.0F));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public void onPlace(BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, BlockState oldState, boolean isMoving) {
        if (oldState.getBlock() != state.getBlock()) {
            this.trySpawnStonemob(world, pos);
        }
    }

    private void trySpawnStonemob(Level world, BlockPos pos) {
        BlockPattern.BlockPatternMatch patternHelper = this.getStonewardenPattern().find(world, pos);

        if (patternHelper != null) {
            for (int x = 0; x < this.getStonewardenPattern().getWidth(); ++x) {
                for (int y = 0; y < this.getStonewardenPattern().getHeight(); ++y) {
                    BlockPos patternPos = patternHelper.getBlock(x, y, 0).getPos();
                    if (world.getBlockState(patternPos).getBlock() instanceof IKhnumite) {
                        world.setBlock(patternPos, Blocks.AIR.defaultBlockState(), 2);
                    }
                }
            }

            BlockPos stonewardenPos = patternHelper.getBlock(1, 2, 0).getPos();
            StonewardenEntity stonewarden = AtumEntities.STONEWARDEN_FRIENDLY.get().create(world);
            if (stonewarden != null) {
                stonewarden.setPlayerCreated(true);
                if (world instanceof ServerLevel) {
                    stonewarden.finalizeSpawn((ServerLevelAccessor) world, world.getCurrentDifficultyAt(pos), MobSpawnType.MOB_SUMMONED, null, null);
                    stonewarden.moveTo((double) stonewardenPos.getX() + 0.5D, (double) stonewardenPos.getY() + 0.05D, (double) stonewardenPos.getZ() + 0.5D, 0.0F, 0.0F);
                    world.addFreshEntity(stonewarden);
                }

                for (ServerPlayer playerMP : world.getEntitiesOfClass(ServerPlayer.class, stonewarden.getBoundingBox().inflate(5.0D))) {
                    CriteriaTriggers.SUMMONED_ENTITY.trigger(playerMP, stonewarden);
                }

                for (int x = 0; x < this.getStonewardenPattern().getWidth(); ++x) {
                    for (int y = 0; y < this.getStonewardenPattern().getHeight(); ++y) {
                        BlockInWorld worldState1 = patternHelper.getBlock(x, y, 0);
                        world.updateNeighborsAt(worldState1.getPos(), Blocks.AIR);
                    }
                }
            }
        } else {
            patternHelper = this.getStoneguardPattern().find(world, pos);

            if (patternHelper != null) {
                for (int x = 0; x < this.getStoneguardPattern().getWidth(); ++x) {
                    for (int y = 0; y < this.getStoneguardPattern().getHeight(); ++y) {
                        BlockInWorld worldState = patternHelper.getBlock(x, y, 0);
                        if (world.getBlockState(worldState.getPos()).getBlock() instanceof IKhnumite) {
                            world.setBlock(worldState.getPos(), Blocks.AIR.defaultBlockState(), 2);
                        }
                    }
                }
                StoneguardEntity stoneguard = AtumEntities.STONEGUARD_FRIENDLY.get().create(world);
                if (stoneguard != null) {
                    stoneguard.setPlayerCreated(true);
                    if (world instanceof ServerLevel) {
                        stoneguard.finalizeSpawn((ServerLevelAccessor) world, world.getCurrentDifficultyAt(pos), MobSpawnType.MOB_SUMMONED, null, null);
                        BlockPos stoneguardPos = patternHelper.getBlock(0, 2, 0).getPos();
                        stoneguard.moveTo((double) stoneguardPos.getX() + 0.5D, (double) stoneguardPos.getY() + 0.05D, (double) stoneguardPos.getZ() + 0.5D, 0.0F, 0.0F);
                        world.addFreshEntity(stoneguard);
                    }

                    for (ServerPlayer playerMP : world.getEntitiesOfClass(ServerPlayer.class, stoneguard.getBoundingBox().inflate(5.0D))) {
                        CriteriaTriggers.SUMMONED_ENTITY.trigger(playerMP, stoneguard);
                    }

                    for (int x = 0; x < this.getStoneguardPattern().getWidth(); ++x) {
                        for (int y = 0; y < this.getStoneguardPattern().getHeight(); ++y) {
                            BlockInWorld worldState = patternHelper.getBlock(x, y, 0);
                            world.updateNeighborsAt(worldState.getPos(), Blocks.AIR);
                        }
                    }
                }
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public static void addDispenserSupport() {
        DispenserBlock.registerBehavior(AtumBlocks.KHNUMITE_FACE.get().asItem(), new OptionalDispenseItemBehavior() {
            @Override
            @Nonnull
            protected ItemStack execute(@Nonnull BlockSource source, @Nonnull ItemStack stack) {
                Level world = source.getLevel();
                BlockPos pos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
                KhnumiteFaceBlock khnumiteFace = (KhnumiteFaceBlock) AtumBlocks.KHNUMITE_FACE.get();

                if (world.isEmptyBlock(pos) && khnumiteFace.canDispenserPlace(world, pos)) {
                    if (!world.isClientSide) {
                        world.setBlock(pos, khnumiteFace.defaultBlockState(), 3);
                    }
                    stack.shrink(1);
                    this.setSuccess(true);
                } else {
                    this.setSuccess(ArmorItem.dispenseArmor(source, stack));
                }
                return stack;
            }
        });
    }

    private boolean canDispenserPlace(Level world, BlockPos pos) {
        return this.getStonewardenBasePattern().find(world, pos) != null || this.getStoneguardBasePattern().find(world, pos) != null;
    }

    private BlockPattern getStoneguardBasePattern() {
        if (this.stoneguardBasePattern == null) {
            this.stoneguardBasePattern = BlockPatternBuilder.start().aisle("   ", "~#~", " # ").where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(AtumBlocks.KHNUMITE_BLOCK.get()))).where('~', BlockInWorld.hasState(BlockMaterialPredicate.forMaterial(Material.AIR))).build();
        }
        return this.stoneguardBasePattern;
    }

    private BlockPattern getStoneguardPattern() {
        if (this.stoneguardPattern == null) {
            this.stoneguardPattern = BlockPatternBuilder.start().aisle(" ^ ", "~#~", " # ").where('^', BlockInWorld.hasState(BlockStatePredicate.forBlock(AtumBlocks.KHNUMITE_FACE.get()))).where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(AtumBlocks.KHNUMITE_BLOCK.get()))).where('~', BlockInWorld.hasState(BlockMaterialPredicate.forMaterial(Material.AIR))).build();
        }
        return this.stoneguardPattern;
    }

    private BlockPattern getStonewardenBasePattern() {
        if (this.stonewardenBasePattern == null) {
            this.stonewardenBasePattern = BlockPatternBuilder.start().aisle("~ ~", "###", "~#~").where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(AtumBlocks.KHNUMITE_BLOCK.get()))).where('~', BlockInWorld.hasState(BlockMaterialPredicate.forMaterial(Material.AIR))).build();
        }
        return this.stonewardenBasePattern;
    }

    private BlockPattern getStonewardenPattern() {
        if (this.stonewardenPattern == null) {
            this.stonewardenPattern = BlockPatternBuilder.start().aisle("~^~", "###", "~#~").where('^', BlockInWorld.hasState(BlockStatePredicate.forBlock(AtumBlocks.KHNUMITE_FACE.get()))).where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(AtumBlocks.KHNUMITE_BLOCK.get()))).where('~', BlockInWorld.hasState(BlockMaterialPredicate.forMaterial(Material.AIR))).build();
        }
        return this.stonewardenPattern;
    }
}
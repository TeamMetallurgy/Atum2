package com.teammetallurgy.atum.blocks.stone.khnumite;

import com.teammetallurgy.atum.entity.stone.StoneguardEntity;
import com.teammetallurgy.atum.entity.stone.StonewardenEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.pattern.BlockMaterialMatcher;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.BlockStateMatcher;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.OptionalDispenseBehavior;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.util.CachedBlockInfo;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;

public class KhnumiteFaceBlock extends HorizontalBlock implements IKhnumite {
    private BlockPattern stoneguardBasePattern;
    private BlockPattern stoneguardPattern;
    private BlockPattern stonewardenBasePattern;
    private BlockPattern stonewardenPattern;

    public KhnumiteFaceBlock() {
        super(Block.Properties.create(Material.ROCK, MaterialColor.CLAY).hardnessAndResistance(2.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1));
        this.setDefaultState(this.stateContainer.getBaseState().with(HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (oldState.getBlock() != state.getBlock()) {
            this.trySpawnStonemob(world, pos);
        }
    }

    private void trySpawnStonemob(World world, BlockPos pos) {
        BlockPattern.PatternHelper patternHelper = this.getStonewardenPattern().match(world, pos);

        if (patternHelper != null) {
            for (int x = 0; x < this.getStonewardenPattern().getPalmLength(); ++x) {
                for (int y = 0; y < this.getStonewardenPattern().getThumbLength(); ++y) {
                    BlockPos patternPos = patternHelper.translateOffset(x, y, 0).getPos();
                    if (world.getBlockState(patternPos).getBlock() instanceof IKhnumite) {
                        world.setBlockState(patternPos, Blocks.AIR.getDefaultState(), 2);
                    }
                }
            }

            BlockPos stonewardenPos = patternHelper.translateOffset(1, 2, 0).getPos();
            StonewardenEntity stonewarden = AtumEntities.STONEWARDEN_FRIENDLY.create(world);
            if (stonewarden != null) {
                stonewarden.setPlayerCreated(true);
                stonewarden.onInitialSpawn(world, world.getDifficultyForLocation(pos), SpawnReason.MOB_SUMMONED, null, null);
                stonewarden.setLocationAndAngles((double) stonewardenPos.getX() + 0.5D, (double) stonewardenPos.getY() + 0.05D, (double) stonewardenPos.getZ() + 0.5D, 0.0F, 0.0F);
                world.addEntity(stonewarden);

                for (ServerPlayerEntity playerMP : world.getEntitiesWithinAABB(ServerPlayerEntity.class, stonewarden.getBoundingBox().grow(5.0D))) {
                    CriteriaTriggers.SUMMONED_ENTITY.trigger(playerMP, stonewarden);
                }

                for (int x = 0; x < this.getStonewardenPattern().getPalmLength(); ++x) {
                    for (int y = 0; y < this.getStonewardenPattern().getThumbLength(); ++y) {
                        CachedBlockInfo worldState1 = patternHelper.translateOffset(x, y, 0);
                        world.notifyNeighborsOfStateChange(worldState1.getPos(), Blocks.AIR);
                    }
                }
            }
        } else {
            patternHelper = this.getStoneguardPattern().match(world, pos);

            if (patternHelper != null) {
                for (int x = 0; x < this.getStoneguardPattern().getPalmLength(); ++x) {
                    for (int y = 0; y < this.getStoneguardPattern().getThumbLength(); ++y) {
                        CachedBlockInfo worldState = patternHelper.translateOffset(x, y, 0);
                        if (world.getBlockState(worldState.getPos()).getBlock() instanceof IKhnumite) {
                            world.setBlockState(worldState.getPos(), Blocks.AIR.getDefaultState(), 2);
                        }
                    }
                }
                StoneguardEntity stoneguard = AtumEntities.STONEGUARD_FRIENDLY.create(world);
                if (stoneguard != null) {
                    stoneguard.setPlayerCreated(true);
                    stoneguard.onInitialSpawn(world, world.getDifficultyForLocation(pos), SpawnReason.MOB_SUMMONED, null, null);
                    BlockPos stoneguardPos = patternHelper.translateOffset(0, 2, 0).getPos();
                    stoneguard.setLocationAndAngles((double) stoneguardPos.getX() + 0.5D, (double) stoneguardPos.getY() + 0.05D, (double) stoneguardPos.getZ() + 0.5D, 0.0F, 0.0F);
                    world.addEntity(stoneguard);

                    for (ServerPlayerEntity playerMP : world.getEntitiesWithinAABB(ServerPlayerEntity.class, stoneguard.getBoundingBox().grow(5.0D))) {
                        CriteriaTriggers.SUMMONED_ENTITY.trigger(playerMP, stoneguard);
                    }

                    for (int x = 0; x < this.getStoneguardPattern().getPalmLength(); ++x) {
                        for (int y = 0; y < this.getStoneguardPattern().getThumbLength(); ++y) {
                            CachedBlockInfo worldState = patternHelper.translateOffset(x, y, 0);
                            world.notifyNeighborsOfStateChange(worldState.getPos(), Blocks.AIR);
                        }
                    }
                }
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
    }

    public static void addDispenserSupport() {
        DispenserBlock.registerDispenseBehavior(AtumBlocks.KHNUMITE_FACE.asItem(), new OptionalDispenseBehavior() {
            @Override
            @Nonnull
            protected ItemStack dispenseStack(@Nonnull IBlockSource source, @Nonnull ItemStack stack) {
                World world = source.getWorld();
                BlockPos pos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));
                KhnumiteFaceBlock khnumiteFace = (KhnumiteFaceBlock) AtumBlocks.KHNUMITE_FACE;

                if (world.isAirBlock(pos) && khnumiteFace.canDispenserPlace(world, pos)) {
                    if (!world.isRemote) {
                        world.setBlockState(pos, khnumiteFace.getDefaultState(), 3);
                    }
                    stack.shrink(1);
                    this.setSuccessful(true);
                } else {
                    this.setSuccessful(ArmorItem.func_226626_a_(source, stack));
                }
                return stack;
            }
        });
    }

    private boolean canDispenserPlace(World world, BlockPos pos) {
        return this.getStonewardenBasePattern().match(world, pos) != null || this.getStoneguardBasePattern().match(world, pos) != null;
    }

    private BlockPattern getStoneguardBasePattern() {
        if (this.stoneguardBasePattern == null) {
            this.stoneguardBasePattern = BlockPatternBuilder.start().aisle("   ", "~#~", " # ").where('#', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(AtumBlocks.KHNUMITE_BLOCK))).where('~', CachedBlockInfo.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
        }
        return this.stoneguardBasePattern;
    }

    private BlockPattern getStoneguardPattern() {
        if (this.stoneguardPattern == null) {
            this.stoneguardPattern = BlockPatternBuilder.start().aisle(" ^ ", "~#~", " # ").where('^', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(AtumBlocks.KHNUMITE_FACE))).where('#', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(AtumBlocks.KHNUMITE_BLOCK))).where('~', CachedBlockInfo.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
        }
        return this.stoneguardPattern;
    }

    private BlockPattern getStonewardenBasePattern() {
        if (this.stonewardenBasePattern == null) {
            this.stonewardenBasePattern = BlockPatternBuilder.start().aisle("~ ~", "###", "~#~").where('#', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(AtumBlocks.KHNUMITE_BLOCK))).where('~', CachedBlockInfo.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
        }
        return this.stonewardenBasePattern;
    }

    private BlockPattern getStonewardenPattern() {
        if (this.stonewardenPattern == null) {
            this.stonewardenPattern = BlockPatternBuilder.start().aisle("~^~", "###", "~#~").where('^', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(AtumBlocks.KHNUMITE_FACE))).where('#', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(AtumBlocks.KHNUMITE_BLOCK))).where('~', CachedBlockInfo.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
        }
        return this.stonewardenPattern;
    }
}
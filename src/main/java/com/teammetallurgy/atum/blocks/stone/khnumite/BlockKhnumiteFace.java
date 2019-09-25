package com.teammetallurgy.atum.blocks.stone.khnumite;

import com.teammetallurgy.atum.entity.stone.StoneguardEntity;
import com.teammetallurgy.atum.entity.stone.StonewardenEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.pattern.BlockMaterialMatcher;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.init.Bootstrap;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockKhnumiteFace extends BlockHorizontal implements IKhnumite {
    private BlockPattern stoneguardBasePattern;
    private BlockPattern stoneguardPattern;
    private BlockPattern stonewardenBasePattern;
    private BlockPattern stonewardenPattern;

    public BlockKhnumiteFace() {
        super(Material.ROCK, MapColor.CLAY);
        this.setDefaultState(this.blockState.getBaseState().with(FACING, Direction.NORTH));
        this.setHardness(2.0F);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, BlockState state) {
        super.onBlockAdded(world, pos, state);
        this.trySpawnStonemob(world, pos);
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
            StonewardenEntity stonewarden = new StonewardenEntity(world);
            stonewarden.setPlayerCreated(true);
            stonewarden.onInitialSpawn(world.getDifficultyForLocation(pos), null);
            stonewarden.setLocationAndAngles((double) stonewardenPos.getX() + 0.5D, (double) stonewardenPos.getY() + 0.05D, (double) stonewardenPos.getZ() + 0.5D, 0.0F, 0.0F);
            world.addEntity(stonewarden);

            for (ServerPlayerEntity playerMP : world.getEntitiesWithinAABB(ServerPlayerEntity.class, stonewarden.getBoundingBox().grow(5.0D))) {
                CriteriaTriggers.SUMMONED_ENTITY.trigger(playerMP, stonewarden);
            }

            for (int x = 0; x < this.getStonewardenPattern().getPalmLength(); ++x) {
                for (int y = 0; y < this.getStonewardenPattern().getThumbLength(); ++y) {
                    BlockWorldState worldState1 = patternHelper.translateOffset(x, y, 0);
                    world.notifyNeighborsRespectDebug(worldState1.getPos(), Blocks.AIR, false);
                }
            }
        } else {
            patternHelper = this.getStoneguardPattern().match(world, pos);

            if (patternHelper != null) {
                for (int x = 0; x < this.getStoneguardPattern().getPalmLength(); ++x) {
                    for (int y = 0; y < this.getStoneguardPattern().getThumbLength(); ++y) {
                        BlockWorldState worldState = patternHelper.translateOffset(x, y, 0);
                        if (world.getBlockState(worldState.getPos()).getBlock() instanceof IKhnumite) {
                            world.setBlockState(worldState.getPos(), Blocks.AIR.getDefaultState(), 2);
                        }
                    }
                }
                StoneguardEntity stoneguard = new StoneguardEntity(world);
                stoneguard.setPlayerCreated(true);
                stoneguard.onInitialSpawn(world.getDifficultyForLocation(pos), null);
                BlockPos stoneguardPos = patternHelper.translateOffset(0, 2, 0).getPos();
                stoneguard.setLocationAndAngles((double) stoneguardPos.getX() + 0.5D, (double) stoneguardPos.getY() + 0.05D, (double) stoneguardPos.getZ() + 0.5D, 0.0F, 0.0F);
                world.addEntity(stoneguard);

                for (ServerPlayerEntity playerMP : world.getEntitiesWithinAABB(ServerPlayerEntity.class, stoneguard.getBoundingBox().grow(5.0D))) {
                    CriteriaTriggers.SUMMONED_ENTITY.trigger(playerMP, stoneguard);
                }

                for (int x = 0; x < this.getStoneguardPattern().getPalmLength(); ++x) {
                    for (int y = 0; y < this.getStoneguardPattern().getThumbLength(); ++y) {
                        BlockWorldState worldState = patternHelper.translateOffset(x, y, 0);
                        world.notifyNeighborsRespectDebug(worldState.getPos(), Blocks.AIR, false);
                    }
                }
            }
        }
    }

    @Override
    @Nonnull
    public BlockState getStateForPlacement(World world, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer) {
        return this.getDefaultState().with(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    @Nonnull
    public BlockState withRotation(@Nonnull BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    @Nonnull
    public BlockState withMirror(@Nonnull BlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @Override
    public int getMetaFromState(BlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    @Nonnull
    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState().with(FACING, Direction.byHorizontalIndex(meta));
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    public static void addDispenerSupport() {
        DispenserBlock.registerDispenseBehavior(AtumBlocks.KHNUMITE_FACE.asItem(), new Bootstrap.BehaviorDispenseOptional() {
            @Override
            @Nonnull
            protected ItemStack dispenseStack(IBlockSource source, @Nonnull ItemStack stack) {
                World world = source.getWorld();
                BlockPos pos = source.getBlockPos().offset(source.getBlockState().getValue(BlockDispenser.FACING));
                BlockKhnumiteFace khnumiteFace = (BlockKhnumiteFace) AtumBlocks.KHNUMITE_FACE;
                this.successful = true;

                if (world.isAirBlock(pos) && khnumiteFace.canDispenserPlace(world, pos)) {
                    if (!world.isRemote) {
                        world.setBlockState(pos, khnumiteFace.getDefaultState(), 3);
                    }
                    stack.shrink(1);
                } else {
                    ItemStack itemstack = ItemArmor.dispenseArmor(source, stack);

                    if (itemstack.isEmpty()) {
                        this.successful = false;
                    }
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
            this.stoneguardBasePattern = FactoryBlockPattern.start().aisle("   ", "~#~", " # ").where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(AtumBlocks.KHNUMITE_BLOCK))).where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
        }
        return this.stoneguardBasePattern;
    }

    private BlockPattern getStoneguardPattern() {
        if (this.stoneguardPattern == null) {
            this.stoneguardPattern = FactoryBlockPattern.start().aisle(" ^ ", "~#~", " # ").where('^', BlockWorldState.hasState(BlockStateMatcher.forBlock(AtumBlocks.KHNUMITE_FACE))).where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(AtumBlocks.KHNUMITE_BLOCK))).where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
        }
        return this.stoneguardPattern;
    }

    private BlockPattern getStonewardenBasePattern() {
        if (this.stonewardenBasePattern == null) {
            this.stonewardenBasePattern = FactoryBlockPattern.start().aisle("~ ~", "###", "~#~").where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(AtumBlocks.KHNUMITE_BLOCK))).where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
        }
        return this.stonewardenBasePattern;
    }

    private BlockPattern getStonewardenPattern() {
        if (this.stonewardenPattern == null) {
            this.stonewardenPattern = FactoryBlockPattern.start().aisle("~^~", "###", "~#~").where('^', BlockWorldState.hasState(BlockStateMatcher.forBlock(AtumBlocks.KHNUMITE_FACE))).where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(AtumBlocks.KHNUMITE_BLOCK))).where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
        }
        return this.stonewardenPattern;
    }
}
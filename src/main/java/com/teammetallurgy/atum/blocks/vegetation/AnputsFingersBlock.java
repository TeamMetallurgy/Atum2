package com.teammetallurgy.atum.blocks.vegetation;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class AnputsFingersBlock extends CropBlock {
    private static final IntegerProperty ANPUTS_FINGERS_AGE = BlockStateProperties.AGE_3;
    private static final VoxelShape[] SHAPE = new VoxelShape[]{Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D, 1.0D), Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, 0.6875D, 1.0D), Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D)};
    private final HashMap<UUID, Integer> lastTouchedTick = new HashMap<>();

    public AnputsFingersBlock() {
        super(Properties.of(Material.PLANT, MaterialColor.COLOR_GRAY).randomTicks().noCollission().noOcclusion());
    }

    @Override
    @Nonnull
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPE[this.getAge(state)];
    }

    @Override
    @Nonnull
    protected ItemLike getBaseSeedId() {
        return AtumItems.ANPUTS_FINGERS_SPORES.get();
    }

    @Override
    @Nonnull
    public IntegerProperty getAgeProperty() {
        return ANPUTS_FINGERS_AGE;
    }

    @Override
    public int getMaxAge() {
        return 3;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos) {
        return state.getBlock() == AtumBlocks.SAND.get();
    }

    @Override
    public boolean canSurvive(@Nonnull BlockState state, LevelReader world, @Nonnull BlockPos pos) {
        BlockState stateDown = world.getBlockState(pos.below());
        return this.mayPlaceOn(stateDown, world, pos.below()) && world.getBrightness(LightLayer.SKY, pos) < 14;
    }

    @Override
    public void tick(@Nonnull BlockState state, @Nonnull ServerLevel world, @Nonnull BlockPos pos, @Nonnull Random rand) {
        int age = this.getAge(state);
        if (age < this.getMaxAge() && ForgeHooks.onCropsGrowPre(world, pos, state, rand.nextInt(8) == 0)) {
            BlockState newState = state.setValue(this.getAgeProperty(), age + 1);
            world.setBlock(pos, newState, 2);
            ForgeHooks.onCropsGrowPost(world, pos, state);
        }
    }

    @Override
    public void entityInside(@Nonnull BlockState state, Level world, @Nonnull BlockPos pos, @Nonnull Entity entity) {
        if (!world.isClientSide && entity instanceof Player player) {
            MinecraftServer server = world.getServer();
            Integer lastTouched = this.lastTouchedTick.get(player.getUUID());
            if (server != null) {
                if (lastTouched != null && server.getTickCount() - lastTouched < 35) return;
                if (player.getFoodData().getFoodLevel() > 0) {
                    player.getFoodData().eat(-1, -0.1F);
                    this.lastTouchedTick.put(player.getUUID(), server.getTickCount());
                }
            }
        }
    }

    @Override
    public boolean isValidBonemealTarget(@Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull BlockState state, boolean isClient) {
        return false;
    }

    @Override
    public boolean isBonemealSuccess(@Nonnull Level world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
        container.add(ANPUTS_FINGERS_AGE);
    }
}
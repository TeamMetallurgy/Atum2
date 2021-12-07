package com.teammetallurgy.atum.blocks.machines;

import com.teammetallurgy.atum.blocks.machines.tileentity.KilnBaseTileEntity;
import com.teammetallurgy.atum.blocks.machines.tileentity.KilnTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;

public class KilnFakeBlock extends BaseEntityBlock {
    public static final BooleanProperty UP = BooleanProperty.create("up");

    public KilnFakeBlock() {
        super(Properties.of(Material.STONE, MaterialColor.SAND).strength(1.5F, 10.0F).sound(SoundType.STONE));
        this.registerDefaultState(this.stateDefinition.any().setValue(UP, false));
    }

    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new KilnTileEntity(pos, state);
    }

    @Override
    @Nonnull
    public InteractionResult use(@Nonnull BlockState state, Level world, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult rayTraceResult) {
        if (world.isClientSide) {
            return InteractionResult.PASS;
        }
        BlockPos tepos = getPrimaryKilnBlock(world, pos);
        if (tepos != null) {
            MenuProvider container = this.getMenuProvider(world.getBlockState(tepos), world, tepos);
            if (container != null && player instanceof ServerPlayer) {
                NetworkHooks.openGui((ServerPlayer) player, container, tepos);
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(state, world, pos, player, hand, rayTraceResult);
    }

    @Override
    public void onRemove(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        BlockPos primaryPos = this.getPrimaryKilnBlock(world, pos);
        if (primaryPos != null) {
            BlockState primaryState = world.getBlockState(primaryPos);
            if (primaryState.getBlock() == AtumBlocks.KILN && primaryState.getValue(KilnBlock.MULTIBLOCK_PRIMARY)) {
                ((KilnBlock) AtumBlocks.KILN).destroyMultiblock(world, primaryPos, primaryState.getValue(KilnBlock.FACING));
            }
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }

    private BlockPos getPrimaryKilnBlock(Level world, BlockPos pos) {
        BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof KilnBaseTileEntity tekb) {
            return tekb.getPrimaryPos();
        }
        return null;
    }

    @Override
    @Nonnull
    public ItemStack getCloneItemStack(@Nonnull BlockGetter getter, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new ItemStack(AtumBlocks.KILN);
    }

    @Override
    @Nonnull
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
        container.add(UP);
    }
}
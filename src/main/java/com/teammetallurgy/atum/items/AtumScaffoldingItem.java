package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.wood.AtumScaffoldingBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ScaffoldingBlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class AtumScaffoldingItem extends ScaffoldingBlockItem {

    public AtumScaffoldingItem(Block block) {
        super(block, new Properties().tab(Atum.GROUP));
    }

    @Nullable
    public BlockPlaceContext updatePlacementContext(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        Level level = context.getLevel();
        BlockState blockstate = level.getBlockState(blockpos);
        Block block = this.getBlock();
        if (!blockstate.is(block)) {
            return AtumScaffoldingBlock.getDistance(level, blockpos) == 7 ? null : context; //Only line changed, to call different getDistance method
        } else {
            Direction direction;
            if (context.isSecondaryUseActive()) {
                direction = context.isInside() ? context.getClickedFace().getOpposite() : context.getClickedFace();
            } else {
                direction = context.getClickedFace() == Direction.UP ? context.getHorizontalDirection() : Direction.UP;
            }

            int i = 0;
            BlockPos.MutableBlockPos blockpos$mutable = blockpos.mutable().move(direction);

            while (i < 7) {
                if (!level.isClientSide && !level.isInWorldBounds(blockpos$mutable)) {
                    Player player = context.getPlayer();
                    int j = level.getMaxBuildHeight();
                    if (player instanceof ServerPlayer && blockpos$mutable.getY() >= j) {
                        ((ServerPlayer)player).sendSystemMessage(Component.translatable("build.tooHigh", j - 1).withStyle(ChatFormatting.RED), true);
                    }
                    break;
                }

                blockstate = level.getBlockState(blockpos$mutable);
                if (!blockstate.is(this.getBlock())) {
                    if (blockstate.canBeReplaced(context)) {
                        return BlockPlaceContext.at(context, blockpos$mutable, direction);
                    }
                    break;
                }

                blockpos$mutable.move(direction);
                if (direction.getAxis().isHorizontal()) {
                    ++i;
                }
            }
            return null;
        }
    }
}
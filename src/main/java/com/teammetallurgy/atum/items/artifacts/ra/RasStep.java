package com.teammetallurgy.atum.items.artifacts.ra;

import com.google.common.base.Objects;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.items.artifacts.RingItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.neoforge.common.util.BlockSnapshot;
import net.neoforged.neoforge.event.EventHooks;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nonnull;

public class RasStep extends RingItem implements IArtifact {
    private BlockPos prevBlockpos;

    public RasStep() {
        super();
    }

    @Override
    public God getGod() {
        return God.RA;
    }

    @Override
    public void curioTick(SlotContext slotContext, @Nonnull ItemStack stack) {
        super.curioTick(slotContext, stack);
        LivingEntity livingEntity = slotContext.entity();
        Level level = livingEntity.level();
        if (livingEntity.isAlive() && !level.isClientSide) {
            BlockPos pos = livingEntity.blockPosition();

            if (!Objects.equal(this.prevBlockpos, pos)) {
                this.prevBlockpos = pos;
                this.lavaWalk(livingEntity, level, pos);
            }
        }
    }

    private void lavaWalk(LivingEntity living, Level level, BlockPos pos) {
        if (living.onGround()) {
            BlockState raStone = AtumBlocks.RA_STONE.get().defaultBlockState();
            float area = (float) Math.min(16, 2);
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

            for (BlockPos posBox : BlockPos.betweenClosed(pos.offset((int) -area, (int) -1.0D, (int) -area), pos.offset((int) area, (int) -1.0D, (int) area))) {
                if (posBox.closerThan(living.blockPosition(), area)) {
                    mutablePos.set(posBox.getX(), posBox.getY() + 1, posBox.getZ());
                    BlockState state = level.getBlockState(mutablePos);
                    if (state.isAir()) {
                        BlockState checkState = level.getBlockState(posBox);
                        boolean isFull = checkState.getFluidState().is(FluidTags.LAVA) && checkState.hasProperty(LiquidBlock.LEVEL) && checkState.getValue(LiquidBlock.LEVEL) == 0;
                        if (checkState == Blocks.LAVA.defaultBlockState() && isFull && raStone.canSurvive(level, posBox) && level.isUnobstructed(raStone, posBox, CollisionContext.empty()) && !EventHooks.onBlockPlace(living, BlockSnapshot.create(level.dimension(), level, posBox), Direction.UP)) {
                            level.setBlockAndUpdate(posBox, raStone);
                            level.scheduleTick(posBox, raStone.getBlock(), Mth.nextInt(living.getRandom(), 60, 120));
                        }
                    }
                }
            }
        }
    }
}
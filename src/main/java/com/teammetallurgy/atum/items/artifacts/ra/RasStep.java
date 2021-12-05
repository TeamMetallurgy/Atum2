package com.teammetallurgy.atum.items.artifacts.ra;

import com.google.common.base.Objects;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.items.artifacts.RingItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;

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
    public void curioTick(String identifier, int index, LivingEntity livingEntity, @Nonnull ItemStack stack) {
        super.curioTick(identifier, index, livingEntity, stack);
        Level world = livingEntity.level;
        if (livingEntity.isAlive() && !world.isClientSide) {
            BlockPos pos = livingEntity.blockPosition();

            if (!Objects.equal(this.prevBlockpos, pos)) {
                this.prevBlockpos = pos;
                this.lavaWalk(livingEntity, world, pos);
            }
        }
    }

    private void lavaWalk(LivingEntity living, Level world, BlockPos pos) {
        if (living.isOnGround()) {
            BlockState raStone = AtumBlocks.RA_STONE.defaultBlockState();
            float area = (float) Math.min(16, 2);
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

            for (BlockPos posBox : BlockPos.betweenClosed(pos.offset(-area, -1.0D, -area), pos.offset(area, -1.0D, area))) {
                if (posBox.closerThan(living.position(), area)) {
                    mutablePos.set(posBox.getX(), posBox.getY() + 1, posBox.getZ());
                    BlockState state = world.getBlockState(mutablePos);
                    if (state.isAir(world, mutablePos)) {
                        BlockState checkState = world.getBlockState(posBox);
                        boolean isFull = checkState.getFluidState().is(FluidTags.LAVA) && checkState.hasProperty(LiquidBlock.LEVEL) && checkState.getValue(LiquidBlock.LEVEL) == 0;
                        if (checkState.getMaterial() == Material.LAVA && isFull && raStone.canSurvive(world, posBox) && world.isUnobstructed(raStone, posBox, CollisionContext.empty()) && !ForgeEventFactory.onBlockPlace(living, BlockSnapshot.create(world.dimension(), world, posBox), Direction.UP)) {
                            world.setBlockAndUpdate(posBox, raStone);
                            world.getBlockTicks().scheduleTick(posBox, raStone.getBlock(), Mth.nextInt(living.getRandom(), 60, 120));
                        }
                    }
                }
            }
        }
    }
}
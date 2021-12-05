package com.teammetallurgy.atum.items.artifacts.ra;

import com.google.common.base.Objects;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.items.artifacts.RingItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.World;
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
        World world = livingEntity.world;
        if (livingEntity.isAlive() && !world.isRemote) {
            BlockPos pos = livingEntity.getPosition();

            if (!Objects.equal(this.prevBlockpos, pos)) {
                this.prevBlockpos = pos;
                this.lavaWalk(livingEntity, world, pos);
            }
        }
    }

    private void lavaWalk(LivingEntity living, World world, BlockPos pos) {
        if (living.isOnGround()) {
            BlockState raStone = AtumBlocks.RA_STONE.getDefaultState();
            float area = (float) Math.min(16, 2);
            BlockPos.Mutable mutablePos = new BlockPos.Mutable();

            for (BlockPos posBox : BlockPos.getAllInBoxMutable(pos.add(-area, -1.0D, -area), pos.add(area, -1.0D, area))) {
                if (posBox.withinDistance(living.getPositionVec(), area)) {
                    mutablePos.setPos(posBox.getX(), posBox.getY() + 1, posBox.getZ());
                    BlockState state = world.getBlockState(mutablePos);
                    if (state.isAir(world, mutablePos)) {
                        BlockState checkState = world.getBlockState(posBox);
                        boolean isFull = checkState.getFluidState().isTagged(FluidTags.LAVA) && checkState.hasProperty(FlowingFluidBlock.LEVEL) && checkState.get(FlowingFluidBlock.LEVEL) == 0;
                        if (checkState.getMaterial() == Material.LAVA && isFull && raStone.isValidPosition(world, posBox) && world.placedBlockCollides(raStone, posBox, ISelectionContext.dummy()) && !ForgeEventFactory.onBlockPlace(living, BlockSnapshot.create(world.getDimensionKey(), world, posBox), Direction.UP)) {
                            world.setBlockState(posBox, raStone);
                            world.getPendingBlockTicks().scheduleTick(posBox, raStone.getBlock(), MathHelper.nextInt(living.getRNG(), 60, 120));
                        }
                    }
                }
            }
        }
    }
}
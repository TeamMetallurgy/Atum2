package com.teammetallurgy.atum.items.artifacts.ra;

import com.google.common.base.Objects;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
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

public class FeetOfRaItem extends RaArmor {
    private BlockPos prevBlockpos;

    public FeetOfRaItem() {
        super("ra_armor", EquipmentSlotType.FEET);
    }

    @Override
    public void onArmorTick(@Nonnull ItemStack stack, World world, PlayerEntity player) {
        super.onArmorTick(stack, world, player);
        if (player.isAlive() && !world.isRemote) {
            BlockPos pos = player.getPosition();

            if (!Objects.equal(this.prevBlockpos, pos)) {
                this.prevBlockpos = pos;
                this.lavaWalk(player, player.world, pos);
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
                        boolean isFull = checkState.getFluidState().isTagged(FluidTags.LAVA) && checkState.get(FlowingFluidBlock.LEVEL) == 0;
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
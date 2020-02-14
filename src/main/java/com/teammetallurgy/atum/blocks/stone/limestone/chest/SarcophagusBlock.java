package com.teammetallurgy.atum.blocks.stone.limestone.chest;

import com.teammetallurgy.atum.blocks.base.ChestBaseBlock;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.SarcophagusTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.*;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class SarcophagusBlock extends ChestBaseBlock {

    @Override
    public TileEntity createNewTileEntity(IBlockReader reader) {
        return new SarcophagusTileEntity();
    }

    @Override
    public float getBlockHardness(BlockState state, IBlockReader world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof SarcophagusTileEntity && !((SarcophagusTileEntity) tileEntity).isOpenable) {
            return -1.0F;
        } else {
            return 4.0F;
        }
    }

    @Override
    public float getExplosionResistance(BlockState state, IWorldReader world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof SarcophagusTileEntity && !((SarcophagusTileEntity) tileEntity).isOpenable) {
            return 6000000.0F;
        } else {
            return super.getExplosionResistance(state, world, pos, exploder, explosion);
        }
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        TileEntity tileEntity = world.getTileEntity(pos);
        Direction facing = state.get(FACING);

        //Right-Click left block, when right-clicking right block
        BlockPos posLeft = pos.offset(facing.rotateY());
        TileEntity tileLeft = world.getTileEntity(posLeft);
        if (world.getBlockState(posLeft).getBlock() == this && tileLeft instanceof SarcophagusTileEntity) {
            SarcophagusTileEntity sarcophagus = (SarcophagusTileEntity) tileLeft;
            if (!sarcophagus.hasSpawned) {
                this.onBlockActivated(state, world, pos.offset(facing.rotateY()), player, hand, hit);
                return false;
            }
        }

        if (tileEntity instanceof SarcophagusTileEntity) {
            SarcophagusTileEntity sarcophagus = (SarcophagusTileEntity) tileEntity;
            if (!sarcophagus.hasSpawned) {
                if (this.canSpawnPharaoh(world, pos, facing)) {
                    for (Direction horizontal : Direction.Plane.HORIZONTAL) {
                        TileEntity tileEntityOffset = world.getTileEntity(pos.offset(horizontal));
                        if (tileEntityOffset instanceof SarcophagusTileEntity) {
                            ((SarcophagusTileEntity) tileEntityOffset).hasSpawned = true;
                        }
                    }
                    sarcophagus.spawn(player, world.getDifficultyForLocation(pos));
                    sarcophagus.hasSpawned = true;
                    return false;
                } else if (!sarcophagus.isOpenable) {
                    player.sendStatusMessage(new TranslationTextComponent("chat.atum.cannotSpawnPharaoh").setStyle(new Style().setColor(TextFormatting.RED)), true);
                    world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ZOMBIE_INFECT, SoundCategory.HOSTILE, 0.7F, 0.4F, false);
                    return false;
                }
            }
        }
        return super.onBlockActivated(state, world, pos, player, hand, hit);
    }

    private boolean canSpawnPharaoh(World world, BlockPos pos, Direction facing) {
        boolean isTopLeftCorner = world.getBlockState(pos.offset(facing.rotateY(), 2).offset(facing.getOpposite(), 1)).getBlock() == AtumBlocks.PHARAOH_TORCH;
        boolean isBottomLeftCorner = world.getBlockState(pos.offset(facing.rotateY(), 2).offset(facing, 2)).getBlock() == AtumBlocks.PHARAOH_TORCH;
        boolean isTopRightCorner = world.getBlockState(pos.offset(facing.rotateYCCW(), 3).offset(facing.getOpposite(), 1)).getBlock() == AtumBlocks.PHARAOH_TORCH;
        boolean isBottomRightCorner = world.getBlockState(pos.offset(facing.rotateYCCW(), 3).offset(facing, 2)).getBlock() == AtumBlocks.PHARAOH_TORCH;
        return isTopLeftCorner && isBottomLeftCorner && isTopRightCorner && isBottomRightCorner;
    }

    @Override
    public void onBlockPlacedBy(@Nonnull World world, @Nonnull BlockPos pos, BlockState state, LivingEntity placer, @Nonnull ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof SarcophagusTileEntity) {
            SarcophagusTileEntity sarcophagus = (SarcophagusTileEntity) tileEntity;
            sarcophagus.hasSpawned = true;
            sarcophagus.setOpenable();
            sarcophagus.updateContainingBlockInfo();

            for (Direction horizontal : Direction.Plane.HORIZONTAL) {
                TileEntity tileEntityOffset = world.getTileEntity(pos.offset(horizontal));
                if (tileEntityOffset instanceof SarcophagusTileEntity) {
                    ((SarcophagusTileEntity) tileEntityOffset).hasSpawned = true;
                    ((SarcophagusTileEntity) tileEntityOffset).setOpenable();
                    tileEntityOffset.updateContainingBlockInfo();
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlaced(BlockEvent.EntityPlaceEvent event) { //Prevent placement, if right side of Sarcophagus would be next to another Sarcophagi block
        BlockState placedState = event.getPlacedBlock();
        if (placedState.getBlock() instanceof SarcophagusBlock) {
            if (!canPlaceRightSac(event.getWorld(), event.getPos(), placedState.get(FACING))) {
                event.setCanceled(true);
            }
        }
    }

    private static boolean canPlaceRightSac(IWorld world, BlockPos pos, Direction facing) {
        boolean right2 = world.getBlockState(pos.offset(facing.rotateYCCW(), 2)).getBlock() instanceof SarcophagusBlock;
        boolean up = world.getBlockState(pos.offset(facing.rotateYCCW()).offset(facing)).getBlock() instanceof SarcophagusBlock;
        boolean down = world.getBlockState(pos.offset(facing.rotateYCCW()).offset(facing.getOpposite())).getBlock() instanceof SarcophagusBlock;
        return !right2 && !up && !down;
    }

    @Nonnull
    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return new ItemStack(AtumBlocks.SARCOPHAGUS);
    }
}
package com.teammetallurgy.atum.blocks.stone.limestone.chest;

import com.teammetallurgy.atum.blocks.base.BlockChestBase;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.TileEntitySarcophagus;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class BlockSarcophagus extends BlockChestBase {

    public BlockSarcophagus() {
        this.setHardness(4.0F);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntitySarcophagus();
    }

    @Override
    public float getBlockHardness(BlockState state, World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntitySarcophagus && !((TileEntitySarcophagus) tileEntity).isOpenable) {
            return -1.0F;
        } else {
            return super.getBlockHardness(state, world, pos);
        }
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntitySarcophagus && !((TileEntitySarcophagus) tileEntity).isOpenable) {
            return 6000000.0F;
        } else {
            return super.getExplosionResistance(world, pos, exploder, explosion);
        }
    }

    @Override
    public boolean onBlockActivated(World world, @Nonnull BlockPos pos, BlockState state, @Nonnull PlayerEntity player, Hand hand, Direction side, float hitX, float hitY, float hitZ) {
        TileEntity tileEntity = world.getTileEntity(pos);
        Direction facing = state.getValue(FACING);

        //Right-Click left block, when right-clicking right block
        BlockPos posLeft = pos.offset(facing.rotateY());
        TileEntity tileLeft = world.getTileEntity(posLeft);
        if (world.getBlockState(posLeft).getBlock() == this && tileLeft instanceof TileEntitySarcophagus) {
            TileEntitySarcophagus sarcophagus = (TileEntitySarcophagus) tileLeft;
            if (!sarcophagus.hasSpawned) {
                this.onBlockActivated(world, pos.offset(facing.rotateY()), state, player, hand, side, hitX, hitY, hitZ);
                return false;
            }
        }

        if (tileEntity instanceof TileEntitySarcophagus) {
            TileEntitySarcophagus sarcophagus = (TileEntitySarcophagus) tileEntity;
            if (!sarcophagus.hasSpawned) {
                if (this.canSpawnPharaoh(world, pos, facing)) {
                    for (Direction horizontal : Direction.HORIZONTALS) {
                        TileEntity tileEntityOffset = world.getTileEntity(pos.offset(horizontal));
                        if (tileEntityOffset instanceof TileEntitySarcophagus) {
                            ((TileEntitySarcophagus) tileEntityOffset).hasSpawned = true;
                        }
                    }
                    sarcophagus.spawn(player, world.getDifficultyForLocation(pos));
                    sarcophagus.hasSpawned = true;
                    return false;
                } else if (!sarcophagus.isOpenable) {
                    player.sendStatusMessage(new TextComponentTranslation("chat.atum.cannotSpawnPharaoh").setStyle(new Style().setColor(TextFormatting.RED)), true);
                    world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ZOMBIE_INFECT, SoundCategory.HOSTILE, 0.7F, 0.4F, false);
                    return false;
                }
            }
        }
        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }

    private boolean canSpawnPharaoh(World world, BlockPos pos, Direction facing) {
        boolean isTopLeftCorner = world.getBlockState(pos.offset(facing.rotateY(), 2).offset(facing.getOpposite(), 1)).getBlock() == AtumBlocks.PHARAOH_TORCH;
        boolean isBottomLeftCorner = world.getBlockState(pos.offset(facing.rotateY(), 2).offset(facing, 2)).getBlock() == AtumBlocks.PHARAOH_TORCH;
        boolean isTopRightCorner = world.getBlockState(pos.offset(facing.rotateYCCW(), 3).offset(facing.getOpposite(), 1)).getBlock() == AtumBlocks.PHARAOH_TORCH;
        boolean isBottomRightCorner = world.getBlockState(pos.offset(facing.rotateYCCW(), 3).offset(facing, 2)).getBlock() == AtumBlocks.PHARAOH_TORCH;
        return isTopLeftCorner && isBottomLeftCorner && isTopRightCorner && isBottomRightCorner;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, @Nonnull ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof TileEntitySarcophagus) {
            TileEntitySarcophagus sarcophagus = (TileEntitySarcophagus) tileEntity;
            sarcophagus.hasSpawned = true;
            sarcophagus.setOpenable();
            sarcophagus.updateContainingBlockInfo();

            for (Direction horizontal : Direction.HORIZONTALS) {
                TileEntity tileEntityOffset = world.getTileEntity(pos.offset(horizontal));
                if (tileEntityOffset instanceof TileEntitySarcophagus) {
                    ((TileEntitySarcophagus) tileEntityOffset).hasSpawned = true;
                    ((TileEntitySarcophagus) tileEntityOffset).setOpenable();
                    tileEntityOffset.updateContainingBlockInfo();
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlaced(BlockEvent.PlaceEvent event) { //Prevent placement, if right side of Sarcophagus would be next to another Sarcophagi block
        BlockState placedState = event.getPlacedBlock();
        if (placedState.getBlock() instanceof BlockSarcophagus) {
            if (!canPlaceRightSac(event.getWorld(), event.getPos(), placedState.getValue(FACING))) {
                event.setCanceled(true);
            }
        }
    }

    private static boolean canPlaceRightSac(World world, BlockPos pos, Direction facing) {
        boolean right2 = world.getBlockState(pos.offset(facing.rotateYCCW(), 2)).getBlock() instanceof BlockSarcophagus;
        boolean up = world.getBlockState(pos.offset(facing.rotateYCCW()).offset(facing)).getBlock() instanceof BlockSarcophagus;
        boolean down = world.getBlockState(pos.offset(facing.rotateYCCW()).offset(facing.getOpposite())).getBlock() instanceof BlockSarcophagus;
        return !right2 && !up && !down;
    }

    @Nonnull
    @Override
    public ItemStack getPickBlock(@Nonnull BlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, PlayerEntity player) {
        return new ItemStack(AtumBlocks.SARCOPHAGUS);
    }

    @Override
    public void getDrops(@Nonnull NonNullList<ItemStack> drops, IBlockReader world, BlockPos pos, @Nonnull BlockState state, int fortune) {
        drops.add(new ItemStack(AtumBlocks.SARCOPHAGUS));
    }
}
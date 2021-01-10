package com.teammetallurgy.atum.blocks.stone.limestone.chest;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.QuandaryBlock;
import com.teammetallurgy.atum.blocks.base.ChestBaseBlock;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.SarcophagusTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.*;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class SarcophagusBlock extends ChestBaseBlock {

    public SarcophagusBlock() {
        super(() -> AtumTileEntities.SARCOPHAGUS, AbstractBlock.Properties.create(Material.ROCK, MaterialColor.SAND).hardnessAndResistance(4.0F));
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull IBlockReader reader) {
        return new SarcophagusTileEntity();
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        BlockState state = event.getState();
        if (state.getBlock() instanceof SarcophagusBlock) {
            TileEntity tileEntity = event.getWorld().getTileEntity(event.getPos());
            if (tileEntity instanceof SarcophagusTileEntity && !((SarcophagusTileEntity) tileEntity).isOpenable) {
                event.setCanceled(true);
            }
        }
    }

    @Override
    public float getExplosionResistance(BlockState state, IBlockReader world, BlockPos pos, Explosion explosion) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof SarcophagusTileEntity && !((SarcophagusTileEntity) tileEntity).isOpenable) {
            return 6000000.0F;
        } else {
            return super.getExplosionResistance(state, world, pos, explosion);
        }
    }

    @Override
    @Nonnull
    public ActionResultType onBlockActivated(BlockState state, World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull BlockRayTraceResult hit) {
        TileEntity tileEntity = world.getTileEntity(pos);
        Direction facing = state.get(FACING);

        //Right-Click left block, when right-clicking right block
        BlockPos posLeft = pos.offset(facing.rotateY());
        TileEntity tileLeft = world.getTileEntity(posLeft);
        if (world.getBlockState(posLeft).getBlock() == this && tileLeft instanceof SarcophagusTileEntity) {
            SarcophagusTileEntity sarcophagus = (SarcophagusTileEntity) tileLeft;
            if (world.getDifficulty() != Difficulty.PEACEFUL && !sarcophagus.hasSpawned) {
                this.onBlockActivated(state, world, pos.offset(facing.rotateY()), player, hand, hit);
                return ActionResultType.PASS;
            }
        }

        if (tileEntity instanceof SarcophagusTileEntity) {
            SarcophagusTileEntity sarcophagus = (SarcophagusTileEntity) tileEntity;
            if (world.getDifficulty() != Difficulty.PEACEFUL && !sarcophagus.hasSpawned) {
                if (QuandaryBlock.Helper.canSpawnPharaoh(world, pos, facing, player, sarcophagus)) {
                    return ActionResultType.PASS;
                } else if (!sarcophagus.isOpenable) {
                    player.sendStatusMessage(new TranslationTextComponent("chat.atum.cannot_spawn_pharaoh").mergeStyle(TextFormatting.RED), true);
                    world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ZOMBIE_INFECT, SoundCategory.HOSTILE, 0.7F, 0.4F, false);
                    return ActionResultType.PASS;
                }
            }
        }
        return super.onBlockActivated(state, world, pos, player, hand, hit);
    }

    @Nullable
    @Override
    public INamedContainerProvider getContainer(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos) { //Workaround so you can't see loot before pharaoh is beaten
        TileEntity tileEntity = world.getTileEntity(pos);
        return tileEntity instanceof SarcophagusTileEntity && ((SarcophagusTileEntity) tileEntity).isOpenable ? super.getContainer(state, world, pos) : null;
    }

    @Override
    public void onBlockPlacedBy(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull LivingEntity placer, @Nonnull ItemStack stack) {
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
    public static void onPlaced(BlockEvent.EntityPlaceEvent event) { //Prevent placement, 1 block left of another block
        BlockState placedState = event.getPlacedBlock();
        if (placedState.getBlock() instanceof SarcophagusBlock) {
            if (!canPlaceRightSac(event.getWorld(), event.getPos(), placedState.get(FACING))) {
                event.setCanceled(true);
            }
        }
    }

    private static boolean canPlaceRightSac(IWorld world, BlockPos pos, Direction facing) {
        BlockPos posOffset = pos.offset(facing.rotateYCCW());
        BlockState offsetState = world.getBlockState(posOffset);
        if (offsetState.getBlock() instanceof SarcophagusBlock) {
            return offsetState.get(SarcophagusBlock.TYPE) == ChestType.LEFT && offsetState.get(SarcophagusBlock.FACING) == facing;
        }
        return false;
    }

    @Nonnull
    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return new ItemStack(AtumBlocks.SARCOPHAGUS);
    }
}
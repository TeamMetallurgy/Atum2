package com.teammetallurgy.atum.blocks.stone.limestone.chest;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.QuandaryBlock;
import com.teammetallurgy.atum.blocks.base.ChestBaseBlock;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.SarcophagusTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumTileEntities;
import com.teammetallurgy.atum.network.NetworkHandler;
import com.teammetallurgy.atum.network.packet.SyncHandStackSizePacket;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class SarcophagusBlock extends ChestBaseBlock {

    public SarcophagusBlock() {
        super(AtumTileEntities.SARCOPHAGUS::get, BlockBehaviour.Properties.of(Material.STONE, MaterialColor.SAND).strength(4.0F));
    }

    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new SarcophagusTileEntity(pos, state);
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        BlockState state = event.getState();
        if (state.getBlock() instanceof SarcophagusBlock) {
            BlockEntity tileEntity = event.getWorld().getBlockEntity(event.getPos());
            if (tileEntity instanceof SarcophagusTileEntity && !((SarcophagusTileEntity) tileEntity).isOpenable) {
                event.setCanceled(true);
            }
        }
    }

    @Override
    public float getExplosionResistance(BlockState state, BlockGetter world, BlockPos pos, Explosion explosion) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof SarcophagusTileEntity && !((SarcophagusTileEntity) tileEntity).isOpenable) {
            return 6000000.0F;
        } else {
            return super.getExplosionResistance(state, world, pos, explosion);
        }
    }

    @Override
    @Nonnull
    public InteractionResult use(BlockState state, Level world, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        Direction facing = state.getValue(FACING);

        //Right-Click left block, when right-clicking right block
        BlockPos posLeft = pos.relative(facing.getClockWise());
        BlockEntity tileLeft = world.getBlockEntity(posLeft);
        if (world.getBlockState(posLeft).getBlock() == this && tileLeft instanceof SarcophagusTileEntity sarcophagus) {
            if (world.getDifficulty() != Difficulty.PEACEFUL && !sarcophagus.hasSpawned) {
                this.use(state, world, pos.relative(facing.getClockWise()), player, hand, hit);
                return InteractionResult.PASS;
            }
        }

        if (tileEntity instanceof SarcophagusTileEntity sarcophagus) {
            if (world.getDifficulty() != Difficulty.PEACEFUL && !sarcophagus.hasSpawned) {
                if (QuandaryBlock.Helper.canSpawnPharaoh(world, pos, facing, player, sarcophagus)) {
                    return InteractionResult.PASS;
                } else if (!sarcophagus.isOpenable) {
                    player.displayClientMessage(new TranslatableComponent("chat.atum.cannot_spawn_pharaoh").withStyle(ChatFormatting.RED), true);
                    world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ZOMBIE_INFECT, SoundSource.HOSTILE, 0.7F, 0.4F, false);
                    return InteractionResult.PASS;
                }
            }
        }
        return super.use(state, world, pos, player, hand, hit);
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos) { //Workaround so you can't see loot before pharaoh is beaten
        BlockEntity tileEntity = world.getBlockEntity(pos);
        return tileEntity instanceof SarcophagusTileEntity && ((SarcophagusTileEntity) tileEntity).isOpenable ? super.getMenuProvider(state, world, pos) : null;
    }

    @Override
    public void setPlacedBy(@Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull LivingEntity placer, @Nonnull ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);
        BlockEntity tileEntity = world.getBlockEntity(pos);

        if (tileEntity instanceof SarcophagusTileEntity sarcophagus) {
            sarcophagus.hasSpawned = true;
            sarcophagus.setOpenable();
            sarcophagus.setChanged();

            for (Direction horizontal : Direction.Plane.HORIZONTAL) {
                BlockEntity tileEntityOffset = world.getBlockEntity(pos.relative(horizontal));
                if (tileEntityOffset instanceof SarcophagusTileEntity) {
                    ((SarcophagusTileEntity) tileEntityOffset).hasSpawned = true;
                    ((SarcophagusTileEntity) tileEntityOffset).setOpenable();
                    tileEntityOffset.setChanged();
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlaced(BlockEvent.EntityPlaceEvent event) { //Prevent placement, 1 block left of another block
        BlockState placedState = event.getPlacedBlock();
        if (placedState.getBlock() instanceof SarcophagusBlock) {
            if (!canPlaceRightSac(event.getWorld(), event.getPos(), placedState.getValue(FACING))) {
                event.setCanceled(true);
                if (event.getEntity() instanceof ServerPlayer player) {
                    ItemStack placedStack = new ItemStack(placedState.getBlock().asItem());
                    InteractionHand hand = player.getMainHandItem().getItem() == placedStack.getItem() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
                    NetworkHandler.sendTo(player, new SyncHandStackSizePacket(placedStack, hand == InteractionHand.MAIN_HAND ? 1 : 0));
                }
            }
        }
    }

    private static boolean canPlaceRightSac(LevelAccessor world, BlockPos pos, Direction facing) {
        BlockPos posOffset = pos.relative(facing.getCounterClockWise());
        BlockState offsetState = world.getBlockState(posOffset);
        if (offsetState.getBlock() instanceof SarcophagusBlock) {
            return offsetState.getValue(SarcophagusBlock.TYPE) == ChestType.LEFT && offsetState.getValue(SarcophagusBlock.FACING) == facing;
        }
        return false;
    }

    @Override
    @Nonnull
    public ItemStack getCloneItemStack(@Nonnull BlockGetter getter, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new ItemStack(AtumBlocks.SARCOPHAGUS);
    }
}
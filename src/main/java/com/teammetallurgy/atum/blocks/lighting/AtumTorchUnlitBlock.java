package com.teammetallurgy.atum.blocks.lighting;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.misc.StackHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.Random;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class AtumTorchUnlitBlock extends AtumTorchBlock {

    public AtumTorchUnlitBlock() {
        super(0);
    }

    @Override
    @Nonnull
    public ActionResultType onBlockActivated(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, PlayerEntity player, @Nonnull Hand hand, @Nonnull BlockRayTraceResult rayTraceResult) {
        ItemStack heldStack = player.getHeldItem(hand);
        Block block = Block.getBlockFromItem(heldStack.getItem());
        if ((heldStack.getItem() instanceof FlintAndSteelItem || block.getLightValue(block.getDefaultState(), world, pos) > 0)) {
            if (heldStack.getItem().isDamageable()) {
                heldStack.damageItem(1, player, (p) -> p.sendBreakAnimation(hand));
            }
            world.setBlockState(pos, LIT.get(this).getDefaultState());
            world.playSound(null, pos, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 2.5F, 1.0F);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickBlock event) { //Light unlit held torch
        BlockState state = event.getWorld().getBlockState(event.getPos());
        if (Block.getBlockFromItem(event.getItemStack().getItem()) instanceof AtumTorchUnlitBlock && state.getBlock().getLightValue(state.getBlock().getDefaultState(), event.getWorld(), event.getPos()) > 0) {
            BlockPos pos = event.getPos();
            event.setCanceled(true); //Cancel placement
            event.getItemStack().shrink(1);
            StackHelper.giveItem(event.getPlayer(), event.getHand(), new ItemStack(LIT.get(Block.getBlockFromItem(event.getItemStack().getItem()))));
            event.getWorld().playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 10.0F, 1.0F, false);
        }
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Random random) {
    }
}
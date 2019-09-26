package com.teammetallurgy.atum.blocks.wood;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.utils.AtumRegistry;
import com.teammetallurgy.atum.utils.Constants;
import com.teammetallurgy.atum.utils.StackHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class BlockAtumTorchUnlit extends BlockAtumTorch {
    private static final List<BlockAtumTorch> TORCHES = Arrays.asList(AtumBlocks.PALM_TORCH, AtumBlocks.DEADWOOD_TORCH, AtumBlocks.LIMESTONE_TORCH, AtumBlocks.BONE_TORCH, AtumBlocks.PHARAOH_TORCH);
    private static final Map<Block, Block> UNLIT = Maps.newHashMap();
    private static final Map<Block, Block> LIT = Maps.newHashMap();

    private BlockAtumTorchUnlit() {
        super();
    }

    public static void registerUnlitTorches() {
        for (BlockAtumTorch torch : TORCHES) {
            BlockAtumTorchUnlit unlitTorch = new BlockAtumTorchUnlit();
            UNLIT.put(torch, unlitTorch);
            LIT.put(unlitTorch, torch);
            AtumRegistry.registerBlock(unlitTorch, Objects.requireNonNull(torch.getRegistryName()).getPath() + "_unlit", null);
        }
    }

    public static Block getUnlitTorch(Block torch) {
        return UNLIT.get(torch);
    }

    private static Block getLitTorch(Block torch) {
        return LIT.get(torch);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, BlockState state, PlayerEntity player, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
        ItemStack heldStack = player.getHeldItem(hand);
        Block block = Block.getBlockFromItem(heldStack.getItem());
        if ((heldStack.getItem() instanceof ItemFlintAndSteel || block.getLightValue(block.getDefaultState(), world, pos) > 0)) {
            if (heldStack.getItem().isDamageable()) {
                heldStack.damageItem(1, player);
            }
            world.setBlockState(pos, getLitTorch(this).getStateForPlacement(world, pos, state.getValue(BlockAtumTorch.FACING), hitX, hitY, hitZ, 0, player, hand));
            world.playSound(null, pos, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 2.5F, 1.0F);
            return true;
        }
        return false;
    }

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        BlockState state = event.getWorld().getBlockState(event.getPos());
        if (Block.getBlockFromItem(event.getItemStack().getItem()) instanceof BlockAtumTorchUnlit && state.getBlock().getLightValue(state.getBlock().getDefaultState(), event.getWorld(), event.getPos()) > 0) {
            BlockPos pos = event.getPos();
            event.setCanceled(true); //Cancel placement
            event.getItemStack().shrink(1);
            StackHelper.giveItem(event.getEntityPlayer(), event.getHand(), new ItemStack(getLitTorch(Block.getBlockFromItem(event.getItemStack().getItem()))));
            event.getWorld().playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 10.0F, 1.0F, false);
        }
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return 0;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random rand) {
    }
}
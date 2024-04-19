package com.teammetallurgy.atum.blocks.lighting;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.misc.StackHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.registries.DeferredBlock;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class AtumTorchUnlitBlock extends AtumTorchBlock {
    private final Supplier<? extends Block> lit;
    public static final Map<DeferredBlock<Block>, DeferredBlock<Block>> UNLIT = Maps.newHashMap();

    public AtumTorchUnlitBlock(Supplier<? extends Block> lit) {
        super(0);
        this.lit = lit;
    }

    public Supplier<? extends Block> getLit() {
        return this.lit;
    }

    @Override
    @Nonnull
    public InteractionResult use(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult rayTraceResult) {
        ItemStack heldStack = player.getItemInHand(hand);
        Block block = Block.byItem(heldStack.getItem());
        if ((heldStack.getItem() instanceof FlintAndSteelItem || block.getLightEmission(block.defaultBlockState(), level, pos) > 0)) {
            if (heldStack.getItem().canBeDepleted()) {
                heldStack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
            }
            level.setBlockAndUpdate(pos, this.getLit().get().defaultBlockState());
            level.playSound(null, pos, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 2.5F, 1.0F);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickBlock event) { //Light unlit held torch
        BlockState state = event.getLevel().getBlockState(event.getPos());
        Block block =  Block.byItem(event.getItemStack().getItem());
        if (block instanceof AtumTorchUnlitBlock unlit && state.getBlock().getLightEmission(state.getBlock().defaultBlockState(), event.getLevel(), event.getPos()) > 0) {
            BlockPos pos = event.getPos();
            event.setCanceled(true); //Cancel placement
            event.getItemStack().shrink(1);
            StackHelper.giveItem(event.getEntity(), event.getHand(), new ItemStack(unlit.getLit().get()));
            event.getLevel().playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 10.0F, 1.0F, false);
        }
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
    }
}
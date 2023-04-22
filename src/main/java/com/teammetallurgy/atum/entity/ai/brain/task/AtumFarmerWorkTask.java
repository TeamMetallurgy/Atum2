package com.teammetallurgy.atum.entity.ai.brain.task;

import com.google.common.collect.ImmutableList;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ai.behavior.WorkAtPoi;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public class AtumFarmerWorkTask extends WorkAtPoi {
    private static final List<Item> SEEDS = ImmutableList.of(AtumItems.EMMER_SEEDS.get(), AtumItems.FLAX_SEEDS.get());

    @Override
    protected void useWorkstation(@Nonnull ServerLevel level, Villager villager) {
        Optional<GlobalPos> optional = villager.getBrain().getMemory(MemoryModuleType.JOB_SITE);
        if (optional.isPresent()) {
            GlobalPos globalpos = optional.get();
            BlockState blockstate = level.getBlockState(globalpos.pos());
            if (blockstate.is(Blocks.COMPOSTER)) {
                this.bakeBread(villager);
                this.compost(level, villager, globalpos, blockstate);
            }
        }
    }

    private void compost(ServerLevel level, Villager villager, GlobalPos globalPos, BlockState state) {
        BlockPos pos = globalPos.pos();
        if (state.getValue(ComposterBlock.LEVEL) == 8) {
            state = ComposterBlock.extractProduce(villager, state, level, pos);
        }

        int i = 20;
        int j = 10;
        int[] aint = new int[SEEDS.size()];
        SimpleContainer inventory = villager.getInventory();
        int k = inventory.getContainerSize();
        BlockState blockstate = state;

        for (int l = k - 1; l >= 0 && i > 0; --l) {
            ItemStack itemstack = inventory.getItem(l);
            int i1 = SEEDS.indexOf(itemstack.getItem());
            if (i1 != -1) {
                int j1 = itemstack.getCount();
                int k1 = aint[i1] + j1;
                aint[i1] = k1;
                int l1 = Math.min(Math.min(k1 - 10, i), j1);
                if (l1 > 0) {
                    i -= l1;

                    for (int i2 = 0; i2 < l1; ++i2) {
                        blockstate = ComposterBlock.insertItem(villager, blockstate, level, itemstack, pos);
                        if (blockstate.getValue(ComposterBlock.LEVEL) == 7) {
                            this.spawnComposterFillEffects(level, state, pos, blockstate);
                            return;
                        }
                    }
                }
            }
        }
        this.spawnComposterFillEffects(level, state, pos, blockstate);
    }

    private void spawnComposterFillEffects(ServerLevel p_242308_1_, BlockState p_242308_2_, BlockPos p_242308_3_, BlockState p_242308_4_) {
        p_242308_1_.levelEvent(1500, p_242308_3_, p_242308_4_ != p_242308_2_ ? 1 : 0);
    }

    private void bakeBread(Villager villager) {
        SimpleContainer inventory = villager.getInventory();
        if (inventory.countItem(AtumItems.EMMER_BREAD.get()) <= 36) {
            int i = inventory.countItem(AtumItems.EMMER_EAR.get());
            int j = 3;
            int k = 3;
            int l = Math.min(3, i / 3);
            if (l != 0) {
                int i1 = l * 3;
                inventory.removeItemType(AtumItems.EMMER_EAR.get(), i1);
                ItemStack itemstack = inventory.addItem(new ItemStack(AtumItems.EMMER_BREAD.get(), l));
                if (!itemstack.isEmpty()) {
                    villager.spawnAtLocation(itemstack, 0.5F);
                }
            }
        }
    }
}
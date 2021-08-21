package com.teammetallurgy.atum.entity.ai.brain.task;

import com.google.common.collect.ImmutableList;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.SpawnGolemTask;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public class AtumFarmerWorkTask extends SpawnGolemTask {
    private static final List<Item> SEEDS = ImmutableList.of(AtumItems.EMMER_SEEDS, AtumItems.FLAX_SEEDS);

    @Override
    protected void execute(@Nonnull ServerWorld world, VillagerEntity villager) {
        Optional<GlobalPos> optional = villager.getBrain().getMemory(MemoryModuleType.JOB_SITE);
        if (optional.isPresent()) {
            GlobalPos globalpos = optional.get();
            BlockState blockstate = world.getBlockState(globalpos.getPos());
            if (blockstate.isIn(Blocks.COMPOSTER)) {
                this.bakeBread(villager);
                this.compost(world, villager, globalpos, blockstate);
            }
        }
    }

    private void compost(ServerWorld world, VillagerEntity villager, GlobalPos globalPos, BlockState state) {
        BlockPos pos = globalPos.getPos();
        if (state.get(ComposterBlock.LEVEL) == 8) {
            state = ComposterBlock.empty(state, world, pos);
        }

        int i = 20;
        int j = 10;
        int[] aint = new int[SEEDS.size()];
        Inventory inventory = villager.getVillagerInventory();
        int k = inventory.getSizeInventory();
        BlockState blockstate = state;

        for (int l = k - 1; l >= 0 && i > 0; --l) {
            ItemStack itemstack = inventory.getStackInSlot(l);
            int i1 = SEEDS.indexOf(itemstack.getItem());
            if (i1 != -1) {
                int j1 = itemstack.getCount();
                int k1 = aint[i1] + j1;
                aint[i1] = k1;
                int l1 = Math.min(Math.min(k1 - 10, i), j1);
                if (l1 > 0) {
                    i -= l1;

                    for (int i2 = 0; i2 < l1; ++i2) {
                        blockstate = ComposterBlock.attemptFill(blockstate, world, itemstack, pos);
                        if (blockstate.get(ComposterBlock.LEVEL) == 7) {
                            this.func_242308_a(world, state, pos, blockstate);
                            return;
                        }
                    }
                }
            }
        }
        this.func_242308_a(world, state, pos, blockstate);
    }

    private void func_242308_a(ServerWorld p_242308_1_, BlockState p_242308_2_, BlockPos p_242308_3_, BlockState p_242308_4_) {
        p_242308_1_.playEvent(1500, p_242308_3_, p_242308_4_ != p_242308_2_ ? 1 : 0);
    }

    private void bakeBread(VillagerEntity villager) {
        Inventory inventory = villager.getVillagerInventory();
        if (inventory.count(AtumItems.EMMER_BREAD) <= 36) {
            int i = inventory.count(AtumItems.EMMER_EAR);
            int j = 3;
            int k = 3;
            int l = Math.min(3, i / 3);
            if (l != 0) {
                int i1 = l * 3;
                inventory.func_223374_a(AtumItems.EMMER_EAR, i1);
                ItemStack itemstack = inventory.addItem(new ItemStack(AtumItems.EMMER_BREAD, l));
                if (!itemstack.isEmpty()) {
                    villager.entityDropItem(itemstack, 0.5F);
                }
            }
        }
    }
}
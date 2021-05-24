package com.teammetallurgy.atum.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.teammetallurgy.atum.blocks.vegetation.FertileSoilTilledBlock;
import com.teammetallurgy.atum.entity.villager.AtumVillagerEntity;
import com.teammetallurgy.atum.entity.villager.AtumVillagerProfession;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.memory.WalkTarget;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPosWrapper;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class AtumFarmTask extends Task<VillagerEntity> {
    @Nullable
    private BlockPos pos;
    private long taskCooldown;
    private int idleTime;
    private final List<BlockPos> farmableBlocks = Lists.newArrayList();

    public AtumFarmTask() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_ABSENT, MemoryModuleType.SECONDARY_JOB_SITE, MemoryModuleStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean shouldExecute(@Nonnull ServerWorld world, @Nonnull VillagerEntity owner) {
        if (!ForgeEventFactory.getMobGriefingEvent(world, owner) && !(owner instanceof AtumVillagerEntity)) {
            return false;
        } else if (((AtumVillagerEntity) owner).getAtumVillagerData().getAtumProfession() != AtumVillagerProfession.FARMER.get()) {
            return false;
        } else {
            BlockPos.Mutable mutablePos = owner.getPosition().toMutable();
            this.farmableBlocks.clear();

            for (int i = -1; i <= 1; ++i) {
                for (int j = -1; j <= 1; ++j) {
                    for (int k = -1; k <= 1; ++k) {
                        mutablePos.setPos(owner.getPosX() + (double) i, owner.getPosY() + (double) j, owner.getPosZ() + (double) k);
                        if (this.isValidPosForFarming(mutablePos, world)) {
                            this.farmableBlocks.add(new BlockPos(mutablePos));
                        }
                    }
                }
            }
            this.pos = this.getNextPosForFarming(world);
            return this.pos != null;
        }
    }

    @Nullable
    private BlockPos getNextPosForFarming(ServerWorld serverworld) {
        return this.farmableBlocks.isEmpty() ? null : this.farmableBlocks.get(serverworld.getRandom().nextInt(this.farmableBlocks.size()));
    }

    private boolean isValidPosForFarming(BlockPos pos, ServerWorld serverworld) {
        BlockState blockstate = serverworld.getBlockState(pos);
        Block block = blockstate.getBlock();
        Block block1 = serverworld.getBlockState(pos.down()).getBlock();
        return block instanceof CropsBlock && ((CropsBlock) block).isMaxAge(blockstate) || blockstate.isAir() && block1 instanceof FertileSoilTilledBlock;
    }

    @Override
    protected void startExecuting(@Nonnull ServerWorld world, @Nonnull VillagerEntity entity, long gameTime) {
        if (gameTime > this.taskCooldown && this.pos != null) {
            entity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosWrapper(this.pos));
            entity.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosWrapper(this.pos), 0.5F, 1));
        }
    }

    @Override
    protected void resetTask(@Nonnull ServerWorld world, VillagerEntity entity, long gameTime) {
        entity.getBrain().removeMemory(MemoryModuleType.LOOK_TARGET);
        entity.getBrain().removeMemory(MemoryModuleType.WALK_TARGET);
        this.idleTime = 0;
        this.taskCooldown = gameTime + 40L;
    }

    @Override
    protected void updateTask(@Nonnull ServerWorld world, @Nonnull VillagerEntity owner, long gameTime) {
        if (this.pos == null || this.pos.withinDistance(owner.getPositionVec(), 1.0D)) {
            if (this.pos != null && gameTime > this.taskCooldown) {
                BlockState blockstate = world.getBlockState(this.pos);
                Block block = blockstate.getBlock();
                Block block1 = world.getBlockState(this.pos.down()).getBlock();
                if (block instanceof CropsBlock && ((CropsBlock) block).isMaxAge(blockstate)) {
                    world.destroyBlock(this.pos, true, owner);
                }

                if (blockstate.isAir() && block1 instanceof FertileSoilTilledBlock && owner.isFarmItemInInventory()) {
                    Inventory inventory = owner.getVillagerInventory();

                    for (int i = 0; i < inventory.getSizeInventory(); ++i) {
                        ItemStack itemstack = inventory.getStackInSlot(i);
                        boolean flag = false;
                        if (!itemstack.isEmpty()) {
                            if (itemstack.getItem() == AtumItems.EMMER_SEEDS) {
                                world.setBlockState(this.pos, AtumBlocks.EMMER_WHEAT.getDefaultState(), 3);
                                flag = true;
                            } else if (itemstack.getItem() == AtumItems.FLAX_SEEDS) {
                                world.setBlockState(this.pos, AtumBlocks.FLAX.getDefaultState(), 3);
                                flag = true;
                            } else if (itemstack.getItem() == Items.POTATO) {
                                world.setBlockState(this.pos, Blocks.POTATOES.getDefaultState(), 3);
                                flag = true;
                            } else if (itemstack.getItem() == Items.CARROT) {
                                world.setBlockState(this.pos, Blocks.CARROTS.getDefaultState(), 3);
                                flag = true;
                            } else if (itemstack.getItem() == Items.BEETROOT_SEEDS) {
                                world.setBlockState(this.pos, Blocks.BEETROOTS.getDefaultState(), 3);
                                flag = true;
                            } else if (itemstack.getItem() instanceof net.minecraftforge.common.IPlantable) {
                                if (((net.minecraftforge.common.IPlantable) itemstack.getItem()).getPlantType(world, pos) == net.minecraftforge.common.PlantType.CROP) {
                                    world.setBlockState(pos, ((net.minecraftforge.common.IPlantable) itemstack.getItem()).getPlant(world, pos), 3);
                                    flag = true;
                                }
                            }
                        }

                        if (flag) {
                            world.playSound((PlayerEntity) null, (double) this.pos.getX(), (double) this.pos.getY(), (double) this.pos.getZ(), SoundEvents.ITEM_CROP_PLANT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                            itemstack.shrink(1);
                            if (itemstack.isEmpty()) {
                                inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                            }
                            break;
                        }
                    }
                }

                if (block instanceof CropsBlock && !((CropsBlock) block).isMaxAge(blockstate)) {
                    this.farmableBlocks.remove(this.pos);
                    this.pos = this.getNextPosForFarming(world);
                    if (this.pos != null) {
                        this.taskCooldown = gameTime + 20L;
                        owner.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosWrapper(this.pos), 0.5F, 1));
                        owner.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosWrapper(this.pos));
                    }
                }
            }

            ++this.idleTime;
        }
    }

    @Override
    protected boolean shouldContinueExecuting(@Nonnull ServerWorld world, @Nonnull VillagerEntity entity, long gameTimeIn) {
        return this.idleTime < 200;
    }
}
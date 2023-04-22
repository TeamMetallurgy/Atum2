package com.teammetallurgy.atum.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.teammetallurgy.atum.blocks.vegetation.FertileSoilTilledBlock;
import com.teammetallurgy.atum.entity.villager.AtumVillagerEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumVillagerProfession;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class AtumFarmTask extends Behavior<Villager> {
    @Nullable
    private BlockPos pos;
    private long taskCooldown;
    private int idleTime;
    private final List<BlockPos> farmableBlocks = Lists.newArrayList();

    public AtumFarmTask() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SECONDARY_JOB_SITE, MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(@Nonnull ServerLevel level, @Nonnull Villager owner) {
        if (!ForgeEventFactory.getMobGriefingEvent(level, owner) && !(owner instanceof AtumVillagerEntity)) {
            return false;
        } else if (((AtumVillagerEntity) owner).getAtumVillagerData().getAtumProfession() != AtumVillagerProfession.FARMER.get()) {
            return false;
        } else {
            BlockPos.MutableBlockPos mutablePos = owner.blockPosition().mutable();
            this.farmableBlocks.clear();

            for (int i = -1; i <= 1; ++i) {
                for (int j = -1; j <= 1; ++j) {
                    for (int k = -1; k <= 1; ++k) {
                        mutablePos.set(owner.getX() + (double) i, owner.getY() + (double) j, owner.getZ() + (double) k);
                        if (this.isValidPosForFarming(mutablePos, level)) {
                            this.farmableBlocks.add(new BlockPos(mutablePos));
                        }
                    }
                }
            }
            this.pos = this.getNextPosForFarming(level);
            return this.pos != null;
        }
    }

    @Nullable
    private BlockPos getNextPosForFarming(ServerLevel serverworld) {
        return this.farmableBlocks.isEmpty() ? null : this.farmableBlocks.get(serverworld.getRandom().nextInt(this.farmableBlocks.size()));
    }

    private boolean isValidPosForFarming(BlockPos pos, ServerLevel serverworld) {
        BlockState blockstate = serverworld.getBlockState(pos);
        Block block = blockstate.getBlock();
        Block block1 = serverworld.getBlockState(pos.below()).getBlock();
        return block instanceof CropBlock && ((CropBlock) block).isMaxAge(blockstate) || blockstate.isAir() && block1 instanceof FertileSoilTilledBlock;
    }

    @Override
    protected void start(@Nonnull ServerLevel level, @Nonnull Villager entity, long gameTime) {
        if (gameTime > this.taskCooldown && this.pos != null) {
            entity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(this.pos));
            entity.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosTracker(this.pos), 0.5F, 1));
        }
    }

    @Override
    protected void stop(@Nonnull ServerLevel level, Villager entity, long gameTime) {
        entity.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
        entity.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        this.idleTime = 0;
        this.taskCooldown = gameTime + 40L;
    }

    @Override
    protected void tick(@Nonnull ServerLevel level, @Nonnull Villager owner, long gameTime) {
        if (this.pos == null || this.pos.closerThan(owner.blockPosition(), 1.0D)) {
            if (this.pos != null && gameTime > this.taskCooldown) {
                BlockState blockstate = level.getBlockState(this.pos);
                Block block = blockstate.getBlock();
                Block block1 = level.getBlockState(this.pos.below()).getBlock();
                if (block instanceof CropBlock && ((CropBlock) block).isMaxAge(blockstate)) {
                    level.destroyBlock(this.pos, true, owner);
                }

                if (blockstate.isAir() && block1 instanceof FertileSoilTilledBlock && owner.hasFarmSeeds()) {
                    SimpleContainer inventory = owner.getInventory();

                    for (int i = 0; i < inventory.getContainerSize(); ++i) {
                        ItemStack itemstack = inventory.getItem(i);
                        boolean flag = false;
                        if (!itemstack.isEmpty()) {
                            if (itemstack.getItem() == AtumItems.EMMER_SEEDS.get()) {
                                level.setBlock(this.pos, AtumBlocks.EMMER_WHEAT.get().defaultBlockState(), 3);
                                flag = true;
                            } else if (itemstack.getItem() == AtumItems.FLAX_SEEDS.get()) {
                                level.setBlock(this.pos, AtumBlocks.FLAX.get().defaultBlockState(), 3);
                                flag = true;
                            } else if (itemstack.getItem() == Items.POTATO) {
                                level.setBlock(this.pos, Blocks.POTATOES.defaultBlockState(), 3);
                                flag = true;
                            } else if (itemstack.getItem() == Items.CARROT) {
                                level.setBlock(this.pos, Blocks.CARROTS.defaultBlockState(), 3);
                                flag = true;
                            } else if (itemstack.getItem() == Items.BEETROOT_SEEDS) {
                                level.setBlock(this.pos, Blocks.BEETROOTS.defaultBlockState(), 3);
                                flag = true;
                            } else if (itemstack.getItem() instanceof net.minecraftforge.common.IPlantable) {
                                if (((net.minecraftforge.common.IPlantable) itemstack.getItem()).getPlantType(level, pos) == net.minecraftforge.common.PlantType.CROP) {
                                    level.setBlock(pos, ((net.minecraftforge.common.IPlantable) itemstack.getItem()).getPlant(level, pos), 3);
                                    flag = true;
                                }
                            }
                        }

                        if (flag) {
                            level.playSound((Player) null, (double) this.pos.getX(), (double) this.pos.getY(), (double) this.pos.getZ(), SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1.0F, 1.0F);
                            itemstack.shrink(1);
                            if (itemstack.isEmpty()) {
                                inventory.setItem(i, ItemStack.EMPTY);
                            }
                            break;
                        }
                    }
                }

                if (block instanceof CropBlock && !((CropBlock) block).isMaxAge(blockstate)) {
                    this.farmableBlocks.remove(this.pos);
                    this.pos = this.getNextPosForFarming(level);
                    if (this.pos != null) {
                        this.taskCooldown = gameTime + 20L;
                        owner.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosTracker(this.pos), 0.5F, 1));
                        owner.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(this.pos));
                    }
                }
            }

            ++this.idleTime;
        }
    }

    @Override
    protected boolean canStillUse(@Nonnull ServerLevel level, @Nonnull Villager entity, long gameTimeIn) {
        return this.idleTime < 200;
    }
}
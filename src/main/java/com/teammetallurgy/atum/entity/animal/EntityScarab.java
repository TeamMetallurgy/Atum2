package com.teammetallurgy.atum.entity.animal;

import com.teammetallurgy.atum.blocks.stone.limestone.BlockLimestone;
import com.teammetallurgy.atum.blocks.wood.BlockDeadwood;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;

public class EntityScarab extends EntityMob {
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityScarab.class, DataSerializers.VARINT);
    private String texturePath;

    public EntityScarab(World world) {
        super(world);
        this.setSize(0.4F, 0.3F);
    }

    @Override
    protected int getExperiencePoints(EntityPlayer player) {
        if (this.getVariant() == 1) {
            return 30;
        } else {
            return 3;
        }
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(3, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(5, new AIHideInBlock(this));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);

        if (this.getVariant() == 1) {
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(24.0D);
            this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
        } else {
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
            this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
        }
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(VARIANT, 0);
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        if (world.rand.nextDouble() <= 0.002D) {
            this.setVariant(1);
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(24.0D);
            this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
            this.heal(16); //Make sure Golden scarab have have full health on initial spawn
        } else {
            this.setVariant(0);
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
            this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
        }
        return livingdata;
    }

    private void setVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
        this.texturePath = null;
    }

    private int getVariant() {
        return this.dataManager.get(VARIANT);
    }

    @SideOnly(Side.CLIENT)
    public String getTexture() {
        String entityName = Objects.requireNonNull(Objects.requireNonNull(EntityRegistry.getEntry(this.getClass())).getRegistryName()).getPath();
        if (this.texturePath == null) {
            if (this.getVariant() == 1) {
                this.texturePath = String.valueOf(new ResourceLocation(Constants.MOD_ID, "textures/entity/" + entityName + "_golden") + ".png");
            } else {
                this.texturePath = String.valueOf(new ResourceLocation(Constants.MOD_ID, "textures/entity/" + entityName) + ".png");
            }
        }
        return this.texturePath;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ENDERMITE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_ENDERMITE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ENDERMITE_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.ENTITY_ENDERMITE_STEP, 0.15F, 1.0F);
    }

    @Override
    protected ResourceLocation getLootTable() {
        if (this.getVariant() == 1) {
            return AtumLootTables.SCARAB_GOLDEN;
        } else {
            return AtumLootTables.SCARAB;
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.world.isRemote && this.dataManager.isDirty()) {
            this.dataManager.setClean();
            this.texturePath = null;
        }
    }

    @Override
    public float getBlockPathWeight(@Nonnull BlockPos pos) {
        return this.world.getBlockState(pos.down()).getBlock() == AtumBlocks.LIMESTONE ? 10.0F : super.getBlockPathWeight(pos);
    }

    @Override
    public float getEyeHeight() {
        return 0.1F;
    }

    @Override
    public double getYOffset() {
        return 0.1D;
    }

    @Override
    protected boolean isValidLightLevel() {
        return true;
    }

    @Override
    public boolean getCanSpawnHere() {
        if (super.getCanSpawnHere()) {
            EntityPlayer player = this.world.getClosestPlayerToEntity(this, 5.0D);
            return player == null;
        } else {
            return false;
        }
    }

    @Override
    @Nonnull
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variant", this.getVariant());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setVariant(compound.getInteger("Variant"));
    }

    static class AIHideInBlock extends EntityAIWander {
        private EnumFacing facing;
        private boolean doMerge;

        AIHideInBlock(EntityScarab scarab) {
            super(scarab, 1.0D, 10);
            this.setMutexBits(1);
        }

        @Override
        public boolean shouldExecute() {
            if (this.entity.getAttackTarget() != null) {
                return false;
            } else if (!this.entity.getNavigator().noPath()) {
                return false;
            } else {
                Random random = this.entity.getRNG();

                if (ForgeEventFactory.getMobGriefingEvent(this.entity.world, this.entity) && random.nextInt(10) == 0) {
                    this.facing = EnumFacing.random(random);
                    BlockPos pos = (new BlockPos(this.entity.posX, this.entity.posY + 0.5D, this.entity.posZ)).offset(this.facing);
                    IBlockState state = this.entity.world.getBlockState(pos);

                    if (state.getBlock() == AtumBlocks.LIMESTONE || state.getBlock() == AtumBlocks.DEADWOOD_LOG) {
                        this.doMerge = true;
                        return true;
                    }
                }
                this.doMerge = false;
                return super.shouldExecute();
            }
        }

        @Override
        public boolean shouldContinueExecuting() {
            return !this.doMerge && super.shouldContinueExecuting();
        }

        @Override
        public void startExecuting() {
            if (!this.doMerge) {
                super.startExecuting();
            } else {
                World world = this.entity.world;
                BlockPos pos = (new BlockPos(this.entity.posX, this.entity.posY + 0.5D, this.entity.posZ)).offset(this.facing);
                IBlockState state = world.getBlockState(pos);

                if (state.getBlock() == AtumBlocks.LIMESTONE) {
                    world.setBlockState(pos, AtumBlocks.LIMESTONE.getDefaultState().withProperty(BlockLimestone.HAS_SCARAB, true), 3);
                    this.entity.spawnExplosionParticle();
                    this.entity.setDead();
                }
                if (state.getBlock() == AtumBlocks.DEADWOOD_LOG) {
                    world.setBlockState(pos, AtumBlocks.DEADWOOD_LOG.getDefaultState().withProperty(BlockDeadwood.HAS_SCARAB, true), 3);
                    this.entity.spawnExplosionParticle();
                    this.entity.setDead();
                }
            }
        }
    }
}
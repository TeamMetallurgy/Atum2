package com.teammetallurgy.atum.entity.bandit;

import com.teammetallurgy.atum.entity.ITexture;
import com.teammetallurgy.atum.entity.animal.DesertWolfEntity;
import com.teammetallurgy.atum.entity.stone.StoneBaseEntity;
import com.teammetallurgy.atum.entity.undead.UndeadBaseEntity;
import com.teammetallurgy.atum.integration.champion.ChampionsHelper;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.PatrollerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;

public class BanditBaseEntity extends PatrollerEntity implements ITexture {
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(BanditBaseEntity.class, DataSerializers.VARINT);
    private String texturePath;
    private boolean canPatrol;

    BanditBaseEntity(EntityType<? extends BanditBaseEntity> entityType, World world) {
        super(entityType, world);
        new GroundPathNavigator(this, world).getEnterDoors();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(4, new BanditPatrolGoal<>(this, 0.7D, 0.595D)); //Only applies if spawned in a patrol
        this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new AvoidEntityGoal<>(this, DesertWolfEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.applyEntityAI();
    }

    protected void applyEntityAI() {
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this, BanditBaseEntity.class));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, UndeadBaseEntity.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, StoneBaseEntity.class, true));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30.0D);
    }

    @Override
    public boolean canAttack(EntityType<?> type) {
        return type != this.getType() && super.canAttack(type);
    }

    @Override
    protected void registerData() {
        super.registerData();
        if (this.hasSkinVariants()) {
            this.dataManager.register(VARIANT, 0);
        }
    }

    @Override
    @Nullable
    public ILivingEntityData onInitialSpawn(@Nonnull IWorld world, @Nonnull DifficultyInstance difficulty, @Nonnull SpawnReason spawnReason, @Nullable ILivingEntityData spawnData, @Nullable CompoundNBT nbt) {
        spawnData = mobInitialSpawn(spawnData);
        this.setEnchantmentBasedOnDifficulty(difficulty);
        this.setEquipmentBasedOnDifficulty(difficulty);
        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * difficulty.getClampedAdditionalDifficulty());

        if (this.isLeader()) {
            this.setItemStackToSlot(EquipmentSlotType.HEAD, createBanditBanner());
            this.setDropChance(EquipmentSlotType.HEAD, 1.5F);
        }

        if (spawnReason == SpawnReason.PATROL) {
            this.func_226541_s_(true);
        }

        if (this.hasSkinVariants()) {
            final int variant = MathHelper.nextInt(this.rand, 0, getVariantAmount());
            this.setVariant(variant);
        }
        return spawnData;
    }

    public ILivingEntityData mobInitialSpawn(@Nullable ILivingEntityData spawnData) {
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05D, AttributeModifier.Operation.MULTIPLY_BASE));
        if (this.rand.nextFloat() < 0.5F) {
            this.setLeftHanded(true);
        } else {
            this.setLeftHanded(false);
        }
        return spawnData;
    }

    public static ItemStack createBanditBanner() {
        ItemStack banner = new ItemStack(Items.WHITE_BANNER);
        CompoundNBT nbt = banner.getOrCreateChildTag("BlockEntityTag");
        ListNBT nbtList = new BannerPattern.Builder().func_222477_a(BannerPattern.BASE, DyeColor.WHITE).func_222477_a(BannerPattern.STRIPE_DOWNLEFT, DyeColor.GRAY)
                .func_222477_a(BannerPattern.STRIPE_DOWNRIGHT, DyeColor.GRAY).func_222477_a(BannerPattern.CROSS, DyeColor.RED)
                .func_222477_a(BannerPattern.FLOWER, DyeColor.BLACK).func_222477_a(BannerPattern.FLOWER, DyeColor.ORANGE)
                .func_222477_a(BannerPattern.CIRCLE_MIDDLE, DyeColor.BLACK).func_222477_a(BannerPattern.CIRCLE_MIDDLE, DyeColor.YELLOW)
                .func_222477_a(BannerPattern.SKULL, DyeColor.BLACK).func_222477_a(BannerPattern.SKULL, DyeColor.WHITE).func_222476_a();
        nbt.put("Patterns", nbtList);
        banner.setDisplayName(new TranslationTextComponent("block.atum.bandit_banner").applyTextStyle(TextFormatting.GOLD));
        return banner;
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        //Don't use for now, might do something with it later
    }

    int getVariantAmount() {
        return 6;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.world.isRemote && this.dataManager.isDirty()) {
            this.dataManager.setClean();
            this.texturePath = null;
        }
    }

    boolean hasSkinVariants() {
        return true;
    }

    private void setVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
        this.texturePath = null;
    }

    private int getVariant() {
        return this.dataManager.get(VARIANT);
    }

    protected boolean canPatrol() {
        return this.canPatrol;
    }

    public void setCanPatrol(boolean canPatrol) {
        this.canPatrol = canPatrol;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public String getTexture() {
        if (this.texturePath == null) {
            String entityName = Objects.requireNonNull(this.getType().getRegistryName()).getPath();

            if (ChampionsHelper.isChampion(this)) {
                ResourceLocation texture = ChampionsHelper.getTexture(this, entityName);
                if (texture != null) {
                    this.texturePath = texture.toString();
                    return this.texturePath;
                }
            }

            if (this.hasSkinVariants()) {
                this.texturePath = new ResourceLocation(Constants.MOD_ID, "textures/entity/" + entityName + "_" + this.getVariant()) + ".png";
            } else {
                this.texturePath = new ResourceLocation(Constants.MOD_ID, "textures/entity/" + entityName) + ".png";
            }
        }
        return this.texturePath;
    }

    @Override
    @Nonnull
    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.UNDEFINED;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_PILLAGER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ENTITY_PILLAGER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PILLAGER_DEATH;
    }

    public static boolean canSpawn(EntityType<? extends BanditBaseEntity> banditBase, IWorld world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return pos.getY() > 62 && world.canBlockSeeSky(pos) && world.getLightFor(LightType.BLOCK, pos) <= 8 && canMonsterSpawn(banditBase, world, spawnReason, pos, random);
    }

    @Override
    public boolean canDespawn(double distanceToPlayer) {
        return !this.canPatrol() || super.canDespawn(distanceToPlayer);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        if (this.hasSkinVariants()) {
            compound.putInt("Variant", this.getVariant());
        }
        compound.putBoolean("CanPatrol", this.canPatrol);
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (this.hasSkinVariants()) {
            this.setVariant(compound.getInt("Variant"));
        }
        this.canPatrol = compound.getBoolean("CanPatrol");
    }

    public static class BanditPatrolGoal<T extends BanditBaseEntity> extends PatrollerEntity.PatrolGoal<T> {
        private final T owner;

        public BanditPatrolGoal(T bandit, double d, double d1) {
            super(bandit, d, d1);
            this.owner = bandit;
        }

        @Override
        public boolean shouldExecute() {
            return this.owner.canPatrol() && super.shouldExecute();
        }
    }
}
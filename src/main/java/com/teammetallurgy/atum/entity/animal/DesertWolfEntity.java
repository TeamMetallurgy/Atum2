package com.teammetallurgy.atum.entity.animal;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.ai.goal.BegGoal;
import com.teammetallurgy.atum.entity.ai.goal.FollowOwnerWithoutSaddleGoal;
import com.teammetallurgy.atum.entity.ai.goal.SitWithCheckGoal;
import com.teammetallurgy.atum.entity.undead.UndeadBaseEntity;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.inventory.container.entity.AlphaDesertWolfContainer;
import com.teammetallurgy.atum.network.NetworkHandler;
import com.teammetallurgy.atum.network.packet.OpenWolfGuiPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.*;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class DesertWolfEntity extends TamableAnimal implements PlayerRideableJumping, ContainerListener, MenuProvider, NeutralMob {
    private static final EntityDataAccessor<Boolean> DATA_INTERESTED_ID = SynchedEntityData.defineId(DesertWolfEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_COLLAR_COLOR = SynchedEntityData.defineId(DesertWolfEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SADDLED = SynchedEntityData.defineId(DesertWolfEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<ItemStack> ARMOR_STACK = SynchedEntityData.defineId(DesertWolfEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(DesertWolfEntity.class, EntityDataSerializers.INT);
    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("0b3da7ef-52bf-47c9-9829-862ffa35b418");
    private String texturePath;
    private SimpleContainer desertWolfInventory;
    private float interestedAngle;
    private float interestedAngleO;
    private boolean isWet;
    private boolean isShaking;
    private float shakeAnim;
    private float shakeAnimO;
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(DesertWolfEntity.class, EntityDataSerializers.INT);
    private static final Attribute JUMP_STRENGTH = (new RangedAttribute("attribute.name.wolf.jump_strength", 0.7D, 0.0D, 2.0D)).setSyncable(true);
    private boolean isWolfJumping;
    private float jumpPower;
    private static long lastAlphaTime = 0;
    private UUID angryAt;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);

    public DesertWolfEntity(EntityType<? extends DesertWolfEntity> entityType, Level world) {
        super(entityType, world);
        this.xpReward = 6;
        this.maxUpStep = 1.1F;
        this.initInventory();
        this.setTame(false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SitWithCheckGoal(this, !this.isAlpha()));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, DesertWolfEntity.class, 8.0F, 0.6D, 1.0D, avoid -> avoid instanceof DesertWolfEntity && ((DesertWolfEntity) avoid).isAlpha() && (!this.isAlpha() && this.isTame())));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Llama.class, 24.0F, 0.6D, 1.2D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, CamelEntity.class, 24.0F, 0.6D, 1.2D, avoid -> avoid != null && !this.isAlpha()));
        this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(6, new FollowOwnerWithoutSaddleGoal(this, 1.0D, 10.0F, 2.0F));
        this.goalSelector.addGoal(7, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.4D));
        this.goalSelector.addGoal(9, new BegGoal(this, 8.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 0, false, false, target -> !this.isTame()));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, UndeadBaseEntity.class, 10, false, false, target -> !this.isTame()));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(4, new NonTameRandomTargetGoal<>(this, Animal.class, false, (entity) -> entity instanceof Sheep || entity instanceof Rabbit || entity instanceof Fox));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, AbstractSkeleton.class, 10, false, false, target -> !this.isTame()));
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@Nonnull ServerLevelAccessor world, @Nonnull DifficultyInstance difficulty, @Nonnull MobSpawnType spawnReason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag nbt) {
        livingdata = super.finalizeSpawn(world, difficulty, spawnReason, livingdata, nbt);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.getWolfMaxHealth());
        this.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(this.getWolfAttack());

        if (this.random.nextDouble() <= 0.25D && System.currentTimeMillis() > lastAlphaTime + 100) {
            this.setVariant(1);
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.getWolfMaxHealth());
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(this.getWolfAttack());
            this.setHealth(this.getWolfMaxHealth());
            this.xpReward = 12;
            lastAlphaTime = System.currentTimeMillis();
        } else {
            this.setVariant(0);
        }
        return livingdata;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0F).add(Attributes.MOVEMENT_SPEED, 0.4D).add(Attributes.ATTACK_DAMAGE, 8.0D).add(JUMP_STRENGTH);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_INTERESTED_ID, Boolean.FALSE);
        this.entityData.define(DATA_COLLAR_COLOR, DyeColor.GREEN.getId());
        this.entityData.define(VARIANT, 0);
        this.entityData.define(SADDLED, Boolean.FALSE);
        this.entityData.define(ARMOR_STACK, ItemStack.EMPTY);
        this.entityData.define(DATA_REMAINING_ANGER_TIME, 0);
    }

    public static boolean canSpawn(EntityType<? extends DesertWolfEntity> animal, LevelAccessor world, MobSpawnType spawnReason, BlockPos pos, Random random) {
        return pos.getY() > 62 && ((ServerLevel) world.getChunkSource().getLevel()).getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING) && world.canSeeSkyFromBelowWater(pos) && AtumEntities.canAnimalSpawn(animal, world, spawnReason, pos, random);
    }

    @Override
    protected void playStepSound(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        this.playSound(SoundEvents.WOLF_STEP, 0.15F, 1.0F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.isAngry()) {
            return SoundEvents.WOLF_GROWL;
        } else if (this.random.nextInt(3) == 0) {
            return this.isTame() && this.getHealth() < 10.0F ? SoundEvents.WOLF_WHINE : SoundEvents.WOLF_PANT;
        } else {
            return SoundEvents.WOLF_AMBIENT;
        }
    }

    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
        return SoundEvents.WOLF_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WOLF_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    @Nonnull
    protected ResourceLocation getDefaultLootTable() {
        if (isAlpha()) {
            return AtumLootTables.DESERT_WOLF_ALPHA;
        }
        return AtumLootTables.DESERT_WOLF;
    }

    private void cancelShake() {
        this.isShaking = false;
        this.shakeAnim = 0.0F;
        this.shakeAnimO = 0.0F;
    }

    @Override
    public void die(@Nonnull DamageSource cause) {
        this.isWet = false;
        this.isShaking = false;
        this.shakeAnimO = 0.0F;
        this.shakeAnim = 0.0F;
        if (!this.level.isClientSide && this.desertWolfInventory != null) {
            for (int i = 0; i < this.desertWolfInventory.getContainerSize(); ++i) {
                ItemStack slotStack = this.desertWolfInventory.getItem(i);
                if (!slotStack.isEmpty()) {
                    this.spawnAtLocation(slotStack, 0.0F);
                }
            }
        }
        super.die(cause);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level.isClientSide && this.isWet && !this.isShaking && !this.isPathFinding() && this.onGround) {
            this.isShaking = true;
            this.shakeAnim = 0.0F;
            this.shakeAnimO = 0.0F;
            this.level.broadcastEntityEvent(this, (byte) 8);
        }

        if (!this.level.isClientSide) {
            this.updatePersistentAnger((ServerLevel) this.level, true);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide && this.entityData.isDirty()) {
            this.entityData.clearDirty();
            this.texturePath = null;
        }

        if (this.isAlive()) {
            this.interestedAngleO = this.interestedAngle;
            if (this.isInterested()) {
                this.interestedAngle += (1.0F - this.interestedAngle) * 0.4F;
            } else {
                this.interestedAngle += (0.0F - this.interestedAngle) * 0.4F;
            }

            if (this.isInWaterRainOrBubble()) {
                this.isWet = true;
                if (this.isShaking && !this.level.isClientSide) {
                    this.level.broadcastEntityEvent(this, (byte) 56);
                    this.cancelShake();
                }
            } else if ((this.isWet || this.isShaking) && this.isShaking) {
                if (this.shakeAnim == 0.0F) {
                    this.playSound(SoundEvents.WOLF_SHAKE, this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                    this.gameEvent(GameEvent.WOLF_SHAKING);
                }

                this.shakeAnimO = this.shakeAnim;
                this.shakeAnim += 0.05F;
                if (this.shakeAnimO >= 2.0F) {
                    this.isWet = false;
                    this.isShaking = false;
                    this.shakeAnimO = 0.0F;
                    this.shakeAnim = 0.0F;
                }

                if (this.shakeAnim > 0.4F) {
                    float f = (float) this.getY();
                    int i = (int) (Mth.sin((this.shakeAnim - 0.4F) * (float) Math.PI) * 7.0F);
                    Vec3 vector3d = this.getDeltaMovement();

                    for (int j = 0; j < i; ++j) {
                        float f1 = (this.random.nextFloat() * 2.0F - 1.0F) * this.getBbWidth() * 0.5F;
                        float f2 = (this.random.nextFloat() * 2.0F - 1.0F) * this.getBbWidth() * 0.5F;
                        this.level.addParticle(ParticleTypes.SPLASH, this.getX() + (double) f1, (double) (f + 0.8F), this.getZ() + (double) f2, vector3d.x, vector3d.y, vector3d.z);
                    }
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public String getTexture() {
        if (this.texturePath == null) {
            this.texturePath = isAngry() ? "angry" : "tamed";
        }
        return this.texturePath;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isWolfWet() {
        return this.isWet;
    }

    @OnlyIn(Dist.CLIENT)
    public float getShadingWhileWet(float shading) {
        return 0.75F + (this.shakeAnimO + (this.shakeAnim - this.shakeAnimO) * shading) / 2.0F * 0.25F;
    }

    @OnlyIn(Dist.CLIENT)
    public float getShakeAngle(float p_70923_1_, float p_70923_2_) {
        float f = (this.shakeAnimO + (this.shakeAnim - this.shakeAnimO) * p_70923_1_ + p_70923_2_) / 1.8F;

        if (f < 0.0F) {
            f = 0.0F;
        } else if (f > 1.0F) {
            f = 1.0F;
        }
        return Mth.sin(f * (float) Math.PI) * Mth.sin(f * (float) Math.PI * 11.0F) * 0.15F * (float) Math.PI;
    }

    @OnlyIn(Dist.CLIENT)
    public float getInterestedAngle(float angle) {
        return (this.interestedAngleO + (this.interestedAngle - this.interestedAngleO) * angle) * 0.15F * (float) Math.PI;
    }

    @Override
    protected float getStandingEyeHeight(@Nonnull Pose pose, EntityDimensions size) {
        return size.height * 0.8F;
    }

    @Override
    public float getScale() {
        return this.isAlpha() ? 1.6F : super.getScale();
    }

    @Override
    public int getMaxHeadXRot() {
        return this.isOrderedToSit() ? 20 : super.getMaxHeadXRot();
    }

    @Override
    public boolean hurt(@Nonnull DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            Entity entity = source.getEntity();
            this.setOrderedToSit(false);

            if (entity != null && !(entity instanceof Player) && !(entity instanceof Arrow)) {
                amount = (amount + 1.0F) / 2.0F;
            }
            return super.hurt(source, amount);
        }
    }

    @Override
    public boolean doHurtTarget(@Nonnull Entity entity) {
        boolean shouldAttack = entity.hurt(DamageSource.mobAttack(this), (float) ((int) this.getAttributeValue(Attributes.ATTACK_DAMAGE)));

        if (shouldAttack) {
            this.doEnchantDamageEffects(this, entity);
        }
        return shouldAttack;
    }

    @Override
    public void setTame(boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            this.setRemainingPersistentAngerTime(0);
        }
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.getWolfMaxHealth());
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(this.getWolfAttack());
    }

    @Override
    @Nonnull
    public InteractionResult mobInteract(Player player, @Nonnull InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);
        Item item = heldStack.getItem();
        boolean tameItem = Tags.Items.BONES.contains(item) || item == Items.RABBIT || item == Items.COOKED_RABBIT;
        if (this.level.isClientSide) {
            boolean flag = this.isOwnedBy(player) || this.isTame() || tameItem && !this.isTame();
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else {
            if (this.isTame()) {
                if (this.isFood(heldStack) && this.getHealth() < this.getMaxHealth()) {
                    if (!player.getAbilities().instabuild) {
                        heldStack.shrink(1);
                    }

                    this.heal((float) item.getFoodProperties().getNutrition());
                    return InteractionResult.SUCCESS;
                }

                boolean holdsArmor = ArmorType.isArmor(heldStack);
                boolean holdsSaddle = !this.isBaby() && !this.isSaddled() && heldStack.getItem() instanceof SaddleItem;

                if (holdsArmor || holdsSaddle) {
                    this.openGUI(player);
                    return InteractionResult.SUCCESS;
                }

                if (!(item instanceof DyeItem)) {
                    if (!this.isBaby()) {
                        if (player.isCrouching()) {
                            this.openGUI(player);
                            return InteractionResult.SUCCESS;
                        }
                        if (this.isVehicle()) {
                            return super.mobInteract(player, hand);
                        }
                    }

                    InteractionResult resultType = super.mobInteract(player, hand);
                    if (this.isAlpha() && !this.isVehicle()) {
                        this.mountTo(player);
                        return InteractionResult.SUCCESS;
                    } else if (!this.isAlpha() && (!resultType.consumesAction() || this.isBaby()) && this.isOwnedBy(player)) { //Sit
                        this.setOrderedToSit(!this.isOrderedToSit());
                        this.jumping = false;
                        this.navigation.stop();
                        this.setTarget(null);
                        return InteractionResult.SUCCESS;
                    }
                    return resultType;
                }

                DyeColor color = ((DyeItem) item).getDyeColor();
                if (color != this.getCollarColor()) {
                    this.setCollarColor(color);
                    if (!player.getAbilities().instabuild) {
                        heldStack.shrink(1);
                    }
                    return InteractionResult.SUCCESS;
                }
            } else if (tameItem) {
                if (!player.getAbilities().instabuild) {
                    heldStack.shrink(1);
                }

                if (this.random.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
                    this.tame(player);
                    this.navigation.stop();
                    this.setTarget(null);
                    if (!this.isAlpha()) {
                        this.setOrderedToSit(true);
                    }
                    this.stopBeingAngry();
                    this.setHealth(40.0F);
                    this.level.broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level.broadcastEntityEvent(this, (byte) 6);
                }
                return InteractionResult.SUCCESS;
            }
            return super.mobInteract(player, hand);
        }
    }

    @Override
    public boolean isOrderedToSit() {
        if (this.isAlpha()) {
            return false;
        }
        return super.isOrderedToSit();
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() && this.isVehicle() && this.isSaddled();
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void openInventoryOverride(ScreenOpenEvent event) {
        if (event.getScreen() instanceof EffectRenderingInventoryScreen) {
            Player player = Minecraft.getInstance().player;
            if (player != null && player.getVehicle() instanceof DesertWolfEntity desertWolf) {
                if (player.getUUID() == player.getUUID()) {
                    if (desertWolf.isAlpha() && desertWolf.isVehicle()) {
                        event.setCanceled(true);
                        NetworkHandler.sendToServer(new OpenWolfGuiPacket(desertWolf.getId()));
                    }
                }
            }
        }
    }

    private void openGUI(Player player) {
        if (!this.level.isClientSide && (!this.isVehicle() || this.hasPassenger(player)) && this.isTame()) {
            if (player instanceof ServerPlayer) {
                NetworkHooks.openGui((ServerPlayer) player, this, buf -> buf.writeInt(this.getId()));
            }
        }
    }

    public Inventory getInventory() {
        return this.desertWolfInventory;
    }

    private void initInventory() {
        Container inventory = this.desertWolfInventory;
        this.desertWolfInventory = new Inventory(2);

        if (inventory != null) {
            inventory.removeListener(this);
            for (int j = 0; j < 2; ++j) {
                ItemStack slotStack = inventory.getItem(j);
                if (!slotStack.isEmpty()) {
                    this.desertWolfInventory.setItem(j, slotStack.copy());
                }
            }
        }
        this.desertWolfInventory.addListener(this);
        this.updateSlots();
        this.itemHandler = LazyOptional.of(() -> new InvWrapper(this.desertWolfInventory));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte id) {
        if (id == 8) {
            this.isShaking = true;
            this.shakeAnim = 0.0F;
            this.shakeAnimO = 0.0F;
        } else if (id == 56) {
            this.cancelShake();
        } else {
            super.handleEntityEvent(id);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public float getTailRotation() {
        if (this.isAngry()) {
            return 1.5393804F;
        } else if (!this.isVehicle()) {
            return this.isTame() ? (0.55F - (this.getMaxHealth() - this.getHealth()) * 0.02F) * (float) Math.PI : ((float) Math.PI / 5F);
        } else {
            return 1.5F;
        }
    }

    @Override
    public boolean isFood(@Nonnull ItemStack stack) {
        Item item = stack.getItem();
        return item.isEdible() && item.getFoodProperties() != null && item.getFoodProperties().isMeat();
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 6;
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(DATA_REMAINING_ANGER_TIME);
    }

    @Override
    public void setRemainingPersistentAngerTime(int time) {
        if (!this.isTame()) {
            this.entityData.set(DATA_REMAINING_ANGER_TIME, time);
        }
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    @Nullable
    public UUID getPersistentAngerTarget() {
        return this.angryAt;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID target) {
        this.angryAt = target;
    }

    @Override
    public boolean isAngry() {
        return !this.isTame() || this.getRemainingPersistentAngerTime() > 0;
    }

    public DyeColor getCollarColor() {
        return DyeColor.byId(this.entityData.get(DATA_COLLAR_COLOR));
    }

    private void setCollarColor(DyeColor color) {
        this.entityData.set(DATA_COLLAR_COLOR, color.getId());
    }

    public boolean isSaddled() {
        return this.entityData.get(SADDLED);
    }

    private void setSaddled(boolean saddled) {
        if (saddled) {
            this.entityData.set(SADDLED, Boolean.TRUE);
        } else {
            this.entityData.set(SADDLED, Boolean.FALSE);
        }
    }

    @Override
    public DesertWolfEntity getBreedOffspring(@Nonnull ServerLevel level, @Nonnull AgeableMob ageable) {
        DesertWolfEntity desertWolf = AtumEntities.DESERT_WOLF.create(this.level);
        if (desertWolf != null) {
            UUID uuid = this.getOwnerUUID();

            if (uuid != null) {
                desertWolf.setOwnerUUID(uuid);
                desertWolf.setTame(true);
                desertWolf.stopBeingAngry();
                desertWolf.heal(8.0F);
            }
            return desertWolf;
        }
        return null;
    }

    public void setDATA_INTERESTED_ID(boolean beg) {
        this.entityData.set(DATA_INTERESTED_ID, beg);
    }

    private boolean isInterested() {
        return this.entityData.get(DATA_INTERESTED_ID);
    }

    @Override
    public boolean canMate(@Nonnull Animal otherAnimal) {
        if (otherAnimal == this) {
            return false;
        } else if (!this.isTame()) {
            return false;
        } else if (!(otherAnimal instanceof DesertWolfEntity)) {
            return false;
        } else {
            DesertWolfEntity desertWolf = (DesertWolfEntity) otherAnimal;

            if (!desertWolf.isTame()) {
                return false;
            } else if (desertWolf.isOrderedToSit()) {
                return false;
            } else {
                return this.isInLove() && desertWolf.isInLove();
            }
        }
    }

    @Override
    public boolean wantsToAttack(@Nonnull LivingEntity target, @Nonnull LivingEntity owner) {
        if (!(target instanceof Creeper) && !(target instanceof Ghast)) {
            if (target instanceof DesertWolfEntity desertWolf) {
                if (desertWolf.isTame() && desertWolf.getOwner() == owner) {
                    return false;
                }
            }
            if (target instanceof Player && owner instanceof Player && !((Player) owner).canHarmPlayer((Player) target)) {
                return false;
            } else if (target instanceof AbstractHorse && ((AbstractHorse) target).isTamed()) {
                return false;
            } else {
                return !(target instanceof Cat) || !((Cat) target).isTame();
            }
        } else {
            return false;
        }
    }

    @SubscribeEvent
    public void onTarget(LivingSetAttackTargetEvent event) {
        if (event.getTarget() instanceof DesertWolfEntity && event.getEntityLiving() instanceof DesertWolfEntity) {
            if (((DesertWolfEntity) event.getTarget()).isTame() && ((DesertWolfEntity) event.getEntityLiving()).isTame()) {
                ((DesertWolfEntity) event.getEntityLiving()).setTarget(null);
            }
        }
    }

    @Override
    public boolean canBeLeashed(@Nonnull Player player) {
        return !this.isAngry() && super.canBeLeashed(player);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Variant", this.getVariant());
        super.addAdditionalSaveData(tag);
        tag.putByte("CollarColor", (byte) this.getCollarColor().getId());
        tag.putBoolean("Saddle", this.isSaddled());
        this.addPersistentAngerSaveData(tag);
        if (!this.desertWolfInventory.getItem(0).isEmpty()) {
            tag.put("SaddleItem", this.desertWolfInventory.getItem(0).save(new CompoundTag()));
        }
        if (!this.desertWolfInventory.getItem(1).isEmpty()) {
            tag.put("ArmorItem", this.desertWolfInventory.getItem(1).save(new CompoundTag()));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        this.setVariant(tag.getInt("Variant"));
        super.readAdditionalSaveData(tag);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.getWolfMaxHealth());
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(this.getWolfAttack());
        this.setSaddled(tag.getBoolean("Saddle"));
        this.readPersistentAngerSaveData(this.level, tag);

        if (tag.contains("CollarColor", 99)) {
            this.setCollarColor(DyeColor.byId(tag.getInt("CollarColor")));
        }
        if (tag.contains("SaddleItem", 10)) {
            ItemStack saddleStack = ItemStack.of(tag.getCompound("SaddleItem"));
            if (saddleStack.getItem() instanceof SaddleItem) {
                this.desertWolfInventory.setItem(0, saddleStack);
            }
        }
        if (tag.contains("ArmorItem", 10)) {
            ItemStack armorStack = ItemStack.of(tag.getCompound("ArmorItem"));
            if (!armorStack.isEmpty() && isArmor(armorStack)) {
                this.desertWolfInventory.setItem(1, armorStack);
            }
        }
        this.updateSlots();
    }

    private void updateSlots() {
        this.setArmorStack(this.desertWolfInventory.getItem(1));
        if (!this.level.isClientSide) {
            this.setSaddled(!this.desertWolfInventory.getItem(0).isEmpty());
        }
    }

    private void setArmorStack(@Nonnull ItemStack stack) {
        ArmorType armorType = ArmorType.getByItemStack(stack);
        this.entityData.set(ARMOR_STACK, stack);

        if (!this.level.isClientSide) {
            AttributeInstance armor = this.getAttribute(Attributes.ARMOR);
            if (armor != null) {
                armor.removeModifier(ARMOR_MODIFIER_UUID);
                int protection = armorType.getProtection();
                if (protection != 0) {
                    armor.addTransientModifier((new AttributeModifier(ARMOR_MODIFIER_UUID, "Desert wolf armor bonus", protection, AttributeModifier.Operation.ADDITION)));
                }
            }
        }
    }

    @Nonnull
    public ItemStack getArmor() {
        return this.entityData.get(ARMOR_STACK);
    }

    @Override
    public void containerChanged(@Nonnull Container invBasic) {
        this.updateSlots();
    }

    @Override
    public boolean setSlot(int inventorySlot, @Nonnull ItemStack stack) {
        int slot = inventorySlot - 400;
        if (slot >= 0 && slot < 2 && slot < this.desertWolfInventory.getContainerSize()) {
            if (slot == 0 && !(stack.getItem() instanceof SaddleItem)) {
                return false;
            } else if (slot != 1 || this.isArmor(stack)) {
                this.desertWolfInventory.setItem(slot, stack);
                this.updateSlots();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
        this.texturePath = null;
    }

    private int getVariant() {
        return this.entityData.get(VARIANT);
    }

    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @Override
    public boolean canBeControlledByRider() {
        return true;
    }

    @Override
    public double getPassengersRidingOffset() {
        return super.getPassengersRidingOffset() + 0.07D;
    }

    @Override
    public void positionRider(@Nonnull Entity passenger) {
        if (this.hasPassenger(passenger)) {
            float cos = Mth.cos(this.yBodyRot * 0.017453292F);
            float sin = Mth.sin(this.yBodyRot * 0.017453292F);
            passenger.setPos(this.getX() + (double) (0.4F * sin), this.getY() + this.getPassengersRidingOffset() + passenger.getMyRidingOffset(), this.getZ() - (double) (0.4F * cos));
        }
    }

    private void mountTo(Player player) {
        player.setYRot(this.getYRot());
        player.setXRot(this.getXRot());
        if (!this.level.isClientSide) {
            player.startRiding(this);
        }
    }

    @Override
    public void travel(@Nonnull Vec3 travelVec) {
        if (this.isAlive()) {
            if (this.isVehicle() && this.canBeControlledByRider() && this.isSaddled()) {
                LivingEntity livingBase = (LivingEntity) this.getControllingPassenger();
                if (livingBase != null) {
                    this.setYRot(livingBase.getYRot());
                    this.yRotO = this.getYRot();
                    this.setXRot(livingBase.getXRot() * 0.5F);
                    this.setRot(this.getYRot(), this.getXRot());
                    this.yBodyRot = this.getYRot();
                    this.yHeadRot = this.yBodyRot;
                    float strafe = livingBase.xxa * 0.5F;
                    float forward = livingBase.zza;

                    if (forward <= 0.0F) {
                        forward *= 0.25F;
                    }

                    if (this.jumpPower > 0.0F && !this.isJumping() && this.onGround) {
                        double wolfJumpStrength = this.getWolfJumpStrength() * (double) this.jumpPower;
                        double jumpY;

                        if (this.hasEffect(MobEffects.JUMP)) {
                            MobEffectInstance jumpBoost = this.getEffect(MobEffects.JUMP);
                            jumpY = wolfJumpStrength + (jumpBoost != null ? (double) ((float) (jumpBoost.getAmplifier() + 1) * 0.1F) : 0);
                        } else {
                            jumpY = wolfJumpStrength;
                        }

                        Vec3 motion = this.getDeltaMovement();
                        this.setDeltaMovement(motion.x, jumpY, motion.z);
                        this.setWolfJumping(true);
                        this.hasImpulse = true;

                        if (forward > 0.0F) {
                            float f2 = Mth.sin(this.getYRot() * ((float) Math.PI / 180F));
                            float f3 = Mth.cos(this.getYRot() * ((float) Math.PI / 180F));
                            this.setDeltaMovement(this.getDeltaMovement().add(-0.4F * f2 * this.jumpPower, 0.0D, 0.4F * f3 * this.jumpPower));
                            this.playSound(SoundEvents.HORSE_JUMP, 0.4F, 1.0F);
                        }
                        this.jumpPower = 0.0F;
                    }
                    this.flyingSpeed = this.getSpeed() * 0.1F;

                    if (this.isControlledByLocalInstance()) {
                        this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.80F);
                        super.travel(new Vec3(strafe, travelVec.y, forward));
                    } else if (livingBase instanceof Player) {
                        this.setDeltaMovement(Vec3.ZERO);
                    }

                    if (this.onGround) {
                        this.jumpPower = 0.0F;
                        this.setWolfJumping(false);
                    }
                }
            } else {
                this.flyingSpeed = 0.02F;
                super.travel(travelVec);
            }
        }
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, @Nonnull DamageSource source) {
        if (this.isAlpha() && distance > 5.0F) {
            return super.causeFallDamage(distance, damageMultiplier, source);
        } else if (!this.isAlpha() && distance > 2.5F) {
            return super.causeFallDamage(distance, damageMultiplier, source);
        }
        return false;
    }

    private boolean isJumping() {
        return this.isWolfJumping;
    }

    private void setWolfJumping(boolean jumping) {
        this.isWolfJumping = jumping;
    }

    private double getWolfJumpStrength() {
        return this.getAttributeValue(JUMP_STRENGTH);
    }

    @OnlyIn(Dist.CLIENT)
    public void onPlayerJump(int jumpPower) {
        if (this.isAlpha()) {
            if (jumpPower < 0) {
                jumpPower = 0;
            }

            if (jumpPower >= 90) {
                this.jumpPower = 1.0F;
            } else {
                this.jumpPower = 0.4F + 0.4F * (float) jumpPower / 90.0F;
            }
        }
    }

    public boolean canJump() {
        return this.isSaddled();
    }

    public void handleStartJump(int jumpPower) {
    }

    public void handleStopJump() {
    }

    public boolean isAlpha() {
        return this.getVariant() == 1;
    }

    private float getWolfMaxHealth() {
        if (this.isTame()) {
            if (this.isAlpha()) {
                return 36;
            } else {
                return 20;
            }
        } else {
            if (this.isAlpha()) {
                return 24;
            } else {
                return 12;
            }
        }
    }

    private double getWolfAttack() {
        if (this.isAlpha()) {
            return 8.0;
        } else {
            return 4.0;
        }
    }

    public boolean isArmor(@Nonnull ItemStack stack) {
        return ArmorType.isArmor(stack);
    }

    private LazyOptional<?> itemHandler = null;

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (this.isAlive() && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && itemHandler != null) {
            return itemHandler.cast();
        }
        return super.getCapability(capability, facing);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowID, @Nonnull Inventory playerInventory, @Nonnull Player player) {
        return new AlphaDesertWolfContainer(windowID, playerInventory, this.getId());
    }

    public enum ArmorType {
        NONE(0),
        IRON(5, "iron"),
        GOLD(7, "gold"),
        DIAMOND(11, "diamond");

        private final String typeName;
        private final int protection;

        ArmorType(int armorStrength) {
            this.protection = armorStrength;
            this.typeName = null;
        }

        ArmorType(int armorStrength, String typeName) {
            this.protection = armorStrength;
            this.typeName = typeName;
        }

        public int getProtection() {
            return this.protection;
        }

        public String getName() {
            return this.typeName;
        }

        public static ArmorType getByItemStack(@Nonnull ItemStack stack) {
            Item item = stack.getItem();
            if (item == AtumItems.DESERT_WOLF_IRON_ARMOR) {
                return IRON;
            } else if (item == AtumItems.DESERT_WOLF_GOLD_ARMOR) {
                return GOLD;
            } else if (item == AtumItems.DESERT_WOLF_DIAMOND_ARMOR) {
                return DIAMOND;
            } else {
                return NONE;
            }
        }

        public static boolean isArmor(@Nonnull ItemStack stack) {
            return getByItemStack(stack) != NONE;
        }
    }
}
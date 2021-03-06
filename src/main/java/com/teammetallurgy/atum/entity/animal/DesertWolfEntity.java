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
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class DesertWolfEntity extends TameableEntity implements IJumpingMount, IInventoryChangedListener, INamedContainerProvider, IAngerable {
    private static final DataParameter<Boolean> BEGGING = EntityDataManager.createKey(DesertWolfEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> COLLAR_COLOR = EntityDataManager.createKey(DesertWolfEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> SADDLED = EntityDataManager.createKey(DesertWolfEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<ItemStack> ARMOR_STACK = EntityDataManager.createKey(DesertWolfEntity.class, DataSerializers.ITEMSTACK);
    private static final DataParameter<Integer> ANGER = EntityDataManager.createKey(DesertWolfEntity.class, DataSerializers.VARINT);
    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("0b3da7ef-52bf-47c9-9829-862ffa35b418");
    private String texturePath;
    private Inventory desertWolfInventory;
    private float headRotationCourse;
    private float headRotationCourseWild;
    private boolean isWet;
    private boolean isShaking;
    private float timeWolfIsShaking;
    private float prevTimeWolfIsShaking;
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(DesertWolfEntity.class, DataSerializers.VARINT);
    private static final Attribute JUMP_STRENGTH = (new RangedAttribute("attribute.name.wolf.jump_strength", 0.7D, 0.0D, 2.0D)).setShouldWatch(true);
    private boolean isWolfJumping;
    private float jumpPower;
    private static long lastAlphaTime = 0;
    private UUID angryAt;
    private static final RangedInteger ANGRY_TIMER_RANGE = TickRangeConverter.convertRange(20, 39);

    public DesertWolfEntity(EntityType<? extends DesertWolfEntity> entityType, World world) {
        super(entityType, world);
        this.experienceValue = 6;
        this.stepHeight = 1.1F;
        this.initInventory();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new SitWithCheckGoal(this, !this.isAlpha()));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, DesertWolfEntity.class, 8.0F, 0.6D, 1.0D, avoid -> avoid instanceof DesertWolfEntity && ((DesertWolfEntity) avoid).isAlpha() && (!this.isAlpha() && this.isTamed())));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, LlamaEntity.class, 24.0F, 0.6D, 1.2D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, CamelEntity.class, 24.0F, 0.6D, 1.2D, avoid -> avoid != null && !this.isAlpha()));
        this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(6, new FollowOwnerWithoutSaddleGoal(this, 1.0D, 10.0F, 2.0F));
        this.goalSelector.addGoal(7, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 0.4D));
        this.goalSelector.addGoal(9, new BegGoal(this, 8.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(10, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 0, false, false, target -> !this.isTamed()));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, UndeadBaseEntity.class, 10, false, false, target -> !this.isTamed()));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this).setCallsForHelp());
        this.targetSelector.addGoal(4, new NonTamedTargetGoal<>(this, AnimalEntity.class, false, (entity) -> entity instanceof SheepEntity || entity instanceof RabbitEntity || entity instanceof FoxEntity));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, AbstractSkeletonEntity.class, 10, false, false, target -> !this.isTamed()));
    }

    @Override
    @Nullable
    public ILivingEntityData onInitialSpawn(@Nonnull IServerWorld world, @Nonnull DifficultyInstance difficulty, @Nonnull SpawnReason spawnReason, @Nullable ILivingEntityData livingdata, @Nullable CompoundNBT nbt) {
        livingdata = super.onInitialSpawn(world, difficulty, spawnReason, livingdata, nbt);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.getWolfMaxHealth());
        this.getAttributeManager().createInstanceIfAbsent(Attributes.ATTACK_DAMAGE).setBaseValue(this.getWolfAttack());

        if (this.rand.nextDouble() <= 0.25D && System.currentTimeMillis() > lastAlphaTime + 100) {
            this.setVariant(1);
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.getWolfMaxHealth());
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(this.getWolfAttack());
            this.setHealth(this.getWolfMaxHealth());
            this.experienceValue = 12;
            lastAlphaTime = System.currentTimeMillis();
        } else {
            this.setVariant(0);
        }
        return livingdata;
    }

    public static AttributeModifierMap.MutableAttribute getAttributes() {
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 20.0F).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.4D).createMutableAttribute(Attributes.ATTACK_DAMAGE, 8.0D).createMutableAttribute(JUMP_STRENGTH);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(BEGGING, Boolean.FALSE);
        this.dataManager.register(COLLAR_COLOR, DyeColor.GREEN.getId());
        this.dataManager.register(VARIANT, 0);
        this.dataManager.register(SADDLED, Boolean.FALSE);
        this.dataManager.register(ARMOR_STACK, ItemStack.EMPTY);
        this.dataManager.register(ANGER, 0);
    }

    public static boolean canSpawn(EntityType<? extends DesertWolfEntity> animal, IServerWorld world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return pos.getY() > 62 && ((ServerWorld) world.getChunkProvider().getWorld()).getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING) && world.canBlockSeeSky(pos) && AtumEntities.canAnimalSpawn(animal, world, spawnReason, pos, random);
    }

    @Override
    protected void playStepSound(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.func_233678_J__()) {
            return SoundEvents.ENTITY_WOLF_GROWL;
        } else if (this.rand.nextInt(3) == 0) {
            return this.isTamed() && this.getHealth() < 10.0F ? SoundEvents.ENTITY_WOLF_WHINE : SoundEvents.ENTITY_WOLF_PANT;
        } else {
            return SoundEvents.ENTITY_WOLF_AMBIENT;
        }
    }

    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
        return SoundEvents.ENTITY_WOLF_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WOLF_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    @Nonnull
    protected ResourceLocation getLootTable() {
        if (isAlpha()) {
            return AtumLootTables.DESERT_WOLF_ALPHA;
        }
        return AtumLootTables.DESERT_WOLF;
    }

    @Override
    public void onDeath(@Nonnull DamageSource cause) {
        this.isWet = false;
        this.isShaking = false;
        this.prevTimeWolfIsShaking = 0.0F;
        this.timeWolfIsShaking = 0.0F;
        if (!this.world.isRemote && this.desertWolfInventory != null) {
            for (int i = 0; i < this.desertWolfInventory.getSizeInventory(); ++i) {
                ItemStack slotStack = this.desertWolfInventory.getStackInSlot(i);
                if (!slotStack.isEmpty()) {
                    this.entityDropItem(slotStack, 0.0F);
                }
            }
        }
        super.onDeath(cause);
    }

    @Override
    public void livingTick() {
        super.livingTick();

        if (!this.world.isRemote && this.isWet && !this.isShaking && !this.hasPath() && this.onGround) {
            this.isShaking = true;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
            this.world.setEntityState(this, (byte) 8);
        }

        if (!this.world.isRemote) {
            this.func_241359_a_((ServerWorld) this.world, true);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.world.isRemote && this.dataManager.isDirty()) {
            this.dataManager.setClean();
            this.texturePath = null;
        }

        if (this.isAlive()) {
            this.headRotationCourseWild = this.headRotationCourse;
            if (this.isBegging()) {
                this.headRotationCourse += (1.0F - this.headRotationCourse) * 0.4F;
            } else {
                this.headRotationCourse += (0.0F - this.headRotationCourse) * 0.4F;
            }

            if (!this.world.isRemote && this.world.getDifficulty() == Difficulty.PEACEFUL && !this.isTamed()) {
                this.remove();
            }

            if (this.isInWaterRainOrBubbleColumn()) {
                this.isWet = true;
                if (this.isShaking && !this.world.isRemote) {
                    this.world.setEntityState(this, (byte) 56);
                    this.isShaking = false;
                    this.timeWolfIsShaking = 0.0F;
                    this.prevTimeWolfIsShaking = 0.0F;
                }
            } else if ((this.isWet || this.isShaking) && this.isShaking) {
                if (this.timeWolfIsShaking == 0.0F) {
                    this.playSound(SoundEvents.ENTITY_WOLF_SHAKE, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                }

                this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
                this.timeWolfIsShaking += 0.05F;
                if (this.prevTimeWolfIsShaking >= 2.0F) {
                    this.isWet = false;
                    this.isShaking = false;
                    this.prevTimeWolfIsShaking = 0.0F;
                    this.timeWolfIsShaking = 0.0F;
                }

                if (this.timeWolfIsShaking > 0.4F) {
                    float f = (float) this.getPosY();
                    int i = (int) (MathHelper.sin((this.timeWolfIsShaking - 0.4F) * (float) Math.PI) * 7.0F);
                    Vector3d vector3d = this.getMotion();

                    for (int j = 0; j < i; ++j) {
                        float f1 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.getWidth() * 0.5F;
                        float f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.getWidth() * 0.5F;
                        this.world.addParticle(ParticleTypes.SPLASH, this.getPosX() + (double) f1, (double) (f + 0.8F), this.getPosZ() + (double) f2, vector3d.x, vector3d.y, vector3d.z);
                    }
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public String getTexture() {
        if (this.texturePath == null) {
            this.texturePath = func_233678_J__() ? "angry" : "tamed";
        }
        return this.texturePath;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isWolfWet() {
        return this.isWet;
    }

    @OnlyIn(Dist.CLIENT)
    public float getShadingWhileWet(float shading) {
        return 0.75F + (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * shading) / 2.0F * 0.25F;
    }

    @OnlyIn(Dist.CLIENT)
    public float getShakeAngle(float p_70923_1_, float p_70923_2_) {
        float f = (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70923_1_ + p_70923_2_) / 1.8F;

        if (f < 0.0F) {
            f = 0.0F;
        } else if (f > 1.0F) {
            f = 1.0F;
        }
        return MathHelper.sin(f * (float) Math.PI) * MathHelper.sin(f * (float) Math.PI * 11.0F) * 0.15F * (float) Math.PI;
    }

    @OnlyIn(Dist.CLIENT)
    public float getInterestedAngle(float angle) {
        return (this.headRotationCourseWild + (this.headRotationCourse - this.headRotationCourseWild) * angle) * 0.15F * (float) Math.PI;
    }

    @Override
    protected float getStandingEyeHeight(@Nonnull Pose pose, EntitySize size) {
        return size.height * 0.8F;
    }

    @Override
    public float getRenderScale() {
        return this.isAlpha() ? 1.6F : super.getRenderScale();
    }

    @Override
    public int getVerticalFaceSpeed() {
        return this.isSitting() ? 20 : super.getVerticalFaceSpeed();
    }

    @Override
    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            Entity entity = source.getTrueSource();
            this.func_233687_w_(false);

            if (entity != null && !(entity instanceof PlayerEntity) && !(entity instanceof ArrowEntity)) {
                amount = (amount + 1.0F) / 2.0F;
            }
            return super.attackEntityFrom(source, amount);
        }
    }

    @Override
    public boolean attackEntityAsMob(@Nonnull Entity entity) {
        boolean shouldAttack = entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) ((int) this.getAttributeValue(Attributes.ATTACK_DAMAGE)));

        if (shouldAttack) {
            this.applyEnchantments(this, entity);
        }
        return shouldAttack;
    }

    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
            this.setAngerTime(0);
        }
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.getWolfMaxHealth());
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(this.getWolfAttack());
    }

    @Override
    @Nonnull
    public ActionResultType func_230254_b_(PlayerEntity player, @Nonnull Hand hand) {
        ItemStack heldStack = player.getHeldItem(hand);
        Item item = heldStack.getItem();
        boolean tameItem = item.isIn(Tags.Items.BONES) || item == Items.RABBIT || item == Items.COOKED_RABBIT;
        if (this.world.isRemote) {
            boolean flag = this.isOwner(player) || this.isTamed() || tameItem && !this.isTamed();
            return flag ? ActionResultType.CONSUME : ActionResultType.PASS;
        } else {
            if (this.isTamed()) {
                if (this.isBreedingItem(heldStack) && this.getHealth() < this.getMaxHealth()) {
                    if (!player.abilities.isCreativeMode) {
                        heldStack.shrink(1);
                    }

                    this.heal((float) item.getFood().getHealing());
                    return ActionResultType.SUCCESS;
                }

                boolean holdsArmor = ArmorType.isArmor(heldStack);
                boolean holdsSaddle = !this.isChild() && !this.isSaddled() && heldStack.getItem() instanceof SaddleItem;

                if (holdsArmor || holdsSaddle) {
                    this.openGUI(player);
                    return ActionResultType.SUCCESS;
                }

                if (!(item instanceof DyeItem)) {
                    if (!this.isChild()) {
                        if (player.isCrouching()) {
                            this.openGUI(player);
                            return ActionResultType.SUCCESS;
                        }
                        if (this.isBeingRidden()) {
                            return super.func_230254_b_(player, hand);
                        }
                    }

                    ActionResultType resultType = super.func_230254_b_(player, hand);
                    if (this.isAlpha() && !this.isBeingRidden()) {
                        this.mountTo(player);
                        return ActionResultType.SUCCESS;
                    } else if (!this.isAlpha() && (!resultType.isSuccessOrConsume() || this.isChild()) && this.isOwner(player)) { //Sit
                        this.func_233687_w_(!this.isSitting());
                        this.isJumping = false;
                        this.navigator.clearPath();
                        this.setAttackTarget(null);
                        return ActionResultType.SUCCESS;
                    }
                    return resultType;
                }

                DyeColor color = ((DyeItem) item).getDyeColor();
                if (color != this.getCollarColor()) {
                    this.setCollarColor(color);
                    if (!player.abilities.isCreativeMode) {
                        heldStack.shrink(1);
                    }
                    return ActionResultType.SUCCESS;
                }
            } else if (tameItem) {
                if (!player.abilities.isCreativeMode) {
                    heldStack.shrink(1);
                }

                if (this.rand.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
                    this.setTamedBy(player);
                    this.navigator.clearPath();
                    this.setAttackTarget(null);
                    if (!this.isAlpha()) {
                        this.func_233687_w_(true);
                    }
                    this.func_241356_K__();
                    this.setHealth(40.0F);
                    this.world.setEntityState(this, (byte) 7);
                } else {
                    this.world.setEntityState(this, (byte) 6);
                }
                return ActionResultType.SUCCESS;
            }
            return super.func_230254_b_(player, hand);
        }
    }

    @Override
    public boolean isSitting() {
        if (this.isAlpha()) {
            return false;
        }
        return super.isSitting();
    }

    @Override
    protected boolean isMovementBlocked() {
        return super.isMovementBlocked() && this.isBeingRidden() && this.isSaddled();
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void openInventoryOverride(GuiOpenEvent event) {
        if (event.getGui() instanceof DisplayEffectsScreen) {
            PlayerEntity player = Minecraft.getInstance().player;
            if (player != null && player.getRidingEntity() instanceof DesertWolfEntity) {
                DesertWolfEntity desertWolf = (DesertWolfEntity) player.getRidingEntity();
                if (player.getUniqueID() == player.getUniqueID()) {
                    if (desertWolf.isAlpha() && desertWolf.isBeingRidden()) {
                        event.setCanceled(true);
                        NetworkHandler.sendToServer(new OpenWolfGuiPacket(desertWolf.getEntityId()));
                    }
                }
            }
        }
    }

    private void openGUI(PlayerEntity player) {
        if (!this.world.isRemote && (!this.isBeingRidden() || this.isPassenger(player)) && this.isTamed()) {
            if (player instanceof ServerPlayerEntity) {
                NetworkHooks.openGui((ServerPlayerEntity) player, this, buf -> buf.writeInt(this.getEntityId()));
            }
        }
    }

    public Inventory getInventory() {
        return this.desertWolfInventory;
    }

    private void initInventory() {
        Inventory inventory = this.desertWolfInventory;
        this.desertWolfInventory = new Inventory(2);

        if (inventory != null) {
            inventory.removeListener(this);
            for (int j = 0; j < 2; ++j) {
                ItemStack slotStack = inventory.getStackInSlot(j);
                if (!slotStack.isEmpty()) {
                    this.desertWolfInventory.setInventorySlotContents(j, slotStack.copy());
                }
            }
        }
        this.desertWolfInventory.addListener(this);
        this.updateSlots();
        this.itemHandler = LazyOptional.of(() -> new InvWrapper(this.desertWolfInventory));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 8) {
            this.isShaking = true;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
        } else {
            super.handleStatusUpdate(id);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public float getTailRotation() {
        if (this.func_233678_J__()) {
            return 1.5393804F;
        } else if (!this.isBeingRidden()) {
            return this.isTamed() ? (0.55F - (this.getMaxHealth() - this.getHealth()) * 0.02F) * (float) Math.PI : ((float) Math.PI / 5F);
        } else {
            return 1.5F;
        }
    }

    @Override
    public boolean isBreedingItem(@Nonnull ItemStack stack) {
        Item item = stack.getItem();
        return item.isFood() && item.getFood() != null && item.getFood().isMeat();
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 6;
    }

    @Override
    public int getAngerTime() {
        return this.dataManager.get(ANGER);
    }

    @Override
    public void setAngerTime(int time) {
        if (!this.isTamed()) {
            this.dataManager.set(ANGER, time);
        }
    }

    @Override
    public void func_230258_H__() {
        this.setAngerTime(ANGRY_TIMER_RANGE.getRandomWithinRange(this.rand));
    }

    @Override
    @Nullable
    public UUID getAngerTarget() {
        return this.angryAt;
    }

    @Override
    public void setAngerTarget(@Nullable UUID target) {
        this.angryAt = target;
    }

    @Override
    public boolean func_233678_J__() {
        return !this.isTamed() || this.getAngerTime() > 0;
    }

    public DyeColor getCollarColor() {
        return DyeColor.byId(this.dataManager.get(COLLAR_COLOR));
    }

    private void setCollarColor(DyeColor color) {
        this.dataManager.set(COLLAR_COLOR, color.getId());
    }

    public boolean isSaddled() {
        return this.dataManager.get(SADDLED);
    }

    private void setSaddled(boolean saddled) {
        if (saddled) {
            this.dataManager.set(SADDLED, Boolean.TRUE);
        } else {
            this.dataManager.set(SADDLED, Boolean.FALSE);
        }
    }

    @Override
    public DesertWolfEntity func_241840_a(@Nonnull ServerWorld world, @Nonnull AgeableEntity ageable) {
        DesertWolfEntity desertWolf = AtumEntities.DESERT_WOLF.create(this.world);
        if (desertWolf != null) {
            UUID uuid = this.getOwnerId();

            if (uuid != null) {
                desertWolf.setOwnerId(uuid);
                desertWolf.setTamed(true);
                desertWolf.func_241356_K__();
                desertWolf.heal(8.0F);
            }
            return desertWolf;
        }
        return null;
    }

    public void setBegging(boolean beg) {
        this.dataManager.set(BEGGING, beg);
    }

    private boolean isBegging() {
        return this.dataManager.get(BEGGING);
    }

    @Override
    public boolean canMateWith(@Nonnull AnimalEntity otherAnimal) {
        if (otherAnimal == this) {
            return false;
        } else if (!this.isTamed()) {
            return false;
        } else if (!(otherAnimal instanceof DesertWolfEntity)) {
            return false;
        } else {
            DesertWolfEntity desertWolf = (DesertWolfEntity) otherAnimal;

            if (!desertWolf.isTamed()) {
                return false;
            } else if (desertWolf.isSitting()) {
                return false;
            } else {
                return this.isInLove() && desertWolf.isInLove();
            }
        }
    }

    @Override
    public boolean shouldAttackEntity(@Nonnull LivingEntity target, @Nonnull LivingEntity owner) {
        if (!(target instanceof CreeperEntity) && !(target instanceof GhastEntity)) {
            if (target instanceof DesertWolfEntity) {
                DesertWolfEntity desertWolf = (DesertWolfEntity) target;
                if (desertWolf.isTamed() && desertWolf.getOwner() == owner) {
                    return false;
                }
            }
            if (target instanceof PlayerEntity && owner instanceof PlayerEntity && !((PlayerEntity) owner).canAttackPlayer((PlayerEntity) target)) {
                return false;
            } else if (target instanceof AbstractHorseEntity && ((AbstractHorseEntity) target).isTame()) {
                return false;
            } else {
                return !(target instanceof CatEntity) || !((CatEntity) target).isTamed();
            }
        } else {
            return false;
        }
    }

    @SubscribeEvent
    public void onTarget(LivingSetAttackTargetEvent event) {
        if (event.getTarget() instanceof DesertWolfEntity && event.getEntityLiving() instanceof DesertWolfEntity) {
            if (((DesertWolfEntity) event.getTarget()).isTamed() && ((DesertWolfEntity) event.getEntityLiving()).isTamed()) {
                ((DesertWolfEntity) event.getEntityLiving()).setAttackTarget(null);
            }
        }
    }

    @Override
    public boolean canBeLeashedTo(@Nonnull PlayerEntity player) {
        return !this.func_233678_J__() && super.canBeLeashedTo(player);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        compound.putInt("Variant", this.getVariant());
        super.writeAdditional(compound);
        compound.putByte("CollarColor", (byte) this.getCollarColor().getId());
        compound.putBoolean("Saddle", this.isSaddled());
        this.writeAngerNBT(compound);
        if (!this.desertWolfInventory.getStackInSlot(0).isEmpty()) {
            compound.put("SaddleItem", this.desertWolfInventory.getStackInSlot(0).write(new CompoundNBT()));
        }
        if (!this.desertWolfInventory.getStackInSlot(1).isEmpty()) {
            compound.put("ArmorItem", this.desertWolfInventory.getStackInSlot(1).write(new CompoundNBT()));
        }
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        this.setVariant(compound.getInt("Variant"));
        super.readAdditional(compound);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.getWolfMaxHealth());
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(this.getWolfAttack());
        this.setSaddled(compound.getBoolean("Saddle"));
        this.readAngerNBT((ServerWorld) this.world, compound);

        if (compound.contains("CollarColor", 99)) {
            this.setCollarColor(DyeColor.byId(compound.getInt("CollarColor")));
        }
        if (compound.contains("SaddleItem", 10)) {
            ItemStack saddleStack = ItemStack.read(compound.getCompound("SaddleItem"));
            if (saddleStack.getItem() instanceof SaddleItem) {
                this.desertWolfInventory.setInventorySlotContents(0, saddleStack);
            }
        }
        if (compound.contains("ArmorItem", 10)) {
            ItemStack armorStack = ItemStack.read(compound.getCompound("ArmorItem"));
            if (!armorStack.isEmpty() && isArmor(armorStack)) {
                this.desertWolfInventory.setInventorySlotContents(1, armorStack);
            }
        }
        this.updateSlots();
    }

    private void updateSlots() {
        this.setArmorStack(this.desertWolfInventory.getStackInSlot(1));
        if (!this.world.isRemote) {
            this.setSaddled(!this.desertWolfInventory.getStackInSlot(0).isEmpty());
        }
    }

    private void setArmorStack(@Nonnull ItemStack stack) {
        ArmorType armorType = ArmorType.getByItemStack(stack);
        this.dataManager.set(ARMOR_STACK, stack);

        if (!this.world.isRemote) {
            ModifiableAttributeInstance armor = this.getAttribute(Attributes.ARMOR);
            if (armor != null) {
                armor.removeModifier(ARMOR_MODIFIER_UUID);
                int protection = armorType.getProtection();
                if (protection != 0) {
                    armor.applyNonPersistentModifier((new AttributeModifier(ARMOR_MODIFIER_UUID, "Desert wolf armor bonus", protection, AttributeModifier.Operation.ADDITION)));
                }
            }
        }
    }

    @Nonnull
    public ItemStack getArmor() {
        return this.dataManager.get(ARMOR_STACK);
    }

    @Override
    public void onInventoryChanged(@Nonnull IInventory invBasic) {
        this.updateSlots();
    }

    @Override
    public boolean replaceItemInInventory(int inventorySlot, @Nonnull ItemStack stack) {
        int slot = inventorySlot - 400;
        if (slot >= 0 && slot < 2 && slot < this.desertWolfInventory.getSizeInventory()) {
            if (slot == 0 && !(stack.getItem() instanceof SaddleItem)) {
                return false;
            } else if (slot != 1 || this.isArmor(stack)) {
                this.desertWolfInventory.setInventorySlotContents(slot, stack);
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
        this.dataManager.set(VARIANT, variant);
        this.texturePath = null;
    }

    private int getVariant() {
        return this.dataManager.get(VARIANT);
    }

    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @Override
    public boolean canBeSteered() {
        return true;
    }

    @Override
    public double getMountedYOffset() {
        return super.getMountedYOffset() + 0.07D;
    }

    @Override
    public void updatePassenger(@Nonnull Entity passenger) {
        if (this.isPassenger(passenger)) {
            float cos = MathHelper.cos(this.renderYawOffset * 0.017453292F);
            float sin = MathHelper.sin(this.renderYawOffset * 0.017453292F);
            passenger.setPosition(this.getPosX() + (double) (0.4F * sin), this.getPosY() + this.getMountedYOffset() + passenger.getYOffset(), this.getPosZ() - (double) (0.4F * cos));
        }
    }

    private void mountTo(PlayerEntity player) {
        player.rotationYaw = this.rotationYaw;
        player.rotationPitch = this.rotationPitch;
        if (!this.world.isRemote) {
            player.startRiding(this);
        }
    }

    @Override
    public void travel(@Nonnull Vector3d travelVec) {
        if (this.isAlive()) {
            if (this.isBeingRidden() && this.canBeSteered() && this.isSaddled()) {
                LivingEntity livingBase = (LivingEntity) this.getControllingPassenger();
                if (livingBase != null) {
                    this.rotationYaw = livingBase.rotationYaw;
                    this.prevRotationYaw = this.rotationYaw;
                    this.rotationPitch = livingBase.rotationPitch * 0.5F;
                    this.setRotation(this.rotationYaw, this.rotationPitch);
                    this.renderYawOffset = this.rotationYaw;
                    this.rotationYawHead = this.renderYawOffset;
                    float strafe = livingBase.moveStrafing * 0.5F;
                    float forward = livingBase.moveForward;

                    if (forward <= 0.0F) {
                        forward *= 0.25F;
                    }

                    if (this.jumpPower > 0.0F && !this.isJumping() && this.onGround) {
                        double wolfJumpStrength = this.getWolfJumpStrength() * (double) this.jumpPower;
                        double jumpY;

                        if (this.isPotionActive(Effects.JUMP_BOOST)) {
                            EffectInstance jumpBoost = this.getActivePotionEffect(Effects.JUMP_BOOST);
                            jumpY = wolfJumpStrength + (jumpBoost != null ? (double) ((float) (jumpBoost.getAmplifier() + 1) * 0.1F) : 0);
                        } else {
                            jumpY = wolfJumpStrength;
                        }

                        Vector3d motion = this.getMotion();
                        this.setMotion(motion.x, jumpY, motion.z);
                        this.setWolfJumping(true);
                        this.isAirBorne = true;

                        if (forward > 0.0F) {
                            float f2 = MathHelper.sin(this.rotationYaw * ((float) Math.PI / 180F));
                            float f3 = MathHelper.cos(this.rotationYaw * ((float) Math.PI / 180F));
                            this.setMotion(this.getMotion().add(-0.4F * f2 * this.jumpPower, 0.0D, 0.4F * f3 * this.jumpPower));
                            this.playSound(SoundEvents.ENTITY_HORSE_JUMP, 0.4F, 1.0F);
                        }
                        this.jumpPower = 0.0F;
                    }
                    this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;

                    if (this.canPassengerSteer()) {
                        this.setAIMoveSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.80F);
                        super.travel(new Vector3d(strafe, travelVec.y, forward));
                    } else if (livingBase instanceof PlayerEntity) {
                        this.setMotion(Vector3d.ZERO);
                    }

                    if (this.onGround) {
                        this.jumpPower = 0.0F;
                        this.setWolfJumping(false);
                    }
                }
            } else {
                this.jumpMovementFactor = 0.02F;
                super.travel(travelVec);
            }
        }
    }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) {
        if (this.isAlpha() && distance > 5.0F) {
            return super.onLivingFall(distance, damageMultiplier);
        } else if (!this.isAlpha() && distance > 2.5F) {
            return super.onLivingFall(distance, damageMultiplier);
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
    public void setJumpPower(int jumpPower) {
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
        if (this.isTamed()) {
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
    public Container createMenu(int windowID, @Nonnull PlayerInventory playerInventory, @Nonnull PlayerEntity player) {
        return new AlphaDesertWolfContainer(windowID, playerInventory, this.getEntityId());
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
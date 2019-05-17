package com.teammetallurgy.atum.entity.animal;

import com.google.common.base.Predicate;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.ai.AIBeg;
import com.teammetallurgy.atum.entity.ai.AISitWithCheck;
import com.teammetallurgy.atum.entity.ai.EntityAIFollowOwnerWithoutSaddle;
import com.teammetallurgy.atum.entity.undead.EntityUndeadBase;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.network.NetworkHandler;
import com.teammetallurgy.atum.network.packet.PacketOpenWolfGui;
import com.teammetallurgy.atum.utils.AtumUtils;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class EntityDesertWolf extends EntityTameable implements IJumpingMount, IInventoryChangedListener {
    private static final DataParameter<Float> DATA_HEALTH_ID = EntityDataManager.createKey(EntityDesertWolf.class, DataSerializers.FLOAT);
    private static final DataParameter<Boolean> BEGGING = EntityDataManager.createKey(EntityDesertWolf.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> COLLAR_COLOR = EntityDataManager.createKey(EntityDesertWolf.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> SADDLED = EntityDataManager.createKey(EntityDesertWolf.class, DataSerializers.BOOLEAN);
    private static final DataParameter<ItemStack> ARMOR_STACK = EntityDataManager.createKey(EntityDesertWolf.class, DataSerializers.ITEM_STACK);
    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("0b3da7ef-52bf-47c9-9829-862ffa35b418");
    private String texturePath;
    private InventoryBasic desertWolfInventory;
    private float headRotationCourse;
    private float headRotationCourseWild;
    private boolean isWet;
    private boolean isShaking;
    private float timeWolfIsShaking;
    private float prevTimeWolfIsShaking;
    private int angryTimer;
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityDesertWolf.class, DataSerializers.VARINT);
    private static final IAttribute JUMP_STRENGTH = (new RangedAttribute(null, "wolf.jumpStrength", 0.7D, 0.0D, 2.0D)).setDescription("Jump Strength").setShouldWatch(true);
    private boolean isWolfJumping;
    private float jumpPower;
    private static long lastAlphaTime = 0;

    public EntityDesertWolf(World world) {
        super(world);
        this.setSize(0.6F, 0.8F);
        this.setAngry(true);
        this.setTamed(false);
        this.experienceValue = 6;
        this.stepHeight = 1.1F;
        MinecraftForge.EVENT_BUS.register(this);
        this.initInventory();
    }

    @Override
    protected void initEntityAI() {
        this.aiSit = new AISitWithCheck(this, !this.isAlpha());
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, new EntityAIAvoidEntity<>(this, EntityDesertWolf.class, avoid -> avoid != null && avoid.isAlpha() && (!this.isAlpha() && this.isTamed()), 8.0F, 0.6D, 1.0D));
        this.tasks.addTask(3, new EntityAIAvoidEntity<>(this, EntityLlama.class, 24.0F, 0.6D, 1.2D));
        this.tasks.addTask(3, new EntityAIAvoidEntity<>(this, EntityCamel.class, avoid -> avoid != null && !this.isAlpha(), 24.0F, 0.6D, 1.2D));
        this.tasks.addTask(4, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(5, new EntityAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(6, new EntityAIFollowOwnerWithoutSaddle(this, 1.0D, 10.0F, 2.0F));
        this.tasks.addTask(7, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWanderAvoidWater(this, 0.4D));
        this.tasks.addTask(9, new AIBeg(this, 8.0F));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(10, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, 0, false, false, target -> !this.isTamed()));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityUndeadBase.class, false));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(4, new EntityAITargetNonTamed<>(this, EntityAnimal.class, false, (Predicate<Entity>) entity -> entity instanceof EntitySheep || entity instanceof EntityRabbit));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>(this, AbstractSkeleton.class, false));
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        if (world.rand.nextDouble() <= 0.25D && System.currentTimeMillis() > lastAlphaTime + 100) {
            this.setVariant(1);
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getWolfMaxHealth());
            this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(this.getWolfAttack());
            this.setHealth(this.getWolfMaxHealth());
            this.experienceValue = 12;
            lastAlphaTime = System.currentTimeMillis();
            this.aiSit = null;
        } else {
            this.setVariant(0);
        }
        return livingdata;
    }

    @Override
    @Nonnull
    public String getName() {
        if (this.hasCustomName()) {
            return this.getCustomNameTag();
        } else {
            String s = EntityList.getEntityString(this);

            if (s == null) {
                s = "generic";
            }

            if (this.isAlpha()) {
                s += ".alpha";
            }
            return AtumUtils.format("entity." + s + ".name");
        }
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();

        this.getAttributeMap().registerAttribute(JUMP_STRENGTH);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getWolfMaxHealth());

        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(this.getWolfAttack());
    }

    @Override
    public void setAttackTarget(@Nullable EntityLivingBase livingBase) {
        super.setAttackTarget(livingBase);

        if (livingBase == null) {
            this.setAngry(false);
        } else if (!this.isTamed()) {
            this.setAngry(true);
        }
    }

    @Override
    protected void updateAITasks() {
        this.dataManager.set(DATA_HEALTH_ID, this.getHealth());
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(DATA_HEALTH_ID, this.getHealth());
        this.dataManager.register(BEGGING, Boolean.FALSE);
        this.dataManager.register(COLLAR_COLOR, EnumDyeColor.GREEN.getDyeDamage());
        this.dataManager.register(VARIANT, 0);
        this.dataManager.register(SADDLED, Boolean.FALSE);
        this.dataManager.register(ARMOR_STACK, ItemStack.EMPTY);
    }

    @Override
    public boolean getCanSpawnHere() {
        BlockPos pos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
        if (pos.getY() <= 62 || !this.world.getGameRules().getBoolean("doMobSpawning")) {
            return false;
        } else {
            return this.world.getBlockState(pos.down()) == AtumBlocks.SAND.getDefaultState() && this.world.getLight(pos) > 8 && this.world.canBlockSeeSky(pos) &&
                    this.world.checkNoEntityCollision(this.getEntityBoundingBox()) && this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.world.containsAnyLiquid(this.getEntityBoundingBox());
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, Block block) {
        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.isAngry()) {
            return SoundEvents.ENTITY_WOLF_GROWL;
        } else if (this.rand.nextInt(3) == 0) {
            return this.isTamed() && this.dataManager.get(DATA_HEALTH_ID) < 10.0F ? SoundEvents.ENTITY_WOLF_WHINE : SoundEvents.ENTITY_WOLF_PANT;
        } else {
            return SoundEvents.ENTITY_WOLF_AMBIENT;
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
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
    @Nullable
    protected ResourceLocation getLootTable() {
        if (isAlpha()) {
            return AtumLootTables.DESERT_WOLF_ALPHA;
        }
        return AtumLootTables.DESERT_WOLF;
    }

    @Override
    public void onDeath(@Nonnull DamageSource cause) {
        super.onDeath(cause);

        if (!this.world.isRemote && this.desertWolfInventory != null) {
            for (int i = 0; i < this.desertWolfInventory.getSizeInventory(); ++i) {
                ItemStack slotStack = this.desertWolfInventory.getStackInSlot(i);
                if (!slotStack.isEmpty()) {
                    this.entityDropItem(slotStack, 0.0F);
                }
            }
        }
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (!this.world.isRemote && this.isWet && !this.isShaking && !this.hasPath() && this.onGround) {
            this.isShaking = true;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
            this.world.setEntityState(this, (byte) 8);
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.world.isRemote && this.dataManager.isDirty()) {
            this.dataManager.setClean();
            this.texturePath = null;
        }

        this.headRotationCourseWild = this.headRotationCourse;

        if (angryTimer > 0) {
            this.setAngry(false);
            if (getAttackTarget() instanceof EntityPlayer) {
                this.setAttackTarget(null);
                this.setRevengeTarget(null);
            }
            if (!this.isAngry()) {
                angryTimer--;
            }
            if (this.isTamed()) {
                angryTimer = 0;
            }
        }

        if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL && !this.isTamed()) {
            this.setDead();
        }

        if (this.isBegging()) {
            this.headRotationCourse += (1.0F - this.headRotationCourse) * 0.4F;
        } else {
            this.headRotationCourse += (0.0F - this.headRotationCourse) * 0.4F;
        }

        if (this.isWet()) {
            this.isWet = true;
            this.isShaking = false;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
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
                float y = (float) this.getEntityBoundingBox().minY;
                int shakingTime = (int) (MathHelper.sin((this.timeWolfIsShaking - 0.4F) * (float) Math.PI) * 7.0F);

                for (int j = 0; j < shakingTime; ++j) {
                    float f1 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
                    float f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
                    this.world.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + (double) f1, (double) (y + 0.8F), this.posZ + (double) f2, this.motionX, this.motionY, this.motionZ);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public String getTexture() {
        if (this.texturePath == null) {
            this.texturePath = isAngry() ? "angry" : "tamed";

            ItemStack armor = this.getArmor();
            if (!armor.isEmpty()) {
                EntityDesertWolf.ArmorType armorType = EntityDesertWolf.ArmorType.getByItemStack(armor);
                this.texturePath += "_" + armorType.getName();
            }

            if (isSaddled()){
                this.texturePath += "_saddled";
            }
        }
        return this.texturePath;
    }

    @SideOnly(Side.CLIENT)
    public boolean isWolfWet() {
        return this.isWet;
    }

    @SideOnly(Side.CLIENT)
    public float getShadingWhileWet(float shading) {
        return 0.75F + (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * shading) / 2.0F * 0.25F;
    }

    @SideOnly(Side.CLIENT)
    public float getShakeAngle(float p_70923_1_, float p_70923_2_) {
        float f = (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70923_1_ + p_70923_2_) / 1.8F;

        if (f < 0.0F) {
            f = 0.0F;
        } else if (f > 1.0F) {
            f = 1.0F;
        }
        return MathHelper.sin(f * (float) Math.PI) * MathHelper.sin(f * (float) Math.PI * 11.0F) * 0.15F * (float) Math.PI;
    }

    @SideOnly(Side.CLIENT)
    public float getInterestedAngle(float angle) {
        return (this.headRotationCourseWild + (this.headRotationCourse - this.headRotationCourseWild) * angle) * 0.15F * (float) Math.PI;
    }

    @Override
    public float getEyeHeight() {
        return this.height * 0.8F;
    }

    @Override
    public void setScaleForAge(boolean child) {
        if (this.isAlpha()) {
            this.setScale(1.6F);
        } else {
            super.setScaleForAge(child);
        }
    }

    @Override
    public int getVerticalFaceSpeed() {
        return this.isSitting() ? 20 : super.getVerticalFaceSpeed();
    }

    @Override
    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        } else {
            Entity entity = source.getTrueSource();

            if (this.aiSit != null) {
                this.aiSit.setSitting(false);
            }

            if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow)) {
                amount = (amount + 1.0F) / 2.0F;
            }
            return super.attackEntityFrom(source, amount);
        }
    }

    @Override
    public boolean attackEntityAsMob(@Nonnull Entity entity) {
        boolean shouldAttack = entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));

        if (shouldAttack) {
            this.applyEnchantments(this, entity);
        }
        return shouldAttack;
    }

    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getWolfMaxHealth());
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(this.getWolfAttack());
    }

    @Override
    public boolean processInteract(EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack heldStack = player.getHeldItem(hand);

        if (this.isTamed()) {
            if (!heldStack.isEmpty()) {
                if (heldStack.getItem() instanceof ItemFood) {
                    ItemFood food = (ItemFood) heldStack.getItem();

                    if (food.isWolfsFavoriteMeat() && this.dataManager.get(DATA_HEALTH_ID) < getMaxHealth()) {
                        if (!player.capabilities.isCreativeMode) {
                            heldStack.shrink(1);
                        }
                        this.heal((float) food.getHealAmount(heldStack));
                        return true;
                    }
                } else if (heldStack.getItem() instanceof ItemDye) {
                    EnumDyeColor color = EnumDyeColor.byDyeDamage(heldStack.getMetadata());

                    if (color != this.getCollarColor()) {
                        this.setCollarColor(color);

                        if (!player.capabilities.isCreativeMode) {
                            heldStack.shrink(1);
                        }
                        return true;
                    }
                }

                boolean holdsArmor = ArmorType.isArmor(heldStack);
                boolean holdsSaddle = !this.isChild() && !this.isSaddled() && heldStack.getItem() == Items.SADDLE;

                if (holdsArmor || holdsSaddle) {
                    this.openGUI(player);
                    return true;
                }
            }

            if (!this.isChild()) {
                if (player.isSneaking()) {
                    this.openGUI(player);
                    return true;
                }
                if (this.isBeingRidden()) {
                    return super.processInteract(player, hand);
                }
            }

            if (!this.world.isRemote && !this.isBreedingItem(heldStack) && !this.isBeingRidden()) {
                if (this.isAlpha()) {
                    this.mountTo(player);
                } else if (!this.isAlpha() && this.isOwner(player)) {
                    this.aiSit.setSitting(!this.isSitting());
                    this.isJumping = false;
                    this.navigator.clearPath();
                    this.setAttackTarget(null);
                }
            }
        } else if ((heldStack.getItem() == Items.BONE || heldStack.getItem() == AtumItems.DUSTY_BONE || heldStack.getItem() == Items.RABBIT) || heldStack.getItem() == Items.COOKED_RABBIT) {
            if (!player.capabilities.isCreativeMode) {
                heldStack.shrink(1);
            }
            if (this.isAngry() && !world.isRemote) {
                this.angryTimer = (this.isAlpha() && rand.nextDouble() <= 0.5D || !this.isAlpha()) ? 200 : 0;
            } else if (!this.isAngry() && angryTimer > 0) {
                if (!this.world.isRemote) {
                    if (this.rand.nextInt(2) == 0 && !ForgeEventFactory.onAnimalTame(this, player)) {
                        this.setTamedBy(player);
                        this.navigator.clearPath();
                        this.setAttackTarget(null);
                        if (!this.isAlpha()) {
                            this.aiSit.setSitting(true);
                        }
                        this.setHealth(40.0F);
                        this.playTameEffect(true);
                        this.world.setEntityState(this, (byte) 7);
                    } else {
                        this.playTameEffect(false);
                        this.world.setEntityState(this, (byte) 6);
                    }
                }
            }
            return true;
        }
        return super.processInteract(player, hand);
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
    @SideOnly(Side.CLIENT)
    public void openInventoryOverride(GuiOpenEvent event) {
        if (this.isBeingRidden() && event.getGui() instanceof GuiInventory) {
            Entity entity = this.getControllingPassenger();
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                if (player.getUniqueID() == Minecraft.getMinecraft().player.getUniqueID()) {
                    NetworkHandler.WRAPPER.sendToServer(new PacketOpenWolfGui(this.getEntityId()));
                    event.setCanceled(true);
                }
            }
        }
    }

    private void openGUI(EntityPlayer player) {
        if (!this.world.isRemote && (!this.isBeingRidden() || this.isPassenger(player)) && this.isTamed()) {
            desertWolfInventory.setCustomName(this.getName());
            player.openGui(Atum.instance, 4, world, this.getEntityId(), 0, 0);
        }
    }

    public InventoryBasic getInventory() {
        return desertWolfInventory;
    }

    private void initInventory() {
        InventoryBasic inventory = this.desertWolfInventory;
        this.desertWolfInventory = new InventoryBasic("DesertWolfInventory", true, 2);
        this.desertWolfInventory.setCustomName(this.getName());

        if (inventory != null) {
            inventory.removeInventoryChangeListener(this);
            for (int j = 0; j < 2; ++j) {
                ItemStack slotStack = inventory.getStackInSlot(j);
                if (!slotStack.isEmpty()) {
                    this.desertWolfInventory.setInventorySlotContents(j, slotStack.copy());
                }
            }
        }
        this.desertWolfInventory.addInventoryChangeListener(this);
        this.updateSlots();
        this.itemHandler = new InvWrapper(this.desertWolfInventory);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 8) {
            this.isShaking = true;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
        } else {
            super.handleStatusUpdate(id);
        }
    }

    @SideOnly(Side.CLIENT)
    public float getTailRotation() {
        if (this.isAngry()) {
            return 1.5393804F;
        } else if (!this.isBeingRidden()) {
            return this.isTamed() ? (0.55F - (this.getMaxHealth() - this.dataManager.get(DATA_HEALTH_ID)) * 0.02F) * (float) Math.PI : ((float) Math.PI / 5F);
        } else {
            return 1.5F;
        }
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() instanceof ItemFood && ((ItemFood) stack.getItem()).isWolfsFavoriteMeat();
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 6;
    }

    public boolean isAngry() {
        return (this.dataManager.get(TAMED) & 2) != 0;
    }

    private void setAngry(boolean angry) {
        byte tamed = this.dataManager.get(TAMED);
        if (angry) {
            this.dataManager.set(TAMED, (byte) (tamed | 2));
        } else {
            this.dataManager.set(TAMED, (byte) (tamed & -3));
        }
    }

    public EnumDyeColor getCollarColor() {
        return EnumDyeColor.byDyeDamage(this.dataManager.get(COLLAR_COLOR) & 15);
    }

    private void setCollarColor(EnumDyeColor color) {
        this.dataManager.set(COLLAR_COLOR, color.getDyeDamage());
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
    public EntityDesertWolf createChild(@Nonnull EntityAgeable ageable) {
        EntityDesertWolf desertWolf = new EntityDesertWolf(this.world);
        UUID uuid = this.getOwnerId();

        if (uuid != null) {
            desertWolf.setOwnerId(uuid);
            desertWolf.setTamed(true);
            desertWolf.heal(8.0F);
        }
        return desertWolf;
    }

    public void setBegging(boolean beg) {
        this.dataManager.set(BEGGING, beg);
    }

    private boolean isBegging() {
        return this.dataManager.get(BEGGING);
    }

    @Override
    public boolean canMateWith(@Nonnull EntityAnimal otherAnimal) {
        if (otherAnimal == this) {
            return false;
        } else if (!this.isTamed()) {
            return false;
        } else if (!(otherAnimal instanceof EntityDesertWolf)) {
            return false;
        } else {
            EntityDesertWolf desertWolf = (EntityDesertWolf) otherAnimal;

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
    public boolean shouldAttackEntity(EntityLivingBase target, EntityLivingBase owner) {
        if (!(target instanceof EntityCreeper) && !(target instanceof EntityGhast)) {
            if (target instanceof EntityDesertWolf) {
                EntityDesertWolf desertWolf = (EntityDesertWolf) target;

                if (desertWolf.isTamed() && desertWolf.getOwner() == owner) {
                    return false;
                }
            }
            if (target instanceof EntityPlayer && owner instanceof EntityPlayer && !((EntityPlayer) owner).canAttackPlayer((EntityPlayer) target)) {
                return false;
            } else {
                return !(target instanceof AbstractHorse) || !((AbstractHorse) target).isTame();
            }
        } else {
            return false;
        }
    }

    @SubscribeEvent
    public void onTarget(LivingSetAttackTargetEvent event) {
        if (event.getTarget() instanceof EntityDesertWolf && event.getEntityLiving() instanceof EntityDesertWolf) {
            if (((EntityDesertWolf) event.getTarget()).isTamed() && ((EntityDesertWolf) event.getEntityLiving()).isTamed()) {
                ((EntityLiving) event.getEntityLiving()).setAttackTarget(null);
            }
        }
    }

    @Override
    public boolean canBeLeashedTo(EntityPlayer player) {
        return !this.isAngry() && super.canBeLeashedTo(player);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger("Variant", this.getVariant());
        super.writeEntityToNBT(compound);
        compound.setBoolean("Angry", this.isAngry());
        compound.setByte("CollarColor", (byte) this.getCollarColor().getDyeDamage());
        compound.setBoolean("Saddle", this.isSaddled());
        if (angryTimer > 0) {
            compound.setInteger("AngryTimer", angryTimer);
        }
        if (!this.desertWolfInventory.getStackInSlot(0).isEmpty()) {
            compound.setTag("SaddleItem", this.desertWolfInventory.getStackInSlot(0).writeToNBT(new NBTTagCompound()));
        }
        if (!this.desertWolfInventory.getStackInSlot(1).isEmpty()) {
            compound.setTag("ArmorItem", this.desertWolfInventory.getStackInSlot(1).writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        this.setVariant(compound.getInteger("Variant"));
        super.readEntityFromNBT(compound);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getWolfMaxHealth());
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(this.getWolfAttack());
        this.setAngry(compound.getBoolean("Angry"));
        this.setSaddled(compound.getBoolean("Saddle"));
        angryTimer = compound.getInteger("AngryTimer");

        if (compound.hasKey("CollarColor", 99)) {
            this.setCollarColor(EnumDyeColor.byDyeDamage(compound.getByte("CollarColor")));
        }
        if (compound.hasKey("SaddleItem", 10)) {
            ItemStack saddleStack = new ItemStack(compound.getCompoundTag("SaddleItem"));
            if (saddleStack.getItem() == Items.SADDLE) {
                this.desertWolfInventory.setInventorySlotContents(0, saddleStack);
            }
        }
        if (compound.hasKey("ArmorItem", 10)) {
            ItemStack armorStack = new ItemStack(compound.getCompoundTag("ArmorItem"));
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
            this.getEntityAttribute(SharedMonsterAttributes.ARMOR).removeModifier(ARMOR_MODIFIER_UUID);
            int protection = armorType.getProtection();
            if (protection != 0) {
                this.getEntityAttribute(SharedMonsterAttributes.ARMOR).applyModifier((new AttributeModifier(ARMOR_MODIFIER_UUID, "Desert wolf armor bonus", (double) protection, 0)).setSaved(false));
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
            if (slot == 0 && stack.getItem() != Items.SADDLE) {
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
            passenger.setPosition(this.posX + (double) (0.4F * sin), this.posY + this.getMountedYOffset() + passenger.getYOffset(), this.posZ - (double) (0.4F * cos));
        }
    }

    private void mountTo(EntityPlayer player) {
        player.rotationYaw = this.rotationYaw;
        player.rotationPitch = this.rotationPitch;
        if (!this.world.isRemote) {
            player.startRiding(this);
        }
    }

    @Override
    public void travel(float strafe, float vertical, float forward) {
        if (this.isBeingRidden() && this.canBeSteered() && this.isSaddled()) {
            EntityLivingBase livingBase = (EntityLivingBase) this.getControllingPassenger();
            if (livingBase != null) {
                this.rotationYaw = livingBase.rotationYaw;
                this.prevRotationYaw = this.rotationYaw;
                this.rotationPitch = livingBase.rotationPitch * 0.5F;
                this.setRotation(this.rotationYaw, this.rotationPitch);
                this.renderYawOffset = this.rotationYaw;
                this.rotationYawHead = this.renderYawOffset;
                strafe = livingBase.moveStrafing * 0.5F;
                forward = livingBase.moveForward;

                if (forward <= 0.0F) {
                    forward *= 0.25F;
                }

                if (this.jumpPower > 0.0F && !this.isJumping() && this.onGround) {
                    this.motionY = this.getWolfJumpStrength() * (double) this.jumpPower;

                    if (this.isPotionActive(MobEffects.JUMP_BOOST)) {
                        PotionEffect jumpBoost = this.getActivePotionEffect(MobEffects.JUMP_BOOST);
                        if (jumpBoost != null) {
                            this.motionY += (double) ((float) (jumpBoost.getAmplifier() + 1) * 0.1F);
                        }
                    }

                    this.setWolfJumping(true);
                    this.isAirBorne = true;

                    if (forward > 0.0F) {
                        float f = MathHelper.sin(this.rotationYaw * 0.017453292F);
                        float f1 = MathHelper.cos(this.rotationYaw * 0.017453292F);
                        this.motionX += (double) (-0.4F * f * this.jumpPower);
                        this.motionZ += (double) (0.4F * f1 * this.jumpPower);
                        this.playSound(SoundEvents.ENTITY_HORSE_JUMP, 0.4F, 1.0F);
                    }
                    this.jumpPower = 0.0F;
                }

                this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;

                if (this.canPassengerSteer()) {
                    this.setAIMoveSpeed((float) this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * 0.80F);
                    super.travel(strafe, vertical, forward);
                } else if (livingBase instanceof EntityPlayer) {
                    this.motionX = 0.0D;
                    this.motionY = 0.0D;
                    this.motionZ = 0.0D;
                }

                if (this.onGround) {
                    this.jumpPower = 0.0F;
                    this.setWolfJumping(false);
                }
            }
        } else {
            this.jumpMovementFactor = 0.02F;
            super.travel(strafe, vertical, forward);
        }
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        if (this.isAlpha() && distance > 5.0F) {
            super.fall(distance, damageMultiplier);
        } else if (!this.isAlpha() && distance > 2.5F) {
            super.fall(distance, damageMultiplier);
        }
    }

    private boolean isJumping() {
        return this.isWolfJumping;
    }

    private void setWolfJumping(boolean jumping) {
        this.isWolfJumping = jumping;
    }

    private double getWolfJumpStrength() {
        return this.getEntityAttribute(JUMP_STRENGTH).getAttributeValue();
    }

    @SideOnly(Side.CLIENT)
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

    public boolean isArmor(ItemStack stack) {
        return ArmorType.isArmor(stack);
    }

    private IItemHandler itemHandler = null;

    @Override
    @Nullable
    @SuppressWarnings("unchecked")
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) itemHandler;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    public enum ArmorType {
        NONE(0),
        IRON(5, "iron"),
        GOLD(7, "gold"),
        DIAMOND(11, "diamond");

        private final String textureName;
        private final String typeName;
        private final int protection;

        ArmorType(int armorStrength) {
            this.protection = armorStrength;
            this.typeName = null;
            this.textureName = null;
        }

        ArmorType(int armorStrength, String typeName) {
            this.protection = armorStrength;
            this.typeName = typeName;
            this.textureName = new ResourceLocation(Constants.MOD_ID, "textures/entity/armor/desert_wolf_armor_" + typeName + ".png").toString();
        }

        public int getProtection() {
            return this.protection;
        }

        public String getName() {
            return typeName;
        }

        public String getTextureName() {
            return textureName;
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
package com.teammetallurgy.atum.entity.animal;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.wood.BlockCrate;
import com.teammetallurgy.atum.entity.ai.AICamelCaravan;
import com.teammetallurgy.atum.entity.projectile.EntityCamelSpit;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class EntityCamel extends AbstractHorse implements IRangedAttackMob {
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityCamel.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> DATA_COLOR_ID = EntityDataManager.createKey(EntityCamel.class, DataSerializers.VARINT);
    private static final DataParameter<ItemStack> LEFT_CRATE = EntityDataManager.createKey(EntityCamel.class, DataSerializers.ITEM_STACK);
    private static final DataParameter<ItemStack> RIGHT_CRATE = EntityDataManager.createKey(EntityCamel.class, DataSerializers.ITEM_STACK);
    public static final float CAMEL_RIDING_SPEED_AMOUNT = 0.65F;
    private String texturePath;
    private boolean didSpit;
    private EntityCamel caravanHead;
    private EntityCamel caravanTail;

    public EntityCamel(World world) {
        super(world);
        this.experienceValue = 3;
        this.setSize(0.9F, 1.87F);
        this.canGallop = false;
        this.stepHeight = 1.6F;
        this.initHorseChest();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(DATA_COLOR_ID, -1);
        if (this.hasSkinVariants()) {
            this.dataManager.register(VARIANT, 0);
        }
        this.dataManager.register(LEFT_CRATE, ItemStack.EMPTY);
        this.dataManager.register(RIGHT_CRATE, ItemStack.EMPTY);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(36.0D);
        this.getEntityAttribute(JUMP_STRENGTH).setBaseValue(0.0D);
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(@Nonnull DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);

        if (hasSkinVariants()) {
            final int variant = MathHelper.getInt(world.rand, 0, getVariantAmount());
            this.setVariant(variant);
        }
        return livingdata;
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIRunAroundLikeCrazy(this, 1.2D));
        this.tasks.addTask(2, new AICamelCaravan(this, 2.0999999046325684D));
        this.tasks.addTask(3, new EntityAIAttackRanged(this, 1.25D, 40, 20.0F));
        this.tasks.addTask(3, new EntityAIPanic(this, 1.2D));
        this.tasks.addTask(4, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(5, new EntityAIFollowParent(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.7D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityCamel.AIHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityCamel.AIDefendTarget(this));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_LLAMA_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ENTITY_LLAMA_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_LLAMA_DEATH;
    }

    @Override
    protected void playStepSound(@Nonnull BlockPos pos, Block block) {
        this.playSound(SoundEvents.ENTITY_HORSE_STEP, 0.15F, 1.0F);
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    protected SoundEvent getAngrySound() {
        return SoundEvents.ENTITY_LLAMA_AMBIENT;
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_COW; //TODO
    }

    @Override
    public EntityCamel createChild(@Nonnull EntityAgeable ageable) {
        EntityCamel camel = new EntityCamel(this.world);
        camel.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(ageable)), null);
        return camel;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.world.isRemote && this.dataManager.isDirty()) {
            this.dataManager.setClean();
            this.texturePath = null;
        }
    }

    private int getVariantAmount() {
        return 5;
    }

    private boolean hasSkinVariants() {
        return true;
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
        if (this.hasSkinVariants()) {
            if (this.texturePath == null) {
                this.texturePath = new ResourceLocation(Constants.MOD_ID, "textures/entities/" + entityName + "_" + this.getVariant()) + ".png";
            }
        } else {
            this.texturePath = String.valueOf(new ResourceLocation(Constants.MOD_ID, "textures/entities/" + entityName + ".png"));
        }
        return this.texturePath;
    }

    @Override
    public void attackEntityWithRangedAttack(@Nonnull EntityLivingBase target, float distanceFactor) {
        this.spit(target);
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {
    }

    private void spit(EntityLivingBase target) {
        EntityCamelSpit camelSpit = new EntityCamelSpit(this.world, this);
        double d0 = target.posX - this.posX;
        double d1 = target.getEntityBoundingBox().minY + (double) (target.height / 3.0F) - camelSpit.posY;
        double d2 = target.posZ - this.posZ;
        float f = MathHelper.sqrt(d0 * d0 + d2 * d2) * 0.2F;
        camelSpit.shoot(d0, d1 + (double) f, d2, 1.5F, 10.0F);
        this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LLAMA_SPIT, this.getSoundCategory(), 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
        this.world.spawnEntity(camelSpit);
        this.didSpit = true;
    }

    private void setDidSpit(boolean didSpit) {
        this.didSpit = didSpit;
    }

    @Override
    public boolean canJump() {
        return false;
    }

    @Override
    public double getMountedYOffset() {
        return (double) this.height * 0.78D;
    }

    @Override
    public void updatePassenger(@Nonnull Entity passenger) {
        if (this.isPassenger(passenger)) {
            float cos = MathHelper.cos(this.renderYawOffset * 0.017453292F);
            float sin = MathHelper.sin(this.renderYawOffset * 0.017453292F);
            passenger.setPosition(this.posX + (double) (0.1F * sin), this.posY + this.getMountedYOffset() + passenger.getYOffset(), this.posZ - (double) (0.1F * cos));
        }
    }

    @Override
    public void setAIMoveSpeed(float speed) {
        if (this.isBeingRidden()) {
            super.setAIMoveSpeed(speed * CAMEL_RIDING_SPEED_AMOUNT);
        } else {
            super.setAIMoveSpeed(speed);
        }
    }

    public void leaveCaravan() {
        if (this.caravanHead != null) {
            this.caravanHead.caravanTail = null;
        }
        this.caravanHead = null;
    }

    public void joinCaravan(EntityCamel camel) {
        this.caravanHead = camel;
        this.caravanHead.caravanTail = this;
    }

    public boolean hasCaravanTrail() {
        return this.caravanTail != null;
    }

    public boolean inCaravan() {
        return this.caravanHead != null;
    }

    @Nullable
    public EntityCamel getCaravanHead() {
        return this.caravanHead;
    }

    @Override
    protected double followLeashSpeed() {
        return 2.0D;
    }

    @Override
    protected void followMother() {
        if (!this.inCaravan() && this.isChild()) {
            super.followMother();
        }
    }

    @Override
    public boolean canEatGrass() {
        return false;
    }

    @Nullable
    public EnumDyeColor getColor() {
        int color = this.dataManager.get(DATA_COLOR_ID);
        return color == -1 ? null : EnumDyeColor.byMetadata(color);
    }

    private void setColor(@Nullable EnumDyeColor color) {
        this.dataManager.set(DATA_COLOR_ID, color == null ? -1 : color.getMetadata());
    }

    public boolean hasColor() {
        return this.getColor() != null;
    }

    @Override
    protected void updateHorseSlots() {
        if (!this.world.isRemote) {
            super.updateHorseSlots();
            this.setColorByItem(this.horseChest.getStackInSlot(2));
        }
        this.dataManager.set(LEFT_CRATE, this.horseChest.getStackInSlot(3));
        this.dataManager.set(RIGHT_CRATE, this.horseChest.getStackInSlot(4));
    }

    private void setColorByItem(@Nonnull ItemStack stack) {
        if (this.isValidCarpet(stack)) {
            this.setColor(EnumDyeColor.byMetadata(stack.getMetadata()));
        } else {
            this.setColor(null);
        }
    }

    public boolean isValidCarpet(ItemStack stack) {
        return stack.getItem() == Item.getItemFromBlock(Blocks.CARPET);
    }

    @Override
    public boolean isArmor(ItemStack stack) {
        return false; //TODO Armor items needs to be implemented first
    }

    @Override
    public boolean wearsArmor() {
        return true;
    }

    @Override
    public void openGUI(@Nonnull EntityPlayer player) {
        if (!this.world.isRemote && (!this.isBeingRidden() || this.isPassenger(player)) && this.isTame()) {
            this.horseChest.setCustomName(this.getName());
            player.openGui(Atum.instance, 3, world, this.getEntityId(), 0, 0);
        }
    }

    public ContainerHorseChest getHorseChest() {
        return this.horseChest;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variant", this.getVariant());

        if (!this.horseChest.getStackInSlot(1).isEmpty()) {
            compound.setTag("ArmorItem", this.horseChest.getStackInSlot(1).writeToNBT(new NBTTagCompound()));
        }
        if (!this.horseChest.getStackInSlot(2).isEmpty()) {
            compound.setTag("Carpet", this.horseChest.getStackInSlot(2).writeToNBT(new NBTTagCompound()));
        }
        if (!this.horseChest.getStackInSlot(3).isEmpty()) {
            compound.setTag("CrateLeft", this.horseChest.getStackInSlot(3).writeToNBT(new NBTTagCompound()));
        }
        if (!this.horseChest.getStackInSlot(3).isEmpty()) {
            compound.setTag("CrateRight", this.horseChest.getStackInSlot(4).writeToNBT(new NBTTagCompound()));
        }

        if (this.hasLeftCrate()) {
            NBTTagList tagList = new NBTTagList();
            for (int slot = this.getNonCrateSize(); slot < this.horseChest.getSizeInventory(); ++slot) {
                ItemStack slotStack = this.horseChest.getStackInSlot(slot);
                if (!slotStack.isEmpty()) {
                    NBTTagCompound tagCompound = new NBTTagCompound();
                    tagCompound.setByte("Slot", (byte) slot);
                    slotStack.writeToNBT(tagCompound);
                    tagList.appendTag(tagCompound);
                }
            }
            compound.setTag("Items", tagList);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setVariant(compound.getInteger("Variant"));

        if (compound.hasKey("Carpet", 10)) {
            this.horseChest.setInventorySlotContents(2, new ItemStack(compound.getCompoundTag("Carpet")));
        }
        if (compound.hasKey("ArmorItem", 10)) {
            ItemStack armorStack = new ItemStack(compound.getCompoundTag("ArmorItem"));
            if (!armorStack.isEmpty() && isArmor(armorStack)) {
                this.horseChest.setInventorySlotContents(1, armorStack);
            }
        }
        if (compound.hasKey("CrateLeft", 10)) {
            this.horseChest.setInventorySlotContents(3, new ItemStack(compound.getCompoundTag("CrateLeft")));
        }
        if (compound.hasKey("CrateRight", 10)) {
            this.horseChest.setInventorySlotContents(4, new ItemStack(compound.getCompoundTag("CrateRight")));
        }

        if (this.hasLeftCrate()) {
            NBTTagList tagList = compound.getTagList("Items", 10);
            this.initHorseChest();
            for (int i = 0; i < tagList.tagCount(); ++i) {
                NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
                int slot = tagCompound.getByte("Slot") & 255;
                if (slot >= this.getNonCrateSize() && slot < this.horseChest.getSizeInventory()) {
                    this.horseChest.setInventorySlotContents(slot, new ItemStack(tagCompound));
                }
            }
        }
        this.updateHorseSlots();
    }

    @Override
    protected void initHorseChest() {
        ContainerHorseChest caemlInventory = this.horseChest;
        System.out.println("Camel Inventory Size testing " +  this.getInventorySize());
        this.horseChest = new ContainerHorseChest("CamelChest", this.getInventorySize());
        this.horseChest.setCustomName(this.getName());

        if (caemlInventory != null) {
            caemlInventory.removeInventoryChangeListener(this);
            int size = Math.min(caemlInventory.getSizeInventory(), this.horseChest.getSizeInventory());

            for (int slot = 0; slot < size; ++slot) {
                ItemStack slotStack = caemlInventory.getStackInSlot(slot);
                if (!slotStack.isEmpty()) {
                    this.horseChest.setInventorySlotContents(slot, slotStack.copy());
                }
            }
        }

        this.horseChest.addInventoryChangeListener(this);
        this.updateHorseSlots();
        //this.itemHandler = new InvWrapper(this.horseChest); //TODO, when inventory is working
    }

    @Override
    public void onInventoryChanged(IInventory invBasic) {
        this.updateHorseSlots();
    }

    @Override
    protected int getInventorySize() {
        int size = this.getNonCrateSize();
        if (this.hasLeftCrate()) {
            size += this.getInventoryColumns() * 3;
        }
        if (this.hasRightCrate()) {
            size += this.getInventoryColumns() * 3;
        }
        return size;
    }

    public int getNonCrateSize() {
        return 5;
    }

    public int getInventoryColumns() {
        return 4;
    }

    public ItemStack getLeftCrate() {
        return this.dataManager.get(LEFT_CRATE);
    }

    public boolean hasLeftCrate() {
        return !getLeftCrate().isEmpty();
    }

    public ItemStack getRightCrate() {
        return this.dataManager.get(RIGHT_CRATE);
    }

    public boolean hasRightCrate() {
        return !getRightCrate().isEmpty();
    }

    @Override
    public boolean processInteract(EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack heldStack = player.getHeldItem(hand);

        if (heldStack.getItem() == Items.SPAWN_EGG) {
            return super.processInteract(player, hand);
        } else {
            if (!this.isChild()) {
                if (this.isTame() && player.isSneaking()) {
                    this.openGUI(player);
                    return true;
                }
                if (this.isBeingRidden()) {
                    return super.processInteract(player, hand);
                }
            }
            if (!heldStack.isEmpty()) {
                boolean eating = this.handleEating(player, heldStack);

                if (!eating && !this.isTame()) {
                    if (heldStack.interactWithEntity(player, this, hand)) {
                        return true;
                    }
                    this.makeMad();
                    return true;
                }

                if (!eating && (!this.hasLeftCrate() ||!this.hasRightCrate()) && Block.getBlockFromItem(heldStack.getItem()) instanceof BlockCrate) {
                    this.openGUI(player);
                    eating = true;
                }
                if (!eating && !this.isChild() && !this.isHorseSaddled() && heldStack.getItem() == Items.SADDLE) {
                    this.openGUI(player);
                    return true;
                }

                if (eating) {
                    if (!player.capabilities.isCreativeMode) {
                        heldStack.shrink(1);
                    }
                    return true;
                }
            }
            if (this.isChild()) {
                return super.processInteract(player, hand);
            } else if (heldStack.interactWithEntity(player, this, hand)) {
                return true;
            } else {
                this.mountTo(player);
                return true;
            }
        }
    }

    @Override
    protected boolean handleEating(@Nonnull EntityPlayer player, @Nonnull ItemStack stack) {
        boolean isEating = false;
        float healAmount = 0.0F;
        int growthAmount = 0;
        int temperAmount = 0;
        Item item = stack.getItem();

        if (item == Items.WHEAT) {
            healAmount = 2.0F;
            growthAmount = 20;
            temperAmount = 3;
        } else if (item == Item.getItemFromBlock(Blocks.HAY_BLOCK)) {
            healAmount = 20.0F;
            growthAmount = 180;
        } else if (item == Items.APPLE) {
            healAmount = 3.0F;
            growthAmount = 60;
            temperAmount = 3;
        } else if (item == Items.GOLDEN_CARROT) {
            healAmount = 4.0F;
            growthAmount = 60;
            temperAmount = 5;
            if (this.isTame() && this.getGrowingAge() == 0 && !this.isInLove()) {
                isEating = true;
                this.setInLove(player);
            }
        } else if (item == Items.GOLDEN_APPLE || item == AtumItems.GOLDEN_DATE || item == AtumItems.ENCHANTED_GOLDEN_DATE) {
            healAmount = 10.0F;
            growthAmount = 240;
            temperAmount = 10;

            if (!this.isTame()) {
                this.setTamedBy(player);
            } else if (this.getGrowingAge() == 0 && !this.isInLove()) {
                isEating = true;
                this.setInLove(player);
            }
        }
        if (this.getHealth() < this.getMaxHealth() && healAmount > 0.0F) {
            this.heal(healAmount);
            isEating = true;
        }
        if (this.isChild() && growthAmount > 0) {
            this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, 0.0D, 0.0D, 0.0D);

            if (!this.world.isRemote) {
                this.addGrowth(growthAmount);
            }
            isEating = true;
        }

        if (temperAmount > 0 && (isEating || !this.isTame()) && this.getTemper() < this.getMaxTemper()) {
            isEating = true;
            if (!this.world.isRemote) {
                this.increaseTemper(temperAmount);
            }
        }
        if (isEating) {
            this.eatingCamel();
        }
        return isEating;
    }

    private void eatingCamel() {
        if (!this.isSilent()) {
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LLAMA_EAT, this.getSoundCategory(), 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
        }
    }

    static class AIDefendTarget extends EntityAINearestAttackableTarget<EntityDesertWolf> {
        AIDefendTarget(EntityCamel camel) {
            super(camel, EntityDesertWolf.class, 16, false, true, null);
        }

        @Override
        public boolean shouldExecute() {
            if (super.shouldExecute() && this.targetEntity != null && !this.targetEntity.isTamed()) {
                return true;
            } else {
                this.taskOwner.setAttackTarget(null);
                return false;
            }
        }

        @Override
        protected double getTargetDistance() {
            return super.getTargetDistance() * 0.25D;
        }
    }

    static class AIHurtByTarget extends EntityAIHurtByTarget {
        AIHurtByTarget(EntityCamel camel) {
            super(camel, false);
        }

        @Override
        public boolean shouldContinueExecuting() {
            if (this.taskOwner instanceof EntityCamel) {
                EntityCamel camel = (EntityCamel) this.taskOwner;
                if (camel.didSpit) {
                    camel.setDidSpit(false);
                    return false;
                }
            }
            return super.shouldContinueExecuting();
        }
    }
}
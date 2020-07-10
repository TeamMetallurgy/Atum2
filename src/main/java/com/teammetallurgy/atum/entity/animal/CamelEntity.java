package com.teammetallurgy.atum.entity.animal;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.linen.LinenCarpetBlock;
import com.teammetallurgy.atum.blocks.wood.CrateBlock;
import com.teammetallurgy.atum.entity.ai.goal.CamelCaravanGoal;
import com.teammetallurgy.atum.entity.projectile.CamelSpitEntity;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.inventory.container.entity.CamelContainer;
import com.teammetallurgy.atum.world.biome.DeadOasisBiome;
import com.teammetallurgy.atum.world.biome.OasisBiome;
import com.teammetallurgy.atum.world.biome.SandDunesBiome;
import com.teammetallurgy.atum.world.biome.SandPlainsBiome;
import com.teammetallurgy.atum.world.dimension.AtumDimensionType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CarpetBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class CamelEntity extends AbstractHorseEntity implements IRangedAttackMob, INamedContainerProvider {
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(CamelEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> DATA_COLOR_ID = EntityDataManager.createKey(CamelEntity.class, DataSerializers.VARINT);
    private static final DataParameter<ItemStack> LEFT_CRATE = EntityDataManager.createKey(CamelEntity.class, DataSerializers.ITEMSTACK);
    private static final DataParameter<ItemStack> RIGHT_CRATE = EntityDataManager.createKey(CamelEntity.class, DataSerializers.ITEMSTACK);
    private static final DataParameter<ItemStack> ARMOR_STACK = EntityDataManager.createKey(CamelEntity.class, DataSerializers.ITEMSTACK);
    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("13a48eeb-c17d-45cc-8163-e7210a6adfc9");
    public static final float CAMEL_RIDING_SPEED_AMOUNT = 0.65F;
    private String textureName;
    private boolean didSpit;
    private CamelEntity caravanHead;
    private CamelEntity caravanTail;

    public CamelEntity(EntityType<? extends CamelEntity> entityType, World world) {
        super(entityType, world);
        this.experienceValue = 3;
        this.canGallop = false;
        this.stepHeight = 1.6F;
        this.initHorseChest();
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(DATA_COLOR_ID, -1);
        this.dataManager.register(VARIANT, 0);
        this.dataManager.register(LEFT_CRATE, ItemStack.EMPTY);
        this.dataManager.register(RIGHT_CRATE, ItemStack.EMPTY);
        this.dataManager.register(ARMOR_STACK, ItemStack.EMPTY);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getCamelMaxHealth());
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(36.0D);
        this.getAttribute(JUMP_STRENGTH).setBaseValue(0.0D);
    }

    @Override
    @Nullable
    public ILivingEntityData onInitialSpawn(@Nonnull IWorld world, @Nonnull DifficultyInstance difficulty, @Nonnull SpawnReason spawnReason, @Nullable ILivingEntityData livingdata, @Nullable CompoundNBT nbt) {
        livingdata = super.onInitialSpawn(world, difficulty, spawnReason, livingdata, nbt);

        final int variant = this.getCamelVariantBiome();
        this.setVariant(variant);
        return livingdata;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new RunAroundLikeCrazyGoal(this, 1.2D));
        this.goalSelector.addGoal(2, new CamelCaravanGoal(this, 2.0999999046325684D));
        this.goalSelector.addGoal(3, new RangedAttackGoal(this, 1.25D, 40, 20.0F));
        this.goalSelector.addGoal(3, new PanicGoal(this, 1.2D));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 0.7D));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new CamelEntity.SpitGoal(this));
        this.targetSelector.addGoal(2, new CamelEntity.DefendDesertWolfGoal(this));
        this.targetSelector.addGoal(3, new CamelEntity.DefendWolfGoal(this));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_LLAMA_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
        return SoundEvents.ENTITY_LLAMA_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_LLAMA_DEATH;
    }

    @Override
    protected void playStepSound(@Nonnull BlockPos pos, @Nonnull BlockState state) {
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

    @Override
    public boolean canMateWith(@Nonnull AnimalEntity otherAnimal) {
        return otherAnimal != this && otherAnimal instanceof CamelEntity && this.canMate() && ((CamelEntity) otherAnimal).canMate();
    }

    @Override
    public CamelEntity createChild(@Nonnull AgeableEntity ageable) {
        CamelEntity camel = new CamelEntity(AtumEntities.CAMEL, this.world);
        camel.onInitialSpawn(this.world, this.world.getDifficultyForLocation(new BlockPos(ageable)), SpawnReason.BREEDING, null, null);
        return camel;
    }

    private float getCamelMaxHealth() {
        if (this.isTame()) {
            return 40.0F;
        } else {
            return 20.0F;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.world.isRemote && this.dataManager.isDirty()) {
            this.dataManager.setClean();
            this.textureName = null;
        }
    }

    private void setVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
        this.textureName = null;
    }

    public int getVariant() {
        return this.dataManager.get(VARIANT);
    }

    private int getCamelVariantBiome() {
        Biome biome = this.world.getBiome(new BlockPos(this));
        int chance = this.rand.nextInt(100);

        if (this.world.dimension.getType() == AtumDimensionType.ATUM) {
            if (biome instanceof SandPlainsBiome) {
                return chance <= 50 ? 0 : 5;
            } else if (biome instanceof SandDunesBiome) {
                return chance <= 50 ? 0 : 2;
            } else if (biome instanceof OasisBiome) {
                return chance <= 50 ? 0 : 1;
            } else if (biome instanceof DeadOasisBiome) {
                return chance <= 50 ? 3 : 4;
            } else {
                return 0;
            }
        } else {
            return MathHelper.nextInt(rand, 0, 5);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public String getTexture() {
        if (this.textureName == null) {
            this.textureName = String.valueOf(this.getVariant());
            if (this.hasCustomName() && this.getCustomName() != null) {
                String customName = this.getCustomName().getFormattedText();
                if (customName.equalsIgnoreCase("girafi")) {
                    this.textureName = "girafi";
                }
            }

            ItemStack armor = this.getArmor();
            if (!armor.isEmpty()) {
                CamelEntity.ArmorType armorType = CamelEntity.ArmorType.getByItemStack(armor);
                this.textureName += "_" + armorType.getName();
            }

            DyeColor color = this.getColor();
            if (color != null) {
                this.textureName += "_" + color.getName();
            }
        }
        return this.textureName;
    }

    @Override
    public void attackEntityWithRangedAttack(@Nonnull LivingEntity target, float distanceFactor) {
        this.spit(target);
    }

    private void spit(LivingEntity target) {
        CamelSpitEntity camelSpit = new CamelSpitEntity(this.world, this);
        double d0 = target.getPosX() - this.getPosX();
        double d1 = target.getBoundingBox().minY + (double) (target.getHeight() / 3.0F) - camelSpit.getPosY();
        double d2 = target.getPosZ() - this.getPosZ();
        float f = MathHelper.sqrt(d0 * d0 + d2 * d2) * 0.2F;
        camelSpit.shoot(d0, d1 + (double) f, d2, 1.5F, 10.0F);
        this.world.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_LLAMA_SPIT, this.getSoundCategory(), 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
        this.world.addEntity(camelSpit);
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
        return (double) this.getHeight() * 0.78D;
    }

    @Override
    public void updatePassenger(@Nonnull Entity passenger) {
        if (this.isPassenger(passenger)) {
            float cos = MathHelper.cos(this.renderYawOffset * 0.017453292F);
            float sin = MathHelper.sin(this.renderYawOffset * 0.017453292F);
            passenger.setPosition(this.getPosX() + (double) (0.1F * sin), this.getPosY() + this.getMountedYOffset() + passenger.getYOffset(), this.getPosZ() - (double) (0.1F * cos));
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

    public void joinCaravan(CamelEntity camel) {
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
    public CamelEntity getCaravanHead() {
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
    public DyeColor getColor() {
        int color = this.dataManager.get(DATA_COLOR_ID);
        return color == -1 ? null : DyeColor.byId(color);
    }

    private void setColor(@Nullable DyeColor color) {
        this.dataManager.set(DATA_COLOR_ID, color == null ? -1 : color.getId());
    }

    public boolean hasColor() {
        return this.getColor() != null;
    }

    @Override
    protected void updateHorseSlots() {
        if (!this.world.isRemote) {
            super.updateHorseSlots();
            this.setColor(getCarpetColor(this.horseChest.getStackInSlot(2)));
        }
        this.setArmorStack(this.horseChest.getStackInSlot(1));
        this.dataManager.set(LEFT_CRATE, this.horseChest.getStackInSlot(3));
        this.dataManager.set(RIGHT_CRATE, this.horseChest.getStackInSlot(4));
    }

    private void setArmorStack(@Nonnull ItemStack stack) {
        ArmorType armorType = ArmorType.getByItemStack(stack);
        this.dataManager.set(ARMOR_STACK, stack);

        if (!this.world.isRemote) {
            this.getAttribute(SharedMonsterAttributes.ARMOR).removeModifier(ARMOR_MODIFIER_UUID);
            int protection = armorType.getProtection();
            if (protection != 0) {
                this.getAttribute(SharedMonsterAttributes.ARMOR).applyModifier((new AttributeModifier(ARMOR_MODIFIER_UUID, "Camel armor bonus", protection, AttributeModifier.Operation.ADDITION)).setSaved(false));
            }
        }
    }

    @Nonnull
    public ItemStack getArmor() {
        return this.dataManager.get(ARMOR_STACK);
    }

    @Nullable
    private static DyeColor getCarpetColor(ItemStack stack) {
        Block block = Block.getBlockFromItem(stack.getItem());
        return block instanceof CarpetBlock ? ((CarpetBlock) block).getColor() : null;
    }

    public boolean isValidCarpet(@Nonnull ItemStack stack) {
        return stack.getItem().isIn(ItemTags.CARPETS) || Block.getBlockFromItem(stack.getItem()) instanceof LinenCarpetBlock;
    }

    @Override
    public boolean isArmor(@Nonnull ItemStack stack) {
        return ArmorType.isArmor(stack);
    }

    @Override
    public boolean wearsArmor() {
        return true;
    }

    @Override
    public void openGUI(@Nonnull PlayerEntity player) {
        if (!this.world.isRemote && (!this.isBeingRidden() || this.isPassenger(player)) && this.isTame()) {
            if (player instanceof ServerPlayerEntity) {
                NetworkHooks.openGui((ServerPlayerEntity) player, this, buf -> buf.writeInt(this.getEntityId()));
            }
        }
    }

    public Inventory getHorseChest() {
        return this.horseChest;
    }

    @Override
    public void writeAdditional(@Nonnull CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("Variant", this.getVariant());

        if (!this.horseChest.getStackInSlot(1).isEmpty()) {
            compound.put("ArmorItem", this.horseChest.getStackInSlot(1).write(new CompoundNBT()));
        }
        if (!this.horseChest.getStackInSlot(2).isEmpty()) {
            compound.put("Carpet", this.horseChest.getStackInSlot(2).write(new CompoundNBT()));
        }
        if (!this.horseChest.getStackInSlot(3).isEmpty()) {
            compound.put("CrateLeft", this.horseChest.getStackInSlot(3).write(new CompoundNBT()));
        }
        if (!this.horseChest.getStackInSlot(4).isEmpty()) {
            compound.put("CrateRight", this.horseChest.getStackInSlot(4).write(new CompoundNBT()));
        }

        if (this.hasLeftCrate()) {
            ListNBT tagList = new ListNBT();
            for (int slot = this.getNonCrateSize(); slot < this.horseChest.getSizeInventory(); ++slot) {
                ItemStack slotStack = this.horseChest.getStackInSlot(slot);
                if (!slotStack.isEmpty()) {
                    CompoundNBT tagCompound = new CompoundNBT();
                    tagCompound.putByte("Slot", (byte) slot);
                    slotStack.write(tagCompound);
                    tagList.add(tagCompound);
                }
            }
            compound.put("Items", tagList);
        }
    }

    @Override
    public void readAdditional(@Nonnull CompoundNBT compound) {
        super.readAdditional(compound);
        this.setVariant(compound.getInt("Variant"));

        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getCamelMaxHealth());

        if (compound.contains("Carpet", 10)) {
            this.horseChest.setInventorySlotContents(2, ItemStack.read(compound.getCompound("Carpet")));
        }
        if (compound.contains("ArmorItem", 10)) {
            ItemStack armorStack = ItemStack.read(compound.getCompound("ArmorItem"));
            if (!armorStack.isEmpty() && isArmor(armorStack)) {
                this.horseChest.setInventorySlotContents(1, armorStack);
            }
        }
        if (compound.contains("CrateLeft", 10)) {
            this.horseChest.setInventorySlotContents(3, ItemStack.read(compound.getCompound("CrateLeft")));
        }
        if (compound.contains("CrateRight", 10)) {
            this.horseChest.setInventorySlotContents(4, ItemStack.read(compound.getCompound("CrateRight")));
        }

        if (this.hasLeftCrate()) {
            ListNBT tagList = compound.getList("Items", 10);
            this.initHorseChest();
            for (int i = 0; i < tagList.size(); ++i) {
                CompoundNBT tagCompound = tagList.getCompound(i);
                int slot = tagCompound.getByte("Slot") & 255;
                if (slot >= this.getNonCrateSize() && slot < this.horseChest.getSizeInventory()) {
                    this.horseChest.setInventorySlotContents(slot, ItemStack.read(tagCompound));
                }
            }
        }
        this.updateHorseSlots();
    }

    @Override
    public void onInventoryChanged(@Nonnull IInventory invBasic) {
        this.updateHorseSlots();
    }

    @Override
    protected int getInventorySize() {
        return this.getNonCrateSize() + 2 * (this.getInventoryColumns() * 3);
    }

    public int getNonCrateSize() {
        return 5;
    }

    public int getInventoryColumns() {
        return 4;
    }

    public boolean hasLeftCrate() {
        return !this.dataManager.get(LEFT_CRATE).isEmpty();
    }

    public boolean hasRightCrate() {
        return !this.dataManager.get(RIGHT_CRATE).isEmpty();
    }

    @Override
    public boolean processInteract(PlayerEntity player, @Nonnull Hand hand) {
        ItemStack heldStack = player.getHeldItem(hand);

        if (heldStack.getItem() instanceof SpawnEggItem) {
            return super.processInteract(player, hand);
        } else {
            if (!heldStack.isEmpty()) {
                boolean eating = this.handleEating(player, heldStack);

                if (!eating && !this.isTame()) {
                    if (heldStack.interactWithEntity(player, this, hand)) {
                        return true;
                    }
                    this.makeMad();
                    return true;
                }

                if (!eating && (!this.hasLeftCrate() || !this.hasRightCrate()) && Block.getBlockFromItem(heldStack.getItem()) instanceof CrateBlock) {
                    this.openGUI(player);
                    return true;
                }
                if (!eating && this.getArmor().isEmpty() && this.isArmor(heldStack)) {
                    this.openGUI(player);
                    return true;
                }
                if (!eating && this.horseChest.getStackInSlot(2).isEmpty() && this.isValidCarpet(heldStack)) {
                    this.openGUI(player);
                    return true;
                }
                if (!eating && !this.isChild() && !this.isHorseSaddled() && heldStack.getItem() instanceof SaddleItem) {
                    this.openGUI(player);
                    return true;
                }
                if (!eating && heldStack.getItem() == Items.BUCKET && !this.isChild() && this.isTame()) {
                    heldStack.shrink(1);
                    player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
                    if (heldStack.isEmpty()) {
                        player.setHeldItem(hand, new ItemStack(Items.MILK_BUCKET));
                    } else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.MILK_BUCKET))) {
                        player.dropItem(new ItemStack(Items.MILK_BUCKET), false);
                    }
                    return true;
                }

                if (eating) {
                    if (!player.abilities.isCreativeMode) {
                        heldStack.shrink(1);
                    }
                    return true;
                }
            }

            if (!this.isChild()) {
                if (this.isTame() && player.isCrouching()) {
                    this.openGUI(player);
                    return true;
                }
                if (this.isBeingRidden()) {
                    return super.processInteract(player, hand);
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
    protected boolean handleEating(@Nonnull PlayerEntity player, @Nonnull ItemStack stack) {
        boolean isEating = false;
        float healAmount = 0.0F;
        int growthAmount = 0;
        int temperAmount = 0;
        Item item = stack.getItem();

        if (item.isIn(Tags.Items.CROPS_WHEAT)) {
            healAmount = 2.0F;
            growthAmount = 20;
            temperAmount = 3;
        } else if (item == Blocks.HAY_BLOCK.asItem()) {
            healAmount = 20.0F;
            growthAmount = 180;
        } else if (item == Items.APPLE) {
            healAmount = 3.0F;
            growthAmount = 60;
            temperAmount = 3;
        } else if (item == AtumItems.DATE) {
            healAmount = 3.0F;
            growthAmount = 60;
            temperAmount = 3;
            if (this.isTame() && this.getGrowingAge() == 0 && !this.isInLove()) {
                isEating = true;
                this.setInLove(player);
            }
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
            this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + 0.5D + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), 0.0D, 0.0D, 0.0D);

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
            this.world.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_LLAMA_EAT, this.getSoundCategory(), 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
        }
    }

    @Override
    public void setHorseTamed(boolean tamed) {
        super.setHorseTamed(tamed);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getCamelMaxHealth());
        this.heal(this.getCamelMaxHealth());
    }

    @Nullable
    @Override
    public Container createMenu(int windowID, @Nonnull PlayerInventory playerInventory, @Nonnull PlayerEntity player) {
        return new CamelContainer(windowID, playerInventory, this.getEntityId());
    }

    static class DefendDesertWolfGoal extends NearestAttackableTargetGoal<DesertWolfEntity> {
        DefendDesertWolfGoal(CamelEntity camel) {
            super(camel, DesertWolfEntity.class, 16, false, true, (entity) -> !((DesertWolfEntity) entity).isTamed());
        }

        @Override
        protected double getTargetDistance() {
            return super.getTargetDistance() * 0.25D;
        }
    }

    static class DefendWolfGoal extends NearestAttackableTargetGoal<WolfEntity> {
        public DefendWolfGoal(CamelEntity camel) {
            super(camel, WolfEntity.class, 16, false, true, (entity) -> !((WolfEntity) entity).isTamed());
        }

        @Override
        protected double getTargetDistance() {
            return super.getTargetDistance() * 0.25D;
        }
    }

    static class SpitGoal extends HurtByTargetGoal {
        SpitGoal(CamelEntity camel) {
            super(camel);
        }

        @Override
        public boolean shouldContinueExecuting() {
            if (this.goalOwner instanceof CamelEntity) {
                CamelEntity camel = (CamelEntity) this.goalOwner;
                if (camel.didSpit) {
                    camel.setDidSpit(false);
                    return false;
                }
            }
            return super.shouldContinueExecuting();
        }
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
            this.textureName = new ResourceLocation(Atum.MOD_ID, "textures/entity/armor/camel_armor_" + typeName) + ".png";
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
            if (item == AtumItems.CAMEL_IRON_ARMOR) {
                return IRON;
            } else if (item == AtumItems.CAMEL_GOLD_ARMOR) {
                return GOLD;
            } else if (item == AtumItems.CAMEL_DIAMOND_ARMOR) {
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
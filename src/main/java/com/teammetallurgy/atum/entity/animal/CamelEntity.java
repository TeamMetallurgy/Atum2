package com.teammetallurgy.atum.entity.animal;

import com.mojang.serialization.Dynamic;
import com.teammetallurgy.atum.blocks.linen.LinenCarpetBlock;
import com.teammetallurgy.atum.blocks.wood.CrateBlock;
import com.teammetallurgy.atum.entity.ai.brain.AtumCamelAi;
import com.teammetallurgy.atum.entity.projectile.CamelSpitEntity;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.inventory.container.entity.CamelContainer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.camel.CamelAi;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WoolCarpetBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.Tags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class CamelEntity extends Camel implements RangedAttackMob, MenuProvider {
    public static final Ingredient TEMPTATION_ITEM = Ingredient.of(AtumItems.DATE, Items.CACTUS);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(CamelEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_COLOR_ID = SynchedEntityData.defineId(CamelEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<ItemStack> LEFT_CRATE = SynchedEntityData.defineId(CamelEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<ItemStack> RIGHT_CRATE = SynchedEntityData.defineId(CamelEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<ItemStack> ARMOR_STACK = SynchedEntityData.defineId(CamelEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDimensions SITTING_DIMENSIONS = EntityDimensions.scalable(AtumEntities.CAMEL.get().getWidth(), AtumEntities.CAMEL.get().getHeight() - 1.43F);
    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("13a48eeb-c17d-45cc-8163-e7210a6adfc9");
    private String textureName;
    public boolean didSpit;

    public CamelEntity(EntityType<? extends CamelEntity> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 3;
        this.createInventory();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_COLOR_ID, -1);
        this.entityData.define(VARIANT, 0);
        this.entityData.define(LEFT_CRATE, ItemStack.EMPTY);
        this.entityData.define(RIGHT_CRATE, ItemStack.EMPTY);
        this.entityData.define(ARMOR_STACK, ItemStack.EMPTY);
    }

    @Nonnull
    public static AttributeSupplier.Builder createAttributes() {
        return createBaseHorseAttributes().add(Attributes.MAX_HEALTH, 32.0).add(Attributes.MOVEMENT_SPEED, 0.09F).add(Attributes.JUMP_STRENGTH, 0.42F);
    }

    private float getCamelMaxHealth() {
        if (this.isTamed()) {
            return 40.0F;
        } else {
            return 20.0F;
        }
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@Nonnull ServerLevelAccessor level, @Nonnull DifficultyInstance difficulty, @Nonnull MobSpawnType spawnReason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag nbt) {
        livingdata = super.finalizeSpawn(level, difficulty, spawnReason, livingdata, nbt);

        this.setRandomVariant();
        return livingdata;
    }

    public void setRandomVariant() {
        final int variant = this.getCamelVariantBiome();
        this.setVariant(variant);
    }

    @Override
    @Nonnull
    protected Brain.Provider<Camel> brainProvider() {
        return AtumCamelAi.brainProvider();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new RangedAttackGoal(this, 1.25D, 40, 20.0F));
        this.targetSelector.addGoal(1, new CamelEntity.SpitGoal(this));
    }

    @Override
    @Nonnull
    protected Brain<?> makeBrain(@Nonnull Dynamic<?> dynamic) {
        return AtumCamelAi.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    @Override
    @Nonnull
    public EntityDimensions getDimensions(@Nonnull Pose pose) {
        return pose == Pose.SITTING ? SITTING_DIMENSIONS.scale(this.getScale()) : super.getDimensions(pose);
    }

    @Override
    protected SoundEvent getAngrySound() {
        return SoundEvents.CAMEL_DASH;
    }

    @Override
    public boolean canMate(@Nonnull Animal otherAnimal) {
        return otherAnimal != this && otherAnimal instanceof CamelEntity && this.canParent() && ((CamelEntity) otherAnimal).canParent();
    }

    @Override
    public CamelEntity getBreedOffspring(@Nonnull ServerLevel level, @Nonnull AgeableMob ageable) {
        CamelEntity camel = new CamelEntity(AtumEntities.CAMEL.get(), this.level());
        camel.finalizeSpawn(level, this.level().getCurrentDifficultyAt(ageable.blockPosition()), MobSpawnType.BREEDING, null, null);
        return camel;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide && this.entityData.isDirty()) {
            this.textureName = null;
        }
        if (this.getVariant() == -1) {
            this.setRandomVariant();
        }
    }

    private void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
        this.textureName = null;
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    private int getCamelVariantBiome() {
        Biome biome = this.level().getBiome(this.blockPosition()).value();
        int chance = this.random.nextInt(100);

        Optional<ResourceKey<Biome>> optional = level().registryAccess().registryOrThrow(Registries.BIOME).getResourceKey(biome);

        if (optional.isPresent()) {
            ResourceKey<Biome> biomeKey = optional.get();
            if (biomeKey.equals(AtumBiomes.SAND_PLAINS)) {
                return chance <= 50 ? 0 : 5;
            } else if (biomeKey.equals(AtumBiomes.SAND_DUNES)) {
                return chance <= 50 ? 0 : 2;
            } else if (biomeKey.equals(AtumBiomes.OASIS)) {
                return chance <= 50 ? 0 : 1;
            } else if (biomeKey.equals(AtumBiomes.DEAD_OASIS)) {
                return chance <= 50 ? 3 : 4;
            } else {
                return 0;
            }
        } else {
            return Mth.nextInt(random, 0, 5);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public String getTexture() {
        if (this.textureName == null) {
            this.textureName = String.valueOf(this.getVariant());
            if (this.hasCustomName() && this.getCustomName() != null) {
                String customName = this.getCustomName().getString();
                if (customName.equalsIgnoreCase("girafi")) {
                    this.textureName = "girafi";
                }
            }
        }
        return this.textureName;
    }

    @Override
    public void performRangedAttack(@Nonnull LivingEntity target, float distanceFactor) {
        this.spit(target);
    }

    private void spit(LivingEntity target) {
        CamelSpitEntity camelSpit = new CamelSpitEntity(this.level(), this);
        double d0 = target.getX() - this.getX();
        double d1 = target.getBoundingBox().minY + (double) (target.getBbHeight() / 3.0F) - camelSpit.getY();
        double d2 = target.getZ() - this.getZ();
        float f = Mth.sqrt((float) (d0 * d0 + d2 * d2)) * 0.2F;
        camelSpit.shoot(d0, d1 + (double) f, d2, 1.5F, 10.0F);
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LLAMA_SPIT, this.getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
        this.level().addFreshEntity(camelSpit);
        this.didSpit = true;
    }

    public void setDidSpit(boolean didSpit) {
        this.didSpit = didSpit;
    }

    @Nullable
    public DyeColor getColor() {
        int color = this.entityData.get(DATA_COLOR_ID);
        return color == -1 ? null : DyeColor.byId(color);
    }

    private void setColor(@Nullable DyeColor color) {
        this.entityData.set(DATA_COLOR_ID, color == null ? -1 : color.getId());
    }

    public boolean hasColor() {
        return this.getColor() != null;
    }

    @Override
    protected void updateContainerEquipment() {
        if (!this.level().isClientSide) {
            super.updateContainerEquipment();
            this.setColor(getCarpetColor(this.inventory.getItem(2)));
        }
        this.setArmorStack(this.inventory.getItem(1));
        this.entityData.set(LEFT_CRATE, this.inventory.getItem(3));
        this.entityData.set(RIGHT_CRATE, this.inventory.getItem(4));
    }

    private void setArmorStack(@Nonnull ItemStack stack) {
        ArmorType armorType = ArmorType.getByItemStack(stack);
        this.entityData.set(ARMOR_STACK, stack);

        if (!this.level().isClientSide) {
            AttributeInstance armor = this.getAttribute(Attributes.ARMOR);
            if (armor != null) {
                armor.removeModifier(ARMOR_MODIFIER_UUID);
                int protection = armorType.getProtection();
                if (protection != 0) {
                    armor.addTransientModifier((new AttributeModifier(ARMOR_MODIFIER_UUID, "Camel armor bonus", protection, AttributeModifier.Operation.ADDITION)));
                }
            }
        }
    }

    @Nonnull
    public ItemStack getArmor() {
        return this.entityData.get(ARMOR_STACK);
    }

    @Nullable
    private static DyeColor getCarpetColor(ItemStack stack) {
        Block block = Block.byItem(stack.getItem());
        return block instanceof WoolCarpetBlock ? ((WoolCarpetBlock) block).getColor() : null;
    }

    public boolean isValidCarpet(@Nonnull ItemStack stack) {
        return stack.is(ItemTags.WOOL_CARPETS) || Block.byItem(stack.getItem()) instanceof LinenCarpetBlock;
    }

    @Override
    public boolean isArmor(@Nonnull ItemStack stack) {
        return ArmorType.isArmor(stack);
    }

    @Override
    public boolean canWearArmor() {
        return true;
    }

    @Override
    public void openCustomInventoryScreen(@Nonnull Player player) {
        if (!this.level().isClientSide && (!this.isVehicle() || this.hasPassenger(player)) && this.isTamed()) {
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.openMenu(this, buf -> buf.writeInt(this.getId()));
            }
        }
    }

    public SimpleContainer getCamelCrate() {
        return this.inventory;
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());

        if (!this.inventory.getItem(1).isEmpty()) {
            compound.put("ArmorItem", this.inventory.getItem(1).save(new CompoundTag()));
        }
        if (!this.inventory.getItem(2).isEmpty()) {
            compound.put("Carpet", this.inventory.getItem(2).save(new CompoundTag()));
        }
        if (!this.inventory.getItem(3).isEmpty()) {
            compound.put("CrateLeft", this.inventory.getItem(3).save(new CompoundTag()));
        }
        if (!this.inventory.getItem(4).isEmpty()) {
            compound.put("CrateRight", this.inventory.getItem(4).save(new CompoundTag()));
        }

        if (this.hasLeftCrate()) {
            ListTag tagList = new ListTag();
            for (int slot = this.getNonCrateSize(); slot < this.inventory.getContainerSize(); ++slot) {
                ItemStack slotStack = this.inventory.getItem(slot);
                if (!slotStack.isEmpty()) {
                    CompoundTag tagCompound = new CompoundTag();
                    tagCompound.putByte("Slot", (byte) slot);
                    slotStack.save(tagCompound);
                    tagList.add(tagCompound);
                }
            }
            compound.put("Items", tagList);
        }
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));

        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.getCamelMaxHealth());

        if (compound.contains("Carpet", 10)) {
            this.inventory.setItem(2, ItemStack.of(compound.getCompound("Carpet")));
        }
        if (compound.contains("ArmorItem", 10)) {
            ItemStack armorStack = ItemStack.of(compound.getCompound("ArmorItem"));
            if (!armorStack.isEmpty() && isArmor(armorStack)) {
                this.inventory.setItem(1, armorStack);
            }
        }
        if (compound.contains("CrateLeft", 10)) {
            this.inventory.setItem(3, ItemStack.of(compound.getCompound("CrateLeft")));
        }
        if (compound.contains("CrateRight", 10)) {
            this.inventory.setItem(4, ItemStack.of(compound.getCompound("CrateRight")));
        }

        if (this.hasLeftCrate()) {
            ListTag tagList = compound.getList("Items", 10);
            this.createInventory();
            for (int i = 0; i < tagList.size(); ++i) {
                CompoundTag tagCompound = tagList.getCompound(i);
                int slot = tagCompound.getByte("Slot") & 255;
                if (slot >= this.getNonCrateSize() && slot < this.inventory.getContainerSize()) {
                    this.inventory.setItem(slot, ItemStack.of(tagCompound));
                }
            }
        }
        this.updateContainerEquipment();
    }

    @Override
    public void containerChanged(@Nonnull Container invBasic) {
        this.updateContainerEquipment();
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
        return !this.entityData.get(LEFT_CRATE).isEmpty();
    }

    public boolean hasRightCrate() {
        return !this.entityData.get(RIGHT_CRATE).isEmpty();
    }

    @Override
    @Nonnull
    public InteractionResult mobInteract(Player player, @Nonnull InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);

        if (heldStack.getItem() instanceof SpawnEggItem) {
            return super.mobInteract(player, hand);
        } else {
            if (!heldStack.isEmpty()) {
                boolean eating = this.handleEating(player, heldStack);

                if (!eating && !this.isTamed()) {
                    if (heldStack.interactLivingEntity(player, this, hand).consumesAction()) {
                        return InteractionResult.SUCCESS;
                    }
                    this.makeMad();
                    return InteractionResult.SUCCESS;
                }

                if (!eating && (!this.hasLeftCrate() || !this.hasRightCrate()) && Block.byItem(heldStack.getItem()) instanceof CrateBlock) {
                    this.openCustomInventoryScreen(player);
                    return InteractionResult.SUCCESS;
                }
                if (!eating && this.getArmor().isEmpty() && this.isArmor(heldStack)) {
                    this.openCustomInventoryScreen(player);
                    return InteractionResult.SUCCESS;
                }
                if (!eating && this.inventory.getItem(2).isEmpty() && this.isValidCarpet(heldStack)) {
                    this.openCustomInventoryScreen(player);
                    return InteractionResult.SUCCESS;
                }
                if (!eating && !this.isBaby() && !this.isSaddled() && heldStack.getItem() instanceof SaddleItem) {
                    this.openCustomInventoryScreen(player);
                    return InteractionResult.SUCCESS;
                }
                if (!eating && heldStack.getItem() == Items.BUCKET && !this.isBaby() && this.isTamed()) {
                    heldStack.shrink(1);
                    player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
                    if (heldStack.isEmpty()) {
                        player.setItemInHand(hand, new ItemStack(Items.MILK_BUCKET));
                    } else if (!player.getInventory().add(new ItemStack(Items.MILK_BUCKET))) {
                        player.drop(new ItemStack(Items.MILK_BUCKET), false);
                    }
                    return InteractionResult.SUCCESS;
                }

                InteractionResult actionresulttype = heldStack.interactLivingEntity(player, this, hand);
                if (actionresulttype.consumesAction()) {
                    return actionresulttype;
                }

                if (eating) {
                    if (!player.getAbilities().instabuild) {
                        heldStack.shrink(1);
                    }
                    return InteractionResult.SUCCESS;
                }
            }

            if (!this.isBaby()) {
                if (this.isTamed() && player.isSecondaryUseActive()) {
                    this.openCustomInventoryScreen(player);
                    return InteractionResult.sidedSuccess(this.level().isClientSide);
                }

                if (this.isVehicle()) {
                    return super.mobInteract(player, hand);
                }
            }

            if (this.isBaby()) {
                return super.mobInteract(player, hand);
            } else {
                if (this.getPassengers().size() < 2 && !this.isBaby()) {
                    this.doPlayerRide(player);
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
        }
    }

    @Override
    protected boolean handleEating(@Nonnull Player player, @Nonnull ItemStack stack) {
        boolean isEating = false;
        float healAmount = 0.0F;
        int growthAmount = 0;
        int temperAmount = 0;
        Item item = stack.getItem();

        if (stack.is(Tags.Items.CROPS_WHEAT)) {
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
        } else if (item == AtumItems.DATE.get() || item == Items.CACTUS) {
            healAmount = 3.0F;
            growthAmount = 60;
            temperAmount = 3;
            if (this.isTamed() && this.getAge() == 0 && !this.isInLove()) {
                isEating = true;
                this.setInLove(player);
            }
        } else if (item == Items.GOLDEN_CARROT) {
            healAmount = 4.0F;
            growthAmount = 60;
            temperAmount = 5;
            if (this.isTamed() && this.getAge() == 0 && !this.isInLove()) {
                isEating = true;
                this.setInLove(player);
            }
        } else if (item == Items.GOLDEN_APPLE || item == AtumItems.GOLDEN_DATE.get() || item == AtumItems.ENCHANTED_GOLDEN_DATE.get()) {
            healAmount = 10.0F;
            growthAmount = 240;
            temperAmount = 10;

            if (!this.isTamed()) {
                this.tameWithName(player);
            } else if (this.getAge() == 0 && !this.isInLove()) {
                isEating = true;
                this.setInLove(player);
            }
        }
        if (this.getHealth() < this.getMaxHealth() && healAmount > 0.0F) {
            this.heal(healAmount);
            isEating = true;
        }
        if (this.isBaby() && growthAmount > 0) {
            this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getY() + 0.5D + (double) (this.random.nextFloat() * this.getBbHeight()), this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), 0.0D, 0.0D, 0.0D);

            if (!this.level().isClientSide) {
                this.ageUp(growthAmount);
            }
            isEating = true;
        }

        if (temperAmount > 0 && (isEating || !this.isTamed()) && this.getTemper() < this.getMaxTemper()) {
            isEating = true;
            if (!this.level().isClientSide) {
                this.modifyTemper(temperAmount);
            }
        }
        if (isEating) {
            this.eatingCamel();
        }
        return isEating;
    }

    @Override
    public boolean isFood(@Nonnull ItemStack stack) {
        return TEMPTATION_ITEM.test(stack);
    }

    private void eatingCamel() {
        if (!this.isSilent()) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LLAMA_EAT, this.getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
        }
    }

    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.getCamelMaxHealth());
        this.heal(this.getCamelMaxHealth());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowID, @Nonnull Inventory playerInventory, @Nonnull Player player) {
        return new CamelContainer(windowID, playerInventory, this.getId());
    }

    static class SpitGoal extends HurtByTargetGoal {

        public SpitGoal(CamelEntity camel) {
            super(camel);
        }

        @Override
        public boolean canContinueToUse() {
            if (this.mob instanceof CamelEntity camel) {
                if (camel.didSpit) {
                    camel.setDidSpit(false);
                    return false;
                }
            }
            return super.canContinueToUse();
        }
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
            if (item == AtumItems.CAMEL_IRON_ARMOR.get()) {
                return IRON;
            } else if (item == AtumItems.CAMEL_GOLD_ARMOR.get()) {
                return GOLD;
            } else if (item == AtumItems.CAMEL_DIAMOND_ARMOR.get()) {
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
package com.teammetallurgy.atum.entity.undead;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.event.AtumEvents;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.SarcophagusTileEntity;
import com.teammetallurgy.atum.entity.ai.goal.OpenAnyDoorGoal;
import com.teammetallurgy.atum.entity.ai.goal.OrbAttackGoal;
import com.teammetallurgy.atum.entity.projectile.PharaohOrbEntity;
import com.teammetallurgy.atum.init.AtumEffects;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.items.tools.ScepterItem;
import com.teammetallurgy.atum.misc.AtumConfig;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.server.ServerLifecycleHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class PharaohEntity extends UndeadBaseEntity implements RangedAttackMob {
    private static final String[] PREFIXES = {"Ama", "Ata", "Ato", "Bak", "Cal", "Djet", "Eje", "For", "Gol", "Gut", "Hop", "Hor", "Huni", "Iam", "Jor", "Kal", "Khas", "Khor", "Lat", "Mal", "Not", "Oap", "Pra", "Qo", "Ras", "Shas", "Thoth", "Tui", "Uld", "Ver", "Wot", "Xo", "Yat", "Zyt", "Khep"};
    private static final String[] SUFFIXES = {"Ahat", "Amesh", "Amon", "Anut", "Baroom", "Chanta", "Erant", "Funam", "Daresh", "Djer", "Hotesh", "Khaden", "Kron", "Gorkum", "Ialenter", "Ma'at", "Narmer", "Radeem", "Jaloom", "Lepsha", "Quor", "Oleshet", "Peput", "Talat", "Ulam", "Veresh", "Ranesh", "Snef", "Wollolo", "Hathor", "Intef", "Neferk", "Khatne", "Tepy", "Moret"};
    private static final String[] NUMERALS = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII", "XIII", "XIV", "XV"};
    private static final EntityDataAccessor<Integer> PREFIX = SynchedEntityData.defineId(PharaohEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SUFFIX = SynchedEntityData.defineId(PharaohEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> NUMERAL = SynchedEntityData.defineId(PharaohEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Optional<BlockPos>> SARCOPHAGUS_POS = SynchedEntityData.defineId(PharaohEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    private static final EntityDataAccessor<Boolean> DROP_GOD_SPECIFIC_LOOT = SynchedEntityData.defineId(PharaohEntity.class, EntityDataSerializers.BOOLEAN);
    private final ServerBossEvent bossInfo = (ServerBossEvent) new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.YELLOW, BossEvent.BossBarOverlay.NOTCHED_10).setCreateWorldFog(true);
    private final OrbAttackGoal<PharaohEntity> aiOrbAttack = new OrbAttackGoal<>(this, 1.2D, 20, 24.0F);
    private final MeleeAttackGoal aiAttackOnCollide = new MeleeAttackGoal(this, 1.2D, true) {
        @Override
        public void stop() {
            super.stop();
            PharaohEntity.this.setAggressive(false);
        }

        @Override
        public void start() {
            super.start();
            PharaohEntity.this.setAggressive(true);
        }
    };
    private int stage;
    private int suffixID = 0;
    private int prefixID = 0;
    private int numID = 0;
    private int regenTime = 0;
    private String texturePath;
    private boolean dropsGodSpecificLoot;

    public PharaohEntity(EntityType<? extends PharaohEntity> entityType, Level world) {
        super(entityType, world);
        this.setHealth(this.getMaxHealth());
        this.xpReward = 250;
        this.stage = 0;
        this.setCanPickUpLoot(false);
        this.setCombatTask();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    protected boolean hasSkinVariants() {
        return true;
    }

    @Override
    protected int getVariantAmount() {
        return God.values().length - 1;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new OpenAnyDoorGoal(this, false, true));
    }

    @Override
    protected void applyEntityAI() {
        super.applyEntityAI();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 300.0D).add(Attributes.MOVEMENT_SPEED, 0.3D).add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.FOLLOW_RANGE, 36.0D).add(Attributes.ARMOR, 10.0F);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PREFIX, 0);
        this.entityData.define(SUFFIX, 0);
        this.entityData.define(NUMERAL, 0);
        this.entityData.define(SARCOPHAGUS_POS, Optional.empty());
        this.entityData.define(DROP_GOD_SPECIFIC_LOOT, false);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VINDICATOR_AMBIENT;
    }

    @Override
    protected boolean shouldBurnInDay() {
        return false;
    }

    @Override
    protected int calculateFallDamage(float distance, float damageMultiplier) {
        return 0;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randomSource, @Nonnull DifficultyInstance difficulty) {
        Item scepter = ScepterItem.getScepter(God.getGod(getVariant()));
        if (scepter != null) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(scepter));
        }
    }

    @Override
    public void setItemSlot(@Nonnull EquipmentSlot slot, @Nonnull ItemStack stack) {
        super.setItemSlot(slot, stack);
        if (!this.level.isClientSide) {
            this.setCombatTask();
        }
    }

    @Override
    public boolean canPickUpLoot() {
        return false;
    }

    @Override
    protected void dropCustomDeathLoot(@Nonnull DamageSource source, int looting, boolean recentlyHit) {
        //Don't drop equipment
    }

    @Override
    @Nonnull
    protected ResourceLocation getDefaultLootTable() {
        if (this.dropsGodSpecificLoot()) {
            return new ResourceLocation(Atum.MOD_ID, "gods/" + God.getGod(this.getVariant()).getName());
        } else {
            return AtumLootTables.GODS_ALL;
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public String getTexture() {
        if (this.texturePath == null) {
            this.texturePath = new ResourceLocation(Atum.MOD_ID, "textures/entity/pharaoh_" + God.getGod(this.getVariant()).getName()) + ".png";
        }
        return this.texturePath;
    }

    @Override
    protected void setVariant(int variant) {
        super.setVariant(variant);
        this.texturePath = null;
    }

    @Override
    protected void setVariantAbilities(DifficultyInstance difficulty, RandomSource randomSource, int variant) {
        super.setVariantAbilities(difficulty, randomSource, variant);

        prefixID = random.nextInt(PREFIXES.length);
        suffixID = random.nextInt(SUFFIXES.length);
        numID = random.nextInt(NUMERALS.length);

        this.setPharaohName(this.prefixID, this.suffixID, this.numID);

        this.populateDefaultEquipmentSlots(randomSource, difficulty);
        this.populateDefaultEquipmentEnchantments(randomSource, difficulty);
    }

    @Override
    public SpawnGroupData finalizeSpawn(@Nonnull ServerLevelAccessor world, @Nonnull DifficultyInstance difficulty, @Nonnull MobSpawnType spawnReason, @Nullable SpawnGroupData livingData, @Nullable CompoundTag nbt) {
        this.setCombatTask();
        return super.finalizeSpawn(world, difficulty, spawnReason, livingData, nbt);
    }

    private void setCombatTask() {
        if (this.level != null && !this.level.isClientSide) {
            this.goalSelector.removeGoal(this.aiAttackOnCollide);
            this.goalSelector.removeGoal(this.aiOrbAttack);
            ItemStack heldItem = this.getItemInHand(InteractionHand.MAIN_HAND);
            if (heldItem.getItem() instanceof ScepterItem) {
                int cooldown = 18;
                if (this.level.getDifficulty() != Difficulty.HARD) {
                    cooldown = 32;
                }
                this.aiOrbAttack.setAttackCooldown(cooldown);
                this.goalSelector.addGoal(4, this.aiOrbAttack);
            } else {
                this.goalSelector.addGoal(4, this.aiAttackOnCollide);
            }
        }
    }

    @Override
    public void performRangedAttack(@Nonnull LivingEntity target, float distanceFactor) {
        PharaohOrbEntity orb = new PharaohOrbEntity(this.level, this, God.getGod(this.getVariant()));
        double x = target.getX() - this.getX();
        double y = target.getY(0.3333333333333333D) - orb.getY();
        double z = target.getZ() - this.getZ();
        double height = Mth.sqrt((float) (x * x + z * z));
        orb.shoot(x, y + height * 0.2D, z, 1.6F, 1);
        this.playSound(SoundEvents.GHAST_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(orb);
    }

    public BlockPos getSarcophagusPos() {
        return this.entityData.get(SARCOPHAGUS_POS).orElse(null);
    }

    public void setSarcophagusPos(BlockPos pos) {
        this.entityData.set(SARCOPHAGUS_POS, Optional.ofNullable(pos));
    }

    public boolean dropsGodSpecificLoot() {
        return this.dropsGodSpecificLoot;
    }

    public void setDropsGodSpecificLoot(boolean dropsGodSpecificLoot) {
        this.dropsGodSpecificLoot = dropsGodSpecificLoot;
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    public void die(@Nonnull DamageSource source) {
        if (!this.level.isClientSide) {
            BlockPos sarcophagusPos = getSarcophagusPos();
            if (sarcophagusPos != null) {
                BlockEntity tileEntity = this.level.getBlockEntity(sarcophagusPos);
                if (tileEntity instanceof SarcophagusTileEntity) {
                    ((SarcophagusTileEntity) tileEntity).setOpenable();
                    for (Direction horizontal : Direction.Plane.HORIZONTAL) {
                        BlockEntity tileEntityOffset = this.level.getBlockEntity(sarcophagusPos.relative(horizontal));
                        if (tileEntityOffset instanceof SarcophagusTileEntity) {
                            ((SarcophagusTileEntity) tileEntityOffset).setOpenable();
                        }
                    }
                    AtumEvents.onPharaohBeaten(this, source);
                } else {
                    Atum.LOG.error("Unable to find sarcophagus coordinates for " + this.getName() + " on " + sarcophagusPos);
                }
            }
        }

        if (AtumConfig.MOBS.displayPharaohSlainMessage.get()) {
            if (source.msgId.equals("player")) {
                Player slayer = (Player) source.getEntity();
                if (!this.level.isClientSide && slayer != null) {
                    List<ServerPlayer> players = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers(); //TODO Might not be needed, sendSystemMessage might send to all players?
                    for (Player player : players) {
                        player.sendSystemMessage(this.getName().copy().append(" ").append(Component.translatable("chat.atum.kill_pharaoh")).append(" " + slayer.getGameProfile().getName()).setStyle(this.getName().getStyle().withColor(God.getGod(this.getVariant()).getColor())));
                    }
                }
            }
        }
        Entity killer = source.getEntity();
        if (killer instanceof Player) {
            ((Player) killer).addEffect(new MobEffectInstance(AtumEffects.MARKED_FOR_DEATH.get(), 2400, 0, false, false, true));
        }
        super.die(source);
    }

    @Override
    public void startSeenByPlayer(@Nonnull ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(@Nonnull ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    @Nonnull
    public Component getName() {
        int p = this.entityData.get(PREFIX);
        int s = this.entityData.get(SUFFIX);
        int n = this.entityData.get(NUMERAL);
        return Component.translatable(this.getType().getDescriptionId()).append(" ").append(Component.translatable("entity.atum.pharaoh." + PREFIXES[p])).append(Component.translatable("entity.atum.pharaoh." + SUFFIXES[s].toLowerCase(Locale.ROOT))).append(" " + NUMERALS[n]);
    }


    @Override
    public void knockback(double strength, double xRatio, double zRatio) {
        if (God.getGod(this.getVariant()) != God.PTAH) {
            strength *= 0.20F;
            super.knockback(strength, xRatio, zRatio);
        }
    }

    @Override
    public boolean hurt(@Nonnull DamageSource source, float amount) {
        if (super.hurt(source, amount)) {
            if (this.getHealth() < this.getMaxHealth() * 0.75F && stage == 0) {
                stage++;
                spawnGuards(this.blockPosition());
            } else if (stage == 1 && this.getHealth() < this.getMaxHealth() * 0.5F) {
                stage++;
                spawnGuards(this.blockPosition());
            } else if (stage == 2 && this.getHealth() < this.getMaxHealth() * 0.25F) {
                stage++;
                spawnGuards(this.blockPosition());
            }
            return true;
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level.isClientSide) {
            this.setBossInfo(this.getVariant());
        }

        if (this.level.getDifficulty() == Difficulty.PEACEFUL) {
            if (this.getSarcophagusPos() != null) {
                BlockEntity te = this.level.getBlockEntity(this.getSarcophagusPos());
                if (te instanceof SarcophagusTileEntity) {
                    ((SarcophagusTileEntity) te).hasSpawned = false;
                }
            }
            this.discard();
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public void aiStep() {
        if (regenTime++ > 60) {
            regenTime = 0;
            this.heal(God.getGod(this.getVariant()) == God.OSIRIS ? 6 : 1);
        }
        super.aiStep();
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("prefix", prefixID);
        compound.putInt("suffix", suffixID);
        compound.putInt("numeral", numID);
        compound.putBoolean("dropGodSpecificLoot", this.dropsGodSpecificLoot());
        BlockPos sarcophagusPos = getSarcophagusPos();
        if (sarcophagusPos != null) {
            compound.putInt("sarcophagus_x", sarcophagusPos.getX());
            compound.putInt("sarcophagus_y", sarcophagusPos.getY());
            compound.putInt("sarcophagus_z", sarcophagusPos.getZ());
        }
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.prefixID = compound.getInt("prefix");
        this.suffixID = compound.getInt("suffix");
        this.numID = compound.getInt("numeral");
        this.dropsGodSpecificLoot = compound.getBoolean("dropGodSpecificLoot");
        if (compound.contains("sarcophagus_x")) {
            int x = compound.getInt("sarcophagus_x");
            int y = compound.getInt("sarcophagus_y");
            int z = compound.getInt("sarcophagus_z");
            this.entityData.set(SARCOPHAGUS_POS, Optional.of(new BlockPos(x, y, z)));
        } else {
            this.entityData.set(SARCOPHAGUS_POS, Optional.empty());
        }
        this.setPharaohName(compound.getInt("prefix"), compound.getInt("suffix"), compound.getInt("numeral"));
        this.setCombatTask();
    }

    private void setPharaohName(int prefix, int suffix, int numeral) {
        this.entityData.set(PREFIX, prefix);
        this.entityData.set(SUFFIX, suffix);
        this.entityData.set(NUMERAL, numeral);
    }

    public void spawnGuards(BlockPos pos) {
        Direction facing = Direction.from2DDataValue(Mth.floor(this.getYRot() * 4.0F / 360.0F + 0.5D) & 3).getOpposite();
        this.trySpawnMummy(pos, facing);
        this.trySpawnMummy(pos, facing.getClockWise().getClockWise());
    }

    private void trySpawnMummy(BlockPos pos, Direction facing) {
        BlockPos base = pos.relative(facing);

        if (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, this.level, base, AtumEntities.MUMMY.get())) {
            MummyEntity mummy = AtumEntities.MUMMY.get().create(this.level);

            if (mummy != null) {
                if (this.level instanceof ServerLevel) {
                    mummy.finalizeSpawn((ServerLevelAccessor) level, this.level.getCurrentDifficultyAt(base), MobSpawnType.TRIGGERED, null, null);
                    mummy.moveTo(base.getX() + 0.5D, base.getY(), base.getZ() + 0.5D, 0.0F, 0.0F);

                    if (!level.isClientSide) {
                        this.level.addFreshEntity(mummy);
                    }
                }
                mummy.spawnAnim();
                return;
            }
        }

        for (Direction offset : Direction.Plane.HORIZONTAL) {
            BlockPos newPos = base.relative(offset);
            if (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, this.level, newPos, AtumEntities.MUMMY.get())) {
                MummyEntity mummy = AtumEntities.MUMMY.get().create(this.level);
                if (mummy != null) {
                    if (this.level instanceof ServerLevel) {
                        mummy.finalizeSpawn((ServerLevelAccessor) level, this.level.getCurrentDifficultyAt(base), MobSpawnType.TRIGGERED, null, null);
                        mummy.moveTo(newPos.getX() + 0.5D, newPos.getY(), newPos.getZ() + 0.5D, this.random.nextFloat() * 360.0F, 0.0F);

                        if (!this.level.isClientSide) {
                            this.level.addFreshEntity(mummy);
                        }
                    }
                    mummy.spawnAnim();
                    return;
                }
            }
        }
    }

    private void setBossInfo(int variant) {
        this.bossInfo.setName(this.getDisplayName().copy().setStyle(this.getDisplayName().getStyle().withColor(God.getGod(variant).getColor())));
    }
}
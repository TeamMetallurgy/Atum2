package com.teammetallurgy.atum.entity.undead;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.event.AtumEvents;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.SarcophagusTileEntity;
import com.teammetallurgy.atum.entity.ai.goal.OpenAnyDoorGoal;
import com.teammetallurgy.atum.init.AtumEffects;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.items.artifacts.horus.HorusAscensionItem;
import com.teammetallurgy.atum.items.tools.ScepterItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class PharaohEntity extends UndeadBaseEntity {
    private static final String[] PREFIXES = {"Ama", "Ata", "Ato", "Bak", "Cal", "Djet", "Eje", "For", "Gol", "Gut", "Hop", "Hor", "Huni", "Iam", "Jor", "Kal", "Khas", "Khor", "Lat", "Mal", "Not", "Oap", "Pra", "Qo", "Ras", "Shas", "Thoth", "Tui", "Uld", "Ver", "Wot", "Xo", "Yat", "Zyt", "Khep"};
    private static final String[] SUFFIXES = {"Ahat", "Amesh", "Amon", "Anut", "Baroom", "Chanta", "Erant", "Funam", "Daresh", "Djer", "Hotesh", "Khaden", "Kron", "Gorkum", "Ialenter", "Ma'at", "Narmer", "Radeem", "Jaloom", "Lepsha", "Quor", "Oleshet", "Peput", "Talat", "Ulam", "Veresh", "Ranesh", "Snef", "Wollolo", "Hathor", "Intef", "Neferk", "Khatne", "Tepy", "Moret"};
    private static final String[] NUMERALS = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII", "XIII", "XIV", "XV"};
    private static final DataParameter<Integer> PREFIX = EntityDataManager.createKey(PharaohEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> SUFFIX = EntityDataManager.createKey(PharaohEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> NUMERAL = EntityDataManager.createKey(PharaohEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Optional<BlockPos>> SARCOPHAGUS_POS = EntityDataManager.createKey(PharaohEntity.class, DataSerializers.OPTIONAL_BLOCK_POS);
    private final ServerBossInfo bossInfo = (ServerBossInfo) new ServerBossInfo(this.getDisplayName(), BossInfo.Color.YELLOW, BossInfo.Overlay.NOTCHED_10).setCreateFog(true);
    private int stage;
    private int suffixID = 0;
    private int prefixID = 0;
    private int numID = 0;
    private int regenTime = 0;
    private int berserkTimer;
    private float berserkDamage;
    private String texturePath;

    public PharaohEntity(EntityType<? extends PharaohEntity> entityType, World world) {
        super(entityType, world);
        this.setHealth(this.getMaxHealth());
        this.experienceValue = 250;
        this.stage = 0;
        this.setCanPickUpLoot(false);
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
        this.goalSelector.addGoal(1, new OpenAnyDoorGoal(this, false));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
    }

    @Override
    protected void applyEntityAI() {
        super.applyEntityAI();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    public static AttributeModifierMap.MutableAttribute getAttributes() {
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 300.0D).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3D).createMutableAttribute(Attributes.ATTACK_DAMAGE, 8.0D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 36.0D).createMutableAttribute(Attributes.ARMOR, 10.0F);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(PREFIX, 0);
        this.dataManager.register(SUFFIX, 0);
        this.dataManager.register(NUMERAL, 0);
        this.dataManager.register(SARCOPHAGUS_POS, Optional.empty());
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_VINDICATOR_AMBIENT;
    }

    @Override
    protected boolean shouldBurnInDay() {
        return false;
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(@Nonnull DifficultyInstance difficulty) {
        ScepterItem scepter = ScepterItem.getScepter(God.getGod(getVariant()));
        if (scepter != null) {
            this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(scepter));
        }
    }

    @Override
    public boolean canPickUpLoot() {
        return false;
    }

    @Override
    protected void dropSpecialItems(@Nonnull DamageSource source, int looting, boolean recentlyHit) {
        //Don't drop equipment
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
    protected void setVariantAbilities(DifficultyInstance difficulty, int variant) {
        super.setVariantAbilities(difficulty, variant);

        prefixID = rand.nextInt(PREFIXES.length);
        suffixID = rand.nextInt(SUFFIXES.length);
        numID = rand.nextInt(NUMERALS.length);

        this.setPharaohName(this.prefixID, this.suffixID, this.numID);

        this.setEquipmentBasedOnDifficulty(difficulty);
        this.setEnchantmentBasedOnDifficulty(difficulty);
    }

    public BlockPos getSarcophagusPos() {
        return this.dataManager.get(SARCOPHAGUS_POS).orElse(null);
    }

    public void setSarcophagusPos(BlockPos pos) {
        this.dataManager.set(SARCOPHAGUS_POS, Optional.ofNullable(pos));
    }

    @Override
    public boolean isNonBoss() {
        return false;
    }

    @Override
    public boolean preventDespawn() {
        return true;
    }

    @Override
    public void onDeath(@Nonnull DamageSource source) {
        if (!this.world.isRemote) {
            BlockPos sarcophagusPos = getSarcophagusPos();
            if (sarcophagusPos != null) {
                TileEntity tileEntity = this.world.getTileEntity(sarcophagusPos);
                if (tileEntity instanceof SarcophagusTileEntity) {
                    ((SarcophagusTileEntity) tileEntity).setOpenable();
                    for (Direction horizontal : Direction.Plane.HORIZONTAL) {
                        TileEntity tileEntityOffset = this.world.getTileEntity(sarcophagusPos.offset(horizontal));
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

        if (source.damageType.equals("player")) {
            PlayerEntity slayer = (PlayerEntity) source.getTrueSource();
            if (!this.world.isRemote && slayer != null) {
                List<ServerPlayerEntity> players = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers();
                for (PlayerEntity player : players) {
                    player.sendMessage(this.getName().deepCopy().appendString(" ").append(new TranslationTextComponent("chat.atum.kill_pharaoh")).appendString(" " + slayer.getGameProfile().getName()).mergeStyle(God.getGod(this.getVariant()).getColor()), Util.DUMMY_UUID);
                }
            }
        }
        Entity killer = source.getTrueSource();
        if (killer instanceof PlayerEntity) {
            ((PlayerEntity) killer).addPotionEffect(new EffectInstance(AtumEffects.MARKED_FOR_DEATH, 2400, 0, false, false, true));
        }
        super.onDeath(source);
    }

    @Override
    public void addTrackingPlayer(@Nonnull ServerPlayerEntity player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(@Nonnull ServerPlayerEntity player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    @Nonnull
    public ITextComponent getName() {
        int p = this.dataManager.get(PREFIX);
        int s = this.dataManager.get(SUFFIX);
        int n = this.dataManager.get(NUMERAL);
        return new TranslationTextComponent(this.getType().getTranslationKey()).appendString(" ").append(new TranslationTextComponent("entity.atum.pharaoh." + PREFIXES[p])).append(new TranslationTextComponent("entity.atum.pharaoh." + SUFFIXES[s].toLowerCase(Locale.ROOT))).appendString(" " + NUMERALS[n]);
    }

    @Override
    public void applyKnockback(float strength, double xRatio, double zRatio) {
        if (God.getGod(this.getVariant()) != God.PTAH) {
            strength *= 0.20F;
            super.applyKnockback(strength, xRatio, zRatio);
        }
    }

    @Override
    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        if (super.attackEntityFrom(source, amount)) {
            if (this.getHealth() < this.getMaxHealth() * 0.75F && stage == 0) {
                stage++;
                spawnGuards(this.getPosition());
            } else if (stage == 1 && this.getHealth() < this.getMaxHealth() * 0.5F) {
                stage++;
                spawnGuards(this.getPosition());
            } else if (stage == 2 && this.getHealth() < this.getMaxHealth() * 0.25F) {
                stage++;
                spawnGuards(this.getPosition());
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean attackEntityAsMob(@Nonnull Entity entity) {
        if (!super.attackEntityAsMob(entity)) {
            return false;
        } else {
            if (entity instanceof LivingEntity && !this.world.isRemote) {
                LivingEntity entityLiving = (LivingEntity) entity;
                switch (God.getGod(this.getVariant())) {
                    case ANPUT:
                        entityLiving.addPotionEffect(new EffectInstance(Effects.HUNGER, 80, 1));
                        break;
                    case ANUBIS:
                        entityLiving.addPotionEffect(new EffectInstance(Effects.WITHER, 60, 1));
                        break;
                    case GEB:
                        entityLiving.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 60, 1));
                        break;
                    case HORUS:
                        entityLiving.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 60, 1));
                        break;
                    case NUIT:
                        entityLiving.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 60));
                        break;
                    case RA:
                        entityLiving.setFire(4);
                        break;
                    case SETH:
                        entityLiving.addPotionEffect(new EffectInstance(Effects.POISON, 100, 1));
                        break;
                    case SHU:
                        HorusAscensionItem.knockUp(entityLiving, this, rand);
                        break;
                    case TEFNUT:
                        entityLiving.addPotionEffect(new EffectInstance(Effects.NAUSEA, 60));
                        break;
                    default:
                        break;
                }
            }
            return true;
        }
    }

    @SubscribeEvent
    public void onBerserk(LivingHurtEvent event) {
        if (event.getSource().getTrueSource() == this && God.getGod(this.getVariant()) == God.MONTU) {
            if (this.berserkTimer == 0) {
                event.setAmount(event.getAmount());
                this.berserkDamage = (event.getAmount() / 10) + event.getAmount();
                this.berserkTimer = 80;
            } else {
                this.berserkDamage = this.berserkDamage + (event.getAmount() / 10);
                event.setAmount(this.berserkDamage);
                this.berserkTimer = 80;
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.world.isRemote) {
            this.setBossInfo(this.getVariant());
        }

        if (this.world.getDifficulty().getId() == 0) {
            if (this.getSarcophagusPos() != null) {
                TileEntity te = this.world.getTileEntity(this.getSarcophagusPos());
                if (te instanceof SarcophagusTileEntity) {
                    ((SarcophagusTileEntity) te).hasSpawned = false;
                }
            }
            this.remove();
        }
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public void livingTick() {
        if (regenTime++ > 60) {
            regenTime = 0;
            this.heal(God.getGod(this.getVariant()) == God.ISIS ? 2 : 1);
        }
        if (God.getGod(this.getVariant()) == God.MONTU && this.berserkTimer > 1) {
            this.berserkTimer--;
        }
        if (this.berserkTimer == 1) {
            this.berserkDamage = 0;
            this.berserkTimer = 0;
        }
        super.livingTick();
    }

    @Override
    public void writeAdditional(@Nonnull CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("prefix", prefixID);
        compound.putInt("suffix", suffixID);
        compound.putInt("numeral", numID);
        BlockPos sarcophagusPos = getSarcophagusPos();
        if (sarcophagusPos != null) {
            compound.putInt("sarcophagus_x", sarcophagusPos.getX());
            compound.putInt("sarcophagus_y", sarcophagusPos.getY());
            compound.putInt("sarcophagus_z", sarcophagusPos.getZ());
        }
    }

    @Override
    public void readAdditional(@Nonnull CompoundNBT compound) {
        super.readAdditional(compound);
        prefixID = compound.getInt("prefix");
        suffixID = compound.getInt("suffix");
        numID = compound.getInt("numeral");
        if (compound.contains("sarcophagus_x")) {
            int x = compound.getInt("sarcophagus_x");
            int y = compound.getInt("sarcophagus_y");
            int z = compound.getInt("sarcophagus_z");
            this.dataManager.set(SARCOPHAGUS_POS, Optional.of(new BlockPos(x, y, z)));
        } else {
            this.dataManager.set(SARCOPHAGUS_POS, Optional.empty());
        }
        this.setPharaohName(compound.getInt("prefix"), compound.getInt("suffix"), compound.getInt("numeral"));
    }

    private void setPharaohName(int prefix, int suffix, int numeral) {
        this.dataManager.set(PREFIX, prefix);
        this.dataManager.set(SUFFIX, suffix);
        this.dataManager.set(NUMERAL, numeral);
    }

    public void spawnGuards(BlockPos pos) {
        Direction facing = Direction.byHorizontalIndex(MathHelper.floor(this.rotationYaw * 4.0F / 360.0F + 0.5D) & 3).getOpposite();
        this.trySpawnMummy(pos, facing);
        this.trySpawnMummy(pos, facing.rotateY().rotateY());
    }

    private void trySpawnMummy(BlockPos pos, Direction facing) {
        BlockPos base = pos.offset(facing, 1);

        BlockState state = world.getBlockState(pos);
        if (WorldEntitySpawner.func_234968_a_(this.world, base, state, state.getFluidState(), AtumEntities.MUMMY)) {
            MummyEntity mummy = AtumEntities.MUMMY.create(this.world);

            if (mummy != null) {
                if (this.world instanceof ServerWorld) {
                    mummy.onInitialSpawn((IServerWorld) world, this.world.getDifficultyForLocation(base), SpawnReason.TRIGGERED, null, null);
                    mummy.setLocationAndAngles(base.getX(), base.getY(), base.getZ(), this.rand.nextFloat() * 360.0F, 0.0F);

                    if (!world.isRemote) {
                        this.world.addEntity(mummy);
                    }
                }
                mummy.spawnExplosionParticle();
                return;
            }
        }

        for (int i = 0; i < 4; i++) {
            Direction offset = Direction.byHorizontalIndex((5 - i) % 4); //[W, S, E, N]
            // Don't spawn the mummy on top of the pharaoh
            if (offset == facing.getOpposite()) continue;

            BlockPos newPos = base.offset(offset);
            state = this.world.getBlockState(newPos);
            if (WorldEntitySpawner.func_234968_a_(this.world, base, state, state.getFluidState(), AtumEntities.MUMMY)) {
                MummyEntity mummy = AtumEntities.MUMMY.create(this.world);
                if (mummy != null) {
                    if (this.world instanceof ServerWorld) {
                        mummy.onInitialSpawn((IServerWorld) world, this.world.getDifficultyForLocation(base), SpawnReason.TRIGGERED, null, null);
                        mummy.setLocationAndAngles(newPos.getX(), newPos.getY(), newPos.getZ(), this.rand.nextFloat() * 360.0F, 0.0F);

                        if (!this.world.isRemote) {
                            this.world.addEntity(mummy);
                        }
                    }
                    mummy.spawnExplosionParticle();
                    return;
                }
            }
        }
    }

    private void setBossInfo(int variant) {
        this.bossInfo.setName(this.getDisplayName().deepCopy().mergeStyle(God.getGod(variant).getColor()));
    }

    public enum God {
        ANPUT("anput", TextFormatting.BLACK),
        ANUBIS("anubis", TextFormatting.DARK_PURPLE),
        ATEM("atem", TextFormatting.DARK_AQUA),
        GEB("geb", TextFormatting.GOLD),
        HORUS("horus", TextFormatting.AQUA),
        ISIS("isis", TextFormatting.LIGHT_PURPLE),
        MONTU("montu", TextFormatting.DARK_RED),
        NUIT("nuit", TextFormatting.GRAY),
        PTAH("ptah", TextFormatting.YELLOW),
        RA("ra", TextFormatting.DARK_RED),
        SETH("seth", TextFormatting.GREEN),
        SHU("shu", TextFormatting.BLUE),
        TEFNUT("tefnut", TextFormatting.DARK_BLUE);

        static Map<Integer, God> MAP;
        private final String name;
        private final TextFormatting color;

        God(String name, TextFormatting color) {
            this.name = name;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public TextFormatting getColor() {
            return color;
        }

        public static God getGod(int godType) {
            if (MAP == null) {
                MAP = Maps.newHashMap();
                for (God g : God.values()) {
                    MAP.put(g.ordinal(), g);
                }
            }
            God god = MAP.get(godType);
            return god == null ? ANPUT : god;
        }
    }
}
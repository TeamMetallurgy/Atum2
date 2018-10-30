package com.teammetallurgy.atum.entity.undead;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.TileEntitySarcophagus;
import com.teammetallurgy.atum.items.ItemScepter;
import com.teammetallurgy.atum.items.artifacts.horus.ItemHorusAscension;
import com.teammetallurgy.atum.utils.AtumUtils;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.BlockStairs;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.*;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EntityPharaoh extends EntityUndeadBase {
    private static String[] prefixArray = {"Ama", "Ata", "Ato", "Bak", "Cal", "Djet", "Eje", "For", "Gol", "Gut", "Hop", "Hor", "Huni", "Iam", "Jor", "Kal", "Khas", "Khor", "Lat", "Mal", "Not", "Oap", "Pra", "Qo", "Ras", "Shas", "Thoth", "Tui", "Uld", "Ver", "Wot", "Xo", "Yat", "Zyt", "Khep"};
    private static String[] suffixArray = {"Ahat", "Amesh", "Amon", "Anut", "Baroom", "Chanta", "Erant", "Funam", "Daresh", "Djer", "Hotesh", "Khaden", "Kron", "Gorkum", "Ialenter", "Ma'at", "Narmer", "Radeem", "Jaloom", "Lepsha", "Quor", "Oleshet", "Peput", "Talat", "Ulam", "Veresh", "Ranesh", "Snef", "Wollolo", "Hathor", "Intef", "Neferk", "Khatne", "Tepy", "Moret"};
    private static String[] numeralArray = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII", "XIII", "XIV", "XV"};
    private static final DataParameter<Integer> PREFIX = EntityDataManager.createKey(EntityPharaoh.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> SUFFIX = EntityDataManager.createKey(EntityPharaoh.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> NUMERAL = EntityDataManager.createKey(EntityPharaoh.class, DataSerializers.VARINT);
    private static final DataParameter<Optional<BlockPos>> SARCOPHAGUS_POS = EntityDataManager.createKey(EntityPharaoh.class, DataSerializers.OPTIONAL_BLOCK_POS);
    private final BossInfoServer bossInfo = (BossInfoServer) new BossInfoServer(this.getDisplayName(), BossInfo.Color.YELLOW, BossInfo.Overlay.PROGRESS).setCreateFog(true);
    private boolean hasSarcophagus;
    private int stage;
    private int suffixID = 0;
    private int prefixID = 0;
    private int numID = 0;
    private int regenTime = 0;
    private int berserkTimer;
    private float berserkDamage;

    public EntityPharaoh(World world) {
        this(world, false);
    }

    public EntityPharaoh(World world, boolean setSarcophagusPos) {
        super(world);
        this.setHealth(this.getMaxHealth());
        this.isImmuneToFire = true;
        this.experienceValue = 250;
        this.hasSarcophagus = setSarcophagusPos;
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
    protected void setVariant(int variant) {
        super.setVariant(variant);
        God.MAP.put(variant, God.values()[variant]);
    }

    @Override
    protected void applyEntityAI() {
        super.applyEntityAI();
        this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, false));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(300.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(36.0D);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        if (prefixID == 0 && suffixID == 0 && numID == 0) {
            prefixID = rand.nextInt(prefixArray.length);
            suffixID = rand.nextInt(suffixArray.length);
            numID = rand.nextInt(numeralArray.length);
        }
        this.dataManager.register(PREFIX, suffixID);
        this.dataManager.register(SUFFIX, prefixID);
        this.dataManager.register(NUMERAL, numID);
    }

    @Override
    protected boolean shouldBurnInDay() {
        return false;
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        ItemScepter scepter = ItemScepter.getScepter(God.getGod(getVariant()));
        if (scepter != null) {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(scepter));
        }
    }

    @Override
    public String getTexture() {
        return String.valueOf(new ResourceLocation(Constants.MOD_ID, "textures/entities/pharaoh" + "_" + God.getGod(this.getVariant()) + ".png"));
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingData) {
        livingData = super.onInitialSpawn(difficulty, livingData);

        bossInfo.setName(this.getDisplayName().setStyle(new Style().setColor(God.getGod(this.getVariant()).getColor())));

        this.setEquipmentBasedOnDifficulty(difficulty);
        this.setEnchantmentBasedOnDifficulty(difficulty);

        return livingData;
    }

    @Override
    protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier) { //Don't drop Pharaoh Scepters
    }

    public BlockPos getSarcophagusPos() {
        return this.dataManager.get(SARCOPHAGUS_POS).orNull();
    }

    public void setSarcophagusPos(BlockPos pos) {
        this.dataManager.register(SARCOPHAGUS_POS, Optional.of(pos));
    }

    @Override
    public boolean isNonBoss() {
        return false;
    }

    @Override
    protected void despawnEntity() {
    }

    @Override
    public void onDeath(@Nonnull DamageSource source) {
        super.onDeath(source);

        if (source.damageType.equals("player")) {
            EntityPlayer slayer = (EntityPlayer) source.getTrueSource();
            if (!world.isRemote && slayer != null) {
                List<EntityPlayerMP> players = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();
                for (EntityPlayer player : players) {
                    player.sendMessage(new TextComponentString(God.getGod(this.getVariant()).getColor() + this.getName() + " " + AtumUtils.format("chat.atum.killPharaoh") + " " + slayer.getGameProfile().getName()));
                }
            }
        }

        if (this.hasSarcophagus) {
            BlockPos sarcophagusPos = getSarcophagusPos();
            if (sarcophagusPos != null) {
                TileEntity te = world.getTileEntity(sarcophagusPos);
                if (te != null) {
                    if (te instanceof TileEntitySarcophagus) {
                        TileEntitySarcophagus sarcophagus = (TileEntitySarcophagus) te;
                        sarcophagus.setOpenable();
                        sarcophagus.hasSpawned = true;
                    }
                } else {
                    Atum.LOG.error("Unable to find sarcophagus coordinates for " + this.getName() + " on " + sarcophagusPos);
                }
            }
        }
    }

    @Override
    public void addTrackingPlayer(EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    @Nonnull
    public String getName() {
        try {
            int p = this.dataManager.get(PREFIX);
            int s = this.dataManager.get(SUFFIX);
            int n = this.dataManager.get(NUMERAL);
            return "Pharaoh " + AtumUtils.format("entity.atum.pharaoh." + prefixArray[p]) + AtumUtils.format("entity.atum.pharaoh." + suffixArray[s].toLowerCase(Locale.ENGLISH)) + " " + numeralArray[n];
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public void knockBack(@Nonnull Entity entity, float strength, double xRatio, double zRatio) {
        if (God.getGod(this.getVariant()) != God.PTAH) {
            super.knockBack(entity, strength, xRatio, zRatio);
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
    public boolean attackEntityAsMob(Entity entity) {
        if (entity instanceof EntityLivingBase && !world.isRemote) {
            EntityLivingBase entityLiving = (EntityLivingBase) entity;
            switch (God.getGod(this.getVariant())) {
                case ANPUT:
                    entityLiving.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 80, 1));
                    break;
                case ANUBIS:
                    entityLiving.addPotionEffect(new PotionEffect(MobEffects.WITHER, 60, 1));
                    break;
                case GEB:
                    entityLiving.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 60, 1));
                    break;
                case HORUS:
                    entityLiving.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 60, 1));
                    break;
                case NUIT:
                    entityLiving.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 60));
                    break;
                case RA:
                    entityLiving.setFire(4);
                    break;
                case SETH:
                    entityLiving.addPotionEffect(new PotionEffect(MobEffects.POISON, 100, 1));
                    break;
                case SHU:
                    ItemHorusAscension.knockUp(entityLiving, this, rand);
                    break;
                case TEFNUT:
                    entityLiving.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 60));
                    break;
                default:
                    break;
            }
        }
        return super.attackEntityAsMob(entity);
    }

    @SubscribeEvent
    public void onBerserk (LivingHurtEvent event) {
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
    public void onUpdate() {
        super.onUpdate();

        if (!this.world.isRemote && this.world.getDifficulty().getId() == 0) {
            if (this.hasSarcophagus) {
                TileEntity te = world.getTileEntity(this.getSarcophagusPos());
                if (te instanceof TileEntitySarcophagus) {
                    ((TileEntitySarcophagus) te).setPharaohDespawned();
                }
            }
            this.setDead();
        }
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public void onLivingUpdate() {
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
        super.onLivingUpdate();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("prefix", this.dataManager.get(PREFIX));
        compound.setInteger("suffix", this.dataManager.get(SUFFIX));
        compound.setInteger("numeral", this.dataManager.get(NUMERAL));
        if (hasSarcophagus) {
            BlockPos sarcophagusPos = getSarcophagusPos();
            if (sarcophagusPos != null) {
                compound.setInteger("sarcophagus_x", sarcophagusPos.getX());
                compound.setInteger("sarcophagus_y", sarcophagusPos.getY());
                compound.setInteger("sarcophagus_z", sarcophagusPos.getZ());
            }
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        suffixID = compound.getInteger("suffix");
        prefixID = compound.getInteger("prefix");
        numID = compound.getInteger("numeral");
        this.dataManager.set(PREFIX, prefixID);
        this.dataManager.set(SUFFIX, suffixID);
        this.dataManager.set(NUMERAL, numID);
        if (this.hasSarcophagus) {
            if (compound.hasKey("sarcophagus_x")) {
                int x = compound.getInteger("sarcophagus_x");
                int y = compound.getInteger("sarcophagus_y");
                int z = compound.getInteger("sarcophagus_z");
                this.dataManager.set(SARCOPHAGUS_POS, Optional.of(new BlockPos(x, y, z)));
            } else {
                this.dataManager.set(SARCOPHAGUS_POS, Optional.absent());
            }
        }
    }

    public void spawnGuards(BlockPos pos) {
        EnumFacing facing = EnumFacing.byHorizontalIndex(MathHelper.floor(this.rotationYaw * 4.0F / 360.0F + 0.5D) & 3).getOpposite();
        this.trySpawnMummy(pos, facing.rotateYCCW());
        this.trySpawnMummy(pos, facing.rotateY());
    }

    private void trySpawnMummy(BlockPos pos, EnumFacing facing) {
        pos = pos.offset(facing);
        if (!world.isAirBlock(pos.offset(facing))) {
            pos = pos.offset(facing, 2);
        }
        if ((WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, world, pos) || world.getBlockState(pos.down()) instanceof BlockStairs) && world.isAirBlock(pos)) {
            EntityMummy entityMummy = new EntityMummy(world);
            entityMummy.onInitialSpawn(world.getDifficultyForLocation(pos), null);
            entityMummy.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), world.rand.nextFloat() * 360.0F, 0.0F);

            if (!world.isRemote) {
                AnvilChunkLoader.spawnEntity(entityMummy, world);
            }
            entityMummy.spawnExplosionParticle();
        }
    }

    public enum God {
        ANPUT("anput", TextFormatting.BLACK),
        ANUBIS("anubis", TextFormatting.DARK_PURPLE),
        ATUM("atum", TextFormatting.DARK_AQUA),
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

        public static final Map<Integer, God> MAP = Maps.newHashMap();
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
            God god = MAP.get(godType);
            return god == null ? ANPUT : god;
        }
    }
}
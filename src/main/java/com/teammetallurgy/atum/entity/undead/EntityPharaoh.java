package com.teammetallurgy.atum.entity.undead;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.TileEntitySarcophagus;
import com.teammetallurgy.atum.items.artifacts.horus.HorusAscensionItem;
import com.teammetallurgy.atum.items.tools.ScepterItem;
import com.teammetallurgy.atum.utils.AtumUtils;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    private final BossInfoServer bossInfo = (BossInfoServer) new BossInfoServer(this.getDisplayName(), BossInfo.Color.YELLOW, BossInfo.Overlay.NOTCHED_10).setCreateFog(true);
    private int stage;
    private int suffixID = 0;
    private int prefixID = 0;
    private int numID = 0;
    private int regenTime = 0;
    private int berserkTimer;
    private float berserkDamage;
    private String texturePath;

    public EntityPharaoh(World world) {
        super(world);
        this.setHealth(this.getMaxHealth());
        this.isImmuneToFire = true;
        this.experienceValue = 250;
        this.stage = 0;
        this.setCanPickUpLoot(false);
        ((PathNavigateGround) this.getNavigator()).setBreakDoors(true);
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
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(1, new EntityAIOpenDoor(this, false));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
    }

    @Override
    protected void applyEntityAI() {
        super.applyEntityAI();
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(300.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(36.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10.0F);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(PREFIX, 0);
        this.dataManager.register(SUFFIX, 0);
        this.dataManager.register(NUMERAL, 0);
        this.dataManager.register(SARCOPHAGUS_POS, Optional.absent());
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VINDICATION_ILLAGER_AMBIENT;
    }

    @Override
    protected boolean shouldBurnInDay() {
        return false;
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        ScepterItem scepter = ScepterItem.getScepter(God.getGod(getVariant()));
        if (scepter != null) {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(scepter));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getTexture() {
        if (this.texturePath == null) {
            this.texturePath = new ResourceLocation(Constants.MOD_ID, "textures/entity/pharaoh_" + God.getGod(this.getVariant())) + ".png";
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

        prefixID = rand.nextInt(prefixArray.length);
        suffixID = rand.nextInt(suffixArray.length);
        numID = rand.nextInt(numeralArray.length);

        this.setPharaohName(this.prefixID, this.suffixID, this.numID);

        this.setEquipmentBasedOnDifficulty(difficulty);
        this.setEnchantmentBasedOnDifficulty(difficulty);
    }

    @Override
    protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier) { //Don't drop Pharaoh Scepters
    }

    private BlockPos getSarcophagusPos() {
        return this.dataManager.get(SARCOPHAGUS_POS).orNull();
    }

    public void setSarcophagusPos(BlockPos pos) {
        this.dataManager.set(SARCOPHAGUS_POS, Optional.fromNullable(pos));
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
        if (!world.isRemote) {
            BlockPos sarcophagusPos = getSarcophagusPos();
            if (sarcophagusPos != null) {
                TileEntity tileEntity = world.getTileEntity(sarcophagusPos);
                if (tileEntity instanceof TileEntitySarcophagus) {
                    ((TileEntitySarcophagus) tileEntity).setOpenable();
                    for (EnumFacing horizontal : EnumFacing.HORIZONTALS) {
                        TileEntity tileEntityOffset = world.getTileEntity(sarcophagusPos.offset(horizontal));
                        if (tileEntityOffset instanceof TileEntitySarcophagus) {
                            ((TileEntitySarcophagus) tileEntityOffset).setOpenable();
                        }
                    }
                } else {
                    Atum.LOG.error("Unable to find sarcophagus coordinates for " + this.getName() + " on " + sarcophagusPos);
                }
            }
        }

        if (source.damageType.equals("player")) {
            EntityPlayer slayer = (EntityPlayer) source.getTrueSource();
            if (!world.isRemote && slayer != null) {
                List<EntityPlayerMP> players = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();
                for (EntityPlayer player : players) {
                    player.sendMessage(new TextComponentString(God.getGod(this.getVariant()).getColor() + this.getName() + " " + AtumUtils.format("chat.atum.killPharaoh") + " " + slayer.getGameProfile().getName()));
                }
            }
        }
        super.onDeath(source);
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
        int p = this.dataManager.get(PREFIX);
        int s = this.dataManager.get(SUFFIX);
        int n = this.dataManager.get(NUMERAL);
        return "Pharaoh " + AtumUtils.format("entity.atum.pharaoh." + prefixArray[p]) + AtumUtils.format("entity.atum.pharaoh." + suffixArray[s].toLowerCase(Locale.ENGLISH)) + " " + numeralArray[n];
    }

    @Override
    public void knockBack(@Nonnull Entity entity, float strength, double xRatio, double zRatio) {
        if (God.getGod(this.getVariant()) != God.PTAH) {
            strength *= 0.20F;
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
        if (!super.attackEntityAsMob(entity)) {
            return false;
        } else {
            if (entity instanceof LivingEntity && !world.isRemote) {
                LivingEntity entityLiving = (LivingEntity) entity;
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
                        HorusAscensionItem.knockUp(entityLiving, this, rand);
                        break;
                    case TEFNUT:
                        entityLiving.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 60));
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
    public void onUpdate() {
        super.onUpdate();

        if (!world.isRemote) {
            this.setBossInfo(this.getVariant());
        }

        if (this.world.getDifficulty().getId() == 0) {
            if (this.getSarcophagusPos() != null) {
                TileEntity te = world.getTileEntity(this.getSarcophagusPos());
                if (te instanceof TileEntitySarcophagus) {
                    ((TileEntitySarcophagus) te).hasSpawned = false;
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
        compound.setInteger("prefix", prefixID);
        compound.setInteger("suffix", suffixID);
        compound.setInteger("numeral", numID);
        BlockPos sarcophagusPos = getSarcophagusPos();
        if (sarcophagusPos != null) {
            compound.setInteger("sarcophagus_x", sarcophagusPos.getX());
            compound.setInteger("sarcophagus_y", sarcophagusPos.getY());
            compound.setInteger("sarcophagus_z", sarcophagusPos.getZ());
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        prefixID = compound.getInteger("prefix");
        suffixID = compound.getInteger("suffix");
        numID = compound.getInteger("numeral");
        if (compound.hasKey("sarcophagus_x")) {
            int x = compound.getInteger("sarcophagus_x");
            int y = compound.getInteger("sarcophagus_y");
            int z = compound.getInteger("sarcophagus_z");
            this.dataManager.set(SARCOPHAGUS_POS, Optional.of(new BlockPos(x, y, z)));
        } else {
            this.dataManager.set(SARCOPHAGUS_POS, Optional.absent());
        }
        this.setPharaohName(compound.getInteger("prefix"), compound.getInteger("suffix"), compound.getInteger("numeral"));
    }

    private void setPharaohName(int prefix, int suffix, int numeral) {
        this.dataManager.set(PREFIX, prefix);
        this.dataManager.set(SUFFIX, suffix);
        this.dataManager.set(NUMERAL, numeral);
    }

    public void spawnGuards(BlockPos pos) {
        EnumFacing facing = EnumFacing.byHorizontalIndex(MathHelper.floor(this.rotationYaw * 4.0F / 360.0F + 0.5D) & 3).getOpposite();
        this.trySpawnMummy(pos, facing);
        this.trySpawnMummy(pos, facing.rotateY().rotateY());
    }

    private void trySpawnMummy(BlockPos pos, EnumFacing facing) {
        BlockPos base = pos.offset(facing, 1);

        if (!world.isBlockFullCube(base) && !world.isBlockFullCube(base.offset(EnumFacing.UP))) {
            EntityMummy entityMummy = new EntityMummy(world);
            entityMummy.onInitialSpawn(world.getDifficultyForLocation(base), null);
            entityMummy.setLocationAndAngles(base.getX(), base.getY(), base.getZ(), world.rand.nextFloat() * 360.0F, 0.0F);

            if (!world.isRemote) {
                AnvilChunkLoader.spawnEntity(entityMummy, world);
            }
            entityMummy.spawnExplosionParticle();
            return;
        }

        for (EnumFacing offset : EnumFacing.HORIZONTALS) {
            // Don't spawn the mummy on top of the pharaoh
            if (offset == facing.getOpposite())
                continue;

            BlockPos new_pos = base.offset(offset);
            if (!world.isBlockFullCube(new_pos) && !world.isBlockFullCube(new_pos.offset(EnumFacing.UP))) {
                EntityMummy entityMummy = new EntityMummy(world);
                entityMummy.onInitialSpawn(world.getDifficultyForLocation(new_pos), null);
                entityMummy.setLocationAndAngles(new_pos.getX(), new_pos.getY(), new_pos.getZ(), world.rand.nextFloat() * 360.0F, 0.0F);

                if (!world.isRemote) {
                    AnvilChunkLoader.spawnEntity(entityMummy, world);
                }
                entityMummy.spawnExplosionParticle();
                return;
            }
        }
    }

    private void setBossInfo(int variant) {
        this.bossInfo.setName(this.getDisplayName().setStyle(new Style().setColor(God.getGod(variant).getColor())));
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
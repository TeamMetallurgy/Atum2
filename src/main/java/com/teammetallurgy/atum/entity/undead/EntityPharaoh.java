package com.teammetallurgy.atum.entity.undead;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.limestone.chest.tileentity.TileEntitySarcophagus;
import com.teammetallurgy.atum.items.ItemScepter;
import com.teammetallurgy.atum.utils.AtumUtils;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.FMLCommonHandler;

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
    private final BossInfoServer bossInfo = (BossInfoServer) (new BossInfoServer(this.getDisplayName(), BossInfo.Color.YELLOW, BossInfo.Overlay.PROGRESS)).setCreateFog(true);
    private boolean hasSarcophagus;
    private int stage;
    private int suffixID = 0;
    private int prefixID = 0;
    private int numID = 0;
    private int regenTime = 0;

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
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30.0D);
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
        super.setEquipmentBasedOnDifficulty(difficulty);
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

        this.setEquipmentBasedOnDifficulty(difficulty);
        this.setEnchantmentBasedOnDifficulty(difficulty);

        for (int i = 0; i < this.inventoryArmorDropChances.length; ++i) {
            this.inventoryArmorDropChances[i] = 0F;
        }
        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * difficulty.getClampedAdditionalDifficulty());

        return livingData;
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
                    player.sendMessage(new TextComponentString(TextFormatting.DARK_RED + this.getName() + " " + AtumUtils.format("chat.atum.killPharaoh") + " " + slayer.getGameProfile().getName()));
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
    public void knockBack(@Nonnull Entity entity, float par2, double par3, double par5) {
        this.isAirBorne = true;
        float f = MathHelper.sqrt(par3 * par3 + par5 * par5);
        float f1 = 0.3F;
        this.motionX /= 2.0D;
        this.motionY /= 2.0D;
        this.motionZ /= 2.0D;
        this.motionX -= par3 / (double) f * (double) f1;
        this.motionZ -= par5 / (double) f * (double) f1;

        if (this.motionY > 0.4000000059604645D) {
            this.motionY = 0.4000000059604645D;
        }
    }

    @Override
    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        if (source.isFireDamage()) {
            amount = 0;
        }
        if (super.attackEntityFrom(source, amount)) {
            if (source.getTrueSource() != null) {
                Entity entity = source.getTrueSource();
                int j = 0;
                if (entity instanceof EntityLiving) {
                    j += EnchantmentHelper.getKnockbackModifier(this);
                    if (j > 0) {
                        this.motionX /= 0.6D;
                        this.motionZ /= 0.6D;
                        this.addVelocity((double) (MathHelper.sin(entity.rotationYaw * (float) Math.PI / 180.0F) * (float) j * 0.5F), -0.1D, (double) (-MathHelper.cos(entity.rotationYaw * (float) Math.PI / 180.0F) * (float) j * 0.5F));
                    }
                }
            }
            if (this.getHealth() < this.getMaxHealth() * 0.75 && stage == 0) {
                stage++;
                spawnGuards();
            } else if (stage == 1 && this.getHealth() < this.getMaxHealth() * 0.5) {
                stage++;
                spawnGuards();
            } else if (stage == 2 && this.getHealth() < this.getMaxHealth() * 0.25) {
                stage++;
                spawnGuards();
            }
            return true;
        }
        return false;
    }

    @Override
    protected void dropFewItems(boolean recentlyHit, int looting) {
        int amount = MathHelper.getInt(rand, 1, 2) + looting;
        this.dropItem(Items.GOLD_INGOT, amount);
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
            this.heal(1);
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

    private void spawnGuards() {
        int numSpawned = 0;
        for (BlockPos.MutableBlockPos mutablePos : BlockPos.MutableBlockPos.getAllInBoxMutable(this.getPosition().add(1, 0, 1), this.getPosition().add(-1, 0, -1))) {
            if (numSpawned >= 2) return;
            if (trySpawnMummy(mutablePos)) {
                numSpawned++;
            }
        }
    }

    public boolean trySpawnMummy(BlockPos pos) {
        EntityMummy entityMummy = new EntityMummy(world);
        entityMummy.onInitialSpawn(world.getDifficultyForLocation(pos), null);
        entityMummy.setPosition(pos.getX(), pos.getY(), pos.getZ());

        IBlockState state = world.getBlockState(pos);
        if (WorldEntitySpawner.isValidEmptySpawnBlock(state) && state.getBlock().isAir(state, world, pos.up())) {
            if (!world.isRemote) {
                world.spawnEntity(entityMummy);
            }
            entityMummy.spawnExplosionParticle();
            return true;
        }
        return false;
    }

    public enum God {
        ANPUT("anput"),
        ANUBIS("anubis"),
        ATUM("atum"),
        GEB("geb"),
        HORUS("horus"),
        ISIS("isis"),
        MONTU("montu"),
        NUIT("nuit"),
        PTAH("ptah"),
        RA("ra"),
        SETH("seth"),
        SHU("shu"),
        TEFNUT("tefnut");

        public static final Map<Integer, God> MAP = Maps.newHashMap();
        private final String name;

        God(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static God getGod(int godType) {
            God god = MAP.get(godType);
            return god == null ? ANPUT : god;
        }
    }
}
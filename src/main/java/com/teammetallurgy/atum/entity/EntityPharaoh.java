package com.teammetallurgy.atum.entity;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.BlockLimestoneBricks;
import com.teammetallurgy.atum.blocks.tileentity.chests.TileEntityPharaohChest;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.AtumLoot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class EntityPharaoh extends EntityMob {
    private static String[] prefixArray = {"Ama", "Ata", "Ato", "Bak", "Cal", "Djet", "Eje", "For", "Gol", "Gut", "Hop", "Hor", "Huni", "Iam", "Jor", "Kal", "Khas", "Khor", "Lat", "Mal", "Not", "Oap", "Pra", "Qo", "Ras", "Shas", "Thoth", "Tui", "Uld", "Ver", "Wot", "Xo", "Yat", "Zyt", "Khep"};
    private static String[] suffixArray = {"Ahat", "Amesh", "Amon", "Anut", "Baroom", "Chanta", "Erant", "Funam", "Daresh", "Djer", "Hotesh", "Khaden", "Kron", "Gorkum", "Ialenter", "Ma'at", "Narmer", "Radeem", "Jaloom", "Lepsha", "Quor", "Oleshet", "Peput", "Talat", "Ulam", "Veresh", "Ranesh", "Snef", "Wollolo", "Hathor", "Intef", "Neferk", "Khatne", "Tepy", "Moret"};
    private static String[] numeralArray = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII", "XIII", "XIV", "XV"};
    private static final DataParameter<Integer> PREFIX = EntityDataManager.createKey(EntityPharaoh.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> SUFFIX = EntityDataManager.createKey(EntityPharaoh.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> NUMERAL = EntityDataManager.createKey(EntityPharaoh.class, DataSerializers.VARINT);
    private static final DataParameter<BlockPos> LINKED_POS = EntityDataManager.createKey(EntityPharaoh.class, DataSerializers.BLOCK_POS);
    private static final DataParameter<BlockPos> CHEST_POS = EntityDataManager.createKey(EntityPharaoh.class, DataSerializers.BLOCK_POS);
    private int stage;
    private int suffixID = 0;
    private int prefixID = 0;
    private int numID = 0;
    private int regenTime = 0;

    public EntityPharaoh(World world) {
        super(world);
        this.setHealth(this.getMaxHealth());
        this.experienceValue = 250;
        stage = 0;
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(4, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(300.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.53000000417232513D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(10.0D);
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
        this.dataManager.register(CHEST_POS, new BlockPos(posX, posY, posZ));
        this.dataManager.register(LINKED_POS, this.getPosition());
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(AtumItems.SCEPTER));
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);

        this.setEquipmentBasedOnDifficulty(difficulty);
        this.setEnchantmentBasedOnDifficulty(difficulty);


        for (int i = 0; i < this.inventoryArmorDropChances.length; ++i) {
            this.inventoryArmorDropChances[i] = 0F;
        }
        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * difficulty.getClampedAdditionalDifficulty());

        return livingdata;
    }

    public void link(BlockPos pos) {
        this.dataManager.register(LINKED_POS, pos);
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
            if (!world.isRemote) {
                List<EntityPlayerMP> players = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();
                for (EntityPlayer player : players) {
                    player.sendMessage(new TextComponentString(this.getName() + " " + I18n.format("chat.atum.killPharaoh") + " " + slayer.getGameProfile().getName()));
                }
            }
        }

        BlockPos chestPos = this.dataManager.get(CHEST_POS);

        TileEntity te = world.getTileEntity(chestPos);
        if (te != null) {
            if (te instanceof TileEntityPharaohChest) {
                TileEntityPharaohChest tepc = (TileEntityPharaohChest) te;
                tepc.setOpenable();
            }
        } else {
            Atum.LOG.error("Unable to find chest coords for " + this.getName() + " on " + chestPos);
        }
    }

    @Override
    @Nonnull
    public String getName() {
        try {
            int p = this.dataManager.get(PREFIX);
            int s = this.dataManager.get(SUFFIX);
            int n = this.dataManager.get(NUMERAL);
            return "Pharaoh " + I18n.format("entity.atum.pharaoh." + prefixArray[p]) + I18n.format("entity.atum.pharaoh." + suffixArray[s]) + " " + numeralArray[n];
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    @Nonnull
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
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

    private boolean destroyBlocksInAABB(AxisAlignedBB axisAlignedBB) {
        int minX = MathHelper.floor(axisAlignedBB.minX);
        int minY = MathHelper.floor(axisAlignedBB.minY);
        int minZ = MathHelper.floor(axisAlignedBB.minZ);
        int maxX = MathHelper.floor(axisAlignedBB.maxX);
        int maxY = MathHelper.floor(axisAlignedBB.maxY);
        int maxZ = MathHelper.floor(axisAlignedBB.maxZ);
        boolean flag = false;
        boolean flag1 = false;

        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    BlockPos pos = new BlockPos(x, y, z);
                    IBlockState state = world.getBlockState(pos);

                    if (state != null) {
                        if (state != BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.LARGE).setBlockUnbreakable().getDefaultState() && state != AtumBlocks.PHARAOH_CHEST.getDefaultState() && state != Blocks.BEDROCK.getDefaultState() && state.getMaterial().isSolid()) {
                            state.getBlock().dropBlockAsItem(world, pos, state, 0);
                            flag1 = this.world.setBlockToAir(pos) || flag1;
                        }

                        flag = true;
                    }
                }
            }
        }

        if (flag1) {
            double d0 = axisAlignedBB.minX + (axisAlignedBB.maxX - axisAlignedBB.minX) * (double) this.rand.nextFloat();
            double d1 = axisAlignedBB.minY + (axisAlignedBB.maxY - axisAlignedBB.minY) * (double) this.rand.nextFloat();
            double d2 = axisAlignedBB.minZ + (axisAlignedBB.maxZ - axisAlignedBB.minZ) * (double) this.rand.nextFloat();
            world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0 + Math.random(), d1 + 1.2D, d2 + Math.random(), 0.0D, 0.0D, 0.0D);

        }

        return flag;
    }

    private void spawnGuards() {
        int numSpawned = 0;

        if (trySpawnMummy((int) posX + 1, (int) posY, (int) posZ)) {
            numSpawned++;
        }
        if (numSpawned >= 2)
            return;

        if (trySpawnMummy((int) posX - 1, (int) posY, (int) posZ - 1)) {
            numSpawned++;
        }
        if (numSpawned >= 2)
            return;

        if (trySpawnMummy((int) posX, (int) posY, (int) posZ + 1)) {
            numSpawned++;
        }
        if (numSpawned >= 2)
            return;

        if (trySpawnMummy((int) posX, (int) posY, (int) posZ - 1)) {
            numSpawned++;
        }
        if (numSpawned >= 2)
            return;

        if (trySpawnMummy((int) posX + 1, (int) posY, (int) posZ + 1)) {
            numSpawned++;
        }
        if (numSpawned >= 2)
            return;

        if (trySpawnMummy((int) posX - 1, (int) posY, (int) posZ - 1)) {
            numSpawned++;
        }
        if (numSpawned >= 2)
            return;

        if (trySpawnMummy((int) posX - 1, (int) posY, (int) posZ + 1)) {
            numSpawned++;
        }
        if (numSpawned >= 2)
            return;

        if (trySpawnMummy((int) posX + 1, (int) posY, (int) posZ - 1)) {
            numSpawned++;
        }
        if (numSpawned >= 2) {
            return;
        }
    }

    private boolean trySpawnMummy(int x, int y, int z) {
        EntityMummy entityMummy = new EntityMummy(world);
        entityMummy.setPosition(x, y, z);
        if (entityMummy.getCanSpawnHere()) {
            if (!world.isRemote) {
                world.spawnEntity(entityMummy);
            }
            entityMummy.spawnExplosionParticle();
            return true;
        }
        return false;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("prefix", this.dataManager.get(PREFIX));
        compound.setInteger("suffix", this.dataManager.get(SUFFIX));
        compound.setInteger("numeral", this.dataManager.get(NUMERAL));
        compound.setInteger("chestPos", this.dataManager.get(CHEST_POS).getX());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        suffixID = compound.getInteger("suffix");
        prefixID = compound.getInteger("prefix");
        numID = compound.getInteger("numeral");
        //linkedXYZ = compound.getInteger("chestPos"); //TODO
        this.dataManager.set(PREFIX, prefixID);
        this.dataManager.set(SUFFIX, suffixID);
        this.dataManager.set(NUMERAL, numID);
        this.dataManager.set(LINKED_POS, this.dataManager.get(LINKED_POS));
        this.dataManager.set(CHEST_POS, this.dataManager.get(CHEST_POS));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (!this.world.isRemote && this.world.getDifficulty().getDifficultyId() == 0) {
            TileEntity te = world.getTileEntity(this.dataManager.get(LINKED_POS));
            if (te instanceof TileEntityPharaohChest) {
                ((TileEntityPharaohChest) te).setPharaohDespawned();
            }
            this.setDead();
        }
    }

    @Override
    public void onLivingUpdate() {
        if (regenTime++ > 20) {
            regenTime = 0;
            this.heal(1);
        }

        super.onLivingUpdate();

        if (!world.isRemote)
            this.destroyBlocksInAABB(this.getEntityBoundingBox().expand(0.1, 0, 0.1));
    }

    @Override
    protected void dropFewItems(boolean recentlyHit, int looting) {
        int amount = rand.nextInt(2) + 1;
        this.dropItem(Items.GOLD_INGOT, amount);

        this.entityDropItem(AtumLoot.getRandomArtifact(), 0.0F);
    }
}
package com.teammetallurgy.atum.entity.efreet;

import com.teammetallurgy.atum.blocks.wood.BlockAtumPlank;
import com.teammetallurgy.atum.blocks.wood.BlockAtumSapling;
import com.teammetallurgy.atum.entity.undead.EntityPharaoh;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.items.ItemLoot;
import com.teammetallurgy.atum.items.tools.ItemScepter;
import com.teammetallurgy.atum.utils.StackHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class EntitySunspeaker extends EntityEfreetBase implements IMerchant {
    @Nullable
    private EntityPlayer buyingPlayer;
    @Nullable
    private MerchantRecipeList buyingList;
    private int timeUntilReset;
    private boolean needsInitilization;
    private int wealth;
    private static final EntityVillager.ITradeList[] TRADES = new EntityVillager.ITradeList[]{new ItemsForCoins(24, AtumItems.DATE, new EntityVillager.PriceInfo(15, 16)), new ItemsForCoins(48, AtumItems.CAMEL_RAW, new EntityVillager.PriceInfo(12, 18)), new ItemsForCoins(64, AtumItems.SCROLL, new EntityVillager.PriceInfo(8, 10)), new ItemsForCoins(48, Items.GLOWSTONE_DUST, new EntityVillager.PriceInfo(1, 4)), new ItemsForCoins(48, AtumItems.GRAVEROBBERS_MAP, new EntityVillager.PriceInfo(1, 1)), new ItemsForCoins(48, Items.NAME_TAG, new EntityVillager.PriceInfo(1, 1)), new ItemsForCoins(64, Items.SADDLE, new EntityVillager.PriceInfo(1, 1)), new ItemsForCoins(36, Item.getItemFromBlock(BlockAtumSapling.getSapling(BlockAtumPlank.WoodType.PALM)), new EntityVillager.PriceInfo(4, 5)), new ItemsForCoins(24, AtumItems.ANPUTS_FINGERS_SPORES, new EntityVillager.PriceInfo(6, 8)), new ItemsForCoins(48, AtumItems.LINEN_CLOTH, new EntityVillager.PriceInfo(5, 9)), new ItemsForCoins(96, Items.BREWING_STAND, new EntityVillager.PriceInfo(1, 1))};

    public EntitySunspeaker(World world) {
        super(world);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, false));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0F);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ItemScepter.getScepter(EntityPharaoh.God.RA)));
    }

    @Override
    protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier) { //Don't drop the scepter
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return AtumLootTables.SUNSPEAKER;
    }

    @Override
    @Nullable
    public EntityAgeable createChild(@Nonnull EntityAgeable ageable) {
        EntitySunspeaker sunspeaker = new EntitySunspeaker(this.world);
        sunspeaker.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(sunspeaker)), null);
        return sunspeaker;
    }

    @Override
    protected void updateAITasks() {
        if (!this.isTrading() && this.timeUntilReset > 0) {
            --this.timeUntilReset;

            if (this.timeUntilReset <= 0) {
                if (this.needsInitilization && this.buyingList != null) {
                    for (MerchantRecipe merchantrecipe : this.buyingList) {
                        if (merchantrecipe.isRecipeDisabled()) {
                            merchantrecipe.increaseMaxTradeUses(this.rand.nextInt(6) + this.rand.nextInt(6) + 2);
                        }
                    }

                    this.populateBuyingList();
                    this.needsInitilization = false;
                }
                this.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 0));
            }
        }
        super.updateAITasks();
    }

    private void populateBuyingList() {
        if (this.buyingList == null) {
            this.buyingList = new MerchantRecipeList();
        }

        List<EntityVillager.ITradeList> trades = Collections.singletonList(TRADES[rand.nextInt(TRADES.length - 1)]);

        for (EntityVillager.ITradeList tradeList : trades) {
            tradeList.addMerchantRecipe(this, this.buyingList, this.rand);
        }
    }

    @Override
    public void setCustomer(@Nullable EntityPlayer player) {
        this.buyingPlayer = player;
    }

    @Override
    @Nullable
    public EntityPlayer getCustomer() {
        return this.buyingPlayer;
    }

    public boolean isTrading() {
        return this.buyingPlayer != null;
    }

    @Override
    @Nullable
    public MerchantRecipeList getRecipes(@Nonnull EntityPlayer player) {
        if (this.buyingList == null) {
            this.populateBuyingList();
        }
        return this.buyingList;
    }

    @Override
    public void useRecipe(@Nonnull MerchantRecipe recipe) {
        recipe.incrementToolUses();
        this.livingSoundTime = -this.getTalkInterval();
        this.playSound(SoundEvents.ENTITY_VILLAGER_YES, this.getSoundVolume(), this.getSoundPitch());
        int i = 3 + this.rand.nextInt(4);

        if (recipe.getToolUses() == 1 || this.rand.nextInt(5) == 0) {
            this.timeUntilReset = 40;
            this.needsInitilization = true;
            i += 5;
        }

        if (recipe.getItemToBuy().getItem() == AtumItems.GOLD_COIN) {
            this.wealth += recipe.getItemToBuy().getCount();
        }

        if (recipe.getRewardsExp()) {
            this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY + 0.5D, this.posZ, i));
        }
    }

    @Override
    public void verifySellingItem(@Nonnull ItemStack stack) {
        if (!this.world.isRemote && this.livingSoundTime > -this.getTalkInterval() + 20) {
            this.livingSoundTime = -this.getTalkInterval();
            this.playSound(stack.isEmpty() ? SoundEvents.ENTITY_VILLAGER_NO : SoundEvents.ENTITY_VILLAGER_YES, this.getSoundVolume(), this.getSoundPitch());
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setRecipes(@Nullable MerchantRecipeList recipeList) {
    }

    @Override
    @Nonnull
    public World getWorld() {
        return this.world;
    }

    @Override
    @Nonnull
    public BlockPos getPos() {
        return new BlockPos(this);
    }

    @Override
    public boolean processInteract(EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack heldStack = player.getHeldItem(hand);
        boolean nameTag = heldStack.getItem() == Items.NAME_TAG;

        if (nameTag) {
            heldStack.interactWithEntity(player, this, hand);
            return true;
        } else if (heldStack.getItem() instanceof ItemLoot && !world.isRemote) {
            ItemLoot.Type type = ItemLoot.getType(heldStack.getItem());
            ItemLoot.Quality quality = ItemLoot.getQuality(heldStack.getItem());

            if (quality != ItemLoot.Quality.DIRTY) {
                double modifier = 1.0D;
                if (type == ItemLoot.Type.NECKLACE) {
                    modifier = 2.0D;
                } else if (type == ItemLoot.Type.BROACH) {
                    modifier = 2.5D;
                } else if (type == ItemLoot.Type.SCEPTER) {
                    modifier = 3.0D;
                } else if (type == ItemLoot.Type.IDOL) {
                    modifier = 5.0D;
                }
                if (!player.isCreative()) {
                    heldStack.shrink(1);
                }
                this.handleRelicTrade(player, hand, modifier, quality);
                return true;

            } else {
                return super.processInteract(player, hand);
            }
        } else if (!this.holdingSpawnEggOfClass(heldStack, this.getClass()) && this.isEntityAlive() && !this.isTrading() && !this.isChild() && !player.isSneaking()) {
            if (this.buyingList == null) {
                this.populateBuyingList();
            }

            if (!this.world.isRemote && !this.buyingList.isEmpty()) {
                this.setCustomer(player);
                player.displayVillagerTradeGui(this);
            } else if (this.buyingList.isEmpty()) {
                return super.processInteract(player, hand);
            }
            return true;
        } else {
            return super.processInteract(player, hand);
        }
    }

    private void handleRelicTrade(EntityPlayer player, EnumHand hand, double modifier, ItemLoot.Quality quality) {
        int amount = 0;

        if (quality == ItemLoot.Quality.SILVER) {
            amount += modifier;
        } else if (quality == ItemLoot.Quality.GOLD) {
            amount += modifier * 2;
        } else if (quality == ItemLoot.Quality.SAPPHIRE) {
            amount += modifier * 3;
        } else if (quality == ItemLoot.Quality.RUBY) {
            amount += modifier * 4;
        } else if (quality == ItemLoot.Quality.EMERALD) {
            amount += modifier * 5;
        } else if (quality == ItemLoot.Quality.DIAMOND) {
            amount += modifier * 10;
        }

        if (amount > 0) {
            StackHelper.giveItem(player, hand, new ItemStack(AtumItems.GOLD_COIN, amount));
        }
    }

    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Riches", this.wealth);

        if (this.buyingList != null) {
            compound.setTag("Offers", this.buyingList.getRecipiesAsTags());
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.wealth = compound.getInteger("Riches");

        if (compound.hasKey("Offers", 10)) {
            NBTTagCompound nbttagcompound = compound.getCompoundTag("Offers");
            this.buyingList = new MerchantRecipeList(nbttagcompound);
        }
    }

    public static class ItemsForCoins implements EntityVillager.ITradeList {
        int price;
        Item buyingItem;
        EntityVillager.PriceInfo buyingAmount;

        public ItemsForCoins(int price, Item item, EntityVillager.PriceInfo buyingAmount) {
            this.buyingItem = item;
            this.price = price;
            this.buyingAmount = buyingAmount;
        }

        public void addMerchantRecipe(@Nonnull IMerchant merchant, @Nonnull MerchantRecipeList recipeList, @Nonnull Random random) {
            int buyingAmount = 1;
            if (this.buyingAmount != null) {
                buyingAmount = this.buyingAmount.getPrice(random);
            }
            recipeList.add(new MerchantRecipe(new ItemStack(AtumItems.GOLD_COIN, this.price), new ItemStack(this.buyingItem, buyingAmount)));
        }
    }
}
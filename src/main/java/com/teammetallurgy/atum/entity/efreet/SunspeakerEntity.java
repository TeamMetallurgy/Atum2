package com.teammetallurgy.atum.entity.efreet;

import com.teammetallurgy.atum.entity.undead.PharaohEntity;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.LootItem;
import com.teammetallurgy.atum.utils.StackHelper;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.merchant.IMerchant;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SunspeakerEntity extends EfreetBaseEntity implements IMerchant {
    @Nullable
    private PlayerEntity buyingPlayer;
    @Nullable
    private MerchantRecipeList buyingList;
    private int timeUntilReset;
    private boolean needsInitilization;
    private int wealth;
    private int tradeLevel;
    private static final List<VillagerEntity.ITradeList[]> TRADES = Arrays.asList(
    ///*Tier 1*/new VillagerEntity.ITradeList[]{new ItemsForCoins(32, AtumBlocks.PALM_SAPLING.asItem()), new VillagerEntity.PriceInfo(4, 5)), new ItemsForCoins(16,Item.getItemFromBlock(Blocks.DOUBLE_PLANT/*Sunflower*/), new VillagerEntity.PriceInfo(1, 2)), new ItemsForCoins(24,AtumItems.DATE, new VillagerEntity.PriceInfo(14, 16)), new ItemsForCoins(24,AtumItems.EMMER_BREAD, new VillagerEntity.PriceInfo(3, 4))},
    ///*Tier 2*/new VillagerEntity.ITradeList[]{new ItemsForCoins(36, AtumItems.LINEN_CLOTH, new VillagerEntity.PriceInfo(5, 10)), new ItemsForCoins(48, AtumItems.CAMEL_RAW, new VillagerEntity.PriceInfo(13, 18)), new ItemsForCoins(48, AtumItems.SCROLL, new VillagerEntity.PriceInfo(9, 12)), new ItemsForCoins(32, AtumItems.ANPUTS_FINGERS_SPORES, new VillagerEntity.PriceInfo(8, 10))},
    ///*Tier 3*/new VillagerEntity.ITradeList[]{new ItemsForCoins(48, Item.getItemFromBlock(Blocks.GLOWSTONE), new VillagerEntity.PriceInfo(3, 4)), new ItemsForCoins(48, Items.NAME_TAG, new VillagerEntity.PriceInfo(1, 2)), new ItemsForCoins(64, Items.BREWING_STAND, new VillagerEntity.PriceInfo(1, 1)), new ItemsForCoins(36, Items.BLAZE_POWDER, new VillagerEntity.PriceInfo(4, 5))},
    ///*Tier 4*/new VillagerEntity.ITradeList[]{new ItemsForCoins(48, Items.SADDLE, new VillagerEntity.PriceInfo(1, 1)), new ItemsForCoins(48, AtumItems.GRAVEROBBERS_MAP, new VillagerEntity.PriceInfo(1, 1)), new ItemsForCoins(64, AtumItems.ENCHANTED_GOLDEN_DATE, new VillagerEntity.PriceInfo(1, 2)), new ItemsForCoins(48, Items.ENDER_PEARL, new VillagerEntity.PriceInfo(3, 4)), new ItemsForCoins(64, AtumItems.DISENCHANTING_SCROLL, new VillagerEntity.PriceInfo(1, 1))});

    public SunspeakerEntity(EntityType<? extends SunspeakerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(2, new ILookAtTradePlayer(this));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0F);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(AtumItems.SCEPTERS.getScepter(PharaohEntity.God.RA)));
    }

    @Override
    protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier) { //Don't drop the scepter
    }

    @Override
    @Nullable
    public AgeableEntity createChild(@Nonnull AgeableEntity ageable) {
        SunspeakerEntity sunspeaker = AtumEntities.SUNSPEAKER.create(this.world);
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
                this.addPotionEffect(new EffectInstance(Effects.REGENERATION, 200, 0));
            }
        }
        super.updateAITasks();
    }

    private void populateBuyingList() {
        if (this.tradeLevel != 0) {
            ++this.tradeLevel;
        } else {
            this.tradeLevel = 1;
        }

        if (this.buyingList == null) {
            this.buyingList = new MerchantRecipeList();
        }

        int level = this.tradeLevel - 1;
        if (level < TRADES.size()) {
            VillagerEntity.ITradeList[] tradeArray = TRADES.get(level);
            VillagerEntity.ITradeList tradeList = tradeArray[rand.nextInt(tradeArray.length)];
            tradeList.addMerchantRecipe(this, this.buyingList, this.rand);
        }
    }

    @Override
    public void setCustomer(@Nullable PlayerEntity player) {
        this.buyingPlayer = player;
    }

    @Override
    @Nullable
    public PlayerEntity getCustomer() {
        return this.buyingPlayer;
    }

    private boolean isTrading() {
        return this.buyingPlayer != null;
    }

    @Override
    @Nullable
    public MerchantRecipeList getRecipes(@Nonnull PlayerEntity player) {
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
            this.world.addEntity(new ExperienceOrbEntity(this.world, this.posX, this.posY + 0.5D, this.posZ, i));
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
    @OnlyIn(Dist.CLIENT)
    public void setRecipes(@Nullable MerchantRecipeList recipeList) {
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
    }

    @Override
    public boolean processInteract(PlayerEntity player, @Nonnull Hand hand) {
        ItemStack heldStack = player.getHeldItem(hand);
        boolean nameTag = heldStack.getItem() == Items.NAME_TAG;

        if (nameTag) {
            heldStack.interactWithEntity(player, this, hand);
            return true;
        } else if (heldStack.getItem() instanceof LootItem) {
            LootItem.Type type = LootItem.getType(heldStack.getItem());
            LootItem.Quality quality = LootItem.getQuality(heldStack.getItem());

            if (quality != LootItem.Quality.DIRTY) {
                double modifier = 1.0D;
                if (type == LootItem.Type.NECKLACE) {
                    modifier = 2.0D;
                } else if (type == LootItem.Type.BROOCH) {
                    modifier = 2.5D;
                } else if (type == LootItem.Type.SCEPTER) {
                    modifier = 3.0D;
                } else if (type == LootItem.Type.IDOL) {
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
        } else if (!this.holdingSpawnEggOfClass(heldStack, this.getClass()) && this.isAlive() && !this.isTrading() && !this.isChild() && !player.isSneaking()) {
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

    private void handleRelicTrade(PlayerEntity player, Hand hand, double modifier, LootItem.Quality quality) {
        int amount = 0;

        if (quality == LootItem.Quality.SILVER) {
            amount += modifier;
        } else if (quality == LootItem.Quality.GOLD) {
            amount += modifier * 2;
        } else if (quality == LootItem.Quality.SAPPHIRE) {
            amount += modifier * 3;
        } else if (quality == LootItem.Quality.RUBY) {
            amount += modifier * 4;
        } else if (quality == LootItem.Quality.EMERALD) {
            amount += modifier * 5;
        } else if (quality == LootItem.Quality.DIAMOND) {
            amount += modifier * 10;
        }

        if (amount > 0) {
            if (world.isRemote) {
                this.addParticles(ParticleTypes.HAPPY_VILLAGER);
                this.playSound(SoundEvents.ENTITY_VILLAGER_YES, this.getSoundVolume(), this.getSoundPitch());
            }

            if (!world.isRemote) {
                this.playSound(SoundEvents.ENTITY_VILLAGER_YES, this.getSoundVolume(), this.getSoundPitch());
                StackHelper.giveItem(player, hand, new ItemStack(AtumItems.GOLD_COIN, amount));
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void addParticles(BasicParticleType particleType) {
        for (int amount = 0; amount < 5; ++amount) {
            double x = this.rand.nextGaussian() * 0.02D;
            double y = this.rand.nextGaussian() * 0.02D;
            double z = this.rand.nextGaussian() * 0.02D;
            this.world.addParticle(particleType, this.posX + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.posY + 1.0D + (double) (this.rand.nextFloat() * this.getHeight()), this.posZ + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), x, y, z);
        }
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("Riches", this.wealth);
        compound.putInt("TradeLevel", this.tradeLevel);

        if (this.buyingList != null) {
            compound.put("Offers", this.buyingList.getRecipiesAsTags());
        }
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.wealth = compound.getInt("Riches");
        this.tradeLevel = compound.getInt("TradeLevel");

        if (compound.contains("Offers", 10)) {
            CompoundNBT nbttagcompound = compound.getCompound("Offers");
            this.buyingList = new MerchantRecipeList(nbttagcompound);
        }
    }

    static class ItemsForCoins implements VillagerEntity.ITradeList {
        int price;
        Item buyingItem;
        VillagerEntity.PriceInfo buyingAmount;

        ItemsForCoins(int price, Item item, VillagerEntity.PriceInfo buyingAmount) {
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

    static class ILookAtTradePlayer extends LookAtGoal {
        private final SunspeakerEntity sunspeaker;

        ILookAtTradePlayer(SunspeakerEntity sunspeaker) {
            super(sunspeaker, PlayerEntity.class, 8.0F);
            this.sunspeaker = sunspeaker;
        }

        @Override
        public boolean shouldExecute() {
            if (this.sunspeaker.isTrading()) {
                this.closestEntity = this.sunspeaker.getCustomer();
                return true;
            } else {
                return false;
            }
        }
    }
}
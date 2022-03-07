package com.teammetallurgy.atum.entity.animal;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumLootTables;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Random;

public class ServalEntity extends Cat {
    private static final Ingredient BREEDING_ITEMS = Ingredient.of(AtumItems.SKELETAL_FISH.get());
    private CatTemptGoal temptGoal;
    public static final Map<Integer, ResourceLocation> SERVAL_TEXTURE_BY_ID = Util.make(Maps.newHashMap(), (m) -> {
        m.put(0, new ResourceLocation(Atum.MOD_ID, "textures/entity/serval/black.png"));
        m.put(1, new ResourceLocation(Atum.MOD_ID, "textures/entity/serval/white.png"));
        m.put(2, new ResourceLocation(Atum.MOD_ID, "textures/entity/serval/white_spotted.png"));
        m.put(3, new ResourceLocation(Atum.MOD_ID, "textures/entity/serval/beige.png"));
        m.put(4, new ResourceLocation(Atum.MOD_ID, "textures/entity/serval/beige_spotted.png"));
    });

    public ServalEntity(EntityType<? extends Cat> type, Level world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        this.temptGoal = new Cat.CatTemptGoal(this, 0.6D, BREEDING_ITEMS, true);
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new ServalMorningGiftGoal(this));
        this.goalSelector.addGoal(3, this.temptGoal);
        this.goalSelector.addGoal(5, new CatLieOnBedGoal(this, 1.1D, 8));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0D, 10.0F, 5.0F, false));
        this.goalSelector.addGoal(7, new CatSitOnBlockGoal(this, 0.8D));
        this.goalSelector.addGoal(8, new LeapAtTargetGoal(this, 0.3F));
        this.goalSelector.addGoal(9, new OcelotAttackGoal(this));
        this.goalSelector.addGoal(10, new BreedGoal(this, 0.8D));
        this.goalSelector.addGoal(11, new WaterAvoidingRandomStrollGoal(this, 0.8D, 1.0000001E-5F));
        this.goalSelector.addGoal(12, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>(this, Rabbit.class, false, null));
        this.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>(this, Turtle.class, false, Turtle.BABY_ON_LAND_SELECTOR));
    }

    @Override
    public boolean isFood(@Nonnull ItemStack stack) {
        return BREEDING_ITEMS.test(stack);
    }

    @Override
    protected float getStandingEyeHeight(@Nonnull Pose pose, @Nonnull EntityDimensions size) {
        return size.height * 0.9F;
    }

    @Override
    @Nonnull
    public ResourceLocation getResourceLocation() {
        return SERVAL_TEXTURE_BY_ID.getOrDefault(this.getCatType(), SERVAL_TEXTURE_BY_ID.get(0));
    }

    @Override
    public void setCatType(int type) {
        if (type < 0 || type > SERVAL_TEXTURE_BY_ID.size()) {
            type = this.random.nextInt(SERVAL_TEXTURE_BY_ID.size());
        }
        this.entityData.set(DATA_TYPE_ID, type);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.temptGoal != null && this.temptGoal.isRunning() && !this.isTame() && this.tickCount % 100 == 0) {
            this.playSound(SoundEvents.CAT_BEG_FOR_FOOD, 1.0F, 1.0F);
        }
    }

    @Override
    public Cat getBreedOffspring(@Nonnull ServerLevel serverLevel, @Nonnull AgeableMob ageableEntity) {
        ServalEntity serval = AtumEntities.SERVAL.get().create(serverLevel);
        if (serval != null && ageableEntity instanceof ServalEntity) {
            if (this.random.nextBoolean()) {
                serval.setCatType(this.getCatType());
            } else {
                serval.setCatType(((ServalEntity) ageableEntity).getCatType());
            }

            if (this.isTame()) {
                serval.setOwnerUUID(this.getOwnerUUID());
                serval.setTame(true);
                if (this.random.nextBoolean()) {
                    serval.setCollarColor(this.getCollarColor());
                } else {
                    serval.setCollarColor(((ServalEntity) ageableEntity).getCollarColor());
                }
            }
        }
        return serval;
    }

    static class ServalMorningGiftGoal extends Cat.CatRelaxOnOwnerGoal {
        private final Cat cat;

        public ServalMorningGiftGoal(Cat cat) {
            super(cat);
            this.cat = cat;
        }

        @Override
        public void stop() {
            this.cat.setLying(false);
            float f = this.cat.level.getTimeOfDay(1.0F);
            if (this.ownerPlayer.getSleepTimer() >= 100 && (double) f > 0.77D && (double) f < 0.8D && (double) this.cat.level.getRandom().nextFloat() < 0.7D) {
                this.giveGift();
            }

            this.onBedTicks = 0;
            this.cat.setRelaxStateOne(false);
            this.cat.getNavigation().stop();
        }

        private void giveGift() {
            Random random = this.cat.getRandom();
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
            mutablePos.set(this.cat.blockPosition());
            this.cat.randomTeleport(mutablePos.getX() + random.nextInt(11) - 5, mutablePos.getY() + random.nextInt(5) - 2, mutablePos.getZ() + random.nextInt(11) - 5, false);
            mutablePos.set(this.cat.blockPosition());
            LootTable lootTable = this.cat.level.getServer().getLootTables().get(AtumLootTables.GAMEPLAY_SERVAL_MORNING_GIFT);
            LootContext.Builder builder = (new LootContext.Builder((ServerLevel) this.cat.level)).withParameter(LootContextParams.ORIGIN, this.cat.position()).withParameter(LootContextParams.THIS_ENTITY, this.cat).withRandom(random);

            for (ItemStack stack : lootTable.getRandomItems(builder.create(LootContextParamSets.GIFT))) {
                this.cat.level.addFreshEntity(new ItemEntity(this.cat.level, (double) mutablePos.getX() - (double) Mth.sin(this.cat.yBodyRot * ((float) Math.PI / 180F)), mutablePos.getY(), (double) mutablePos.getZ() + (double) Mth.cos(this.cat.yBodyRot * ((float) Math.PI / 180F)), stack));
            }

        }
    }
}
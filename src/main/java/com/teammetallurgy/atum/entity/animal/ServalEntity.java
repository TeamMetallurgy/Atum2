package com.teammetallurgy.atum.entity.animal;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumLootTables;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Random;

public class ServalEntity extends CatEntity {
    private static final Ingredient BREEDING_ITEMS = Ingredient.fromItems(AtumItems.SKELETAL_FISH);
    private TemptGoal temptGoal;
    public static final Map<Integer, ResourceLocation> SERVAL_TEXTURE_BY_ID = Util.make(Maps.newHashMap(), (m) -> {
        m.put(1, new ResourceLocation(Atum.MOD_ID, "textures/entity/serval/black.png"));
        m.put(2, new ResourceLocation(Atum.MOD_ID, "textures/entity/serval/white.png"));
        m.put(3, new ResourceLocation(Atum.MOD_ID, "textures/entity/serval/white_spotted.png"));
        m.put(4, new ResourceLocation(Atum.MOD_ID, "textures/entity/serval/beige.png"));
        m.put(5, new ResourceLocation(Atum.MOD_ID, "textures/entity/serval/beige_spotted.png"));
    });

    public ServalEntity(EntityType<? extends CatEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        this.temptGoal = new CatEntity.TemptGoal(this, 0.6D, BREEDING_ITEMS, true);
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(1, new SitGoal(this));
        this.goalSelector.addGoal(2, new ServalMorningGiftGoal(this));
        this.goalSelector.addGoal(3, this.temptGoal);
        this.goalSelector.addGoal(5, new CatLieOnBedGoal(this, 1.1D, 8));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0D, 10.0F, 5.0F, false));
        this.goalSelector.addGoal(7, new CatSitOnBlockGoal(this, 0.8D));
        this.goalSelector.addGoal(8, new LeapAtTargetGoal(this, 0.3F));
        this.goalSelector.addGoal(9, new OcelotAttackGoal(this));
        this.goalSelector.addGoal(10, new BreedGoal(this, 0.8D));
        this.goalSelector.addGoal(11, new WaterAvoidingRandomWalkingGoal(this, 0.8D, 1.0000001E-5F));
        this.goalSelector.addGoal(12, new LookAtGoal(this, PlayerEntity.class, 10.0F));
        this.targetSelector.addGoal(1, new NonTamedTargetGoal<>(this, RabbitEntity.class, false, null));
        this.targetSelector.addGoal(1, new NonTamedTargetGoal<>(this, TurtleEntity.class, false, TurtleEntity.TARGET_DRY_BABY));
    }

    @Override
    public boolean isBreedingItem(@Nonnull ItemStack stack) {
        return BREEDING_ITEMS.test(stack);
    }

    @Override
    @Nonnull
    public ResourceLocation getCatTypeName() {
        return SERVAL_TEXTURE_BY_ID.getOrDefault(this.getCatType(), SERVAL_TEXTURE_BY_ID.get(0));
    }

    @Override
    public void setCatType(int type) {
        if (type < 0 || type > SERVAL_TEXTURE_BY_ID.size()) {
            type = this.rand.nextInt(SERVAL_TEXTURE_BY_ID.size());
        }
        this.dataManager.set(CAT_TYPE, type);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.temptGoal != null && this.temptGoal.isRunning() && !this.isTamed() && this.ticksExisted % 100 == 0) {
            this.playSound(SoundEvents.ENTITY_CAT_BEG_FOR_FOOD, 1.0F, 1.0F);
        }
    }

    @Override
    public CatEntity func_241840_a(@Nonnull ServerWorld serverWorld, @Nonnull AgeableEntity ageableEntity) {
        ServalEntity serval = AtumEntities.SERVAL.create(serverWorld);
        if (ageableEntity instanceof ServalEntity) {
            if (this.rand.nextBoolean()) {
                serval.setCatType(this.getCatType());
            } else {
                serval.setCatType(((CatEntity)ageableEntity).getCatType());
            }

            if (this.isTamed()) {
                serval.setOwnerId(this.getOwnerId());
                serval.setTamed(true);
                if (this.rand.nextBoolean()) {
                    serval.setCollarColor(this.getCollarColor());
                } else {
                    serval.setCollarColor(((CatEntity)ageableEntity).getCollarColor());
                }
            }
        }
        return serval;
    }

    static class ServalMorningGiftGoal extends CatEntity.MorningGiftGoal {
        private final CatEntity cat;

        public ServalMorningGiftGoal(CatEntity cat) {
            super(cat);
            this.cat = cat;
        }

        @Override
        public void resetTask() {
            this.cat.func_213419_u(false);
            float f = this.cat.world.func_242415_f(1.0F);
            if (this.owner.getSleepTimer() >= 100 && (double) f > 0.77D && (double) f < 0.8D && (double) this.cat.world.getRandom().nextFloat() < 0.7D) {
                this.giveGift();
            }

            this.tickCounter = 0;
            this.cat.func_213415_v(false);
            this.cat.getNavigator().clearPath();
        }

        private void giveGift() {
            Random random = this.cat.getRNG();
            BlockPos.Mutable mutablePos = new BlockPos.Mutable();
            mutablePos.setPos(this.cat.getPosition());
            this.cat.attemptTeleport(mutablePos.getX() + random.nextInt(11) - 5, mutablePos.getY() + random.nextInt(5) - 2, mutablePos.getZ() + random.nextInt(11) - 5, false);
            mutablePos.setPos(this.cat.getPosition());
            LootTable lootTable = this.cat.world.getServer().getLootTableManager().getLootTableFromLocation(AtumLootTables.GAMEPLAY_SERVAL_MORNING_GIFT);
            LootContext.Builder builder = (new LootContext.Builder((ServerWorld) this.cat.world)).withParameter(LootParameters.field_237457_g_, this.cat.getPositionVec()).withParameter(LootParameters.THIS_ENTITY, this.cat).withRandom(random);

            for (ItemStack stack : lootTable.generate(builder.build(LootParameterSets.GIFT))) {
                this.cat.world.addEntity(new ItemEntity(this.cat.world, (double) mutablePos.getX() - (double) MathHelper.sin(this.cat.renderYawOffset * ((float) Math.PI / 180F)), mutablePos.getY(), (double) mutablePos.getZ() + (double) MathHelper.cos(this.cat.renderYawOffset * ((float) Math.PI / 180F)), stack));
            }

        }
    }
}
package com.teammetallurgy.atum.entity.villager;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.ITexture;
import com.teammetallurgy.atum.init.AtumDataSerializer;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.merchant.villager.VillagerData;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.villager.VillagerType;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Objects;

public class AtumVillagerEntity extends VillagerEntity implements ITexture {
    private static final DataParameter<AtumVillagerData> ATUM_VILLAGER_DATA = EntityDataManager.createKey(AtumVillagerEntity.class, AtumDataSerializer.VILLAGER_DATA);
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(AtumVillagerEntity.class, DataSerializers.VARINT);
    private String texturePath;

    public AtumVillagerEntity(EntityType<? extends VillagerEntity> type, World world) {
        super(type, world, VillagerType.DESERT); //Type not used, by Atum villagers
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(ATUM_VILLAGER_DATA, new AtumVillagerData(VillagerProfession.NONE, 1, Race.HUMAN, false));
        this.dataManager.register(VARIANT, 0);
    }

    @Override
    @Nonnull
    public VillagerData getVillagerData() {
        return this.dataManager.get(ATUM_VILLAGER_DATA);
    }

    @Override
    public void setVillagerData(VillagerData data) {
        AtumVillagerData atumVillagerData = (AtumVillagerData) this.getVillagerData();
        if (atumVillagerData.getProfession() != data.getProfession()) {
            this.offers = null;
        }
        this.dataManager.set(ATUM_VILLAGER_DATA, (AtumVillagerData) data);
    }

    @Override
    public VillagerEntity func_241840_a(@Nonnull ServerWorld serverWorld, @Nonnull AgeableEntity ageableEntity) {
        AtumVillagerEntity atumVillagerEntity = new AtumVillagerEntity(AtumEntities.VILLAGER, serverWorld);
        atumVillagerEntity.onInitialSpawn(serverWorld, serverWorld.getDifficultyForLocation(atumVillagerEntity.getPosition()), SpawnReason.BREEDING, null, null);
        return atumVillagerEntity;
    }

    protected int getVariantAmount() {
        return 6;
    }

    private void setVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
        this.texturePath = null;
    }

    private int getVariant() {
        return this.dataManager.get(VARIANT);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public String getTexture() {
        if (this.texturePath == null) {
            AtumVillagerData atumVillagerData = (AtumVillagerData) this.getVillagerData();
            String gender = atumVillagerData.isFemale() ? "female" : "male";
            this.texturePath = new ResourceLocation(Atum.MOD_ID, "textures/entity/villager/" + atumVillagerData.getRace() + "/" + gender + "_" + this.getVariant()) + ".png";
        }
        return this.texturePath;
    }
}
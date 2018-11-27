package com.teammetallurgy.atum.entity;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.biome.*;
import com.teammetallurgy.atum.world.biome.base.AtumBiome;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityDesertRabbit extends EntityRabbit {

    public EntityDesertRabbit(World world) {
        super(world);
        this.spawnableBlock = AtumBlocks.SAND;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.32D);
    }

    @Override
    protected int getRandomRabbitType() {
        AtumBiome biome = (AtumBiome) this.world.getBiome(new BlockPos(this));
        int i = this.rand.nextInt(100);

        if (biome instanceof BiomeSandPlains) {
            return i <= 80 ? 0 : 1;
        } else if (biome instanceof BiomeSandDunes) {
            return i <= 60 ? 1 : 2;
        } else if (biome instanceof BiomeSandHills) {
            return i <= 30 ? 1 : 2;
        } else if (biome instanceof BiomeLimestoneMountains) {
            return i <= 30 ? 2 : 3;
        } else if (biome instanceof BiomeLimestoneCrags) {
            return i <= 30 ? 3 : 4;
        } else if (biome instanceof BiomeDeadwoodForest) {
            return i <= 50 ? 2 : 3;
        } else if (biome instanceof BiomeOasis) {
            return i <= 50 ? 2 : 3;
        } else if (biome instanceof BiomeDeadOasis) {
            return i <= 33 ? 2 : (i <= 66 ? 3 : 4);
        } else if (biome instanceof BiomeDriedRiver) {
            return i <= 50 ? 1 : 2;
        } else {
            return 0;
        }
    }
}
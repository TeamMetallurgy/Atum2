package com.teammetallurgy.atum.world;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.biome.base.AtumBiomeProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class WorldProviderAtum extends WorldProvider {

    @Override
    @Nonnull
    public DimensionType getDimensionType() {
        return AtumDimension.ATUM;
    }

    @Override
    protected void init() {
        this.hasSkyLight = true;
        this.biomeProvider = new AtumBiomeProvider(world.getWorldInfo());
    }

    @Override
    @Nonnull
    public IChunkGenerator createChunkGenerator() {
        return new ChunkGeneratorAtum(world, world.getSeed(), true, world.getWorldInfo().getGeneratorOptions());
    }

    @Override
    public boolean canCoordinateBeSpawn(int x, int z) {
        BlockPos blockpos = new BlockPos(x, 0, z);

        if (this.world.getBiome(blockpos).ignorePlayerSpawnSuitability()) {
            return true;
        } else {
            return this.world.getGroundAboveSeaLevel(blockpos).getBlock() == AtumBlocks.SAND;
        }
    }

    /*@Override
    protected void generateLightBrightnessTable() { //TODO?
        float f = 0.0F;

        for (int i = 0; i <= 15; ++i) {
            float scaledBrightness = (float) i / 15.0F;
            float f1 = 1.0F - scaledBrightness;
            if (i < 5) {
                super.lightBrightnessTable[i] = 0.5F * scaledBrightness / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
            } else {
                super.lightBrightnessTable[i] = scaledBrightness / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
            }
        }
    }*/

    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        int i = (int) (worldTime % 48000L);
        float f = ((float) i + partialTicks) / 48000.0F - 0.25F;

        if (f < 0.0F) {
            ++f;
        }
        if (f > 1.0F) {
            --f;
        }

        float f1 = 1.0F - (float) ((Math.cos((double) f * Math.PI) + 1.0D) / 2.0D);
        f = f + (f1 - f) / 3.0F;
        return f;
    }

    @Override
    public int getMoonPhase(long worldTime) {
        return (int) (worldTime / 48000L % 8L + 8L) % 8;
    }

    @Override
    @Nonnull
    @SideOnly(Side.CLIENT)
    public Vec3d getFogColor(float par1, float par2) {
        float f = MathHelper.cos(par1 * 3.1415927F * 2.0F) * 2.0F + 0.5F;
        if (f < 0.2F) {
            f = 0.2F;
        }

        if (f > 1.0F) {
            f = 1.0F;
        }

        float f1 = 0.9F * f;
        float f2 = 0.75F * f;
        float f3 = 0.6F * f;
        return new Vec3d((double) f1, (double) f2, (double) f3);
    }
}
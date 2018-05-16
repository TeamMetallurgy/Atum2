package com.teammetallurgy.atum.world.gen.structure;

import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;

import javax.annotation.Nonnull;

public class MapGenAtumMineshaft extends MapGenStructure {
    private double chance = 0.004D;

    public MapGenAtumMineshaft() {
    }

    @Override
    @Nonnull
    public String getStructureName() {
        return String.valueOf(new ResourceLocation(Constants.MOD_ID, "Mineshaft"));
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        return this.rand.nextDouble() < this.chance && this.rand.nextInt(80) < Math.max(Math.abs(chunkX), Math.abs(chunkZ));
    }

    @Override
    public BlockPos getNearestStructurePos(@Nonnull World world, @Nonnull BlockPos pos, boolean findUnexplored) {
        int i = 1000;
        int j = pos.getX() >> 4;
        int k = pos.getZ() >> 4;

        for (int l = 0; l <= i; ++l) {
            for (int i1 = -l; i1 <= l; ++i1) {
                boolean flag = i1 == -l || i1 == l;

                for (int j1 = -l; j1 <= l; ++j1) {
                    boolean flag1 = j1 == -l || j1 == l;

                    if (flag || flag1) {
                        int k1 = j + i1;
                        int l1 = k + j1;
                        this.rand.setSeed((long) (k1 ^ l1) ^ world.getSeed());
                        this.rand.nextInt();

                        if (this.canSpawnStructureAtCoords(k1, l1) && (!findUnexplored || !world.isChunkGeneratedAt(k1, l1))) {
                            return new BlockPos((k1 << 4) + 8, 64, (l1 << 4) + 8);
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    @Nonnull
    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        int chance = rand.nextInt(100);
        Type type;
        if (chance > 50) {
            type = Type.LIMESTONE;
        } else {
            type = Type.DEADWOOD;
        }
        return new StructureAtumMineshaftStart(this.world, this.rand, chunkX, chunkZ, type);
    }

    public static enum Type {
        DEADWOOD,
        LIMESTONE;

        public static Type byOrdinal(int ordinal) {
            return ordinal >= 0 && ordinal < values().length ? values()[ordinal] : DEADWOOD;
        }
    }
}
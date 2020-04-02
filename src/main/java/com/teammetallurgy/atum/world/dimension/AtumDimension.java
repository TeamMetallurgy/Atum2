package com.teammetallurgy.atum.world.dimension;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.SandLayersBlock;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.network.NetworkHandler;
import com.teammetallurgy.atum.network.packet.StormStrengthPacket;
import com.teammetallurgy.atum.network.packet.WeatherPacket;
import com.teammetallurgy.atum.utils.AtumConfig;
import com.teammetallurgy.atum.world.biome.provider.AtumBiomeProvider;
import com.teammetallurgy.atum.world.biome.provider.AtumBiomeProviderSettings;
import com.teammetallurgy.atum.world.biome.provider.AtumBiomeProviderTypes;
import com.teammetallurgy.atum.world.gen.AtumChunkGenerator;
import com.teammetallurgy.atum.world.gen.AtumChunkGeneratorType;
import com.teammetallurgy.atum.world.gen.AtumGenSettings;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DerivedWorldInfo;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class AtumDimension extends Dimension {
    private static BlockPos usePos;
    public boolean hasStartStructureSpawned;

    public AtumDimension(World world, DimensionType dimensionType) {
        super(world, dimensionType, 0.0F /*Brightness. Look into?*/);
        //init(); //TODO
    }

    @SubscribeEvent
    public static void onUseBucket(PlayerInteractEvent.RightClickBlock event) {
        if (AtumConfig.WORLD_GEN.waterLevel.get() > 0) {
            usePos = event.getPos();
        } else {
            usePos = null;
        }
    }

    @Override
    public boolean doesWaterVaporize() {
        if (usePos != null) {
            return world.getBiome(usePos) != AtumBiomes.OASIS && usePos.getY() >= AtumConfig.WORLD_GEN.waterLevel.get();
        } else {
            return false;
        }
    }

    @Override
    public void setWorldTime(long time) {
        if (time == 24000L && this.world.getWorldInfo() instanceof DerivedWorldInfo) {
            ((DerivedWorldInfo) this.world.getWorldInfo()).delegate.setDayTime(time); //Workaround for making sleeping work in Atum
        }
        super.setWorldTime(time);
    }

    @Override
    @Nonnull
    public ChunkGenerator<?> createChunkGenerator() {
        BiomeProviderType<AtumBiomeProviderSettings, AtumBiomeProvider> biomeType = AtumBiomeProviderTypes.ATUM;
        ChunkGeneratorType<AtumGenSettings, AtumChunkGenerator> chunkType = AtumChunkGeneratorType.ATUM;
        AtumGenSettings genSettings = chunkType.createSettings();
        AtumBiomeProviderSettings biomeSettings = biomeType.func_226840_a_(this.world.getWorldInfo()).setGeneratorSettings(genSettings);
        return chunkType.create(this.world, biomeType.create(biomeSettings), genSettings);
    }

    @Override
    @Nullable
    public BlockPos findSpawn(@Nonnull ChunkPos chunkPos, boolean checkValid) { //Copied from OverworldDimension
        for (int x = chunkPos.getXStart(); x <= chunkPos.getXEnd(); ++x) {
            for (int z = chunkPos.getZStart(); z <= chunkPos.getZEnd(); ++z) {
                BlockPos pos = this.findSpawn(x, z, checkValid);
                if (pos != null) {
                    return pos;
                }
            }
        }
        return null;
    }

    @Override
    @Nullable
    public BlockPos findSpawn(int posX, int posZ, boolean checkValid) { //Copied from OverworldDimension. Else if removed
        BlockPos.Mutable mutablePos = new BlockPos.Mutable(posX, 0, posZ);
        Biome biome = this.world.getBiome(mutablePos);
        BlockState state = biome.getSurfaceBuilderConfig().getTop();
        if (checkValid && !state.getBlock().isIn(BlockTags.VALID_SPAWN)) {
            return null;
        } else {
            Chunk chunk = this.world.getChunk(posX >> 4, posZ >> 4);
            int x = chunk.getTopBlockY(Heightmap.Type.MOTION_BLOCKING, posX & 15, posZ & 15);
            if (x < 0) {
                return null;
            } else {
                for (int y = x + 1; y >= 0; --y) {
                    mutablePos.setPos(posX, y, posZ);
                    BlockState stateMutable = this.world.getBlockState(mutablePos);
                    if (!stateMutable.getFluidState().isEmpty()) {
                        break;
                    }
                    if (stateMutable.equals(state)) {
                        return mutablePos.up().toImmutable();
                    }
                }
                return null;
            }
        }
    }

    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) { //Copied from OverworldDimension
        double d0 = MathHelper.frac((double) worldTime / 24000.0D - 0.25D);
        double d1 = 0.5D - Math.cos(d0 * Math.PI) / 2.0D;
        return (float) (d0 * 2.0D + d1) / 3.0F;
    }

    @Override
    public boolean isSurfaceWorld() {
        return true;
    }

    @Override
    @Nonnull
    public Vec3d getFogColor(float celestialAngle, float partialTicks) {
        float f = MathHelper.cos((float) (celestialAngle * Math.PI * 2.0F)) * 2.0F + 0.5F;
        if (f < 0.2F) {
            f = 0.2F;
        }

        if (f > 1.0F) {
            f = 1.0F;
        }

        // Darken fog as sandstorm builds
        // f *= (1 - this.stormStrength) * 0.8 + 0.2;

        float f1 = 0.9F * f;
        float f2 = 0.75F * f;
        float f3 = 0.6F * f;
        return new Vec3d(f1, f2, f3);
    }

    @Override
    public boolean isDaytime() {
        return this.getWorld().getSkylightSubtracted() < 4;
    }

    @Override
    public boolean canRespawnHere() {
        return true;
    }

    @Override
    public boolean doesXZShowFog(int x, int z) {
        return true; //TODO Test
    }

    @Override
    public void onWorldSave() {
        /*CompoundNBT tagCompound = new CompoundNBT(); //TODO
        tagCompound.putBoolean("HasStartStructureSpawned", hasStartStructureSpawned);
        tagCompound.putBoolean("IsStorming", isStorming);
        world.getWorldInfo().setDimensionData(this.world.dimension.getType(), tagCompound);*/
    }

    protected void init() {
        CompoundNBT tagCompound = this.world.getWorldInfo().getDimensionData(this.getType());
        this.hasStartStructureSpawned = this.world instanceof ServerWorld && tagCompound.getBoolean("HasStartStructureSpawned");
        this.isStorming = this.world instanceof ServerWorld && tagCompound.getBoolean("IsStorming");
    }

    //Sandstorm
    public boolean isStorming;
    public int stormTime;
    public float prevStormStrength;
    public float stormStrength;
    private int updateLCG = (new Random()).nextInt();
    private long lastUpdateTime;

    @Override
    public void calculateInitialWeather() {
        super.calculateInitialWeather();
        if (isStorming) {
            stormStrength = 1;
        }
    }

    private boolean canPlaceSandAt(BlockPos pos, Biome biome) {
        BlockState state = world.getBlockState(pos.down());
        /*if (state.getBlock() == AtumBlocks.SAND || state.getBlock() == AtumBlocks.LIMESTONE_GRAVEL || !ChunkGeneratorAtum.canPlaceSandLayer(world, pos, biome)) { //TODO
            return false;
        }
        state = world.getBlockState(pos);
        if (state.getBlock().isReplaceable(world, pos)) {
            state = world.getBlockState(pos.down());
            BlockFaceShape blockFaceShape = state.getBlockFaceShape(world, pos.down(), Direction.UP);
            return blockFaceShape == BlockFaceShape.SOLID || state.getBlock().isLeaves(state, world, pos.down());
        }*/
        return false;
    }

    @Override
    public void updateWeather(Runnable defaultLogic) {
        if (!world.isRemote) {
            ServerWorld serverWorld = (ServerWorld) world;
            int cleanWeatherTime = serverWorld.getWorldInfo().getClearWeatherTime();

            if (cleanWeatherTime > 0) {
                --cleanWeatherTime;
                serverWorld.getWorldInfo().setClearWeatherTime(cleanWeatherTime);
                this.stormTime = isStorming ? 1 : 2;
            }

            if (stormTime <= 0) {
                if (isStorming) {
                    stormTime = this.world.rand.nextInt(6000) + 6000;
                } else {
                    stormTime = this.world.rand.nextInt(168000) + 12000;
                }
                NetworkHandler.sendToDimension(new WeatherPacket(isStorming, stormTime), serverWorld, this.getType());
            } else {
                stormTime--;
                if (stormTime <= 0) {
                    isStorming = !isStorming;
                }
            }

            prevStormStrength = stormStrength;
            if (isStorming) {
                stormStrength += 1 / (float) (20 * AtumConfig.SANDSTORM.sandstormTransitionTime.get());
            } else {
                stormStrength -= 1 / (float) (20 * AtumConfig.SANDSTORM.sandstormTransitionTime.get());
            }
            stormStrength = MathHelper.clamp(stormStrength, 0, 1);

            if (stormStrength != prevStormStrength || lastUpdateTime < System.currentTimeMillis() - 1000) {
                NetworkHandler.sendToDimension(new StormStrengthPacket(stormStrength), serverWorld, this.getType());
                lastUpdateTime = System.currentTimeMillis();
            }

            /*Iterator<Chunk> iterator = world.getPersistentChunkIterable(((ServerWorld) world).getPlayerChunkMap().getChunkIterator()); //TODO
            while (iterator.hasNext()) {
                Chunk chunk = iterator.next();*/
            //int x = chunk.x * 16;
            //int z = chunk.z * 16;

            if (world.rand.nextInt(40) == 0) {
                this.updateLCG = this.updateLCG * 3 + 1013904223;
                int j2 = this.updateLCG >> 2;
                BlockPos pos = BlockPos.ZERO /*world.getPrecipitationHeight(new BlockPos(x + (j2 & 15), 0, z + (j2 >> 8 & 15)))*/; //TODO
                BlockPos posDown = pos.down();

                if (world.isAreaLoaded(posDown, 1)) {// Forge: check area to avoid loading neighbors in unloaded chunks
                    BlockState sandState = world.getBlockState(pos);
                    if (stormStrength > 0.9f) {
                        if (sandState.getBlock() == AtumBlocks.SAND_LAYERED) {
                            int layers = sandState.get(SandLayersBlock.LAYERS);
                            if (layers < 3) {
                                world.setBlockState(pos, sandState.with(SandLayersBlock.LAYERS, ++layers));
                            }
                        } else if (canPlaceSandAt(pos, world.getBiome(pos))) {
                            world.setBlockState(pos, AtumBlocks.SAND_LAYERED.getDefaultState());
                        }
                    }
                }
            }
            //}
        }
    }
}
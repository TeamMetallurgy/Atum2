package com.teammetallurgy.atum.world;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.SandLayersBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.network.NetworkHandler;
import com.teammetallurgy.atum.network.packet.weather.StormStrengthPacket;
import com.teammetallurgy.atum.network.packet.weather.WeatherPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.ServerLevelData;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.Optional;

public class SandstormHandler {
    public static final SandstormHandler INSTANCE = new SandstormHandler();
    public int stormTime;
    public float prevStormStrength;
    public float stormStrength;
    private long lastUpdateTime;

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onWorldLoad(LevelEvent.Load event) { // calculateInitialWeather
        if (event.getLevel() instanceof ServerLevel serverLevel && serverLevel.dimension() == Atum.ATUM && DimensionHelper.getData(serverLevel).isStorming()) {
            this.stormStrength = 1.0F;
        }
    }

    @SubscribeEvent
    public void onPreServerTick(TickEvent.LevelTickEvent event) {
        if (event.level.dimension() == Atum.ATUM) {
            updateWeather(event.level);
        }
    }

    private boolean canPlaceSandAt(ServerLevel serverLevel, BlockPos pos) {
        BlockState state = serverLevel.getBlockState(pos.below());
        return ((state.getBlock() != AtumBlocks.STRANGE_SAND.get() && state.getBlock() != AtumBlocks.LIMESTONE_GRAVEL.get()) || state.is(BlockTags.LEAVES)) && DimensionHelper.canPlaceSandLayer(serverLevel, pos);
    }

    public void updateWeather(Level level) {
        if (level instanceof ServerLevel serverLevel && !level.isClientSide) {
            ServerLevelData worldInfo = serverLevel.getServer().getWorldData().overworldData();
            int cleanWeatherTime = worldInfo.getClearWeatherTime();

            if (cleanWeatherTime > 0) {
                --cleanWeatherTime;
                this.stormTime = DimensionHelper.getData(serverLevel).isStorming() ? 1 : 2;
            }

            if (this.stormTime <= 0) {
                if (DimensionHelper.getData(serverLevel).isStorming()) {
                    this.stormTime = serverLevel.random.nextInt(6000) + 6000;
                } else {
                    this.stormTime = serverLevel.random.nextInt(168000) + 12000;
                }
                DimensionHelper.getData(serverLevel).setStorming(DimensionHelper.getData(serverLevel).isStorming());
                NetworkHandler.sendToDimension(new WeatherPacket(this.stormTime), serverLevel, Atum.ATUM);
            } else {
                this.stormTime--;
                if (this.stormTime <= 0) {
                    DimensionHelper.getData(serverLevel).setStorming(!DimensionHelper.getData(serverLevel).isStorming());
                }
            }

            worldInfo.setClearWeatherTime(cleanWeatherTime);

            this.prevStormStrength = this.stormStrength;
            if (DimensionHelper.getData(serverLevel).isStorming()) {
                this.stormStrength += 1.0F / (float) (20 * AtumConfig.SANDSTORM.sandstormTransitionTime.get());
            } else {
                this.stormStrength -= 1.0F / (float) (20 * AtumConfig.SANDSTORM.sandstormTransitionTime.get());
            }
            this.stormStrength = Mth.clamp(this.stormStrength, 0.0F, 1.0F);

            if (this.stormStrength != this.prevStormStrength || this.lastUpdateTime < System.currentTimeMillis() - 1000) {
                NetworkHandler.sendToDimension(new StormStrengthPacket(this.stormStrength), serverLevel, Atum.ATUM);
                this.lastUpdateTime = System.currentTimeMillis();
            }

            try {
                if (AtumConfig.SANDSTORM.sandstormSandLayerChance.get() > 0 && serverLevel.random.nextInt(AtumConfig.SANDSTORM.sandstormSandLayerChance.get()) == 0) {
                    if (this.stormStrength > 0.9F) {
                        ChunkMap chunkManager = serverLevel.getChunkSource().chunkMap;

                        chunkManager.getChunks().forEach(chunkHolder -> {
                            Optional<LevelChunk> optionalChunk = chunkHolder.getEntityTickingChunkFuture().getNow(ChunkHolder.UNLOADED_LEVEL_CHUNK).left();
                            if (optionalChunk.isPresent()) {
                                ChunkPos chunkPos = optionalChunk.get().getPos();
                                if (!chunkManager.getPlayersCloseForSpawning(chunkPos).isEmpty()) {
                                    BlockPos pos = serverLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, serverLevel.getBlockRandomPos(chunkPos.getMinBlockX(), 0, chunkPos.getMinBlockZ(), 15));
                                    BlockPos posDown = pos.below();

                                    if (serverLevel.isAreaLoaded(posDown, 1)) {
                                        BlockState sandState = serverLevel.getBlockState(pos);
                                        if (sandState.getBlock() == AtumBlocks.STRANGE_SAND_LAYERED.get()) {
                                            int layers = sandState.getValue(SandLayersBlock.LAYERS);
                                            if (layers < 3) {
                                                serverLevel.setBlockAndUpdate(pos, sandState.setValue(SandLayersBlock.LAYERS, ++layers));
                                            }
                                        } else if (this.canPlaceSandAt(serverLevel, pos)) {
                                            serverLevel.setBlockAndUpdate(pos, AtumBlocks.STRANGE_SAND_LAYERED.get().defaultBlockState());
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            } catch (Exception e) {
                Atum.LOG.error("Error occurred while Sandstorm attempted to place Sand Layer");
            }
        }
    }
}
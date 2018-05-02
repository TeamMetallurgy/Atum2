package com.teammetallurgy.atum.world;

import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

import javax.annotation.Nonnull;

public class AtumWorldServer extends WorldServer {

    public AtumWorldServer(MinecraftServer server, ISaveHandler saveHandler, WorldInfo info, int dimensionId, Profiler profiler) {
        super(server, saveHandler, info, dimensionId, profiler);
    }

    @Override
    @Nonnull
    public Teleporter getDefaultTeleporter() {
        return new AtumTeleporter(this);
    }
}
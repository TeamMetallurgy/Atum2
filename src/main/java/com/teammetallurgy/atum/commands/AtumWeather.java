package com.teammetallurgy.atum.commands;

import java.util.Random;

import com.teammetallurgy.atum.utils.AtumConfig;
import com.teammetallurgy.atum.world.WorldProviderAtum;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class AtumWeather extends CommandBase {

	@Override
	public String getName() {
		return "atumweather";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "atumweather";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		System.out.println("test " + server.getWorld(AtumConfig.DIMENSION_ID).isRemote);

		
		World world = server.getWorld(AtumConfig.DIMENSION_ID);
		if(world != null && world.provider instanceof WorldProviderAtum) {
			WorldProviderAtum atum = (WorldProviderAtum) world.provider;
			
            int time = (300 + (new Random()).nextInt(600)) * 20;

            if (args.length >= 2)
            {
                time = parseInt(args[1], 1, 1000000) * 20;
            }
			
			if(args[0].equals("clear")) {
				atum.isStorming = false;
				atum.stormTime = time == -1 ? 15000 : time;
			} else if(args[0].equals("storm") || args[0].equals("sandstorm")) {
				atum.isStorming = true;
				atum.stormTime = time == -1 ? 15000 : time;
			}
		}
	}
}

package com.teammetallurgy.atum.commands;

import com.teammetallurgy.atum.world.AtumDimensionRegistration;
import com.teammetallurgy.atum.world.WorldProviderAtum;
import net.minecraft.command.CommandException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AtumWeather {

	@Override
	@Nonnull
	public String getName() {
		return "atumweather";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	@Nonnull
	public String getUsage(@Nonnull ICommandSender sender) {
		return "atum.commands.weather.usage";
	}

	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
		World world = server.getWorld(AtumDimensionRegistration.ATUM);

		if (world.dimension instanceof WorldProviderAtum) {
			WorldProviderAtum atum = (WorldProviderAtum) world.dimension;

			int time = (300 + (new Random()).nextInt(600)) * 20;

			if (args.length >= 2) {
				time = parseInt(args[1], 1, 1000000) * 20;
			}

			if (args.length <= 0) {
				throw new WrongUsageException("atum.commands.weather.usage");
			} else {
				if (args[0].equals("clear")) {
					atum.isStorming = false;
					atum.stormTime = time == -1 ? 15000 : time;
					notifyCommandListener(sender, this, "commands.weather.clear");
				} else if (args[0].equals("storm") || args[0].equals("sandstorm")) {
					atum.isStorming = true;
					atum.stormTime = time == -1 ? 15000 : time;
					notifyCommandListener(sender, this, "atum.commands.weather.sandstorm");
				} else {
					throw new WrongUsageException("atum.commands.weather.usage");
				}
			}
		}
	}

	@Override
	@Nonnull
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		return args.length == 1 ? getListOfStringsMatchingLastWord(args, "clear", "sandstorm") : Collections.emptyList();
	}
}
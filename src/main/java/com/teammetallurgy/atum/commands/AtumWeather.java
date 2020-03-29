package com.teammetallurgy.atum.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class AtumWeather { //TODO

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(Commands.literal("atumweather").requires((p) -> p.hasPermissionLevel(2))
						.executes((c) -> sendUsage(c.getSource()))
				/*.then(Commands.argument())*/);

	}

	/*@Override
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
	}*/

	private static int sendUsage(CommandSource source) {
		source.sendFeedback(new TranslationTextComponent("atum.commands.weather.usage"), true);
		return 0; //TODO?
	}
}
package com.teammetallurgy.atum.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.teammetallurgy.atum.world.dimension.AtumDimension;
import com.teammetallurgy.atum.world.dimension.AtumDimensionType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.Random;

public class AtumWeather {

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(Commands.literal("atumweather").requires((p) -> p.hasPermissionLevel(2)).executes(c -> sendUsage(c.getSource()))
				.then(Commands.literal("clear").executes(c -> execute(c.getSource(), false, 0)).then(Commands.argument("time", IntegerArgumentType.integer(-1)).executes(c -> execute(c.getSource(), false, IntegerArgumentType.getInteger(c, "time")))))
				.then(Commands.literal("sandstorm").executes(c -> execute(c.getSource(), true, 0)).then(Commands.argument("time", IntegerArgumentType.integer(-1)).executes(c -> execute(c.getSource(), true, IntegerArgumentType.getInteger(c, "time"))))));
	}

	private static int sendUsage(CommandSource source) {
		source.sendFeedback(new TranslationTextComponent("atum.commands.weather.usage"), true);
		return 0;
	}

	private static int execute(CommandSource source, boolean isSandstorm, int time) {
		World world = source.getWorld();
		if (world.getDimension().getType() == AtumDimensionType.ATUM) {
			AtumDimension atum = (AtumDimension) world.getDimension();
			AtumDimension.DATA.setStorming(isSandstorm);
			atum.stormTime = time == -1 ? 1500 : time != 0 ? Math.min(time, 1000000) * 20 : (300 + (new Random()).nextInt(600)) * 20;
			if (isSandstorm) {
				source.sendFeedback(new TranslationTextComponent("atum.commands.weather.sandstorm"), true);
			} else {
				source.sendFeedback(new TranslationTextComponent("commands.weather.set.clear"), true);
			}
			return 0;
		}
		return 0;
	}
}
package me.lbuddyboy.core.commands.params;

import me.blazingtide.zetsu.adapters.ParameterAdapter;
import me.blazingtide.zetsu.schema.CachedCommand;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 23/10/2021 / 10:46 AM
 * LBuddyBoy Development / me.lbuddyboy.core.commands.params
 */
public class GamemodeParameterType implements ParameterAdapter<GameMode> {

	@Override
	public GameMode process(String str) {
		if (str.equalsIgnoreCase("c")) {
			return GameMode.CREATIVE;
		} else if (str.equalsIgnoreCase("a")) {
			return GameMode.ADVENTURE;
		} else if (str.equalsIgnoreCase("s")) {
			return GameMode.SURVIVAL;
		}

		return GameMode.valueOf(str.toUpperCase());
	}

	@Override
	public void processException(CommandSender sender, String given, Exception exception) {
		sender.sendMessage(ChatColor.RED + "'" + given + "' could not be found.");
	}

	@Override
	public @Nullable List<String> processTabComplete(@NotNull CommandSender sender, @NotNull CachedCommand command) {
		return Arrays.asList("c", "creative", "s", "survival", "a", "adventure");
	}
}

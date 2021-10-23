package me.lbuddyboy.core.commands.params;

import me.blazingtide.zetsu.adapters.ParameterAdapter;
import me.blazingtide.zetsu.schema.CachedCommand;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 23/10/2021 / 10:46 AM
 * LBuddyBoy Development / me.lbuddyboy.core.commands.params
 */
public class RankParameterType implements ParameterAdapter<Rank> {

	@Override
	public Rank process(String str) {
		return Core.getInstance().getRankHandler().getByName(str);
	}

	@Override
	public void processException(CommandSender sender, String given, Exception exception) {
		sender.sendMessage(ChatColor.RED + "'" + given + "' could not be found.");
	}

	@Override
	public @Nullable List<String> processTabComplete(@NotNull CommandSender sender, @NotNull CachedCommand command) {
		List<String> toReturn = new ArrayList<>();
		for (Rank rank : Core.getInstance().getRankHandler().getRanks()) {
			toReturn.add(rank.getName());
		}
		return toReturn;
	}
}

package me.lbuddyboy.core.rank.command.param;

import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.libraries.command.ParameterType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 7:04 PM
 * lCore / me.lbuddyboy.core.rank.command.param
 */
public class RankParameterType implements ParameterType<Rank> {
	@Override
	public Rank transform(CommandSender sender, String source) {

		for (Rank rank : Core.getInstance().getRankHandler().getRanks()) {
			if (rank.getName().equals(source)) {
				return rank;
			}
		}
		return null;
	}

	@Override
	public List<String> tabComplete(Player sender, Set<String> flags, String source) {

		List<String> completions = new ArrayList<>();

		for (Rank rank : Core.getInstance().getRankHandler().getRanks()) {
			if (StringUtils.startsWithIgnoreCase(rank.getName(), source)) {
				completions.add(rank.getName());
			}
		}

		return null;
	}
}

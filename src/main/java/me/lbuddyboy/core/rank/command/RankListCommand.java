package me.lbuddyboy.core.rank.command;

import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.Settings;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.libraries.command.Command;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 5:59 PM
 * lCore / me.lbuddyboy.core.rank.command
 */
public class RankListCommand {

	@Command(names = {"rank list", "rank dump"}, permission = "lcore.command.rank.list", async = true)
	public static void listRanks(CommandSender sender) {

		for (String s : Settings.LIST_RANKS_HEADER.getList()) {
			sender.sendMessage(CC.translate(s));
		}

		List<Rank> ranks = new ArrayList<>(Core.getInstance().getRankHandler().getRanks());
		ranks.sort(Comparator.comparingInt(Rank::getWeight));

		for (Rank rank : ranks) {
			sender.sendMessage(CC.translate(Settings.LIST_RANKS_FORMAT.getMessage()
					.replaceAll("%weight%", "" + rank.getWeight())
					.replaceAll("%rank%", rank.getName())));
		}

	}

}

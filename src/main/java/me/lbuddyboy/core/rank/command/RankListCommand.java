package me.lbuddyboy.core.rank.command;

import me.blazingtide.zetsu.permissible.impl.permissible.Permissible;
import me.blazingtide.zetsu.schema.annotations.Command;
import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.rank.Rank;
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

	@Command(labels = "rank list", async = true, description = "Lists all ranks")
	@Permissible("lcore.command.rank.list")
	public void rankAddPerm(CommandSender sender) {

		for (String s : Configuration.LIST_RANKS_HEADER.getList()) {
			sender.sendMessage(CC.translate(s));
		}

		List<Rank> ranks = new ArrayList<>(Core.getInstance().getRankHandler().getRanks());
		ranks.sort(Comparator.comparingInt(Rank::getWeight));

		for (Rank rank : ranks) {
			sender.sendMessage(CC.translate(Configuration.LIST_RANKS_FORMAT.getMessage()
					.replaceAll("%weight%", "" + rank.getWeight())
					.replaceAll("%rank%", rank.getName())));
		}

	}

}

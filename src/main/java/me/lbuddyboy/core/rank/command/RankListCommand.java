package me.lbuddyboy.core.rank.command;

import me.lbuddyboy.core.Settings;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.libraries.command.Command;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.command.CommandSender;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 5:59 PM
 * lCore / me.lbuddyboy.core.rank.command
 */
public class RankListCommand {

	@Command(names = {"rank list", "rank dump"}, permission = "lcore.command.rank.list")
	public static void listRanks(CommandSender sender) {

		for (String s : Settings.LIST_RANKS_HEADER.getList()) {
			sender.sendMessage(CC.translate(s));
		}

		for (Rank rank : Rank.getRanks()) {
			sender.sendMessage(CC.translate(Settings.LIST_RANKS_FORMAT.getMessage().replaceAll("%rank%", rank.getName())));
		}

	}

}

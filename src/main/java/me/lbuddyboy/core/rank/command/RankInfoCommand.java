package me.lbuddyboy.core.rank.command;

import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.libraries.command.Command;
import me.lbuddyboy.libraries.command.Param;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.command.CommandSender;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 09/09/2021 / 12:50 PM
 * LBuddyBoy Development / me.lbuddyboy.core.rank.command
 */
public class RankInfoCommand {

	@Command(names = "rank info", permission = "lcore.command.rank.info")
	public static void rankInfo(CommandSender sender, @Param(name = "rank") Rank rank) {

		sender.sendMessage(CC.translate("&6&lRank Info"));
		sender.sendMessage(CC.translate("&6Display Name: " + rank.getDisplayName()));
		sender.sendMessage(CC.translate("&6Prefix: " + rank.getPrefix()));
		sender.sendMessage(CC.translate("&6Weight: " + rank.getWeight()));
		sender.sendMessage(CC.translate("&6Color: " + rank.getColor() + "Example"));
		sender.sendMessage(CC.translate("&6Permissions: "));
		for (String permission : rank.getPermissions()) {
			sender.sendMessage(CC.translate("&7- " + permission));
		}

	}

}

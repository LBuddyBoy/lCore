package me.lbuddyboy.core.rank.command;

import me.blazingtide.zetsu.permissible.impl.permissible.Permissible;
import me.blazingtide.zetsu.schema.annotations.Command;
import me.blazingtide.zetsu.schema.annotations.parameter.Param;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.command.CommandSender;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 09/09/2021 / 12:50 PM
 * LBuddyBoy Development / me.lbuddyboy.core.rank.command
 */
public class RankInfoCommand {

	@Command(labels = "rank info", async = true, description = "Shows all info of a rank")
	@Permissible("lcore.command.rank.info")
	public void rankAddPerm(CommandSender sender, @Param("rank") Rank rank) {

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

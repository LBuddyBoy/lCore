package me.lbuddyboy.core.rank.command;

import me.lbuddyboy.core.Settings;
import me.lbuddyboy.core.database.packets.rank.RankSetPrefixPacket;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.libraries.command.Command;
import me.lbuddyboy.libraries.command.Param;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.command.CommandSender;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 7:03 PM
 * lCore / me.lbuddyboy.core.rank.command
 */
public class RankSetPrefixCommand {

	@Command(names = "rank setprefix", permission = "lcore.command.rank.setprefix", async = true)
	public static void rankSetPrefix(CommandSender sender, @Param(name = "rank")Rank rank, @Param(name = "newPrefix", wildcard = true) String newPrefix) {

		if (rank == null) {
			sender.sendMessage(CC.translate(Settings.RANK_NONEXISTANT.getMessage()));
			return;
		}

		rank.setPrefix(newPrefix);
		rank.save();

		new RankSetPrefixPacket(rank, newPrefix).send();

		sender.sendMessage(CC.translate(Settings.SET_RANK_PREFIX.getMessage()
				.replaceAll("%new%", newPrefix)
				.replaceAll("%rank%", rank.getDisplayName())));

	}

}

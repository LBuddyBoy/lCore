package me.lbuddyboy.core.rank.command;

import me.lbuddyboy.core.Settings;
import me.lbuddyboy.core.database.packets.rank.RankRenamePacket;
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
public class RankRenameCommand {

	@Command(names = "rank rename", permission = "lcore.command.rank.setdisplay", async = true)
	public static void rankSetPrefix(CommandSender sender, @Param(name = "rank")Rank rank, @Param(name = "newName") String newName) {

		if (rank == null) {
			sender.sendMessage(CC.translate(Settings.RANK_NONEXISTANT.getMessage()));
			return;
		}

		rank.setName(newName);
		rank.save();

		new RankRenamePacket(rank, newName).send();

		sender.sendMessage(CC.translate(Settings.RANK_RENAME.getMessage()
				.replaceAll("%new%", newName)
				.replaceAll("%rank%", rank.getName())));

	}

}

package me.lbuddyboy.core.rank.command;

import me.lbuddyboy.core.Settings;
import me.lbuddyboy.core.database.packets.rank.RankSetDisplayPacket;
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
public class RankSetDisplayCommand {

	@Command(names = "rank setdisplay", permission = "lcore.command.rank.setdisplay", async = true)
	public static void rankSetPrefix(CommandSender sender, @Param(name = "rank")Rank rank, @Param(name = "newDisplay") String newDisplay) {

		if (rank == null) {
			sender.sendMessage(CC.translate(Settings.RANK_NONEXISTANT.getMessage()));
			return;
		}

		rank.setDisplayName(newDisplay);
		rank.save();

		new RankSetDisplayPacket(rank, newDisplay).send();

		sender.sendMessage(CC.translate(Settings.SET_RANK_DISPLAY.getMessage()
				.replaceAll("%new%", newDisplay)
				.replaceAll("%rank%", rank.getName())));

	}

}

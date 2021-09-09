package me.lbuddyboy.core.rank.command;

import me.lbuddyboy.core.Settings;
import me.lbuddyboy.core.database.packets.rank.RankSetColorPacket;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.libraries.command.Command;
import me.lbuddyboy.libraries.command.Param;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 7:03 PM
 * lCore / me.lbuddyboy.core.rank.command
 */
public class RankSetColorCommand {

	@Command(names = "rank setcolor", permission = "lcore.command.rank.setcolor", async = true)
	public static void rankSetColor(CommandSender sender, @Param(name = "rank")Rank rank, @Param(name = "newColor") String color) {

		if (rank == null) {
			sender.sendMessage(CC.translate(Settings.RANK_NONEXISTANT.getMessage()));
			return;
		}

		ChatColor newColor;

		try {
			newColor = ChatColor.valueOf(color.toUpperCase());
		} catch (Exception ignored) {
			sender.sendMessage(CC.translate("&cThat color is invalid."));
			return;
		}

		rank.setColor(newColor);
		rank.save();

		new RankSetColorPacket(rank, newColor).send();

		sender.sendMessage(CC.translate(Settings.SET_RANK_COLOR.getMessage()
				.replaceAll("%new%", color.toUpperCase())
				.replaceAll("%rank%", rank.getDisplayName())));

	}

}

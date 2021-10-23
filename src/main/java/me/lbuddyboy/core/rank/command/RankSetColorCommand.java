package me.lbuddyboy.core.rank.command;

import me.blazingtide.zetsu.permissible.impl.permissible.Permissible;
import me.blazingtide.zetsu.schema.annotations.Command;
import me.blazingtide.zetsu.schema.annotations.parameter.Param;
import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.core.database.packets.rank.RankSetColorPacket;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 7:03 PM
 * lCore / me.lbuddyboy.core.rank.command
 */
public class RankSetColorCommand {

	@Command(labels = "rank setcolor", async = true, description = "Sets the color of a rank")
	@Permissible("lcore.command.rank.setcolor")
	public void rankAddPerm(CommandSender sender, @Param("rank")Rank rank, @Param("newColor") String color) {

		if (rank == null) {
			sender.sendMessage(CC.translate(Configuration.RANK_NONEXISTANT.getMessage()));
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

		sender.sendMessage(CC.translate(Configuration.SET_RANK_COLOR.getMessage()
				.replaceAll("%new%", color.toUpperCase())
				.replaceAll("%rank%", rank.getDisplayName())));

	}

}

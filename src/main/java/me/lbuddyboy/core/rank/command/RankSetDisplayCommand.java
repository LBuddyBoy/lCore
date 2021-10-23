package me.lbuddyboy.core.rank.command;

import me.blazingtide.zetsu.permissible.impl.permissible.Permissible;
import me.blazingtide.zetsu.schema.annotations.Command;
import me.blazingtide.zetsu.schema.annotations.parameter.Param;
import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.core.database.packets.rank.RankSetDisplayPacket;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.command.CommandSender;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 7:03 PM
 * lCore / me.lbuddyboy.core.rank.command
 */
public class RankSetDisplayCommand {

	@Command(labels = "rank setdisplay", async = true, description = "Sets the display name of a rank")
	@Permissible("lcore.command.rank.setdisplay")
	public void rankAddPerm(CommandSender sender, @Param("rank")Rank rank, @Param("newDisplay") String newDisplay) {

		if (rank == null) {
			sender.sendMessage(CC.translate(Configuration.RANK_NONEXISTANT.getMessage()));
			return;
		}

		rank.setDisplayName(newDisplay);
		rank.save();

		new RankSetDisplayPacket(rank, newDisplay).send();

		sender.sendMessage(CC.translate(Configuration.SET_RANK_DISPLAY.getMessage()
				.replaceAll("%new%", newDisplay)
				.replaceAll("%rank%", rank.getName())));

	}

}

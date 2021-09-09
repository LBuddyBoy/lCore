package me.lbuddyboy.core.rank.command;

import me.lbuddyboy.core.Settings;
import me.lbuddyboy.core.database.packets.rank.RankSetWeightPacket;
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
public class RankSetWeightCommand {

	@Command(names = "rank setprefix", permission = "lcore.command.rank.setprefix", async = true)
	public static void rankSetPrefix(CommandSender sender, @Param(name = "rank")Rank rank, @Param(name = "newWeight") int newWeight) {

		if (rank == null) {
			sender.sendMessage(CC.translate(Settings.RANK_NONEXISTANT.getMessage()));
			return;
		}

		rank.setWeight(newWeight);
		rank.save();

		new RankSetWeightPacket(rank, newWeight).send();

		sender.sendMessage(CC.translate(Settings.SET_RANK_WEIGHT.getMessage()
				.replaceAll("%new%", "" + newWeight)
				.replaceAll("%rank%", rank.getDisplayName())));

	}

}

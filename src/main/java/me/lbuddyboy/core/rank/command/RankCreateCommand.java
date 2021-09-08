package me.lbuddyboy.core.rank.command;

import me.lbuddyboy.core.Settings;
import me.lbuddyboy.core.database.packets.RankCreatePacket;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.libraries.command.Command;
import me.lbuddyboy.libraries.command.Param;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.command.CommandSender;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 5:59 PM
 * lCore / me.lbuddyboy.core.rank.command
 */
public class RankCreateCommand {

	@Command(names = "rank create", permission = "lcore.command.rank.create")
	public static void creatRank(CommandSender sender, @Param(name = "name") String name) {

		if (Rank.getByName(name) != null) {
			sender.sendMessage(CC.translate(Settings.RANK_EXISTS.getMessage()));
			return;
		}

		Rank rank = new Rank(name);
		rank.save();

		Rank.getRanks().add(rank);

		new RankCreatePacket(name).send();

		sender.sendMessage(CC.translate(Settings.CREATED_RANK.getMessage().replaceAll("%rank%", name)));

	}

}

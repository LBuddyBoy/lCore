package me.lbuddyboy.core.rank.command;

import com.mongodb.client.model.Filters;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.Settings;
import me.lbuddyboy.core.database.packets.rank.RankDeletePacket;
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
public class RankDeleteCommand {

	@Command(names = "rank delete", permission = "lcore.command.rank.delete", async = true)
	public static void deleteRank(CommandSender sender, @Param(name = "name") String name) {

		if (Core.getInstance().getRankHandler().getByName(name) == null) {
			sender.sendMessage(CC.translate(Settings.RANK_NONEXISTANT.getMessage()));
			return;
		}

		Rank rank = Core.getInstance().getRankHandler().getByName(name);
		Core.getInstance().getRankHandler().getRanks().remove(rank);

		new RankDeletePacket(rank).send();

		sender.sendMessage(CC.translate(Settings.DELETED_RANK.getMessage().replaceAll("%rank%", name)));

		Core.getInstance().getRankHandler().getCollection().deleteOne(Filters.eq("name", name));

	}

}

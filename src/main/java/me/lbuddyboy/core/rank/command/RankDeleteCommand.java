package me.lbuddyboy.core.rank.command;

import com.mongodb.client.model.Filters;
import me.lbuddyboy.core.Settings;
import me.lbuddyboy.core.database.packets.RankDeletePacket;
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

	@Command(names = "rank delete", permission = "lcore.command.rank.delete")
	public static void deleteRank(CommandSender sender, @Param(name = "name") String name) {

		if (Rank.getByName(name) == null) {
			sender.sendMessage(CC.translate(Settings.RANK_NONEXISTANT.getMessage()));
			return;
		}

		Rank rank = Rank.getByName(name);
		Rank.getRanks().remove(rank);

		new RankDeletePacket(rank).send();

		sender.sendMessage(CC.translate(Settings.DELETED_RANK.getMessage().replaceAll("%rank%", name)));

		Rank.collection.deleteOne(Filters.eq("name", name));

	}

}

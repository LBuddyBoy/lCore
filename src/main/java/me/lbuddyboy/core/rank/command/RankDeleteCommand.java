package me.lbuddyboy.core.rank.command;

import me.blazingtide.zetsu.permissible.impl.permissible.Permissible;
import me.blazingtide.zetsu.schema.annotations.Command;
import me.blazingtide.zetsu.schema.annotations.parameter.Param;
import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.database.redis.packets.rank.RankDeletePacket;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.command.CommandSender;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 5:59 PM
 * lCore / me.lbuddyboy.core.rank.command
 */
public class RankDeleteCommand {

	@Command(labels = "rank delete", async = true, description = "Deletes a current rank")
	@Permissible("lcore.command.rank.delete")
	public void rankAddPerm(CommandSender sender, @Param("name") String name) {

		if (Core.getInstance().getRankHandler().getByName(name) == null) {
			sender.sendMessage(CC.translate(Configuration.RANK_NONEXISTANT.getMessage()));
			return;
		}

		Rank rank = Core.getInstance().getRankHandler().getByName(name);
		Core.getInstance().getRankHandler().getRanks().remove(rank);

		new RankDeletePacket(rank).send();

		sender.sendMessage(CC.translate(Configuration.DELETED_RANK.getMessage().replaceAll("%rank%", name)));

	}

}

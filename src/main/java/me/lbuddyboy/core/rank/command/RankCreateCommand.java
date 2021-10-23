package me.lbuddyboy.core.rank.command;

import me.blazingtide.zetsu.permissible.impl.permissible.Permissible;
import me.blazingtide.zetsu.schema.annotations.Command;
import me.blazingtide.zetsu.schema.annotations.parameter.Param;
import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.database.packets.rank.RankCreatePacket;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.command.CommandSender;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 5:59 PM
 * lCore / me.lbuddyboy.core.rank.command
 */
public class RankCreateCommand {

	@Command(labels = "rank create", async = true, description = "Creates a brand new rank")
	@Permissible("lcore.command.rank.create")
	public void rankAddPerm(CommandSender sender, @Param("name") String name) {

		if (Core.getInstance().getRankHandler().getByName(name) != null) {
			sender.sendMessage(CC.translate(Configuration.RANK_EXISTS.getMessage()));
			return;
		}

		Rank rank = new Rank(name, false);
		rank.save();

		Core.getInstance().getRankHandler().getRanks().add(rank);

		new RankCreatePacket(name).send();

		sender.sendMessage(CC.translate(Configuration.CREATED_RANK.getMessage().replaceAll("%rank%", name)));

	}

}

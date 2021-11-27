package me.lbuddyboy.core.rank.command;

import me.blazingtide.zetsu.permissible.impl.permissible.Permissible;
import me.blazingtide.zetsu.schema.annotations.Command;
import me.blazingtide.zetsu.schema.annotations.parameter.Param;
import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.core.database.redis.packets.rank.RankRenamePacket;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.command.CommandSender;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 7:03 PM
 * lCore / me.lbuddyboy.core.rank.command
 */
public class RankRenameCommand {

	@Command(labels = "rank rename", async = true, description = "Renames a current rank")
	@Permissible("lcore.command.rank.rename")
	public void rankAddPerm(CommandSender sender, @Param("rank")Rank rank, @Param("newName") String newName) {

		if (rank == null) {
			sender.sendMessage(CC.translate(Configuration.RANK_NONEXISTANT.getMessage()));
			return;
		}

		rank.setName(newName);
		rank.save();

		new RankRenamePacket(rank.getName(), newName).send();

		sender.sendMessage(CC.translate(Configuration.RANK_RENAME.getMessage()
				.replaceAll("%new%", newName)
				.replaceAll("%rank%", rank.getName())));

	}

}

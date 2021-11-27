package me.lbuddyboy.core.rank.command;

import me.blazingtide.zetsu.permissible.impl.permissible.Permissible;
import me.blazingtide.zetsu.schema.annotations.Command;
import me.blazingtide.zetsu.schema.annotations.parameter.Param;
import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.core.database.redis.packets.rank.RankSetPrefixPacket;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.command.CommandSender;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 7:03 PM
 * lCore / me.lbuddyboy.core.rank.command
 */
public class RankSetPrefixCommand {

	@Command(labels = "rank setprefix", async = true, description = "Sets the prefix of a rank")
	@Permissible("lcore.command.rank.setprefix")
	public void rankAddPerm(CommandSender sender, @Param("rank")Rank rank, @Param("newPrefix") String newPrefix) {

		if (rank == null) {
			sender.sendMessage(CC.translate(Configuration.RANK_NONEXISTANT.getMessage()));
			return;
		}

		rank.setPrefix(newPrefix);
		rank.save();

		new RankSetPrefixPacket(rank.getName(), newPrefix).send();

		sender.sendMessage(CC.translate(Configuration.SET_RANK_PREFIX.getMessage()
				.replaceAll("%new%", newPrefix)
				.replaceAll("%rank%", rank.getDisplayName())));

	}

}

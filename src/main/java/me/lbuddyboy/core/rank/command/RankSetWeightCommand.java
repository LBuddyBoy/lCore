package me.lbuddyboy.core.rank.command;

import me.blazingtide.zetsu.permissible.impl.permissible.Permissible;
import me.blazingtide.zetsu.schema.annotations.Command;
import me.blazingtide.zetsu.schema.annotations.parameter.Param;
import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.core.database.redis.packets.rank.RankSetWeightPacket;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.command.CommandSender;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 7:03 PM
 * lCore / me.lbuddyboy.core.rank.command
 */
public class RankSetWeightCommand {

	@Command(labels = "rank setweight", async = true, description = "Sets the weight of a rank")
	@Permissible("lcore.command.rank.setweight")
	public void rankAddPerm(CommandSender sender, @Param("rank")Rank rank, @Param("newWeight") Integer newWeight) {

		if (rank == null) {
			sender.sendMessage(CC.translate(Configuration.RANK_NONEXISTANT.getMessage()));
			return;
		}

		rank.setWeight(newWeight);
		rank.save();

		new RankSetWeightPacket(rank.getName(), newWeight).send();

		sender.sendMessage(CC.translate(Configuration.SET_RANK_WEIGHT.getMessage()
				.replaceAll("%new%", "" + newWeight)
				.replaceAll("%rank%", rank.getDisplayName())));

	}

}

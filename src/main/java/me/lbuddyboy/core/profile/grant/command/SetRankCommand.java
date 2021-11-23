package me.lbuddyboy.core.profile.grant.command;

import me.blazingtide.zetsu.permissible.impl.permissible.Permissible;
import me.blazingtide.zetsu.schema.annotations.Command;
import me.blazingtide.zetsu.schema.annotations.parameter.Param;
import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.database.redis.packets.grant.GrantAddPacket;
import me.lbuddyboy.core.profile.grant.Grant;
import me.lbuddyboy.core.profile.lProfile;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.libraries.util.CC;
import me.lbuddyboy.libraries.util.JavaUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 7:26 PM
 * lCore / me.lbuddyboy.core.profile.grant.command
 */
public class SetRankCommand {

	@Command(labels = "setrank", async = true)
	@Permissible("lcore.command.setrank")
	public void setRank(CommandSender sender, @Param("target")UUID uuid, @Param("rank")Rank rank, @Param("time") String time, @Param("reason") String reason) {

		lProfile profile = Core.getInstance().getProfileHandler().getByUUID(uuid);

		if (profile == null) {
			sender.sendMessage(CC.translate(Configuration.INVALID_PROFILE.getMessage()));
			return;
		}

		if (rank == null) {
			sender.sendMessage(CC.translate(Configuration.RANK_NONEXISTANT.getMessage()));
			return;
		}

		UUID senderUUID = (sender instanceof Player ? ((Player) sender).getUniqueId() : null);

		long duration = (time.equalsIgnoreCase("perm") ? Long.MAX_VALUE : JavaUtils.parse(time));

		Grant grant = new Grant(UUID.randomUUID(), rank, senderUUID, uuid, reason, System.currentTimeMillis(), duration);

		sender.sendMessage(CC.translate(Configuration.GRANTED_SENDER.getMessage()
				.replaceAll("%time%", grant.getTimeRemaining())
				.replaceAll("%rank%", rank.getDisplayName())
				.replaceAll("%player%", profile.getName())
		));

		new GrantAddPacket(grant).send();

		Player player = Bukkit.getPlayer(grant.getTarget());
		if (player != null) {
			player.sendMessage(CC.translate(Configuration.GRANTED_TARGET.getMessage()
					.replaceAll("%time%", grant.getTimeRemaining())
					.replaceAll("%rank%", grant.getRank().getDisplayName())
					.replaceAll("%player%", profile.getName())
			));
		}

		profile.getGrants().add(grant);
		profile.grantNext();
		profile.save();

	}

}

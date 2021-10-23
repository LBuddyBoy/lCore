package me.lbuddyboy.core.rank.command;

import me.blazingtide.zetsu.permissible.impl.permissible.Permissible;
import me.blazingtide.zetsu.schema.annotations.Command;
import me.blazingtide.zetsu.schema.annotations.parameter.Param;
import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.database.packets.rank.RankRemovedPermissionPacket;
import me.lbuddyboy.core.profile.lProfile;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.command.CommandSender;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 08/09/2021 / 6:30 PM
 * LBuddyBoy Development / me.lbuddyboy.core.rank.command
 */

public class RankRemovePermissionCommand {

	@Command(labels = "rank removeperm", async = true, description = "Removes a permission from a rank")
	@Permissible("lcore.command.rank.removeperm")
	public void rankAddPerm(CommandSender sender, @Param("rank") Rank rank, @Param("permission") String permission) {

		if (rank == null) {
			sender.sendMessage(CC.translate(Configuration.RANK_NONEXISTANT.getMessage()));
			return;
		}

		if (!rank.getPermissions().contains(permission)) {
			sender.sendMessage(CC.translate(Configuration.RANK_DOES_NOT_HAVE_PERM.getMessage()));
			return;
		}

		rank.getPermissions().remove(permission);
		rank.save();

		new RankRemovedPermissionPacket(rank, permission).send();

		sender.sendMessage(CC.translate(Configuration.RANK_REMOVED_PERM.getMessage()
				.replaceAll("%perm%", permission)
				.replaceAll("%rank%", rank.getDisplayName())));

		for (lProfile profile : Core.getInstance().getProfileHandler().getProfiles().values()) {
			if (profile.getCurrentRank().getName().equals(rank.getName())) {
				profile.setupPermissions();
			}
		}

	}

}

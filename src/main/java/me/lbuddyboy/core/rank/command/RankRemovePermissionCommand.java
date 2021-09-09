package me.lbuddyboy.core.rank.command;

import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.Settings;
import me.lbuddyboy.core.database.packets.rank.RankRemovedPermissionPacket;
import me.lbuddyboy.core.profile.Profile;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.libraries.command.Command;
import me.lbuddyboy.libraries.command.Param;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.command.CommandSender;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 08/09/2021 / 6:30 PM
 * LBuddyBoy Development / me.lbuddyboy.core.rank.command
 */

public class RankRemovePermissionCommand {

	@Command(names = "rank removeperm", permission = "lcore.command.rank.removeperm", async = true)
	public static void rankRemovePerm(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "permission") String permission) {

		if (rank == null) {
			sender.sendMessage(CC.translate(Settings.RANK_NONEXISTANT.getMessage()));
			return;
		}

		if (!rank.getPermissions().contains(permission)) {
			sender.sendMessage(CC.translate(Settings.RANK_DOES_NOT_HAVE_PERM.getMessage()));
			return;
		}

		rank.getPermissions().remove(permission);
		rank.save();

		new RankRemovedPermissionPacket(rank, permission).send();

		sender.sendMessage(CC.translate(Settings.RANK_REMOVED_PERM.getMessage()
				.replaceAll("%perm%", permission)
				.replaceAll("%rank%", rank.getDisplayName())));

		for (Profile profile : Core.getInstance().getProfileHandler().getProfiles()) {
			if (profile.getCurrentRank().getName().equals(rank.getName())) {
				profile.setupPermissions();
			}
		}

	}

}

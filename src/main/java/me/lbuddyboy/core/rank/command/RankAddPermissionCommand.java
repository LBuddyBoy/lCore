package me.lbuddyboy.core.rank.command;

import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.Settings;
import me.lbuddyboy.core.database.packets.rank.RankAddPermissionPacket;
import me.lbuddyboy.core.profile.lProfile;
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

public class RankAddPermissionCommand {

	@Command(names = "rank addperm", permission = "lcore.command.rank.addperm", async = true)
	public static void rankAddPerm(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "permission") String permission) {

		if (rank == null) {
			sender.sendMessage(CC.translate(Settings.RANK_NONEXISTANT.getMessage()));
			return;
		}

		if (rank.getPermissions().contains(permission)) {
			sender.sendMessage(CC.translate(Settings.RANK_ALREADY_HAS_PERM.getMessage()));
			return;
		}

		rank.getPermissions().add(permission);
		rank.save();

		new RankAddPermissionPacket(rank, permission).send();

		sender.sendMessage(CC.translate(Settings.RANK_ADDED_PERM.getMessage()
				.replaceAll("%perm%", permission)
				.replaceAll("%rank%", rank.getDisplayName())));

		for (lProfile profile : Core.getInstance().getProfileHandler().getProfiles().values()) {
			if (profile.getCurrentRank().getName().equals(rank.getName())) {
				profile.setupPermissions();
			}
		}

	}

}

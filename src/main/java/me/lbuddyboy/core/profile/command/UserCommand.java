package me.lbuddyboy.core.profile.command;

import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.Settings;
import me.lbuddyboy.core.profile.lProfile;
import me.lbuddyboy.libraries.command.Command;
import me.lbuddyboy.libraries.command.Param;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.command.CommandSender;

import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 9:19 PM
 * lCore / me.lbuddyboy.core.profile.command
 */
public class UserCommand {

	@Command(names = "user info", permission = "lcore.command.user.info")
	public static void userInfo(CommandSender sender, @Param(name = "player")UUID uuid) {

		lProfile profile = Core.getInstance().getProfileHandler().getByUUID(uuid);
		if (profile == null) {
			sender.sendMessage(CC.translate(Settings.INVALID_PROFILE.getMessage()));
			return;
		}

		sender.sendMessage(CC.translate(""));
		sender.sendMessage(CC.translate("&6&lUser Info&7:&f " + profile.getName()));
		sender.sendMessage(CC.translate(""));
		sender.sendMessage(CC.translate("&6IP&7: &f" + profile.getCurrentIP()));
		sender.sendMessage(CC.translate("&6Rank&7: &f" + profile.getCurrentRank().getDisplayName()));
		sender.sendMessage(CC.translate("&6Rank Duration&7: &f" + profile.getCurrentGrant().getTimeRemaining()));

	}

}

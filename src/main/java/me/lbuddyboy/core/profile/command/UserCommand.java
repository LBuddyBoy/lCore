package me.lbuddyboy.core.profile.command;

import me.blazingtide.zetsu.permissible.impl.permissible.Permissible;
import me.blazingtide.zetsu.schema.annotations.Command;
import me.blazingtide.zetsu.schema.annotations.parameter.Param;
import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.profile.lProfile;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.command.CommandSender;

import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 9:19 PM
 * lCore / me.lbuddyboy.core.profile.command
 */
public class UserCommand {

	@Command(labels = "user info")
	@Permissible("lcore.command.user.info")
	public void userInfo(CommandSender sender, @Param("player")UUID uuid) {

		lProfile profile = Core.getInstance().getProfileHandler().getByUUID(uuid);
		if (profile == null) {
			sender.sendMessage(CC.translate(Configuration.INVALID_PROFILE.getMessage()));
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

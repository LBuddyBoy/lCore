package me.lbuddyboy.core.profile.command;

import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.Settings;
import me.lbuddyboy.core.database.packets.ProfileStatusPacket;
import me.lbuddyboy.core.profile.Profile;
import me.lbuddyboy.libraries.command.Command;
import me.lbuddyboy.libraries.command.Param;
import me.lbuddyboy.libraries.util.CC;
import me.lbuddyboy.libraries.uuid.UniqueIDCache;
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

		Profile profile = Core.getInstance().getProfileHandler().getByUUID(uuid);
		if (profile == null) {
			sender.sendMessage(CC.translate(Settings.INVALID_PROFILE.getMessage()));
			return;
		}

		sender.sendMessage(CC.translate(""));
		sender.sendMessage(CC.translate("&6&lUser Info&7:&f " + profile.getName()));
		sender.sendMessage(CC.translate(""));
		sender.sendMessage(CC.translate("&6Rank&7: &f" + profile.getCurrentRank().getDisplayName()));
		sender.sendMessage(CC.translate("&6Rank Duration&7: &f" + profile.getCurrentGrant().getTimeRemaining()));

		ProfileStatusPacket statusPacket = new ProfileStatusPacket(UniqueIDCache.name(uuid));
		statusPacket.send();
		String onlineString = (statusPacket.isOnline() ? "&aOnline" : "Offline");
		if (statusPacket.isOnline()) {
			sender.sendMessage(CC.translate("&6Profile Status&7: &f" + onlineString));
			sender.sendMessage(CC.translate("&6Server&7: &f" + statusPacket.getServer()));
		} else {
			sender.sendMessage(CC.translate("&6Profile Status&7: &f" + onlineString));
		}

	}

}

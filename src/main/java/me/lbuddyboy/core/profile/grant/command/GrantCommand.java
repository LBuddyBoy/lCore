package me.lbuddyboy.core.profile.grant.command;

import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.Settings;
import me.lbuddyboy.core.profile.grant.menu.GrantMenu;
import me.lbuddyboy.core.profile.lProfile;
import me.lbuddyboy.libraries.command.Command;
import me.lbuddyboy.libraries.command.Param;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 08/09/2021 / 1:28 AM
 * lCore / me.lbuddyboy.core.profile.grant.command
 */
public class GrantCommand {

	@Command(names = "grant", permission = "lcore.command.grant", async = true)
	public static void grant(Player sender, @Param(name = "target")UUID uuid) {

		lProfile profile = Core.getInstance().getProfileHandler().getByUUID(uuid);

		if (profile == null) {
			sender.sendMessage(CC.translate(Settings.INVALID_PROFILE.getMessage()));
			return;
		}

		new GrantMenu(profile).openMenu(sender);

	}

}

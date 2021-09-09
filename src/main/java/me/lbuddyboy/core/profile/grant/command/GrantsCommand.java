package me.lbuddyboy.core.profile.grant.command;

import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.Settings;
import me.lbuddyboy.core.profile.Profile;
import me.lbuddyboy.core.profile.grant.menu.GrantsMenu;
import me.lbuddyboy.libraries.command.Command;
import me.lbuddyboy.libraries.command.Param;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 8:06 PM
 * lCore / me.lbuddyboy.core.profile.grant.command
 */
public class GrantsCommand {

	@Command(names = "grants", permission = "lcore.command.grants")
	public static void grants(Player sender, @Param(name = "name")UUID uuid) {

		Profile profile = Core.getInstance().getProfileHandler().getByUUID(uuid);
		if (profile == null) {
			sender.sendMessage(CC.translate(Settings.INVALID_PROFILE.getMessage()));
			return;
		}

		new GrantsMenu(profile).openMenu(sender);

	}

}

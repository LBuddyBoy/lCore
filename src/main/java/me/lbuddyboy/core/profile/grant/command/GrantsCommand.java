package me.lbuddyboy.core.profile.grant.command;

import me.blazingtide.zetsu.permissible.impl.permissible.Permissible;
import me.blazingtide.zetsu.schema.annotations.Command;
import me.blazingtide.zetsu.schema.annotations.parameter.Param;
import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.profile.grant.menu.GrantsMenu;
import me.lbuddyboy.core.profile.lProfile;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 8:06 PM
 * lCore / me.lbuddyboy.core.profile.grant.command
 */
public class GrantsCommand {

	@Command(labels = "grants", async = true)
	@Permissible("lcore.command.grants")
	public void rankAddPerm(Player sender, @Param("name")UUID uuid) {

		lProfile profile = Core.getInstance().getProfileHandler().getByUUID(uuid);
		if (profile == null) {
			sender.sendMessage(CC.translate(Configuration.INVALID_PROFILE.getMessage()));
			return;
		}

		new GrantsMenu(profile).openMenu(sender);

	}

}

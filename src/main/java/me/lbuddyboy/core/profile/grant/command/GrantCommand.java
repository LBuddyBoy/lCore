package me.lbuddyboy.core.profile.grant.command;

import me.blazingtide.zetsu.permissible.impl.permissible.Permissible;
import me.blazingtide.zetsu.schema.annotations.Command;
import me.blazingtide.zetsu.schema.annotations.parameter.Param;
import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.profile.grant.menu.GrantMenu;
import me.lbuddyboy.core.profile.lProfile;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 08/09/2021 / 1:28 AM
 * lCore / me.lbuddyboy.core.profile.grant.command
 */
public class GrantCommand {

	@Command(labels = "grant", async = true)
	@Permissible("lcore.command.grant")
	public void rankAddPerm(Player sender, @Param("target")UUID uuid) {

		lProfile profile = Core.getInstance().getProfileHandler().getByUUID(uuid);

		if (profile == null) {
			sender.sendMessage(CC.translate(Configuration.INVALID_PROFILE.getMessage()));
			return;
		}

		new GrantMenu(profile).openMenu(sender);

	}

}

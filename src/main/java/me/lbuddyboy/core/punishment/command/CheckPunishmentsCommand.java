package me.lbuddyboy.core.punishment.command;

import me.blazingtide.zetsu.permissible.impl.permissible.Permissible;
import me.blazingtide.zetsu.schema.annotations.Command;
import me.blazingtide.zetsu.schema.annotations.parameter.Param;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.profile.lProfile;
import me.lbuddyboy.core.punishment.menu.PunishmentsMainMenu;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 24/10/2021 / 12:37 PM
 * LBuddyBoy Development / me.lbuddyboy.core.punishment.command
 */
public class CheckPunishmentsCommand {

	@Command(labels = {"c", "check", "punishments"}, description = "Opens a gui with all know punishments of a player", async = true)
	@Permissible("lcore.command.check")
	public void check(Player player, @Param("target")UUID target) {
		lProfile profile = Core.getInstance().getProfileHandler().getByUUID(target);
		new PunishmentsMainMenu(profile).openMenu(player);
	}

}

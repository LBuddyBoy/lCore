package me.lbuddyboy.core.commands;

import me.blazingtide.zetsu.permissible.impl.permissible.Permissible;
import me.blazingtide.zetsu.schema.annotations.Command;
import me.blazingtide.zetsu.schema.annotations.parameter.Param;
import org.bukkit.entity.Player;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 23/11/2021 / 11:58 AM
 * LBuddyBoy Development / me.lbuddyboy.core.commands
 */
public class TeleportCommand {

	@Command(labels = {"tp", "teleport"})
	@Permissible("lcore.command.teleport")
	public void teleport(Player sender, @Param("target") Player target) {
		sender.teleport(target.getLocation());
	}

}

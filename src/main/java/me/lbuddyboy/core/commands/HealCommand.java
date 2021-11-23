package me.lbuddyboy.core.commands;

import me.blazingtide.zetsu.permissible.impl.permissible.Permissible;
import me.blazingtide.zetsu.schema.annotations.Command;
import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.entity.Player;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/11/2021 / 3:34 PM
 * LBuddyBoy Development / me.lbuddyboy.core.commands
 */
public class HealCommand {

	@Command(labels = {"heal"})
	@Permissible("lcore.command.heal")
	public void feed(Player sender) {
		sender.setHealth(20);
		sender.setFoodLevel(20);
		sender.setSaturation(20);
		sender.sendMessage(CC.translate(Configuration.ESSENTIALS_HEAL.getMessage()));
	}

}

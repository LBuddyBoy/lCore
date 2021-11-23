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
public class FeedCommand {

	@Command(labels = {"feed", "eat"})
	@Permissible("lcore.command.feed")
	public void feed(Player sender) {
		sender.setFoodLevel(20);
		sender.setSaturation(20);
		sender.sendMessage(CC.translate(Configuration.ESSENTIALS_FEED.getMessage()));
	}

}

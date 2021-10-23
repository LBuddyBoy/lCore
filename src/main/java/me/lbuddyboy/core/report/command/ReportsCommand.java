package me.lbuddyboy.core.report.command;

import me.blazingtide.zetsu.permissible.impl.permissible.Permissible;
import me.blazingtide.zetsu.schema.annotations.Command;
import me.lbuddyboy.core.report.menu.ReportsMenu;
import org.bukkit.entity.Player;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 23/10/2021 / 1:30 PM
 * LBuddyBoy Development / me.lbuddyboy.core.report.command
 */
public class ReportsCommand {

	@Command(labels = "reports", async = true)
	@Permissible("lcore.command.reports")
	public void reports(Player sender) {
		new ReportsMenu().openMenu(sender);
	}

}

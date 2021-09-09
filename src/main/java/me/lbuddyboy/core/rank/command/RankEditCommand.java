package me.lbuddyboy.core.rank.command;

import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.core.rank.menu.RankEditMenu;
import me.lbuddyboy.libraries.command.Command;
import me.lbuddyboy.libraries.command.Param;
import org.bukkit.entity.Player;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 08/09/2021 / 12:26 AM
 * lCore / me.lbuddyboy.core.rank.command
 */
public class RankEditCommand {

	@Command(names = "rank edit", permission = "lcore.command.rank.edit")
	public static void editRank(Player sender, @Param(name = "rank") Rank rank) {
		new RankEditMenu(rank).openMenu(sender);
	}

}

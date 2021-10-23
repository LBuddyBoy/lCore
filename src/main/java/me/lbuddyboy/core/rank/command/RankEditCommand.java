package me.lbuddyboy.core.rank.command;

import me.blazingtide.zetsu.permissible.impl.permissible.Permissible;
import me.blazingtide.zetsu.schema.annotations.Command;
import me.blazingtide.zetsu.schema.annotations.parameter.Param;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.core.rank.menu.RankEditMenu;
import org.bukkit.entity.Player;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 08/09/2021 / 12:26 AM
 * lCore / me.lbuddyboy.core.rank.command
 */
public class RankEditCommand {

	@Command(labels = "rank edit", async = true, description = "Pulls up a rank editor gui")
	@Permissible("lcore.command.rank.edit")
	public void rankAddPerm(Player sender, @Param("rank") Rank rank) {
		new RankEditMenu(rank).openMenu(sender);
	}

}

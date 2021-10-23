package me.lbuddyboy.core.rank.listener;

import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.core.database.packets.rank.*;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.core.rank.menu.RankEditMenu;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 08/09/2021 / 12:51 AM
 * lCore / me.lbuddyboy.core.rank.listener
 */
public class RankEditListener implements Listener {

	public static List<Player> rename = new ArrayList<>();
	public static List<Player> chatcolor = new ArrayList<>();
	public static List<Player> display = new ArrayList<>();
	public static List<Player> prefix = new ArrayList<>();
	public static List<Player> weight = new ArrayList<>();
	public static Map<Player, Rank> renameMap = new HashMap<>();


	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {

		Player p = event.getPlayer();

		if (rename.contains(p)) {
			if (event.getMessage().equalsIgnoreCase("cancel")) {
				p.sendMessage(CC.translate("&cProcess cancelled."));
				event.setCancelled(true);
				rename.remove(p);
				return;
			}

			Rank rank = renameMap.get(p);
			rank.setName(event.getMessage());
			rank.save();

			new RankRenamePacket(rank, event.getMessage()).send();

			p.sendMessage(CC.translate(Configuration.RANK_RENAME.getMessage()
					.replaceAll("%new%", event.getMessage())
					.replaceAll("%rank%", rank.getName())));

			rename.remove(p);

			event.setCancelled(true);
			new RankEditMenu(rank).openMenu(p);
		} else if (chatcolor.contains(p)) {
			if (event.getMessage().equalsIgnoreCase("cancel")) {
				p.sendMessage(CC.translate("&cProcess cancelled."));
				event.setCancelled(true);
				chatcolor.remove(p);
				return;
			}

			ChatColor color;
			try {
				 color = ChatColor.valueOf(event.getMessage().toUpperCase());
			} catch (Exception ignored) {
				p.sendMessage(CC.translate("&cThat color does not exist. Try again..."));
				return;
			}

			Rank rank = renameMap.get(p);
			rank.setColor(color);
			rank.save();

			new RankSetColorPacket(rank, color).send();

			p.sendMessage(CC.translate(Configuration.SET_RANK_COLOR.getMessage()
					.replaceAll("%new%", event.getMessage())
					.replaceAll("%rank%", rank.getName())));

			chatcolor.remove(p);

			event.setCancelled(true);
			new RankEditMenu(rank).openMenu(p);
		} else if (weight.contains(p)) {
			if (event.getMessage().equalsIgnoreCase("cancel")) {
				p.sendMessage(CC.translate("&cProcess cancelled."));
				event.setCancelled(true);
				weight.remove(p);
				return;
			}

			weight.remove(p);

			int weight = Integer.parseInt(event.getMessage());

			Rank rank = renameMap.get(p);
			rank.setWeight(weight);
			rank.save();

			new RankSetWeightPacket(rank, weight).send();

			p.sendMessage(CC.translate(Configuration.SET_RANK_WEIGHT.getMessage()
					.replaceAll("%new%", event.getMessage())
					.replaceAll("%rank%", rank.getName())));



			event.setCancelled(true);
			new RankEditMenu(rank).openMenu(p);
		} else if (prefix.contains(p)) {
			if (event.getMessage().equalsIgnoreCase("cancel")) {
				p.sendMessage(CC.translate("&cProcess cancelled."));
				event.setCancelled(true);
				prefix.remove(p);
				return;
			}

			prefix.remove(p);

			Rank rank = renameMap.get(p);
			rank.setPrefix(event.getMessage());
			rank.save();

			new RankSetPrefixPacket(rank, event.getMessage()).send();

			p.sendMessage(CC.translate(Configuration.SET_RANK_PREFIX.getMessage()
					.replaceAll("%new%", event.getMessage())
					.replaceAll("%rank%", rank.getName())));

			event.setCancelled(true);
			new RankEditMenu(rank).openMenu(p);
		} else if (display.contains(p)) {
			if (event.getMessage().equalsIgnoreCase("cancel")) {
				p.sendMessage(CC.translate("&cProcess cancelled."));
				event.setCancelled(true);
				display.remove(p);
				return;
			}

			display.remove(p);

			Rank rank = renameMap.get(p);
			rank.setDisplayName(event.getMessage());
			rank.save();

			new RankSetDisplayPacket(rank, event.getMessage()).send();

			p.sendMessage(CC.translate(Configuration.SET_RANK_DISPLAY.getMessage()
					.replaceAll("%new%", event.getMessage())
					.replaceAll("%rank%", rank.getName())));

			event.setCancelled(true);
			new RankEditMenu(rank).openMenu(p);

		}
	}

}

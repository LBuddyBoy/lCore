package me.lbuddyboy.core.rank.listener;

import me.lbuddyboy.core.Settings;
import me.lbuddyboy.core.database.packets.rank.*;
import me.lbuddyboy.core.rank.Rank;
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
	public static List<Player> color = new ArrayList<>();
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
				rename.remove(p);
				return;
			}

			Rank rank = renameMap.get(p);
			rank.setName(event.getMessage());
			rank.save();

			new RankRenamePacket(rank, event.getMessage()).send();

			p.sendMessage(CC.translate(Settings.RANK_RENAME.getMessage()
					.replaceAll("%new%", event.getMessage())
					.replaceAll("%rank%", rank.getName())));

		} else if (color.contains(p)) {
			if (event.getMessage().equalsIgnoreCase("cancel")) {
				p.sendMessage(CC.translate("&cProcess cancelled."));
				color.remove(p);
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

			p.sendMessage(CC.translate(Settings.SET_RANK_COLOR.getMessage()
					.replaceAll("%new%", event.getMessage())
					.replaceAll("%rank%", rank.getName())));
		} else if (weight.contains(p)) {
			if (event.getMessage().equalsIgnoreCase("cancel")) {
				p.sendMessage(CC.translate("&cProcess cancelled."));
				weight.remove(p);
				return;
			}

			int weight;
			try {
				weight = Integer.parseInt(event.getMessage());
			} catch (Exception ignored) {
				p.sendMessage(CC.translate("&cThat color does not exist. Try again..."));
				return;
			}

			Rank rank = renameMap.get(p);
			rank.setWeight(weight);
			rank.save();

			new RankSetWeightPacket(rank, weight).send();

			p.sendMessage(CC.translate(Settings.SET_RANK_WEIGHT.getMessage()
					.replaceAll("%new%", event.getMessage())
					.replaceAll("%rank%", rank.getName())));
		} else if (prefix.contains(p)) {
			if (event.getMessage().equalsIgnoreCase("cancel")) {
				p.sendMessage(CC.translate("&cProcess cancelled."));
				prefix.remove(p);
				return;
			}

			Rank rank = renameMap.get(p);
			rank.setPrefix(event.getMessage());
			rank.save();

			new RankSetPrefixPacket(rank, event.getMessage()).send();

			p.sendMessage(CC.translate(Settings.SET_RANK_PREFIX.getMessage()
					.replaceAll("%new%", event.getMessage())
					.replaceAll("%rank%", rank.getName())));
		} else if (display.contains(p)) {
			if (event.getMessage().equalsIgnoreCase("cancel")) {
				p.sendMessage(CC.translate("&cProcess cancelled."));
				display.remove(p);
				return;
			}

			Rank rank = renameMap.get(p);
			rank.setDisplayName(event.getMessage());
			rank.save();

			new RankSetDisplayPacket(rank, event.getMessage()).send();

			p.sendMessage(CC.translate(Settings.SET_RANK_DISPLAY.getMessage()
					.replaceAll("%new%", event.getMessage())
					.replaceAll("%rank%", rank.getName())));

		}
	}
	
}

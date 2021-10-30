package me.lbuddyboy.core.punishment.listener;

import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.profile.lProfile;
import me.lbuddyboy.core.punishment.Punishment;
import me.lbuddyboy.core.punishment.menu.PunishmentsMenu;
import me.lbuddyboy.libraries.util.CC;
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
 * 24/10/2021 / 12:03 PM
 * LBuddyBoy Development / me.lbuddyboy.core.punishment.listener
 */
public class PunishmentListener implements Listener {

	public static List<Player> reasons = new ArrayList<>();
	public static Map<Player, Punishment> pMap = new HashMap<>();

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		if (reasons.contains(event.getPlayer())) {
			if (event.getMessage().equalsIgnoreCase("cancel")) {
				event.setCancelled(true);
				reasons.remove(event.getPlayer());
				pMap.remove(event.getPlayer());
				return;
			}
			Punishment punishment = pMap.get(event.getPlayer());
			lProfile target = Core.getInstance().getProfileHandler().getByUUID(punishment.getTarget());
			target.getPunishments().remove(punishment);

			punishment.setResolvedAt(System.currentTimeMillis());
			punishment.setResolvedBy(event.getPlayer().getUniqueId());
			punishment.setResolvedReason(event.getMessage());
			punishment.setResolved(true);

			target.getPunishments().add(punishment);
			target.save();

			pMap.remove(event.getPlayer());
			reasons.remove(event.getPlayer());

			new PunishmentsMenu(target, punishment.getType()).openMenu(event.getPlayer());

			event.setCancelled(true);
			event.getPlayer().sendMessage(CC.translate("&aSuccessfully removed the punishment for the reason '" + event.getMessage() + "'"));
		}
	}

}

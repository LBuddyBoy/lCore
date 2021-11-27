package me.lbuddyboy.core.punishment.listener;

import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.database.redis.packets.punishments.UnPunishPacket;
import me.lbuddyboy.core.profile.lProfile;
import me.lbuddyboy.core.punishment.Punishment;
import me.lbuddyboy.core.punishment.PunishmentType;
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
		lProfile profile = Core.getInstance().getProfileHandler().getByUUID(event.getPlayer().getUniqueId());
		if (reasons.contains(event.getPlayer())) {
			if (event.getMessage().equalsIgnoreCase("cancel")) {
				event.setCancelled(true);
				reasons.remove(event.getPlayer());
				pMap.remove(event.getPlayer());
				return;
			}
			lProfile target = Core.getInstance().getProfileHandler().getByUUID(pMap.get(event.getPlayer()).getTarget());
			Punishment punishment = pMap.get(event.getPlayer());

			punishment.setResolvedAt(System.currentTimeMillis());
			punishment.setResolvedBy(event.getPlayer().getUniqueId());
			punishment.setResolvedReason(event.getMessage());
			punishment.setResolved(true);

			target.getPunishments().removeIf(p -> p.getId().equals(punishment.getId()));
			target.getPunishments().add(punishment);

			target.save();

			new UnPunishPacket(punishment.getTarget(), punishment).send();

			pMap.remove(event.getPlayer());
			reasons.remove(event.getPlayer());

			new PunishmentsMenu(target, punishment.getType()).openMenu(event.getPlayer());

			event.setCancelled(true);
			event.getPlayer().sendMessage(CC.translate("&aSuccessfully removed the punishment for the reason '" + event.getMessage() + "'"));
			return;
		}
		if (profile.hasActivePunishment(PunishmentType.MUTE)) {
			Punishment punishment = profile.getActivePunishment(PunishmentType.MUTE);
			event.setCancelled(true);
			event.getPlayer().sendMessage(CC.translate(Configuration.MUTE_MESSAGE.getMessage()
					.replaceAll("%text%", punishment.getType().getBroadcastText())
					.replaceAll("%reason%", punishment.getReason())
					.replaceAll("%time%", punishment.getFormattedTimeLeft())
					.replaceAll("%temp-format%", Configuration.BAN_TEMPORARY_FORMAT.getMessage().replaceAll("%time%", punishment.getFormattedTimeLeft())
					)));
		}
	}
}

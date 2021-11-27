package me.lbuddyboy.core.profile;

import me.lbuddyboy.core.Configuration;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.punishment.Punishment;
import me.lbuddyboy.core.punishment.PunishmentType;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 23/09/2021 / 2:28 AM
 * LBuddyBoy Development / me.lbuddyboy.core.profile
 */
public class lProfileListener implements Listener {

	@EventHandler
	public void onAsyncPreLogin(AsyncPlayerPreLoginEvent event) {

		if (!Core.isLoaded()) {
			event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, CC.translate("&cThe server is still starting up."));
			return;
		}

		lProfile profile = new lProfile(event.getUniqueId());

		if (profile.hasActivePunishment(PunishmentType.BLACKLIST)) {
			Punishment punishment = profile.getActivePunishment(PunishmentType.BLACKLIST);
			event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, CC.translate(Configuration.BLACKLIST_KICK_MESSAGE.getMessage()
					.replaceAll("%reason%", punishment.getReason())
					.replaceAll("%time%", punishment.getFormattedTimeLeft())
					.replaceAll("%temp-format%", Configuration.BAN_TEMPORARY_FORMAT.getMessage().replaceAll("%time%", punishment.getFormattedTimeLeft())
					)));
			return;
		}
		if (profile.hasActivePunishment(PunishmentType.BAN)) {
			Punishment punishment = profile.getActivePunishment(PunishmentType.BAN);
			event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, CC.translate(Configuration.BAN_KICK_MESSAGE.getMessage()
					.replaceAll("%reason%", punishment.getReason())
					.replaceAll("%time%", punishment.getFormattedTimeLeft())
					.replaceAll("%temp-format%", Configuration.BAN_TEMPORARY_FORMAT.getMessage().replaceAll("%time%", punishment.getFormattedTimeLeft())
					)));
			return;
		}

		profile.setName(event.getName());
		profile.setCurrentIP(Configuration.PROFILE_HASHED_IPS.getBoolean() ? hashString(event.getAddress().getHostAddress()) : event.getAddress().getHostAddress());

		Core.getInstance().getProfileHandler().getProfiles().put(event.getUniqueId(), profile);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), () -> {
			lProfile profile = Core.getInstance().getProfileHandler().getProfiles().get(event.getPlayer().getUniqueId());

			profile.save();
		});
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), () -> {
			Core.getInstance().getProfileHandler().getProfiles().remove(event.getPlayer().getUniqueId());
		});
	}

	public String hashString(String toHash) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] hash = md.digest(toHash.getBytes(StandardCharsets.UTF_8));
			BigInteger number = new BigInteger(1, hash);
			StringBuilder hexString = new StringBuilder(number.toString(16));

			while (hexString.length() < 32) {
				hexString.insert(0, '0');
			}

			return hexString.toString();
		} catch (Exception var5) {
			var5.printStackTrace();
			return "null";
		}
	}

}

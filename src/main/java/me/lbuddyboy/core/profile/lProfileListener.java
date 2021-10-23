package me.lbuddyboy.core.profile;

import me.lbuddyboy.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
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

		lProfile profile = new lProfile(event.getUniqueId());
		profile.setName(event.getName());
		profile.setCurrentIP(hashString(event.getAddress().getHostAddress()));

		Core.getInstance().getProfileHandler().getProfiles().put(event.getUniqueId(), profile);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), () -> {
			lProfile profile = Core.getInstance().getProfileHandler().getProfiles().remove(event.getPlayer().getUniqueId());

			profile.save();
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

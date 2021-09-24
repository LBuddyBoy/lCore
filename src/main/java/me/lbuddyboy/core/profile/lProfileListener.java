package me.lbuddyboy.core.profile;

import me.lbuddyboy.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 23/09/2021 / 2:28 AM
 * LBuddyBoy Development / me.lbuddyboy.core.profile
 */
public class lProfileListener implements Listener {

	@EventHandler
	public void onAsyncPreLogin(AsyncPlayerPreLoginEvent event) {

		lProfile profile = new lProfile(event.getUniqueId());
		profile.load();
		profile.setName(event.getName());
		profile.setCurrentIP(event.getAddress().getHostAddress());

		profile.save();

		Core.getInstance().getProfileHandler().getProfiles().put(event.getUniqueId(), profile);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), () -> {
			lProfile profile = Core.getInstance().getProfileHandler().getProfiles().remove(event.getPlayer().getUniqueId());

			profile.save();
		});
	}

}

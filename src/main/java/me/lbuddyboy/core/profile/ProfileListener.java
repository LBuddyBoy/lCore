package me.lbuddyboy.core.profile;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 4:52 PM
 * lCore / me.lbuddyboy.core.profile
 */
public class ProfileListener implements Listener {

	@EventHandler
	public void onAsyncPlayerJoinEvent(AsyncPlayerPreLoginEvent event) {

		String ip = "omgcoolip";

		UUID uuid = event.getUniqueId();

		Profile profile = new Profile(uuid, event.getName(), ip);

		if (!profile.getKnownIPs().contains(ip)) {
			profile.getKnownIPs().add(ip);
		}

		Profile.profiles.add(profile);

		profile.save();

//		if (!profile.isLoaded()) {
//			event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
//			event.setKickMessage(Settings.FAILED_TO_LOAD_PROFILE.getMessage());
//		}

	}


}

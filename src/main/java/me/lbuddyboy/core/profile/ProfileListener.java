package me.lbuddyboy.core.profile;

import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.Settings;
import me.lbuddyboy.core.util.HashUtil;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 4:52 PM
 * lCore / me.lbuddyboy.core.profile
 */
public class ProfileListener implements Listener {

	@EventHandler
	public void onAsyncPlayerJoinEvent(AsyncPlayerPreLoginEvent event) {

		String ip = (Settings.PROFILE_HASHED_IPS.getBoolean() ? HashUtil.hash(event.getAddress().getHostAddress()) : event.getAddress().getHostAddress());

		UUID uuid = event.getUniqueId();

		Profile profile = new Profile(uuid, event.getName(), ip);

		if (!profile.getKnownIPs().contains(ip)) {
			profile.getKnownIPs().add(ip);
		}

		profile.recalculateGrants();

		if (Core.getInstance().getProfileHandler().getByUUID(uuid) == null) {
			Core.getInstance().getProfileHandler().getProfiles().add(profile);
		}

		profile.save();

//		if (!profile.isLoaded()) {
//			event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
//			event.setKickMessage(Settings.FAILED_TO_LOAD_PROFILE.getMessage());
//		}

	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Profile profile = Core.getInstance().getProfileHandler().getByUUID(event.getPlayer().getUniqueId());

		if (profile == null) {
			event.getPlayer().sendMessage(Settings.FAILED_TO_LOAD_PROFILE.getMessage());
			event.getPlayer().kickPlayer(Settings.FAILED_TO_LOAD_PROFILE.getMessage());
			return;
		}

		profile.setupPermissions();

		event.getPlayer().setDisplayName(CC.translate(profile.getDisplayName()));
	}

}

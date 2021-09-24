package me.lbuddyboy.core.profile;

import lombok.Getter;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.profile.grant.listener.GrantListener;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 23/09/2021 / 2:31 AM
 * LBuddyBoy Development / me.lbuddyboy.core.profile
 */

@Getter
public class lProfileHandler {

	private final Map<UUID, lProfile> profiles;

	public lProfileHandler() {
		profiles = new HashMap<>();
		Bukkit.getPluginManager().registerEvents(new lProfileListener(), Core.getInstance());
		Bukkit.getPluginManager().registerEvents(new GrantListener(), Core.getInstance());

		Bukkit.getScheduler().runTaskTimerAsynchronously(Core.getInstance(), () -> {
			for (lProfile profile : getProfiles().values()) {
				profile.calculateGrants();
			}
		}, 20 * 5, 20 * 5);
	}

	public lProfile getByUUID(UUID toLook) {
		lProfile profile = new lProfile(toLook);
		profile.load();
		if (!profile.isLoaded()) {
			return null;
		}
		return profile;
	}

}

package me.lbuddyboy.core.profile;

import lombok.Getter;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.profile.global.GlobalStatistic;
import me.lbuddyboy.core.profile.grant.listener.GrantListener;
import me.lbuddyboy.core.punishment.listener.PunishmentListener;
import org.bukkit.Bukkit;

import java.util.*;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 23/09/2021 / 2:31 AM
 * LBuddyBoy Development / me.lbuddyboy.core.profile
 */

@Getter
public class lProfileHandler {

	private final Map<UUID, lProfile> profiles;
	private final List<GlobalStatistic> globalStatistics;
	private List<UUID> checkedProfiles = new ArrayList<>();

	public lProfileHandler() {
		this.profiles = new HashMap<>();
		this.globalStatistics = new ArrayList<>();

		Bukkit.getPluginManager().registerEvents(new lProfileListener(), Core.getInstance());
		Bukkit.getPluginManager().registerEvents(new PunishmentListener(), Core.getInstance());
		Bukkit.getPluginManager().registerEvents(new GrantListener(), Core.getInstance());

		Bukkit.getScheduler().runTaskTimerAsynchronously(Core.getInstance(), () -> {
			checkedProfiles.clear();
			for (lProfile profile : getProfiles().values()) {
				if (checkedProfiles.contains(profile.getUniqueId())) continue;
				profile.calculateGrants();
				checkedProfiles.add(profile.getUniqueId());
			}
		}, 20 * 30, 20 * 30);
	}

	public lProfile getByUUID(UUID toLook) {
		lProfile profile = new lProfile(toLook);
		if (!profile.isLoaded()) {
			return null;
		}
		return profile;
	}

}

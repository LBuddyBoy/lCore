package me.lbuddyboy.core.util;

import me.lbuddyboy.core.Core;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 26/11/2021 / 3:09 PM
 * LBuddyBoy Development / me.lbuddyboy.core.util
 */
public class UUIDCache {

	public static String name(UUID uuid) {
		try {
			return Core.getInstance().getProfileHandler().getAllCachedPlayers().get(uuid);
		} catch (NullPointerException e) {
			return "Console";
		}
	}

	public static UUID uuid(String name) {
		for (Map.Entry<UUID, String> entry : Core.getInstance().getProfileHandler().getAllCachedPlayers().entrySet()) {
			if (Objects.equals(entry.getValue(), name)) {
				return entry.getKey();
			}
		}
		return null;
	}

}

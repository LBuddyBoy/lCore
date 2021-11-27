package me.lbuddyboy.core.database.redis.packets.rank;

import lombok.AllArgsConstructor;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.database.redis.JedisPacket;
import me.lbuddyboy.core.profile.lProfile;
import me.lbuddyboy.core.rank.Rank;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 5:59 PM
 * lCore / me.lbuddyboy.core.database.packets
 */

@AllArgsConstructor
public class RankRemovePermissionPacket implements JedisPacket {

	private final String name;
	private final String permission;

	@Override
	public void onReceive() {
		Rank rank = Core.getInstance().getRankHandler().getByName(name);
		rank.getPermissions().remove(permission);
		rank.save();

		for (lProfile profile : Core.getInstance().getProfileHandler().getProfiles().values()) {
			if (profile == null || !profile.isLoaded()) continue;
			if (profile.getCurrentRank().getName().equals(rank.getName())) {
				profile.setupPermissions();
			}
		}
	}

	@Override
	public String getID() {
		return "Rank Remove Perm";
	}
}

package me.lbuddyboy.core.database.packets.rank;

import lombok.AllArgsConstructor;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.profile.Profile;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.libraries.redis.JedisPacket;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 5:59 PM
 * lCore / me.lbuddyboy.core.database.packets
 */

@AllArgsConstructor
public class RankRemovePermissionPacket implements JedisPacket {

	private final Rank rank;
	private final String permission;

	@Override
	public void onReceive() {
		rank.getPermissions().remove(permission);

		for (Profile profile : Core.getInstance().getProfileHandler().getProfiles()) {
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

package me.lbuddyboy.core.database.redis.packets;

import lombok.AllArgsConstructor;
import me.lbuddyboy.core.database.redis.JedisPacket;
import me.lbuddyboy.core.profile.lProfile;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/11/2021 / 2:17 PM
 * LBuddyBoy Development / me.lbuddyboy.core.database.packets.global
 */

@AllArgsConstructor
public class UserRemovePermPacket implements JedisPacket {

	private lProfile profile;
	private String perm;

	@Override
	public void onReceive() {
		profile.getPermissions().remove(perm);
	}

	@Override
	public String getID() {
		return "User Addperm";
	}
}

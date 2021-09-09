package me.lbuddyboy.core.database.packets.rank;

import lombok.AllArgsConstructor;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.libraries.redis.JedisPacket;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 5:59 PM
 * lCore / me.lbuddyboy.core.database.packets
 */

@AllArgsConstructor
public class RankSetDisplayPacket implements JedisPacket {

	private final Rank rank;
	private final String newDisplay;

	@Override
	public void onReceive() {
		rank.setDisplayName(newDisplay);
	}

	@Override
	public String getID() {
		return "Rank Set DisplayName";
	}
}

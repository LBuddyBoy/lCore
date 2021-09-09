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
public class RankSetPrefixPacket implements JedisPacket {

	private final Rank rank;
	private final String newPrefix;

	@Override
	public void onReceive() {
		rank.setPrefix(newPrefix);
	}

	@Override
	public String getID() {
		return "Rank Set Prefix";
	}
}

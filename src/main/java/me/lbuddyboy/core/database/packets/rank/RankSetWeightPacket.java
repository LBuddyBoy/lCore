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
public class RankSetWeightPacket implements JedisPacket {

	private final Rank rank;
	private final int newWeight;

	@Override
	public void onReceive() {
		rank.setWeight(newWeight);
	}

	@Override
	public String getID() {
		return "Rank Set Weight";
	}
}

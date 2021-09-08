package me.lbuddyboy.core.database.packets;

import lombok.AllArgsConstructor;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.libraries.redis.JedisPacket;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 5:59 PM
 * lCore / me.lbuddyboy.core.database.packets
 */

@AllArgsConstructor
public class RankCreatePacket implements JedisPacket {

	private String name;

	@Override
	public void onReceive() {
		Rank rank = new Rank(this.name);
		Rank.getRanks().add(rank);
		rank.save();
	}

	@Override
	public String getID() {
		return "Rank Create";
	}
}

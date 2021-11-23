package me.lbuddyboy.core.database.redis.packets.rank;

import lombok.AllArgsConstructor;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.database.redis.JedisPacket;
import me.lbuddyboy.core.rank.Rank;

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
		Core.getInstance().getRankHandler().getRanks().add(rank);

	}

	@Override
	public String getID() {
		return "Rank Create";
	}
}

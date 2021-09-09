package me.lbuddyboy.core.database.packets.rank;

import lombok.AllArgsConstructor;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.libraries.redis.JedisPacket;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 5:59 PM
 * lCore / me.lbuddyboy.core.database.packets
 */

@AllArgsConstructor
public class RankDeletePacket implements JedisPacket {

	private final Rank rank;

	@Override
	public void onReceive() {
		Core.getInstance().getRankHandler().getRanks().remove(rank);
	}

	@Override
	public String getID() {
		return "Rank Delete";
	}
}

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
public class RankRenamePacket implements JedisPacket {

	private final String name;
	private final String newDisplay;

	@Override
	public void onReceive() {
		Rank rank = Core.getInstance().getRankHandler().getByName(name);
		rank.setName(newDisplay);
		rank.save();
	}

	@Override
	public String getID() {
		return "Rank Rename";
	}
}

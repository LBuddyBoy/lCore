package me.lbuddyboy.core.database.redis.packets.rank;

import lombok.AllArgsConstructor;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.database.redis.JedisPacket;
import me.lbuddyboy.core.rank.Rank;
import org.bukkit.ChatColor;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 5:59 PM
 * lCore / me.lbuddyboy.core.database.packets
 */

@AllArgsConstructor
public class RankSetColorPacket implements JedisPacket {

	private final String name;
	private final ChatColor newColor;

	@Override
	public void onReceive() {
		Rank rank = Core.getInstance().getRankHandler().getByName(name);
		rank.setColor(newColor);
		rank.save();
	}

	@Override
	public String getID() {
		return "Rank Set Color";
	}
}

package me.lbuddyboy.core.database.packets.rank;

import lombok.AllArgsConstructor;
import me.lbuddyboy.core.rank.Rank;
import me.lbuddyboy.libraries.redis.JedisPacket;
import org.bukkit.ChatColor;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 07/09/2021 / 5:59 PM
 * lCore / me.lbuddyboy.core.database.packets
 */

@AllArgsConstructor
public class RankSetColorPacket implements JedisPacket {

	private final Rank rank;
	private final ChatColor newColor;

	@Override
	public void onReceive() {
		rank.setColor(newColor);
	}

	@Override
	public String getID() {
		return "Rank Set Color";
	}
}

package me.lbuddyboy.core.database.redis.packets.rank;

import lombok.AllArgsConstructor;
import me.lbuddyboy.core.Core;
import me.lbuddyboy.core.database.redis.JedisPacket;
import me.lbuddyboy.core.rank.Rank;
import org.bukkit.configuration.file.YamlConfiguration;

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
		YamlConfiguration config = Core.getInstance().getRanksYML().gc();
		config.getConfigurationSection("ranks." + this.rank.getName()).getKeys(false).clear();
		config.set("ranks." + rank.getName(), null);
		try {
			Core.getInstance().getRanksYML().save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getID() {
		return "Rank Delete";
	}
}

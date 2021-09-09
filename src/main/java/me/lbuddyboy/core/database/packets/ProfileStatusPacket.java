package me.lbuddyboy.core.database.packets;

import lombok.Getter;
import lombok.Setter;
import me.lbuddyboy.core.Settings;
import me.lbuddyboy.libraries.redis.JedisPacket;
import org.bukkit.Bukkit;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 08/09/2021 / 12:22 PM
 * LBuddyBoy Development / me.lbuddyboy.core.database.packets
 */

@Getter
public class ProfileStatusPacket implements JedisPacket {

	private final String player;

	@Setter private boolean isOnline;
	@Setter private String server;

	public ProfileStatusPacket(String player) {
		this.player = player;
	}

	@Override
	public void onReceive() {
		setOnline((Bukkit.getPlayer(this.player) != null));
		setServer(Settings.SERVER_NAME.getMessage());
	}

	@Override
	public String getID() {
		return "Profile Status";
	}
}

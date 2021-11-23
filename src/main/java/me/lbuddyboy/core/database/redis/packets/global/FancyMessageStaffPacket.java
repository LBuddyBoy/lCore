package me.lbuddyboy.core.database.redis.packets.global;

import lombok.AllArgsConstructor;
import me.lbuddyboy.core.database.redis.JedisPacket;
import me.lbuddyboy.libraries.util.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 23/10/2021 / 1:11 PM
 * LBuddyBoy Development / me.lbuddyboy.core.database.packets.global
 */

@AllArgsConstructor
public class FancyMessageStaffPacket implements JedisPacket {

	private FancyMessage message;

	@Override
	public void onReceive() {
		for (Player staff : Bukkit.getOnlinePlayers()) {
			if (staff.hasPermission("lcore.staff") || staff.isOp()) {
				message.send(staff);
			}
		}
	}

	@Override
	public String getID() {
		return "Staff FancyMSG";
	}
}

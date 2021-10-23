package me.lbuddyboy.core.database.packets.global;

import lombok.AllArgsConstructor;
import me.lbuddyboy.libraries.redis.JedisPacket;
import me.lbuddyboy.libraries.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 23/10/2021 / 1:11 PM
 * LBuddyBoy Development / me.lbuddyboy.core.database.packets.global
 */

@AllArgsConstructor
public class MessageStaffPacket implements JedisPacket {

	private List<String> msg;

	@Override
	public void onReceive() {
		for (Player staff : Bukkit.getOnlinePlayers()) {
			if (staff.hasPermission("lcore.staff") || staff.isOp()) {
				for (String s : msg) {
					staff.sendMessage(CC.translate(s));
				}
			}
		}
	}

	@Override
	public String getID() {
		return "Staff MSG";
	}
}

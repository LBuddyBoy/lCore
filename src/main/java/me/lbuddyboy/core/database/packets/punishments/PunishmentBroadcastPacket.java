package me.lbuddyboy.core.database.packets.punishments;

import lombok.AllArgsConstructor;
import me.lbuddyboy.core.punishment.Punishment;
import me.lbuddyboy.libraries.redis.JedisPacket;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 08/09/2021 / 7:02 PM
 * LBuddyBoy Development / me.lbuddyboy.core.database.packets.punishments
 */

@AllArgsConstructor
public class PunishmentBroadcastPacket implements JedisPacket {

	private Punishment punishment;
	private String senderDisplay;
	private String targetDisplay;

	@Override
	public void onReceive() {
		punishment.alert(senderDisplay, targetDisplay);
	}

	@Override
	public String getID() {
		return "Punishment Broadcast";
	}
}
